package aws.services.cloudsearchv2.search;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

/**
 * Created by shenyineng on 15/4/15.
 */
public class FieldStatsInfo {
    private Double min;

    private Double max;

    private int count;

    private int missing;

    private int sum;

    private Double mean;

    public FieldStatsInfo(JSONObject jsonObj) throws JSONException {
        this.min = jsonObj.getDouble("min");
        this.max = jsonObj.getDouble("max");
        this.count = jsonObj.getInt("count");
        this.missing = jsonObj.getInt("missing");
        this.sum = jsonObj.getInt("sum");
        this.mean = jsonObj.getDouble("mean");
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }

    public int getMissing() {
        return missing;
    }

    public int getSum() {
        return sum;
    }

    public Double getMean() {
        return mean;
    }
}
