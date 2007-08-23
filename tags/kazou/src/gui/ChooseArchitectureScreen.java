package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import util.Constants;

import business.Controller;

public class ChooseArchitectureScreen extends Screen implements ActionListener {

	private static ChooseArchitectureScreen instance;
	private Controller controller = Controller.getInstance();
	private JRadioButton btradioNode;
	private JRadioButton btradioSuperNode;
	private JButton btOk;
	
	public static ChooseArchitectureScreen getInstance(MainFrame frame){
		if (instance == null){
			instance = new ChooseArchitectureScreen(frame);
		}
		return instance;
	}
	
	private ChooseArchitectureScreen(MainFrame fr) {
		super(fr);
		//Configure layout manager
		setupGridBagLayout();
		
		//Create items
		btradioNode = new JRadioButton("Nó");
		btradioNode.setSelected(true);
		btradioSuperNode = new JRadioButton("Super Nó");
		ButtonGroup btGroup = new ButtonGroup();
		btGroup.add(btradioNode);
		btGroup.add(btradioSuperNode);
		
		JLabel chooseLabel = new JLabel("Quem é você?");
		
		btOk = new JButton(Constants.OK_LABEL);
		
		addToGridBag(chooseLabel,0,0,1,1,1,1,GridBagConstraints.BOTH);
		addToGridBag(btradioNode,0,1,1,1,1,1,GridBagConstraints.BOTH);
		addToGridBag(btradioSuperNode,0,2,1,1,1,1,GridBagConstraints.BOTH);
		addToGridBag(btOk,1,3,1,1,1,1,GridBagConstraints.HORIZONTAL);
		//gambiarra para centralizar o botão de ok
		addToGridBag(new JLabel("                         "),2,3,1,1,1,1,GridBagConstraints.BOTH);
		
		//Handle events
		btOk.addActionListener(this);
		
	}

	@Override
	public String getTitle() {
		return Constants.CHOOSEARCH_SCREEN_TITLE;
	}

	@Override
	public void reset() {
		// do nothing
		
	}

	@Override
	public void setupSize() {
		setPreferredSize(new Dimension(Constants.CHOOSEARCH_SCREEN_WIDTH, Constants.CHOOSEARCH_SCREEN_HEIGHT));
		
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btOk){
			if (btradioNode.isSelected()==true){
				Controller.getInstance().configureArch(Constants.NODE_ARCHITECTURE);
			}else {
				Controller.getInstance().configureArch(Constants.SUPERNODE_ARCHITECTURE);
			}
			getOwner().showScreen(Constants.SETUP_SCREEN);
		}
		
	}

}
