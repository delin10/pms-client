package util.ui.swing.model.form;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.alibaba.fastjson.JSONObject;

import util.comm.lambda.StreamUtil;
import util.ui.swing.bean.KV;
import util.ui.swing.border.RoundBorder;
import util.ui.swing.comm.Util;

public class StandardForm  extends Form {
	private static final long serialVersionUID = 565139623856282603L;
	private int top_gap = 50;
	private int bottom_gap = 50;
	private JPanel root_container = new JPanel();
	private HashMap<String, Object> attrs = new HashMap<>();
	private HashMap<String, Input> inputs = new HashMap<>();
	private Box root = Box.createVerticalBox();
	private JButton submit_button = new JButton("提交");
	private JButton cancel_button = new JButton("取消");
	private Box button_group = Box.createHorizontalBox();
	private KV[] kvs;
	private int max_word_len = 0;;
	private int hgap;
	private int vgap;

	public StandardForm(KV[] kvs) {
		this.kvs = kvs;
	}

	public static StandardForm create(KV[] kvs) {
		return new StandardForm(kvs);
	}

	public StandardForm init(int hgap, int vgap) {
		root_container.setLayout(new FlowLayout());
		this.hgap = hgap;
		this.vgap = vgap;
		root.add(Box.createVerticalStrut(top_gap));
		Arrays.stream(kvs).forEach(kv -> {
			JLabel label = new JLabel(kv.getTitle());
			JTextField input = new JTextField();
			label.setHorizontalAlignment(JLabel.RIGHT);
			input.setBorder(new RoundBorder());
			Box box = Box.createHorizontalBox();
			// box.setBorder(BorderFactory.createLoweredBevelBorder());
			Util.addBatch(box, label, Box.createHorizontalStrut(hgap), input);
			Util.addBatch(root, box, Box.createVerticalStrut(vgap));
			int len = kv.getTitle().length();
			max_word_len = max_word_len > len ? max_word_len : len;
			inputs.put(kv.getField(), new Input().setLabel(label).setInput(input).setRoot(box));
		});
		root.add(button_group);
		root.add(Box.createVerticalStrut(bottom_gap));
		root_container.add(root);
		this.setViewportView(root_container);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// root.setBorder(BorderFactory.createLoweredBevelBorder());
		return this;
	}

	public StandardForm hidden(String... fields) {
		HashSet<String> set = StreamUtil.parse(fields, HashSet<String>::new);
		inputs.entrySet().forEach(input -> {
			if (set.contains(input.getKey())) {
				Box root = input.getValue().getRoot();
				root.setVisible(false);
				root.setPreferredSize(new Dimension(0, 0));
			}
		});
		return this;
	}

	public StandardForm disable(String... fields) {
		HashSet<String> set = StreamUtil.parse(fields, HashSet<String>::new);
		inputs.entrySet().forEach(input -> {
			if (set.contains(input.getKey())) {
				Input root = input.getValue();
				root.getInput().setEditable(false);
			}
		});
		return this;
	}

	public StandardForm attr(String name, Object value) {
		attrs.put(name, value);
		return this;
	}

	public StandardForm initSize(int root_height, Dimension size) {
		// max_word_len * 15 + hgap + (int) size.getWidth()
		Dimension scroll_size = new Dimension((int)((hgap+size.getWidth()+max_word_len*15)), (int) (top_gap+bottom_gap+inputs.size()*(vgap+size.getHeight())));
		inputs.values().forEach(input -> {
			// (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()
			// prefer设置大小,FlowLayout,注意layout对设置大小的影响
			 input.getLabel().setPreferredSize(new Dimension(max_word_len * 15, (int)
			 size.getHeight()));
			 input.getInput().setPreferredSize(size);
			Util.addToBoxBatch(button_group, Box.CENTER_ALIGNMENT, submit_button, Box.createHorizontalStrut(hgap),
					cancel_button);
		});
		System.out.println(scroll_size);
		root_container.setPreferredSize(scroll_size);
		return this;
	}

	public StandardForm fill(Map<String, Object> values) {
		inputs.entrySet().stream().forEach(entry -> {
			entry.getValue().getInput().setText(values.get(entry.getKey()) + "");
		});
		return this;
	}

	@Override
	public Form onSubmit(FormFunc func) {
		getSubmit_button().addActionListener(e -> {
			func.handle(e, toMap());
		});
		return this;
	}

	@Override
	public Form onCanel(FormFunc func) {
		// TODO Auto-generated method stub
		getCancel_button().addActionListener(e -> {
			func.handle(e, toMap());
		});
		return this;
	}

	public StandardForm complete() {
		return this;
	}

	public HashMap<String, String> toMap() {
		HashMap<String, String> map = new HashMap<>();
		inputs.entrySet().forEach(input -> {
			map.put(input.getKey(), input.getValue().getInput().getText());
		});
		return map;
	}

	public String toJSONString() {
		JSONObject jsonObject = new JSONObject();
		inputs.entrySet().forEach(input -> {
			jsonObject.put(input.getKey(), input.getValue().getInput().getText());
		});
		return jsonObject.toJSONString();
	}

	public Object val(String name) {
		return attrs.get(name);
	}

	public JButton getSubmit_button() {
		return submit_button;
	}

	public JButton getCancel_button() {
		return cancel_button;
	}

	public JTextField input(String field) {
		return (JTextField) inputs.get(field).getInput();
	}

	public static class Input {
		private Box root;
		private JLabel label;
		private JTextField input;

		public JLabel getLabel() {
			return label;
		}

		public Input setLabel(JLabel label) {
			this.label = label;
			return this;
		}

		public JTextField getInput() {
			return input;
		}

		public Input setInput(JTextField input) {
			this.input = input;
			return this;
		}

		public Box getRoot() {
			return root;
		}

		public Input setRoot(Box root) {
			this.root = root;
			return this;
		}
	}
}
