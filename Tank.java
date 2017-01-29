package XC.TankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};
	private boolean BU = false, BL =false, BR = false, BD = false;
	private Direction dir = Direction.STOP; 
	private Direction ptdir = Direction.U;
	
	public static final int XSPEED = 5,YSPEED = 5;
	public static final int WIDTH =30, HEIGHT = 30;
	public static int round = 2; // totally round of games
	
	private int x, y;
	private int oldX,oldY; // store the previous status of the tank
	private boolean good; // true for our tanks, false for enemy tanks;
	private boolean live = true; // initialized the tank alive;
	private int life = 100; 
	private int numsOfTankToAdd = 5;
	
	
	
	public static Random ran = new Random();	
	private int step = ran.nextInt(12)+3;
	
	private TankClient tc; // reference for TankClient class
	BloodBar bb = new BloodBar();
	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}
	public Tank(int x, int y,boolean good,Direction dir, TankClient tc){
		this(x, y,good);
		this.tc = tc;
		this.dir = dir;
	}

	public void draw(Graphics g) {
		if(good && isLive()) bb.draw(g); // when the good tank is die, the blood will not draw any more
		if(!live) {
			
			if(!good) 
				tc.enemyTanks.remove(this);
			return; // if a tank died, then no need to draw it.
		}
		// as long as the round is not 0, when the enemy tanks all die, creat new round of enemy tank
		if(round!=0 && tc.enemyTanks.size() == 0){
			for(int i=0;i<numsOfTankToAdd;i++){
				tc.enemyTanks.add(new Tank(60*(i+1),ran.nextInt(600) +100,false,Tank.Direction.D, tc));			
			}
			round--;
		}
		Color c = g.getColor(); // didn't change the foreground color
		if(this.good) g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
		
		//the switch loop aims to draw a line standing for fire tube according to the moving direction of tank.
		switch(dir){
		case L:  
			g.drawLine(x, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y+Tank.HEIGHT/2); 
			break;
		case LU: 
			g.drawLine(x, y, x+Tank.WIDTH/2, y+Tank.HEIGHT/2); 
			break; 
		case U:	 
			g.drawLine(x+Tank.WIDTH/2, y, x+Tank.WIDTH/2, y+Tank.HEIGHT/2); 
			break;
		case RU: 
			g.drawLine(x+Tank.WIDTH, y, x+Tank.WIDTH/2, y+Tank.HEIGHT/2);
			break;
		case R:  
			g.drawLine(x+Tank.WIDTH, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y+Tank.HEIGHT/2); 
			break;
		case RD: 
			g.drawLine(x+Tank.WIDTH, y+Tank.HEIGHT, x+Tank.WIDTH/2, y+Tank.HEIGHT/2); 
			break;
		case D:  
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT, x+Tank.WIDTH/2, y+Tank.HEIGHT/2); 
			break;
		case LD: 
			g.drawLine(x, y+Tank.HEIGHT, x+Tank.WIDTH/2, y+Tank.HEIGHT/2); 
			break;
		case STOP:
			g.drawLine(x+Tank.WIDTH/2, y, x+Tank.WIDTH/2, y+Tank.HEIGHT/2); 
			break;
		}
	}
	
	public void move(){
		oldX = x;
		oldY = y;
		
		switch(dir){
		case L: x -= XSPEED; break;
		case LU: x -= XSPEED; y -= YSPEED; break; // Notice: this speed is Sqrt(2) times larger than vertical and horizontal direction. 
		case U:	 y -= YSPEED; break;
		case RU: x += XSPEED; y -= YSPEED; break;
		case R:  x += XSPEED; break;
		case RD: x += XSPEED; y += YSPEED; break;
		case D:  y += YSPEED; break;
		case LD: x -= XSPEED; y += YSPEED; break;
		case STOP: break;
		}
		if(this.dir!= Direction.STOP){
			this.ptdir = this.dir;
		}
		
		// when the tank comes to the edge of windows, the tank stops
		if(x<0) x=0;
		if(y<25) y=25;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if(!good) {
			Direction[] dirs = Direction.values();
			if(step==0){
				int i = ran.nextInt(dirs.length);
				this.dir = dirs[i];
				step = ran.nextInt(12)+3;
			}
			step--;	
			
			if(ran.nextInt(40)>=38) this.fire();
			if(hitsWall(tc.w1) || hitsWall(tc.w2)) this.stay();// if the tank came across the wall, the x and y change to the previous status.
		}
		
	
	}
	public Missile fire(){
		return this.fire(ptdir);
	}
	public Missile fire(Direction dir){
		
		if(!this.live) return null;
		int x = this.x + Tank.WIDTH/2-Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m = new Missile(x,y, good, dir,tc);
		tc.missiles.add(m);
		return m;
	}
	
	public void superFire(){
		Direction[] dirs = Direction.values();
		for(int i=0;i<8;i++){
			fire(dirs[i]);
		}		
	}
	
	
	public void getDirection(){
		if(BL && !BU && !BR && !BD) dir = Direction.L;
		else if(BL && BU && !BR && !BD) dir = Direction.LU;
		else if(!BL && BU && !BR && !BD) dir = Direction.U;
		else if(!BL && BU && BR && !BD) dir = Direction.RU;
		else if(!BL && !BU && BR && !BD) dir = Direction.R;
		else if(!BL && !BU && BR && BD) dir = Direction.RD;
		else if(!BL && !BU && !BR && BD) dir = Direction.D;
		else if(BL && !BU && !BR && BD) dir = Direction.LD;
	}
	public void KeyPressed(KeyEvent e){
		int key = e.getKeyCode();
		// the switch loop realized that the following key press changes the previous moving direction. But only along 4 directions.
		switch(key){
		
		case KeyEvent.VK_LEFT: BL =true; break;
		case KeyEvent.VK_UP: BU = true; break;
		case KeyEvent.VK_RIGHT: BR = true; break;
		case KeyEvent.VK_DOWN: BD = true; break;
		case KeyEvent.VK_F2: 
			if(this.isGood() && !this.isLive()){
				this.setLive(true);
				this.setLife(100);
			}
			break;
		}
		getDirection();
	}
	

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		// the switch loop realized that the following key press changes the previous moving direction. But only along 4 directions.
		switch(key){
		case KeyEvent.VK_CONTROL: 
			fire();
			break;
		case KeyEvent.VK_LEFT: BL = false;  break;
		case KeyEvent.VK_UP: BU = false; break;
		case KeyEvent.VK_RIGHT: BR = false; break;
		case KeyEvent.VK_DOWN: BD = false; break;
		case KeyEvent.VK_A: superFire();break;
		}
		getDirection();
	}
	public Rectangle getRec() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public void stay(){
		x = oldX;
		y = oldY;
	}
	public boolean hitsWall(Wall w){
		if(this.getRec().intersects(w.getRec())){			
			return true;	
		}				
		return false;
	}
	public boolean colidesWithTank(Tank t){
		if(this !=t){
			if(this.live && t.isLive() && this.getRec().intersects(t.getRec())){
				this.stay();
				t.stay();
				return true;
			}
		}					
		return false;
	}
	public boolean colidesWithTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			if(colidesWithTank(tanks.get(i))) 
				return true;
		}
		return false;
	}
	
	public boolean isBlooded(Blood b){
		
			if(this.live && b.isLive() && this.getRec().intersects(b.getRec())){
				life = 100;
				b.setLive(false);
				tc.bloods.add(new Blood(Tank.ran.nextInt(500) + 10, Tank.ran.nextInt(50), 10, 20, tc));
				return true;
			}
			
		return false;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isGood() {
		return good;
	}
	public void setGood(boolean good) {
		this.good = good;
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.drawRect(x, y-10, WIDTH, 10);
			g.setColor(Color.RED);
			int w = WIDTH*life/100;
			g.fillRect(x, y-10, w, 10);
			g.setColor(Color.RED);
			g.setColor(c);
			
		}
		
	}
}
