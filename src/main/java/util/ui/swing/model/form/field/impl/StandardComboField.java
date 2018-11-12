package util.ui.swing.model.form.field.impl;

import java.awt.Container;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import util.ui.swing.bean.KV;
import util.ui.swing.model.form.field.FormField;

public class StandardComboField extends FormField {
	private static final long serialVersionUID = -6855818498833149435L;
	private JComboBox<Object> input;

	public StandardComboField(Container form) {
		super(form);
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return ((KV)input.getSelectedItem()).getValue();
	}

	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub
		System.out.println(value);
		input.removeAll();
		KV[] values = (KV[]) value;
		input.setEditable(false);
		Object v = values[0].getAttr("_default_");
		Arrays.stream(values).forEach(input::addItem);
		if (v != null) {
			input.setSelectedItem(v);
		}
	}

	@Override
	public Object getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JComponent input() {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		input.setEditable(false);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		input = new JComboBox<>();
		super.init();
	}

	@Override
	public void init(Object value) {
		// TODO Auto-generated method stub
		input.removeAll();
		KV[] values = (KV[]) value;
		input.setEditable(false);
		Arrays.stream(values).forEach(input::addItem);
	}
}
