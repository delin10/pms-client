package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class UserTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = 4424597948267987698L;
	{
		this.setColumns(new KV[] { new KV().setField("userid").setTitle("用户标识符"),
				new KV().setField("roleid").setTitle("角色标识符"), new KV().setField("role_name").setTitle("角色名"),
				new KV().setTitle("角色描述").setField("role_description") });
	}
}
