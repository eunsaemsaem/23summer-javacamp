package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Adjust {

    JFrame frame;
    JPanel brightP;
    JPanel contrastP;
    JSlider brightSl;
    JSlider contrastSl;
    JLabel brightL;
    JLabel contrastL;

    void run(BufferedImage img, JPanel Spanel) {
        BufferedImage img2=new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int w = 0; w < img.getWidth(); w++) {
            for (int h = 0; h < img.getHeight(); h++) {
                img2.setRGB(w, h, img.getRGB(w, h));
            }
        }
        frame = new JFrame();
        frame.setBounds(100, 100, 360, 180);
        frame.setLocation(20, 60);


        frame.getContentPane().setLayout(null);

        brightP = new JPanel();
        brightP.setBounds(0, 0, 399, 77);

        brightL = new JLabel("Brightness  ");
        brightP.add(brightL);

        brightSl = new JSlider(-50, 50, 0);
        brightSl.setMajorTickSpacing(20);
        brightSl.setPaintTicks(true);
        brightSl.setPaintLabels(true);
        brightP.add(brightSl);
        frame.getContentPane().add(brightP);

        contrastP = new JPanel();
        contrastP.setBounds(0, 79, 399, 77);

        contrastL = new JLabel("Contrast  ");
        contrastP.add(contrastL);

        contrastSl = new JSlider(-10, 10, 0);
        contrastSl.setMajorTickSpacing(5);
        contrastSl.setPaintTicks(true);
        contrastSl.setPaintLabels(true);
        contrastP.add(contrastSl);
        frame.getContentPane().add(contrastP);

        frame.setVisible(true);


        brightSl.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub

                int b = brightSl.getValue();
                b*=0.6;

                int width = img.getWidth();
                int height = img.getHeight();


                for (int w = 0; w < width; w++) {
                    for (int h = 0; h < height; h++) {

                        int pixel = img2.getRGB(w, h);

                        int red = (pixel >> 16) & 0xff;
                        int green = (pixel >> 8) & 0xff;
                        int blue = pixel & 0xff;


                        int br = Math.max(0, Math.min(red+b, 255)); //0과 255 사이로 값을 제한함
                        int bg = Math.max(0, Math.min(green+b, 255));
                        int bb = Math.max(0, Math.min(blue+b, 255));

                        int rgb = new Color(br, bg, bb).getRGB();
                        img.setRGB(w, h, rgb);
                    }
                }
                Spanel.repaint();

            }

        });

        contrastSl.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub

                double c = contrastSl.getValue();
                c *= 0.01;

                int width = img.getWidth();
                int height = img.getHeight();


                for (int w = 0; w < width; w++) {
                    for (int h = 0; h < height; h++) {

                        int pixel = img2.getRGB(w, h);
                        Color color = new Color(pixel);


                        int red = color.getRed();
                        if (red >= 128) {
                            red *= c+1;
                        } else {
                            red *= -c+1;
                        }

                        int green = color.getGreen();
                        if (green >= 128) {
                            green *= c+1;
                        } else {
                            green *= -c+1;
                        }

                        int blue = color.getBlue();
                        if (blue >= 128) {
                            blue *= c+1;
                        } else {
                            blue *= -c+1;
                        }

                        int r = Math.max(0, Math.min(255, red));
                        int g = Math.max(0, Math.min(255, green));
                        int b = Math.max(0, Math.min(255, blue));
//
                        int rgb = new Color(r, g, b).getRGB();
                        img.setRGB(w, h, rgb);
                    }
                }
                Spanel.repaint();
            }

        });
    }


}
