package util.ui.swing.model.tree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import util.ui.swing.model.tree.listener.CheckedTreeNodeSelectionListener;
import util.ui.swing.model.tree.node.CheckedTreeNode;
import util.ui.swing.model.tree.node.IdentifiedTreeNode;
import util.ui.swing.model.tree.render.CheckedTreeRenderer;

public class TreeNodeGenerator {
	/**
	 * 该方法采用递归方式生成树节点
	 * 
	 * @param root_name
	 *            总根的名称
	 * @param conf
	 *            携带关于json的参数，其中id_field代表id在json中的域，以此类推，fid_field,其中root_fid代表跟结点的id，也就是最上层的结点的fid；data代表json数据；attrs
	 *            String[]类型，代表结点需要携带的参数；label 表示呈现出来的文字域
	 * @return
	 */
	public static DefaultMutableTreeNode generator(String root_name, Map<String, Object> conf) {
		String json = conf.get("json").toString();
		String fid_field = conf.get("fid_field").toString();
		String id_field = conf.get("id_field").toString();
		String root_fid = conf.get("root_fid").toString();
		String name_field = conf.get("name_field").toString();
		String[] attrs = (String[]) conf.get("attrs");
		JSONArray array = JSONArray.parseArray(json);
		//System.out.println(json);
		LinkedList<JSONObject> swap = array.stream().map(Object::toString).map(JSONObject::parseObject)
				.collect(Collectors.toCollection(LinkedList<JSONObject>::new));
		//System.out.println(swap.size());
		IdentifiedTreeNode root = IdentifiedTreeNode.create().init(root_name).setId(root_fid);
		root.setUserObject("功能菜单");
		recursiveGenerate(root, swap, id_field, fid_field, name_field, attrs);
		return root;
	}

	private static void recursiveGenerate(IdentifiedTreeNode root, LinkedList<JSONObject> swap, String id_field,
			String fid_field, String name_field, String[] attrs) {
		if (swap.size() == 0) {
			return;
		}

		for (int i = 0; i < swap.size();) {
			JSONObject value = (JSONObject) swap.get(i);
			String fid = value.getString(fid_field);
			String id = value.getString(id_field);
			String name = value.getString(name_field);
			if (fid.equals(root.getId())) {
				IdentifiedTreeNode node = IdentifiedTreeNode.create().init(id);
				node.setUserObject(name);
				if (attrs != null) {
					Arrays.stream(attrs).forEach(attr -> {
						node.put(attr, value.getString(attr));
					});
				}
				root.add(node);
				recursiveGenerate(node, swap, id_field, fid_field, name_field, attrs);
				i=swap.indexOf(value);
				swap.remove(i);
			} else {
				++i;
			}
		}
		//System.out.println(root.getUserObject()+":"+root.getChildCount());
	}

	/**
	 * 该方法采用递归方式生成树节点
	 * 
	 * @param root_name
	 *            总根的名称
	 * @param conf
	 *            携带关于json的参数，其中id_field代表id在json中的域，以此类推，fid_field,其中root_fid代表跟结点的id，也就是最上层的结点的fid；data代表json数据；attrs
	 *            String[]类型，代表结点需要携带的参数；label 表示呈现出来的文字域
	 * @return
	 */
	private static DefaultMutableTreeNode generator_Checked(String root_name, Map<String, Object> conf) {
		String json = conf.get("json").toString();
		String fid_field = conf.get("fid_field").toString();
		String id_field = conf.get("id_field").toString();
		String root_fid = conf.get("root_fid").toString();
		String name_field = conf.get("name_field").toString();
		String[] attrs = (String[]) conf.get("attrs");
		JSONArray array = JSONArray.parseArray(json);
		LinkedList<JSONObject> swap = array.stream().map(Object::toString).map(JSONObject::parseObject)
				.collect(Collectors.toCollection(LinkedList<JSONObject>::new));
		// ClassCastExec
		// IdentifiedTreeNode root =
		// CheckedTreeNode.create(false).init(root_name).setId(root_fid);
		IdentifiedTreeNode root = CheckedTreeNode.create(false).init(root_name).setId(root_fid);
		root.setUserObject("功能菜单");
		recursiveGenerate_Checked(root, swap, id_field, fid_field, name_field, attrs);
		return root;
	}

	public static JTree genCheckedTree(String root_name, Map<String, Object> conf) {
		DefaultTreeModel model = new DefaultTreeModel(generator_Checked(root_name, conf));
		JTree tree = new JTree();
		tree.setModel(model);
		tree.setRootVisible(false);
		tree.addMouseListener(new CheckedTreeNodeSelectionListener());
		tree.setModel(model);
		tree.setCellRenderer(new CheckedTreeRenderer());
		return tree;
	}

	private static void recursiveGenerate_Checked(IdentifiedTreeNode root, LinkedList<JSONObject> swap, String id_field,
			String fid_field, String name_field, String[] attrs) {
		if (swap.size() == 0) {
			return;
		}

		for (int i = 0; i < swap.size();) {
			JSONObject value = (JSONObject) swap.get(i);
			String fid = value.getString(fid_field);
			String id = value.getString(id_field);
			String name = value.getString(name_field);
			if (fid.equals(root.getId())) {
				swap.remove(i);
				IdentifiedTreeNode node = CheckedTreeNode.create(false).init(name).setId(id);
				// System.out.println(node instanceof CheckedTreeNode);
				node.setUserObject(name);
				if (attrs != null) {
					Arrays.stream(attrs).forEach(attr -> {
						node.put(attr, value.getString(attr));
					});
				}
				root.add(node);
				recursiveGenerate_Checked(node, swap, id_field, fid_field, name_field, attrs);
			} else {
				++i;
			}
		}
	}

	public static LinkedList<CheckedTreeNode> getSelecteds(JTree tree) {
		LinkedList<CheckedTreeNode> selecteds = new LinkedList<>();
		LinkedList<CheckedTreeNode> queue = new LinkedList<>();
		CheckedTreeNode root = (CheckedTreeNode) tree.getModel().getRoot();
		queue.addFirst(root);
		while (queue.size() != 0) {
			CheckedTreeNode node = queue.removeFirst();
			int size = node.getChildCount();
			for (int i = 0; i <size; ++i) {
				//System.out.println("size:"+size+";"+"index:"+i);
				CheckedTreeNode child = (CheckedTreeNode) node.getChildAt(i);
				if (child.isSelected()) {
					selecteds.addFirst(child);
				}
				if (child.getChildCount() > 0) {
					queue.addFirst(child);
				}
			}
		}
		return selecteds;
	}
}
