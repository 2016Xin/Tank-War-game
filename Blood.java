package XC.TankWar;
import java.awt.*;


public class Blood {
	private int x,y,width,height;
	private TankClient tc;
	private boolean live;
	private int speed = 2;
	
	

	// private int [][] posi = { {400,400},{450,350},{500,400},{480,380},{500,400},{470,390},{450,380},{430,390},{420,420} };
	/*
	public Blood(int x, int y, int width, int height){
		x = Tank.ran.nextInt(500) + 10;
		y = 50;
		width = height = 50;
	
	}
		*/
	public Blood(int x,int y, int width, int height, TankClient tc){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.live = true;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!this.isLive()) return;
		
		Color c = g.getColor();
		g.setColor(Color.red);
		g.fillRect(x, y, width, height);
		g.setColor(c);
		move();
	}
	
	// the blood moves from top to bottom
	public void move(){
		if(!live) return;
		if (y <= TankClient.GAME_HEIGHT) {
			y += speed;
		} else this.live = false;
		
		/*
		step++;
		if(step == posi.length-1) 
			step = 0;
			x = posi[step][0];
		    y = posi[step][1];
		 */
		
	
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Rectangle getRec() {
		return new Rectangle(x,y,width,height);
	}
}
