package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class BuildingTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = 1L;
	{
		this.setColumns(new KV[] { new KV().setField("building_id").setTitle("建筑编号"),
				new KV().setField("community_name").setTitle("所在小区名字"),
				new KV().setField("building_type").setTitle("建筑类型"),
				new KV().setField("floor_area").setTitle("建筑面积"), new KV().setField("floor_num").setTitle("层数"),
				new KV().setField("direction").setTitle("建筑朝向"), new KV().setField("height").setTitle("楼高"),
				new KV().setField("crttime").setTitle("楼建筑时间"),
				new KV().setField("description").setTitle("楼描述") });
	}
}
