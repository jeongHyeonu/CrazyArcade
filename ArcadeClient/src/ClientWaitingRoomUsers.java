import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

// 대기방에 유저가 가질 정보를 담을 클래스입니다.
public class ClientWaitingRoomUsers extends JLabel {
	
	private ImageIcon emptyUser = new ImageIcon("./WaitingRoomImages/emptyUser.png");
	private ImageIcon enteredUser = new ImageIcon("./WaitingRoomImages/enteredUser.png");
	private ImageIcon readyUser = new ImageIcon("./WaitingRoomImages/readyUser.png");
	private ImageIcon manageUser = new ImageIcon("./WaitingRoomImages/manageUser.png");
	
	// 대기방에 유저가 들어오면 할당할 변수들, 일단 기본적으로 다 false/null 을 가지게 한다
	private Boolean isManageUser = false;
	public Boolean isUserEntered = false;
	public Boolean isReady = false;
	public BufferedWriter out = null;
	
	public String userName = "-";
	public String userID = null;
	public int clientUserIndex = -1; // 유저가 방 입장시, 몇 번째로 입장했는지 저장
	private int roomNumber;
	public int idx;
	
	public ClientWaitingRoomUsers(String userName, String userID, int roomNumber, int idx) {
		setIcon(emptyUser);
		this.setSize(emptyUser.getIconWidth(),emptyUser.getIconHeight());
		this.userName = userName;
		this.userID = userID;
		this.roomNumber = roomNumber;
		this.idx = idx;
		
		if(idx==0) this.setLocation(20+(emptyUser.getIconWidth())*0,100+(emptyUser.getIconHeight())*0);
		if(idx==1) this.setLocation(20+(emptyUser.getIconWidth())*1,100+(emptyUser.getIconHeight())*0);
		if(idx==2) this.setLocation(20+(emptyUser.getIconWidth())*2,100+(emptyUser.getIconHeight())*0);
		if(idx==3) this.setLocation(20+(emptyUser.getIconWidth())*3,100+(emptyUser.getIconHeight())*0);
		if(idx==4) this.setLocation(20+(emptyUser.getIconWidth())*0,100+(emptyUser.getIconHeight())*1);
		if(idx==5) this.setLocation(20+(emptyUser.getIconWidth())*1,100+(emptyUser.getIconHeight())*1);
		if(idx==6) this.setLocation(20+(emptyUser.getIconWidth())*2,100+(emptyUser.getIconHeight())*1);
		if(idx==7) this.setLocation(20+(emptyUser.getIconWidth())*3,100+(emptyUser.getIconHeight())*1);
		
		setVisible(true);
	}

	public void userEntered(String username,String userId,int idx) {
		setIcon(enteredUser);
		this.userName=username;
		this.userID=userId;
		isUserEntered = true;
		
		// 첫 번째 들어온 유저는 방장
		if(idx==0) {setIcon(manageUser);isManageUser=true;}
		
		// 유저이름
		JLabel name = new JLabel(username);
		name.setSize(150,100);
		name.setLocation(50,50);
		add(name);
		
	}
	
	public void userReady(boolean b) {
		// 방장은 준비 필요없음
		if(isManageUser==true) return;
		
		// 입장하지 않은 방이면 실행 X
		if(!isUserEntered) return;
		
		if(b==true) setIcon(readyUser);
		else setIcon(enteredUser);
	}
	
	

}
