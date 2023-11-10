import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ArcadeServer extends JFrame {
	private BufferedReader in = null;
	private BufferedWriter out = null;
	private ServerSocket listener = null; // 서버 소켓
	private Socket socket = null; // 클라이언트 소켓 (클라.accept로 생성된 소켓)
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	
	private JPanel contentPane;
	private JTextArea textArea;
	private JTextField txtPortNumber;
	
	int width = 600;
	int height = 600;
	int port = 9999;
	int clientId = 0;
	
	public static void main(String[] args) {
		ArcadeServer server = new ArcadeServer();
	}
	

	
	public ArcadeServer() { //생성자
		//ArcadeServer server = this;
		setTitle("서버 로그");
		setLocation(0,0);
		setSize(width,height);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 서버 정보 라벨
		JLabel serverInfoLabel = new JLabel();
		serverInfoLabel.setSize(width,100);
		serverInfoLabel.setLocation(10,10);
		serverInfoLabel.setText("<html>"
				+ "<h1>크레이지아케이드 서버</h1><hr/>"
				+ "<p>서버 실행을 담당하고 클라이언트 정보를 얻어옵니다.</p>"
				+ "<p>서버는 9999 포트에서 시작됩니다.</p>"
				+ "</html>");
		add(serverInfoLabel);
		
		// 서버 및 클라이언트 정보를 기록할 텍스트 영역
		textArea = new JTextArea();
		textArea.setLocation(10,120);
		textArea.setSize(width-50,height-280);
		textArea.setEditable(false);
		add(textArea);
		
		// 서버 실행 버튼
		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// 서버 소켓 생성
				try {
					listener = new ServerSocket(port);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				AppendText("서버가 "+port+" 포트에서 실행중입니다.");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				
				// 서버 스레드 수행, 클라이언트 접속을 waiting..
				ServerThread serverThread = new ServerThread();
				serverThread.start();
			}
		});
		btnServerStart.setSize(300,50);
		btnServerStart.setLocation(width/2 - 300/2,height-110);
		this.add(btnServerStart);
		
		this.repaint();
	}
	
	void AppendText(String s) {
		textArea.append(s + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	class ServerThread extends Thread{
		@Override
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("클라이언트 접속을 대기중입니다.");
					socket = listener.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + socket);
					
					// User 당 하나씩 수신 할 수 있는 Thread 생성
					clientId++;
					ServerReceiver new_user = new ServerReceiver(socket, clientId);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 동접자 수 :" + UserVec.size());
					
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
		
		class ServerReceiver extends Thread {// User 스레드
			private BufferedReader in;
			private BufferedWriter out;
			private Socket clientSocket;
			private int clientId;
			private Vector user_vc;
			public String UserName = "";
			public String UserStatus;
	
			public ServerReceiver(Socket s, int clientId) { 
				this.clientSocket = s;
				clientId = clientId;
				this.user_vc = UserVec;
				try {
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					out.write("클라이언트 "+clientId+ " 접속");
					out.flush();
				} catch (Exception e) {
					AppendText("에러");
				}
			}
			
			@Override
			public void run() {
				String msg = null;
				try {
					msg = in.readLine();
				} catch (Exception e) {
					AppendText("에러");
				}
				AppendText(msg);
			}
		}

	}
}