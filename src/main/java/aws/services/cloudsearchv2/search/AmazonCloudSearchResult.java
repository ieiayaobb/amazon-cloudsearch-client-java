package aws.services.cloudsearchv2.search;

import java.util.List;
import java.util.Map;

/**
 * Result of a query executed on Amazon Cloud Search.
 * 
 * @author Tahseen Ur Rehman Fida
 * @email tahseen.ur.rehman@gmail.com
 *
 */
public class AmazonCloudSearchResult {
	public String rid;
	
	public long time;
	
	public int found;
	
	public int start;
	
	public List<Hit> hits;

    public Map<String, Buckets> facets;

    public Map<String, FieldStatsInfo> fieldStatsInfoMap;

    public Map<String, List<PivotField>> pivotMap;

    public List<Hit> getResults(){
        return hits;
    }

    //TODO : implements
    public Map<String, List<PivotField>> getFacetPivot(){
        return pivotMap;
    }

    //TODO : implements
    public Map<String, FieldStatsInfo> getFieldStatsInfo(){
        return fieldStatsInfoMap;
    }

    //TODO : implements
    public Buckets getFacetField(String key){
        return facets.get(key);
    }

    @Override
    public String toString() {
        return "AmazonCloudSearchResult{" +
                "rid='" + rid + '\'' +
                ", time=" + time +
                ", found=" + found +
                ", start=" + start +
                ", hits=" + hits +
                '}';
    }
}
