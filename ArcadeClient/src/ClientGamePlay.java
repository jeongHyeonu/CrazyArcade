import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

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
	
	private ImageIcon dizini = new ImageIcon("./GamePlayImages/Charactor/dizini_front.png");
	
	// 캐릭터 만든 후, 담는 배열
	public Vector<GameCharacter> characterVector = new Vector<GameCharacter>();
	public int userCounts;
	JLabel backgroundLabel = new JLabel(bg);
	
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
    
    
	public ClientGamePlay() {
		setTitle("크레이지아케이드 - 게임방");
		setLocation(0,0);
		setSize(bg.getIconWidth(),bg.getIconHeight());
		setLayout(null);
		setVisible(true);
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
	}
	
	public void CharacterCreate(int userCounts,String xList, String yList, List<String> selectedCharacter) {
		this.userCounts = userCounts;
		for(int i=0;i<userCounts;i++) {
			// 캐릭터 생성
		    int x = Integer.parseInt(xList.split(",")[i]);
		    int y = Integer.parseInt(yList.split(",")[i]);
			String userName = ClientLobby.instance.username;
			int clientId = ClientLobby.instance.clientId;
			BufferedWriter out = ClientLobby.instance.out;
			GameCharacter c = CharacterFactory.getCharacter(selectedCharacter.get(i),x,y,clientId,userName,out);
			c.currentDir = Direction.down;
			c.setSize(60,70);
			c.setVisible(true);
			c.rowIndex = x/blockWidth;
			c.columnIndex = y/blockHeight;
			characterVector.add(c);
			backgroundLabel.add(c);
			backgroundLabel.setComponentZOrder(c, 0);
		}
		int clientId = ClientLobby.instance.clientId;
		clientCharacter = characterVector.elementAt(clientId);
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
		characterVector.elementAt(characterIndex).attack(row,col,backgroundLabel,MapArray);
	}
	
	// 물풍선 터지면, 블록 지우기
	public void DeleteBlockInMap(int row, int col) {
		MapArray[col][row] = 0;
		MapLabelArray[col][row].setVisible(false);
		repaint();
	}
	
	// 플레이어 피격시
	public void CharacterAttacked(int client_id) {
		characterVector.elementAt(client_id).Attacked();
		repaint();
	}
	
	// 플레이어 피격 검사
	public void isCharacterAttacked(int x, int y) {
		for(int i=0;i<userCounts;i++) {
			if(x == characterVector.elementAt(i).rowIndex && y == characterVector.elementAt(i).columnIndex) {
				characterVector.elementAt(i).Attacked();
			}
		}
	}
	
    @Override
    public void keyPressed(KeyEvent e) {
        // keyPressed는 키를 눌렀을 때 호출됩니다.
        int keyCode = e.getKeyCode();

        // 만약 캐릭터가 사망시 실행X
        if(clientCharacter.isDead) return;
        
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
}
