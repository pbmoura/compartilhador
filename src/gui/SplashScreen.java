package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import util.Constants;
import business.Controller;

public class SplashScreen extends Screen {

	private static SplashScreen instance;

	private Timer timer;
	private EventSplashEnd splashEnd;
	
	public static SplashScreen getInstance(MainFrame frame){
		if (instance == null){
			instance = new SplashScreen(frame);
		}		
		return instance;
	}
	
	private SplashScreen(MainFrame fr) {
		super(fr);	
		
		setLayout(new BorderLayout());
		JLabel imageSplash = new JLabel(new ImageIcon(Constants.WINDOW_SPLASH));
		add(imageSplash, BorderLayout.CENTER);
		getOwner().setUndecorated(true);
		timer = new Timer();
		splashEnd = new EventSplashEnd(this);
		timer.schedule(splashEnd, Constants.SPLASH_TIME);
	}


	public String getTitle() {		
		return Constants.SPLASH_SCREEN_TITLE;
	}

	
	public void reset() {
		//do nothing
	}

	
	public void setupSize() {
		setPreferredSize(new Dimension(Constants.SPLASH_SCREEN_WIDTH, 
				Constants.SPLASH_SCREEN_HEIGHT));
	}
	
	class EventSplashEnd extends TimerTask{
		Screen screen ;
		public EventSplashEnd(Screen sc){
			super();
			screen = sc;
		}
		public void run(){	
			screen.getOwner().dispose();
			screen.getOwner().setUndecorated(false);
			
			if (Controller.getInstance().getArchitecture()==-1){
				//choose architecture, for first run
				screen.getOwner().showScreen(Constants.CHOOSEARCH_SCREEN);
			}else {
				//initialize node or supernode
				Controller.getInstance().initCommunications();
				screen.getOwner().showScreen(Constants.MANAGEMENT_SCREEN);	
			}
		}
	} 

}
