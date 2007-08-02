package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import business.Controler;
import util.Constants;

public class MainFrame extends JFrame {

	private int currentScreenID=-1;
	private int oldScreenID=-1;
	
	public MainFrame(){		
		super("");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Controler.exit(0);
			}
		});	
		setIconImage(Toolkit.getDefaultToolkit().getImage(Constants.WINDOW_ICON));
	}
	
	public void showScreen(int screenID){		
		Screen oldScreen = ScreenFactory.getScreen(this.currentScreenID, this);
		oldScreen.reset();
		this.oldScreenID = this.currentScreenID;
		this.currentScreenID = screenID;
		
		Screen screen = ScreenFactory.getScreen(screenID,this);
		this.setTitle(screen.getTitle());
		this.getContentPane().removeAll();
		this.getContentPane().add(screen,BorderLayout.CENTER);
		
		//Show screen centralized, with correct size
		screen.setupSize();		
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenDimension.width-this.getSize().width)/2,    
				(screenDimension.height-this.getSize().height)/2);
		setResizable(false);
		setVisible(true);		
	}	

	public int getOldScreenID() {
		return oldScreenID;
	}
	
	/*FOR TESTS ONLY. MAYBE THIS METHOD MUST BE IN THE CONTROLLER*/
	public static void main(String args[]){
		
		MainFrame frame = new MainFrame();
		frame.showScreen(Constants.SPLASH_SCREEN);
	}

}
