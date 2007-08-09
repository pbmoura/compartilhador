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
		SetupScreen.getInstance(getOwner()).reset();
		TransferScreen.getInstance(getOwner()).reset();
		SetupScreen.getInstance(getOwner()).reset();

	}

	
	public void setupSize() {
		//Do anything: preferred size is decided by its tabs
		
	}

}
