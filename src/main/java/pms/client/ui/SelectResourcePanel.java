package pms.client.ui;

import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import util.ui.swing.comm.Util;
import util.ui.swing.model.tree.TreeNodeGenerator;
import util.ui.swing.model.tree.node.CheckedTreeNode;

public class SelectResourcePanel extends JDialog {
	private static final long serialVersionUID = -7239564571861525023L;
	private final int DIALOG_W=600;
	private final int DIALOG_H=600;
	private final int PANEL_W = 600;
	private final int PANEL_H = 700;
	private JPanel panel=new JPanel();
	private JTree tree;
	private Box root_box = Box.createVerticalBox();
	private JButton ok_button = new JButton("确认");
	private JButton cancel_button = new JButton("取消");
	private JScrollPane tree_pane = new JScrollPane();
	private String role_id;

	public SelectResourcePanel init(String role_id) {
		this.role_id=role_id;
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setMinimumSize(new Dimension(DIALOG_W, DIALOG_H));
		this.setLocationRelativeTo(null);
		panel.setSize(new Dimension(PANEL_W, PANEL_H));
		Box button_group = Box.createHorizontalBox();
		
		ok_button.addActionListener(e->{
			if (tree!=null) {
				LinkedList<CheckedTreeNode> selecteds=TreeNodeGenerator.getSelecteds(tree);
				String[] ids=selecteds.stream().map(CheckedTreeNode::getId).toArray(String[]::new);
				UIHandler.handle_auth(role_id, ids);
			}
		});
		button_group.add(ok_button);
		button_group.add(cancel_button);
		tree_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tree_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tree_pane.setPreferredSize(new Dimension(500, 500));
		root_box.add(tree_pane);
		root_box.add(button_group);
		panel.add(root_box);
		this.add(panel);
		this.initTree();
		this.setVisible(true);
		return this;
	}
	
	public SelectResourcePanel initTree() {
		Func.exec(()->{
			tree=UIHandler.generateResourceSelectionTree(Func.resources(), this);
			tree.setPreferredSize(new Dimension(500, 500));
			tree_pane.setViewportView(tree);
			Util.runUi(()->{
				panel.updateUI();
			});	
		});
		return this;
	}
}
