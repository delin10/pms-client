package util.ui.swing.model.form.factory;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import util.ui.swing.bean.KV;
import util.ui.swing.comm.Util;
import util.ui.swing.model.form.FieldType;

public class ComboBoxBox extends Box  {
	private static final long serialVersionUID = 3428150899800634508L;
	private Box box = Box.createHorizontalBox();
	private JLabel label = new JLabel();
	private JComboBox<KV> field = new JComboBox<>();
	private int hgap;
	private FieldType type = FieldType.COMBOBOX;

	private ComboBoxBox() {
		super(BoxLayout.LINE_AXIS);
		// TODO Auto-generated constructor stub
	}

	public static ComboBoxBox create() {
		return new ComboBoxBox();
	}
	
	public ComboBoxBox init_size(int field_width,int field_height,int hgap) {
		this.hgap=hgap;
		field.setPreferredSize(new Dimension(field_width, field_height));
		return this;
	}
	
	public ComboBoxBox init_label(String text) {
		label.setText(text);
		return this;
	}
	
	public ComboBoxBox init_field(KV...content) {
		if (content!=null) {
			Arrays.stream(content).forEach(field::addItem);
		}
		return this;
	}
	
	public ComboBoxBox build() {
		Util.addBatch(box, label,Box.createHorizontalStrut(hgap),field);
		return this;
	}

	public JComponent  getInput() {
		return field;
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public FieldType type() {
		return type;
	}
}
