package util.ui.swing.model.form.bean;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import util.ui.swing.bean.KV;
import util.ui.swing.border.RoundBorder;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.form.Form;
import util.ui.swing.model.table.impl.StandardTableSelector;

public class FormField {
	// 是
	private Form form;
	private KV kv;
	private JLabel label;
	private JComponent input;
	private FieldType type;

	private FormField(FieldType type) {
		// Factory
		this.type = type;
	}

	public static FormField create(FieldType type) {
		return new FormField(type);
	}

	public Object getValue() {
		if (type.equals(FieldType.COMBOBOX)) {
			return ((KV)combo().getSelectedItem()).getValue();
		} else if (type.equals(FieldType.DEFAULT_TEXT_FIELD)) {
			return text().getText();
		}
		return null;
	}

	/**
	 * 如果要设置组合框的默认值，把默认值提到开头
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FormField setValue(Object value) {
		if (type.equals(FieldType.COMBOBOX)) {
			@SuppressWarnings("rawtypes")
			JComboBox combo = combo();
			if (value instanceof KV[]) {
				combo.removeAllItems();
				Arrays.stream((KV[]) value).forEach(combo::addItem);
			} else {
				combo.addItem(value);
			}
		} else if (type.equals(FieldType.DEFAULT_TEXT_FIELD)) {
			text().setText(value.toString());
		}else if (type.equals(FieldType.TABLE_SELECTOR)) {
			input.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						JDialog dialog = new JDialog();
						if (form!=null) {
							Point point=form.getLocation();
							dialog.setLocationRelativeTo(form);
							dialog.setLocation((int) point.getX(), (int) point.getY());
						}
						dialog.setSize(500,500);
						dialog.setModal(true);
						dialog.setLayout(new FlowLayout());
						StandardTableSelector selector=(StandardTableSelector)value;
						selector.set("field",input);
						selector.set("form",form);
						dialog.add(selector.update());
						dialog.setVisible(true);
					}
				}
			});
		}
		return this;
	}

	public FormField disable() {
		if (type.equals(FieldType.COMBOBOX)) {
			combo().setEditable(false);
		} else if (type.equals(FieldType.DEFAULT_TEXT_FIELD)) {
			text().setEditable(false);
		} else if (type.equals(FieldType.TABLE_SELECTOR)) {

		}
		return this;
	}
	
	public FormField hidden() {
		if (type.equals(FieldType.COMBOBOX)) {
			combo().setEditable(false);
		} else if (type.equals(FieldType.DEFAULT_TEXT_FIELD)) {
			text().setEditable(false);
		} else if (type.equals(FieldType.TABLE_SELECTOR)) {

		}
		return this;
	}
	
	public FormField setRoot(Form container) {
		this.form=container;
		return this;
	}

	public KV getKv() {
		return kv;
	}

	public FormField setKv(KV kv) {
		this.kv = kv;
		return this;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox combo() {
		if (input == null) {
			input = new JComboBox<>();
			input.setBorder(new RoundBorder().padding(0));
		}
		return (JComboBox) input;
	}

	public JTextField text() {
		if (input == null) {
			input = new JTextField();
			input.setBorder(new RoundBorder());
			if (type.equals(FieldType.TABLE_SELECTOR)) {
				((JTextField) input).setEditable(false);
				
			}
		}
		return (JTextField) input;
	}

	public JLabel label() {
		if (label == null) {
			label = new JLabel();
		}
		return label;
	}

	public JComponent input() {
		if (type.equals(FieldType.COMBOBOX)) {
			return combo();
		} else if (type.equals(FieldType.DEFAULT_TEXT_FIELD) || type.equals(FieldType.TABLE_SELECTOR)) {
			return text();
		} 
		return null;
	}

	public JLabel getLabel() {
		return label;
	}

	public FormField setLabel(JLabel label) {
		this.label = label;
		return this;
	}

	public JComponent getInput() {
		return input;
	}

	public FormField setInput(JComponent input) {
		this.input = input;
		return this;
	}
}
