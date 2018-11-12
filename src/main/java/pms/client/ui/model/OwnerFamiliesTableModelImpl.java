package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class OwnerFamiliesTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = 7480065439429355093L;
	{
		this.setColumns(new KV[] { new KV().setField("building_id").setTitle("楼宇编号"),
				new KV().setField("community_name").setTitle("社区名称"),
				new KV().setField("room_id").setTitle("房间编号"),
				new KV().setField("owner_id").setTitle("业主身份证号码"),
				new KV().setField("floor_id").setTitle("楼层编号"),
				new KV().setField("member_id").setTitle("身份证号码"), new KV().setField("sex").setTitle("业主性别"),
				new KV().setField("relation").setTitle("与业主关系"), new KV().setField("hukou").setTitle("户口所在地"),
				new KV().setField("work_study_place").setTitle("工作学习单位"),
				new KV().setField("tel").setTitle("联系电话") });
	}
}
