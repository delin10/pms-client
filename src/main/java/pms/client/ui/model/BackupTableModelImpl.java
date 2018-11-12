package pms.client.ui.model;

import util.ui.swing.bean.KV;
import util.ui.swing.model.table.JSONTableModel;

public class BackupTableModelImpl extends JSONTableModel {
	private static final long serialVersionUID = -5075046659841270440L;
	{
		this.setColumns(new KV[] { new KV().setField("name").setTitle("备份文件名称"),
				new KV().setField("path").setTitle("备份文件路径"),
				new KV().setField("last_modified").setTitle("备份创建时间") });
	}
}
