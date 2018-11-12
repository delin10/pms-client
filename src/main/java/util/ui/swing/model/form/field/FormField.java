package util.ui.swing.model.form.field;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

import util.ui.swing.comm.Util;

public abstract class FormField extends Box {
	private static final long serialVersionUID = 4173153740538326202L;
	protected Container form;
	private JLabel label = new JLabel();
	private boolean enable = true;
	private boolean hidden = true;

	protected FormField(Container form) {
		super(BoxLayout.LINE_AXIS);
		this.form=form;
		init();
		// TODO Auto-generated constructor stub
	}

	public void setText(String text) {
		label.setText(text);
	}

	public String getText() {
		return label.getText();
	}

	public void setEnable(boolean enable) {
		input().setEnabled(enable);
	}

	public final void setHidden(boolean hidden) {
		this.setVisible(!hidden);
	}

	public final boolean isHidden() {
		return hidden;
	}
	
	public final void setInputSize(int width,int height) {
		input().setPreferredSize(new Dimension(width, height));;
	}
	
	public final void setLabelSize(int width,int height) {
		label().setPreferredSize(new Dimension(width, height));;
	}

	public  boolean isEnable() {
		return enable;
	}

	public final JLabel label() {
		return label;
	}


	public Object getRoot() {
		return form;
	}
	
	public void init() {
		Util.addBatch(this, label, Box.createHorizontalStrut(10), input());
	}
	
	public abstract Object getValue();

	public abstract void setValue(Object value);

	public abstract JComponent input();
	
	public abstract void init(Object value);
}
