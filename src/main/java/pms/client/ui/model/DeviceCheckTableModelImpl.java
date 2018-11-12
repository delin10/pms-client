package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class DeviceCheckTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = -5743675192091731686L;
	{
		this.setColumns(new KV[] { new KV().setField("device_id").setTitle("设备编号"),
				new KV().setField("check_frequency").setTitle("检修频次"), new KV().setField("descript").setTitle("检修内容"),
				new KV().setField("result").setTitle("效果"), new KV().setField("checker").setTitle("检修人员") });
	}
}
