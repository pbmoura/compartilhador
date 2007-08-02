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
		case Constants.SEARCH_SCREEN:
			instance = SearchScreen.getInstance(frame);
			break;
		case Constants.TRANSFER_SCREEN:
			instance = TransferScreen.getInstance(frame);
			break;
		default:
			instance = SetupScreen.getInstance(frame);
			break;
				
		}
		return instance;
	}
}
