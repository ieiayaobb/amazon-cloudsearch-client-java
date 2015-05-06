package aws.services.cloudsearchv2.search;

import java.util.List;

/**
 * Created by shenyineng on 15/4/15.
 */
public class Buckets {
    private List<FacetResult> facetResultList;

    public Buckets(List<FacetResult> facetResultList){
        this.facetResultList = facetResultList;
    }

    public int getValueCount(){
        return this.facetResultList.size();
    }

    public List<FacetResult> getValues(){
        return facetResultList;
    }

    @Override
    public String toString() {
        return "Buckets{" +
                "facetResultList=" + facetResultList +
                '}';
    }
}
