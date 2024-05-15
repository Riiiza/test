
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

/**
 * @author Jaryt Bustard
 */
public class Snake  implements Runnable, ActionListener,KeyListener
{

	public static Snake snake;
	public RenderPanel renderPanel;
	public Timer timer = new Timer(10, this);

	public ArrayList<Point> snakeParts = new ArrayList<Point>();

	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, SCALE = 10;
	private Game game;
	public int ticks = 0, direction = 4, tailLength = 10, time;
	public Integer score;
	public Point head, cherry;

	public Random random;
	public Server server;
	public boolean over = false, paused;
	//public Client client;
	public Dimension dim;
	int x;
	public ChatMessage testMsg ;
	
	int NumberOfThreads;
	
	public Snake(Game g)
	{
	
		game = g;
		dim = g.dim;
		g.add(renderPanel = new RenderPanel(this));
		g.addKeyListener(this);
		
	}

	
	
	public void startGame()
	{

		//if(server.getNumberOfThreads(x) >1)
		
		{
		over = false;
		paused = true;
		time = 0;
		score = 0;
		tailLength = 14;
		ticks = 0;
		direction = DOWN;
		head = new Point(0, -1);
		random = new Random();
		snakeParts.clear();
		cherry = new Point(random.nextInt(67), random.nextInt(63));
		
		timer.start();
		}
	}

	private void addKeyListener(Snake snake2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		renderPanel.repaint();
		ticks++;
		

	//  testMsg = new ChatMessage(ChatMessage.SCORE,String.valueOf(snake.GetScore()));
		
		if (ticks % 2 == 0 && head != null && !over && !paused)
		{
			time++;

			snakeParts.add(new Point(head.x, head.y));
			
			
			
			if (direction == UP)
			{
				
				if (head.y - 1 >= 0 && noTailAt(head.x, head.y - 1))
				{
					head = new Point(head.x, head.y - 1);
				}
				else
				{
					
					testMsg = new ChatMessage(ChatMessage.SCORE,String.valueOf(String.valueOf(GetScore())));
					game.client.sendMessage(testMsg);
					over = true;
					

				}
			}

			if (direction == DOWN)
			{
				if (head.y + 1 < 64 && noTailAt(head.x, head.y + 1))
				{
					head = new Point(head.x, head.y + 1);
				}
				else
				{
					
					testMsg = new ChatMessage(ChatMessage.SCORE,String.valueOf(String.valueOf(GetScore())));
					game.client.sendMessage(testMsg);
					over = true;
					
				}
			}

			if (direction == LEFT)
			{
				if (head.x - 1 >= 0 && noTailAt(head.x - 1, head.y))
				{
					head = new Point(head.x - 1, head.y);
				}
				else
				{
					testMsg = new ChatMessage(ChatMessage.SCORE,String.valueOf(String.valueOf(GetScore())));
					game.client.sendMessage(testMsg);
					over = true;
					
				}
			}

			if (direction == RIGHT)
			{
				if (head.x + 1 < 68 && noTailAt(head.x + 1, head.y))
				{
					head = new Point(head.x + 1, head.y);
				}
				else
				{
					testMsg = new ChatMessage(ChatMessage.SCORE,String.valueOf(String.valueOf(GetScore())));
					game.client.sendMessage(testMsg);
					over = true;
				}
			}

			if (snakeParts.size() > tailLength)
			{
				snakeParts.remove(0);
			}

			if (cherry != null)
			{
				if (head.equals(cherry))
				{
					score += 100;
				
					tailLength = tailLength + 5;
					cherry.setLocation(random.nextInt(67), random.nextInt(60));
		
				}
			}
		}
	
	}

	public boolean noTailAt(int x, int y)
	{
		
		for (Point point : snakeParts)
		{
			
			if (point.equals(new Point(x, y)))
			{
			
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		int i = e.getKeyCode();

		if ((i == KeyEvent.VK_A || i == KeyEvent.VK_LEFT) && direction != RIGHT)
		{
			direction = LEFT;
		}

		if ((i == KeyEvent.VK_D || i == KeyEvent.VK_RIGHT) && direction != LEFT)
		{
			direction = RIGHT;
		}

		if ((i == KeyEvent.VK_W || i == KeyEvent.VK_UP) && direction != DOWN)
		{
			direction = UP;
		}

		if ((i == KeyEvent.VK_S || i == KeyEvent.VK_DOWN) && direction != UP)
		{
			direction = DOWN;
		}
		
		if(i == KeyEvent.VK_T)
		{

			if(direction == DOWN)
			{
				head = new Point(head.x, head.y +direction *2);
			}
			if(direction == UP)
			{
				head = new Point(head.x, head.y - direction*3 );
			}
			if(direction == LEFT)
			{
				head = new Point(head.x - direction, head.y);
			}
			if(direction == RIGHT)
			{
				head = new Point(head.x + direction, head.y);
			}
		}


	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		
	}
	public Integer GetScore()
	{
		return score;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}
        
        //        Турбо для змейки
        //Получение идентификатора с компьютера
        //Каждое сообщение, отправляемое на сервер, представлено в виде строк
        //Клиент и сервер должны иметь структуру для хранения этих сообщений
        //Функция для проверки, является ли это сообщение клиент-серверным
        //Тег добавляется с помощью ключевого слова "super"
        //Клиент-серверное сообщение должно иметь функцию для разбора всех сообщений (для отделения от тегов)

	@Override
	public void run() {
		// TODO Auto-generated method stub
	
	}
	
	
	
        //        Попробуйте сначала создать csMessage
        //Соединение змейки и клиента (чат в обоих)
        //Результат игры в чате
        //Сохранение имени и результата в базу данных (СУБД) 1 таблица: имя и результат
	
        
        //        Попробуйте сначала создать csMessage
        //Соединение змейки и клиента (чат в обоих)
        //Результат игры в чате
        //Сохранение имени и результата в базу данных (СУБД) 1 таблица: имя и результат
	
}