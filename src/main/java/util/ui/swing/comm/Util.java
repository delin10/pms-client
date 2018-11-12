package util.ui.swing.comm;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.alibaba.fastjson.JSONObject;

import util.comm.lambda.DStream;
import util.ui.swing.BatchFunc;
import util.ui.swing.event.focus.FocusFunc;
import util.ui.swing.event.focus.FocusRecordAdapter;

public class Util {
	public static void setSize(int width, int height, Component... comps) {
		Arrays.stream(comps).forEach(comp -> comp.setSize(width, height));
	}

	public static void setMaxSize(int width, int height, Component... comps) {
		Arrays.stream(comps).forEach(comp -> comp.setMaximumSize(new Dimension(width, height)));
	}

	public static void setMinSize(int width, int height, Component... comps) {
		Arrays.stream(comps).forEach(comp -> comp.setMinimumSize(new Dimension(width, height)));
	}

	public static void setPreSize(int width, int height, Component... comps) {
		Arrays.stream(comps).forEach(comp -> comp.setPreferredSize(new Dimension(width, height)));
	}

	public static void setAlign(float align, JLabel... comps) {
		Arrays.stream(comps).forEach(comp -> comp.setAlignmentX(align));
	}

	public static void enable(Component... comps) {
		Arrays.stream(comps).forEach(comp -> comp.setEnabled(true));
	}

	public static void disable(Component... comps) {
		Arrays.stream(comps).forEach(comp -> comp.setEnabled(false));
	}

	public static void addBatch(Container container, Component... components) {
		Arrays.stream(components).forEach(container::add);
	}

	public static void addToBoxBatch(Box container, float align, Component... components) {
		Arrays.stream(components).forEach(component -> {
			container.add(component, align);
		});
	}

	public static void setBorder(Border border, JTextField... components) {
		Arrays.stream(components).forEach(component -> {
			component.setBorder(border);
		});
	}

	public static boolean isEmpty(JTextField field) {
		return field.getText().trim().isEmpty();
	}

	public static boolean isEmpty(JPasswordField field) {
		return String.valueOf(field).trim().isEmpty();
	}

	public static void runUi(Runnable run) {
		SwingUtilities.invokeLater(run);
	}

	public static void record(FocusFunc gain, JTextField... ins) {
		Arrays.stream(ins).forEach(in -> {
			FocusRecordAdapter recordAdapter = (FocusRecordAdapter) FocusRecordAdapter.create();
			recordAdapter.setGained(e -> {
				recordAdapter.setValue(in.getText());
				if (gain != null) {
					gain.handle(e);
				}
				return null;
			});
			in.addFocusListener(recordAdapter);
		});
	}

	public static void batch(BatchFunc batch, Object[] args, JComponent... comps) {
		Arrays.stream(comps).forEach(comp -> {
			batch.batch(comp, args);
		});
	}

	public static void resume(int index, JTextField... ins) {
		Arrays.stream(ins).forEach(in -> {
			Optional<FocusListener> adapter = Arrays.stream(in.getFocusListeners())
					.filter(listener -> listener instanceof FocusRecordAdapter).reduce((acc, next) -> acc = next);
			if (adapter.isPresent()) {
				FocusRecordAdapter recordAdapter = (FocusRecordAdapter) adapter.get();
				in.setText(recordAdapter.getValue().toString());
			}
		});
	}

	public static String map(String[] fields, JTextField... ins) {
		JSONObject json = new JSONObject();
		DStream.newInstance().merge(Arrays.stream(fields), Arrays.stream(ins))
				.map(null, o -> ((JTextField) o).getText())
				.mergeMap(args -> json.put(args[0].toString(), args[1].toString()));
		return json.toJSONString();
	}

	public static void updateUI(JComponent... comps) {
		Arrays.stream(comps).forEach(comp -> comp.updateUI());
	}

	public static void setHeaderWidth(JTable table, int width, int height) {
//		JTableHeader head = table.getTableHeader(); // 创建表格标题对象
//		head.setMinimumSize(new Dimension(width, height));// 设置表头大小
//		head.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
		table.setRowHeight(25);// 设置表格行宽

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);// 以下设置表格列宽
		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			TableColumn column = model.getColumn(i);
			column.setPreferredWidth(width);
		}
	}

}
