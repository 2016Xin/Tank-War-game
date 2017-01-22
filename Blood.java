package XC.TankWar;
import java.awt.*;
public class Blood {
	private int x,y,width,height;
	private TankClient tc;
	private int step = 0;
	private boolean live = true;
	
	

	private int [][] posi = { {400,400},{450,350},{500,400},{480,380},{500,400},{470,390},{450,380},{430,390},{420,420} };
	
	public Blood(){
		x = posi[0][0];
		y = posi[0][1];
		width = height = 10;
	}
	
	public void draw(Graphics g){
		if(!this.isLive()) return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, width, height);
		g.setColor(c);
		move();
	}
	
	public void move(){
		if(!live) return;
		step++;
		if(step == posi.length-1) 
			step = 0;
		x = posi[step][0];
		y = posi[step][1];
	
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
