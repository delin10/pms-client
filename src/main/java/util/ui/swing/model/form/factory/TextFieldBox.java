package util.ui.swing.model.form.factory;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import util.ui.swing.bean.KV;
import util.ui.swing.comm.Util;
import util.ui.swing.model.form.FieldType;

public class TextFieldBox extends Box {
	private static final long serialVersionUID = 3428150899800634508L;
	private Box box = Box.createHorizontalBox();
	private JLabel label = new JLabel();
	private JTextField field = new JTextField();
	private KV mapper;
	private int hgap;
	private FieldType type = FieldType.DEFAULT_TEXT_FIELD;

	private TextFieldBox() {
		super(BoxLayout.LINE_AXIS);
		// TODO Auto-generated constructor stub
	}

	public static TextFieldBox create() {
		return new TextFieldBox();
	}
	
	public TextFieldBox init_size(int field_width,int field_height,int hgap) {
		this.hgap=hgap;
		field.setPreferredSize(new Dimension(field_width, field_height));
		return this;
	}
	
	public TextFieldBox init_label(String text) {
		label.setText(text);
		return this;
	}
	
	public TextFieldBox init_field(KV...content) {
		if (content!=null) {
			field.setText(content[0].getTitle());
			mapper=content[0];
		}
		return this;
	}
	
	public TextFieldBox build() {
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
	
	public String getField() {
		return mapper.getField();
	}
}
