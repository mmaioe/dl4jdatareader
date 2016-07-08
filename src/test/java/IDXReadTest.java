import mmaioe.com.dl4jdatasetiterator.idx.MNISTConverter;

import java.io.*;

/**
 * Created by ito_m on 5/17/16.
 */
public class IDXReadTest {
    public static void main(String[] args) throws Exception {
//        String labelFile = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-labels-idx1-ubyte.gz";
//        String attributeFIle = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-images-idx3-ubyte.gz";
//        MNISTConverter.read(attributeFIle, labelFile);


        String labelFile_test = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-labels-idx1-ubyte.gz";
        String attributeFIle_test = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-images-idx3-ubyte.gz";
        String bettiDataFile_test = "/Users/maoito/myProject/tensorflow/MNIST_data/betti_test2_f.txt";

        String labelFile_train = "/Users/maoito/myProject/tensorflow/MNIST_data/train-labels-idx1-ubyte.gz";
        String attributeFIle_train = "/Users/maoito/myProject/tensorflow/MNIST_data/train-images-idx3-ubyte.gz";
        String bettiDataFile_train = "/Users/maoito/myProject/tensorflow/MNIST_data/betti_train_2_f.txt";

        try{
            File file = new File(bettiDataFile_train);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String str;
            while((str = br.readLine()) != null){
                System.out.println(str.split(","));
            }

            br.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }

    }
}
