import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientLobby extends JFrame {
	public static ClientLobby instance = null;
	public ClientGamePlay gameInstance = null;
	
	public static ClientLobby getInstance() { return instance; }
	
	private BufferedReader in;
	public BufferedWriter out;
	private Socket socket;
	public String username;
	public String userId;
	public String selectedCharacter;
	public List<String> selectedCharacterList = new ArrayList<>();
	public String selectedMap;
	public int clientId;
	
	private ImageIcon bg = new ImageIcon("./LobbyImages/LobbyBackground.png"); //배경화면
	private ImageIcon chatPanel = new ImageIcon("./LobbyImages/chatingPanel.png"); // 대화창 패널
	private ImageIcon room = new ImageIcon("./LobbyImages/emptyRoom.png"); //방
	private ImageIcon chatBox = new ImageIcon("./LobbyImages/chatBox.png"); // 대화창 내부
	private ImageIcon roomList = new ImageIcon("./LobbyImages/roomList.png"); // 방 리스트 패널
	private int width = bg.getIconWidth();
	private int height = bg.getIconHeight();
	private JTextArea textArea;
	private JLabel backgroundLabel;
	
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
		setSize(width,height+30);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 배경
		backgroundLabel = new JLabel(bg);
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
		
		// 로비 텍스트 쌓일 공간
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setSize(chatBox.getIconWidth()-20,chatBox.getIconHeight()-50);
		textArea.setLocation(40,50);
		chatPanelLabel.add(textArea);
		
		// 대화창 내부 입력창
		JTextField inputField = new JTextField();
		inputField.setSize(chatBox.getIconWidth()-100,20);
		inputField.setLocation(40,140);
		chatPanelLabel.add(inputField);
		chatPanelLabel.setComponentZOrder(inputField, 0);
		
		// 전송
		JButton sendBtn = new JButton("전송");
		sendBtn.setSize(80,20);
		sendBtn.setLocation(chatBox.getIconWidth()-60,140);
		chatPanelLabel.add(sendBtn);
		chatPanelLabel.setComponentZOrder(sendBtn, 0);
		sendBtn.addMouseListener(new MouseAdapter(){ // 액션 이벤트 처리	
			public void mouseEntered(MouseEvent e) {
		    	SoundManager.getInstance().playSound(SoundManager.SoundEnum.button_mouseOn); // 마우스 올리면 실행할 사운드
		    }
			@Override
			public void mouseClicked(MouseEvent e) {
				SoundManager.getInstance().playSound(SoundManager.SoundEnum.button_click);
				sendMsgToServer(inputField.getText());
			}
		});
		
		receiver = new ServerReceiver();
		receiver.start();

		try {
			out.write("1/"+userId+"/"+username+"/-1/"+"\n");
			out.flush();
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
			out.write("200/"+userId+"/"+username+"/"+username+"/"+msg+"\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	void AppendText(String s) {
		textArea.append(s + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
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
				
				System.out.println(msg);
				
				switch(msgType) {
				case 0:
					clientId = Integer.parseInt(msgContent);
					break;
				case 1: // 플레이어가 방 생성시, 서버에서 클라로 해당 방이 만들어졌음을 알린다
					roomVector.elementAt(Integer.parseInt(msgContent)).roomCreate(userName);
					break;
				case 2: // 플레이어가 대기실 방에 입장 시, 대기실 방에 유저 정보 업데이트, 서버가 클라이언트에게 클라이언트가 대기실에 입장함을 알림
					for(int i=0;i<8;i++) {
						roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).userName = msg.split("/")[4].split(",")[i];
						roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).isReady = (msg.split("/")[5].split(",")[i].equals("true"))?true:false;
					}
					roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.refreshWaitingRoom();
					break;
				case 3: // 서버가 클라이언트에게 모든 클라이언트의 준비 여부를 알린다
					int userCount = 0; // 대기방 유저수
					int readyUserCount = 0; // 준비 완료 한 유저 수
					//selectedCharacter = msg.split("/")[6]; //
					
					for(int i=0;i<8;i++) {
						if(!msg.split("/")[4].split(",")[i].equals("-"))userCount++;
						System.out.println(roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).userName);
						roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).userName = msg.split("/")[4].split(",")[i];
						roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).isReady = (msg.split("/")[5].split(", ")[i].equals("true"))?true:false;
						
						// 대기방 유저 상태 변경 - 준비 상태인지 아닌지
						if(roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).isReady) {
							roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).userReady(true);
							readyUserCount++;
						}
						else {
							roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.waitUsers.elementAt(i).userReady(false);
						}
					}
					System.out.println(userCount+", "+readyUserCount);
					if(userCount == readyUserCount) roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.SetStartBtnAble();
					else roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.SetStartBtnDisable();
					break;
				case 4: // 서버가 클라이언트에게 게임 시작 명령을 보내는 경우, 이를 처리한다
					// 클라이언트 id가 일치하는 경우, 게임 시작
					if(Integer.parseInt(msg.split("/")[4])==clientId)
						roomVector.elementAt(Integer.parseInt(msgContent)).waitingRoom.gamePlay();
					break;
				case 5: // 서버가 클라이언트에게 게임 시작 시 정보 전송
					int userCounts = Integer.parseInt(msgContent);
					
					selectedCharacter = msg.split("/")[6]; //
					selectedCharacterList = Arrays.asList(selectedCharacter.split(","));
					// selectedMap = msg.split("/")[7]; // 일단 맵은 나중에 추가하는거로..! - 현우
					List<String> userNames = Arrays.asList(msg.split("/")[8].split(","));
					gameInstance.CharacterCreate(userCounts,msg.split("/")[4],msg.split("/")[5],selectedCharacterList, userNames);
					break;
				case 6: // 서버가 클라이언트에게 캐릭터의 좌표, x,y를 불러온다
					gameInstance.UpdateCharacterVector(
							msg.split("/")[3], 
							msg.split("/")[4], 
							Integer.parseInt(msg.split("/")[5]), 
							Integer.parseInt(msg.split("/")[6])
						);
					break;
				case 7: // 폭탄 생성
					gameInstance.SetBomb(Integer.parseInt(msg.split("/")[3]), Integer.parseInt(msg.split("/")[4]), Integer.parseInt(msg.split("/")[5]));
					break;
				case 100: // 게임 내 맵 배열 업데이트
					gameInstance.DeleteBlockInMap(Integer.parseInt(msg.split("/")[3]), Integer.parseInt(msg.split("/")[4]));
					break;
				case 101: // 플레이어 폭탄 피격당했는지 검사
					gameInstance.isCharacterAttacked(Integer.parseInt(msg.split("/")[3]),Integer.parseInt(msg.split("/")[4]));
					break;
				case 102: // 플레이어가 사망했다는 메시지 전송
					gameInstance.CharacterDied(msg.split("/")[2],Integer.parseInt(msg.split("/")[3]),Integer.parseInt(msg.split("/")[4]));
					break;
				case 103: // 결과창
					gameInstance.ResultOpen(msg.split("/")[3],msg.split("/")[4]);
					break;
				case 104: // 아이템
					gameInstance.SpawnItem(Integer.parseInt(msg.split("/")[3]),Integer.parseInt(msg.split("/")[4]),Integer.parseInt(msg.split("/")[5]));
					break;
					
				case 200: // 로비 채팅방
					AppendText(msg.split("/")[3]+ " : " +msg.split("/")[4]);
					break;
				default:
					break;
				}
			}

		}
	    
	}
}
