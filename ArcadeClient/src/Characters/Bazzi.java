package Characters;

import java.io.BufferedWriter;

import javax.swing.ImageIcon;

public class Bazzi extends GameCharacter {
	private ImageIcon front = new ImageIcon("./GamePlayImages/Charactor/Bazzi/bazzi_front.png");
	private ImageIcon left = new ImageIcon("./GamePlayImages/Charactor/Bazzi/bazzi_left.png");
	private ImageIcon right = new ImageIcon("./GamePlayImages/Charactor/Bazzi/bazzi_right.png");
	private ImageIcon back = new ImageIcon("./GamePlayImages/Charactor/Bazzi/bazzi_back.png");
	
	public Bazzi(int clientId, int x, int y, String userName, BufferedWriter out) {
		super();
		this.setIcon(front);
		this.x = x;
		this.y = y;
		this.clientId = clientId;
		this.username = userName;
		this.out = out;
		
		// 캐릭터 이미지
		for(int i=0;i<6;i++) {
			frontMove[i] = new ImageIcon("./GamePlayImages/Charactor/Bazzi/bazzi_front_"+i+".png");
			leftMove[i] = new ImageIcon("./GamePlayImages/Charactor/Bazzi/bazzi_left_"+i+".png");
			rightMove[i] = new ImageIcon("./GamePlayImages/Charactor/Bazzi/bazzi_right_"+i+".png");
			backMove[i] = new ImageIcon("./GamePlayImages/Charactor/Bazzi/bazzi_back_"+i+".png");
		}
	}
	
	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void died() {
		// TODO Auto-generated method stub
		
	}

}
