package probus.jam.entity;

import probus.jam.gameengine.ProbusJam;
import probus.jam.gameengine.Keys;
import probus.jam.graphics.*;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

import java.awt.Graphics;

public class Player extends MovingEntity {

	private int shootingTicksDelay = 12;
	private int shootingTick = 0;
	private boolean shooted = false;
	
	private Direction shootingDirection = Direction.UP;
	private Keys keys;
	
	private Entity objectHeld = null;

	public Player(GameLevel level, Keys k, int xx, int yy)
	{
		super(level, xx, yy);
		
		gameLevel = level;
		
		walkTime = 0;
		keys = k;
		
		speed = 2;
		
		bBox = new BoundingBox(this, xx, yy, 32, 48);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	public void render(Graphics g)
	{
		int spriteDirection = 0;
		spriteX = (walkTime / 4 % 4);
		
		switch(direction)
		{
		case LEFT:
			spriteDirection = 2;
			break;
			
		case RIGHT:
			spriteDirection = 3;
			break;
			
		case UP:
			spriteDirection = 0;
			break;
			
		case DOWN:
			spriteDirection = 1;
			break;
		}
		
		GameArt.hero.Draw(g, spriteX, spriteDirection, x, y);
	}
	
	public void tick()
	{
		move();
		walkTime++;
		
		if(objectHeld == null)
			fire();
	}
	
	private void move()
	{
		int beginX = x;
		int beginY = y;
		
		int moveX = 0;
		int moveY = 0;
		
		if(keys.left.isDown()) moveX--;
		if(keys.right.isDown()) moveX++;
		if(keys.up.isDown()) moveY--;
		if(keys.down.isDown()) moveY++;
		
		if(moveX < 0){ direction = Direction.LEFT; x -= speed; }
		if(moveX > 0){ direction = Direction.RIGHT; x += speed; }
		if(moveY < 0){ direction = Direction.UP; y -= speed; }
		if(moveY > 0){ direction = Direction.DOWN; y += speed; }

		
		// TP1 : Gestion de collision du joueur
		
		bBox.UpdatePosition(x, y);
		

		ProbusJam.camera.x = ProbusJam.VIEWPORT_WIDTH / 2 - x - 16;
		ProbusJam.camera.y = ProbusJam.VIEWPORT_HEIGHT / 2 - y - 16;
		
		if(shooted == true)
			direction = shootingDirection;
		
		if(moveX == 0 && moveY == 0)
			walkTime = 0;
		
		if(objectHeld != null)
		{
			objectHeld.setPosition(x, y);
		}
	}
	
	private void fire()
	{
		if(shooted == true)
		{
			shootingTick++;
			
			if(shootingTick >= shootingTicksDelay)
			{
				shooted = false;
			}
		}
		
		if(shooted == false)
		{
			if(keys.fireUp.isDown()){ new Bullet(gameLevel, x + 12, y - 32, 0, -2); shootingDirection = Direction.UP; shooted = true;}
			else if(keys.fireDown.isDown()){ new Bullet(gameLevel, x + 12, y + 60, 0, 2); shootingDirection = Direction.DOWN; shooted = true;}
			else if(keys.fireLeft.isDown()){ new Bullet(gameLevel, x - 32, y + 24, -2, 0); shootingDirection = Direction.LEFT; shooted = true;}
			else if(keys.fireRight.isDown()){ new Bullet(gameLevel, x + 48, y + 24, 2, 0); shootingDirection = Direction.RIGHT; shooted = true;}
			
			if(shooted == true)
				shootingTick = 0;
		}
	}
}
