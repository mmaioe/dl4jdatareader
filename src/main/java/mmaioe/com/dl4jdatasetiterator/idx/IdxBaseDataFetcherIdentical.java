package mmaioe.com.dl4jdatasetiterator.idx;

import edu.stanford.nlp.io.IOUtils;
import mmaioe.com.featureextraction.FeatureExtraction;
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.ArrayUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ito_m on 6/5/16.
 */
public class IdxBaseDataFetcherIdentical extends BaseDataFetcher {
    private String attributeFile;
    private String labelFile;
    private DataInputStream xStream;
    private DataInputStream yStream;
    private int xRows;
    private int xColumns;
    private FeatureExtraction featureExtraction = null;

    public IdxBaseDataFetcherIdentical(String attributeFile, String labelFile,int inputColumn, int numOutcomes,FeatureExtraction featureExtraction) throws IOException {
        init(attributeFile,labelFile,inputColumn,numOutcomes);
        this.featureExtraction = featureExtraction;
    }

    public void init(String attributeFile, String labelFile,int inputColumn, int numOutcomes) throws IOException{
        this.attributeFile = attributeFile;
        this.labelFile = labelFile;
        xStream = IOUtils.getDataInputStream(attributeFile);
        yStream = IOUtils.getDataInputStream(labelFile);

        int xMagic = xStream.readInt();
        if (xMagic != 2051) throw new RuntimeException("Bad format of xStream");
        int yMagic = yStream.readInt();
        if (yMagic != 2049) throw new RuntimeException("Bad format of yStream");
        int xNumImages = xStream.readInt();
        int yNumLabels = yStream.readInt();
        if (xNumImages != yNumLabels) throw new RuntimeException("x and y sizes don't match");
        System.out.println("Images and label file both contain " + xNumImages + " entries.");
        xRows = xStream.readInt();
        xColumns = xStream.readInt();

        this.totalExamples = yNumLabels;
        this.inputColumns = inputColumn;
        this.numOutcomes = numOutcomes;
    }

    public INDArray toOneOfK(int label){
        int[] nums = new int[numOutcomes];
        nums[label] = 1;
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
                INDArray feature = Nd4j.create(1, xRows * xColumns);

                for (int j = 0; j < xRows * xColumns; j++) {
                    feature.putScalar(j,xStream.readUnsignedByte()/255.0);
                }

                if(this.featureExtraction != null){
                    feature = this.featureExtraction.encode(feature);
                }

                dataSet.add(
                        new DataSet(
                                feature, //
//                                toOneOfK(yStream.readUnsignedByte()) //
                                feature
                        )
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        initializeCurrFromList(dataSet);
        cursor += numExamples;
    }

    @Override
    public void reset(){
        super.reset();
        try {
            this.close();
            this.init(attributeFile,labelFile,this.inputColumns,this.numOutcomes);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void close() throws IOException {
        xStream.close();
        yStream.close();
    }
}