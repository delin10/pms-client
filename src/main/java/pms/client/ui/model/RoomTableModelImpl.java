package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class RoomTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = 1L;

	{
		this.setColumns(new KV[] { new KV().setField("building_id").setTitle("楼宇编号"),
				new KV().setField("community_name").setTitle("社区名称"),
				new KV().setField("room_id").setTitle("房间编号"), new KV().setField("floor_id").setTitle("楼层编号"),
				new KV().setField("room_layout").setTitle("房型布置"),
				new KV().setField("room_area").setTitle("房间面积"),
				new KV().setField("room_type").setTitle("房间类型"), new KV().setField("room_use").setTitle("房间用途"),
				new KV().setField("is_vacancy").setTitle("是否空置"),
				new KV().setField("decorated").setTitle("是否装修") });
	}
}
