package pms.client.ui.intef;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class SwitchPanel extends JPanel {
	private static final long serialVersionUID = 2345928698490994380L;
	public abstract SwitchPanel switchIn(JComponent container,Runnable run);
	public abstract SwitchPanel refresh();
}
