import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;



public class SUKSprite implements DisplayableSprite, MovableSprite {
	
	private static final int PERIOD_LENGTH = 200;			
	private static final int IMAGES_IN_CYCLE = 5;
	//required for advanced collision detection
	private CollisionDetection collisionDetection;

	
	private long elapsedTime = 0; 
	private static Image[] images;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 100;
	private double height = 100;
	private boolean dispose = false;	
	
	//PIXELS PER SECOND PER SECOND
	private final double ACCCELERATION_X = 10000;
	private final double ACCCELERATION_Y = 800;
	private final double MAX_VELOCITY_X = 300;
	private final double DEACCELERATION_X = 1000;
	private final double MINIMUM_X_VELOCITY = 1;
	private final double INITIAL_JUMP_VELOCITY = 600;
	private double velocityX = 0;
	private double velocityY = 0;
	private boolean hit = false;
	int facingDirection = 0;
	
	private boolean isJumping = false;
	
	private Direction direction = Direction.DOWNLEFT;
	private enum Direction { RIGHT(0), LEFT(1), UPRIGHT(2), UPLEFT(3), DOWNRIGHT(4), DOWNLEFT(5), NULLLEFT(6), NULLRIGHT(7);
		private int value = 0;
		private Direction(int value) {
			this.value = value; 
		} 
	};

	
	
//**************************************************************************************************************************************************************************************************//
//**************************************************************************************************************************************************************************************************//
	
	
	public SUKSprite(double centerX, double centerY, double height, double width) {
		this(centerX, centerY);
		
		this.height = height;
		this.width = width;
	}
	
	public SUKSprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;
		
		collisionDetection = new CollisionDetection();
		collisionDetection.setBounceFactorY(0);
		collisionDetection.setBounceFactorX(0);
		
		if (images == null) {
			try {
				images = new Image[40];
				for (int i = 0; i < 40; i++) {
					String path = String.format("res/PlayerRes/Player%d.png",i);
					images[i] = ImageIO.read(new File(path));
				}
			}
			catch (IOException e) {
				System.err.println(e.toString());
			}		
		}		
	}
	
	public Image getImage() {
		
		
		//calculate how many periods of 200 milliseconds have elapsed since this sprite was instantiated?
		long period = elapsedTime / PERIOD_LENGTH;
		//calculate which image (aka 'frame') of the sprite animation should be shown out of the cycle of images
		int image = (int) (period % IMAGES_IN_CYCLE);
		//calculate index into array of all images. this is an arbitrary value, depending on how the image files are ordered
		int index = direction.value * IMAGES_IN_CYCLE + image;
						
		return images[index];
				
	}

	//##################//
	/* 	SET CENTER X/Y 	*/
	//##################//
	
	@Override
	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}


	@Override
	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	//##################//
	/* 	SET VELOCITY 	*/
	//##################//

	@Override
	public void setVelocityX(double pixelsPerSecond) {
		this.velocityX = pixelsPerSecond;
	}


	@Override
	public void setVelocityY(double pixelsPerSecond) {
		this.velocityY = pixelsPerSecond;
	}

	@Override
	public boolean getVisible() {
		return true;
	}

	//##################//
	/* 	GET MIN/MAX		*/
	//##################//
	
	@Override
	public double getMinX() {
		return centerX - (width / 2);
	}


	@Override
	public double getMaxX() {
		return centerX + (width / 2);
	}


	@Override
	public double getMinY() {
		return centerY - (width / 2);
	}


	@Override
	public double getMaxY() {
		return centerY + (width / 2);
	}

	//######################//
	/* 	GET HEIGHT/WIDTH 	*/
	//######################//
	
	@Override
	public double getHeight() {
		return height;
	}


	@Override
	public double getWidth() {
		return width;
	}

	//##################//
	/* 	GET CENTER X/Y 	*/
	//##################//
	
	@Override
	public double getCenterX() {
		return centerX;
	}


	@Override
	public double getCenterY() {
		return centerY;
	}


	@Override
	public boolean getDispose() {
		return dispose ;
	}


	@Override
	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}
	
	public void setDirection(){

	}
	
	public void setDirection(boolean movingRight, boolean movingLeft){
		//DOWN
		if (velocityY > 50) {//placeholder until I fix falling detection
			if(facingDirection == 1){//left
				this.direction = direction.DOWNLEFT;
			}
			else if(facingDirection == -1){//right
				this.direction = direction.DOWNRIGHT;
			}
			else {
			}
		}
			
		//UP
		else if (isJumping == true) {
			if(facingDirection == 1){//left
				this.direction = direction.UPLEFT;
			}
			else if(facingDirection == -1){//right
				this.direction = direction.UPRIGHT;
				}
			else {
			}
		}
		//LEFT
		else if (movingLeft) {
			this.direction = direction.LEFT;
		}
		//RIGHT
		else if (movingRight) {
			this.direction = direction.RIGHT;
		}
		//IDLE
		else {
			if(facingDirection == 1){//left
				this.direction = direction.NULLLEFT;
			}
			else if(facingDirection == -1){//right
				this.direction = direction.NULLRIGHT;
			}
			else {
			}
		}
	}


	@Override
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {


		boolean onGround = onGround(universe.getSprites());
		elapsedTime += actual_delta_time;
		
		if (onGround) {
			if (keyboard.keyDown(32)) {
				
				this.isJumping = true;
				this.velocityY -= INITIAL_JUMP_VELOCITY;
				onGround = false;
			}
		}
		

		// RIGHT
		if (keyboard.keyDown(39) && !hit) {
				//velocityX will increase by a constant amount, up to a maximum
				velocityX += actual_delta_time * 0.001 * ACCCELERATION_X;
				setDirection(true,false);
				facingDirection = -1;
				if (velocityX > MAX_VELOCITY_X) {
					velocityX = MAX_VELOCITY_X;
				}
			}
		
		// LEFT
		else if (keyboard.keyDown(37) && !hit) {
				//velocityX will decrease by a constant amount, down to a minimum
				facingDirection = 1;
				setDirection(false,true);
				velocityX -= actual_delta_time * 0.001 * ACCCELERATION_X;
				if (velocityX < - MAX_VELOCITY_X) {
					velocityX = - MAX_VELOCITY_X;
				}
			}
		// STOPPING
		else {
				//if not moving left or right, then velocity will deaccelerate
				//note the use of a practical limit to zero the movement; otherwise, velocity would never be exactly zero
				if (Math.abs(this.velocityX) > MINIMUM_X_VELOCITY) {
					setDirection(false,false);
					this.velocityX /= actual_delta_time * 0.001 *  DEACCELERATION_X * Math.signum(this.velocityX);
				}
				else {
					setDirection(false,false);
					this.velocityX = 0;
			}
		}

		double deltaX = actual_delta_time * 0.001 * velocityX;
		double deltaY = actual_delta_time * 0.001 * velocityY;
		
		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX + 5, 0);
		boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY);
		boolean collidingBoss = checkCollisionWithBoss(universe.getSprites(), deltaX, deltaY);

		
		
		if (collidingBoss) {
			this.velocityY = -500;
			this.velocityX = 0;
			this.hit = true;
		}

		if (collidingBarrierX == true) {
			this.velocityX = 0;
			}
			
		if (collidingBarrierY) {
			this.velocityY = 0;
		}
		
		
		


		if (onGround == true) {
			this.velocityY = 0;
			this.isJumping = false;
		} else {
			this.velocityY = this.velocityY + ACCCELERATION_Y * 0.001 * actual_delta_time;
		}

		this.centerX += actual_delta_time * 0.001 * velocityX;
		this.centerY += actual_delta_time * 0.001 * velocityY;
		this.hit = false;

	}

//**************************************************************************************************************************************************************************************************//
//**************************************************************************************************************************************************************************************************//
	
	private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite) {
				if (CollisionDetection.overlaps(this.getMinX() + deltaX, this.getMinY() + deltaY, 
						this.getMaxX()  + deltaX, this.getMaxY() + deltaY, 
						sprite.getMinX(),sprite.getMinY(), 
						sprite.getMaxX(), sprite.getMaxY())) {
					colliding = true;
					break;					
					}
				}
			}		
		return colliding;	
		}
	private boolean checkCollisionWithBoss(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof Boss) {
				
				
				if (CollisionDetection.pixelBasedOverlaps(this, sprite, deltaX, deltaY)) {
					colliding = true;
					break;					
				}
			}
		}		
		return colliding;		
	}
	
	private boolean onGround(ArrayList<DisplayableSprite> sprites) {
		boolean ground = false;
		for (DisplayableSprite sprite: sprites) {
			if (sprite instanceof BarrierSprite) {
				boolean bottomColiding = this.getMaxY() + 5 >= (sprite.getMinY()) && this.getMaxY() <= sprite.getMinY();
				boolean withinRange = this.getMaxX() > sprite.getMinX() && this.getMinX() < sprite.getMaxX();
			
				if (bottomColiding && withinRange) {
					ground = true;
					break;
				}
			}
		}
		return ground;
	}
	

}