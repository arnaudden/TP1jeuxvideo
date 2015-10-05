package probus.jam.entity;

import java.awt.Graphics;

import probus.jam.gameengine.ProbusJam;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

public class Mob extends MovingEntity {

	enum State
	{
		WAITING,
		WALK
	}
	
	private int walkTicksDelay = 20;
	private int waitingTicksDelay = 100;
	
	private State state = State.WAITING;
	
	private int health = 10;
	
	public Mob(GameLevel level, int xx, int yy)
	{
		super(level, xx, yy);
		speed = 1;
		
		bBox = new BoundingBox(this, xx, yy, 48, 48);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	public void tick()
	{
		int beginX = x;
		int beginY = y;
		
		wander();

		// TP1 : Gestion de collision des ennemies
		
		bBox.UpdatePosition(x, y);
	}
	
	public void render(Graphics g)
	{
		if(state == State.WALK)
		{
			spriteX = (walkTime / 8 % 4);
		}
		
		if(direction == Direction.DOWN) GameArt.mommy.Draw(g, spriteX, 1, x, y);
		else if(direction == Direction.UP) GameArt.mommy.Draw(g, spriteX, 0, x, y);
		else if(direction == Direction.LEFT) GameArt.mommy.Draw(g, spriteX, 2, x, y);
		else if(direction == Direction.RIGHT) GameArt.mommy.Draw(g, spriteX, 3, x, y);
	}
	
	public void hit()
	{
		health--;
		
		if(health <= 0)
			kill();
	}
	
	private void kill()
	{
		gameLevel.removeEntity(this);
	}
	
	private void wander()
	{
		walkTime++;
		
		if(state == State.WAITING)
		{
			if(walkTime >= waitingTicksDelay)
			{
				state = State.WALK;
				walkTime = 0;
				
				int rand = ProbusJam.gameRandomGenerator.nextInt(2);
				
				if(rand == 1)
				{
					int dir = ProbusJam.gameRandomGenerator.nextInt(4);
					
					if(dir == 0) direction = Direction.DOWN;
					else if(dir == 1) direction = Direction.UP;
					else if(dir == 2) direction = Direction.LEFT;
					else if(dir == 3) direction = Direction.RIGHT;
				}
			}
		}
		
		else if(state == State.WALK)
		{
			if(walkTime < walkTicksDelay)
			{
				if(direction == Direction.DOWN) y += speed;
				else if(direction == Direction.UP) y -= speed;
				else if(direction == Direction.LEFT) x -= speed;
				else if(direction == Direction.RIGHT) x += speed;
			}
			
			else
			{
				state = State.WAITING;
				walkTime = 0;
			}
		}
	}
}
