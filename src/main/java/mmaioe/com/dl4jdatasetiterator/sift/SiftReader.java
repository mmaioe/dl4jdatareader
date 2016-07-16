package mmaioe.com.dl4jdatasetiterator.sift;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

/**
 * Created by ito_m on 7/9/16.
 */
public class SiftReader {
    public static String[][] splitIntoFolds(String test_dir, int fold, String[] category){
        String[][] all = new String[category.length][];
        boolean[][] mark = new boolean[category.length][];
        int[] numOfImage = new int[category.length];
        int[] numOfLastFold = new int[category.length];
        int sumOfNotLastFold = 0;
        int sumOfLastFold = 0;
        for(int i=0;i<category.length;i++){
            all[i] = getAllImages(test_dir,category[i]);
            mark[i] = new boolean[all[i].length];
            numOfImage[i] = all[i].length/fold;
            sumOfNotLastFold += numOfImage[i];
            numOfLastFold[i] = all[i].length - numOfImage[i]*fold + numOfImage[i];
            sumOfLastFold += numOfLastFold[i];
        }
        String[][] folds = new String[fold][];
        for(int i=0;i<fold-1;i++){
            folds[i] = new String[sumOfNotLastFold];
        }
        folds[fold-1] = new String[sumOfLastFold];

        //initialize mark
        for(int i=0;i<mark.length;i++){
            for(int j=0;j<mark[i].length;j++) mark[i][j] = false;
        }

        Random r = new Random();
        for(int i=0;i<fold-1;i++){
            int count=0;

            for(int j=0;j<category.length;j++){
                for(int k=0;k<numOfImage[j];k++){
                    int p=0;
                    do{
                        p = r.nextInt(all[j].length);
                    }while(mark[j][p]);
                    folds[i][count] = all[j][p];
                    mark[j][p] = true;
                    count++;
                }
            }
        }
        int count=0;

        for(int j=0;j<category.length;j++){
            for(int k=0;k<numOfLastFold[j];k++){
                int p=0;
                do{
                    p = r.nextInt(all[j].length);
                }while(mark[j][p]);
                folds[folds.length-1][count] = all[j][p];
                mark[j][p] = true;
                count++;
            }
        }
        return folds;
    }
    public static String getClassLable(String path){
        String[] key = path.split("/");
        String[] cs = key[key.length-1].split("_");
        return cs[0];
    }
    public static String[] getAllImages(String dir_path){
        String[] images = null;
        try{
            File dir = new File(dir_path);

            File[] keys = dir.listFiles();
            int count=0;
            for(int i=0;i<keys.length;i++){
                String[] key = keys[i].toString().split("/");
                String[] cs = key[key.length-1].split("_");
                String target = cs[0];

                if(target.charAt(0) != '.'){
                    count++;
                }
            }
            images = new String[count];
            count=0;
            for(int i=0;i<keys.length;i++){
                String[] key = keys[i].toString().split("/");
                String[] cs = key[key.length-1].split("_");
                String target = cs[0];

                if(target.charAt(0) != '.'){
                    images[count] = keys[i].toString();
                    count++;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return images;
    }
    public static String[] getAllImages(String dir_path,String category){
        String[] images = null;

        try{
            File dir = new File(dir_path);

            File[] keys = dir.listFiles();
            int count=0;
            for(int i=0;i<keys.length;i++){
                String[] key = keys[i].toString().split("/");
                String[] cs = key[key.length-1].split("_");
                String target = cs[0];

                if(target.charAt(0) != '.' && target.equals(category)){
                    count++;
                }
            }
            images = new String[count];
            count=0;
            for(int i=0;i<keys.length;i++){
                String[] key = keys[i].toString().split("/");
                String[] cs = key[key.length-1].split("_");
                String target = cs[0];

                if(target.charAt(0) != '.' && target.equals(category)){
                    images[count] = keys[i].toString();
                    count++;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return images;
    }
    public static int[][] getKeyInvariantFeatures(String file){
        int[][] keyFeatures = null;
        BufferedReader bufferReader = null;
        String str = "";
        try {
            bufferReader = new BufferedReader(new FileReader(file));
            str = bufferReader.readLine(); // get the number of key points and the length of discriptors
            String[] line = str.split(" ");
            int numOfKey = Integer.parseInt(line[0]);
            int numOfFeatures = Integer.parseInt(line[1]);
            keyFeatures = new int[numOfKey][numOfFeatures];
            int keyCount=-1;
            int featureCount=0;
            while ((str = bufferReader.readLine()) != null && keyCount<numOfKey) {

                if(str.charAt(0) != ' '){
                    keyCount++;

                    featureCount=0;
                }else{
                    line = str.split(" ");
                    //System.out.println(str);
                    for(int i=1;i<line.length;i++){ //We need to skip the first string " "
                        int feature = Integer.parseInt(line[i]);
                        keyFeatures[keyCount][featureCount] = feature;
                        featureCount++;
                    }
                }
            }

            bufferReader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return keyFeatures;
    }
}
