import com.google.common.io.ByteStreams;
import mmaioe.com.dl4jdatasetiterator.idx.MINISTConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ito_m on 5/17/16.
 */
public class IDXReadTest {
    public static void main(String[] args) throws Exception {
        String labelFile = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-labels-idx1-ubyte.gz";
        String attributeFIle = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-images-idx3-ubyte.gz";
        MINISTConverter.read(attributeFIle,labelFile);
    }
}
