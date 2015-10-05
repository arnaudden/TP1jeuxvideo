package probus.jam.gameengine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import probus.jam.levels.GameLevel;

public class ProbusJam extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	
	public static final int VIEWPORT_WIDTH = 800;
	public static final int VIEWPORT_HEIGHT = 600;
	
	public static Random gameRandomGenerator = new Random();
	private static JFrame mainWindow;
	private boolean running;
	
	private Keys keys = new Keys();
	
	GameLevel level;
	public static Camera camera = new Camera(0, 0);
	
	public static void main(String[] args)
	{
		ProbusJam manager = new ProbusJam();
		manager.setSize(800, 600);
		mainWindow = new JFrame();
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(manager);
		
		mainWindow.setContentPane(panel);
		mainWindow.pack();
		mainWindow.setResizable(false);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		manager.start();
	}
	
	public ProbusJam()
	{
		addKeyListener(new InputHandler(keys));
		level = new GameLevel(keys);
	}
	
	@Override
	public void run()
	{
		setFocusTraversalKeysEnabled(false);
        requestFocus();
        
        long lastTimeFPS = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        
        double framerate = 60;
        double nsPerTick = 1000000000.0 / framerate;
        boolean shouldRender = false;
        double nbTick=0;
        
        
        while(running)
        {
        	shouldRender = false;
            
            // TP1 : Gestion des ticks
        	unprocessed+=(System.nanoTime() - lastTime) / nsPerTick;
        	
        
        	lastTime = System.nanoTime();
        	
        	
        	for(double i=1; i<unprocessed;i++ )
        	{
        		tick();
        		//System.out.println("tick exécuté");
        		unprocessed--;
        		nbTick++;
        		shouldRender=true;
        		//System.out.println(nbTick);
        		
        	}
        	
        	long timeFPS =  System.currentTimeMillis() - lastTimeFPS;
        	if (timeFPS > 1000)
        	{
        		System.out.println("FPS : " + nbTick);
        		mainWindow.setTitle("TP 1 FPS : " + nbTick);
        		lastTimeFPS = System.currentTimeMillis();
        		nbTick = 0;
        		
        	}
        	

            if(shouldRender)
            {
            	// TP1 : Gestion de l'affichage
            	mainWindow.createBufferStrategy(3);
            	BufferStrategy buffer = mainWindow.getBufferStrategy();
            	Graphics graph = buffer.getDrawGraphics();
            	Render(graph);
            	graph.dispose();
            	
            
            }
            
            
            // Delay processing
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }            
        }
	}
	
	private void tick()
	{
		keys.tick();
		level.tick();
	}
	
	private void Render(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if (level != null) {
			g.translate(camera.x, camera.y);
			
			level.render(g);
			g.setClip(null);
			
			g.translate(-camera.x, -camera.y);
		}
	}
	
	public void start()
	{
		Thread thread = new Thread(this);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        
        running = true;
	}
	
	public void keyTyped(KeyEvent e)
	{
    }
	
	public void keyPressed(KeyEvent e)
	{
	}
	
	public void keyReleased(KeyEvent e)
	{}
}
