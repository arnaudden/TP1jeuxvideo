package probus.jam.entity;

import java.awt.Graphics;
import java.util.ArrayList;

import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

public class Bullet extends MovingEntity {

	private GameLevel gameLevel;
	private int velX;
	private int velY;
	
	private long ticksLived = 0;
	private long ticksToKill = 60;
	
	public Bullet(GameLevel level, int xx, int yy, int vx, int vy)
	{
		super(level, xx, yy);
		
		gameLevel = level;
		
		velX = vx;
		velY = vy;
		
		speed = 4;
		
		bBox = new BoundingBox(this, x, y, 8, 8);
		level.addEntity(this);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	private void kill()
	{
		gameLevel.removeEntity(this);
	}
	
	public void tick()
	{
		move();
		
		ticksLived++;
		
		if(ticksLived >= ticksToKill)
			kill();
	}
	
	public void render(Graphics g)
	{
		GameArt.bullet.Draw(g, 0, 0, x, y);
	}
	
	private void move()
	{
		x += velX * speed;
		y += velY * speed;
		
		bBox.UpdatePosition(x, y);
		
		// TP1 : Gestion collision des balles
	}
}
