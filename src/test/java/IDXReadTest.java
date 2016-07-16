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

        for(int i=0;i<600;i++) {
            System.out.println(i*100+","+(i*100+100));
        }
//
//        String labelFile_test = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-labels-idx1-ubyte.gz";
//        String attributeFIle_test = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-images-idx3-ubyte.gz";
//        String bettiDataFile_test = "/Users/maoito/myProject/tensorflow/MNIST_data/betti_test_2.txt";
//
//        String labelFile_train = "/Users/maoito/myProject/tensorflow/MNIST_data/train-labels-idx1-ubyte.gz";
//        String attributeFIle_train = "/Users/maoito/myProject/tensorflow/MNIST_data/train-images-idx3-ubyte.gz";
//        String bettiDataFile_train = "/Users/maoito/myProject/tensorflow/MNIST_data/betti_train_2_2.txt";
//
//        try{
//            int count = 0;
//            File file = new File(bettiDataFile_train);
//            BufferedReader br = new BufferedReader(new FileReader(file));
//
//            String str;
//            while((str = br.readLine()) != null){
//                System.out.println(str.split(","));
//                count ++;
//            }
//
//            System.out.println("count="+count);
//            br.close();
//        }catch(FileNotFoundException e){
//            System.out.println(e);
//        }catch(IOException e){
//            System.out.println(e);
//        }

    }
}
