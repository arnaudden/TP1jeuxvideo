package probus.jam.entity.tiles;
	
import probus.jam.graphics.*;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

import java.awt.Graphics;

public class CollidableSandTile extends Tile {
	
	public CollidableSandTile(GameLevel level, int xx, int yy)
	{
		super(level, xx,yy);
		bBox = new BoundingBox(this, xx,yy,32,32);
	}
	
	public void render(Graphics g)
	{
		GameArt.wall.Draw(g, x, y);
	}
}
