package Characters;

import java.awt.event.KeyListener;
import java.io.BufferedWriter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Characters.GameCharacter.Direction;

public abstract class GameCharacter extends JLabel{
	// 클라이언트 속성
	public int clientId;
	public String username;
	public BufferedWriter out;
	
	// 이동방향
	public enum Direction{
		up,
		left,
		right,
		down
	}
	public Direction currentDir;
	public int moveIndex;
	
	// 캐릭터 속성
	public int x;
	public int y;
	public int speed;
	public int attackRange;
	public boolean isDead;
	
	// 캐릭터 이미지
	public ImageIcon frontMove[] = new ImageIcon[6];
	public ImageIcon leftMove[] = new ImageIcon[6];
	public ImageIcon rightMove[] = new ImageIcon[6];
	public ImageIcon backMove[] = new ImageIcon[6];
	
	// 캐릭터들이 갖는 메서드
	public void move(Direction d) {
		currentDir = d;
		moveIndex = (moveIndex+1)%6;
		switch(d) {
		case up:
			this.setIcon(backMove[moveIndex]);
			break;
		case left:
			this.setIcon(leftMove[moveIndex]);
			break;
		case right:
			this.setIcon(rightMove[moveIndex]);
			break;
		case down:
			this.setIcon(frontMove[moveIndex]);
			break;
		}
	}
	public void stop() {
		moveIndex = 0;
		SetPlayerDir();
	};
	public void SetPlayerDir() {
		switch(currentDir) {
		case up: 
			this.setIcon(backMove[2]);
			break;
		case right: 
			this.setIcon(rightMove[2]);
			break;
		case left: 
			this.setIcon(leftMove[2]);
			break;
		case down: 
			this.setIcon(frontMove[2]);
			break;
		}
	}
	public abstract void attack();
	public abstract void died();
}
