package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Frame1 {

    JFrame Fframe;
    JPanel Fpanel;
    JPanel Spanel;
    JLabel imgLabel1;
    JLabel imgLabel2;

    BufferedImage saveImg;
    BufferedImage openImg1;
    BufferedImage openImg2;

    File selectedFile;

    public void run() {

        Fframe = new JFrame("Image Processing");
        Fframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Fframe.setSize(1650, 1000);
        Fframe.setLayout(null);

        Fpanel = new JPanel();
        Fpanel.setBounds(0, 0, 800, 1000);

        Spanel = new JPanel();
        Spanel.setBounds(850, 0, 800, 1000);

        JMenuBar menubar = createMenuBar();
        Fframe.setJMenuBar(menubar);

        Fframe.getContentPane().add(Fpanel);
        Fframe.getContentPane().add(Spanel);
        Fframe.setVisible(true);

    }

    JMenuBar createMenuBar() {

        JMenuBar menubar = new JMenuBar();
        JMenu fileM = new JMenu("File");
        JMenu editM = new JMenu("Edit");

        JMenuItem openMI = new JMenuItem("Open");
        JMenuItem saveMI = new JMenuItem("Save");
        JMenuItem originMI = new JMenuItem("Original Image");
        JMenuItem grayMI = new JMenuItem("Gray Scale");
        JMenuItem contrastMI = new JMenuItem("Contrast");
        JMenuItem zoomMI = new JMenuItem("Zoom");

        openMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png")); // png만 선택할 수 있도록 제한

                int result = fileChooser.showOpenDialog(Fframe); // 사용자가 선택한 결과 저장

                if (result == JFileChooser.APPROVE_OPTION) { // 확인버튼 누르면

                    selectedFile = fileChooser.getSelectedFile(); // 선택된 파일을 객체로 가져옴
                    String filepath = selectedFile.getAbsolutePath(); // 파일의 경로

                    if (filepath.endsWith(".png")) {

                        try {
                            openImg1 = ImageIO.read(selectedFile);
                            openImg2 = ImageIO.read(selectedFile);

                            imgLabel1 = new JLabel(new ImageIcon(openImg1));
                            Fpanel.add(imgLabel1);
                            imgLabel2 = new JLabel(new ImageIcon(openImg2));
                            Spanel.add(imgLabel2);

                            Fframe.validate(); // 프레임의 레이아웃을 계산해 컴포넌트들의크기와 위치 업데이트

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    } else {
                        JOptionPane.showMessageDialog(Fframe, "Please select a PNG image file.", "Invalid File",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }

            }

        });


        saveMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                JFileChooser fileChooser = new JFileChooser();
                saveImg = null;

                try {
                    Robot robot = new Robot();
                    saveImg = robot.createScreenCapture(new Rectangle(860, 55, 785, 940));
                } catch (AWTException a) {
                    System.out.println(a.toString());
                }

                int userSelection = fileChooser.showSaveDialog(Fframe);

                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    File fileToSave = fileChooser.getSelectedFile();
                    String filePath = fileToSave.getAbsolutePath();

                    try {
                        ImageIO.write(saveImg, "PNG", new File(filePath + ".png"));

                        System.out.println("success to save Image");
                        System.out.println("file path : " + filePath + ".png");
                        JOptionPane.showMessageDialog(null, "이미지가 저장되었습니다");

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        });


        originMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                Spanel.removeAll();

                try {
                    openImg2 = ImageIO.read(selectedFile);

                    imgLabel2 = new JLabel(new ImageIcon(openImg2));
                    Spanel.add(imgLabel2);

                    Fframe.validate();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }

        });


        grayMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                org.example.GrayScale gray = new org.example.GrayScale();
                gray.run(openImg2);

                Spanel.repaint();
            }
        });


        contrastMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Adjust adjust = new Adjust();
                adjust.run(openImg2, Spanel);
            }

        });




        fileM.add(openMI);
        fileM.add(saveMI);
        editM.add(originMI);
        editM.add(grayMI);
        editM.add(contrastMI);
//		editM.add(zoomMI);

        menubar.add(fileM);
        menubar.add(editM);

        return menubar;

    }

}
