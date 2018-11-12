package util.ui.swing.model.tree.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import util.ui.swing.model.tree.node.CheckedTreeNode;

public class CheckedTreeNodeSelectionListener extends MouseAdapter {
	@Override
	public void mouseClicked(MouseEvent event)
	{
		JTree tree = (JTree)event.getSource();
		int x = event.getX();
		int y = event.getY();
		int row = tree.getRowForLocation(x, y);
		TreePath path = tree.getPathForRow(row);
		if(path != null)
		{
			CheckedTreeNode node = (CheckedTreeNode)path.getLastPathComponent();
			if(node != null)
			{
				boolean isSelected = !node.isSelected();
				node.setSelected(isSelected);
				((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);
			}
		}
	}
}
