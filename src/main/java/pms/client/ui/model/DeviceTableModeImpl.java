package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class DeviceTableModeImpl extends JSONTableModel {
	private static final long serialVersionUID = -6806084955384846932L;
	{
		this.setColumns(new KV[] { new KV().setField("device_id").setTitle("设备编号"),
				new KV().setField("name").setTitle("设备名称"), new KV().setField("version").setTitle("设备型号"),
				new KV().setField("floor_id").setTitle("楼层编号"),
				new KV().setField("building_id").setTitle("楼宇编号"),
				new KV().setField("community_name").setTitle("社区名称"), new KV().setField("price").setTitle("价格"),
				new KV().setField("install_time").setTitle("安装时间"),
				new KV().setField("bad_time").setTitle("设备寿命"),
				new KV().setField("install_man").setTitle("安装人员"),
				new KV().setField("status").setTitle("设备状态") });
	}
}
