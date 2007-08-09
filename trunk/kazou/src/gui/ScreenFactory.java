package gui;

import util.Constants;

public class ScreenFactory {

	public static Screen getScreen(int screenID, MainFrame frame) {
		Screen instance=null;
		switch(screenID){

		case Constants.SPLASH_SCREEN:
			instance = SplashScreen.getInstance(frame);
			break;
		case Constants.SETUP_SCREEN:
			instance = SetupScreen.getInstance(frame);
			break;
		case Constants.MANAGEMENT_SCREEN:
			instance = ManagementScreen.getInstance(frame);
			break;
		default:
			instance = SetupScreen.getInstance(frame);
			break;
				
		}
		return instance;
	}
}
