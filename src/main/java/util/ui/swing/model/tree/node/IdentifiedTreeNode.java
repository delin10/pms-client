package util.ui.swing.model.tree.node;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

public class IdentifiedTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 7141527281513423567L;
	private String id;
	private HashMap<Object, Object> attrs;
	private IdentifiedTreeNode() {}
	public IdentifiedTreeNode(Object userObjcet,boolean haveChild) {
		super(userObjcet,haveChild);
	}
	
	public static IdentifiedTreeNode create() {
		return new IdentifiedTreeNode();
	}

	public IdentifiedTreeNode init(String id) {
		this.id=id;
		attrs=new HashMap<>();
		return this;
	}
	
	public IdentifiedTreeNode put(Object name,Object value) {
		attrs.put(name, value);
		return this;
	}
	
	public IdentifiedTreeNode setId(String id) {
		this.id=id;
		return this;
	}
	
	public Object get(Object name) {
		return attrs.get(name);
	}
	
	public String getId() {
		return this.id;
	}
}
