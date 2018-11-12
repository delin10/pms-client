package util.ui.swing.model.table.impl;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import pms.client.ui.ToolBox;
import util.comm.lambda.functon.SimpleTask;
import util.ui.swing.comm.Util;
import util.ui.swing.model.table.TableSelector;

public class StandardTableSelector  extends TableSelector {
	private static final long serialVersionUID = 2847752370474961076L;
	private JTable table = new JTable();
	private JScrollPane scrollPane = new JScrollPane();
	private ToolBox tool_box=new ToolBox();
	private Map<String, Object> attrs=new HashMap<>();

	private StandardTableSelector() {
	}

	public static StandardTableSelector create() {
		return new StandardTableSelector();
	}

	public StandardTableSelector size(int width, int height) {
		this.setPreferredSize(new Dimension(width + 20, height+50));
		scrollPane.setPreferredSize(new Dimension(width, height));
		return this;
	}
	
	public StandardTableSelector init_table_by(SimpleTask task) {
		task.exec(table);
		return this;
	}

	public StandardTableSelector header_size(int width, int height) {
		Util.setHeaderWidth(table, width, height);
		return this;
	}
	
	public StandardTableSelector setModel(TableModel model) {
		table.setModel(model);
		return this;
	}
	
	public TableModel getModel() {
		return table.getModel();
	}

	public StandardTableSelector build() {
		Box root = Box.createVerticalBox();
		Box table_box = Box.createHorizontalBox();
		Box button_group = Box.createHorizontalBox();

		scrollPane.setViewportView(table);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		table_box.add(scrollPane);
		root.add(tool_box.init());
		Util.addBatch(root, table_box, Box.createVerticalStrut(20), button_group);
		this.add(root);
		return this;
	}
	
	public Object get(String key) {
		return attrs.get(key);
	}
	
	public StandardTableSelector set(String key,Object value) {
		attrs.put(key, value);
		return this;
	}
	/**
	 * 第一个参数为对话框跟窗口
	 * 第二个参数为需要显示的列数
	 */
	@Override
	public StandardTableSelector comfirm(SimpleTask task,Object...args) {
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2) {
					int select=table.getSelectedRow();
					if (select>=0) {
						System.out.println(args[0]);
						//int choose=JOptionPane.showConfirmDialog((Component) attrs.get("_root_"), "是否选择"+table.getModel().getValueAt(select, Integer.parseInt(args[0].toString())), "选择信息", JOptionPane.YES_NO_OPTION);
						task.exec(select,table.getModel(),attrs);
						((Window) attrs.get("_root_")).dispose();
					}
				}
			}
		});
		return this;
		
	}
	
	public StandardTableSelector update() {
		Util.runUi(()->{
			table.updateUI();
			scrollPane.updateUI();
		});
		return this;
	}

}
