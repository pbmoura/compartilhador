package gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public abstract class Screen extends JPanel {

	private MainFrame frame;
	private GridBagLayout gbLayout;
	private GridBagConstraints constraints;
	
	public Screen(MainFrame fr){
		super();	
		frame = fr;			
	}	
	
	protected MainFrame getOwner(){
		return frame;
	}
	
	public void addComponentToGridBag(Component c, int x, int y, int width, int height){
		
		constraints.gridx=x;
		constraints.gridy=y;
		constraints.gridwidth=width;
		constraints.gridheight=height;
		gbLayout.setConstraints(c, constraints);
		add(c);
	}

	public void setupGridBagLayout() {
		gbLayout = new GridBagLayout();
		setLayout(gbLayout);
		constraints = new GridBagConstraints();		
		constraints.fill = GridBagConstraints.HORIZONTAL;
	}
	
	public abstract String getTitle();
	public abstract void reset();
	public abstract void setupSize();
	
}
