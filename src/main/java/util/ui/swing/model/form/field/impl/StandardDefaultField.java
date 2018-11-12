package util.ui.swing.model.form.field.impl;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JTextField;

import util.ui.swing.model.form.field.FormField;

public class StandardDefaultField extends FormField{
	private static final long serialVersionUID = 1L;
	private JTextField input;
	public StandardDefaultField(Container form) {
		super(form);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return input.getText();
	}

	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub
		input.setText(value.toString());
	}

	@Override
	public JComponent input() {
		// TODO Auto-generated method stub
		return input;
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		input.setEditable(enable);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		input=new JTextField();
		super.init();
	}

	@Override
	public void init(Object value) {
		// TODO Auto-generated method stub
		input.setText(value.toString());
	}
}
