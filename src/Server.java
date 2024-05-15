import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class Server {
	private static int uniqueId;
	
	public ArrayList<ClientThread> al;
	public ServerGUI sg;
	private SimpleDateFormat sdf;
	private int port;
	private boolean keepGoing;
	public Game Game;
	int x;
	private ArrayList<Integer> scoreList = new ArrayList<Integer>();

	public Server(int port) {
		this(port, null);
	}
	
	public Server(int port, ServerGUI sg) {
		this.sg = sg;
		this.port =port;
		sdf = new SimpleDateFormat("HH:mm:ss");
		al = new ArrayList<ClientThread>();
	}
	
	public void start() {
		keepGoing = true;
		try 
		{
			ServerSocket serverSocket = new ServerSocket(port);

			while(keepGoing) 
			{
				display("Server waiting for Clients on port " + port + ".");

				Socket socket = serverSocket.accept();  
				
				
				if(!keepGoing)
					break;
				ClientThread t = new ClientThread(socket); 
				al.add(t);									
				t.start();

				
                                display("в настоящее время подключено "+ al.size() + " игроков с никами: ");
				for(int i =0; i < al.size() ; i++ )
				{
					display(al.get(i).username+" ");
				}
	
			}
			try {
				serverSocket.close();
				for(int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
					tc.sInput.close();
					tc.sOutput.close();
					tc.socket.close();
					}
					catch(IOException ioE) {
					}
				}
			}
			catch(Exception e) {
				display("Исключение, закрывающее сервер и клиенты: " + e);
			}
		}
		catch (IOException e) {
            String msg = sdf.format(new Date()) + " Исключение для нового ServerSocket: " + e + "\n";
			display(msg);
		}

		}
		

	
	public int getNumberOfThreads()
	{
		al.get(0).writeMsg("Количество клиентов" + al.size());
	

		return al.size();
	}
	
	protected void stop() {
		keepGoing = false;
		try {
			new Socket("localhost", port); 
		}
		catch(Exception e) {
		}
	}
	
	
	private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		if(sg == null)
			System.out.println(time);
		else
			sg.appendEvent(time + "\n");
	}
	
	
	public synchronized void broadcast(String message) {
		
		String time = sdf.format(new Date());
		String messageLf = time + " " + message + "\n";
		if(sg == null)
			System.out.print(messageLf);
		else
			sg.appendRoom(messageLf);    
		
		for(int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);
			
			if(!ct.writeMsg(messageLf)) {
				al.remove(i);
				display("Отключённый клиент " + ct.username + " удалён из списка.");
			}
		}
	}
	
	synchronized void remove(int id) {

		for(int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);

			if(ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}

	class ClientThread extends Thread {

		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;

		int id;

		String username;

		ChatMessage cm;

		String date;
		Integer score;
		
		Server server = null;

		ClientThread(Socket socket) { 

			id = ++uniqueId;
			this.socket = socket;
			this.server = server;
			System.out.println("Поток пытается создать потоки ввода/вывода объектов");
			try
			{

				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());

				username = (String) sInput.readObject();
				display(username + " только что подключился.");
			}
			catch (IOException e) {
				display("Исключение при создании новых потоков ввода/вывода: " + e);
				return;
			}


			catch (ClassNotFoundException e) {
			}
            date = new Date().toString() + "\n";
		}


		public void run() {

			boolean keepGoing = true;
			while(keepGoing) {

				try {
					cm = (ChatMessage) sInput.readObject();


				}
				catch (IOException e) {
					display(username + " Исключение при чтении потоков: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}

				String message = cm.getMessage();
				

				switch(cm.getType()) {

				case ChatMessage.MESSAGE:
					broadcast(username + ": " + message);
				
					break;
				case ChatMessage.LOGOUT:
					//broadcast("Server: " + username + " sie rozі№czyі...");
                                        broadcast("Сервер: " + username + " отключился...");
					
					keepGoing = false;
					break;

				case ChatMessage.SCORE:
				{
					Integer scoreInt = Integer.parseInt(message);
					scoreList.add(scoreInt);
					String time = sdf.format(new Date());
					writeMsg("Сервер: " + time + " Конечный результат игрока: " + username + ": " + message + "\n");
		            String importantInfo[] = {

		            };


		            	System.out.println(scoreList.size());
		            	if (scoreList.size() > 1 )
		            	{
		            		for(int j=0; j< scoreList.size(); j++)
		            		{
		            			//System.out.println(scoreList.get(j));
		            			if( scoreList.get(j) > scoreList.get(j+1))
		            			{
		            				broadcast("победил игрок с результатом: "+ scoreList.get(j) + "\n"); //Выиграл игрок с результатом
		            				break;
		            				
		            			}
		            			else
		            			{
		            				broadcast("победил игрок с результатом: "+ scoreList.get(j+1) +"\n");
		            				break;
		            			}
		            		}
		            		break;
		            	}
		            	else
		            	{
		            		writeMsg("Подождите, пока другой игрок закончит игру \n"); //Подожди, пока другой игрок закончит свой ход.
		            	}
		            }
					break;
				}
		}   
			remove(id);
			close();
		}
		

		private void close() {

			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}



		private boolean writeMsg(String msg) {

			System.out.println("sending message "+msg);
			if(!socket.isConnected()) {
				close();
				return false;
			}

			try {
				sOutput.writeObject(msg);
			}

			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}


