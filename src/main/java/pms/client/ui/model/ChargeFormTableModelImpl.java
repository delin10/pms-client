package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class ChargeFormTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = 4961324536018773872L;
	{
		this.setColumns(new KV[] { new KV().setField("form_id").setTitle("账单编号"),
				new KV().setField("building_id").setTitle("建筑编号"), new KV().setField("community_name").setTitle("小区编号"),
				new KV().setField("room_id").setTitle("所在房间号码"), new KV().setField("floor_id").setTitle("楼层编号"),
				new KV().setField("owner_id").setTitle("账单拥有者编号"), new KV().setField("pay_way").setTitle("结算方式"),
				new KV().setField("item_id").setTitle("收费事件编号"), new KV().setField("item_num").setTitle("收费事件数量"),
				new KV().setField("start_time").setTitle("计费开始时间"), new KV().setField("end_time").setTitle("计费结束时间"),
				new KV().setField("receivable").setTitle("应收金额"), new KV().setField("real_receipt").setTitle("实收金额"),
				new KV().setField("balance").setTitle("余额"), new KV().setField("form_creater").setTitle("制单人"),
				new KV().setField("form_crttime").setTitle("制单时间") });
	}
}
