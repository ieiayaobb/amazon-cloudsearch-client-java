package aws.services.cloudsearchv2.search;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

/**
 * Created by shenyineng on 15/4/15.
 */
public class FieldStatsInfo {
    private String field;

    private Double min;

    private Double max;

    private int count;

    private int missing;

    private double sum;

    private Double mean;

    public FieldStatsInfo() {
    }

    public FieldStatsInfo(String field) {
        this.field = field;
    }

    public FieldStatsInfo(JSONObject jsonObj) throws JSONException {
        if(jsonObj.has("min")) {
            this.min = jsonObj.getDouble("min");
        }else{
            this.min = 0.0;
        }
        if(jsonObj.has("min")) {
            this.max = jsonObj.getDouble("max");
        }else{
            this.max = 0.0;
        }
        if(jsonObj.has("count")) {
            this.count = jsonObj.getInt("count");
        }else{
            this.count = 0;
        }
        if(jsonObj.has("missing")) {
            this.missing = jsonObj.getInt("missing");
        }else{
            this.missing = 0;
        }
        if(jsonObj.has("sum")) {
            this.sum = jsonObj.getDouble("sum");
        }else{
            this.sum = 0.0;
        }
        if(jsonObj.has("mean")) {
            this.mean = jsonObj.getDouble("mean");
        }else{
            this.mean = 0.0;
        }
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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

    public Double getSum() {
        return sum;
    }

    public Double getMean() {
        return mean;
    }

    @Override
    public String toString() {
        return "FieldStatsInfo{" +
                "field='" + field + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", count=" + count +
                ", missing=" + missing +
                ", sum=" + sum +
                ", mean=" + mean +
                '}';
    }
}
