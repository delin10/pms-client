package util.ui.swing.model.form;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import util.ui.swing.border.RoundBorder;
import util.ui.swing.comm.Util;
import util.ui.swing.model.form.factory.FieldFactory;
import util.ui.swing.model.form.field.FormField;
import util.ui.swing.model.form.model.FormModel;

public class ComplexForm2 extends Form {
	private static final long serialVersionUID = 565139623856282603L;
	private JPanel root_container = new JPanel();
	private HashMap<String, FormField> fields = new HashMap<>();
	private Box root = Box.createVerticalBox();
	private JButton submit_button = new JButton("提交");
	private JButton cancel_button = new JButton("取消");
	private Box button_group = Box.createHorizontalBox();
	private int top_gap = 50;
	private int bottom_gap = 50;
	private int hgap=10;
	private int vgap=10;
	private FormModel model;

	public ComplexForm2() {}

	public ComplexForm2 fill(Map<String, Object> values) {
		if (values != null) {
			fields.entrySet().stream().forEach(entry -> {
				Object value = values.get(entry.getKey());
				if (value != null) {
					entry.getValue().setValue(value);
				}
			});
		}
		return this;
	}

	public ComplexForm2 disable(String... fields) {
		if (fields != null) {
			Arrays.stream(fields).forEach(field -> {
				FormField input = this.fields.get(field);
				if (input != null) {
					input.disable();;
				}
			});
		}
		return this;
	}

	public ComplexForm2 hidden(String... fields) {
		if (fields != null) {
			Arrays.stream(fields).forEach(field -> {
				this.fields.get(field);// hidden;
			});
		}
		return this;
	}

	public ComplexForm2 buildByModel(FormModel model) {
		this.model = model;
		root.add(Box.createVerticalStrut(top_gap));
		root_container.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
		Arrays.stream(model.fields()).forEach(field -> {
			FieldType type = model.type(field);
			FormField formField = FieldFactory.create(this, type);
			formField.input().setBorder(new RoundBorder().setArc_h(5).setArc_w(5));
			JLabel label = formField.label();
			label.setHorizontalAlignment(JLabel.RIGHT);
			Util.addBatch(root, formField, Box.createVerticalStrut(vgap));
			fields.put(field, formField);
		});
		update();
		Util.addToBoxBatch(button_group, Box.CENTER_ALIGNMENT, submit_button, Box.createHorizontalStrut(hgap),
				cancel_button);
		root.add(button_group);
		root.add(Box.createVerticalStrut(bottom_gap));
		root_container.add(root);
		this.setViewportView(root_container);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		return this;
	}

	public void update() {
		Arrays.stream(model.fields()).forEach(field -> {
			FormField formField = fields.get(field);
			JLabel label = formField.label();
			Object value=model.getValue(field);
			label.setHorizontalAlignment(JLabel.RIGHT);
			label.setText(model.getLabel(field));
			Dimension labelSize = model.labelSize(field);
			label.setPreferredSize(labelSize);
			formField.input().setPreferredSize(model.inputSize(field));
			formField.setValue(value==null?"":value);
			formField.setEnable(!model.disable(field));
			formField.setHidden(model.hidden(field));
			fields.put(field, formField);
		});
		root_container.updateUI();
		this.updateUI();
	}
	
	public FormModel getModel() {
		return model;
	}

	@Override
	public ComplexForm2 onSubmit(FormFunc func) {
		getSubmit_button().addActionListener(e -> {
			func.handle(e, toMap());
		});
		return this;
	}

	@Override
	public ComplexForm2 onCanel(FormFunc func) {
		// TODO Auto-generated method stub
		getCancel_button().addActionListener(e -> {
			func.handle(e, toMap());
		});
		return this;
	}

	public JButton getSubmit_button() {
		return submit_button;
	}

	public JButton getCancel_button() {
		return cancel_button;
	}

	public FormField getField(String field) {
		return fields.get(field);
	}

	public JComponent getPanel() {
		return root_container;
	}

	public HashMap<String, String> toMap() {
		HashMap<String, String> map = new HashMap<>();
		fields.entrySet().forEach(input -> {
			map.put(input.getKey(), input.getValue().getValue().toString().trim());
		});
		return map;
	}

}
