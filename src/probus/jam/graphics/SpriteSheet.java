package probus.jam.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.*;

import probus.jam.gameengine.GraphicConfiguration;
import probus.jam.gameengine.ProbusJam;

public class SpriteSheet {
	
	private class Sprite
	{
		public int X;
		public int Y;
		
		public Sprite(int x, int y)
		{
			X=x;
			Y=y;
		}
	}
		
		private BufferedImage image = null;
		
		private int width;
		private int height;
		private Sprite[][] sprites;
		
		public SpriteSheet(String path, int w, int h)
		{
			LoadImage(path, w, h);
		}
		
		public void LoadImage(String path,int w, int h)
		{
			try
			{
				image = ImageIO.read(ProbusJam.class.getResource(path));
				
				if(image.getColorModel().equals(GraphicConfiguration.config.getColorModel()))
				{
					image.setAccelerationPriority(1.0f);
				}
				else
				{
					BufferedImage new_image = GraphicConfiguration.config.createCompatibleImage(image.getWidth(),image.getHeight(),image.getTransparency());
					
					Graphics2D g2d = (Graphics2D) new_image.getGraphics();
					
					g2d.drawImage(image,0,0,null);
					g2d.dispose();
					
					new_image.setAccelerationPriority(1.0f);
					image = new_image;
				}
				
				sprites = new Sprite[image.getWidth()/w][image.getHeight()/h];
				
				for(int i=0;i<image.getWidth()/w;i++)
				{
					for(int j=0;j<image.getHeight()/h;j++)
					{
						sprites[i][j] = new Sprite(i*w,j*h);
					}
				}
				
				width = w;
				height = h;
			}
			catch(IOException e)
			{}
		}

		public void Draw(Graphics g, int i, int j, int x, int y)
		{
			Sprite sprite = sprites[i][j];
			
			BufferedImage subImage = image.getSubimage(sprite.X,sprite.Y, width,height);
			
			g.drawImage(subImage, x, y, null);
		}
	}
