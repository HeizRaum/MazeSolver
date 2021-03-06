package maze.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ZoomTest {
    public static void main(String[] args) {
        new ZoomTest();
    }

    public ZoomTest() {
        ImagePanel panel = new ImagePanel();
        ImageZoom zoom = new ImageZoom(panel);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(zoom.getUIPanel(), "North");
        f.getContentPane().add(new JScrollPane(panel));
        f.setSize(400, 400);
        f.setLocation(200, 200);
        f.setVisible(true);
    }


    class ImagePanel extends JPanel {
        BufferedImage image;
        double scale;

        public ImagePanel() {
            loadImage();
            scale = 1.0;
            setBackground(Color.black);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            double x = (w - scale * imageWidth) / 2;
            double y = (h - scale * imageHeight) / 2;
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);
            at.scale(scale, scale);
            g2.drawRenderedImage(image, at);
        }

        /**
         * For the scroll pane.
         */
        public Dimension getPreferredSize() {
            int w = (int) (scale * image.getWidth());
            int h = (int) (scale * image.getHeight());
            return new Dimension(w, h);
        }

        public void setScale(double s) {
            scale = s;
            revalidate();      // update the scroll pane
            repaint();
        }

        private void loadImage() {
            try {
                image = ImageIO.read(new File("res/cat.jpg"));
            } catch (MalformedURLException mue) {
                System.out.println("URL trouble: " + mue.getMessage());
            } catch (IOException ioe) {
                System.out.println("read trouble: " + ioe.getMessage());
            }
        }
    }

    class ImageZoom {
        ImagePanel imagePanel;

        public ImageZoom(ImagePanel ip) {
            imagePanel = ip;
        }

        public JPanel getUIPanel() {
            SpinnerNumberModel model = new SpinnerNumberModel(1.0, 0.1, 5, .1);
            JSpinner spinner = new JSpinner(model);
            spinner.setPreferredSize(new Dimension(45, spinner.getPreferredSize().height));
            spinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    float scale = ((Double) spinner.getValue()).floatValue();
                    imagePanel.setScale(scale);
                }
            });
            JPanel panel = new JPanel();
            panel.add(new JLabel("scale"));
            panel.add(spinner);
            return panel;
        }
    }
}