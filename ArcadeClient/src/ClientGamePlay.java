import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.xml.stream.events.Characters;

import Characters.CharacterFactory;
import Characters.GameCharacter;
import Characters.GameCharacter.Direction;

public class ClientGamePlay extends JFrame implements KeyListener {
	
	private ImageIcon bg = new ImageIcon("./GamePlayImages/play_bg.png"); //배경화면
	
	private int blockWidth = 52;
	private int blockHeight = 51;
	
	private ImageIcon block1 = new ImageIcon("./GamePlayImages/tiles/block1.png");
	private ImageIcon block2 = new ImageIcon("./GamePlayImages/tiles/block2.png");
	private ImageIcon block3 = new ImageIcon("./GamePlayImages/tiles/block3.png");
	private ImageIcon block4 = new ImageIcon("./GamePlayImages/tiles/block4.png");
	private ImageIcon block5 = new ImageIcon("./GamePlayImages/tiles/block5.png");
	private ImageIcon block6 = new ImageIcon("./GamePlayImages/tiles/block6.png");
	private ImageIcon block7 = new ImageIcon("./GamePlayImages/tiles/block7.png");
	private ImageIcon block8 = new ImageIcon("./GamePlayImages/tiles/block8.png");
	private ImageIcon block9 = new ImageIcon("./GamePlayImages/tiles/block9.png");
	private ImageIcon block10 = new ImageIcon("./GamePlayImages/tiles/block10.png");
	private ImageIcon block11 = new ImageIcon("./GamePlayImages/tiles/block11.png");
	private ImageIcon block12 = new ImageIcon("./GamePlayImages/tiles/block12.png");
	
	private ImageIcon speedItem[] = {
			new ImageIcon("./GamePlayImages/Items/speed1.png"),
			new ImageIcon("./GamePlayImages/Items/speed2.png"),
			new ImageIcon("./GamePlayImages/Items/speed3.png")
	};
	private ImageIcon bombPower[] = {
			new ImageIcon("./GamePlayImages/Items/Bpower1.png"),
			new ImageIcon("./GamePlayImages/Items/Bpower2.png"),
			new ImageIcon("./GamePlayImages/Items/Bpower3.png")
	};
	private ImageIcon bombMax[] = {
			new ImageIcon("./GamePlayImages/Items/Bmax1.png"),
			new ImageIcon("./GamePlayImages/Items/Bmax2.png"),
			new ImageIcon("./GamePlayImages/Items/Bmax3.png")
	};
	
	private ImageIcon dizini = new ImageIcon("./GamePlayImages/Charactor/dizini_front.png");
	
	// 캐릭터 만든 후, 담는 배열
	public Vector<GameCharacter> characterVector = new Vector<GameCharacter>();
	public int userCounts;
	JLabel backgroundLabel = new JLabel(bg);
	
	// 플레이어 한명만 남은경우 게임오버
	private boolean isGameOver = false;
	
    int[][] MapArray = { //맵
		   {0, 3, 2, 3, 2,11, 0, 0, 1,11, 5, 2, 5, 0, 5}, 
		   {0, 4, 1, 4, 1,10, 1, 0, 0,10, 2, 3, 0, 0, 1}, 
		   {0, 0, 3, 2, 3,11, 0, 1, 1,11, 5, 1, 5, 1, 5},
		   {1, 4, 1, 4, 1,10, 1, 0, 0,10, 3, 2, 3, 2, 3},
		   {2, 3, 2, 3, 2,11, 0, 0, 1,11, 5, 1, 5, 1, 5},
		   {3, 4, 3, 4, 3,10, 1, 1, 0, 0, 2, 3, 2, 3, 2},
		   {10,11,10,11,10,11, 0, 0, 1,11,10,11,10,11,10},
		   {2, 3, 2, 3, 2, 0, 1, 0, 0,10, 2, 4, 2, 4, 2},
		   {6, 1, 6, 1, 6,11, 0, 1, 1,11, 3, 2, 3, 2, 3},
		   {3, 2, 3, 2, 3,10, 1, 0, 0,10, 1, 4, 1, 4, 1},
		   {6, 0, 6, 1, 6,11, 0, 0, 1,11, 2, 3, 2, 3, 0},
		   {0, 0, 2, 3, 2,10, 1, 1, 0,10, 1, 4, 1, 4, 0},
		   {6, 0, 6, 2, 6,11, 0, 0, 1,11, 3, 2, 3, 0, 0},
	};
    
    // 블록 라벨들
    JLabel[][] MapLabelArray = new JLabel[13][15];
    
    // 캐릭터
    private JLabel characterLabel;


	private GameCharacter clientCharacter;
    
	
	private Vector<String> diedUserVector;
    
	public ClientGamePlay() {
		setTitle("크레이지아케이드 - 게임방");
		setLocation(0,0);
		setSize(bg.getIconWidth(),bg.getIconHeight());
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 로비에서 서버의 응답을 수신하기 때문에, 로비의 게임플레이 인스턴스를 설정해준다 
		ClientLobby.instance.gameInstance = this;
		
		// 키 입력 가능하게끔
        addKeyListener(this);
        setFocusable(true);
        
		// 배경
		backgroundLabel.setSize(bg.getIconWidth(),bg.getIconHeight());
		backgroundLabel.setLocation(0,0);
		add(backgroundLabel);
        
        
        
        // 스레드를 시작하여 캐릭터를 움직이게 한다.
        Thread movementThread = new Thread(() -> {
            while (true) {
            	for(int i=0;i<characterVector.size();i++) {
            		GameCharacter target = characterVector.elementAt(i);
            		target.setLocation(target.x, target.y);
            	}
                try {
                    Thread.sleep(50);  // 50밀리초마다 쉬면서 이동
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        movementThread.start();
        
        // 타일 및 블럭 배치
        for(int row=0;row<15;row++) {
        	for(int column=12;column>=0;column--) { // 라벨이 위에서부터 아래로 그려져야 블럭이 보이므로..
        		JLabel block = new JLabel();
        		block.setLocation(26+(blockWidth*row),45+(blockHeight*column));
        		block.setSize(blockWidth, blockHeight+20);
        		if(MapArray[column][row]==1) block.setIcon(block1);
        		else if(MapArray[column][row]==2) block.setIcon(block2);
        		else if(MapArray[column][row]==3) block.setIcon(block3);
        		else if(MapArray[column][row]==4) block.setIcon(block4);
        		else if(MapArray[column][row]==5) block.setIcon(block5);
        		else if(MapArray[column][row]==6) block.setIcon(block6);
        		else if(MapArray[column][row]==7) block.setIcon(block7);
        		else if(MapArray[column][row]==8) block.setIcon(block8);
        		else if(MapArray[column][row]==9) block.setIcon(block9);
        		else if(MapArray[column][row]==10) block.setIcon(block10);
        		else if(MapArray[column][row]==11) block.setIcon(block11);
        		
        		backgroundLabel.add(block);
        		MapLabelArray[column][row] = block;
        	}
        }
        
        backgroundLabel.repaint();
        setVisible(true);
	}
	
	public void CharacterCreate(int userCounts,String xList, String yList, List<String> selectedCharacter,List<String> userNames) {
		this.userCounts = userCounts;
		for(int i=0;i<userCounts;i++) {
			// 캐릭터 생성
		    int x = Integer.parseInt(xList.split(",")[i]);
		    int y = Integer.parseInt(yList.split(",")[i]);
			String userName = userNames.get(i);
			int clientId = ClientLobby.instance.clientId;
			BufferedWriter out = ClientLobby.instance.out;
			GameCharacter c = CharacterFactory.getCharacter(selectedCharacter.get(i),x,y,clientId,userName,out);
			if(c==null) c = CharacterFactory.getCharacter("Bazzi",x,y,clientId,userName,out);
			c.currentDir = Direction.down;
			c.setSize(100,100);
			c.setVisible(true);
			c.rowIndex = x/blockWidth;
			c.columnIndex = y/blockHeight;
			characterVector.add(c);
			backgroundLabel.add(c);
			backgroundLabel.setComponentZOrder(c, 0);
			
			System.out.println(c.getClass().getSimpleName());
			
			// 오른쪽 플레이어 리스트 - 이미지
			JLabel playerImage = new JLabel(new ImageIcon("./GamePlayImages/rightCharacter_"+c.getClass().getSimpleName()+".png"));
			playerImage.setSize(70,50);
			playerImage.setLocation(853,130+56*i);
			playerImage.setVisible(true);
			backgroundLabel.add(playerImage);
			
			// 오른쪽 플레이어 리스트 - 텍스트
			JLabel characterName = new JLabel(c.username);
			characterName.setSize(70,20);
			characterName.setLocation(925,132+56*i);
			characterName.setForeground(Color.WHITE);
			characterName.setVisible(true);
			backgroundLabel.add(characterName);
		}
		int clientId = ClientLobby.instance.clientId;
		clientCharacter = characterVector.elementAt(clientId);
		backgroundLabel.repaint();
	}
	
	public void UpdateCharacterVector(String xList, String yList, int moveDir, int characterIndex) {
		String[] clientX = xList.split(",");
		String[] clientY = yList.split(",");
		for(int i=0;i<userCounts;i++) {
			characterVector.elementAt(i).x = Integer.parseInt(clientX[i]) ;
			characterVector.elementAt(i).y = Integer.parseInt(clientY[i]) ;
			characterVector.elementAt(i).rowIndex = Integer.parseInt(clientX[i])/blockWidth ;
			characterVector.elementAt(i).columnIndex = Integer.parseInt(clientY[i])/blockHeight ;
		}
		switch(moveDir) {
        case -1: // 캐릭터 이동 정지
        	characterVector.elementAt(characterIndex).stop();
        	break;
		case KeyEvent.VK_UP:
			characterVector.elementAt(characterIndex).move(Direction.up);
			break;
		case KeyEvent.VK_DOWN:
			characterVector.elementAt(characterIndex).move(Direction.down);
			break;
		case KeyEvent.VK_LEFT:
			characterVector.elementAt(characterIndex).move(Direction.left);
			break;
		case KeyEvent.VK_RIGHT:
			characterVector.elementAt(characterIndex).move(Direction.right);
			break;
		}
		
		characterVector.elementAt(characterIndex).repaint();
	}
	
	// 폭탄 설치
	public void SetBomb(int row, int col, int characterIndex) {
        // 게임 오버시 실행 X
        if(isGameOver) return;
		
		characterVector.elementAt(characterIndex).attack(row,col,backgroundLabel,MapArray);
		SoundManager.getInstance().playSound(SoundManager.SoundEnum.bomb_set); // 폭탄 설치 사운드
		
        // 2초 후에 특정 기능 수행 - 폭발사운드
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SoundManager.getInstance().playSound(SoundManager.SoundEnum.bomb_explosion);
            }
        }, 2000); // 2000 밀리초 = 2초
	}
	
	// 물풍선 터지면, 블록 지우기
	public void DeleteBlockInMap(int row, int col) {
        // 게임 오버시 실행 X
        if(isGameOver) return;
		
		MapArray[col][row] = 0;
		MapLabelArray[col][row].setVisible(false);
    	try { // 랜덤 아이템 생성하라는 메세지 서버로 전송
			clientCharacter.out.write("104/_/_/"+row+"/"+col+"\n");
		} catch (IOException e) {
		} 
		repaint();
	}
	
	// 물풍선 터지면 아이템 등장, Lobby에서 호출
	public void SpawnItem(int row, int col, int randomItem) {
        // 게임 오버시 실행 X
        if(isGameOver) return;
        
        // 이미 아이템 있으면 생성 X
        if(MapLabelArray[col][row].getText().equals("item")) return;
        
        Thread itemThread = new Thread(()->{
        	
			int index = 1;
	        
	        // 0 : 폭탄 수 증가
	        // 1 : 폭탄 파워 증가
	        // 2 : 스피드 증가
	        // 나머지 50%확률로 생성 안됬다면 실행X
	        if(randomItem==3||randomItem==4||randomItem==5) return;
	        
	        // 그외에 생성된경우
	        JLabel itemLabel = new JLabel("item");
	        MapLabelArray[col][row] = itemLabel;
	        
	        itemLabel.setLocation(26+row*blockWidth,45+col*blockHeight);
	        itemLabel.setSize(60,60);
	        itemLabel.setVisible(true);
	        itemLabel.setFont(new Font("",0,0));
	        backgroundLabel.add(itemLabel);
	        
            while (true) {
                try {
                    Thread.sleep(100);  // 100밀리초마다 이미지 변경 및 플레이어가 닿았는지 검사
    				if(randomItem==0) itemLabel.setIcon(bombMax[(index++)%3]); // 폭탄 수 증가
    				if(randomItem==1) itemLabel.setIcon(bombPower[(index++)%3]); // 폭탄 파워 증가
    				if(randomItem==2) itemLabel.setIcon(speedItem[(index++)%3]); // 스피드 증가
            		for(int i=0;i<userCounts;i++) {
            			if(row == characterVector.elementAt(i).rowIndex && col == characterVector.elementAt(i).columnIndex) {
            				if(randomItem==0) characterVector.elementAt(i).attackRange++; // 폭탄 수 증가
            				if(randomItem==1) characterVector.elementAt(i).bombStack++; // 폭탄 파워 증가
            				if(randomItem==2) characterVector.elementAt(i).speed+=5; // 스피드 증가
            				SoundManager.getInstance().playSound(SoundManager.SoundEnum.itemGet); // 아이템 먹을때 사운드
            				itemLabel.setVisible(false);
            				return;
            			}
            		}
                } catch (InterruptedException e) {
                }
            }
		});
        itemThread.start();
	}
	
	// 플레이어 피격시
	public void CharacterAttacked(int client_id) {
		characterVector.elementAt(client_id).Attacked(client_id);
		repaint();
	}
	
	// 플레이어 피격 검사
	public void isCharacterAttacked(int x, int y) {
        // 게임 오버시 실행 X
        if(isGameOver) return;
        
		for(int i=0;i<userCounts;i++) {
			if(x == characterVector.elementAt(i).rowIndex && y == characterVector.elementAt(i).columnIndex) {
				characterVector.elementAt(i).Attacked(i);
				SoundManager.getInstance().playSound(SoundManager.SoundEnum.trapped); // 피격당함
			}
		}
	}
	
	// 플레이어 사망
	public void CharacterDied(String userName, int client_id, int dieOrder) {
        // 게임 오버시 실행 X
        if(isGameOver) return;
        
        SoundManager.getInstance().playSound(SoundManager.SoundEnum.player_die);
        
		characterVector.elementAt(client_id).diedIndex = dieOrder; // 몇번재 순서로 죽었는지 체크
		if(dieOrder==userCounts-1) { // 마지막에 한 유저만 남았다면
			isGameOver = true; // 게임 오버
			
			try {
				clientCharacter.out.write("103/_/_/_"+"\n");
				clientCharacter.out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void ResultOpen(String _cliId, String _diedOrder) {
		// 패배 결과화면
		if(clientCharacter.isDead) { 
			JLabel resultLabel = new ResultLabel(false, _cliId, _diedOrder, backgroundLabel);
		}
		// 승리 결과화면
		else { 
			JLabel resultLabel = new ResultLabel(true, _cliId, _diedOrder, backgroundLabel);
		}
		
		backgroundLabel.repaint();
		repaint();
	}
	
    @Override
    public void keyPressed(KeyEvent e) {
        // keyPressed는 키를 눌렀을 때 호출됩니다.
        int keyCode = e.getKeyCode();

        // 생성 아직 안됬으면 실행 X
        if(clientCharacter==null) return;
        
        // 만약 캐릭터가 사망시 실행X
        if(clientCharacter.isDead) return;
        
        // 게임 오버시 실행 X
        if(isGameOver) return;
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
            	if(clientCharacter.y>0)
            		if(MapArray[(clientCharacter.y-10)/blockHeight][clientCharacter.x/blockWidth]==0)
                		clientCharacter.y -= 10;
                break;
            case KeyEvent.VK_DOWN:
            	if(clientCharacter.y<this.getHeight())
            		if(MapArray[(clientCharacter.y+10)/blockHeight][clientCharacter.x/blockWidth]==0)
                		clientCharacter.y += 10;
                break;
            case KeyEvent.VK_LEFT:
            	if(clientCharacter.x>0)
            		if(MapArray[clientCharacter.y/blockHeight][(clientCharacter.x-10)/blockWidth]==0)
            			clientCharacter.x -= 10;
                break;
            case KeyEvent.VK_RIGHT:
            	if(clientCharacter.x<this.getWidth())
            		if(MapArray[clientCharacter.y/blockHeight][(clientCharacter.x+10)/blockWidth]==0)
            			clientCharacter.x += 10;
                break;
    		case KeyEvent.VK_SPACE:
    			// 서버로 공격했다는 신호를 보낸다
    	        try {
    				clientCharacter.out.write("10/"+"_"+"/"+clientCharacter.username+"/"+clientCharacter.clientId+"/"+clientCharacter.rowIndex+"/"+clientCharacter.columnIndex+"\n");
    				clientCharacter.out.flush();
    	        } catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    	        return;
        }
        
        // 서버로 캐릭터 이동 전송
        try {
        	int characterIndex = characterVector.indexOf(clientCharacter);
			clientCharacter.out.write("9/"+"_"+"/"+clientCharacter.username+"/"+clientCharacter.clientId+"/"+clientCharacter.x+"/"+clientCharacter.y+"/"+keyCode+"/"+characterIndex+"\n");
			clientCharacter.out.flush();
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //out.write
    }

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() != KeyEvent.VK_SPACE) {
	        // 서버로 캐릭터 이동 전송
	        try {
	        	// 캐릭터 이동 중단
	        	int characterIndex = characterVector.indexOf(clientCharacter);
				clientCharacter.out.write("9/"+"_"+"/"+clientCharacter.username+"/"+clientCharacter.clientId+"/"+clientCharacter.x+"/"+clientCharacter.y+"/"+ -1 +"/"+characterIndex+"\n");
				clientCharacter.out.flush();
	        } catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
	
	// 결과창
	class ResultLabel extends JLabel{
		private ImageIcon resultPanel = new ImageIcon("./GamePlayImages/resultPanel.png");
		private ImageIcon loseLabel_0 = new ImageIcon("./GamePlayImages/loseLabel_0.png");
		private ImageIcon loseLabel_1 = new ImageIcon("./GamePlayImages/loseLabel_1.png");
		private ImageIcon winLabel_0 = new ImageIcon("./GamePlayImages/winLabel_0.png");
		private ImageIcon winLabel_1 = new ImageIcon("./GamePlayImages/winLabel_1.png");
		private ImageIcon rankLabelImage = new ImageIcon("./GamePlayImages/rankLabel.png");
		
		JLabel winOrLose;
		
		public ResultLabel(boolean isWin, String _cliId, String _diedOrder, JLabel background) {
			if(isWin) {
				winOrLose = new JLabel(winLabel_0);
				SoundManager.getInstance().playSound(SoundManager.SoundEnum.player_win);
				winOrLose.setLocation(250,50);
			}
			else {
				winOrLose = new JLabel(loseLabel_0);
				SoundManager.getInstance().playSound(SoundManager.SoundEnum.player_lose);
				winOrLose.setLocation(230,50);
			}
			winOrLose.setVisible(true);
			winOrLose.setSize(400,100);
			
			background.add(winOrLose);
			background.setComponentZOrder(winOrLose, 0);
			
			JLabel resultLabel = new JLabel(resultPanel);
			resultLabel.setSize(resultPanel.getIconWidth(),resultPanel.getIconHeight());
			resultLabel.setLocation(120,200);
			resultLabel.setVisible(true);
			background.add(resultLabel);
			
			int clientCnt = _cliId.split(",").length;
			
			for(int i=0;i<clientCnt;i++) {
				
				String userTxt = "     ";
				userTxt += characterVector.elementAt(Integer.parseInt(_cliId.split(",")[i])).username + "            ";
				if(characterVector.elementAt(Integer.parseInt(_cliId.split(",")[i])).diedIndex==-1) userTxt += "1등            ";
				else userTxt += characterVector.elementAt(Integer.parseInt(_cliId.split(",")[i])).diedIndex+1+"등            ";
				userTxt += characterVector.elementAt(Integer.parseInt(_cliId.split(",")[i])).getClass().getSimpleName();
				
				JLabel rankLabel = new JLabel(rankLabelImage);
				rankLabel.setHorizontalAlignment(JLabel.CENTER);
				rankLabel.setLocation(15,50+42*i);
				rankLabel.setSize(600,40);
				rankLabel.setVisible(true);
				resultLabel.add(rankLabel);
				
				JLabel rankText = new JLabel(userTxt);
				rankText.setVisible(true);
				rankText.setLocation(100,7);
				rankText.setSize(400,20);
				rankText.setFont(new Font("Serif",Font.BOLD,15));
				rankText.setForeground(Color.WHITE);
				rankLabel.add(rankText);
			}
			
			background.setComponentZOrder(resultLabel, 0);
			
			repaint();
		}
	}

}

