package XC.TankWar;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TankClient extends Frame{
	 public static final int GAME_WIDTH = 800;
	 public static final int GAME_HEIGHT = 600;
	 
	 Image offScreenImage = null;
	 Tank myTank = new Tank(50,50,true, Tank.Direction.STOP, this);
	 List<Missile> missiles = new ArrayList<>();
	 List<Explode> explodes = new ArrayList<>();
	 List<Tank> enemyTanks = new ArrayList<>();
	 Wall w1 = new Wall(500,200,10,150,this);
	 Wall w2 = new Wall(100,400,200,10,this);
	 Blood b = new Blood();
	 
	
	public void paint(Graphics g) {
		g.drawString("Missiles count:" + missiles.size(), 10, 50);
		g.drawString("Explodes count:" + explodes.size(), 10, 70);
		g.drawString("Enemytanks count:" + enemyTanks.size(), 10, 90);
		g.drawString("BloodLeft  count:" + myTank.getLife(), 10, 110);
		g.drawString("Round:" + Tank.round, 700, 50);
		
		for(int i=0;i<enemyTanks.size();i++){
			Tank enemyTank = enemyTanks.get(i);
			enemyTank.draw(g);
			enemyTank.hitsWall(w1);
			enemyTank.hitsWall(w2);
			enemyTank.colidesWithTanks(enemyTanks);
		}
		for(int i=0;i<missiles.size();i++){
			Missile m = missiles.get(i);
			m.HitTanks(enemyTanks);
			m.HitTank(myTank);
			m.hitsWall(w1);
			m.hitsWall(w2);
			//m.HitTank(enemyTank);
		    m.draw(g);
		}
		for(int i=0;i<explodes.size();i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		myTank.draw(g);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
		myTank.isBlooded(b);
		//enemyTank.draw(g);
		//e.draw(g);
	}
	
	// overriding update(),repaint() method will call update() method which will call paint().
	public void update(Graphics g) {
		if(offScreenImage == null) offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		Graphics gOffScreen = offScreenImage.getGraphics(); //Graphics for drawing on the off screen image.
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.green);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT); //fill the whole image with background color before paint.
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null); 
	}

	public void launchFrame(){
		
		for(int i=0;i<10;i++){
			enemyTanks.add(new Tank(60*(i+1),60,false,Tank.Direction.D, this));			
		}
		this.setLocation(400,300);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("TankWar");
		//anonymous inner class to close window.
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.green);
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
		
		new Thread(new PaintThread()).start();
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
		
 	}
	
	private class PaintThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter{
		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.KeyPressed(e);
		}
		
	}
}
