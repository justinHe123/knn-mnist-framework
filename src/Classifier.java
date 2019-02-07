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

    public String classify(short[][] pixels) {
        if (trainingData.size() == 0) return "no training data";

        // TODO: write a k-nearest-neighbor classifier.  Return its prediction of "0" to "9"
        double minDistance = Integer.MAX_VALUE;
        String correctLabel = "no prediction";
        for (DataPoint point : trainingData){
            short[][] d2 = point.getData().getBWPixelGrid();
            double distance = distance(pixels, d2);
            if (distance < minDistance){
                minDistance = distance;
                correctLabel = point.getLabel();
            }
        }

        return correctLabel;  // replace this line
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
