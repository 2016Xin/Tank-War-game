package XC.TankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
	private int x;
	private int y;
	private int width;
	private int height;
	TankClient tc;
	
	public Wall(int x,int y,int width,int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public Wall(int x,int y,int width,int height,TankClient tc){
		this(x,y,width,height);
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.setColor(c);		
	}
	
	public Rectangle getRec(){
		return new Rectangle(x,y,width,height);
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getXPosition(){
		return x;
	}
	public int getYposition(){
		return y;
	}

}
