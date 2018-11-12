package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class ContractTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = 2882459993086781979L;
	{
		this.setColumns(new KV[] { new KV().setField("contract_id").setTitle("合同编号"),
				new KV().setField("crttime").setTitle("建立时间"), new KV().setField("deadtime").setTitle("到期时间"),
				new KV().setField("valid").setTitle("有效性"),
				new KV().setField("creator").setTitle("创建者") });
	}
}	
