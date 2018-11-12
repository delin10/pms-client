package pms.client.ui.impl;

import java.util.Map;

import javax.swing.table.TableModel;

import pms.client.data.RuntimeStore;
import pms.client.handler.request.impl.ADQUPatternRequestImpl;
import pms.client.handler.request.impl.DataBaseRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.BackupTableModelImpl;
import util.comm.array.CollectionUtil;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;

public class DataBaseManagePanelImpl extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static DataBaseRequestImpl req = new DataBaseRequestImpl();
	private static String[] keys = { "path" };

	@Override
	public SwitchPanel refresh() {
		RuntimeStore.runUI().refresh(req.query(), table.getModel());
		update();
		return this;
	}

	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "数据库备份列表";
	}

	@Override
	protected TableModel getJSONTableModel() {
		// TODO Auto-generated method stub
		return new BackupTableModelImpl();
	}

	@Override
	protected ADQUPatternRequestImpl getRequest() {
		// TODO Auto-generated method stub
		return req;
	}

	@Override
	public void add(JSONTableModel model) {
		RuntimeStore.runUI().handleBackupResponse(req.add(null));
		refresh();
	}

	@Override
	public void update(JSONTableModel model) {
		// TODO Auto-generated method stub
		int selected = table.getSelectedRow();
		if (selected != -1) {
			Map<String, Object> path=CollectionUtil.putIn(new String[] {"path"},model.getValuesAt(selected));
			RuntimeStore.runUI().handleRestoreResponse(req.update(path));
		}
	}

	@Override
	protected String getPopupAddItemTitle() {
		// TODO Auto-generated method stub
		return "备份数据库";
	}

	@Override
	protected String getPopupEditItemTitle() {
		// TODO Auto-generated method stub
		return "还原数据库";
	}

	@Override
	protected String getPopupDeleteItemTitle() {
		// TODO Auto-generated method stub
		return "删除备份";
	}

	@Override
	protected String[] getKeys() {
		// TODO Auto-generated method stub
		return keys;
	}

	@Deprecated
	@Override
	protected String getAddFormTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	@Override
	protected String getEditFormTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	@Override
	protected FormModel getAddFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	@Override
	protected FormModel getEditFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return null;
	}

}