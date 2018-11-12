package util.ui.swing.model.form;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javafx.scene.control.SelectionModel;
import util.comm.lambda.StreamUtil;
import util.ui.swing.bean.KV;
import util.ui.swing.comm.Util;
import util.ui.swing.model.form.bean.FormField;
import util.ui.swing.model.form.model.FormModel;

public class ComplexForm extends Form {
	private static final long serialVersionUID = 565139623856282603L;
	private Container container;
	private JPanel root_container = new JPanel();
	private HashMap<String, FormField> fields = new HashMap<>();
	private Box root = Box.createVerticalBox();
	private JButton submit_button = new JButton("提交");
	private JButton cancel_button = new JButton("取消");
	private Box button_group = Box.createHorizontalBox();
	private KV[] kvs;
	private Map<String, FieldType> mapper = new HashMap<>();
	private Dimension scroll_size;
	private Dimension field_size;
	private int top_gap = 50;
	private int bottom_gap = 50;
	private int max_word_len = 0;
	private int hgap;
	private int vgap;
	private FormModel model;

	public ComplexForm(KV[] kvs, Container container) {
		this.kvs = kvs;
		this.container = container;
	}

	public static ComplexForm create(KV[] kvs, Container container) {
		return new ComplexForm(kvs, container);
	}

	public ComplexForm init_field(Map<String, FieldType> mapper) {
		this.mapper.putAll(mapper);
		return this;
	}

	public ComplexForm init_size(int root_height, Dimension size) {
		scroll_size = new Dimension((int) ((hgap + size.getWidth() + max_word_len * 15)),
				(int) (top_gap + bottom_gap + fields.size() * (vgap + size.getHeight())));
		field_size = size;
		return this;

	}

	public ComplexForm init_gap(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
		return this;
	}

	public ComplexForm fill(Map<String, Object> values) {
		if (values != null) {
			fields.entrySet().stream().forEach(entry -> {
				Object value = values.get(entry.getKey());
				System.out.println(value);
				if (value != null) {
					entry.getValue().setValue(value);
				}
			});
		}
		return this;
	}

	public ComplexForm disable(String... fields) {
		if (fields != null) {
			Arrays.stream(fields).forEach(field -> {
				FormField input = this.fields.get(field);
				System.out.println(field + input);
				if (input != null) {
					input.disable();
				}
			});
		}
		return this;
	}

	public ComplexForm hidden(String... fields) {
		if (fields != null) {
			Arrays.stream(fields).forEach(field -> {
				this.fields.get(field);//hidden;
			});
		}
		return this;
	}

	public ComplexForm build() {
		root.add(Box.createVerticalStrut(top_gap));
		root_container.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
		Arrays.stream(kvs).forEach(kv -> {
			String field = kv.getField();
			FieldType type = mapper.get(field);
			FormField formField = FormField.create(type == null ? FieldType.DEFAULT_TEXT_FIELD : type);
			JLabel label = formField.label();
			JComponent input = formField.input();
			label.setHorizontalAlignment(JLabel.RIGHT);
			label.setText(kv.getTitle());
			Box box = Box.createHorizontalBox();
			if (FieldType.TABLE_SELECTOR.equals(type)) {
				formField.setRoot(this);
			}
			// box.setBorder(BorderFactory.createLoweredBevelBorder());
			Util.addBatch(box, label, Box.createHorizontalStrut(hgap), input);
			Util.addBatch(root, box, Box.createVerticalStrut(vgap));
			int len = kv.getTitle().length();
			max_word_len = max_word_len > len ? max_word_len : len;
			fields.put(kv.getField(), formField);
		});
		fields.values().forEach(input -> {
			input.getLabel().setPreferredSize(new Dimension(max_word_len * 15, (int) field_size.getHeight()));
			input.getInput().setPreferredSize(field_size);
		});
		Util.addToBoxBatch(button_group, Box.CENTER_ALIGNMENT, submit_button, Box.createHorizontalStrut(hgap),
				cancel_button);
		root.add(button_group);
		root.add(Box.createVerticalStrut(bottom_gap));
		// root_container.setLayout(new FlowLayout(FlowLayout.CENTER,
		// (scroll_size.width-field_size.width-max_word_len * 15-hgap)/2, vgap));
		root_container.add(root);
		this.setViewportView(root_container);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		return this;
	}
	
	
	public ComplexForm buildByModel(FormModel model) {
		this.model=model;
		root.add(Box.createVerticalStrut(top_gap));
		root_container.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
		Arrays.stream(kvs).forEach(kv -> {
			String field = kv.getField();
			FieldType type = mapper.get(field);
			FormField formField = FormField.create(type == null ? FieldType.DEFAULT_TEXT_FIELD : type);
			JLabel label = formField.label();
			JComponent input = formField.input();
			label.setHorizontalAlignment(JLabel.RIGHT);
			label.setText(kv.getTitle());
			Box box = Box.createHorizontalBox();
			if (FieldType.TABLE_SELECTOR.equals(type)) {
				formField.setRoot(this);
			}
			// box.setBorder(BorderFactory.createLoweredBevelBorder());
			Util.addBatch(box, label, Box.createHorizontalStrut(hgap), input);
			Util.addBatch(root, box, Box.createVerticalStrut(vgap));
			int len = kv.getTitle().length();
			max_word_len = max_word_len > len ? max_word_len : len;
			fields.put(kv.getField(), formField);
		});
		fields.values().forEach(input -> {
			input.getLabel().setPreferredSize(new Dimension(max_word_len * 15, (int) field_size.getHeight()));
			input.getInput().setPreferredSize(field_size);
		});
		Util.addToBoxBatch(button_group, Box.CENTER_ALIGNMENT, submit_button, Box.createHorizontalStrut(hgap),
				cancel_button);
		root.add(button_group);
		root.add(Box.createVerticalStrut(bottom_gap));
		// root_container.setLayout(new FlowLayout(FlowLayout.CENTER,
		// (scroll_size.width-field_size.width-max_word_len * 15-hgap)/2, vgap));
		root_container.add(root);
		this.setViewportView(root_container);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		return this;
	}

	@Override
	public ComplexForm onSubmit(FormFunc func) {
		getSubmit_button().addActionListener(e -> {
			func.handle(e, toMap());
		});
		return this;
	}

	@Override
	public ComplexForm onCanel(FormFunc func) {
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
			map.put(input.getKey(), input.getValue().getValue().toString());
		});
		return map;
	}

}
