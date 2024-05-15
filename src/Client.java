
import java.awt.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client  {

	
	public ObjectInputStream sInput;		

	public ObjectOutputStream sOutput;		
	private Socket socket;
	private static TextArea textArea;
	
	private Game cg;
	private int ID=-1;

	public String server, username;
	private int port;
	public boolean canStart = false;

	
	Client(String server, int port, String username, TextArea textArea) {
		this.server = server;
		this.port = port;
		this.username = username;

		this.textArea =  textArea;
	}
	
	
	public boolean start() {

		try {
			socket = new Socket(server, port);
			this.ID = socket.getPort();
		} 

		catch(Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}
		
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);
	

		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}


		new ListenFromServer(this);


		try
		{
			sOutput.writeObject(username);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}

		return true;
	}


	private void display(String msg) {
		if(cg == null)
			System.out.println(msg);    
		else
			cg.append(msg + "\n");		
	}
	

	void sendMessage(ChatMessage msg) {
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	
	private void disconnect() {
		try { 
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {} 
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {}
        try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} 

		if(cg != null)
			cg.connectionFailed();
			
	}


	
	class ListenFromServer extends Thread {

		private Client client = null;
		
		public ListenFromServer(Client client) {
			this.client = client;
			start();
		}
		public void run() {
			while(true) {
				try {
					String msg = (String) sInput.readObject();
						textArea.append(msg);
					
				}
				catch(IOException e) {
					display("Server has close the connection: " + e);
					if(cg != null) 
						cg.connectionFailed();
					break;
				}

				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}

