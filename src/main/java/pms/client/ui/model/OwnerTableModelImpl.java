package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class OwnerTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = 3695723070360423671L;
	{
		this.setColumns(new KV[] { new KV().setField("building_id").setTitle("楼宇编号"),
				new KV().setField("community_name").setTitle("社区名称"),
				new KV().setField("room_id").setTitle("房间编号"),
				new KV().setField("owner_id").setTitle("业主身份证号码"),
				new KV().setField("floor_id").setTitle("楼层编号"),
				new KV().setField("contract_id").setTitle("合同编号"), new KV().setField("name").setTitle("业主姓名"),
				new KV().setField("sex").setTitle("业主性别"), new KV().setField("age").setTitle("业主年龄"),
				new KV().setField("tel").setTitle("联系电话"), new KV().setField("hukou").setTitle("户口所在地"),
				new KV().setField("work_place").setTitle("工作单位"),
				new KV().setField("contract_address").setTitle("联系地址"),
				new KV().setField("postalcode").setTitle("邮政编码"),
				new KV().setField("check_in_time").setTitle("入住时间"),
				new KV().setField("pay_way").setTitle("付款方式"), new KV().setField("remark").setTitle("备注") });
	}
}
