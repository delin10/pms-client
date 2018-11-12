package pms.client.ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import pms.client.ui.intef.SwitchPanel;
import util.ui.swing.comm.Util;

public abstract class AbstractMainPanel extends SwitchPanel{
	private static final long serialVersionUID = 5512081542720124924L;
	protected final int PANEL_W = 600;
	protected final int PANEL_H = 700;
	protected JLabel title = new JLabel(getTitle());
	protected JTable table = new JTable();
	private Box root = Box.createVerticalBox();
	private Box top_title_box = Box.createHorizontalBox();
	private ToolBox tool_box = new ToolBox();
	private JScrollPane scroll = new JScrollPane();

	/**
	 * initialize the layout,size... of basic component
	 * 
	 * @return BasicMainPanel chain call
	 */
	public AbstractMainPanel init() {
		this.setSize(new Dimension(PANEL_W, PANEL_H));
		title.setFont(new Font("宋体", Font.BOLD, 20));
		table.setDragEnabled(false);
		scroll.setViewportView(table);
		scroll.setPreferredSize(new Dimension(550, 500));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		top_title_box.add(Box.createHorizontalStrut(40));
		top_title_box.add(title);

		root.add(top_title_box);
		root.add(scroll);
		tool_box.init();
		tool_box.setParent(root);
		this.add(root);
		return this;
	}
	
	public AbstractMainPanel update() {
		table.updateUI();
		scroll.updateUI();
		return this;
	}
	

	/**
	 * this is designed to switch Panel During this time , it removes all original
	 * component and respaint new panel
	 * 
	 * @param container
	 *            it is parent container
	 * @param run
	 *            it works after ui had been paint
	 */
	@Override
	public SwitchPanel switchIn(JComponent container, Runnable run) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Util.runUi(() -> {
			container.removeAll();
			container.add(this);
			this.updateUI();
			container.updateUI();
			if (run != null) {
				run.run();
			}
		});
		return this;
	}
	
	protected abstract AbstractMainPanel initTable();
	/**
	 * @return title is the title of table
	 */
	protected abstract String getTitle();

	protected abstract TableModel getJSONTableModel();
}
