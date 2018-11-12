package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class ChargeItemTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = -7775196243119746900L;
	{
		this.setColumns(new KV[] { new KV().setField("item_id").setTitle("收费事件编号"),
				new KV().setField("item_type").setTitle("收费类型"), new KV().setField("descript").setTitle("事件描述"),
				new KV().setField("fee").setTitle("费用") });
	}
}
