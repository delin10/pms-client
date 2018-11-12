package util.ui.swing.model.tree.node;

public class CheckedTreeNode extends IdentifiedTreeNode {
	private static final long serialVersionUID = 3917506862508880712L;
	protected boolean selected;

	public CheckedTreeNode(boolean selected) {
		this(null);
		this.selected=selected;
	}

	public CheckedTreeNode(Object userObject) {
		this(userObject, true, false);
	}
	
	/**
	 * @param userObject
	 *            需要显示的对象，将调用toString方法转换成字符串
	 * @param haveChild
	 *            是否级联
	 * @param isSelected
	 *            设置该节点是否被选中
	 */
	public CheckedTreeNode(Object userObject,boolean isSelected) {
		super(userObject,true);
		this.selected = isSelected;
	}

	/**
	 * @param userObject
	 *            需要显示的对象，将调用toString方法转换成字符串
	 * @param haveChild
	 *            是否级联
	 * @param isSelected
	 *            设置该节点是否被选中
	 */
	public CheckedTreeNode(Object userObject, boolean haveChild, boolean isSelected) {
		super(userObject, haveChild);
		this.selected = isSelected;
	}
	
	public static CheckedTreeNode create(boolean selected) {
		return new CheckedTreeNode(selected);
	}

	public boolean isSelected() {
		return selected;
	}

	/**
	 * 级联选择
	 * 若子节点被选中，根节点到该节点路径上的点一定会被选中，若父节点取消选择，则父节点所有子节点被递归取消选择
	 */
	public void cascade_select_one() {
		if (selected) {
			CheckedTreeNode node = (CheckedTreeNode) this.getParent();
			while (node !=null && !node.isSelected()) {
				node.setSelected(true);
				node = (CheckedTreeNode) node.getParent();
			}
		} else {
			CheckedTreeNode node = this;
			int size = node.getChildCount();
			CheckedTreeNode child;
			for (int i = 0; i < size; ++i) {
				child = (CheckedTreeNode) this.getChildAt(i);
				child.setSelected(false);
			}
		}
	}

	public void setSelected(boolean _isSelected) {
		this.selected = _isSelected;
		cascade_select_one();
	}
}
