package Characters;

import java.io.BufferedWriter;

public class Dizini extends GameCharacter {

	public Dizini(int clientId, int x, int y, String userName, BufferedWriter out) {
		this.x = x;
		this.y = y;
		this.clientId = clientId;
		this.username = userName;
		this.out = out;
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
