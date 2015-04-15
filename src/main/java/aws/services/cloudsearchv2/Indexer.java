package aws.services.cloudsearchv2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import aws.services.cloudsearchv2.documents.AmazonCloudSearchAddRequest;

import com.amazonaws.util.json.JSONException;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * Created by shenyineng on 15/4/13.
 */
public class Indexer {
    private AmazonCloudSearchClient client;
    private BlockingQueue<List<DBObject>> dbObjectListQueue = new LinkedBlockingQueue(10000);
    private CountDownLatch lineCountDownLatch;
    private DBObject fields = new BasicDBObject("pn", 1).append("personAn.original.name", 1).append("personAn.original.address.city", 1).append("analyzed_data.claim_count", 1).append("pbdt", 1);

    public Indexer() throws UnknownHostException, InterruptedException, AmazonCloudSearchRequestException, JSONException, AmazonCloudSearchInternalServerException {
        client = new aws.services.cloudsearchv2.AmazonCloudSearchClient();
        Long start = System.currentTimeMillis();
        String searchEndpoint = "query-test-ranking-f5jcbvwefmvzezpvph6fprdcde.ap-northeast-1.cloudsearch.amazonaws.com";
        String documentEndpoint = "doc-test-ranking-f5jcbvwefmvzezpvph6fprdcde.ap-northeast-1.cloudsearch.amazonaws.com";
        client.setSearchEndpoint(searchEndpoint);
        client.setDocumentEndpoint(documentEndpoint);

//        client.deleteAllDocuments();

        int processors = Runtime.getRuntime().availableProcessors();
        if (processors < 8) {
            processors = 8;
        }
        System.out.println("processors : " + processors);
        ExecutorService pool = Executors.newFixedThreadPool(processors);

        lineCountDownLatch = new CountDownLatch(1);
        new BatchPackageThread().start();

        for (int i = 0; i < processors; i++) {
            pool.execute(new InnerIndexThread());
        }
        pool.awaitTermination(12000, TimeUnit.SECONDS);
    }

	public static void main(String[] args) throws UnknownHostException, AmazonCloudSearchRequestException, JSONException, AmazonCloudSearchInternalServerException, ParseException, InterruptedException {
        new Indexer();
	}

    class BatchPackageThread extends Thread{
        @Override
        public void run() {
            calculateFromFile();
        }
        private void calculateFromFile(){
            try {
                List<DBObject> dbObjectListFromFile = this.readDBObjectFromFile();
                Iterator<DBObject> dbObjectIterator = dbObjectListFromFile.iterator();
                List<DBObject> dbObjectList = new ArrayList();
                while (dbObjectIterator.hasNext()) {
                    dbObjectList.add(dbObjectIterator.next());
                    if (dbObjectList.size() == 200) {
                        try {
                            dbObjectListQueue.put(dbObjectList);
                        } catch (InterruptedException e) {
                            System.out.println("ObjectId list queue error!");
                        }
                        dbObjectList = new ArrayList();
                    }
                }
                if (!dbObjectList.isEmpty()) {
                    try {
                        dbObjectListQueue.put(dbObjectList);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lineCountDownLatch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        private void calculateFromMongoDb(){
//            DBCursor cursor = PatentMongoDBDatabaseSingleton.getInstance().getPatentCollection().find(null, fields).limit(1000).batchSize(200);
//            List<DBObject> dbObjectList = new ArrayList<>();
//            while (cursor.hasNext()) {
//                dbObjectList.add(cursor.next());
//                if (dbObjectList.size() == 200) {
//                    try {
//                        dbObjectListQueue.put(dbObjectList);
//                    } catch (InterruptedException e) {
//                        LOG.error("ObjectId list queue error!", e);
//                    }
//                    dbObjectList = new ArrayList<>();
//                }
//            }
//            cursor.close();
//            LOG.info("-------Cursor closed-------");
//            if (!dbObjectList.isEmpty()) {
//                try {
//                    dbObjectListQueue.put(dbObjectList);
//                } catch (InterruptedException e) {
//                    LOG.error("ObjectId list queue error!", e);
//                }
//            }
//            lineCountDownLatch.countDown();
//        }

        private List<DBObject> readDBObjectFromFile() throws IOException {
            Charset charset = Charset.forName("UTF-8");
            Path path = FileSystems.getDefault().getPath("test.json");
            List<String> allLines = Files.readAllLines(path, charset);
            List<DBObject> allDBObject = new LinkedList();
            for (String line : allLines) {
                allDBObject.add((DBObject) JSON.parse(line));
            }
            return allDBObject;
        }
    }

    class InnerIndexThread extends Thread{
        @Override
        public void run() {
            while (true) {
                if (dbObjectListQueue.isEmpty() && lineCountDownLatch.getCount() == 0) {
                    break;
                }
                List<DBObject> dbObjectList = null;
                try {
                    dbObjectList = dbObjectListQueue.poll(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (dbObjectList == null) {
                    if (lineCountDownLatch.getCount() == 0) {
                        break;
                    } else {
                        continue;
                    }
                }

                file = new File("test.json");
                List<AmazonCloudSearchAddRequest> documents = new LinkedList<AmazonCloudSearchAddRequest>();
                try{
                    for (DBObject eachObject : dbObjectList) {
                        BasicDBObject dbObject = (BasicDBObject) eachObject;
//                        exportToFile(dbObject);
                        documents.add(generateAddRequest(dbObject));
                    }
                    client.addDocuments(documents);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private File file;
        private synchronized void exportToFile(DBObject dbObject){
            FileOutputStream fos = null;
            FileChannel fc_out = null;
            try {
                String each = dbObject.toString() + "\n";
                fos = new FileOutputStream(file, true);
                fc_out = fos.getChannel();
                ByteBuffer buf = ByteBuffer.wrap(each.getBytes());
                buf.put(each.getBytes());
                buf.flip();
                fc_out.write(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private AmazonCloudSearchAddRequest generateAddRequest(DBObject dbObject) throws AmazonCloudSearchRequestException, JSONException, AmazonCloudSearchInternalServerException {
            String pn = (String) dbObject.get("pn");
            List<String> assignees = new LinkedList();
            List<String> cityList = new LinkedList();
            if (dbObject.get("personAn") != null) {
                BasicDBList originalList = (BasicDBList) ((BasicDBObject) dbObject
                        .get("personAn")).get("original");
                if (originalList != null) {
                    for (Object obj : originalList) {
                        String name = (String) ((BasicDBObject) obj).get("name");
                        assignees.add(name);
                        if (((BasicDBObject) obj).get("address") != null
                                && ((BasicDBObject) ((BasicDBObject) obj)
                                .get("address")).get("city") != null) {
                            cityList
                                    .add((String) ((BasicDBObject) ((BasicDBObject) obj)
                                            .get("address")).get("city"));
                        }
                    }
                }
            }

            Date pbdt = (Date) dbObject.get("pbdt");

            int claimsCount = 0;
            if (dbObject.get("analyzed_data") != null) {
                BasicDBObject analyzed_dataObj = (BasicDBObject) dbObject
                        .get("analyzed_data");
                if (analyzed_dataObj.containsField("claim_count")) {
                    claimsCount = analyzed_dataObj.getInt("claim_count");
                } else {
                    claimsCount = 0;
                }
            }

            AmazonCloudSearchAddRequest amazonCloudSearchAddRequest = new AmazonCloudSearchAddRequest();
            amazonCloudSearchAddRequest.id = String.valueOf(UUID.randomUUID());
            //            amazonCloudSearchAddRequest.addField("an_city", cityList);
            amazonCloudSearchAddRequest.addField("ans_facet", assignees);
            amazonCloudSearchAddRequest.addField("claim_count", claimsCount);
            DateFormat outDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd'T'hh:mm:ss'Z'");
            amazonCloudSearchAddRequest.addField("pbdt", outDateFormat
                    .format(pbdt));
            amazonCloudSearchAddRequest.addField("pn", pn);
            return amazonCloudSearchAddRequest;
//            client.addDocument(amazonCloudSearchAddRequest);
        }
    }
}
