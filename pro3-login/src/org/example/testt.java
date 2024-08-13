package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class testt extends JFrame implements ActionListener {

	JTextField idText = new JTextField(10);
	JTextField pwText = new JTextField(10);

	int c = 0;

	private JFrame frame;
	private final JPanel introP = new JPanel();
	private final JPanel outroP = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private final JPanel panel = new JPanel();

	private final JButton btnNewButton = new JButton("CREATE");
	private final JButton chectB = new JButton("ID 중복확인");

	private final JLabel introLB = new JLabel("Let's get start!");
	private final JLabel nameLB = new JLabel("NAME   ");
	private final JLabel birthLB = new JLabel("BIRTH DATE   ");

	private final JTextField nameTF = new JTextField();
	private final JTextField birthTF = new JTextField();
	private JTextField idTF;
	private JTextField pwTF;
	private final JTextField genderTF = new JTextField();

	// Get_String : 회원가입 때 입력한 내용들을 받는 string
	String GnameS;
	String GbirthS;
	String GnewidS;
	String GnewpwS;
	String GgenderS;

	JTable table;
	JScrollPane scrolledTable = new JScrollPane();
	String[][] content = new String[100][5];

	testt() {

		// 첫 로그인 프레임
		JPanel BPanel = new JPanel();
		BPanel.setBackground(new Color(210, 230, 185));
		BPanel.setBounds(0, 147, 386, 66);
		getContentPane().add(BPanel);

		JButton loginB = new JButton("Log in");
		loginB.setBackground(new Color(255, 255, 255));
		loginB.setFont(new Font("Arial", Font.PLAIN, 12));
		BPanel.add(loginB);

		JButton creatB = new JButton("create account");
		creatB.setBackground(new Color(255, 255, 255));
		creatB.setFont(new Font("Arial", Font.PLAIN, 12));
		BPanel.add(creatB);

		JPanel IDpanel = new JPanel();
		IDpanel.setBackground(new Color(210, 230, 185));
		IDpanel.setBounds(0, 0, 386, 149);
		getContentPane().add(IDpanel);
		IDpanel.setLayout(null);

		JPanel FidP = new JPanel();
		FidP.setBackground(new Color(210, 230, 185));
		FidP.setBounds(51, 41, 284, 31);
		IDpanel.add(FidP);

		JLabel FidL = new JLabel("ID               ");
		FidL.setFont(new Font("Arial", Font.BOLD, 12));
		FidP.add(FidL);

		idText = new JTextField();
		FidP.add(idText);
		idText.setColumns(10);

		JPanel FpwP = new JPanel();
		FpwP.setBackground(new Color(210, 230, 185));
		FpwP.setBounds(51, 78, 284, 49);
		IDpanel.add(FpwP);

		JLabel FpwL = new JLabel("Password  ");
		FpwL.setFont(new Font("Arial", Font.BOLD, 12));
		FpwP.add(FpwL);

		pwText = new JTextField();
		FpwP.add(pwText);
		pwText.setColumns(10);
		setVisible(true);

		setTitle("login procedure");
		setSize(400, 250);
		Dimension frameSize = getSize();
		Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((windowSize.width - frameSize.width) / 2, (windowSize.height - frameSize.height) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		loginB.addActionListener(listener);
		creatB.addActionListener(listener);

	}

	ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			String idt = idText.getText(); // 로그인했을 때 사용자가 작성한 text를 저장하는 string
			String pwt = pwText.getText();

			if (e.getActionCommand() == "Log in") {

				int l = 0;

				if (idt.equals("")) {
					JOptionPane.showMessageDialog(null, "아이디를 입력하세요");
				} else if (pwt.equals("")) {
					JOptionPane.showMessageDialog(null, "비밀번호를 입력하세요");
				}

				try {
					Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1", "root",
							"2222"); // db에 연결하기 위한 connection 객체 생성
					Statement statement = connection.createStatement(); // SQL문을 실행하기 위한 statement 객체 생성
					ResultSet resultSet = statement.executeQuery("SELECT * FROM tb"); // tb테이블에서 모든 값을 가져오기 위해 selcet문
					// 실행, 결과를 ResultSet객체에 저장

					while (resultSet.next()) {

						String id = resultSet.getString("id"); // 현재 행의 'id'열 값을 가져와서 id 변수에 할당. 문자열 형태로 가져옴

						if (idt.equals(id)) { // text와 DB의 id가 같으면
							String pw = resultSet.getString("pw"); // DB에서 pw정보를 가져옴

							if (idt.equals("admin")) {
								JOptionPane.showMessageDialog(null, "관리자 로그인 성공");
								l = 1;
								admin();
							} else {

								if (pwt.equals(pw)) { // text와 DB의 pw가 같으면
									JOptionPane.showMessageDialog(null, "로그인 성공");
									l = 1;
									initialize(idt); // 로그인 성공 후 새로운 창으로 이동 / idt : 사용자가 작성하고 로그인에 성공한 id

								}
							}
						}
					}

					if (l != 1) {
						JOptionPane.showMessageDialog(null, "로그인 실패");
					}

					resultSet.close(); // 객체 닫기. 리소스 누수를 방지하기 위함
					statement.close();
					connection.close();

				} catch (SQLException ex) {
					ex.printStackTrace();
				}

			}

			else if (e.getActionCommand() == "create account") {

				// 회원가입 프레임
				genderTF.setColumns(10);
				birthTF.setColumns(10);
				nameTF.setColumns(10);

				frame = new JFrame();
				frame.setBounds(100, 100, 273, 332);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().setLayout(null);
				frame.setVisible(true);

				introP.setBounds(0, 0, 259, 52);
				frame.getContentPane().add(introP);

				introP.setLayout(null);
				introLB.setHorizontalAlignment(SwingConstants.CENTER);
				introLB.setBounds(0, 0, 259, 52);
				introLB.setFont(new Font("Arial", Font.BOLD, 13));

				introP.add(introLB);
				outroP.setBounds(0, 252, 259, 33);

				frame.getContentPane().add(outroP);
				outroP.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

				outroP.add(btnNewButton);
				panel_2.setBounds(0, 62, 259, 150);

				frame.getContentPane().add(panel_2);
				panel_2.setLayout(new GridLayout(6, 0, 0, 0));
				nameLB.setHorizontalAlignment(SwingConstants.RIGHT);

				panel_2.add(nameLB);

				panel_2.add(nameTF);
				birthLB.setHorizontalAlignment(SwingConstants.RIGHT);

				panel_2.add(birthLB);

				panel_2.add(birthTF);

				JLabel genderLB = new JLabel("GENDER   ");
				genderLB.setHorizontalAlignment(SwingConstants.RIGHT);
				panel_2.add(genderLB);

				panel_2.add(genderTF);

				JLabel idLB = new JLabel("NEW ID   ");
				idLB.setHorizontalAlignment(SwingConstants.RIGHT);
				panel_2.add(idLB);

				idTF = new JTextField();
				panel_2.add(idTF);
				idTF.setColumns(10);

				panel_2.add(panel);
				chectB.setVerticalAlignment(SwingConstants.TOP);
				chectB.setHorizontalAlignment(SwingConstants.RIGHT);
//              chectB.setPreferredSize(new Dimension(50,20));

				panel.add(chectB);

				JLabel lblNewLabel = new JLabel("아이디를 입력하세요");
				lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
				lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 11));
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				panel_2.add(lblNewLabel);

				JLabel pwLB = new JLabel("NEW PASSWORD   ");
				pwLB.setHorizontalAlignment(SwingConstants.RIGHT);
				panel_2.add(pwLB);

				pwTF = new JTextField();
				panel_2.add(pwTF);
				pwTF.setColumns(10);

				chectB.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						GnewidS = idTF.getText(); // GnewidS : 아이디 text field의 값
						System.out.println("TFid : " + GnewidS);

						if (GnewidS.length() < 4) {
							lblNewLabel.setText("4자 이상 입력하세요");
						} else {

							try {
								Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1",
										"root", "2222"); // db에 연결하기 위한 connection 객체 생성
								Statement statement = connection.createStatement(); // SQL문을 실행하기 위한 statement 객체 생성
								ResultSet resultSet = statement.executeQuery("SELECT * FROM tb"); // tb테이블에서 모든 값을
																									// 가져오기위해 selcet문
																									// 실행, 결과를
																									// ResultSet객체에 저장

								while (resultSet.next()) {

									String id = resultSet.getString("id"); // 현재 행의 'id'열 값을 가져와서 id 변수에 할당. 문자열 형태로 가져옴
									System.out.println("DBid : " + id);

									if (GnewidS.equals(id)) {
										System.out.println("no");
										lblNewLabel.setText("중복된 아이디입니다");
										lblNewLabel.setForeground(Color.RED);
										break;
									} else if (!GnewidS.equals(id)) {
										lblNewLabel.setText("사용가능한 아이디입니다");
										lblNewLabel.setForeground(Color.BLUE);
										c = 1;
									}

								}

								resultSet.close(); // ResultSet 객체 닫기. 리소스 누수를 방지하기 위함
								statement.close();
								connection.close();

							} catch (SQLException ex) {
								ex.printStackTrace();
							}
						}

					}
				});

				btnNewButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub

						GnameS = nameTF.getText();
						GbirthS = birthTF.getText();
						GnewidS = idTF.getText();
						GnewpwS = pwTF.getText();
						GgenderS = genderTF.getText();

						if (GnewpwS.length() < 4) {
							JOptionPane.showMessageDialog(null, "비밀번호를 4자 이상 입력하세요");
						} else if (Pattern.matches("^[a-zA-Z]*$", GnewpwS)) {
							JOptionPane.showMessageDialog(null, "비밀번호에 숫자를 포함하세요");
						}

						else {

							if (c == 1) {

								// 새로운 값 저장
								try {

									Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1",
											"root", "2222");
									String result = " INSERT INTO tb(id, pw, name, birth, gender) "
											+ " VALUES(?, ?, ?, ?, ?) ";
									PreparedStatement stmt = conn.prepareStatement(result);

									stmt.setString(1, GnewidS); // 첫번째 물음표 : idS값 넣기
									stmt.setString(2, GnewpwS); // 두번째 물음표 : pwS값 넣기
									stmt.setString(3, GnameS);
									stmt.setString(4, GbirthS);
									stmt.setString(5, GgenderS);

									stmt.executeUpdate();
									System.out.println("success");
									JOptionPane.showMessageDialog(null, "가입 성공");
									frame.dispose();

								} catch (SQLException ex) {
									System.out.println("SQLException" + ex);
								}

							} else {
								JOptionPane.showMessageDialog(null, "ID 중복 확인이 필요합니다");
							}

						}

					}

				});

			}

		}
	};

	// 로그인 후 정보수정 창
	void subChangeFrame(String LoginId) { // LoginId : 사용자가 입력하고, 로그인에 성공한 id

		JFrame Sframe;
		JPanel SintroP = new JPanel();
		JPanel SoutroP = new JPanel();
		JButton ScomfirmB = new JButton("확인");
		JLabel SintroLB = new JLabel("Changing information");
		JPanel Spanel_2 = new JPanel();
		JTextField SnameTF = new JTextField(GnameS);
		JLabel SnameLB = new JLabel("NAME   ");
		JLabel SbirthLB = new JLabel("BIRTH DATE   ");
		JTextField SbirthTF = new JTextField(GbirthS);
		JTextField SidTF = new JTextField("변경불가");
		SidTF.setEditable(false);
		JTextField SpwTF;
		JTextField SgenderTF = new JTextField(GgenderS);
		JPanel genderPanel = new JPanel();

		SgenderTF.setColumns(10);
		SbirthTF.setColumns(10);
		SnameTF.setColumns(10);

		Sframe = new JFrame();
		Sframe.setBounds(100, 100, 273, 332);
		Sframe.getContentPane().setLayout(null);
		Sframe.setVisible(true);

		SintroP.setBounds(0, 0, 259, 52);
		Sframe.getContentPane().add(SintroP);

		SintroP.setLayout(null);
		SintroLB.setHorizontalAlignment(SwingConstants.CENTER);
		SintroLB.setBounds(0, 0, 259, 52);
		SintroLB.setFont(new Font("Arial", Font.BOLD, 13));

		SintroP.add(SintroLB);
		SoutroP.setBounds(0, 252, 259, 33);

		Sframe.getContentPane().add(SoutroP);
		SoutroP.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		SoutroP.add(ScomfirmB);
		Spanel_2.setBounds(0, 62, 259, 150);

		Sframe.getContentPane().add(Spanel_2);
		Spanel_2.setLayout(new GridLayout(5, 0, 0, 0));
		SnameLB.setHorizontalAlignment(SwingConstants.RIGHT);

		Spanel_2.add(SnameLB);

		Spanel_2.add(SnameTF);
		SbirthLB.setHorizontalAlignment(SwingConstants.RIGHT);

		Spanel_2.add(SbirthLB);

		Spanel_2.add(SbirthTF);

		JLabel SgenderLB = new JLabel("GENDER   ");
		SgenderLB.setHorizontalAlignment(SwingConstants.RIGHT);
		Spanel_2.add(SgenderLB);

		Spanel_2.add(SgenderTF);

		JLabel SidLB = new JLabel("ID   ");
		SidLB.setHorizontalAlignment(SwingConstants.RIGHT);
		Spanel_2.add(SidLB);

		Spanel_2.add(SidTF);
		SidTF.setColumns(10);

		JLabel SpwLB = new JLabel("PASSWORD   ");
		SpwLB.setHorizontalAlignment(SwingConstants.RIGHT);
		Spanel_2.add(SpwLB);

		SpwTF = new JTextField();
		Spanel_2.add(SpwTF);
		SpwTF.setColumns(10);

		// 확인 -> 정보수정
		ScomfirmB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String sGnameS = SnameTF.getText();
				String sGbirthS = SbirthTF.getText();
				String sGnewpwS = SpwTF.getText();
				String sGgenderS = SgenderTF.getText();

				try {
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1", "root", "2222");
					String query = "UPDATE tb SET pw = ?, name = ?, birth = ?, gender = ? WHERE id = ?";
					PreparedStatement stmt = conn.prepareStatement(query);
					ResultSet resultSet = stmt.executeQuery("SELECT * FROM tb");

					if (sGnewpwS.length() < 4) {
						JOptionPane.showMessageDialog(null, "비밀번호를 4자 이상 입력하세요");
					} else if (Pattern.matches("^[a-zA-Z]*$", sGnewpwS)) {
						JOptionPane.showMessageDialog(null, "비밀번호에 숫자를 포함하세요");
					}

					else {
						while (resultSet.next()) {
							String id = resultSet.getString("id");

							if (LoginId.equals(id)) {

								stmt.setString(1, sGnewpwS);
								stmt.setString(2, sGnameS);
								stmt.setString(3, sGbirthS);
								stmt.setString(4, sGgenderS);
								stmt.setString(5, id);
							}
						}

						stmt.executeUpdate();

						resultSet.close();
						stmt.close();
						conn.close();

						JOptionPane.showMessageDialog(null, "정보 변경 완료");
						Sframe.dispose();
					}

				} catch (SQLException ex) {
					ex.printStackTrace();
				}

			}

		});
	}

	void initialize(String LoginId) { // LoginId : 사용자가 입력하고, 로그인에 성공한 id

		// 로그인 후 창 생성
		JFrame frame;
		JPanel Mpanel = new JPanel();
		JPanel panel_2 = new JPanel();
		JPanel panel_3 = new JPanel();

		frame = new JFrame();
		frame.setBounds(100, 100, 273, 332);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setVisible(true);

		frame.getContentPane().add(Mpanel, BorderLayout.CENTER);
		Mpanel.setLayout(null);
		Mpanel.setBackground(Color.WHITE);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(204, 204, 255));
		panel_1.setBounds(12, 10, 235, 85);
		Mpanel.add(panel_1);
		panel_1.setLayout(null);

		JButton CIbutton = new JButton("Changing information");
		CIbutton.setBackground(new Color(235, 220, 250));
		CIbutton.setFont(new Font("Arial", Font.BOLD, 13));
		CIbutton.setBounds(0, 0, 235, 85);
		panel_1.add(CIbutton);
		panel_2.setBounds(12, 105, 235, 85);

		CIbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				subChangeFrame(LoginId);
			}

		});

		Mpanel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JButton DAbutton = new JButton("Delete account");
		DAbutton.setBackground(new Color(250, 220, 220));
		DAbutton.setFont(new Font("Arial", Font.BOLD, 13));
		panel_2.add(DAbutton);
		panel_3.setBounds(12, 200, 235, 85);

		DAbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int answer = JOptionPane.showConfirmDialog(frame, "탈퇴하시겠습니까?", "confirm", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					frame.dispose();

					try {
						Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1", "root",
								"2222");
						String query = "DELETE FROM tb WHERE id = ?";
						PreparedStatement stmt = conn.prepareStatement(query);
						stmt.setString(1, LoginId);
						stmt.executeUpdate();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}

				} else {
					System.out.println("취소");
				}
			}

		});

		Mpanel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JButton LObutton = new JButton("Log out");
		LObutton.setBackground(new Color(250, 230, 180));
		LObutton.setFont(new Font("Arial", Font.BOLD, 13));
		panel_3.add(LObutton);

		LObutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int answer = JOptionPane.showConfirmDialog(frame, "로그아웃하시겠습니까?", "confirm", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					frame.dispose();
				}
			}

		});

	}

	void admin() {

		JFrame ADframe;
		JPanel Mpanel = new JPanel();
		JPanel panel_2 = new JPanel();
		JPanel panel_3 = new JPanel();

		ADframe = new JFrame();
		ADframe.setBounds(100, 100, 273, 332);
		ADframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ADframe.getContentPane().setLayout(new BorderLayout(0, 0));
		ADframe.setVisible(true);

		ADframe.getContentPane().add(Mpanel, BorderLayout.CENTER);
		Mpanel.setLayout(null);
		Mpanel.setBackground(Color.WHITE);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(204, 204, 255));
		panel_1.setBounds(12, 10, 235, 85);
		Mpanel.add(panel_1);
		panel_1.setLayout(null);

		JButton CIbutton = new JButton("View user information");
		CIbutton.setBackground(new Color(235, 220, 250));
		CIbutton.setFont(new Font("Arial", Font.BOLD, 15));
		CIbutton.setBounds(0, 0, 235, 85);
		panel_1.add(CIbutton);
		panel_2.setBounds(12, 105, 235, 85);

		Mpanel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JButton DAbutton = new JButton("Show chart");
		DAbutton.setBackground(new Color(250, 220, 220));
		DAbutton.setFont(new Font("Arial", Font.BOLD, 15));
		panel_2.add(DAbutton);
		panel_3.setBounds(12, 200, 235, 85);

		CIbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int count = 0;

				String[] header = { "이름", "생년월일", "성별", "아이디", "비밀번호" };
				

				try {

					Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1", "root",
							"2222");
					Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery("SELECT * FROM test1.tb");

					

					while (resultSet.next()) {

						String getName = resultSet.getString("name");
						String getBirth = resultSet.getString("birth");
						String getGender = resultSet.getString("gender");
						String getId = resultSet.getString("id");
						String getPw = resultSet.getString("pw");

//						System.out.println(getBirth);

						content[count][0] = getName;
						content[count][1] = getBirth;
						content[count][2] = getGender;
						content[count][3] = getId;
						content[count][4] = getPw;

						System.out.println(Arrays.toString(content[count]));
						count++;
					}

					resultSet.close();
					statement.close();
					connection.close();

//					System.out.println(table);

				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				table = new JTable(content, header);
				scrolledTable.setViewportView(table);
				
				frame = new JFrame();
				frame.setBounds(100, 100, 480, 550);
				frame.getContentPane().setLayout(null);

				JPanel infoP = new JPanel();
				infoP.setBounds(0, 0, 460, 515);
//				infoP.setPreferredSize(new Dimension(460, 515));
				frame.getContentPane().add(infoP);
				infoP.setLayout(null);

				JPanel textP = new JPanel();
				textP.setBounds(10, 10, 450, 450);
//				textP.setPreferredSize(new Dimension(450, 450));
				infoP.add(textP);

				textP.add(table);
				textP.add(scrolledTable);

				JPanel deleteP = new JPanel();
				deleteP.setBounds(10, 465, 445, 40);
				infoP.add(deleteP);

				JButton deleteB = new JButton("계정삭제");
				deleteP.add(deleteB);

				frame.setVisible(true);

				deleteB.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub

						int n = 0;

						String s = JOptionPane.showInputDialog(null, "삭제할 사용자의 이름을 입력하세요", "delete user account",
								JOptionPane.YES_NO_OPTION);

						if (s == null) {
							JOptionPane.showMessageDialog(null, "사용자 이름을 입력하세요");
						}

						else {

							if (s.equals("관리자")) {
								JOptionPane.showMessageDialog(null, "관리자 계정은 삭제할 수 없습니다");
							} else {

								try {

									Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1",
											"root", "2222");
									String query = "DELETE FROM tb WHERE name = ?";
									PreparedStatement stmt = conn.prepareStatement(query);

									ResultSet resultSet = stmt.executeQuery("SELECT name FROM tb");

									while (resultSet.next()) {

										String Dname = resultSet.getString("name");

										if (s.equals(Dname)) {

											stmt.setString(1, s);
											stmt.executeUpdate();

											JOptionPane.showMessageDialog(null, "\"" + s + "\"" + "  계정이 삭제되었습니다");
											frame.dispose();
											n = 1;
										}

									}

									if (n != 1) {
										JOptionPane.showMessageDialog(null, "\"" + s + "\"" + "  는 등록되지 않은 사용자 이름입니다");
									}

									stmt.close();
									resultSet.close();

								} catch (SQLException ex) {
									ex.printStackTrace();
								}

							}

						}
					}

				});

			}

		});

		DAbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		testt db = new testt();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
