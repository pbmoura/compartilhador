package gui;

import util.Constants;

public class ManagementScreen extends Screen {
	
	private static ManagementScreen instance;
	
	public static ManagementScreen getInstance(MainFrame frame){
		if (instance == null){
			instance = new ManagementScreen(frame);
		}
		return instance;
	}

	private ManagementScreen(MainFrame frame){
		super(frame);
		setupTabbedPane();
		
		addTab(SearchScreen.getInstance(frame));		
		addTab(TransferScreen.getInstance(frame));
		addTab(SetupScreen.getInstance(frame));
	}
	
	public String getTitle() {
		return Constants.MANAGEMENT_SCREEN_TITLE;
	}

	
	public void reset() {
		// TODO Auto-generated method stub

	}

	
	public void setupSize() {
		// TODO Auto-generated method stub

	}

}
