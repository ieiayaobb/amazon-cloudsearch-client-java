package aws.services.cloudsearchv2.search;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

/**
 * Created by shenyineng on 15/4/15.
 */
public class FacetResult {
    private String key;

    private int count;

    public FacetResult(JSONObject jsonObj) throws JSONException {
        this.key = jsonObj.getString("value");
        this.count = jsonObj.getInt("count");
    }

    public String getName(){
        return key;
    }

    public int getCount(){
        return count;
    }

    @Override
    public String toString() {
        return "FacetResult{" +
                "key='" + key + '\'' +
                ", count=" + count +
                '}';
    }
}
