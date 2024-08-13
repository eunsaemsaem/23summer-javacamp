package org.example;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GrayScale {

    void run(BufferedImage img) {
        // TODO Auto-generated method stub

        int width = img.getWidth();
        int height = img.getHeight();


        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {

                int pixel = img.getRGB(w, h);


                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                int avg = (red + green + blue) / 3;
                int rgb = new Color(avg, avg, avg).getRGB();

                img.setRGB(w, h, rgb);
            }
        }

    }
}
