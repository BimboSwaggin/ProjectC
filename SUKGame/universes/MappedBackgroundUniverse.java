import java.util.ArrayList;

public class MappedBackgroundUniverse implements Universe {


	private boolean complete = false;	
	private Background background = null;
	private Background background2 = null;
	private ArrayList<Background> backgrounds = null;
	private DisplayableSprite player1 = null;
	private DisplayableSprite boss = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private double xCenter = 0;
	private double yCenter = 0;
	private static final double GROUND_Y = SUKBackground.TILE_WIDTH * 2; 
	
	private DisplayableSprite floor = null;
	private DisplayableSprite wall = null;
	private DisplayableSprite wall2 = null;
	private DisplayableSprite platform = null;
	private DisplayableSprite platform2 = null;

	public MappedBackgroundUniverse () {

		background = new SUKBackground();
		background.setShiftY(-660);
		background2 = new SUKBackground2();
		background2.setShiftY(50);

		floor = new BarrierSprite(-800,-10,800,0,true,0,55);
		wall = new BarrierSprite(0,0,4,400,true,800,-110 );
		wall2 = new BarrierSprite(0,0,4,400,true,-800,-400 );
		platform = new BarrierSprite(200,0,400,4,true,300,-155 );
		platform2 = new BarrierSprite(200,0,400,4,true,-300,-155 );
		
		backgrounds =new ArrayList<Background>();
		backgrounds.add(background);
		backgrounds.add(background2);
		
		player1 = new SUKSprite(0, -50);
		boss = new Boss(70, 0);
		
		sprites.add(player1);
		sprites.add(boss);
		sprites.add(floor);
		sprites.add(wall);
		sprites.add(wall2);
		sprites.add(platform);
		sprites.add(platform2);
		

	}

	public double getScale() {
		return 1;
	}	
	
	public double getXCenter() {
		return this.xCenter;
	}

	public double getYCenter() {
		return this.yCenter;
	}
	
	public void setXCenter(double xCenter) {
		this.xCenter = xCenter;
	}

	public void setYCenter(double yCenter) {
		this.yCenter = yCenter;
	}
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		complete = true;
	}

	@Override
	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}	
	public DisplayableSprite getPlayer1() {
		return player1;
	}
	public DisplayableSprite getBarrier(boolean wall) {
		if (wall) {
			return this.wall;
		}
		else {
			return this.floor;
		}
	}

	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}
		
	public boolean centerOnPlayer() {
		return true;
	}		
	
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		

		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, keyboard, actual_delta_time);
    	}
		
		background.setShiftX(this.player1.getCenterX() * 0.89);
	}

	public String toString() {
		return "";
	}	

}
