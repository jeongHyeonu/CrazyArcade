import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class ClientLobby extends JFrame {
	public static ClientLobby instance = null;
	public ClientGamePlay gameInstance = null;
	
	public static ClientLobby getInstance() { return instance; }
	
	private BufferedReader in;
	private BufferedWriter out;
	private Socket socket;
	private String username;
	private String userId;
	
	private ImageIcon bg = new ImageIcon("./LobbyImages/LobbyBackground.png"); //배경화면
	private ImageIcon chatPanel = new ImageIcon("./LobbyImages/chatingPanel.png"); // 대화창 패널
	private ImageIcon room = new ImageIcon("./LobbyImages/emptyRoom.png"); //방
	private ImageIcon chatBox = new ImageIcon("./LobbyImages/chatBox.png"); // 대화창 내부
	private ImageIcon roomList = new ImageIcon("./LobbyImages/roomList.png"); // 방 리스트 패널
	private int width = bg.getIconWidth();
	private int height = bg.getIconHeight()+30;
	private JTextArea textArea;
	
	private Vector<ClientRoom> roomVector = new Vector<ClientRoom>();
	private ServerReceiver receiver;
	
	public ClientLobby(String username, String userId) {
		this.instance = this;
		this.username = username;
		this.userId = userId;
		
		try {
			socket = new Socket("localhost", 9999); // 클라이언트 소켓 생성
			// 클라이언트로부터의 입출력 스트림
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // 클라이언트로의 출력 스트림
		} catch (IOException e) {
			System.out.println("서버 접속 실패 : 서버가 켜져있는지 다시 확인해 주세요!");
			setVisible(false);
		} 
		
		
		setTitle("크레이지아케이드 - 대기실");
		setLocation(0,0);
		setSize(width,height);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 배경
		JLabel backgroundLabel = new JLabel(bg);
		backgroundLabel.setSize(width,height);
		backgroundLabel.setLocation(0,0);
		add(backgroundLabel);
		
		// 방 리스트 패널
		JLabel roomListLabel = new JLabel(roomList);
		roomListLabel.setSize(roomList.getIconWidth(),roomList.getIconHeight());
		roomListLabel.setLocation(30,30);
		backgroundLabel.add(roomListLabel);
		
		// 방 생성
		for(int i=0;i<6;i++) {
			ClientRoom emptyRoom = new ClientRoom(room,i,in,out,username,userId,this);
			roomListLabel.add(emptyRoom);
			roomVector.add(emptyRoom);
		}
		
		// 대화창 패널
		JLabel chatPanelLabel = new JLabel(chatPanel);
		chatPanelLabel.setSize(chatPanel.getIconWidth(),chatPanel.getIconHeight());
		chatPanelLabel.setLocation(30,420);
		backgroundLabel.add(chatPanelLabel);
		
		// 대화창 내부 라벨
		JLabel chatBoxLabel = new JLabel(chatBox);
		chatBoxLabel.setSize(chatBox.getIconWidth(),chatBox.getIconHeight());
		chatBoxLabel.setLocation(30,40);
		chatPanelLabel.add(chatBoxLabel);
		
		// 대화창 내부 라벨 속 텍스트필드
		textArea = new JTextArea();
		textArea.setLocation(0,0);
		textArea.setSize(chatBox.getIconWidth(),chatBox.getIconHeight());
		textArea.setEditable(false);
		chatBoxLabel.add(textArea);
		
		
		receiver = new ServerReceiver();
		receiver.start();

		try {
			out.write("5/"+userId+"/"+username+"/-1/"+"\n");
			out.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		repaint();
	}
	
	private void sendMsgToServer(String msg) {
		try { // 문자열 전송
			System.out.println(msg);
			out.write("1/"+userId+"/"+username+"/"+msg+"\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	void AppendText(String s) {
		textArea.append(s + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	
	// 기존에 로비화면에 있는거 모두 없애고 새로 유저 대기방(waitRoom)오픈
	// 사용자가 방 클릭시 호출 (ClientRoom.java 의 mouseListener 에서 참조)
	public void roomOpen(int roomIndex) {
		this.removeAll();
		this.setVisible(false);
		
		// 서버에게 유저가 대기방에 들어왔음을 알린다
		try {
			out.write("6/"+userId+"/"+username+"/"+roomIndex+"\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 서버에서 클라로 정보 재전송하고 받고 다시 적용하는 시간이 있어서 (비동기적인 부분 때문에) nullReferenceException 발생
		// 원랜 synchronized 로 하는게 맞는데, 어느 부분에서 에러나는지 몰라서.. 일단은 0.1초뒤 대기방 오픈
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				(roomVector.elementAt(roomIndex).waitingRoom).userOpenWaitingRoom(username,userId,roomIndex,out);
			}
		};
		timer.schedule(task, 100);
		
	}
	
	// 서버로부터 메세지를 받아옴
	public class ServerReceiver extends Thread {
		@Override
		public synchronized void run() {
			String msg = null;
			while (true) {
				try {
					msg = in.readLine();
				} catch (Exception e) {
					break;
				}

				
				int msgType = Integer.parseInt(msg.split("/")[0]);
				String userId = msg.split("/")[1];
				String userName = msg.split("/")[2];
				String msgContent = msg.split("/")[3];
				
				switch(msgType) {
				case 1: // 플레이어가 방 생성시, 서버에서 클라로 해당 방이 만들어졌음을 알린다
					roomVector.elementAt(Integer.parseInt(msgContent)).roomCreate(userName);
					break;
				case 2: // 플레이어가 대기실 방에 입장 시, 대기실 방에 유저 정보 업데이트, 서버가 클라이언트에게 클라이언트가 대기실에 입장함을 알림
					for(int i=0;i<8;i++) {
						roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).userName = msg.split("/")[4].split(",")[i];
						roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).isReady = (msg.split("/")[5].split(",")[i].equals("true"))?true:false;
					}
					roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.represhWaitingRoom();
					break;
				case 3: // 서버가 클라이언트에게 모든 클라이언트의 준비 여부를 알린다
					System.out.println(msg);
					for(int i=0;i<8;i++) {
						roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).userName = msg.split("/")[4].split(",")[i];
						roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).isReady = (msg.split("/")[5].split(", ")[i].equals("true"))?true:false;
						
						// 대기방 유저 상태 변경 - 준비 상태인지 아닌지
						if(roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).isReady) {
							roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).userReady(true);
						}
						else {
							roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).userReady(false);
						}
					}
					break;
				case 4: // 서버가 클라이언트에게 게임 시작 명령을 보내는 경우, 이를 처리한다
					roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.gamePlay();
					break;
				default:
					break;
				}
			}

		}
	}
	
	
}
