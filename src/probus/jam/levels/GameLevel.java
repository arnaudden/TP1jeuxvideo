package probus.jam.levels;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import probus.jam.gameengine.ProbusJam;
import probus.jam.entity.Entity;
import probus.jam.entity.YOrderComparator;
import probus.jam.entity.Player;
import probus.jam.entity.Mob;
import probus.jam.entity.tiles.SandTile;
import probus.jam.entity.tiles.CollidableSandTile;
import probus.jam.entity.tiles.Tile;
import probus.jam.gameengine.Keys;
import probus.jam.physics.BoundingBox;

public class GameLevel {

	int width;
	int height;
	
	Tile[][] tiles;
	List<Entity> entities;
	
	private int spawnDelay = 50;
	private int spawnTimer = 0;
	
	public GameLevel(Keys keys)
	{
		entities = new ArrayList<Entity>();
		entities.add(new Player(this, keys, 64, 64));
		entities.add(new Mob(this, 100, 100));
		
		width = 42;
		height = 42;
		
		tiles = new Tile[width][height];
		
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				if((i == tiles.length)||(i == 0)||(j == 0)||(j == tiles[i].length))
				{
					tiles[i][j] = new CollidableSandTile(this, i*32,j*32);
				}
				else 
				{
					tiles[i][j] = new SandTile(this, i * 32, j * 32);
				}
			}
		}
	}
	
	public void addEntity(Entity e)
	{
		if(e != null)
			entities.add(e);
	}
	
	public void removeEntity(Entity e)
	{
		entities.remove(e);
	}
	
	public void tick()
	{
		// random spawn
		spawnTimer++;
		
		if(spawnTimer >= spawnDelay)
		{
			int x = ProbusJam.gameRandomGenerator.nextInt(width - 1) + 1;
			int y = ProbusJam.gameRandomGenerator.nextInt(height - 1) + 1;
			
			if(canAddMob(x, y))
				entities.add(new Mob(this, x * 32, y * 32));
			
			spawnTimer = 0;
		}
		
		// tick entities
		ArrayList<Entity> entitiesToTick = new ArrayList<Entity>(entities);
		
		for(int i = 0; i < entitiesToTick.size(); i++)
		{
			entitiesToTick.get(i).tick();
		}
	}
	
	public ArrayList<BoundingBox> getCollisions(BoundingBox box)
	{
		ArrayList<BoundingBox> collided = new ArrayList<BoundingBox>();
		
		// Tiles
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				BoundingBox bb = tiles[i][j].getBox();
				
				if(bb != null && box.Collided(bb) && bb != box)
				{
					collided.add(bb);
				}
			}
		}
		
		// Entities
		for(int i = 0; i < entities.size(); i++)
		{
			BoundingBox bb = entities.get(i).getBox();
			
			if(bb != null && box.Collided(bb) && bb != box)
			{
				collided.add(bb);
			}
		}
		
		return collided;
	}
	
	public boolean canAddMob(int x, int y)
	{		
		BoundingBox bb = new BoundingBox(null, (x * 32) - 16, (y * 32) - 16, 64, 64);
		
		for(int a = 0; a < entities.size(); a++)
		{
			if(bb.Collided(entities.get(a).getBox()))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public Tile getCurrentTile(BoundingBox box)
	{
		int centerX = box.getCenterX();
		int centerY = box.getCenterY();
		
		return getTileAtPosition(centerX, centerY);
	}
	
	public Tile getTileAtPosition(int x, int y)
	{		
		int i = x / 32;
		int j = y / 32;
		
		if(i >= 0 && i < tiles.length)
		{
			if(j >= 0 && j < tiles[i].length)
			{
				return tiles[i][j];
			}
		}
		
		return null;
	}
	
	public void render(Graphics g)
	{
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				tiles[i][j].render(g);
			}
		}
		
		Collections.sort(entities, new YOrderComparator());
		
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).render(g);
		}
		
		// Debug
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				tiles[i][j].debugRender(g);
			}
		}
		
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).debugRender(g);
		}
	}
}
