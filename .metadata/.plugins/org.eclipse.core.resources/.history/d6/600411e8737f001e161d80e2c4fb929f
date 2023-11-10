import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JFrame;

public class ClientLobby extends JFrame {
	private BufferedReader in;
	private BufferedWriter out;
	
	public ClientLobby(String username, String ip_addr) {

		// System.out.println("연결됨");
		
		try {
			Socket socket = new Socket("localhost", 9999); // 클라이언트 소켓 생성
			// 클라이언트로부터의 입출력 스트림
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // 클라이언트로의 출력 스트림
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
}
