package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Retry extends JFrame implements ActionListener {

    Graphics g;

    float stroke = 3;
    Color color = new Color(0, 0, 0);
    static int n = 0;

    int ox, oy, x, y;

    Choice fileB;


    private Vector<Point> startVt = new Vector<Point>();
    private Vector<Point> endVt = new Vector<Point>();
    private Vector<Color> colorVt = new Vector<Color>();
    private Vector<Integer> strokeVt = new Vector<Integer>();
    Vector<Integer> catVt = new Vector<Integer>();


    public Retry() {

        // button
        JButton drawB1 = new JButton("line"); // 버튼 객체
        JButton drawB2 = new JButton("rect"); // 괄호 안에 버튼 이름(내용) 넣을 수 있음
        JButton drawB3 = new JButton("circle");
        JButton drawB4 = new JButton("polyline");
        JButton drawB5 = new JButton("sketch");

        Choice settingB1 = new Choice();
        settingB1.add("stroke");
        settingB1.add("3");
        settingB1.add("10");
        settingB1.add("20");

        Choice settingB2 = new Choice();
        settingB2.add("color");
        settingB2.add("black");
        settingB2.add("red");
        settingB2.add("blue");
        settingB2.add("green");
        settingB2.add("yellow");
        settingB2.add("cyan");
        settingB2.add("gray");
        settingB2.add("magenta");
        settingB2.add("orange");
        settingB2.add("pink");
        settingB2.add("random");

        JButton addB1 = new JButton("erase");

        setTitle("Graphic Editor_retry");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setContentPane(new MyPanel());
        setSize(800, 800);
        setVisible(true);
        setBackground(Color.WHITE);

        add(drawB1);
        add(drawB2);
        add(drawB3);
        add(drawB4);
        add(drawB5);

        add(addB1);
//       add(addB2);

        add(settingB1);
        add(settingB2);

        drawB1.addActionListener(listener);
        drawB2.addActionListener(listener);
        drawB3.addActionListener(listener);
        drawB4.addActionListener(listener);
        drawB5.addActionListener(listener);

        settingB1.addItemListener(iListener);
        settingB2.addItemListener(iListener);

        addB1.addActionListener(listener);
//       addB2.addActionListener(listener);

        // file
        fileB = new Choice();
        fileB.add("file");
        fileB.add("save");
        fileB.add("open");

        fileB.addItemListener(iListener);

        this.add(fileB);
        this.setVisible(true);

    }

    ActionListener listener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand() == "line") {
                n = 1;
            } else if (e.getActionCommand() == "rect") {
                n = 2;
            } else if (e.getActionCommand() == "circle") {
                n = 3;
            } else if (e.getActionCommand() == "polyline") {
                n = 4;
            } else if (e.getActionCommand() == "sketch") {
                n = 5;
            }

            if (e.getActionCommand() == "erase") {
                n = 11;
                color = new Color(238, 238, 238);
                colorVt.add(color);

            } else if (e.getActionCommand() == " ") {
                n = 12;
            }
            catVt.add(n);

        }
    };

    ItemListener iListener = new ItemListener() {

        public void itemStateChanged(ItemEvent e) {

            if (e.getItem() == "3") {
                stroke = 3;
            } else if (e.getItem() == "10") {
                stroke = 10;
            } else if (e.getItem() == "20") {
                stroke = 20;
            }
            strokeVt.add((int) stroke);

            if (e.getItem() == "black") {
                color = (Color.BLACK);
            } else if (e.getItem() == "red") {
                color = (Color.RED);
            } else if (e.getItem() == "blue") {
                color = (Color.blue);
            } else if (e.getItem() == "green") {
                color = (Color.green);
            } else if (e.getItem() == "yellow") {
                color = (Color.yellow);
            } else if (e.getItem() == "cyan") {
                color = (Color.cyan);
            } else if (e.getItem() == "gray") {
                color = (Color.gray);
            } else if (e.getItem() == "magenta") {
                color = (Color.magenta);
            } else if (e.getItem() == "orange") {
                color = (Color.orange);
            } else if (e.getItem() == "pink") {
                color = (Color.pink);
            }
            colorVt.add(color);

            if (e.getItem() == "save") {

                JFileChooser fileChooser = new JFileChooser();

                BufferedImage saveImg = null;

                //캡쳐
                try {
                    Robot robot = new Robot();
                    saveImg = robot.createScreenCapture(new Rectangle(10, 80, 750, 700));
                } catch (AWTException a) {
                    System.out.println(a.toString());
                }


                int userSelection = fileChooser.showSaveDialog(Retry.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    File fileToSave = fileChooser.getSelectedFile();
                    String filePath = fileToSave.getAbsolutePath();


                    try {
                        ImageIO.write(saveImg, "PNG", new File(filePath + ".png"));

                        System.out.println("success to save Image");
                        System.out.println("file path : " + filePath + ".png");

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }


            } else if (e.getItem() == "open") {

                JFileChooser fileChooser = new JFileChooser();

                int result = fileChooser.showOpenDialog(Retry.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    try {
                        BufferedImage loadImg = ImageIO.read(new File(filePath));

                        g.drawImage(loadImg, 5, 50, 750, 700, null);


                        System.out.println("success to save Image");
                        System.out.println("file path : " + filePath + ".png");

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("file name :" + selectedFile.getAbsolutePath());
                }
            }

        }

    };

    class MyPanel extends JPanel {

        BufferedImage bi;


        Point startP = new Point();
        Point endP = new Point();

        public MyPanel() {

            addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    startP = e.getPoint();
                    startVt.add(startP);

                    if (n == 4) {
                        g.drawLine(startP.x, startP.y, endP.x, endP.y);
                    } else if ((n == 5) || (n == 11)) {
                        ox = e.getX();
                        oy = e.getY();
                    }
                    catVt.add(n);

                }

                public void mouseReleased(MouseEvent e) {

                    g = getGraphics();
                    Graphics2D a = (Graphics2D) g;

                    a.setStroke(new BasicStroke(stroke));
                    a.setColor(color);

                    endP = e.getPoint();
                    endVt.add(endP);

                    if (n == 1) {
                        g.drawLine(startP.x, (int) startP.y, (int) endP.getX(), (int) endP.getY());
                    } else if (n == 2) {

                        if ((endP.x < startP.x) && (endP.y > startP.y)) { // 왼쪽 아래로
                            a.drawRect(endP.x, startP.y, (endP.x - startP.x) * (-1), (endP.y - startP.y));
                        } else if ((endP.x > startP.x) && (endP.y < startP.y)) { // 오른쪽 위로
                            a.drawRect(startP.x, endP.y, (endP.x - startP.x), (endP.y - startP.y) * (-1));
                        } else if ((endP.x < startP.x) && (endP.y < startP.y)) { // 왼쪽 위로
                            a.drawRect(endP.x, endP.y, (endP.x - startP.x) * (-1), (endP.y - startP.y) * (-1));
                        } else { // 오른쪽 아래로
                            a.drawRect(startP.x, startP.y, (endP.x - startP.x), (endP.y - startP.y));
                        }
                    } else if (n == 3) {

                        if ((endP.x < startP.x) && (endP.y > startP.y)) { // 왼쪽 아래로
                            a.drawOval(endP.x, startP.y, (endP.x - startP.x) * (-1), (endP.y - startP.y));
                        } else if ((endP.x > startP.x) && (endP.y < startP.y)) { // 오른쪽 위로
                            a.drawOval(startP.x, endP.y, (endP.x - startP.x), (endP.y - startP.y) * (-1));
                        } else if ((endP.x < startP.x) && (endP.y < startP.y)) { // 왼쪽 위로
                            a.drawOval(endP.x, endP.y, (endP.x - startP.x) * (-1), (endP.y - startP.y) * (-1));
                        } else { // 오른쪽 아래로
                            a.drawOval(startP.x, startP.y, (endP.x - startP.x), (endP.y - startP.y));
                        }
                    }
                    catVt.add(n);


                }

            });

            addMouseMotionListener(new MouseAdapter() {

                public void mouseDragged(MouseEvent e) {

                    if ((n == 5) || (n == 11)) {
                        catVt.add(n);

                        x = e.getX();
                        y = e.getY();

                        g.drawLine(ox, oy, x, y);

                        ox = x;
                        oy = y;
                    }


                }
            });

        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        new Retry();

    }

}