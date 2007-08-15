package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*; 

import util.*;
import business.*;



public class SetupScreen extends Screen implements ActionListener{
	JTextField tfRepAddress;
	private JButton    btBrowse;
	private JButton    btOk;
	private JButton    btCancel;
	private JComboBox  cbChooseSupernode;
	
	private static SetupScreen instance;

	
	public static SetupScreen getInstance(MainFrame frame){
		if (instance == null){
			instance = new SetupScreen(frame);
		}
		return instance;
	}

	private SetupScreen(MainFrame frame){
		super(frame);
		
		//Configure layout manager
		setupGridBagLayout();
		
		//Create items
		JLabel tfLabel = new JLabel(Constants.SETUP_SCREEN_REP_LABEL);
		tfRepAddress = new JTextField(10);	
		tfRepAddress.setText(Controler.getUserRepositoryPath());
		
		btBrowse     = new JButton(Constants.SETUP_SCREEN_BROWSE_LABEL);		
		btOk         = new JButton(Constants.OK_LABEL);
		btCancel     = new JButton(Constants.SETUP_SCREEN_CANCEL_LABEL);
		
		JLabel cbLabel = new JLabel(Constants.SETUP_SCREEN_SUPERNODE_LABEL);		
		cbChooseSupernode = new JComboBox();
		ComboBoxModel cbModel = new DefaultComboBoxModel(Controler.getSuperNodeList());
		cbChooseSupernode.setModel(cbModel);
		
		
		//Handle events
		btBrowse.addActionListener(this);		
		btOk.addActionListener(this);
		btCancel.addActionListener(this);
		
		//Add items to pane
		this.addToGridBag(tfLabel,0,1,1,1,0,0,GridBagConstraints.HORIZONTAL);		
		this.addToGridBag(tfRepAddress,1,1,1,1,0,0,GridBagConstraints.HORIZONTAL);
		this.addToGridBag(btBrowse,2,1,1,1,0,0,GridBagConstraints.HORIZONTAL);
		
		this.addToGridBag(cbLabel,0,2,1,1,0,0,GridBagConstraints.HORIZONTAL);
		this.addToGridBag(cbChooseSupernode,1,2,1,1,0,0,GridBagConstraints.HORIZONTAL);
		
		this.addToGridBag(btOk,0,3,1,1,0,0,GridBagConstraints.HORIZONTAL);
		this.addToGridBag(btCancel,2,3,1,1,0,0,GridBagConstraints.HORIZONTAL);
	}

	public String getTitle(){
		return Constants.SETUP_SCREEN_TITLE;
	}
	

	public void actionPerformed(ActionEvent ae){
		Object source = ae.getSource();
		if (source == btBrowse){
			//Open file chooser
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = fileChooser.showDialog(this,Constants.OK_LABEL);
			if (result == JFileChooser.APPROVE_OPTION){
				tfRepAddress.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
			
		}else if (source == btOk){
			String text = tfRepAddress.getText();
			if (text != null && text.trim().length()!=0){
				//Configure user and goto search screen
				Controler.configureUser(tfRepAddress.getText(),(String)cbChooseSupernode.getSelectedItem());
				getOwner().showScreen(Constants.MANAGEMENT_SCREEN);
			}else{
				//mandatory field not filled
				JOptionPane.showMessageDialog(null,Constants.FIELD_NOT_FILLED_LABEL,Constants.WARNING_LABEL,JOptionPane.WARNING_MESSAGE);
			}
			
		}else if (source == btCancel){
			//Ignore modifications and go to former screen, or exit
			int oldScreenID = getOwner().getOldScreenID();
			if(oldScreenID == -1){
				Controler.exit(0);
			}else{
				getOwner().showScreen(oldScreenID);
			}
		}
		
	}
	
	public void reset() {
		
		//Keep the user`s repositorty path
		tfRepAddress.setText(Controler.getUserRepositoryPath());
		
		//Keep the selected supernode
		Object selectedSupernode = cbChooseSupernode.getSelectedItem();
		//reset supernode list		
		Vector supernodes = Controler.getSuperNodeList();
		ComboBoxModel cbModel = new DefaultComboBoxModel(supernodes);
		cbChooseSupernode.setModel(cbModel);
		
		if (supernodes.contains(selectedSupernode)){
			cbChooseSupernode.setSelectedItem(selectedSupernode);
		}
	}
	
	public void setupSize(){
		setPreferredSize(new Dimension(Constants.SETUP_SCREEN_WIDTH, Constants.SETUP_SCREEN_HEIGHT));
	}
	

	
}
