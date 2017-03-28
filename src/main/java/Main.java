import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import java.io.File;
import java.io.IOException;

/**
 * Created by Magda on 2017-03-09.
 */
public class Main {

    private static String pathName = "iris.data";

    public static Classifier trainClassifier(int k){
        /* Load a data set */
        Dataset data;
        try {
            data = FileHandler
                    .loadDataset(new File(pathName),4, ",");
        }
        catch (IOException e){
            return null;
        }
        /* Contruct a KNN classifier that uses 5 neighbors to make a *decision. */
        Classifier knn = new KNearestNeighbors(k);
        knn.buildClassifier(data);
        return knn;
    }

    public static double makePredictions (Classifier knn){
        Dataset dataForClassification;
        try {
            dataForClassification = FileHandler
                    .loadDataset(new File(pathName),4, ",");
        }
        catch (IOException e){
            return -1;
        }

        /* Counters for correct and wrong predictions. */
        int correct = 0, wrong = 0;
        /* Classify all instances and check with the correct class values */
        for (Instance inst : dataForClassification) {
            Object predictedClassValue = knn.classify(inst);
            Object realClassValue = inst.classValue();
            if (predictedClassValue.equals(realClassValue))
                correct++;
            else
                wrong++;
        }

        return 100*correct/(correct+wrong);
    }

    public static void main (String[] args) throws IOException {
        int k = 1;
        Classifier knn = trainClassifier(1);
        double result = makePredictions(knn);
        System.out.println(result);
    }

}
