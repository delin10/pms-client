package util.ui.swing.model.form.field.impl;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextField;

import util.ui.swing.model.form.field.FormField;
import util.ui.swing.model.table.impl.StandardTableSelector;

public class StandardTableSelectorField extends FormField{
	private static final long serialVersionUID = 1L;
	private JTextField input;

	public StandardTableSelectorField(Container form) {
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
		StandardTableSelector selector=(StandardTableSelector)value;
		String _default_=(String) selector.get("_default_");
		input.setEditable(false);
		if (_default_!=null) {
			input.setText(_default_);
		}
		input.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JDialog dialog = new JDialog();
					if (form!=null) {
						Point point=form.getLocationOnScreen();
						System.out.println(point);
						dialog.setLocationRelativeTo(null);
						dialog.setLocation((int) point.getX()+(form.getWidth()-500)/2, (int) point.getY());
					}
					dialog.setSize(500,500);
					dialog.setModal(true);
					dialog.setLayout(new FlowLayout());

					selector.set("_form_",form);
					selector.set("_root_", dialog);
					dialog.add(selector.update());
					dialog.setVisible(true);
				}
			}});
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
		input=new JTextField();
		super.init();
	}

	@Override
	public void init(Object value) {
		// TODO Auto-generated method stub
		StandardTableSelector selector=(StandardTableSelector)value;
		String _default_=(String) selector.get("_default_");
		input.setEditable(false);
		if (_default_!=null) {
			input.setText(_default_);
		}
		input.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JDialog dialog = new JDialog();
					if (form!=null) {
						Point point=form.getLocationOnScreen();
						System.out.println(point);
						dialog.setLocationRelativeTo(null);
						dialog.setLocation((int) point.getX()+(form.getWidth()-500)/2, (int) point.getY());
					}
					dialog.setSize(500,500);
					dialog.setModal(true);
					dialog.setLayout(new FlowLayout());

					selector.set("_form_",form);
					selector.set("_root_", dialog);
					dialog.add(selector.update());
					dialog.setVisible(true);
				}
			}});
	}
}
