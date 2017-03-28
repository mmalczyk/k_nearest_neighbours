import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;

/**
 * Created by Magda on 26.03.2017.
 */
public class MedianComparison {
    private String imageName1 = "stp1-256x256.png";
    private String imageName2 = "stp2-256x256.png";

    public BufferedImage loadBufferedImage(String imageName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(imageName);
        BufferedImage image = null;
        try{
            image = ImageIO.read(url);
        }
        catch (IOException e){

        }
        return image;
    }

    public int[] loadPixelArray(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int[] pixels = new int[w*h];

        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
            {
                int pxl = image.getRGB(j, i);
                //getRGB return byte values written as int
                pixels[i*h+j] = pxl& 0xFF;

            }
        return pixels;
    }

    public int[] sumArrays(int[] a, int[] b){
        assert(a.length == b.length);
        int[] c = new int[a.length];
        for (int i = 0; i < a.length; ++i) {
            c[i] = (a[i] + b[i])%255;
        }
        return c;
    }

    public double medianOfArray(int[] originalArray){
        int[] array = new int[originalArray.length];
        System.arraycopy(originalArray, 0,  array, 0, originalArray.length);
        Arrays.sort(array);
        double median;
        if (array.length % 2 == 0)
            median = ((double)array[array.length/2] + (double)array[array.length/2 - 1])/2;
        else
            median = (double) array[array.length/2];
        return median;
    }

    public void displayImages(BufferedImage image1, BufferedImage image2, BufferedImage image3,
                              int[] pixelArray1, int[] pixelArray2, int[] pixelArray3){
        ImageFrame.createAndShowGUI(image1, image2, image3, pixelArray1, pixelArray2, pixelArray3);
    }

    public BufferedImage pixelArrayToImage(int[] pixelArray, int width, int height) {
        //converting image from grayscale to rgb so I can use Color
        BufferedImage oldImage = loadBufferedImage(imageName1);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(oldImage, 0, 0, null);
        g.dispose();

        int value, pxl;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
            {
                pxl = (pixelArray[i*height+j]*100)%255;
                Color color = new Color(pxl,pxl,pxl);
                image.setRGB(j,i,color.getRGB());
            }

        return image;
    }

    public void runComparison(){
        BufferedImage image1 = loadBufferedImage(imageName1);
        BufferedImage image2 = loadBufferedImage(imageName2);
        int[] pixelArray1 = loadPixelArray(image1);
        int[] pixelArray2 = loadPixelArray(image2);

        int[] summedArray = sumArrays(pixelArray1, pixelArray2);
        double medianOfSum = medianOfArray(summedArray);

        double median1 = medianOfArray(pixelArray1);
        double median2 = medianOfArray(pixelArray2);
        double sumOfMedians = median1+median2;

        assert(summedArray.length == image1.getHeight()*image1.getWidth());
        BufferedImage addedImage = pixelArrayToImage(summedArray, image1.getWidth(), image1.getHeight());

        System.out.println("median of sums = "+medianOfSum);
        System.out.println("sum of medians = "+sumOfMedians);
        displayImages(image1, image2, addedImage, pixelArray1, pixelArray2, summedArray);
    }

    public static void main (String[] args){
        MedianComparison mc = new MedianComparison();
        mc.runComparison();

    }
}
