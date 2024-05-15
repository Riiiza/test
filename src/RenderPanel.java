import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
/**
 * @author Jaryt Bustard
 */
public class RenderPanel extends JPanel implements KeyListener , ActionListener
{
	Snake snake;
	public Game game;
	
	public RenderPanel(Snake snake)
	{
		super();
		this.snake = snake;
		addKeyListener(this);
	
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		// snake 
		
		g.setColor(Color.BLACK);
		
		g.fillRect(0, 0, 800, 650);

		g.setColor(Color.WHITE);

		for (Point point : snake.snakeParts)
		{
			g.fillRect(point.x * Snake.SCALE, point.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
		}
		
		g.fillRect(snake.head.x * Snake.SCALE, snake.head.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
		
		g.setColor(Color.RED);
		
		g.fillRect(snake.cherry.x * Snake.SCALE, snake.cherry.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
		
		String string = "My Score:" + snake.score ; 
		
		g.setColor(Color.white);
		
		g.drawString(string, (int) (getWidth() / 2 - string.length() * 2.5f), 10);

		string = "Конец игры!  \n  Чтобы начать заново, нажмите любую клавишу и нажмите 'Играть' ";

		if (snake.over)
		{
			g.drawString(string, (int) (getWidth() / 2 - string.length() * 2.5f), (int) snake.dim.getHeight() / 4);
		}

		string = "Pauza!";

		if (snake.paused && !snake.over)
		{
			if (string == null)
				System.out.println("String is null");
			
			g.drawString(string, (int) (getWidth() / 2 - string.length() * 2.5f), (int) snake.dim.getHeight() / 4);
		}
		
	
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int i = e.getKeyCode();
		
//		
//			if (i == KeyEvent.VK_R)
//			{
				if (snake.over)
				{
					snake.startGame();
				}
//				else 
//				{
//					snake.paused = !snake.paused;
//				}
		
		
				
		if ((i == KeyEvent.VK_A || i == KeyEvent.VK_LEFT) && snake.direction != snake.RIGHT)
		{
			snake.direction = snake.LEFT;
		}
	
		if ((i == KeyEvent.VK_D || i == KeyEvent.VK_RIGHT) && snake.direction != snake.LEFT)
		{
			snake.direction = snake.RIGHT;
		}

		if ((i == KeyEvent.VK_W || i == KeyEvent.VK_UP) && snake.direction != snake.DOWN)
		{
			snake.direction = snake.UP;
		}

		if ((i == KeyEvent.VK_S || i == KeyEvent.VK_DOWN) && snake.direction != snake.UP)
		{
			snake.direction = snake.DOWN;
		}

		if(i == KeyEvent.VK_T)
		{
//			TURBO !!!!!!!!!!!
			if(snake.direction == snake.DOWN)
				snake.head = new Point(snake.head.x, snake.head.y +snake.direction *2);
			if(snake.direction == snake.UP)
				snake.head = new Point(snake.head.x, snake.head.y - snake.direction*3 );
			if(snake.direction == snake.LEFT)
				snake.head = new Point(snake.head.x - snake.direction, snake.head.y);
			if(snake.direction == snake.RIGHT)
				snake.head = new Point(snake.head.x + snake.direction, snake.head.y);
		}
	}
	

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
}