import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private static SoundManager instance;

    // private 생성자로 외부에서 직접 인스턴스를 생성하는 것을 막음
    private SoundManager() {
    }

    // 인스턴스를 반환하는 정적 메서드
    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    // Enum을 사용하여 음악 파일 경로 관리
    public enum SoundEnum {
        button_mouseOn("./SoundClips/button_mouseOn.wav"),
        button_click("./SoundClips/Button_click.wav"),
        bomb_explosion("./SoundClips/explosion.wav"),
        bomb_set("./SoundClips/bombSet.wav"),
        player_win("./SoundClips/win.wav"),
        player_lose("./SoundClips/lose.wav"),
        player_die("./SoundClips/playerDie.wav"),
        itemGet("./SoundClips/itemGet.wav"),
        gameStart("./SoundClips/gameStart.wav"),
        trapped("./SoundClips/trapped.wav"),
        ;
    	
    	private final String filePath;
    	
        SoundEnum(String filePath) {
            this.filePath = filePath;
        }
        
        public String getFilePath() {
            return filePath;
        }
    }
    
    // 사운드 재생
    public void playSound(SoundEnum soundEnum) {
    	File soundFile = new File(soundEnum.getFilePath());
		try {
			 AudioInputStream audioInputStream;
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            // 음악을 반복 재생하려면 주석 해제
            // clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
