package Characters;

import java.io.BufferedWriter;

public abstract class GameCharacter {
	// 클라이언트 속성
	public int clientId;
	public String username;
	public BufferedWriter out;
	
	// 캐릭터 속성
	public int x;
	public int y;
	public int speed;
	public int attackRange;
	public boolean isDead;
	
	// 캐릭터들이 갖는 메서드
	public abstract void move();
	public abstract void attack();
	public abstract void died();
}
