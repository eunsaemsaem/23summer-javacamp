package org.example;

import java.io.*;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
//import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
//import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class TcpIpClient {

    F frame;
    int[][] Othello;
    JLabel bscore;
    JLabel wscore;
    JLabel turn;
    int count; // 몇번째 라운드인지
    String s;
    int num;
    int bsum;
    int wsum;
    int enable;
    int myX, myY;

    public static void main(String args[]) {
        try {
            String serverIp = "172.17.202.206"; // 소켓을 생성하여 연결을 요청한다.
            Socket socket = new Socket(serverIp, 7000);
            System.out.println("서버에 연결되었습니다.");
            new TcpIpClient(socket);
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
        }
    }

    TcpIpClient(Socket socket) {
        enable = 1;
        frame = new F();
        Thread sender = new Thread(new ClientSender(socket, "grace"));
        Thread receiver = new Thread(new ClientReceiver(socket));
        sender.start();
        receiver.start();
    }

    public class ClientSender extends Thread {
        Socket socket;
        DataOutputStream out;
        String name;

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
                    out.writeUTF("[" + name + "]");
                }
                while (out != null) {
                    // 마우스 클릭 이벤트 리스너 등록
                    frame.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            myX = e.getX();
                            myY = e.getY();
                            if (enable == 1) {
                                sendToServer(myX, myY);
                            }
                        }
                    });
                    // 마우스 클릭 이벤트 대기
                    frame.setVisible(true);
                    while (frame.isVisible()) {
                        try {
                            while (frame.isVisible()) {
                                Thread.sleep(100);
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                }
            } catch (IOException e) {
            }
        }

        private void sendToServer(int x, int y) {
            try {
                out.writeInt(x);
                out.writeInt(y);
                out.flush();
            } catch (IOException e) {
            }
        }
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            count++;

            super.paintComponent(g);
            g.setColor(Color.BLACK);
            for (int i = 0; i <= 640; i += 80) {
                g.drawLine(i, 0, i, 640);
                g.drawLine(0, i, 640, i);
            }
            g.fillOval(325, 245, 70, 70);
            g.fillOval(245, 325, 70, 70);
            g.setColor(Color.WHITE);
            g.fillOval(245, 245, 70, 70);
            g.fillOval(325, 325, 70, 70);
            for (int i = 0; i < 8; i++) { /* 배열 초기화 */
                for (int j = 0; j < 8; j++) {
                    Othello[i][j] = 0;
                }
            }
            turn.setText("흑  /   "); /* 초기 차례와 점수 */
            bscore.setText("점수  2(흑) : ");
            wscore.setText("2(백)");
            Othello[3][4] = 1;
            Othello[4][3] = 1;
            Othello[3][3] = 2;
            Othello[4][4] = 2;
            num = 4;
//           if(count == 1) addMouseListener(new MyMouseListener());
        }
    }

    public class ClientReceiver extends Thread {
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
                    int x = in.readInt(); // x 좌표 받음
                    int y = in.readInt(); // y 좌표 받음
                    drawPicture(x, y); // 받은 좌표를 기반으로 그림을 그리는 메서드 호출
                    if (x == myX && y == myY) {
                        System.out.println("같습니다.");
//                  enable = 0;
                    } else {
                        enable = 1;
                        System.out.println("다릅니다");
                    }
                } catch (IOException e) {
                }
            }
        }

        public void drawPicture(int x, int y) {
            // 받은 좌표를 기반으로 그림을 그리자.

            int i, j;
            x = x - 80;
            y = y - 80;

            if (0 < x && x < 80)
                j = 0;
            else if (80 < x && x < 160)
                j = 1;
            else if (160 < x && x < 240)
                j = 2;
            else if (240 < x && x < 320)
                j = 3;
            else if (320 < x && x < 400)
                j = 4;
            else if (400 < x && x < 480)
                j = 5;
            else if (480 < x && x < 560)
                j = 6;
            else if (560 < x && x < 640)
                j = 7;
            else
                j = 8;
            if (0 < y && y < 80)
                i = 0;
            else if (80 < y && y < 160)
                i = 1;
            else if (160 < y && y < 240)
                i = 2;
            else if (240 < y && y < 320)
                i = 3;
            else if (320 < y && y < 400)
                i = 4;
            else if (400 < y && y < 480)
                i = 5;
            else if (480 < y && y < 560)
                i = 6;
            else if (560 < y && y < 640)
                i = 7;
            else
                i = 8;

            if (i != 8 || j != 8) {
                if (getLocation(i, j, num % 2) == 1) {
                    enable = 0;
                    num++;
                }
            }

            Graphics g = frame.getGraphics();
            bsum = 0;
            wsum = 0;
            for (int m = 0; m < 8; m++) {
                for (int n = 0; n < 8; n++) {
                    if (Othello[m][n] == 1) {
                        g.setColor(Color.BLACK);
                        g.fillOval(n * 80 + 92, m * 80 + 98, 70, 70);
                        bsum++;
                    } else if (Othello[m][n] == 2) {
                        g.setColor(Color.WHITE);
                        g.fillOval(n * 80 + 92, m * 80 + 98, 70, 70);
                        wsum++;
                    }
                }
            }

            int check = 0;
            int fb = finblack();
            int fw = finwhite();

            if (fb == 0 && fw == 0)
                finish();
            else if (num % 2 == 1) {
                if (fw == 0)
                    check = 1;
            } else {
                if (fb == 0)
                    check = 2;
            }
            if (check > 0) {
                num++; // 둘 곳이 없을 때 다음 턴으로 넘기기
                JFrame m = new JFrame();
                m.setSize(280, 150);
                m.setLocationRelativeTo(null);
                m.getContentPane().setLayout(null);
                m.getContentPane().setBackground(Color.WHITE);
                m.setVisible(true);

                JPanel panel = new JPanel();
                panel.setBounds(15, 20, 305, 30);
                panel.setLayout(null);
                panel.setBackground(Color.WHITE);

                if (check == 1)
                    s = "백이 ";
                else if (check == 2)
                    s = "흑이 ";
                JLabel message = new JLabel(s + "둘 곳이 없습니다. 차례가 넘어갑니다.");
                message.setBounds(5, 3, 250, 25);
                panel.add(message);
                m.add(panel);

                JButton confirm = new JButton("확인");
                confirm.setBounds(105, 60, 70, 30);
                m.add(confirm);

                confirm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        m.dispose();
                    }
                });
            }
            if (num % 2 == 0)
                turn.setText("흑  /   "); /* 차례 */
            else
                turn.setText("백  /   ");
            bscore.setText("점수  " + bsum + "(흑) : "); /* 점수 */
            wscore.setText(wsum + "(백)");
        }

        public int getLocation(int x, int y, int n) {
            int check = 0;
            if (Othello[x][y] != 0)
                return 0;
            int i, j, color;
            if (n == 0) { /* 검은색 */
                i = x; // 위
                j = y;
                while (true) {
                    i--;
                    if (i < 0)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 2)
                        continue;
                    else {
                        if (x - i == 1)
                            break;
                        for (int k = x; k > i; k--)
                            Othello[k][y] = 1;
                        check = 1;
                        break;
                    }
                }
                i = x; // 오른쪽위
                j = y;
                while (true) {
                    i--;
                    j++;
                    if (i < 0 || j > 7)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 2)
                        continue;
                    else {
                        if (x - i == 1 && j - y == 1)
                            break;
                        j = y;
                        for (int k = x; k > i; k--)
                            Othello[k][j++] = 1;
                        check = 1;
                        break;
                    }
                }
                i = x; // 오른쪽
                j = y;
                while (true) {
                    j++;
                    if (j > 7)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 2)
                        continue;
                    else {
                        if (j - y == 1)
                            break;
                        for (int k = y; k < j; k++)
                            Othello[x][k] = 1;
                        check = 1;
                        break;
                    }
                }
                i = x; // 오른쪽아래
                j = y;
                while (true) {
                    i++;
                    j++;
                    if (i > 7 || j > 7)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 2)
                        continue;
                    else {
                        if (i - x == 1 && j - y == 1)
                            break;
                        j = y;
                        for (int k = x; k < i; k++)
                            Othello[k][j++] = 1;
                        check = 1;
                        break;
                    }
                }
                i = x; // 아래
                j = y;
                while (true) {
                    i++;
                    if (i > 7)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 2)
                        continue;
                    else {
                        if (i - x == 1)
                            break;
                        for (int k = x; k < i; k++)
                            Othello[k][y] = 1;
                        check = 1;
                        break;
                    }
                }
                i = x; // 왼쪽아래
                j = y;
                while (true) {
                    i++;
                    j--;
                    if (i > 7 || j < 0)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 2)
                        continue;
                    else {
                        if (i - x == 1 && y - j == 1)
                            break;
                        j = y;
                        for (int k = x; k < i; k++)
                            Othello[k][j--] = 1;
                        check = 1;
                        break;
                    }
                }
                i = x; // 왼쪽
                j = y;
                while (true) {
                    j--;
                    if (j < 0)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 2)
                        continue;
                    else {
                        if (y - j == 1)
                            break;
                        for (int k = y; k > j; k--)
                            Othello[x][k] = 1;
                        check = 1;
                        break;
                    }
                }
                i = x; // 왼쪽위
                j = y;
                while (true) {
                    i--;
                    j--;
                    if (i < 0 || j < 0)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 2)
                        continue;
                    else {
                        if (x - i == 1 && y - j == 1)
                            break;
                        j = y;
                        for (int k = x; k > i; k--)
                            Othello[k][j--] = 1;
                        check = 1;
                        break;
                    }
                }
            } else if (n == 1) { /* 흰색 */
                i = x; // 위
                j = y;
                while (true) {
                    i--;
                    if (i < 0)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 1)
                        continue;
                    else {
                        if (x - i == 1)
                            break;
                        for (int k = x; k > i; k--)
                            Othello[k][y] = 2;
                        check = 1;
                        break;
                    }
                }
                i = x; // 오른쪽위
                j = y;
                while (true) {
                    i--;
                    j++;
                    if (i < 0 || j > 7)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 1)
                        continue;
                    else {
                        if (x - i == 1 && j - y == 1)
                            break;
                        j = y;
                        for (int k = x; k > i; k--)
                            Othello[k][j++] = 2;
                        check = 1;
                        break;
                    }
                }
                i = x; // 오른쪽
                j = y;
                while (true) {
                    j++;
                    if (j > 7)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 1)
                        continue;
                    else {
                        if (j - y == 1)
                            break;
                        for (int k = y; k < j; k++)
                            Othello[x][k] = 2;
                        check = 1;
                        break;
                    }
                }
                i = x; // 오른쪽아래
                j = y;
                while (true) {
                    i++;
                    j++;
                    if (i > 7 || j > 7)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 1)
                        continue;
                    else {
                        if (i - x == 1 && j - y == 1)
                            break;
                        j = y;
                        for (int k = x; k < i; k++)
                            Othello[k][j++] = 2;
                        check = 1;
                        break;
                    }
                }
                i = x; // 아래
                j = y;
                while (true) {
                    i++;
                    if (i > 7)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 1)
                        continue;
                    else {
                        if (i - x == 1)
                            break;
                        for (int k = x; k < i; k++)
                            Othello[k][y] = 2;
                        check = 1;
                        break;
                    }
                }
                i = x; // 왼쪽아래
                j = y;
                while (true) {
                    i++;
                    j--;
                    if (i > 7 || j < 0)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 1)
                        continue;
                    else {
                        if (i - x == 1 && y - j == 1)
                            break;
                        j = y;
                        for (int k = x; k < i; k++)
                            Othello[k][j--] = 2;
                        check = 1;
                        break;
                    }
                }
                i = x; // 왼쪽
                j = y;
                while (true) {
                    j--;
                    if (j < 0)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 1)
                        continue;
                    else {
                        if (y - j == 1)
                            break;
                        for (int k = y; k > j; k--)
                            Othello[x][k] = 2;
                        check = 1;
                        break;
                    }
                }
                i = x; // 왼쪽위
                j = y;
                while (true) {
                    i--;
                    j--;
                    if (i < 0 || j < 0)
                        break;
                    color = Othello[i][j];
                    if (color == 0)
                        break;
                    else if (color == 1)
                        continue;
                    else {
                        if (x - i == 1 && y - j == 1)
                            break;
                        j = y;
                        for (int k = x; k > i; k--)
                            Othello[k][j--] = 2;
                        check = 1;
                        break;
                    }
                }
            }
            return check;
        }

        public int finblack() {
            int i, j, color;
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (Othello[x][y] == 0) {
                        i = x; // 위
                        j = y;
                        while (true) {
                            i--;
                            if (i < 0)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 2)
                                continue;
                            else {
                                if (x - i == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 오른쪽위
                        j = y;
                        while (true) {
                            i--;
                            j++;
                            if (i < 0 || j > 7)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 2)
                                continue;
                            else {
                                if (x - i == 1 && j - y == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 오른쪽
                        j = y;
                        while (true) {
                            j++;
                            if (j > 7)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 2)
                                continue;
                            else {
                                if (j - y == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 오른쪽아래
                        j = y;
                        while (true) {
                            i++;
                            j++;
                            if (i > 7 || j > 7)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 2)
                                continue;
                            else {
                                if (i - x == 1 && j - y == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 아래
                        j = y;
                        while (true) {
                            i++;
                            if (i > 7)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 2)
                                continue;
                            else {
                                if (i - x == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 왼쪽아래
                        j = y;
                        while (true) {
                            i++;
                            j--;
                            if (i > 7 || j < 0)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 2)
                                continue;
                            else {
                                if (i - x == 1 && y - j == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 왼쪽
                        j = y;
                        while (true) {
                            j--;
                            if (j < 0)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 2)
                                continue;
                            else {
                                if (y - j == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 왼쪽위
                        j = y;
                        while (true) {
                            i--;
                            j--;
                            if (i < 0 || j < 0)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 2)
                                continue;
                            else {
                                if (x - i == 1 && y - j == 1)
                                    break;
                                return 1;
                            }
                        }
                    }
                }
            }
            return 0;
        }

        public int finwhite() {
            int i, j, color;
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (Othello[x][y] == 0) {
                        i = x; // 위
                        j = y;
                        while (true) {
                            i--;
                            if (i < 0)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 1)
                                continue;
                            else {
                                if (x - i == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 오른쪽위
                        j = y;
                        while (true) {
                            i--;
                            j++;
                            if (i < 0 || j > 7)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 1)
                                continue;
                            else {
                                if (x - i == 1 && j - y == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 오른쪽
                        j = y;
                        while (true) {
                            j++;
                            if (j > 7)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 1)
                                continue;
                            else {
                                if (j - y == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 오른쪽아래
                        j = y;
                        while (true) {
                            i++;
                            j++;
                            if (i > 7 || j > 7)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 1)
                                continue;
                            else {
                                if (i - x == 1 && j - y == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 아래
                        j = y;
                        while (true) {
                            i++;
                            if (i > 7)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 1)
                                continue;
                            else {
                                if (i - x == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 왼쪽아래
                        j = y;
                        while (true) {
                            i++;
                            j--;
                            if (i > 7 || j < 0)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 1)
                                continue;
                            else {
                                if (i - x == 1 && y - j == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 왼쪽
                        j = y;
                        while (true) {
                            j--;
                            if (j < 0)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 1)
                                continue;
                            else {
                                if (y - j == 1)
                                    break;
                                return 1;
                            }
                        }
                        i = x; // 왼쪽위
                        j = y;
                        while (true) {
                            i--;
                            j--;
                            if (i < 0 || j < 0)
                                break;
                            color = Othello[i][j];
                            if (color == 0)
                                break;
                            else if (color == 1)
                                continue;
                            else {
                                if (x - i == 1 && y - j == 1)
                                    break;
                                return 1;
                            }
                        }
                    }
                }
            }
            return 0;
        }

        public void finish() {
            JFrame m = new JFrame();
            m.setSize(200, 150);
            m.setLocationRelativeTo(null);
            m.getContentPane().setLayout(null);
            m.getContentPane().setBackground(Color.WHITE);
            m.setVisible(true);

            JPanel panel = new JPanel();
            panel.setBounds(15, 20, 305, 30);
            panel.setLayout(null);
            panel.setBackground(Color.WHITE);

            String s;
            if (bsum > wsum)
                s = "흑 이김";
            else if (wsum > bsum)
                s = "백 이김";
            else
                s = "비김";
            JLabel message = new JLabel(s);
            message.setBounds(60, 3, 200, 25);
            panel.add(message);
            m.add(panel);

            JButton confirm = new JButton("확인");
            confirm.setBounds(60, 60, 70, 30);
            m.add(confirm);

            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    m.dispose();
                    frame.repaint();
                }
            });
        }
    }

    public class F extends JFrame {

        F() {
            setTitle("Othello Game"); /* 프레임 */
            setLayout(null);
            setSize(800, 750);
            setResizable(false);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JMenuBar menubar = new JMenuBar(); /* 메뉴바 */
            JMenu fileMenu = new JMenu("메뉴");
            JMenuItem start = new JMenuItem("게임 시작");
            JMenuItem restart = new JMenuItem("게임 재시작");
            JMenuItem rule = new JMenuItem("게임 규칙 설명");
            JMenuItem exit = new JMenuItem("나가기");
            menubar.add(fileMenu);
            fileMenu.add(start);
            fileMenu.add(restart);
            fileMenu.add(rule);
            fileMenu.add(exit);
            setJMenuBar(menubar);

            JPanel gamepan = new GamePanel(); /* 게임판 */ // GamePanel에서 JPanel로 고침
            getContentPane().add(gamepan);

            turn = new JLabel("메뉴에서 게임을 시작해 주세요."); /* 누구 차례인지 */
            bscore = new JLabel(); /* 점수 */
            wscore = new JLabel();
            turn.setBounds(15, 15, 500, 15);
            bscore.setBounds(40, 15, 300, 15);
            wscore.setBounds(110, 15, 50, 15);
            getContentPane().add(turn);
            getContentPane().add(bscore);
            getContentPane().add(wscore);

            Othello = new int[8][8]; /* 2차원 배열 생성 */
            count = 0;

            start.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gamepan.setBounds(80, 40, 640, 640); /* 게임판 보여주기 */
                    gamepan.setBackground(new Color(227, 150, 107));
                }
            });

            restart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint();
                }
            });

            rule.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame m = new JFrame("게임 규칙");
                    m.setSize(600, 350);
                    m.setLocationRelativeTo(null);
                    m.getContentPane().setLayout(null);
                    m.getContentPane().setBackground(Color.WHITE);
                    m.setVisible(true);

                    JPanel panel = new JPanel();
                    panel.setBounds(0, 0, 600, 400);
                    panel.setLayout(null);
                    panel.setBackground(Color.WHITE);

                    JLabel message1 = new JLabel("처음에 판 가운데에 사각형으로 엇갈리게 배치된 돌 4개를 놓고 시작한다.\r\n");
                    message1.setBounds(15, 40, 1000, 20);
                    JLabel message2 = new JLabel("돌은 반드시 상대방 돌을 양쪽에서 포위하여 뒤집을 수 있는 곳에 놓아야 한다.\r\n");
                    message2.setBounds(15, 60, 1000, 20);
                    JLabel message3 = new JLabel("돌을 뒤집을 곳이 없는 경우에는 차례가 자동적으로 상대방에게 넘어가게 된다.\r\n");
                    message3.setBounds(15, 80, 1000, 20);
                    JLabel message4 = new JLabel("아래와 같은 조건에 의해 양쪽 모두 더 이상 돌을 놓을 수 없게 되면 게임이 끝나게 된다.\r\n");
                    message4.setBounds(15, 100, 1000, 20);
                    JLabel message5 = new JLabel("-   64개의 돌 모두가 판에 가득 찬 경우 (가장 일반적)\r\n");
                    message5.setBounds(15, 120, 1000, 20);
                    JLabel message6 = new JLabel("-   어느 한 쪽이 돌을 모두 뒤집은 경우\r\n");
                    message6.setBounds(15, 140, 1000, 20);
                    JLabel message7 = new JLabel("-   한 차례에 양 쪽 모두 서로 차례를 넘겨야 하는 경우\r\n");
                    message7.setBounds(15, 160, 1000, 20);
                    JLabel message8 = new JLabel("게임이 끝났을 때 돌이 많이 있는 플레이어가 승자가 된다. 만일 돌의 개수가 같을 경우는 무승부가 된다.");
                    message8.setBounds(15, 180, 1000, 20);
                    panel.add(message1);
                    panel.add(message2);
                    panel.add(message3);
                    panel.add(message4);
                    panel.add(message5);
                    panel.add(message6);
                    panel.add(message7);
                    panel.add(message8);
                    m.add(panel);

                    JButton confirm = new JButton("확인");
                    confirm.setBounds(255, 260, 70, 30);
                    m.add(confirm);

                    confirm.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            m.dispose();
                        }
                    });
                }
            });

            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
    }
}