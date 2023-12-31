import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;



public class ClientRoom extends JButton{
	
	BufferedReader in;
	public int roomIndex;
	
	private ImageIcon waitRoom = new ImageIcon("./LobbyImages/waitRoom.png"); //배경화면
	private boolean isCreated = false;
	public ClientWaitingRoom waitingRoom = new ClientWaitingRoom(); // 로비의 방 버튼 하나당 대기방을 가짐  
	public String userName;
	
	public ClientRoom(ImageIcon roomImage, int idx, BufferedReader in, BufferedWriter out, String userName, String userId, ClientLobby lobby) {
		super(roomImage);
		
		this.roomIndex = idx;
		this.in = in;
		this.userName = userName;
		waitingRoom.setRoomIndex(roomIndex);
		
		setSize(roomImage.getIconWidth(),roomImage.getIconHeight());
		
		if(idx==0) setLocation(15+(roomImage.getIconWidth()+5)*0,40+(roomImage.getIconHeight()+5)*0);
		if(idx==1) setLocation(20+(roomImage.getIconWidth()+5)*1,40+(roomImage.getIconHeight()+5)*0);
		if(idx==2) setLocation(15+(roomImage.getIconWidth()+5)*0,40+(roomImage.getIconHeight()+5)*1);
		if(idx==3) setLocation(20+(roomImage.getIconWidth()+5)*1,40+(roomImage.getIconHeight()+5)*1);
		if(idx==4) setLocation(15+(roomImage.getIconWidth()+5)*0,40+(roomImage.getIconHeight()+5)*2);
		if(idx==5) setLocation(20+(roomImage.getIconWidth()+5)*1,40+(roomImage.getIconHeight()+5)*2);
		
		addMouseListener(new MouseAdapter(){ // 액션 이벤트 처리	
			public void mouseEntered(MouseEvent e) {
		    	SoundManager.getInstance().playSound(SoundManager.SoundEnum.button_mouseOn); // 마우스 올리면 실행할 사운드
		    }
			@Override
			public void mouseClicked(MouseEvent e) {
				SoundManager.getInstance().playSound(SoundManager.SoundEnum.button_click);
				// 빈 방 클릭시
				if(!isCreated) {
					isCreated=true;
					try {
						out.write("3/"+userId+"/"+userName+"/"+roomIndex+"\n");
						out.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} 
				// 이미 만들어져있는 방 클릭시
				else {
					try {
						out.write("4/"+userId+"/"+userName+"/"+idx+"\n");
						out.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				lobby.roomOpen(roomIndex);
			}
		});
	}
	
	// 방 칠하기
	public void roomCreate(String userName) {
		isCreated = true;
		this.setIcon(waitRoom);
		setLayout(null);
		JLabel roomTitle = new JLabel(userName+" 님의 방");
		roomTitle.setFont(new Font("Serif",Font.BOLD,11));
		roomTitle.setForeground(Color.WHITE);
		roomTitle.setBounds(100, 20, roomTitle.getPreferredSize().width, roomTitle.getPreferredSize().height);
		add(roomTitle);
		repaint();
	}
	

	
}
