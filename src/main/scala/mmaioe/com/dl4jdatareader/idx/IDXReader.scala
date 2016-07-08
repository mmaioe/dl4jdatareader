package mmaioe.com.dl4jdatareader.idx

import mmaioe.com.dl4jdatasetiterator.idx.{IdxBaseDataFetcherIdentical, IdxBaseDatasetIterator, IdxBaseDataFetcher}
import mmaioe.com.featureextraction.FeatureExtraction
import org.deeplearning4j.datasets.iterator.DataSetIterator

/**
 * Created by ito_m on 5/13/16.
 */
object IDXReader {

  def read(attributeFile: String, labelFile: String, batch: Int, numOfExamples: Int,inputColumn: Int, numOutcomes: Int, featureExtraction:FeatureExtraction, includeBetti:Boolean,bettiFile: String) : DataSetIterator={
    val fetcher: IdxBaseDataFetcher = new IdxBaseDataFetcher(attributeFile,labelFile, inputColumn, numOutcomes,featureExtraction,includeBetti,bettiFile)

    return new IdxBaseDatasetIterator(batch, numOfExamples, fetcher)
  }

  def read(attributeFile: String, labelFile: String, batch: Int, inputColumn: Int, numOutcomes: Int, featureExtraction:FeatureExtraction, includeBetti:Boolean,bettiFile: String) : DataSetIterator={
    val fetcher: IdxBaseDataFetcher = new IdxBaseDataFetcher(attributeFile,labelFile,inputColumn,numOutcomes,featureExtraction,includeBetti,bettiFile)

    return new IdxBaseDatasetIterator(batch, fetcher.totalExamples(), fetcher)
  }

  def readIdentical(attributeFile: String, labelFile: String, batch: Int, numOfExamples: Int,inputColumn: Int, numOutcomes: Int, featureExtraction:FeatureExtraction, includeBetti:Boolean, bettiFile: String) : DataSetIterator={
    val fetcher: IdxBaseDataFetcherIdentical = new IdxBaseDataFetcherIdentical(attributeFile,labelFile, inputColumn, numOutcomes,featureExtraction, includeBetti,bettiFile)

    return new IdxBaseDatasetIterator(batch, numOfExamples, fetcher)
  }

  def readIdentical(attributeFile: String, labelFile: String, batch: Int, inputColumn: Int, numOutcomes: Int, featureExtraction:FeatureExtraction, includeBetti:Boolean,bettiFile: String) : DataSetIterator={
    val fetcher: IdxBaseDataFetcherIdentical = new IdxBaseDataFetcherIdentical(attributeFile,labelFile,inputColumn,numOutcomes,featureExtraction, includeBetti,bettiFile)

    return new IdxBaseDatasetIterator(batch, fetcher.totalExamples(), fetcher)
  }
}
