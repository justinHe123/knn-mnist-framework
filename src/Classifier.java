import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Classifier {
    private ArrayList<DataPoint> trainingData;
    private int n;

    public Classifier(int n) {
        this.n = n;
        trainingData = new ArrayList<DataPoint>();
    }

    public void addTrainingData(List<DataPoint> points) {
        // TODO: add all points from input to the training data
        for (DataPoint point : points){
            trainingData.add(point);
        }
    }

    public void addTrainingData(DataPoint point) {
        // TODO: add all points from input to the training data
        trainingData.add(point);
    }

    public void addTrainingData(String label, DImage img) {
        addTrainingData(new DataPoint(label, img));
    }

//    public String classify(short[][] pixels) {
//        if (trainingData.size() == 0) return "no training data";
//
//        // TODO: write a k-nearest-neighbor classifier.  Return its prediction of "0" to "9"
//        DataPoint closestPoint = null;
//
//        ArrayList<DataPoint> closePoints = new ArrayList<DataPoint>();
//        int integer = 0;
//        int furthestIndex = 0;
//        for (DataPoint point : trainingData){
//            if(closePoints.size() < n){
//                closePoints.add(point);
//            } else {
//                if (integer == 0) {
//                    furthestIndex = furthestIndex(closePoints, pixels);
//                    integer++;
//                }
//                short[][] d1 = closePoints.get(furthestIndex).getData().getBWPixelGrid();
//                short[][] d2 = point.getData().getBWPixelGrid();
//
//                if (distance(d2, pixels) < distance(d1, pixels)){
//                    closePoints.remove(furthestIndex);
//                    closePoints.add(point);
//                    integer--;
//                }
//            }
//        }
//        return mostFrequentLabel(closePoints);
//    }

    public String classify(short[][] pixels){
        if (trainingData.size() == 0) return "no training data";
        // TODO: write a k-nearest-neighbor classifier.  Return its prediction of "0" to "9"
        DataPoint closestPoint = null;
        ArrayList<DataPoint> closePoints = new ArrayList<DataPoint>();
        int integer = 0;
        int furthestIndex = 0;
        double a = 0;
        for (short[] shorts : pixels){
            for (short val : shorts){
                a += (double)(val*val);
            }
        }
        a = Math.sqrt(a);

        for (DataPoint point : trainingData){
            if(closePoints.size() < n){
                closePoints.add(point);
            } else {
                if (integer == 0) {
                    furthestIndex = furthestIndex(closePoints, pixels);
                    integer++;
                }
                short[][] d1 = closePoints.get(furthestIndex).getData().getBWPixelGrid();
                short[][] d2 = point.getData().getBWPixelGrid();

                if (cossim(a, pixels, d2) < cossim(a, pixels, d1)){
                    closePoints.remove(furthestIndex);
                    closePoints.add(point);
                    integer--;
                }
            }
        }
        return mostFrequentLabel(closePoints);
    }

    public double cossim(double a, short[][] dA, short[][] dB){ //a = ||A||
        short[] arrA = new short[784];
        short[] arrB = new short[784];

        for (int r = 0; r < dA.length; r++){
            for (int c = 0; c < dA[r].length; c++){
                arrA[r*dA[r].length + c] = dA[r][c];
                arrB[r*dB[r].length + c] = dB[r][c];
            }
        }

        double b = 0;
        double ab = 0;
        for (int i = 0; i < arrA.length; i++){
            b += (double)(arrB[i] * arrB[i]);
            ab += (double)(arrA[i] * arrB[i]);
        }
        b = Math.sqrt(b);

        double cos = ab / (a * b);
        return 1.0 - cos;
    }

    public String mostFrequentLabel(ArrayList<DataPoint> points){
        int[] count = new int[10];
        for (DataPoint point : points){
            count[Integer.parseInt(point.getLabel())]++;
        }
        int mostFreq = 0;
        for (int i = 1; i < count.length; i++){
            if(count[i] > count[mostFreq]){
                mostFreq = i;
            }
        }
        return Integer.toString(mostFreq);
    }

    public int furthestIndex(ArrayList<DataPoint> points, short[][] pixels){
        int furthestIndex = -1;
        double furthestDistance = 0;
        for (int i = 0; i < points.size(); i++){
            DataPoint p = points.get(i);
            short[][] d2 = p.getData().getBWPixelGrid();
            double distance = distance(pixels, d2);
            if (distance > furthestDistance){
                furthestDistance = distance;
                furthestIndex = i;
            }
        }
        return furthestIndex;
    }

    public String classify(DImage img) {
        return classify(img.getBWPixelGrid());
    }

    public double distance(short[][] d1, short[][] d2) {
        // TODO:  Use the n-dimensional Euclidean distance formula to find the distance between d1 and d2
        double squaredSums = 0;
        for (int r = 0; r < d1.length; r++){
            for (int c = 0; c < d1[r].length; c++){
                double delta = d1[r][c] - d2[r][c];
                squaredSums += (delta)*(delta);
            }
        }
        return Math.sqrt(squaredSums);
    }

    public void test(List<DataPoint> test) {
        ArrayList<DataPoint> correct = new ArrayList<>();
        ArrayList<DataPoint> wrong = new ArrayList<>();

        int i = 0;
        for (DataPoint p : test) {
            String predict = classify(p.getData());
            System.out.print("#" + i + " REAL:\t" + p.getLabel() + " predicted:\t" + predict);
            if (predict.equals(p.getLabel())) {
                correct.add(p);
                System.out.print(" Correct ");
            } else {
                wrong.add(p);
                System.out.print(" WRONG ");
            }

            i++;
            System.out.println(" % correct: " + ((double) correct.size() / i));
        }

        System.out.println(correct.size() + " correct out of " + test.size());
        System.out.println(wrong.size() + " correct out of " + test.size());
        System.out.println("% Error: " + (double) wrong.size() / test.size());
    }
}
