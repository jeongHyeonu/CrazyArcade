import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ArcadeClient extends JFrame{
		
	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtIpAddress;
	private JTextField txtPortNumber;
	
	JScrollPane scrollPane;
	
	private ImageIcon bg = new ImageIcon("./LoginImages/LoginBackground.png"); //배경화면
	private ImageIcon nameLabelImg = new ImageIcon("./LoginImages/nameLabel.png"); //이름 라벨 이미지
	private ImageIcon idLabelImg = new ImageIcon("./LoginImages/idLabel.png"); //아이디 라벨 이미지
	private ImageIcon gameStartButtonImg = new ImageIcon("./LoginImages/gameStartButton.png"); //게임 시작 버튼 이미지
	
	private int width = bg.getIconWidth();
	private int height = bg.getIconHeight()+30;
	
	public static void main(String[] args) {
		ArcadeClient clientLogin = new ArcadeClient();
	}
	
	public ArcadeClient() {  // 생성자
		setTitle("크레이지아케이드 - 로그인");
		setLocation(0,0);
		setSize(width,height+30);
		setLayout(null);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 배경
		JLabel backgroundLabel = new JLabel(bg);
		backgroundLabel.setSize(width,height);
		backgroundLabel.setLocation(0,0);
		add(backgroundLabel);
		
		// 유저 이름 라벨
		JLabel userNameLabel = new JLabel(nameLabelImg);
		userNameLabel.setVisible(true);
		userNameLabel.setSize(100,30);
		userNameLabel.setLocation(290,440);
		backgroundLabel.add(userNameLabel);
		
		// 유저 이름 텍스트필드
		JTextField userNameTextField = new JTextField();
		userNameTextField.setSize(100,25);
		userNameTextField.setLocation(400,440);
		backgroundLabel.add(userNameTextField);
		
		// 아이디 라벨
		JLabel idLabel = new JLabel(idLabelImg);
		idLabel.setVisible(true);
		idLabel.setSize(100,30);
		idLabel.setLocation(290,470);
		backgroundLabel.add(idLabel);
		
		// 유저 이름 텍스트필드
		JTextField idLabelTextField = new JTextField();
		idLabelTextField.setSize(100,25);
		idLabelTextField.setLocation(400,470);
		backgroundLabel.add(idLabelTextField);
		
		//시작버튼
		JButton btnConnect = new JButton(gameStartButtonImg);
		btnConnect.setSize(gameStartButtonImg.getIconWidth(),gameStartButtonImg.getIconHeight());
		btnConnect.setLocation(width/2-gameStartButtonImg.getIconWidth()/2,520);
		backgroundLabel.add(btnConnect);
		btnConnect.addActionListener(new ActionListener(){ // 액션 이벤트 처리
			public void actionPerformed(ActionEvent e) {
				String username = userNameTextField.getText().trim();
				String ip_addr = idLabelTextField.getText().trim();
				
				ClientLobby clientLobby = new ClientLobby(username, ip_addr);
				setVisible(false);
			}
		});
		
		repaint();
	}
	

}