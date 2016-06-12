package mmaioe.com.dl4jdatasetiterator.idx;

import org.deeplearning4j.datasets.fetchers.BaseDataFetcher;
import org.deeplearning4j.datasets.iterator.BaseDatasetIterator;

/**
 * Created by ito_m on 5/18/16.
 */
public class IdxBaseDatasetIterator extends BaseDatasetIterator {
    public IdxBaseDatasetIterator(int batch, int numExamples, BaseDataFetcher fetcher) {
         super(batch,numExamples,fetcher);
    }
}