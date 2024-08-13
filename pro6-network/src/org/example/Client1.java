package org.example;

import java.net.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Client1 extends JPanel { /////// 검은돌/////////

    static boolean enable = true;
    static int array[][];
    static Othello1 oth;
    static String namee;

    static int row, col;

    public static void main(String args[]) {

        try {
            String serverIp = "172.18.146.214";
            Socket socket = new Socket(serverIp, 5445);
            System.out.println("서버에 연결되었습니다.");

            namee = "player1";
            Thread sender = new Thread(new ClientSender(socket, namee)); // client 이름
            Thread receiver = new Thread(new ClientReceiver(socket));

            sender.start();
            receiver.start(); // public으로 꺼내기

        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
        }

    }

    static class ClientSender extends Thread { // 서버에게 보낼 좌표

        Socket socket;
        static DataOutputStream out;
        static String name;

        ClientSender(Socket socket, String name) {

            this.socket = socket;

            try {
                out = new DataOutputStream(socket.getOutputStream());
                this.name = name;
            } catch (Exception e) {
            }
        }

        public void run() {

            try {
                if (out != null) {
                    out.writeUTF(name);
                }

                oth = new Othello1();

            } catch (IOException e) {
            }
        }

        public static void sentToServer(String ss) {
            try {
                out.writeUTF(ss);
                out.flush();
            } catch (IOException e) {
            }
        }

    }

    static class ClientReceiver extends Thread { // int 2개 -> 배열값으로 변환 -> 배열에 저장 -> repaint 원 그려지도록

        Socket socket;
        DataInputStream in;

        ClientReceiver(Socket socket) {

            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
            }
        }

        public void run() {

            while (in != null) {

                try {
                    String s = in.readUTF();
                    String[] as = s.split(" ");

                    String rename = as[0];
                    int rex = Integer.parseInt(as[1]);
                    int rey = Integer.parseInt(as[2]);
                    System.out.println("c1 name:" + as[0] + " x:" + as[1] + " y:" + as[2]);
                    enable = true;

                    if (rename.equals("player1")) {
                        return;
                    }

                    System.out.println(rex + " " + rey);
                    array[rex][rey] = 2;

//					if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                    int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
                            { 1, 1 } };

                    for (int[] direction : directions) {
                        int dx = direction[0];
                        int dy = direction[1];

                        int nextRow = rex + dx;
                        int nextCol = rey + dy;

                        while (nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8) {
                            if (array[nextRow][nextCol] == 0) {
                                break;
                            } else if (array[nextRow][nextCol] == array[rex][rey]) {
                                for (int i = rex, j = rey; i != nextRow || j != nextCol; i += dx, j += dy) {
                                    array[i][j] = array[rex][rey];
                                }
                                break;
                            }

                            nextRow += dx;
                            nextCol += dy;
                        }
                    }
//					}
                    enable = true;

                    System.out.println("c1 receiver - repaint이후 true인지/" + enable);

                    for (int i = 0; i < 8; i++) { // 색상 정보에 따라 점수 업데이트
                        for (int j = 0; j < 8; j++) {
                            System.out.printf("%3d", array[i][j]);
                        }
                        System.out.println();
                    }
                    System.out.println();

                    oth.repaint();
                } catch (IOException e) {
                }
            }
        }

    }

    static class Othello1 extends JPanel {

        boolean[][] grid; // 그리드의 셀 상태를 저장하는 배열
        boolean[][] able; // 돌을 놓을 수 있는 곳인지 확인하는 배열
        JLabel wscoreL, bscoreL; // 점수라벨
        int w = 0, b = 0; // 점수

        int circleX = -20; // 원의 중심
        int circleY = -20;
        boolean colorb = true;

        int row, col;

        public Othello1() {

            JFrame frame = new JFrame("Othello Game + network");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            setBackground(new Color(200, 230, 255));
            frame.getContentPane().add(this);
            frame.setVisible(true);

            subFrame();

            grid = new boolean[8][8];
            able = new boolean[8][8];
            array = new int[8][8];

            // 그리드 초기화 (모든 셀을 false로 설정)
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    grid[i][j] = false;
                }
            }

            // 색 배열 초기화 (모든 셀을 0으로 설정)
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    array[i][j] = 0;
                }
            }
            array[3][4] = 1;
            array[4][3] = 1;
            array[3][3] = 2;
            array[4][4] = 2;

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!enable) {
                        System.out.println("c1 clicked false/" + enable);
                        return;
                    }

                    int cellWidth = getWidth() / 8;
                    int cellHeight = getHeight() / 8;

                    row = e.getY() / cellHeight; // 클릭한 위치의 행 인덱스
                    col = e.getX() / cellWidth;

                    if (row >= 0 && row < 8 && col >= 0 && col < 8) {

                        circleX = col * cellWidth + (cellWidth / 2); // 원의 중심 X 좌표 계산
                        circleY = row * cellHeight + (cellHeight / 2); // 원의 중심 Y 좌표 계산
                        grid[row][col] = true; // 클릭한 셀을 true로 설정

//						if (colorb) {
                        array[row][col] = 1; // 색을 1로 설정 (검정)
                        repaint();
//						} else {
//							array[row][col] = 2; // 색을 2로 설정 (검정색)
//							repaint();
//						}

//						colorb = !colorb; // 색을 번갈아가며 변경

                        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 },
                                { 1, 0 }, { 1, 1 } };

                        for (int[] direction : directions) {
                            int dx = direction[0];
                            int dy = direction[1];

                            int nextRow = row + dx;
                            int nextCol = col + dy;

                            while (nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8) {
                                if (array[nextRow][nextCol] == 0) {
                                    break;
                                } else if (array[nextRow][nextCol] == array[row][col]) {
                                    for (int i = row, j = col; i != nextRow || j != nextCol; i += dx, j += dy) {
                                        array[i][j] = array[row][col];
                                    }
                                    break;
                                }

                                nextRow += dx;
                                nextCol += dy;
                            }
                        }

                    }

                    System.out.println("c1 receiver - repaint이후 true인지/" + enable);

                    for (int i = 0; i < 8; i++) { // 색상 정보에 따라 점수 업데이트
                        for (int j = 0; j < 8; j++) {
                            System.out.printf("%3d", array[i][j]);
                        }
                        System.out.println();
                    }
                    System.out.println();
                    oth.repaint();

                    String ss = namee + " " + Integer.toString(row) + " " + Integer.toString(col);
                    ClientSender.sentToServer(ss);
                    enable = false;
                }
            });

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int cellWidth = getWidth() / 8; // 각 셀의 너비
            int cellHeight = getHeight() / 8; // 각 셀의 높이

            // 판 그리기
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    int x = j * cellWidth;
                    int y = i * cellHeight;

                    g.setColor(Color.GRAY);
                    g.drawRect(x, y, cellWidth, cellHeight);
                }
            }

            // 원 그리기
            b = 0;
            w = 0;
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    int x = j * cellWidth;
                    int y = i * cellHeight;

                    if (array[i][j] == 1) {
                        b++;
                        g.setColor(Color.BLACK);
                        g.fillOval(x, y, cellWidth, cellHeight);
                    } else if (array[i][j] == 2) {
                        w++;
                        g.setColor(Color.WHITE);
                        g.fillOval(x, y, cellWidth, cellHeight);
                    }
                }
            }
            wscoreL.setText(b + "");
            bscoreL.setText(w + "");

        }

        void subFrame() {
            JFrame subF;

            subF = new JFrame();
            subF.setLocation(0, 420);
            subF.setSize(400, 100);
            subF.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            JLabel wnameL = new JLabel("WHITE  ");
            wnameL.setFont(new Font("Arial", Font.BOLD, 13));
            subF.getContentPane().add(wnameL);

            wscoreL = new JLabel(" ");
            wscoreL.setFont(new Font("Arial", Font.BOLD, 20));
            subF.getContentPane().add(wscoreL);

            JLabel vsL = new JLabel(" VS ");
            vsL.setFont(new Font("Arial", Font.BOLD, 13));
            subF.getContentPane().add(vsL);

            bscoreL = new JLabel(" ");
            bscoreL.setFont(new Font("Arial", Font.BOLD, 20));
            subF.getContentPane().add(bscoreL);

            JLabel bnameL = new JLabel("  BLACK");
            bnameL.setFont(new Font("Arial", Font.BOLD, 13));
            subF.getContentPane().add(bnameL);
            subF.setVisible(true);
        }

    }

}