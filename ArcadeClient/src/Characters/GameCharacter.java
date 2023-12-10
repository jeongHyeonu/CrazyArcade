package Characters;

import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Characters.GameCharacter.Direction;

public abstract class GameCharacter extends JLabel{
	private ImageIcon bomb[] = {
		new ImageIcon("./WaterBomb/bomb0.png"),
		new ImageIcon("./WaterBomb/bomb1.png"),
		new ImageIcon("./WaterBomb/bomb2.png"),
		new ImageIcon("./WaterBomb/bomb3.png"),
	};
	
	private ImageIcon bombLeft_1[] = {
		new ImageIcon("./WaterBomb/left1_0.png"),
		new ImageIcon("./WaterBomb/left1_1.png"),
		new ImageIcon("./WaterBomb/left1_2.png"),
		new ImageIcon("./WaterBomb/left1_3.png"),
		new ImageIcon("./WaterBomb/left1_4.png"),
		new ImageIcon("./WaterBomb/left1_5.png"),
		new ImageIcon("./WaterBomb/left1_6.png"),
		new ImageIcon("./WaterBomb/left1_7.png"),
		new ImageIcon("./WaterBomb/left1_8.png"),
		new ImageIcon("./WaterBomb/left1_9.png"),
		new ImageIcon("./WaterBomb/left1_10.png"),
	};
	private ImageIcon bombLeft_2[] = {
		new ImageIcon("./WaterBomb/left2_0.png"),
		new ImageIcon("./WaterBomb/left2_1.png"),
		new ImageIcon("./WaterBomb/left2_2.png"),
		new ImageIcon("./WaterBomb/left2_3.png"),
		new ImageIcon("./WaterBomb/left2_4.png"),
		new ImageIcon("./WaterBomb/left2_5.png"),
		new ImageIcon("./WaterBomb/left2_6.png"),
		new ImageIcon("./WaterBomb/left2_7.png"),
		new ImageIcon("./WaterBomb/left2_8.png"),
		new ImageIcon("./WaterBomb/left2_9.png"),
		new ImageIcon("./WaterBomb/left2_10.png"),
	};
	
	private ImageIcon bombRight_1[] = {
		new ImageIcon("./WaterBomb/right1_0.png"),
		new ImageIcon("./WaterBomb/right1_1.png"),
		new ImageIcon("./WaterBomb/right1_2.png"),
		new ImageIcon("./WaterBomb/right1_3.png"),
		new ImageIcon("./WaterBomb/right1_4.png"),
		new ImageIcon("./WaterBomb/right1_5.png"),
		new ImageIcon("./WaterBomb/right1_6.png"),
		new ImageIcon("./WaterBomb/right1_7.png"),
		new ImageIcon("./WaterBomb/right1_8.png"),
		new ImageIcon("./WaterBomb/right1_9.png"),
		new ImageIcon("./WaterBomb/right1_10.png"),
	};
	private ImageIcon bombRight_2[] = {
		new ImageIcon("./WaterBomb/right2_0.png"),
		new ImageIcon("./WaterBomb/right2_1.png"),
		new ImageIcon("./WaterBomb/right2_2.png"),
		new ImageIcon("./WaterBomb/right2_3.png"),
		new ImageIcon("./WaterBomb/right2_4.png"),
		new ImageIcon("./WaterBomb/right2_5.png"),
		new ImageIcon("./WaterBomb/right2_6.png"),
		new ImageIcon("./WaterBomb/right2_7.png"),
		new ImageIcon("./WaterBomb/right2_8.png"),
		new ImageIcon("./WaterBomb/right2_9.png"),
		new ImageIcon("./WaterBomb/right2_10.png"),
	};

	private ImageIcon bombUp_1[] = {
		new ImageIcon("./WaterBomb/up1_0.png"),
		new ImageIcon("./WaterBomb/up1_1.png"),
		new ImageIcon("./WaterBomb/up1_2.png"),
		new ImageIcon("./WaterBomb/up1_3.png"),
		new ImageIcon("./WaterBomb/up1_4.png"),
		new ImageIcon("./WaterBomb/up1_5.png"),
		new ImageIcon("./WaterBomb/up1_6.png"),
		new ImageIcon("./WaterBomb/up1_7.png"),
		new ImageIcon("./WaterBomb/up1_8.png"),
		new ImageIcon("./WaterBomb/up1_9.png"),
		new ImageIcon("./WaterBomb/up1_10.png"),
	};
	private ImageIcon bombUp_2[] = {
		new ImageIcon("./WaterBomb/up2_0.png"),
		new ImageIcon("./WaterBomb/up2_1.png"),
		new ImageIcon("./WaterBomb/up2_2.png"),
		new ImageIcon("./WaterBomb/up2_3.png"),
		new ImageIcon("./WaterBomb/up2_4.png"),
		new ImageIcon("./WaterBomb/up2_5.png"),
		new ImageIcon("./WaterBomb/up2_6.png"),
		new ImageIcon("./WaterBomb/up2_7.png"),
		new ImageIcon("./WaterBomb/up2_8.png"),
		new ImageIcon("./WaterBomb/up2_9.png"),
		new ImageIcon("./WaterBomb/up2_10.png"),
	};
	
	private ImageIcon bombDown_1[] = {
		new ImageIcon("./WaterBomb/down1_0.png"),
		new ImageIcon("./WaterBomb/down1_1.png"),
		new ImageIcon("./WaterBomb/down1_2.png"),
		new ImageIcon("./WaterBomb/down1_3.png"),
		new ImageIcon("./WaterBomb/down1_4.png"),
		new ImageIcon("./WaterBomb/down1_5.png"),
		new ImageIcon("./WaterBomb/down1_6.png"),
		new ImageIcon("./WaterBomb/down1_7.png"),
		new ImageIcon("./WaterBomb/down1_8.png"),
		new ImageIcon("./WaterBomb/down1_9.png"),
		new ImageIcon("./WaterBomb/down1_10.png"),
	};
	private ImageIcon bombDown_2[] = {
		new ImageIcon("./WaterBomb/down2_0.png"),
		new ImageIcon("./WaterBomb/down2_1.png"),
		new ImageIcon("./WaterBomb/down2_2.png"),
		new ImageIcon("./WaterBomb/down2_3.png"),
		new ImageIcon("./WaterBomb/down2_4.png"),
		new ImageIcon("./WaterBomb/down2_5.png"),
		new ImageIcon("./WaterBomb/down2_6.png"),
		new ImageIcon("./WaterBomb/down2_7.png"),
		new ImageIcon("./WaterBomb/down2_8.png"),
		new ImageIcon("./WaterBomb/down2_9.png"),
		new ImageIcon("./WaterBomb/down2_10.png"),
	};
			
	
	private ImageIcon bombMiddle[] = {
		new ImageIcon("./WaterBomb/pop0.png"),
		new ImageIcon("./WaterBomb/pop1.png"),
		new ImageIcon("./WaterBomb/pop2.png"),
		new ImageIcon("./WaterBomb/pop3.png"),
		new ImageIcon("./WaterBomb/pop4.png"),
		new ImageIcon("./WaterBomb/pop5.png"),
	};
	
	private ImageIcon attackedImages[] = {
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap0.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap1.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap2.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap3.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap4.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap5.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap6.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap7.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap8.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap9.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap10.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap11.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/trap12.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/death0.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/death1.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/death2.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/death3.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/death4.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/death5.png"),
		new ImageIcon("./GamePlayImages/Charactor/Player_Death/death6.png"),
	};
	
	
	
	// 클라이언트 속성
	public int clientId;
	public String username;
	public BufferedWriter out;
	
	// 이동방향
	public enum Direction{
		up,
		left,
		right,
		down,
		middle
	}
	public Direction currentDir;
	public int moveIndex;
	
	// 캐릭터 속성
	public int x;
	public int y;
	public int speed;
	public int attackRange;
	public boolean isDead;
	
	public int rowIndex;
	public int columnIndex;
	
	// 캐릭터 이미지
	public ImageIcon frontMove[] = new ImageIcon[6];
	public ImageIcon leftMove[] = new ImageIcon[6];
	public ImageIcon rightMove[] = new ImageIcon[6];
	public ImageIcon backMove[] = new ImageIcon[6];
	
	// 캐릭터들이 갖는 메서드
	public void move(Direction d) {
		if(isDead)return; // 사망한경우 실행X
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
		if(isDead)return; // 사망한경우 실행X
		moveIndex = 0;
		SetPlayerDir();
	};
	public void SetPlayerDir() {
		if(isDead)return; // 사망한경우 실행X
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
	public JLabel attack(int row, int col, JLabel background, int map[][]) {
		if(isDead) return null; // 사망한경우 실행X
		int bomb_X = row * 52 + 25;
		int bomb_Y = col * 51 + 53;
		
		JLabel bombLabel = new JLabel(bomb[0]);
		bombLabel.setLocation(bomb_X,bomb_Y);
		bombLabel.setSize(bomb[0].getIconWidth(),bomb[0].getIconHeight());
		bombLabel.setVisible(true);
		bombLabel.repaint();
		background.add(bombLabel);
		Thread bombThread = new Thread(() -> {
			int index = 0;
            while (true) {
                try {
                    Thread.sleep(150);  // 100밀리초마다 쉬면서 이동
                    bombLabel.setIcon(bomb[(index++)%4]);
                    if(index==12) { // 폭발
                    	bombExplosion(Direction.middle,row,col,background,map);
                    	if(row>0) bombExplosion(Direction.left,row-1,col,background,map);
                    	if(row<15) bombExplosion(Direction.right,row+1,col,background,map);
                    	if(col>0) bombExplosion(Direction.up,row,col-1,background,map);
                    	if(col<13) bombExplosion(Direction.down,row,col+1,background,map);
                    	bombLabel.setVisible(false);
                    }
                } catch (InterruptedException e) {
                }
            }
		});
		bombThread.start();
		return bombLabel;
	};
	
	private void bombExplosion(Direction d,int _row, int _col, JLabel _background, int[][] _map) {
		int bomb_X = _row * 52 + 25;
		int bomb_Y = _col * 51 + 53;
		JLabel explosionLabel;
		Thread explosionThread;
		
		// 폭발 위치에 캐릭터가 있는지 검사
		isCharacterAttacked(_row,_col);
		
		switch(d) {
		case middle:
			explosionLabel = new JLabel(bombMiddle[0]);
			explosionLabel.setLocation(bomb_X,bomb_Y);
			explosionLabel.setSize(bomb[0].getIconWidth(),bomb[0].getIconHeight());
			explosionLabel.setVisible(true);
			explosionLabel.repaint();
			_background.add(explosionLabel);
			explosionThread = new Thread(() -> {
				int index = 0;
	            while (true) {
	                try {
	                    Thread.sleep(200);  // 200밀리초마다 이미지 변경
	                    explosionLabel.setIcon(bombMiddle[(index++)%5]);
	                    if(index==5) {
	                    	explosionLabel.setVisible(false);
	                    }
	                } catch (InterruptedException e) {
	                }
	            }
			});
			explosionThread.start();
			break;
		case up:
			switch(_map[_col][_row]) { // 벽과의 충돌 검사 - 뚫을 수 있는 벽은 뚫는다
				case 1:
				case 2:
				case 3:
					BlockDestroyMessage(_row,_col);
					break;
				default:
					break;
			}
			if(_map[_col][_row]!=0) return; // 그 외 다른 블럭이 막고 있다면 실행 X, 바닥 뚫려있을때만 실행
			explosionLabel = new JLabel(bombUp_1[0]);
			explosionLabel.setLocation(bomb_X,bomb_Y);
			explosionLabel.setSize(bomb[0].getIconWidth(),bomb[0].getIconHeight());
			explosionLabel.setVisible(true);
			explosionLabel.repaint();
			_background.add(explosionLabel);
			explosionThread = new Thread(() -> {
				int index = 0;
	            while (true) {
	                try {
	                    Thread.sleep(200);  // 200밀리초마다 이미지 변경
	                    explosionLabel.setIcon(bombUp_1[(index++)%11]);
	                    if(index==10) {
	                    	explosionLabel.setVisible(false);
	                    }
	                } catch (InterruptedException e) {
	                }
	            }
			});
			explosionThread.start();

			break;
		case right:
			switch(_map[_col][_row]) { // 벽과의 충돌 검사 - 뚫을 수 있는 벽은 뚫는다
				case 1:
				case 2:
				case 3:
					BlockDestroyMessage(_row,_col);
					break;
				default:
					break;
			}
			if(_map[_col][_row]!=0) return; // 그 외 다른 블럭이 막고 있다면 실행 X, 바닥 뚫려있을때만 실행
			explosionLabel = new JLabel(bombRight_1[0]);
			explosionLabel.setLocation(bomb_X,bomb_Y);
			explosionLabel.setSize(bomb[0].getIconWidth(),bomb[0].getIconHeight());
			explosionLabel.setVisible(true);
			explosionLabel.repaint();
			_background.add(explosionLabel);
			explosionThread = new Thread(() -> {
				int index = 0;
	            while (true) {
	                try {
	                    Thread.sleep(200);  // 200밀리초마다 이미지 변경
	                    explosionLabel.setIcon(bombRight_1[(index++)%11]);
	                    if(index==10) {
	                    	explosionLabel.setVisible(false);
	                    }
	                } catch (InterruptedException e) {
	                }
	            }
			});
			explosionThread.start();
			break;
		case left:
			switch(_map[_col][_row]) { // 벽과의 충돌 검사 - 뚫을 수 있는 벽은 뚫는다
				case 1:
				case 2:
				case 3:
					BlockDestroyMessage(_row,_col);
					break;
				default:
					break;
			}
			if(_map[_col][_row]!=0) return; // 그 외 다른 블럭이 막고 있다면 실행 X, 바닥 뚫려있을때만 실행
			explosionLabel = new JLabel(bombLeft_1[0]);
			explosionLabel.setLocation(bomb_X,bomb_Y);
			explosionLabel.setSize(bomb[0].getIconWidth(),bomb[0].getIconHeight());
			explosionLabel.setVisible(true);
			explosionLabel.repaint();
			_background.add(explosionLabel);
			explosionThread = new Thread(() -> {
				int index = 0;
	            while (true) {
	                try {
	                    Thread.sleep(200);  // 200밀리초마다 이미지 변경
	                    explosionLabel.setIcon(bombLeft_1[(index++)%11]);
	                    if(index==10) {
	                    	explosionLabel.setVisible(false);
	                    }
	                } catch (InterruptedException e) {
	                }
	            }
			});
			explosionThread.start();
			break;
		case down:
			switch(_map[_col][_row]) { // 벽과의 충돌 검사 - 뚫을 수 있는 벽은 뚫는다
				case 1:
				case 2:
				case 3:
					BlockDestroyMessage(_row,_col);
					break;
				default:
					break;
			}
			if(_map[_col][_row]!=0) return; // 그 외 다른 블럭이 막고 있다면 실행 X, 바닥 뚫려있을때만 실행
			explosionLabel = new JLabel(bombDown_1[0]);
			explosionLabel.setLocation(bomb_X,bomb_Y);
			explosionLabel.setSize(bomb[0].getIconWidth(),bomb[0].getIconHeight());
			explosionLabel.setVisible(true);
			explosionLabel.repaint();
			_background.add(explosionLabel);
			explosionThread = new Thread(() -> {
				int index = 0;
	            while (true) {
	                try {
	                    Thread.sleep(200);  // 200밀리초마다 이미지 변경
	                    explosionLabel.setIcon(bombDown_1[(index++)%11]);
	                    if(index==10) {
	                    	explosionLabel.setVisible(false);
	                    }
	                } catch (InterruptedException e) {
	                }
	            }
			});
			explosionThread.start();
			break;
		}

	}
	
	public void isCharacterAttacked(int _x, int _y) {
		try {
			out.write("101/_/_/"+_x+"/"+_y+"/"+"\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Attacked() {
		setIcon(attackedImages[0]);
		isDead=true;
	}
	
	public abstract void BlockDestroyMessage(int x, int y);
	public abstract void died();
}
