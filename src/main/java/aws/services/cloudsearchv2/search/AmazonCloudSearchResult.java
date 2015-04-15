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
	private String rid;

    private long time;

    private int found;

    private int start;

    private List<Hit> hits;

    private Map<String, Buckets> facets;

    private Map<String, FieldStatsInfo> stats;

    private Map<String, List<PivotField>> pivotMap;

    public String getRid() {
        return rid;
    }

    public long getTime() {
        return time;
    }

    public int getFound() {
        return found;
    }

    public int getStart() {
        return start;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public Map<String, Buckets> getFacets() {
        return facets;
    }

    public List<Hit> getResults(){
        return hits;
    }

    //TODO : implements
    public Map<String, List<PivotField>> getFacetPivot(){
        return pivotMap;
    }

    //TODO : implements
    public Map<String, FieldStatsInfo> getFieldStatsInfo(){
        return stats;
    }

    //TODO : implements
    public Buckets getFacetField(String key){
        return facets.get(key);
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setFound(int found) {
        this.found = found;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public void setFacets(Map<String, Buckets> facets) {
        this.facets = facets;
    }

    public void setStats(Map<String, FieldStatsInfo> stats) {
        this.stats = stats;
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
