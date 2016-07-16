package mmaioe.com.dl4jdatasetiterator.sift;

import com.google.common.primitives.Doubles;
import edu.stanford.nlp.io.IOUtils;
import mmaioe.com.featureextraction.FeatureExtraction;
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.ArrayUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ito_m on 7/9/16.
 */
public class SiftBaseDataFetcher extends BaseDataFetcher {

    private Map<String,SiftFeature> siftMap;
    private Map<String,Integer> targetMap;
    private List<String> featureKeyFilePath;

    public SiftBaseDataFetcher(Map<String,SiftFeature> siftMap,Map<String, Integer> targetMap,List<String> featureKeyFilePath, int numOutcomes) throws IOException {
        this.siftMap = siftMap;
        this.featureKeyFilePath = featureKeyFilePath;
        this.targetMap = targetMap;

        this.totalExamples = featureKeyFilePath.size();
        this.inputColumns = 128;
        this.numOutcomes = numOutcomes;
    }

    public INDArray toOneOfK(int label){
        int[] nums = new int[numOutcomes];
        nums[label] = 1;
        System.out.print(" to One Of K : ");

        for(int i=0;i<nums.length;i++){
         System.out.print(nums[i]+",");
        }

        System.out.println();

        return ArrayUtil.toNDArray(nums);
    }

    @Override
    public void fetch(int numExamples) {
        int from = cursor;
        int to = cursor + numExamples;
        if(to > totalExamples)
            to = totalExamples;

        List<DataSet> dataSet = new ArrayList<DataSet>();

        try {
            for (int i = from; i < to; i++) {

                SiftFeature siftFeature = siftMap.get(featureKeyFilePath.get(i));

                INDArray feature = Nd4j.create(1, 128);

                for(int k=0;k<siftFeature.invariantFeature.length;k++){
                    feature.putScalar(k,siftFeature.invariantFeature[k]);
                }

                System.out.println("Training: " + targetMap.get(featureKeyFilePath.get(i)));
                        dataSet.add(
                                new DataSet(
                                        feature, //
                                        toOneOfK(targetMap.get(featureKeyFilePath.get(i))) //
                                )
                );
//

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        initializeCurrFromList(dataSet);
        cursor += numExamples;
    }


}