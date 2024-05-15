import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//import server.ClientThread;


public class Game extends JFrame  implements ActionListener{

	Snake snake;
	public Integer Score = 10;
	public JButton connect;
	private JButton disconnect;
	private JButton quitGame;
	private JButton play;
	private JScrollPane scrollPane;
	private JPanel panel1, panel2, panel3, panel4, scorePanel;
	
	public TextField opponentField, textField;
	private TextField tfServer, tfPort;
	private JLabel label, usernameLabel, scoreLabel, opponentScore;
	
	public  boolean connected;
	private int defaultPort;
	private String defaultHost;
	public Client client;
	
	public static Server server;
	
	
	public int x;
	
	
	private TextArea textArea = new TextArea("Вы можете писать здесь:" + "\n",26,80);
	public Dimension dim;
	public static Game Game;
	
	public int getSize ; 
	
	public Game() 
	{
		super("Змейка");
		
		
		
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		setVisible(true); // Сделать окно видимым

		setResizable(false); // Запретить изменение размеров окна пользователем
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(dim); //  Установить размер окна равным размеру экрана
		setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2 - getHeight() / 2); // Устанавливает позицию окна посередине экрана, учитывая его размер
	}
	
	
	private void run(String host, int port)
	{
		
		
		
		defaultPort = port;
		defaultHost = host;
		
		tfServer = new TextField(host);
		tfPort = new TextField("" + port);
		
		connect = new JButton();
		connect.setText("Подключиться");
		connect.addActionListener(this);
		
		disconnect = new JButton();
		disconnect.setText("Отключиться");
		disconnect.addActionListener(this);
		disconnect.setEnabled(false);
	
		play = new JButton();
		play.setText("<<< Играть");
		play.addActionListener(this);
		
		quitGame = new JButton();
		quitGame.setText("Выйти");
		quitGame.addActionListener(this);
		
		
		
		panel1 = new JPanel();
		panel1.add(play);
		panel1.add(new JLabel("Номер порта:"));
		panel1.add(new TextField(" "));	

		panel1.add(connect);
		panel1.add(disconnect);
		panel1.add(quitGame);
	
		
		panel2 = new JPanel();
		label = new JLabel("Введите свой никнейм",SwingConstants.CENTER);
		textField = new TextField("Nier",55);
		
		panel2.add(label);
		panel2.add(textField);

		panel3 = new JPanel();
		panel3.add(textArea);
		textArea.setEditable(false);
	
		panel4 = new JPanel();

		
		
		Box box = Box.createVerticalBox();
	

		box.add(panel1);
		box.add(panel2);
		box.add(panel4);
		box.add(panel3);
	
		Container cp = getContentPane();
		cp.add(BorderLayout.EAST, box);

		
		snake = new Snake(Game);
		snake.startGame();
	}   
	
	public void connectionFailed()
	{
		connect.setEnabled(true);
		disconnect.setEnabled(false);
		
		label.setText("Введите свой никнейм");
		textField.setText("Anonymous");
		// Сбросить номер порта и имя хоста при создании объекта
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		// Позволить пользователю изменить их
		tfServer.setEditable(false);
		tfPort.setEditable(false);
		// Не реагировать на нажатие <CR> после имени пользователя
                // <CR> обозначает возврат курсора в начало текущей строки без перехода на следующую строку.
		textField.removeActionListener(this);
		connected = false;
	}

	public void append(String str)
	{
		System.out.println("test text:" + textArea.getText());
	//	textArea.append(str);
		textArea.setCaretPosition(textArea.getText().length() -1);
	}
	public static void main(String[] args)
	{
		Game = new Game();
		
		Game.run("localhost",1500);
		

		
	}
	
	private JPanel createChatPanel()
	{
		JPanel newPanel = new JPanel();
		newPanel.setLocation(10, 10);
		
		newPanel.setSize(10, 10);
		newPanel.setLayout(new GridBagLayout());
		JLabel label = new JLabel("Введите никнейм:");
		JTextField userName = new JTextField(20);
		 
		newPanel.add(label);
		newPanel.add(userName);
		return newPanel;
	}
	
	public int ThreadSize(int Size)
	{
		return Size;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object status = e.getSource();
	
		if(connected && status == textField  ) {
			// just have to send the message
			client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, textField.getText()));				
			textField.setText("");
		
		}
		

		if(status == play  )
		{
			snake.paused = !snake.paused;
			snake.renderPanel.setFocusable(true);
			//snake.renderPanel.setVisible(true);
			//snake.renderPanel.requestDefaultFocus();
			snake.renderPanel.requestFocusInWindow();
			//snake.renderPanel.requestFocus();
			snake.renderPanel.setVisible(true);
			Game.addKeyListener(snake.renderPanel);
//			System.out.println();
			return;
			
		}
		
		if(status == connect)
		{
			
			String username = textField.getText().trim();
			
			
			// empty username ignore it
			if(username.length() == 0)
				return;
			// empty serverAddress ignore it
			String server = tfServer.getText().trim();
			if(server.length() == 0)
				return;
			// empty or invalid port numer, ignore it
			String portNumber = tfPort.getText().trim();
			if(portNumber.length() == 0)
				return;
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			}
			catch(Exception en) {
				return;   // nothing I can do if port number is not valid
			}

			// try creating a new Client with GUI
			client = new Client(server, port, username,textArea);
			// test if we can start the Client
			if(!client.start()) 
				return;
			textField.setText("");
			label.setText("Введите своё сообщение");
			connected = true;
			
			// disable login button
			connect.setEnabled(false);
			// enable the 2 buttons
			disconnect.setEnabled(true);
			
			// disable the Server and Port JTextField
			tfServer.setEditable(false);
			tfPort.setEditable(false);
			// Action listener for when the user enter a message
			textField.addActionListener(this);
			
		}
		
		if(status == disconnect)
		{
			client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
			connect.setEnabled(true);
			disconnect.setEnabled(false);
			return;
		}
		
		if(status == quitGame)
				System.exit(0);
	}

}
