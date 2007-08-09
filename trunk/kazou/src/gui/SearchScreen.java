package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;

import util.Constants;
import business.Controler;

public class SearchScreen extends Screen implements ActionListener {

	private static SearchScreen instance;	
	private JTextField tfSearch ;
	private JButton btSearch;
	private JList lSearch ;
	
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
		
		DefaultListModel lmSearch =new DefaultListModel();
		
		lSearch  = new JList(lmSearch);
		 
		//Handle events
		btSearch.addActionListener(this);
		lSearch.addMouseListener(new JListDoubleClickListener());
		
		//Add 
		addComponentToGridBag(lbSearch, 0, 1, 1, 1);
		addComponentToGridBag(tfSearch, 1, 1, 2, 1);
		addComponentToGridBag(btSearch, 3, 1, 1, 1);
		
		addComponentToGridBag(lSearch, 0, 2, 4, 2);
		
	}
	
	public String getTitle() {

		return Constants.SEARCH_SCREEN_TITLE;
	}

	
	public void reset() {
		tfSearch.setText("");
		lSearch.setModel(new DefaultListModel());

	}

	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if(source == btSearch){
			String text=tfSearch.getText();
			if (text !=null && text.length()!=0){
				Controler.searchFile(text);
				DefaultListModel lmSearch =new DefaultListModel();
				lmSearch.addElement("Resultado Stub");
				lmSearch.addElement("Mais Resultado Stub");
				lSearch.setModel(lmSearch);

			}
		}

	}

	
	public void setupSize() {
		setPreferredSize(new Dimension(Constants.SEARCH_SCREEN_WIDTH, Constants.SEARCH_SCREEN_HEIGHT));
	}
	
	class JListDoubleClickListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			//Double click
			if(e.getClickCount() == 2){
				JList list = (JList)e.getSource();
				int index = list.locationToIndex(e.getPoint());
				ListModel dlm = list.getModel();
				Object item = dlm.getElementAt(index);
				list.ensureIndexIsVisible(index);
				//TODO: start file download if not already started
				System.out.println("iniciar download");
			}
		}
	}

}
