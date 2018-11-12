package pms.client.ui;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomePanel extends JPanel {
	private static final long serialVersionUID = 5293136868285313694L;
	private JLabel label=new JLabel();
	
	public WelcomePanel Init() {
		label.setText("欢迎使用老身的物业管理系统");
		label.setFont(new Font("宋体", Font.PLAIN, 28));
		this.add(label);
		
		return this;
	}

}
