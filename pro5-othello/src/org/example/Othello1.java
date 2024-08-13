package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Othello1 extends JPanel {

    static Othello1 gameP;

    boolean[][] grid; // 그리드의 셀 상태를 저장하는 배열
    boolean[][] able; // 돌을 놓을 수 있는 곳인지 확인하는 배열
    int[][] array; // 색을 기억하는 배열
    JLabel wscoreL, bscoreL; // 점수라벨
    int w = 0, b = 0; // 점수

    int circleX = -20; // 원의 중심
    int circleY = -20;
    boolean colorb = true;

    int row, col;
    int[][] Rp;

    public Othello1() {

        subFrame();

        grid = new boolean[8][8]; // 8x8 그리드
        able = new boolean[8][8];
        array = new int[8][8]; // 색을 기억하는 배열

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

                int cellWidth = gameP.getWidth() / 8;
                int cellHeight = gameP.getHeight() / 8;

                row = e.getY() / cellHeight; // 클릭한 위치의 행 인덱스
                col = e.getX() / cellWidth;

                // checkable
                int[][] directionss = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
                        { 1, 1 } };
                int co;
                if (colorb) {
                    co = 1; // 검정 = 1
                } else {
                    co = 2;
                }

                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        boolean isAble = false; // 각 위치별로 초기값을 false로 설정

                        if (array[row][col] == 0) { // 해당 위치가 비어있는 경우에만 검사 수행
                            for (int[] direction : directionss) {
                                int dx = direction[0];
                                int dy = direction[1];

                                int nextRow = row + dx;
                                int nextCol = col + dy;

                                while (nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8) {
                                    if (co == 1) {
                                        if (array[nextRow][nextCol] == 2) {
                                            isAble = true;
                                            break;
                                        } else if (array[nextRow][nextCol] == 1) {
                                            break;
                                        }
                                    } else if (co == 2) {
                                        if (array[nextRow][nextCol] == 1) {
                                            isAble = true;
                                            break;
                                        } else if (array[nextRow][nextCol] == 2) {
                                            break;
                                        }
                                    }

                                    nextRow += dx;
                                    nextCol += dy;
                                }

                                if (isAble) {
                                    break; // 이미 가능한 경우이므로 추가 검사는 불필요
                                }
                            }
                        }

                        able[row][col] = isAble; // 해당 위치의 able 값을 설정
                    }
                }


                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        System.out.printf("%3s ", able[i][j]);
                    }
                    System.out.println();
                }
                System.out.println();

                if (able[row][col]) {

                    if (row >= 0 && row < 8 && col >= 0 && col < 8) {

                        circleX = col * cellWidth + (cellWidth / 2); // 원의 중심 X 좌표 계산
                        circleY = row * cellHeight + (cellHeight / 2); // 원의 중심 Y 좌표 계산
                        grid[row][col] = true; // 클릭한 셀을 true로 설정

                        if (colorb) {
                            array[row][col] = 1; // 색을 1로 설정 (검정색)
                            repaint();
                        } else {
                            array[row][col] = 2; // 색을 2로 설정 (흰색)
                            repaint();
                        }

                        colorb = !colorb; // 색을 번갈아가며 변경

                        // check
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
                } else {
                    System.out.println("cannot");
                }

                boolean zero = false;
                for (int i = 0; i < 8; i++) { // 색상 정보에 따라 점수 업데이트
                    for (int j = 0; j < 8; j++) {
                        System.out.printf("%3d", array[i][j]);
                        if (array[i][j] == 0) {
                            zero = true;
                        }
                    }
                    System.out.println();
                }
                System.out.println();
                if (!zero) {
                    JOptionPane.showMessageDialog(null, "게임이 종료되었습니다");
                }
            }

        });
    }

//	void check(int row, int col) {
//
//		int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
//
//		for (int[] direction : directions) {
//			int dx = direction[0];
//			int dy = direction[1];
//
//			int nextRow = row + dx;
//			int nextCol = col + dy;
//
//			while (nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8) {
//				if (array[nextRow][nextCol] == 0) {
//					break;
//				} else if (array[nextRow][nextCol] == array[row][col]) {
//					for (int i = row, j = col; i != nextRow || j != nextCol; i += dx, j += dy) {
//						array[i][j] = array[row][col];
//					}
//					break;
//				}
//
//				nextRow += dx;
//				nextCol += dy;
//			}
//		}
//	}

//	void checkAble() {
//
//		int[][] directionss = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
//
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 8; j++) {
//
//				if (array[i][j] != 0) {
//
//					for (int[] direction : directionss) {
//						int dx = direction[0];
//						int dy = direction[1];
//
//						int nextRow = i + dx;
//						int nextCol = j + dy;
//
//						while (nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8) {
//							if (array[nextRow][nextCol] == 0) {
//								break;
//							} else if (array[nextRow][nextCol] == array[row][col]) {
//								for (int a = row, b = col; a != nextRow || b != nextCol; a += dx, b += dy) {
//									able[a][b] = true;
//								}
//								break;
//							}
//
//							nextRow += dx;
//							nextCol += dy;
//						}
//					}
//
//				}
//				able[i][j] = false;
//			}
//		}
//
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 8; j++) {
//				System.out.printf("%3d", able[i][j]);
//			}
//			System.out.println();
//		}
//		System.out.println();
//
//	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellWidth = gameP.getWidth() / 8; // 각 셀의 너비
        int cellHeight = gameP.getHeight() / 8; // 각 셀의 높이

        // 판 그리기
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int x = j * cellWidth;
                int y = i * cellHeight;

//				if (grid[i][j]) {
//					if (array[i][j] == 1) {
//						g.setColor(Color.black);
//						g.fillOval(x, y, cellWidth, cellHeight);
//					} else if (array[i][j] == 2) {
//						g.setColor(Color.white);
//						g.fillOval(x, y, cellWidth, cellHeight);
//					}
//				}

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

    public static void main(String[] args) {

        JFrame frame = new JFrame("Othello Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        gameP = new Othello1();
        gameP.setBackground(new Color(200, 230, 255));
        frame.getContentPane().add(gameP);

        frame.setVisible(true);
    }
}