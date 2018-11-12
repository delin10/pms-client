package pms.client.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;

import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import pms.client.ui.intef.SwitchPanel;
import util.ui.swing.bean.KV;
import util.ui.swing.comm.Util;
import util.ui.swing.gen.CompGenerator;
import util.ui.swing.model.table.JSONTableModel;

public class DMPanel extends SwitchPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private final int PANEL_W = 600;
	private final int PANEL_H = 700;
	private JLabel title = new JLabel("数据库备份列表");
	private JTable table = new JTable();
	private Box root = Box.createVerticalBox();
	private Box top_title_box = Box.createHorizontalBox();
	private ToolBox tool_box = new ToolBox();
	private JScrollPane scroll = new JScrollPane();
	// 下载人图标
	private Box button_group = Box.createHorizontalBox();
	private JButton set_role_button = CompGenerator.genAddButton("设置角色");

	public DMPanel init() {
		this.setSize(new Dimension(PANEL_W, PANEL_H));
		title.setFont(new Font("宋体", Font.BOLD, 20));
		table.setDragEnabled(false);
		scroll.setViewportView(table);
		scroll.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
		table.setPreferredSize(new Dimension(500, 500));
		scroll.setPreferredSize(new Dimension(500, 500));

		top_title_box.add(Box.createHorizontalStrut(40));
		top_title_box.add(title);

		button_group.add(set_role_button, Box.LEFT_ALIGNMENT);

		root.add(top_title_box);
		root.add(button_group);
		root.add(scroll);
		tool_box.init();
		tool_box.setParent(root);
		this.add(root);
		return this;
	}

	public SwitchPanel initTable() {
		Func.exec(() -> {
			UIHandler.handle_backups_table(Func.backups(), table);
			JSONTableModel model = (JSONTableModel) table.getModel();

			ActionListener[] listeners = new ActionListener[] { e -> {
				Func.exec(()->{
					UIHandler.handle_backup();
				});
			}, e -> {
				int i = table.getSelectedRow();
				if (i >= 0) {
					String path=model.getValuesAt(i).get("path").toString();
					Func.exec(()->{
						UIHandler.handle_retore_backup(path);
					});
				}
			},e->{
				int[] is = table.getSelectedRows();
				if (is.length > 0) {
					String[] paths=Arrays.stream(is).mapToObj(model::getValuesAt).map(map->map.get("path")).map(Object::toString).toArray(String[]::new);
					Func.exec(()->{
						UIHandler.handle_delete_backup(paths);
					});
					
				}
			}};
			table.setComponentPopupMenu(CompGenerator.genPopupMenu(null, null, listeners, "备份数据库", "还原备份","删除备份"));
		});
		return this;
	}

	@Override
	public SwitchPanel switchIn(JComponent container, Runnable run) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Util.runUi(() -> {
			container.removeAll();
			container.add(this);
			// table.updateUI();
			// scroll.updateUI();
			// root.updateUI();
			this.updateUI();
			container.updateUI();
			if (run != null) {
				run.run();
			}
		});
		return this;
	}

	@Override
	public SwitchPanel refresh() {
		// TODO Auto-generated method stub
		UIHandler.handle_backups_table(Func.backups(), table);
		return this;
	}
}
