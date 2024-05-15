import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Player implements Runnable{
	private Socket sock;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	public Player(Socket sock){
			this.sock = sock;
			try {
				in = new DataInputStream(sock.getInputStream());
				out = new DataOutputStream(sock.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	public void sendMessage(String message)
	{
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getMessage()
	{
		String  message = null;
		try {
			message = in.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
