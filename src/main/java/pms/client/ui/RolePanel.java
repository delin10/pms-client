package pms.client.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
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

import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import pms.client.ui.intef.SwitchPanel;
import util.ui.swing.bean.KV;
import util.ui.swing.comm.Util;
import util.ui.swing.gen.CompGenerator;
import util.ui.swing.model.table.JSONTableModel;

public class RolePanel extends SwitchPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private final int PANEL_W = 600;
	private final int PANEL_H = 700;
	private JLabel title = new JLabel("角色列表");
	private JTable table = new JTable();
	private Box root = Box.createVerticalBox();
	private Box top_title_box = Box.createHorizontalBox();
	private ToolBox tool_box = new ToolBox();
	private JScrollPane scroll = new JScrollPane();
	// 下载人图标
	private Box button_group = Box.createHorizontalBox();
	private JButton set_role_button = CompGenerator.genAddButton("添加角色");

	public RolePanel init() {
		this.setSize(new Dimension(PANEL_W, PANEL_H));
		title.setFont(new Font("宋体", Font.BOLD, 20));
		table.setDragEnabled(false);
		scroll.setViewportView(table);
		scroll.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
		table.setPreferredSize(new Dimension(500, 500));
		scroll.setPreferredSize(new Dimension(500, 500));

		top_title_box.add(title, Box.CENTER_ALIGNMENT);

		button_group.add(set_role_button, Box.LEFT_ALIGNMENT);

		root.add(top_title_box);
		root.add(button_group);
		root.add(scroll);
		tool_box.init();
		tool_box.setParent(root);
		this.add(root);
		return this;
	}

	@SuppressWarnings({ "unlikely-arg-type" })
	public RolePanel initTable() {
		Func.exec(() -> {
			UIHandler.handle_roles_table(Func.roles(), table);
			String[] icons = { "edit-normal-16px.png", "del-normal-16px.png", "add-normal-16px.png" , "del-normal-16px.png", "add-normal-16px.png" };
			String[] selected_icon = { "edit-pressed-16px.png", "del-pressed-16px.png", "add-pressed-16px.png" , "del-pressed-16px.png", "add-pressed-16px.png"};
			JSONTableModel model = (JSONTableModel) table.getModel();
			Map<String, Object> init_values = new LinkedHashMap<>();
			Map<String, LinkedList<KV>> choices = new LinkedHashMap<>();
			LinkedList<KV> avails = new LinkedList<>();
			avails.add(new KV().setField("available").setTitle("可用").setValue("1"));
			avails.add(new KV().setField("available").setTitle("不可用").setValue("0"));
			choices.put("available", avails);
			init_values.put("available", new KV[] { new KV().setField("available").setTitle("可用").setValue("1"),
					new KV().setField("available").setTitle("不可用").setValue("0") });

			ActionListener[] listeners = new ActionListener[] { e -> {
				int selected = table.getSelectedRow();
				if (selected != -1) {
					Map<String, Object> map = model.getValuesAt(selected);
					init_values.entrySet().stream().forEach(entry -> {
						// 获取行默认值
						String select_row_value = map.get(entry.getKey()).toString();
						// 获取对应组合框的选择列表
						LinkedList<KV> list = choices.get(entry.getKey());
						// System.out.println(list.size());
						int index = list.indexOf(select_row_value);
						if (index >= 0) {
							KV kv = list.remove(index);
							list.addFirst(kv);
						}
						init_values.put("available", list.toArray(new KV[0]));
					});

					UIHandler.showComplexEditForm(table, "编辑角色", "setting?action=updateRole", args -> {
						String json = (String) args[1];
						System.out.println(json);
						model.setValueAt(json, table.getSelectedRow());
						return null;
					}, new String[] { "available" }, init_values, new String[] { "id" }, model.columns());
				}
			}, e -> {

			}, e -> {
				UIHandler.showComplexForm("新增角色", "setting?action=addRole", (args) -> {

					HttpResponse response = (HttpResponse) args[0];
					UIHandler.add_success_callback(response, args1 -> {

						return null;
					});
					return null;
				}, new String[] { "available" }, init_values, ((JSONTableModel) table.getModel()).columns());
			}, e -> {
				//资源授权面板
				int index = table.getSelectedRow();
				if (index != -1) {
					String id=(String) model.getValuesAt(index).get("id");
					new SelectResourcePanel().init(id).initTree();
				}
			} };
			table.setComponentPopupMenu(
					CompGenerator.genPopupMenu(icons, selected_icon, listeners, "编辑", "删除", "新增", "授予"));
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
		UIHandler.handle_roles_table(Func.roles(), table);
		// TODO Auto-generated method stub
		return this;
	}

}
