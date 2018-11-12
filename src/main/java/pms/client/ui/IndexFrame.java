package pms.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import pms.client.data.RuntimeStore;
import pms.client.funcs.Func;
import pms.client.handler.request.impl.MenusRequestImpl;
import pms.client.ui.impl.BuildingPanelImpl;
import pms.client.ui.impl.ChargeFormPanelImpl;
import pms.client.ui.impl.ChargeItemPanelImpl;
import pms.client.ui.impl.ComunityPanelImpl;
import pms.client.ui.impl.ContractPanelImpl;
import pms.client.ui.impl.DataBaseManagePanelImpl;
import pms.client.ui.impl.DepartmentPanelImpl;
import pms.client.ui.impl.DevicePanelImpl;
import pms.client.ui.impl.EmployeePanelImpl;
import pms.client.ui.impl.OwnerPanelImpl;
import pms.client.ui.impl.RolePanelImpl;
import pms.client.ui.impl.RoomPanelImpl;
import pms.client.ui.impl.UserPanelImpl;
import pms.client.ui.intef.SwitchPanel;
import util.ui.swing.comm.Util;
import util.ui.swing.model.tree.node.IdentifiedTreeNode;

public class IndexFrame extends JFrame {
	private static final long serialVersionUID = 3543475485866142125L;
	private final int FRAME_W = 800;
	private final int FRAME_H = 900;
	private final HashMap<String, SwitchPanel> panels = new HashMap<>();
	private JTree tree = new JTree();
	private JMenuBar bar = new JMenuBar();
	private Box main_box = Box.createVerticalBox();
	private Box tree_box = Box.createVerticalBox();
	private JScrollPane scroll = new JScrollPane(tree);
	private MenusRequestImpl req=new MenusRequestImpl();

	public JFrame init() {
		this.setTitle("物业管理系统-主窗口");
		this.setSize(FRAME_W, FRAME_H);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(bar);
		// 只有设置preferedSize才有用？
		scroll.setPreferredSize(new Dimension(200, FRAME_H));
		tree_box.add(scroll);
		main_box.add(new CompanyInfoPanel().init().refresh());
		this.getContentPane().add(tree_box, BorderLayout.WEST);
		this.getContentPane().add(main_box, BorderLayout.CENTER);
		initTree();
		return this;
	}

	public void initTree() {
		tree.setMinimumSize(new Dimension(400, FRAME_H));
		tree.addTreeSelectionListener(e -> {
			IdentifiedTreeNode node = (IdentifiedTreeNode) tree.getLastSelectedPathComponent();
			String id = node.getId();
			SwitchPanel panel = panels.get(id);
			System.out.println(id);
			if ("User".equalsIgnoreCase(id)) {
				if (panel == null) {
					panel = new UserPanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});

			} else if ("role".equalsIgnoreCase(id)) {
				if (panel == null) {
					panel = new RolePanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			} else if ("db".equalsIgnoreCase(id)) {
				if (panel == null) {
					panel = new DataBaseManagePanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			} else if ("companyInfo".equalsIgnoreCase(id)) {
				if (panel == null) {
					panel = new CompanyInfoPanel().init();
					panels.put(id, panel);
				}
				panel.refresh().switchIn(main_box, () -> {
					this.repaint();
				});
			} else if ("houseManage".equalsIgnoreCase(id)) {
				if (panel == null) {
					panel = new BuildingPanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			} else if ("communityManage".equalsIgnoreCase(id)) {
				if (panel == null) {
					panel = new ComunityPanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			}else if ("clientManage".equalsIgnoreCase(id)) {
				if (panel == null) {
					panel = new OwnerPanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			}else if ("roomManage".equals(id)) {
				if (panel == null) {
					panel = new RoomPanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			}else if ("contractManage".equals(id)) {
				if (panel == null) {
					panel = new ContractPanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			}else if ("employeeManage".equals(id)) {
				if (panel == null) {
					panel = new EmployeePanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			}else if ("departmentManage".equals(id)) {
				if (panel == null) {
					panel = new DepartmentPanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			}else if ("deviceManage".equals(id)) {
				if (panel == null) {
					panel = new DevicePanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			}else if ("chargeItemSetting".equals(id)) {
				if (panel == null) {
					panel = new ChargeItemPanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			}else if ("chargeRecord".equals(id)) {
				if (panel == null) {
					panel = new ChargeFormPanelImpl().init().initTable();
					panels.put(id, panel);
				}else {
					panel.refresh();
				}
				panel.switchIn(main_box, () -> {
					this.repaint();
				});
			}
		});
		Func.exec(() -> {
			RuntimeStore.runUI().generateTree(req.menus(), tree, this);
			Util.runUi(() -> {
				tree.updateUI();
				tree_box.updateUI();
			});

		});
	}
}
