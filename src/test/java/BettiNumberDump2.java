import mmaioe.com.dl4jdatasetiterator.idx.BettiNumberReader;

import java.util.Arrays;

/**
 * Created by ito_m on 7/7/16.
 */
public class BettiNumberDump2 {
    public static void main(String[] args) throws Exception {

        String labelFile_test = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-labels-idx1-ubyte.gz";
        String attributeFIle_test = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-images-idx3-ubyte.gz";

        String labelFile_train = "/Users/maoito/myProject/tensorflow/MNIST_data/train-labels-idx1-ubyte.gz";
        String attributeFIle_train = "/Users/maoito/myProject/tensorflow/MNIST_data/train-images-idx3-ubyte.gz";

        int max_dimension = 3;
        for(int i=2;i<=max_dimension;i++) {
            String bettiFile_train = "/Users/maoito/myProject/tensorflow/MNIST_data/betti_train_"+i+"_2.txt";
            BettiNumberReader.writeTo(attributeFIle_train,  (int)(Math.sqrt(28*28)),i, bettiFile_train,0,100);
        }

//        System.out.println(Arrays.toString(new int[]{1,2}));
    }
}
