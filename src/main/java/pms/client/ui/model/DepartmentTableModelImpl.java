package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class DepartmentTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = -5273935777324622093L;

	{
		this.setColumns(new KV[] { new KV().setField("did").setTitle("部门编号"),
				new KV().setField("community_name").setTitle("社区名字"), new KV().setField("name").setTitle("部门名字"),
				new KV().setField("manager").setTitle("管理者") });
	}
}
