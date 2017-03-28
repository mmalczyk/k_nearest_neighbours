import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Magda on 27.03.2017.
 */

public class ImageFrame {


    public static void createAndShowGUI(BufferedImage image1, BufferedImage image2, BufferedImage image3,
                                        int[] pxlArray1, int[] pxlArray2, int[] pxlArray3) {
        JFrame frame;
        JPanel panel;

        assert(image1.getHeight() == image2.getHeight());
        assert(image2.getHeight() == image3.getHeight());
        assert(image1.getWidth() == image2.getWidth());
        assert(image2.getWidth() == image3.getWidth());

        int width = image1.getWidth();
        int height = image1.getHeight();

        frame = new JFrame("Image");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(4*width, 5*height));
        panel.add(new JLabel(new ImageIcon(image1)));
        panel.add(new JLabel(new ImageIcon(image2)));
        panel.add(new JLabel(new ImageIcon(image3)));
        panel.add(new PictureHistogram(pxlArray1).getPanel());
        panel.add(new PictureHistogram(pxlArray2).getPanel());
        panel.add(new PictureHistogram(pxlArray3).getPanel());

        frame.add(panel);
        frame.setVisible(true);
        frame.setSize(4*width, 5*height);
    }

}