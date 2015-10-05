package probus.jam.entity;

import java.awt.Graphics;

import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;


public abstract class Entity {

	public enum Direction
	{
		LEFT,
		RIGHT,
		UP,
		DOWN
	}
	
	public int x;
	public int y;
	protected BoundingBox bBox;
	protected Direction direction;
	
	protected GameLevel gameLevel;
	
	public Entity(GameLevel level, int xx, int yy)
	{
		x = xx;
		y = yy;
		
		gameLevel = level;
	}
	
	public BoundingBox getBox()
	{
		return bBox;
	}
	
	public abstract void init();
	public abstract void tick();
	public abstract void render(Graphics g);
	
	public void hit()
	{
	}
	
	protected void setPosition(int xx, int yy)
	{
		x = xx;
		y = yy;
	}
	
	public void Activate()
	{
	}
	
	public void AltActivate()
	{
	}
	
	public void manageBeamCollision() {
	}

	public boolean beamCanPass() {
		return true;
	}
	
	public void debugRender(Graphics g)
	{
		if(bBox == null)
			return;
		
		bBox.DebugRender(g);
	}
}
