package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class PetTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = 1L;
	{
		this.setColumns(new KV[] { new KV().setField("building_id").setTitle("楼宇编号"),
				new KV().setField("community_name").setTitle("社区名称"),
				new KV().setField("room_id").setTitle("房间编号"),
				new KV().setField("owner_id").setTitle("业主身份证号码"), new KV().setField("variety").setTitle("品种"),
				new KV().setField("tall").setTitle("体高"), new KV().setField("pet_id").setTitle("证号") });
	}
}
