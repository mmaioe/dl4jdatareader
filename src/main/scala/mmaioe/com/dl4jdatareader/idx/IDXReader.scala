package mmaioe.com.dl4jdatareader.idx

import mmaioe.com.dl4jdatasetiterator.idx.{IdxBaseDatasetIterator, IdxBaseDataFetcher}
import org.deeplearning4j.datasets.iterator.DataSetIterator

/**
 * Created by ito_m on 5/13/16.
 */
object IDXReader {

  def read(attributeFile: String, labelFile: String) : DataSetIterator={
    val fetcher: IdxBaseDataFetcher = new IdxBaseDataFetcher(attributeFile,labelFile)

    return new IdxBaseDatasetIterator(1, fetcher.totalExamples(), fetcher)
  }

  def read(attributeFile: String, labelFile: String, batch: Int, numOfExamples: Int) : DataSetIterator={
    val fetcher: IdxBaseDataFetcher = new IdxBaseDataFetcher(attributeFile,labelFile)

    return new IdxBaseDatasetIterator(batch, numOfExamples, fetcher)
  }
}
