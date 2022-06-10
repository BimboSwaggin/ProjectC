 import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Boss implements DisplayableSprite {

	private static Image image;
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;	
	
	private int velocity = 90;
	private double velocityX = 0;
	private double velocityY = 0;
	
	public Boss(double centerX, double centerY) {
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/blue-barrier.png"));
			}
			catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		this.centerX = centerX;
		this.centerY = centerY;
		
	}
	

	public Image getImage() {
		return image;
	}
	
	public double getMinX() {
		return centerX - (width / 2);
	}

	public double getMaxX() {
		return centerX + (width / 2);
	}

	public double getMinY() {
		return centerY - (height / 2);
	}

	public double getMaxY() {
		return centerY + (height / 2);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getCenterX() {
		return centerX;
	};

	public double getCenterY() {
		return centerY;
	};
	
	
	public boolean getDispose() {
		return dispose;
	}

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}
	
	public boolean getVisible() {
		return true;
	}

	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		
		double deltaX = actual_delta_time * 0.001 * velocityX;
		double deltaY = actual_delta_time * 0.001 * velocityY;
		boolean proximity = checkCollisionWithOtherSprite(universe.getSprites(), deltaX, deltaY);
		double playerCenterX = getPlayerCenterX(universe.getSprites());
		
		if (proximity == false) {
			
			double relativeX = this.centerX - universe.getPlayer1().getCenterX();
			
			if (relativeX < 0) { 
				this.centerX += actual_delta_time * 0.001 * velocity;
			}
			else {
				this.centerX += actual_delta_time * 0.001 * -velocity;
			}
		}
	}
	private boolean checkCollisionWithOtherSprite(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		boolean close = false;
		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof SUKSprite) {
				double distanceX = this.centerX - sprite.getCenterX();
				double distanceY = this.centerY - sprite.getCenterY();
				double radialDistance = Math.sqrt((distanceY * distanceY) + (distanceX * distanceX));
				if (radialDistance <= 100) {
					close = true;
				}
				else {
					close = false;
				}
			}
		}
		return close;
	}
	
	private double getPlayerCenterX(ArrayList<DisplayableSprite> sprites) {
		double centerX = 0;
		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof SUKSprite) {
				centerX = sprite.getCenterX();
				}
			}
		return centerX;
	}


	@Override
	public boolean isHit() {
		// TODO Auto-generated method stub
		return false;
	}
	


}
