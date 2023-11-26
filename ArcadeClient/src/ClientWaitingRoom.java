import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClientWaitingRoom extends JFrame {
	
	private ImageIcon BG = new ImageIcon("./WaitingRoomImages/waitRoomBG.png");
	private ImageIcon backUI = new ImageIcon("./WaitingRoomImages/waitRoomUI.png");
	private ImageIcon startButtonDisable = new ImageIcon("./WaitingRoomImages/startButtonDisable.png");
	private ImageIcon startButtonAble = new ImageIcon("./WaitingRoomImages/startButtonAble.png");
	private ImageIcon readyButton = new ImageIcon("./WaitingRoomImages/readyButton.png");
	private ImageIcon selectDaoButton = new ImageIcon("./WaitingRoomImages/characterDao.png");
	private ImageIcon selectDiziniButton = new ImageIcon("./WaitingRoomImages/characterDizini.png");
	private ImageIcon selectVillageMapButton = new ImageIcon("./WaitingRoomImages/mapVillage.png");
	private ImageIcon comingsoonIcon = new ImageIcon("./WaitingRoomImages/mapComingsoon.png");
	private ImageIcon checkIcon = new ImageIcon("./WaitingRoomImages/check.png");
	
	private int roomNumber; // 현재 대기방 번호, 0~5
	private int clientUserIndex; // 현재 대기방에 입장한 유저의 번호, 0~7
	public int userCount=1; // 현재 대기방에 입장한 유저 수
	private String username;
	private String userId;
	private BufferedWriter out;
	
	private JButton startBtn;
	private JButton readyBtn;
	private JLabel bgUI;
	
	private JButton selectDao;
	private JButton selectDizini;
	private JButton selectVillageMap;
	private JLabel comingsoon;
	private JLabel check;
	
	
	public Vector<ClientWaitingRoomUsers> waitUsers = new Vector<ClientWaitingRoomUsers>();
	
	public ClientWaitingRoom() {
		super();
		
		setTitle("크레이지아케이드 - 대기방");
		this.setSize(BG.getIconWidth(),BG.getIconHeight()+35);
		this.setLocation(0,0);
		setLayout(null);
		setVisible(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 배경
		JLabel background = new JLabel(BG);
		background.setLocation(0,0);
		background.setSize(BG.getIconWidth(),BG.getIconHeight());
		background.setVisible(true);
		add(background);
		
		// 배경 위에 올라갈 UI
		bgUI = new JLabel(backUI);
		bgUI.setSize(backUI.getIconWidth(),backUI.getIconHeight());
		bgUI.setLocation(10,20);
		bgUI.setLayout(null);
		background.add(bgUI);
		
		// 캐릭터 선택 UI
		// 다오
		selectDao = new JButton(selectDaoButton);
		SelectCharFunc seletDaoFunc = new SelectCharFunc();
		selectDao.addMouseListener(seletDaoFunc);
		selectDao.setSize(selectDaoButton.getIconWidth(),selectDaoButton.getIconHeight());
		selectDao.setLocation(507,112);
		bgUI.add(selectDao);
		selectDao.setVisible(true);
		
		// 디지니
		selectDizini = new JButton(selectDiziniButton);
		SelectCharFunc seletDiziniFunc = new SelectCharFunc();
		selectDizini.addMouseListener(seletDiziniFunc);
		selectDizini.setSize(selectDiziniButton.getIconWidth(),selectDiziniButton.getIconHeight());
		selectDizini.setLocation(631,112);
		bgUI.add(selectDizini);
		selectDizini.setVisible(true);
		
		// 맵선택 UI
		// 빌리지
		selectVillageMap = new JButton(selectVillageMapButton);
		SelectMapFunc seletVillageFunc = new SelectMapFunc();
		selectVillageMap.addMouseListener(seletVillageFunc);
		selectVillageMap.setSize(selectVillageMapButton.getIconWidth(),selectVillageMapButton.getIconHeight());
		selectVillageMap.setLocation(509,314);
		bgUI.add(selectVillageMap);
		selectVillageMap.setVisible(true);
		
		// coming soon
		comingsoon = new JLabel(comingsoonIcon);
		comingsoon.setSize(comingsoonIcon.getIconWidth(),comingsoonIcon.getIconHeight());
		comingsoon.setLocation(633,314);
		comingsoon.setLayout(null);
		bgUI.add(comingsoon);
		
		
		// 시작 버튼 및 준비 버튼
		// 방장에게는 시작 버튼이, 다른 유저에겐 준비 버튼이 보인다
		startBtn = new JButton(startButtonDisable);
		StartFunc startFunc = new StartFunc();
		startBtn.addMouseListener(startFunc);
		startBtn.setSize(startButtonDisable.getIconWidth(),startButtonDisable.getIconHeight());
		startBtn.setLocation(527,490);
		
		readyBtn = new JButton(readyButton);
		ReadyFunc readyFunc = new ReadyFunc();
		readyBtn.addMouseListener(readyFunc);
		readyBtn.setSize(readyButton.getIconWidth(),readyButton.getIconHeight());
		readyBtn.setLocation(527,490);
		
		// 들어온 유저 목록 아이콘 및 벡터 저장
		for(int i=0;i<8;i++) {
			ClientWaitingRoomUsers user = new ClientWaitingRoomUsers(username,userId,roomNumber ,i);
			waitUsers.add(user);
			bgUI.add(user);
		}
		repaint();
	}
	
	// 유저가 대기방 오픈하면
	public void userOpenWaitingRoom(String username,String userId,int roomIndex,BufferedWriter out) {
		// 해당 클라이언트의 대기방 속성 받아오기
		this.username = username;
		this.userId = userId;
		this.out = out;
		this.userCount++;
		
		// 새로고침 뒤 창을 보이게 한다
		refreshWaitingRoom();
		// 유저가 몇 번째로 입장했는지 저장
		for(int i=0;i<waitUsers.size();i++) {
			if(!waitUsers.elementAt(i).isUserEntered) {
				clientUserIndex=i-1;
				break;
			}
		}
		
		// 방장의 경우 대기방 설정
		if(clientUserIndex==0) {
			bgUI.add(startBtn);
			startBtn.setVisible(true);
		}
		
		// 방장 아닌 경우 대기방 설정
		else {
			bgUI.add(readyBtn);
			readyBtn.setVisible(true);
		}
			
		setVisible(true);
	}
	
	// 대기방 새로고침, 유저가 대기방 오픈하거나 나가거나 레디버튼 누를때 호출
	public void refreshWaitingRoom() {
		int readyCount = 0;
		for(int i=0;i<waitUsers.size();i++) {
			ClientWaitingRoomUsers target = waitUsers.elementAt(i);
			if(!target.userName.equals("-")) target.userEntered(target.userName,target.userID,i);
			if(target.isReady) {
				target.userReady(true);
				readyCount++;
			}
		}
		if(userCount==readyCount)SetStartBtnAble();
		else SetStartBtnDisable();
		
		repaint();
	}
	
	public void setRoomIndex(int idx) {roomNumber = idx;}
	
	
	public class SelectCharFunc extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == selectDao) {
	            System.out.println("Dao Selected");
	            
	        } else if (e.getSource() == selectDizini) {
	            System.out.println("Dizini Selected");
	            
	        }
		}
		
	}
	
	public class SelectMapFunc extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == selectVillageMap) {
	            System.out.println("Village Map Selected");
 
	        } 
		}
		
	}
	
	// 시작 버튼 활성화
	public void SetStartBtnAble() {
		if(clientUserIndex==0)
			startBtn.setIcon(startButtonAble);
		repaint();
	}
	// 시작 버튼 비활성화	
	public void SetStartBtnDisable() {
		if(clientUserIndex==0)
			startBtn.setIcon(startButtonDisable);
		repaint();
	}
	
	
	// 어느 waitRoomUser가 버튼을 눌렀는지 체크해야 되서, 여기서 시작/준비 버튼 이벤트 정의
	
	// 시작 버튼 클릭시 수행할 함수
	public class StartFunc extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				// 서버로 어떤 클라이언트가 게임을 실행하는지 전송
				// 유저 수를 세고, 서버로 전달한다. (서버에서는 준비한 유저 수와 비교해서 같으면 게임을 실행시킨다)
				int userCount = 0;
				for(int i=0;i<waitUsers.size();i++) if(waitUsers.elementAt(i).isUserEntered) userCount++;
				out.write("8/"+userId+"/"+username+"/"+roomNumber+"/"+userCount+"\n");
				out.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	// 준비 버튼 클릭시 수행할 함수
	public class ReadyFunc extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				// 서버로 어떤 클라이언트가 준비 상태를 변경하는지 전송
				out.write("7/"+userId+"/"+username+"/"+roomNumber+"/"+clientUserIndex+"\n");
				out.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	// 게임 시작
	public void gamePlay() {
		// 서버에 방이 없어졌음을 알려야 하는데... 일단은 그건 제외하고 게임 실행
		
		// 대기방 지우고 게임플레이로 이동
		// 이때, 게임플레이는 로비 인스턴스에서 생성하고 받도록 한다. 서버에서 메세지를 받는 receiver가 로비에 정의되어 있기 때문에, 서버에서 메세지 호출 시 활용을 위함
		setVisible(false);
		ClientLobby.getInstance().gameInstance = new ClientGamePlay();
	}
}
