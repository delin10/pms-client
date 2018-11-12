package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class CommunitiesTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = 1L;
	{
		this.init();
		this.setColumns(new KV[] { new KV().setField("name").setTitle("小区名字"),
				new KV().setField("address").setTitle("小区地址"), new KV().setField("floor_area").setTitle("占地面积"),
				new KV().setField("total_area").setTitle("总建筑面积"),
				new KV().setField("green_area").setTitle("绿化率"),
				new KV().setField("crttime").setTitle("小区创建时间"),
				new KV().setField("description").setTitle("小区描述") });
	}
}
