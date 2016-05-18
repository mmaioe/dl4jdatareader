package mmaioe.com.dl4jdatasetiterator.idx;

import edu.stanford.nlp.io.IOUtils;
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ito_m on 5/18/16.
 */
public class IdxBaseDataFetcher extends BaseDataFetcher {
    private DataInputStream xStream;
    private DataInputStream yStream;
    private int xRows;
    private int xColumns;

    public IdxBaseDataFetcher(String attributeFile, String labelFile) throws IOException {
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
        this.inputColumns = xRows*xColumns;
        this.numOutcomes = 10;
    }

    public int[] toOneOfK(int label){
        int[] oneOfK = new int[10];
        oneOfK[label] = 1;
        for(int i=0;i<10;i++) oneOfK[i] = 1;
        return oneOfK;
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
                int[] oneOfK = toOneOfK(yStream.readUnsignedByte());
                INDArray feature = Nd4j.create(1, xRows * xColumns);
                INDArray label = Nd4j.create(1, 10);

                for (int j = 0; j < xRows * xColumns; j++) {
                    feature.putScalar(j,xStream.readUnsignedByte());
                }

                for(int j=0;j<oneOfK.length;j++){
                    label.putScalar(j,oneOfK[j]);
                }

                dataSet.add(
                        new DataSet(
                                feature, //
                                label //
                        )
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        initializeCurrFromList(dataSet);
        cursor += numExamples;
    }

    public void close() throws IOException {
        xStream.close();
        yStream.close();
    }
}
