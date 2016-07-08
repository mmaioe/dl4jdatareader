package mmaioe.com.dl4jdatasetiterator.idx;

import com.google.common.primitives.Doubles;
import edu.stanford.nlp.io.IOUtils;
import mmaioe.com.featureextraction.topology.PersistentHomology;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ito_m on 7/7/16.
 */
public class BettiNumberReader {
    public static void writeTo(String attributeFile, int dimension,String outputFile) throws IOException {
        DataInputStream xStream = IOUtils.getDataInputStream(attributeFile);

        int xMagic = xStream.readInt();
        if (xMagic != 2051) throw new RuntimeException("Bad format of xStream");
        int xNumImages = xStream.readInt();
        System.out.println("Images and label file both contain " + xNumImages + " entries.");
        int xRows = xStream.readInt();
        int xColumns = xStream.readInt();

        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");


        System.out.println(" convert to num of Images, "+xNumImages);
        for (int i = 0; i < xNumImages; i++) {

            System.out.println(" convert "+i+" th image now...");
            List<List<Double>> pointCloud = new ArrayList<List<Double>>();

            int x = 0;
            int y = 0;
            for (int j = 0; j < xRows * xColumns; j++) {
                int byteValue = xStream.readUnsignedByte();
                if (byteValue > 0) {
                    pointCloud.add(Doubles.asList(new double[]{x, y}));
                }

                x++;
                if (j % xColumns == 0) {
                    x = 0;
                    y++;
                }
            }


            PersistentHomology homology = new PersistentHomology(pointCloud);
            int[] bettiNumbers = homology.getBettiNumbers(dimension);


            StringBuilder builder = new StringBuilder();
            for(int j=0;j<bettiNumbers.length-1;j++){
                builder.append(bettiNumbers[j]+",");
            }
            builder.append(bettiNumbers[bettiNumbers.length-1]);
            writer.println(builder.toString());
        }

        writer.close();
        xStream.close();
    }
}
