package XC.TankWar;
import java.awt.Color;
import java.awt.Graphics;

public class Explode {
	private int x, y;
	private boolean live = true;
	int [] diameter = {5,8,12,20,30,45,60,30,10,2};
	private int step = 0;
	private TankClient tc;
	
	public Explode(int x, int y, TankClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	public void draw(Graphics g){
		if(!live)  {
			tc.explodes.remove(this);
			return;
		}
		if(step >= diameter.length) {
			live = false;
			step = 0;
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.fillOval(x, y, diameter[step], diameter[step]);
		step++;
		g.setColor(c);
		
	}
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
}
