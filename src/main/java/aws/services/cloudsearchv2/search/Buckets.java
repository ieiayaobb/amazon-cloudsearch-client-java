package aws.services.cloudsearchv2.search;

import java.util.List;

/**
 * Created by shenyineng on 15/4/15.
 */
public class Buckets {
    private List<FacetResult> facetResultList;

    public int getValueCount(){
        return 0;
    }

    public List<FacetResult> getValues(){
        return facetResultList;
    }
}
