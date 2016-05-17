package mmaioe.com.dl4jdatasetiterator.idx;

import edu.stanford.nlp.io.IOUtils;

import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * https://github.com/stanfordnlp/CoreNLP/blob/master/src/edu/stanford/nlp/classify/demo/MnistConverter.java
 *
 * Created by ito_m on 5/17/16.
 */
public class MINISTConverter {
    public static void read(String attributeFile, String labelFile) throws Exception{
        DataInputStream xStream = IOUtils.getDataInputStream(attributeFile);
        DataInputStream yStream = IOUtils.getDataInputStream(labelFile);

        int xMagic = xStream.readInt();
        if (xMagic != 2051) throw new RuntimeException("Bad format of xStream");
        int yMagic = yStream.readInt();
        if (yMagic != 2049) throw new RuntimeException("Bad format of yStream");
        int xNumImages = xStream.readInt();
        int yNumLabels = yStream.readInt();
        if (xNumImages != yNumLabels) throw new RuntimeException("x and y sizes don't match");
        System.out.println("Images and label file both contain " + xNumImages + " entries.");
        int xRows = xStream.readInt();
        int xColumns = xStream.readInt();
        for (int i = 0; i < xNumImages || i<10; i++) {
            int label = yStream.readUnsignedByte();
            int[] matrix = new int[xRows*xColumns];
            for (int j = 0; j < xRows*xColumns; j++) {
                matrix[j] = xStream.readUnsignedByte();
            }
            System.out.println("label:"+label);
        }
        xStream.close();
        yStream.close();

    }
}
