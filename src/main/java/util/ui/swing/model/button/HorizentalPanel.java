package util.ui.swing.model.button;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class HorizentalPanel extends JPanel{
	private static final long serialVersionUID = -4954646500665097696L;

	private HorizentalPanel() {
		super();
	}
	public static HorizentalPanel create() {
		return new HorizentalPanel();
	}
	
	public HorizentalPanel init() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		return this;
	}
	
	public HorizentalPanel add(Component component) {
		super.add(component);
		return this;
	}
}
