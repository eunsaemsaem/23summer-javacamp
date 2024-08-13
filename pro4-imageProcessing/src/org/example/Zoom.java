//package org.example;
//
//import java.awt.Graphics2D;
//import java.awt.event.MouseAdapter;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//
//public class Zoom {
//
//    void run(BufferedImage img) {
//
//        try {
//            int startX, startY;
//            int zoomWidth, zoomHeight;
//
//            BufferedImage zimg = new BufferedImage(zoomWidth, zoomHeight, BufferedImage.TYPE_INT_RGB);
//
//            Graphics2D g2d = zimg.createGraphics();
//            g2d.drawImage(img, 0, 0, zoomWidth, zoomHeight, startX, startY, startX+zoomWidth, startY+zoomHeight, null);
//            g2d.dispose();
//
//            Spanel.setImage(zimg);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//
//    }
//
//    private void addMouseListener(MouseAdapter mouseAdapter) {
//        // TODO Auto-generated method stub
//
//
//
//    }
//
//}
