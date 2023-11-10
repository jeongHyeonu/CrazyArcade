import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

class User extends Thread {//UserService--------------------------------
			private InputStream is;
			private OutputStream os;
			private DataInputStream dis;
			private DataOutputStream dos;

			private ObjectInputStream ois;
			private ObjectOutputStream oos;

			private Socket client_socket;
			private Vector user_vc;
			public String UserName = "";
			public String UserStatus;

			public UserService(Socket client_socket) { //생성자
				// TODO Auto-generated constructor stub
				// 매개변수로 넘어온 자료 저장
				this.client_socket = client_socket;
				try {

					oos = new ObjectOutputStream(client_socket.getOutputStream());
					oos.flush();
					ois = new ObjectInputStream(client_socket.getInputStream());

				} catch (Exception e) {
					AppendText("userService error");
				}
			}
		}