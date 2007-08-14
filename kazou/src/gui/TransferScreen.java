package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.Constants;

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
		return Constants.TRANSFER_SCREEN_TITLE;
	}

	
	public void reset() {
		// TODO Auto-generated method stub

	}

	
	public void setupSize() {
		setPreferredSize(new Dimension(Constants.TRANSFER_SCREEN_WIDTH,
				Constants.TRANSFER_SCREEN_HEIGHT));

	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
