package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import business.*;

import util.Constants;

public class TransferScreen extends Screen implements ActionListener {

	private static TransferScreen instance;
	private JTable downloadTable;
	private Vector dwnProgressBars;
	
	public static TransferScreen getInstance(MainFrame frame){
		if (instance == null){
			instance = new TransferScreen(frame);
		}
		return instance;
	}

	private TransferScreen(MainFrame frame){
		super(frame);
		//Configure layout 
		setupGridBagLayout();	
		setInsets(new Insets(0,0,2,0));

		
		//Create items
		downloadTable = new JTable();		
		downloadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		downloadTable.setShowGrid(false);
		downloadTable.getTableHeader().setReorderingAllowed(false);
		
		dwnProgressBars = new Vector();
		reset();	
	}
	
	public String getTitle() {
		return Constants.TRANSFER_SCREEN_TITLE;
	}

	
	public void reset() {
		removeAll();
		//clean download progress bars
		dwnProgressBars.removeAllElements();
		
		//clean table
		DownloadTableModel dtModel = new DownloadTableModel();
		downloadTable.setModel(dtModel);
		addToGridBag(downloadTable.getTableHeader(), 0, 0, 1, 1, 0.7,0.1, GridBagConstraints.BOTH);
		addToGridBag(downloadTable,0,1,1,GridBagConstraints.REMAINDER,0.7,1,GridBagConstraints.BOTH);
		
		//Fill with the tranfers
		Vector downloads = Controler.getCurrentDownloads();
		for (int i=0;i<downloads.size();i++){
			FileInfo fileInfo = (FileInfo) downloads.get(i);
			
			dtModel.addRow(new String[]{fileInfo.getFilename(),
					String.valueOf(fileInfo.getTranferRate()),
					String.valueOf(fileInfo.getSizeInKB()),
					String.valueOf(fileInfo.getCompleteKB())});
			JProgressBar jpb = new JProgressBar(0,100);
			
			double percentComplete =fileInfo.getSizeInKB()!=0 ?
					((double)fileInfo.getCompleteKB()/(double)fileInfo.getSizeInKB()):
						0;
					
			jpb.setValue((int)(100* percentComplete));
			dwnProgressBars.add(jpb);		
		
			addToGridBag(jpb,1,i+2,1,1,0.3,0,GridBagConstraints.HORIZONTAL);
		}
	}

	
	public void setupSize() {
		setPreferredSize(new Dimension(Constants.TRANSFER_SCREEN_WIDTH,
				Constants.TRANSFER_SCREEN_HEIGHT));

	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	class DownloadTableModel extends DefaultTableModel{
		public DownloadTableModel(){
			super(0,4);
			setColumnIdentifiers(Constants.DOWNLOAD_TABLE_HEADER);
		}
		public boolean isCellEditable(int rowIndex, int mColIndex){ 
	          return false; 
	     }
	}

}
