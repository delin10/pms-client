package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class RoleTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = -9123625827516648902L;
	{
		this.setColumns(new KV[] { new KV().setField("id").setTitle("角色标识符"),
				new KV().setField("name").setTitle("角色名字"), new KV().setField("description").setTitle("角色描述"),
				new KV().setTitle("是否可用").setField("available") });
		this.setFormatter((field, value) -> {
			if (field.equals("available")) {
				return value.equals("0") ? "不可用" : "可用";
			}
			return value;
		});
	}
}
