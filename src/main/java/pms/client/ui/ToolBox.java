package pms.client.ui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextField;

import util.ui.swing.comm.Util;
import util.ui.swing.gen.CompGenerator;

public class ToolBox extends Box{
	private static final long serialVersionUID = 5800696033732710904L;
	private JButton next_button;
	private JButton prev_button;
	private JButton refresh_button;
	private JButton first_button;
	private JButton last_button;
	private JButton skip_button;
	private JTextField input;
	public ToolBox() {
		super(BoxLayout.LINE_AXIS);
		// TODO Auto-generated constructor stub
	}
	
	public static ToolBox create() {
		return new ToolBox();
	}
	

	public  Box init() {
		//BorderFactory.createEtchedBorder()
		//BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createRaisedBevelBorder())
		this.setBorder(BorderFactory.createEtchedBorder());
		next_button = CompGenerator.genButton("", "next-normal", "next-pressed");
		prev_button = CompGenerator.genButton("", "prev-normal", "prev-pressed");
		refresh_button = CompGenerator.genButton("", "refresh-normal", "refresh-pressed");
		first_button = CompGenerator.genButton("", "first-normal", "first-pressed");
		last_button = CompGenerator.genButton("", "last-normal", "last-pressed");
		skip_button = CompGenerator.genButton("", "skip-normal", "skip-pressed");
		input = new JTextField();
		Util.setMaxSize(50, 20, next_button,prev_button,skip_button,first_button,last_button);
		input.setMaximumSize(new Dimension(50, 20));
		refresh_button.setMaximumSize(new Dimension(200, 20));
		this.add(first_button);
		this.add(prev_button);
		this.add(input);
		this.add(skip_button);
		this.add(next_button);
		this.add(last_button);
		this.add(refresh_button,Box.RIGHT_ALIGNMENT);
		return this;
	}
	
	public ToolBox setParent(Box parent) {
		parent.add(this);
		return this;
	}
}
