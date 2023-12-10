import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import java.util.List;
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

// test push
public class ArcadeServer extends JFrame {
	private BufferedReader in = null;
	private BufferedWriter out = null;
	private ServerSocket listener = null; // 서버 소켓
	private Socket socket = null; // 클라이언트 소켓 (클라.accept로 생성된 소켓)
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	
	private boolean[] rooms = {false,false,false,false,false,false}; // 이용 가능한 방 저장
	private String[] roomUserName = {"","","","","",""};
	
	// 대기실 방 내의 유저 정보
	class UserInfoWaitRoom {
		public String userName[] = {"-","-","-","-","-","-","-","-"};
		public Boolean isUserEntered[] = {false,false,false,false,false,false,false,false};
		public Boolean isReady[] = {true,false,false,false,false,false,false,false}; // 기본적으로, 방장은 준비상태 true
		public int clientId[] = {-1,-1,-1,-1,-1,-1,-1,-1}; // 클라이언트 아이디 저장
	}
	
	// 게임 내 캐릭터 속성
	class GameCharacter {
		// 클라이언트 속성
		public int clientId = -1;
		public String username;
		public BufferedWriter out;
		
		// 캐릭터 속성
		public int x;
		public int y;
		public int speed;
		public int attackRange;
		public boolean isDead;
		public int dir;
	}
	
	// 게임에서 유저 캐릭터 정보
	GameCharacter characters[] = new GameCharacter[8];
	
	
	// 대기방 목록
	private Vector<UserInfoWaitRoom> waitRoomList = new Vector<UserInfoWaitRoom>();
	
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
		
		// 대기방 생성
		waitRoomList.add(new UserInfoWaitRoom());
		waitRoomList.add(new UserInfoWaitRoom()); 
		waitRoomList.add(new UserInfoWaitRoom()); 
		waitRoomList.add(new UserInfoWaitRoom()); 
		waitRoomList.add(new UserInfoWaitRoom()); 
		waitRoomList.add(new UserInfoWaitRoom());
		
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
				}
			}
		}
		

		
		class ServerReceiver extends Thread { // 클라이언트가 보내는 정보를 받아올 리스너
			private BufferedReader in;
			private BufferedWriter out;
			private Socket clientSocket;
			public int clientId;
			private Vector user_vc;
			public String UserName = "";
			public String UserStatus;
			public int enteredRoom = -1; // 게임 시작 전 몇 번째 대기방에 입장한 상태인지 저장, -1이면 입장 안한거고, 그 이외 값이면 입장한것
			public int roomIndex = -1; // 대기방에 몇 번째로 입장한 상태인지 저장, -1이면 입장 안한거고, 그 이외 값이면 입장한것
	
			public ServerReceiver(Socket s, int clientId) {
				this.clientSocket = s;
				this.clientId = clientId;
				System.out.println(clientId);
				this.user_vc = UserVec;
				try {
					in = new BufferedReader(new InputStreamReader(s.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				} catch (Exception e) {
					AppendText("에?러");
					Logout();
				}
			}
			
			public void SendToClient(String msg) {
				try {
					out.write(msg);
					out.flush();
				}catch (IOException e) {
					AppendText("accept() error");
					Logout();
				}
			}
			
			@Override
			public void run() {

				String message = null;
				
				int msgType = 0;
				String msgContent = null;
				String userId = null;
				String userName = null;
				
				while(true) {
					
					try {
						message = in.readLine();
						if (message == null) {
							break;
						}
					} catch (Exception e) {
						Logout(); // 에러난 클라이언트는 제거
						break;
					}
					// 클라이언트로부터 메세지를 얻어온다. 이때 메세지는 타입에 따라 다른 행동을 한다.
					// 여기서 메세지는 아래 예시처럼 "//"으로 타입과 메세지를 구분한다.
					// 1은 클라이언트가 서버에 접속한 경우,  -  "1/로그인했습니다"
					// 2는 클라이언트가 채팅방에 메시지를 입력하는 경우,  -  "2/유저 1 : 안녕하세요"
					System.out.println(message);
					
					msgType = Integer.parseInt(message.split("/")[0]);
					userId = message.split("/")[1];
					userName = message.split("/")[2];
					msgContent = message.split("/")[3];
					// 이후 나머지 부가 정보가 있는 경우
					// ( message.split("/")[4], message.split("/")[5] 같은 추가 정보를 필터링 해야 하는 경우 ) 추가적으로 switch/case 문에 작성할 것
						
					switch(msgType) {
					case 1:
						AppendText("클라이언트 "+clientId+" "+userId+" "+userName+" "+" 로그인");
						// 로그인한 유저의 클라이언트 아이디 전송
						ServerReceiver u = (ServerReceiver) UserVec.elementAt(UserVec.size()-1);
						u.SendToClient("0/-/-/" + (u.clientId-1) +"\n");
						break;
					case 2:
						AppendText("클라이언트 "+clientId+" 채팅 : "+msgContent);
						break;
					case 3: // 클라이언트가 방 생성
						if(rooms[Integer.parseInt(msgContent)]) return; // 방이 이미 만들어져있다면 실행 X
						for (int i = 0; i < UserVec.size(); i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							rooms[Integer.parseInt(msgContent)] = true;
							for(int j=0;j<rooms.length;j++) {
								if(rooms[j]==true) 
									user.SendToClient("1/"+userId+"/"+userName+"/"+j+"\n");
							}
						}
						break;
					case 4: // 클라이언트가 방 입장
						if(!rooms[Integer.parseInt(msgContent)]) return; // 방이 만들어져있지 않다면 실행 X
						for (int i = 0; i < UserVec.size(); i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							System.out.println(UserName);
							user.SendToClient("1/"+userId+"/"+userName+"/"+msgContent+"\n");
							rooms[Integer.parseInt(msgContent)] = true;
							roomUserName[Integer.parseInt(msgContent)] = userName;
						}
						break;
					case 5: // 클라이언트가 로비 입장 시, 열려있는 방이 있는지 검사 후 알림
						for (int i = 0; i < UserVec.size(); i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							for(int j=0;j<rooms.length;j++)
								if(rooms[j]==true) 
									user.SendToClient("1/"+userId+"/"+roomUserName[j]+"/"+j+"\n");
						}
						break;
					case 6: // 클라이언트가 대기방 입장 시, 방에 대한 정보를 저장하고 클라이언트에게 방 정보를 전달한다.
						// 대기실 방에 유저가 없는 부분을 채운다
						UserInfoWaitRoom targetRoom = waitRoomList.elementAt(Integer.parseInt(msgContent)); // 메세지를 보낼 대기실 방 타겟룸
						int enteredRoomIndex = 0;
						
						for(int i=0;i<8;i++) {
							if(targetRoom.userName[i].equals("-")) {
								targetRoom.userName[i] = userName;
								targetRoom.isUserEntered[i] = true;
								targetRoom.clientId[i] = (clientId-1);
								enteredRoomIndex = i;
								break;
							}
						}
						// 각 클라이언트에게 방 정보를 전달한다
						for (int j = 0; j < UserVec.size(); j++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(j);
							
							// 보낼 방 정보들 담을 string
							String msgContents = "";
							// 보낼 유저 이름 정보
							msgContents = String.join(",", targetRoom.userName);
							msgContents += "/";
							// 유저 레디 정보
							for (int i = 0; i < targetRoom.isReady.length; i++) {
								msgContents += Boolean.toString(targetRoom.isReady[i]) + ", ";
					        }
							
							// 해당 클라이언트가 몇 번째 대기방에 입장했는지 저장
							user.enteredRoom = Integer.parseInt(msgContent);
							user.roomIndex = enteredRoomIndex;
							
							// 메세지 전송
							user.SendToClient("2/"+userId+"/"+userName+"/"+msgContent+"/"+msgContents+"\n");
						}
						break;
					case 7: // 클라이언트가 준비 버튼 누를 시
						UserInfoWaitRoom targetWaitRoom = waitRoomList.elementAt(Integer.parseInt(msgContent)); // 메세지를 보낼 대기실 방 타겟룸
						targetWaitRoom.isReady[Integer.parseInt(message.split("/")[4])] = !targetWaitRoom.isReady[Integer.parseInt(message.split("/")[4])];
						
						// 모든 클라이언트에 해당 클라이언트가 준비/준비안됨 을 알림
						for (int i = 0; i < UserVec.size(); i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							
							// 보낼 방 정보들 담을 string
							String contents = "";
							// 보낼 유저 이름 정보
							contents += String.join(",", targetWaitRoom.userName);
							contents += "/";
							// 유저 레디 정보
							for (int j = 0; j < targetWaitRoom.isReady.length; j++) {
								contents += Boolean.toString(targetWaitRoom.isReady[j]) + ", ";
					        }
							
							user.SendToClient("3/"+userId+"/"+userName+"/"+msgContent+"/"+contents+"\n");
						}
						break;
					case 8: // 클라이언트가 시작 버튼 누를 시
						int userCounts = Integer.parseInt(message.split("/")[4]);
						
						// 방장 1명만 있을시, 실행X
						if(userCounts==1) return;
						
						// 모든 유저가 준비 상태가 아니면, 실행하지 않는다
						for(int i=0;i<userCounts;i++) {
							if(waitRoomList.elementAt(Integer.parseInt(msgContent)).isReady[i]==false) return;
						}

						// 대기방 타겟
						UserInfoWaitRoom _targetRoom = waitRoomList.elementAt(Integer.parseInt(msgContent));

						// 모든 클라이언트에게 해당 방에서 게임이 시작되었음을 알린다
						for(int i=0;i<UserVec.size();i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							
							for(int j=0;j<userCounts;j++) {
								user.SendToClient("4/"+userId+"/"+userName+"/"+(Integer.parseInt(msgContent))+"/"+_targetRoom.clientId[j]+"\n");
							}
							
						}
						
						int randomSpawnX[] = {1,1,8,14,15,7,8,8};
						int randomSpawnY[] = {1,12,1,2,12,7,11,4};
						
						// 클라이언트 게임 방에 대한 정보 저장
						for(int i=0;i<userCounts;i++) {
							GameCharacter c = new GameCharacter();
							int randInt = (int)(Math.random()*8);
							c.x = randomSpawnX[randInt]*52 - 26;
							c.y = randomSpawnY[randInt]*51 - 25;
							c.clientId = _targetRoom.clientId[i];
							characters[i] = c;
						}
						
						// 해당 방 초기화
						rooms[Integer.parseInt(msgContent)] = false;
						for(int i=0;i<8;i++) {
							_targetRoom.isReady[i] = false;
							_targetRoom.isUserEntered[i] = false;
							_targetRoom.userName[i] = "-";
							_targetRoom.clientId[i] = -1;
						}
						_targetRoom.isReady[0] = true; // 방장은 레디 상태로
						
						// 보낼 방 정보들 담을 string
						String msgContents = "";
						for(int i=0;i<userCounts;i++) {
							msgContents += characters[i].x;
							msgContents += ",";
						}
						msgContents += "/";
						for(int i=0;i<userCounts;i++) {
							msgContents += characters[i].y;
							msgContents += ",";
						}
						
						// 게임 방 정보 전송
						for(int i=0;i<UserVec.size();i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							user.SendToClient("5/"+userId+"/"+userName+"/"+userCounts+"/"+msgContents+"\n");
						}
						
						break;
					case 9: // 클라이언트 게임 방 내의 플레이어 정보 수신 및 전송
						
						int clientId = Integer.parseInt(message.split("/")[3]); // 클라 아이디
						int client_X = Integer.parseInt(message.split("/")[4]); // 캐릭터좌표 x값
						int client_Y = Integer.parseInt(message.split("/")[5]); // 캐릭터좌표 y값
						int client_dir = Integer.parseInt(message.split("/")[6]); // 캐릭터좌표 moveDirection
						int client_characterIndex = Integer.parseInt(message.split("/")[7]); // 캐릭터좌표 moveDirection
						
						characters[clientId].x = client_X;
						characters[clientId].y = client_Y;
						characters[clientId].dir = client_dir;
						
						// 보낼 방 정보들 담을 string
						String _msgContents = "";
						for(int i=0;i<8;i++) {
							if(characters[i]==null) break;
							_msgContents += characters[i].x;
							_msgContents += ",";
						}
						_msgContents += "/";
						for(int i=0;i<8;i++) {
							if(characters[i]==null) break;
							_msgContents += characters[i].y;
							_msgContents += ",";
						}
						_msgContents +="/";
						_msgContents += client_dir;
						_msgContents +="/";
						_msgContents += client_characterIndex;
						
						for(int i=0;i<UserVec.size();i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							user.SendToClient("6/"+userId+"/"+userName+"/"+_msgContents+"/"+"\n");
						}
						break;
						
					case 10:
						int client_Id = Integer.parseInt(message.split("/")[3]); // 클라 아이디
						int clientRow = Integer.parseInt(message.split("/")[4]); // 캐릭터좌표 row값
						int clientCol = Integer.parseInt(message.split("/")[5]); // 캐릭터좌표 col값
						
						// 보낼 방 정보들 담을 string
						_msgContents = clientRow + "/" + clientCol + "/" + client_Id;
						
						for(int i=0;i<UserVec.size();i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							user.SendToClient("7/"+userId+"/"+userName+"/"+_msgContents+"/"+"\n");
						}
						
						break;
					case 100: // 폭탄 터지면, 방 배열 업데이트
						int mapRow = Integer.parseInt(message.split("/")[3]); // row값
						int mapCol = Integer.parseInt(message.split("/")[4]); // col값
						
						// 보낼 방 정보들 담을 string
						_msgContents = mapRow + "/" + mapCol;
						
						for(int i=0;i<UserVec.size();i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							user.SendToClient("100/"+userId+"/"+userName+"/"+_msgContents+"/"+"\n");
						}
						
						break;
					case 101: // 플레이어가 폭탄 피격범위 내에 있는지 검사
						int explosionX = Integer.parseInt(message.split("/")[3]); // 터질때 x값
						int explosionY = Integer.parseInt(message.split("/")[4]); // 터질때 y값
						
						for(int i=0;i<UserVec.size();i++) {
							ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
							user.SendToClient("101/_/_/"+explosionX+"/"+explosionY+"/"+"\n");
						}
						
						break;
					default:
						break;
				}
			}
		}
			
			
		public void Logout() {
			String msg = "[클라이언트" + clientId + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			if(this.enteredRoom!=-1) { // 만약 대기방에 입장한 상태면, 그 대기방 정보 갱신 및 전송
				UserInfoWaitRoom _targetRoom = waitRoomList.elementAt(enteredRoom);
				// 만약 유저가 대기방 첫번째 손님이었으면 방 없앤다
				if(roomIndex==0) rooms[enteredRoom]=false;
				
				_targetRoom.isReady[roomIndex] = false;
				_targetRoom.isUserEntered[roomIndex] = false;
				_targetRoom.userName[roomIndex] = "-";
				_targetRoom.clientId[roomIndex] = -1;
				
				_targetRoom.isReady[0] = true; // 방장은 레디 상태로
				
				// 모든 클라이언트에 알림
				for (int i = 0; i < UserVec.size(); i++) {
					ServerReceiver user = (ServerReceiver) UserVec.elementAt(i);
					
					// 보낼 방 정보들 담을 string
					String contents = "";
					// 보낼 유저 이름 정보
					contents += String.join(",", _targetRoom.userName);
					contents += "/";
					// 유저 레디 정보
					for (int j = 0; j < _targetRoom.isReady.length; j++) {
						contents += Boolean.toString(_targetRoom.isReady[j]) + ", ";
			        }
					
					user.SendToClient("3/-"+"/"+user.UserName+"/"+roomIndex+"/"+contents+"\n");
				}
			}
			AppendText("사용자 " + "[클라이언트" + (clientId-1) + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}
	}
}}
