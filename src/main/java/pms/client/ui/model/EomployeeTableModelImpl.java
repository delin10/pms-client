package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class EomployeeTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = -6744866260853998797L;
	{
		this.setColumns(new KV[] { new KV().setField("eid").setTitle("员工编号"),
				new KV().setField("name").setTitle("员工姓名"), new KV().setField("sex").setTitle("性别"),
				new KV().setField("birthday").setTitle("出生年月"), new KV().setField("tel").setTitle("员工电话"),
				new KV().setField("did").setTitle("部门编号"), new KV().setField("description").setTitle("个人描述"),
				new KV().setField("contract_id").setTitle("合同编号"),
				new KV().setField("entry_time").setTitle("入职时间") });
	}
}
