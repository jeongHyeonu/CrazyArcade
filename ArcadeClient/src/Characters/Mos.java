package Characters;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.ImageIcon;

public class Mos extends GameCharacter {
	private ImageIcon front = new ImageIcon("./GamePlayImages/Charactor/Mos/mos_front.png");
	private ImageIcon left = new ImageIcon("./GamePlayImages/Charactor/Mos/mos_left.png");
	private ImageIcon right = new ImageIcon("./GamePlayImages/Charactor/Mos/mos_right.png");
	private ImageIcon back = new ImageIcon("./GamePlayImages/Charactor/Mos/mos_back.png");

	public Mos(int clientId, int x, int y, String userName, BufferedWriter out) {
		super();
		this.setIcon(front);
		this.x = x;
		this.y = y;
		this.clientId = clientId;
		this.username = userName;
		this.out = out;
		
		// 캐릭터 이미지
		for(int i=0;i<6;i++) {
			frontMove[i] = new ImageIcon("./GamePlayImages/Charactor/Mos/mos_front_"+i+".png");
			leftMove[i] = new ImageIcon("./GamePlayImages/Charactor/Mos/mos_left_"+i+".png");
			rightMove[i] = new ImageIcon("./GamePlayImages/Charactor/Mos/mos_right_"+i+".png");
			backMove[i] = new ImageIcon("./GamePlayImages/Charactor/Mos/mos_back_"+i+".png");
		}
	}

	@Override
	public void BlockDestroyMessage(int x, int y) {
		// TODO Auto-generated method stub
		try {
			out.write("100/_/_/"+x+"/"+y+"\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void died(int diedClient) {
		// 서버에게 플레이어 사망했음을 알림
		// 사망한 플레이어 클라이언트 아이디인지 체크한다. 죽었다라는 명령은 한 클라이언트만 보내야 하므로..
		if(diedClient != clientId) return;
		try {
			out.write("102/_/"+username+"/"+clientId+"\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
