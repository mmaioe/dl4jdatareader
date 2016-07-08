package mmaioe.com.dl4jdatasetiterator.idx;

import com.google.common.primitives.Doubles;
import edu.stanford.nlp.io.IOUtils;
import mmaioe.com.featureextraction.FeatureExtraction;
import mmaioe.com.featureextraction.topology.PersistentHomology;
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.ArrayUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ito_m on 5/18/16.
 */
public class IdxBaseDataFetcher extends BaseDataFetcher {
    private String attributeFile;
    private String labelFile;
    private DataInputStream xStream;
    private DataInputStream yStream;
    private int xRows;
    private int xColumns;
    private FeatureExtraction featureExtraction = null;
    private boolean includeBetti;
    private String bettiFile;
    BufferedReader bettiReader;
    public IdxBaseDataFetcher(String attributeFile, String labelFile,int inputColumn, int numOutcomes,FeatureExtraction featureExtraction, boolean includeBetti, String bettiFile) throws IOException {
        init(attributeFile,labelFile,inputColumn,numOutcomes, includeBetti,bettiFile);
        this.featureExtraction = featureExtraction;
    }

    public void init(String attributeFile, String labelFile,int inputColumn, int numOutcomes, boolean includeBetti, String bettiFile) throws IOException{
        this.attributeFile = attributeFile;
        this.labelFile = labelFile;
        this.bettiFile = bettiFile;
        xStream = IOUtils.getDataInputStream(attributeFile);
        yStream = IOUtils.getDataInputStream(labelFile);
        File file = new File(this.bettiFile);
        bettiReader = new BufferedReader(new FileReader(file));

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
        this.includeBetti = includeBetti;
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

                List<List<Double>> pointCloud = new ArrayList<List<Double>>();
                int x = 0;
                int y = 0;
                for (int j = 0; j < xRows * xColumns; j++) {
                    int byteValue = xStream.readUnsignedByte();
                    if(byteValue > 0){
                        pointCloud.add(Doubles.asList(new double[]{x, y}));
                    }

                    feature.putScalar(j,byteValue/255.0);

                    x++;
                    if(j % xColumns == 0){
                        x = 0;
                        y++;
                    }
                }

                INDArray newFeature = feature;
                if(includeBetti) {
                    int dimension = 2;
                    newFeature = Nd4j.create(1, xRows * xColumns + dimension);
                    for (int j = 0; j < xRows * xColumns; j++) {
                        newFeature.putScalar(j, feature.getDouble(j));
                    }

                    String[] bettiNumbers = bettiReader.readLine().split(",");

                    System.out.println("Betti numbers:" + bettiNumbers.length + " shape:" + (xRows * xColumns) + " , dimension:" + dimension);
                    for (int j = 0; j < dimension; j++) {
                        if (j < bettiNumbers.length) {
                            System.out.println(" [" + j + "]=" + Double.parseDouble(bettiNumbers[j]) / (double) (xRows * xColumns));
                            newFeature.putScalar(xRows * xColumns + j, Double.parseDouble(bettiNumbers[j]) / (double) (xRows * xColumns));
                        } else {
                            newFeature.putScalar(xRows * xColumns + j, 0);
                        }
                    }
                }

                if(this.featureExtraction != null){
                    feature = this.featureExtraction.encode(newFeature);
                }

                System.out.println(" print out "+xRows*xColumns+" vs "+feature.length()+" , "+this.inputColumns);
                dataSet.add(
                        new DataSet(
                                feature, //
                                toOneOfK(yStream.readUnsignedByte()) //
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
            this.init(attributeFile,labelFile,this.inputColumns,this.numOutcomes,this.includeBetti,this.bettiFile);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void close() throws IOException {
        xStream.close();
        yStream.close();
        bettiReader.close();
    }
}
