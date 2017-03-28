import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.tools.data.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Knn algorithm + zoning
 * Created by Magda on 25.03.2017.
 */
@SuppressWarnings("FieldCanBeLocal")
public class MNISTknn {
    private String mnistTestPath = "mnist_test.csv";
    private String mnistTrainPath = "mnist_train.csv";
    private int dataSize = 28;
    private Random random = new Random();



    public Dataset getDataset (Dataset data, int groupBy, int folds){

        //draw sample
        Dataset newData = new DefaultDataset();
        for (int i = 0; i<folds; i++)
            newData.add(data.get(random.nextInt(data.size())));
        //create zones
        if (groupBy>1)
            newData = prepareDataset(newData, groupBy);
        return newData;
    }

    public int index(int i, int j){
        return i*dataSize+j;
    }

    public Dataset prepareDataset(Dataset data, int groupBy){
        int length = dataSize/groupBy;
        Dataset newData = new DefaultDataset();
        Instance tmpInstance;
        int count, index;
        double pxl;
        int noAttributes = length*length;
        for (Instance kInstance : data) {
            tmpInstance = new SparseInstance(noAttributes, 0);
            tmpInstance.setClassValue(kInstance.classValue());
            count = 0;
            for (int i = 0; i < dataSize; i += groupBy) {
                for (int j = 0; j < dataSize; j += groupBy) {
                    pxl = 0;
                    for (int ii = i; ii < i + groupBy; ii++) {
                        for (int jj = j; jj < j + groupBy; jj++) {
                            index = index(ii, jj);
                            pxl += kInstance.get(index);
                        }
                    }
                    pxl = pxl / (double)(groupBy * groupBy);
                    if (pxl > 0)
                        tmpInstance.put(count, pxl);
                    count++;
                }
            }
            assert(count == noAttributes);
            assert(count*groupBy*groupBy == dataSize*dataSize);

            newData.add(tmpInstance);
        }
        return newData;
    }

    public Classifier trainClassifier(int k, int groupBy, int sampleSize){
        Dataset data;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            data = FileHandler
                    .loadDataset(new File(classLoader.getResource(mnistTrainPath).getFile()),0, ",");
        }
        catch (IOException e){
            return null;
        }
        data = getDataset(data, groupBy, sampleSize);
        /* Contruct a KNN classifier that uses 5 neighbors to make a *decision. */
        Classifier knn = new KNearestNeighbors(k);
        knn.buildClassifier(data);
        return knn;
    }

    public double makePredictions (Classifier knn, int groupBy, int sampleSize){
        Dataset dataForClassification;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            dataForClassification = FileHandler
                    .loadDataset(new File(classLoader.getResource(mnistTestPath).getFile()),0, ",");
        }
        catch (IOException e){
            return -1;
        }

        dataForClassification = getDataset(dataForClassification, groupBy, sampleSize);
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

    public void runKnn(MNISTknn knn, int zone){
        for (int k=1; k<=8; k++)
        {
            Classifier classifier = knn.trainClassifier(k, zone, 300);
            double result = knn.makePredictions(classifier, zone, 50);
            System.out.println("Zone="+zone+" K="+k+" result="+result);
        }
    }

    public static void main (String[] args) throws IOException {
        MNISTknn knn = new MNISTknn();
        knn.runKnn(knn, 1);
        knn.runKnn(knn, 2);
        knn.runKnn(knn, 4);

    }

}
