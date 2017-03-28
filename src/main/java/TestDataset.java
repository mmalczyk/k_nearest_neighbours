import junit.framework.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

/**
 * Created by Magda on 26.03.2017.
 */
public class TestDataset extends TestCase {
    int length = 28;
    Instance instance;
    Dataset dataset;

    public void setUp(){
        int size = length*length;
        double[] values = new double[size];
        int j = 1;
        for (int i = 0 ; i<size; i++, j+=1)
            values[i] = j;
        instance = new DenseInstance(values);
        instance.setClassValue(999);

        dataset = new DefaultDataset();
        dataset.add(instance);

    }

    public void printInstance(Instance instance, int groupBy, int dataSize) {
        int length = dataSize/groupBy;
        System.out.println(instance.classValue());
        System.out.println(dataSize+" "+length);
        for (int i=0; i<length; i++)
        {
            for (int j=0; j<length; j++)
            {
                System.out.print (instance.value(length*i+j) + " ");
            }
            System.out.println();
        }
    }

    public void compareInstance(Instance newInstance, int groupBy){
        //test instance consists of consecutive integers, so the zones can be calculated
        int newLength = length/groupBy;
        int oldIndex;
        double avgValue = 0, newValue;
        for(int i=0;i<newInstance.noAttributes(); i++){
            oldIndex = (i/newLength)*length*groupBy + (i%newLength)*groupBy;
            if (groupBy == 2)
                avgValue = (instance.get(oldIndex)*4.+58.)/4.;
            else if (groupBy == 4)
                avgValue = (instance.get(oldIndex)*16.+24.*29.)/16.;
            newValue = newInstance.get(i);
            assert (avgValue == newValue);
        }
    }

    public void testZone2(){
        testZone(2);
    }

    public void testZone4(){
        testZone(4);
    }

    public void testZone(int zone){
        int groupBy = 1;
        MNISTknn knn = new MNISTknn();
        printInstance(instance, groupBy, length);
        System.out.println("\n");

        groupBy = zone;
        Dataset newDataset = knn.prepareDataset(dataset, groupBy);
        Instance preparedInstance = newDataset.get(0);
        printInstance(preparedInstance,groupBy, length);

        compareInstance(preparedInstance, groupBy);
        System.out.println("\n");
    }
}
