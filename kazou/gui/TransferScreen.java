package kazou.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransferScreen extends Screen implements ActionListener {

	private static TransferScreen instance;
	
	public static TransferScreen getInstance(MainFrame frame){
		if (instance == null){
			instance = new TransferScreen(frame);
		}
		return instance;
	}

	private TransferScreen(MainFrame frame){
		super(frame);
	}
	
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void reset() {
		// TODO Auto-generated method stub

	}

	
	public void setupSize() {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
