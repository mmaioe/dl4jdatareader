import mmaioe.com.dl4jdatasetiterator.idx.BettiNumberReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ito_m on 7/7/16.
 */
public class BettiNumberDump {
    /**
     * Config for system thread pool
     */
    private static int corePoolSize_system = 1000;
    private static int maximumPoolSize_system = 1000;
    private static int keepAliveTime_system = 10;
    private static int queueSize_system = 1000;

    /**
     * Thread pool for system related thread like error handling check thread, etc
     */
    private static ExecutorService executorService;

    private static String labelFile_test = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-labels-idx1-ubyte.gz";
    private static String attributeFIle_test = "/Users/maoito/myProject/tensorflow/MNIST_data/t10k-images-idx3-ubyte.gz";

    private static String labelFile_train = "/Users/maoito/myProject/tensorflow/MNIST_data/train-labels-idx1-ubyte.gz";
    private static String attributeFIle_train = "/Users/maoito/myProject/tensorflow/MNIST_data/train-images-idx3-ubyte.gz";

    private static List<List<List<Double>>> pointCloudArray = BettiNumberReader.readPointCloud(attributeFIle_train);

    public static void main(String[] args) throws Exception {


        abstract class convertToBettiThread implements Runnable{
            public int start;
            public int end;
            public convertToBettiThread(int start, int end){
                this.start = start;
                this.end = end;
            }
        }

        executorService = new ThreadPoolExecutor(corePoolSize_system, maximumPoolSize_system,
                keepAliveTime_system, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize_system));

        for(int i=0;i<600;i++) {
            System.out.println("submitt "+i);
            executorService.submit(
                    new convertToBettiThread(i*100, i*100+100) {
                        @Override
                        public void run() {
                            int max_dimension = 2;
                            for (int j = 2; j <= max_dimension; j++) {
                                try {
                                    String bettiFile_train = "/Users/maoito/myProject/tensorflow/MNIST_data/betti_train_dimension("+j+")_" + start + "_" + end + ".txt";

                                    BettiNumberReader.writeToPointCloud(pointCloudArray, (int)(Math.sqrt(28*28)),j, bettiFile_train, start, end);

                                    System.out.println("Finished "+bettiFile_train);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
            );
        }

//        System.out.println(Arrays.toString(new int[]{1,2}));
    }
}
