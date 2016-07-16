package mmaioe.com.dl4jdatasetiterator.sift;

/**
 * Created by ito_m on 7/9/16.
 */
public class SiftFeature {
    public double[] invariantFeature;

    public SiftFeature(double[] invariantFeature){
        this.invariantFeature = invariantFeature;
    }

    public static double[] encodeKeypoints(int[][] features){
        int[] count = new int[features[0].length];
        for(int i=0;i<features.length;i++){
            int[] temp = encodeFeatures(features[i]);
            for(int j=0;j<temp.length;j++) count[j] += temp[j];
        }

        double[] result = new double[count.length];
        for(int j=0;j<count.length;j++) result[j] = count[j]/(double)(features.length);

        return result;
    }
    public static int[] encodeFeatures(int[] aveFeatures){
        int[] encode = new int[aveFeatures.length];
        double ave = 0;
        for(int i=0;i<encode.length;i++) ave+=aveFeatures[i];
        ave = ave/encode.length;
        for(int i=0;i<encode.length;i++){
            if(ave <= aveFeatures[i]) encode[i] = 1;
            else 					  encode[i] = 0;
        }
        return encode;
    }
}
