package XC.TankWar;
import java.awt.*;
import java.util.List;


public class Missile {
	public static final int XSPEED = 10, YSPEED = 10; // the speed of missile;
	public static final int WIDTH = 10, HEIGHT = 10;
	
	private boolean live = true;
	private int x,y;
	private Tank.Direction dir;
	private TankClient tc;
	private boolean good;
	
	public Missile(int x, int y,  Tank.Direction dir) {
		this.x = x;
		this.y = y; 
		this.dir = dir;
	}
	public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc){
		this(x,y,dir);
		this.tc = tc;
		this.good = good;
	}
	
	public void draw(Graphics g){
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		if(good) g.setColor(Color.MAGENTA);
		else g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH,HEIGHT);
		g.setColor(c);
		move();
	}

	private void move() {
		switch(dir){
		case L:  x -= XSPEED; break;
		case LU: x -= XSPEED; y -= YSPEED; break; // this speed is Sqrt(2) times larger than vertical and horizontal direction. 
		case U:	 y -= YSPEED; break;
		case RU: x += XSPEED; y -= YSPEED; break;
		case R:  x += XSPEED; break;
		case RD: x += XSPEED; y += YSPEED; break;
		case D:  y += YSPEED; break;
		case LD: x -= XSPEED; y += YSPEED; break;
		case STOP: break;
		}
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT){
			live = false;
		}
		
		if(hitsWall(tc.w1) || hitsWall(tc.w2)) live = false; // if a missile collided with the wall, it disappeared.
	}
	public boolean isLive(){
		return live;
	}
	public Rectangle getRec(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	public boolean HitTank(Tank t){
		//take advantage of Rectangle class to see if the rectangle of missile intersects into the rectangle of tank.
		if(this.live && this.getRec().intersects(t.getRec()) && t.isLive() && this.good != t.isGood()){
			if(t.isGood()) {
				t.setLife(t.getLife()-20);
				if(t.getLife()<=0) 
					t.setLive(false);
			}
			else {
				t.setLive(false);				
				}
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			
			return true; 
		} 
		return false;
	}
	public boolean HitTanks(List<Tank> enemyTanks){
		for(int i=0;i<enemyTanks.size();i++){
			if(HitTank(enemyTanks.get(i)))
				return true;
		}
		return false;
	}
	
	
	public boolean hitsWall(Wall w){
		if(this.getRec().intersects(w.getRec())) 
			return true;
		return false;
	}
}
