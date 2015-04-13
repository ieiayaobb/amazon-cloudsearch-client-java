package aws.services.cloudsearchv2;

import aws.services.cloudsearchv2.documents.AmazonCloudSearchAddRequest;
import aws.services.cloudsearchv2.documents.AmazonCloudSearchDeleteRequest;
import aws.services.cloudsearchv2.search.AmazonCloudSearchQuery;
import aws.services.cloudsearchv2.search.AmazonCloudSearchResult;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.json.JSONException;

/**
 * Created by shenyineng on 15/4/13.
 */
public class CloudSearchClient {
    public static void main(String[] args) throws AmazonCloudSearchRequestException, JSONException, AmazonCloudSearchInternalServerException {
        AmazonCloudSearchClient client = new AmazonCloudSearchClient();
        String searchEndpoint = "search-test-ranking-f5jcbvwefmvzezpvph6fprdcde.ap-northeast-1.cloudsearch.amazonaws.com";
        String documentEndpoint = "doc-test-ranking-f5jcbvwefmvzezpvph6fprdcde.ap-northeast-1.cloudsearch.amazonaws.com";
        client.setSearchEndpoint(searchEndpoint);
        client.setDocumentEndpoint(documentEndpoint);

        AmazonCloudSearchDeleteRequest deleteRequest = new AmazonCloudSearchDeleteRequest();
        deleteRequest.id = "ecfb4a29-bd61-426d-8b8c-54b7a51d1dd4";
        deleteRequest.version = 1;
        client.deleteDocument(deleteRequest);

//        System.out.println(client.findAllDocuments());
        client.deleteAllDocuments();

        AmazonCloudSearchAddRequest amazonCloudSearchAddRequest = new AmazonCloudSearchAddRequest();
        amazonCloudSearchAddRequest.id = "new";
        amazonCloudSearchAddRequest.addField("an_city", "test");
        client.addDocument(amazonCloudSearchAddRequest);

        AmazonCloudSearchQuery query = new AmazonCloudSearchQuery();
        query.query = "苏州";
        query.queryParser = "simple";
        query.start = 0;
//        query.size = 16;
//        query.setDefaultOperator("or");
//        query.setFields("sku_no^11", "title^10", "description^9", "features^8", "specification^8", "categories^7");
//        query.addExpression("sort_expr", "(0.3*popularity)+(0.7*_score)");
//        query.addSort("sort_expr", "desc");

        AmazonCloudSearchResult result = client.search(query);
        System.out.println(result);
    }
}
