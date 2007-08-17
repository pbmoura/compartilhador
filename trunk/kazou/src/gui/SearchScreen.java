package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import util.Constants;
import business.Controler;

public class SearchScreen extends Screen implements ActionListener {

	private static SearchScreen instance;	
	private JTextField tfSearch ;
	private JButton btSearch;
	private JTable tbSearch;

	
	public static SearchScreen getInstance(MainFrame frame){
		if (instance == null){
			instance = new SearchScreen(frame);
		}
		return instance;
	}
	
	private SearchScreen(MainFrame frame){
		super(frame);
		//Configure layout manager
		setupGridBagLayout();		
		//Create items
		JLabel lbSearch = new JLabel(Constants.SEARCH_SCREEN_SEARCH_LABEL);
		tfSearch = new JTextField(10);
		btSearch = new JButton(Constants.OK_LABEL);
		
		                     /*Configure table*/
		tbSearch = new JTable(new MyTableModel());
		tbSearch.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbSearch.setShowGrid(false);
		tbSearch.getTableHeader().setReorderingAllowed(false);
		JScrollPane tbScroll = new JScrollPane(tbSearch);
		
		//Handle events
		tfSearch.addActionListener(this);
		btSearch.addActionListener(this);
		tbSearch.addMouseListener(new TableDoubleClickListener());
		
		//Add 
		addToGridBag(lbSearch, 0, 1, 1, 1,0,0,GridBagConstraints.NONE);
		addToGridBag(tfSearch, 1, 1, 2, 1,1,0,GridBagConstraints.BOTH);
		addToGridBag(btSearch, 3, 1, 1, 1,0,0,GridBagConstraints.NONE);
		
		addToGridBag(tbScroll, 0, 2, 4, GridBagConstraints.REMAINDER,0,1,GridBagConstraints.BOTH);
		
	}
	
	public String getTitle() {

		return Constants.SEARCH_SCREEN_TITLE;
	}

	
	public void reset() {
		tfSearch.setText("");
		tbSearch.setModel(new MyTableModel());

	}

	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if(source == btSearch || source ==tfSearch){
			String text=tfSearch.getText();
			if (text !=null && text.length()!=0){
				Vector results = Controler.searchFile(text);
				//reset
				
				fillTable(results);				

			}
		}

	}

	
	private void fillTable(Vector results) {
		// TODO: Determine how this table is filled
		MyTableModel tbModel =new MyTableModel();
		tbSearch.setModel(tbModel);
		for(int i=0;i<results.size();i++){
			tbModel.addRow(new String[]{(String)results.get(i),"Tamanho em kb","Valor hash"});
			
		}
	}

	public void setupSize() {
		setPreferredSize(new Dimension(Constants.SEARCH_SCREEN_WIDTH, Constants.SEARCH_SCREEN_HEIGHT));
	}
	
	/**
	 * 
	 * Double click event for the results table
	 * @author ncq
	 *
	 */
	class TableDoubleClickListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			//Double click
			if(e.getClickCount() == 2){							
				JTable table = (JTable)e.getSource();				
				int index = table.getSelectedRow();
				//the hash value is at column number 2
				Object hash = table.getModel().getValueAt(index,2);
				//Start file download if not already started
				if (hash!=null){
					Controler.startDownload(hash);
				}
			}
		}
	}
	
	/**
	 * This class defines a table model for our application
	 * @author ncq
	 *
	 */
	class MyTableModel extends DefaultTableModel{
		public MyTableModel(){
			super(0,3);
			setColumnIdentifiers(Constants.SEARCH_TABLE_HEADER);
		}
		public boolean isCellEditable(int rowIndex, int mColIndex){ 
	          return false; 
	     }
	}

}