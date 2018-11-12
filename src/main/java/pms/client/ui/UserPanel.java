package pms.client.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import pms.client.ui.intef.SwitchPanel;
import util.ui.swing.comm.Util;
import util.ui.swing.gen.CompGenerator;
import util.ui.swing.model.table.JSONTableModel;

public class UserPanel extends SwitchPanel {
	private static final long serialVersionUID = 8566031432614802194L;
	private final int PANEL_W = 600;
	private final int PANEL_H = 700;
	private JLabel title = new JLabel("用户列表");
	private JTable table = new JTable();
	private Box root = Box.createVerticalBox();
	private Box top_title_box = Box.createHorizontalBox();
	private ToolBox tool_box = new ToolBox();
	private JScrollPane scroll = new JScrollPane();
	// 下载人图标
	private Box button_group = Box.createHorizontalBox();
	private JButton set_role_button = CompGenerator.genAddButton("设置角色");

	public UserPanel init() {
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
			UIHandler.handler_table(Func.users(), table);
			JSONTableModel model = (JSONTableModel) table.getModel();

			ActionListener[] listeners = new ActionListener[] { e -> {
				int selected = table.getSelectedRow();
				if (selected != -1) {
					Map<String, Object> map = model.getValuesAt(selected);
					UIHandler.showTableSector(args -> {
						JTable t = (JTable) args[0];
						UIHandler.handle_roles_table(Func.roles(), t);
						return null;
					}, args1 -> {
						JSONTableModel m = (JSONTableModel) args1[1];
						int i = (int) args1[0];
						String role_id = (String) m.getValuesAt(i).get("id");
						String user_id = (String) model.getValuesAt(selected).get("userid");
						System.out.println(model.getValuesAt(selected));
						System.out.println("role_id=" + role_id);
						System.out.println("user_id=" + user_id);
						UIHandler.handle_set_role(user_id,role_id);
						return null;
					}, "description");
				}
			} };
			table.setComponentPopupMenu(CompGenerator.genPopupMenu(null, null, listeners, "修改角色"));
		});
		return this;
	}

	@Override
	public SwitchPanel switchIn(JComponent container, Runnable run) {
		Func.exec(() -> {
			UIHandler.handler_table(Func.users(), table);
			// TODO Auto-generated method stub
			Util.runUi(() -> {
				container.removeAll();
				container.add(this);
				// table.updateUI();
				// scroll.updateUI();
				// root.updateUI();
				this.updateUI();
				container.updateUI();
				run.run();
			});
		});
		return this;
	}

	@Override
	public SwitchPanel refresh() {
		// TODO Auto-generated method stub
		UIHandler.handler_table(Func.users(), table);
		return null;
	}
}
