import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class SUKBackground implements Background {

	public static final int TILE_HEIGHT = 32;
	public static final int TILE_WIDTH = 32;

	private Image image;
    private int backgroundWidth = 0;
    private int backgroundHeight = 0;
    private int offsetX = 0;
    private double shiftX = 0;
    private double shiftY = 0;
	
    public SUKBackground() {
    	try {
    		this.image = ImageIO.read(new File("res/bg.png"));
    		backgroundWidth = image.getWidth(null);
    		backgroundHeight = image.getHeight(null);
    		offsetX = backgroundWidth / 2  ;  		
    	}
    	catch (IOException e) {
    		//System.out.println(e.toString());
    	}		
    }

	@Override
	public Tile getTile(int col, int row) {
		int x = (col * backgroundWidth) - offsetX;
		int y = (row * backgroundHeight);
		Tile newTile = null;
		
		if (row == 0) {
			newTile = new Tile(image, x, y, backgroundWidth, backgroundHeight, false);
		}
		else {
			newTile = new Tile(null, x, y, backgroundWidth, backgroundHeight, false);
		}
		
		
		return newTile;
	}

	@Override
	public int getCol(double x) {
		int col = 0;
		
		if (backgroundWidth != 0) {
			col = (int) ((x - offsetX)  / backgroundWidth);
			if (x < 0) {
				return col - 1;
			}
			else {
				return col;
			}
		}
		else {
			return 0;
		}
	}


	@Override
	public int getRow(double y) {
		int row = 0;
		
		if (backgroundHeight != 0) {
			row = (int) (y / backgroundHeight);
			if (y < 0) {
				return row - 1;
			}
			else {
				return row;
			}
		}
		else {
			return 0;
		}
	}

	@Override
	public double getShiftX() {
		return shiftX;
	}

	@Override
	public double getShiftY() {
		return shiftY;
	}

	@Override
	public void setShiftX(double shiftX) {
		this.shiftX = shiftX;
	}

	@Override
	public void setShiftY(double shiftY) {
		this.shiftY = shiftY;
		
	}


}
