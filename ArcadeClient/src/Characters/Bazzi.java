package Characters;

import java.io.BufferedWriter;

import javax.swing.ImageIcon;

public class Bazzi extends GameCharacter {
	
	private ImageIcon front = new ImageIcon("./GamePlayImages/Charactor/Bazzi/bazzi_front.png");

	public Bazzi(int clientId, int x, int y, String userName, BufferedWriter out) {
		super();
		this.setIcon(front);
		this.x = x;
		this.y = y;
		this.clientId = clientId;
		this.username = userName;
		this.out = out;
	}
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
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
