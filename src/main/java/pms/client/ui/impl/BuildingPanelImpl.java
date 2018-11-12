package pms.client.ui.impl;

import javax.swing.table.TableModel;

import pms.client.data.RuntimeStore;
import pms.client.handler.request.impl.ADQUPatternRequestImpl;
import pms.client.handler.request.impl.BuildingRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.BuildingTableModelImpl;
import pms.client.ui.model.form.BuildingAddFormModelImpl;
import pms.client.ui.model.form.BuildingEditFormModelImpl;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;

public class BuildingPanelImpl extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static BuildingRequestImpl req = new BuildingRequestImpl();
	private static String[] keys= {"community_name","building_id"};

	@Override
	public SwitchPanel refresh() {
		RuntimeStore.runUI().refresh(req.query(), table.getModel());
		update();
		return this;
	}

	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "楼房列表";
	}

	@Override
	protected ADQUPatternRequestImpl getRequest() {
		// TODO Auto-generated method stub
		return req;
	}

	@Override
	protected String getAddFormTitle() {
		// TODO Auto-generated method stub
		return "新增楼房";
	}

	@Override
	protected String getEditFormTitle() {
		// TODO Auto-generated method stub
		return "编辑楼房信息";
	}

	@Override
	protected FormModel getAddFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new BuildingAddFormModelImpl(model.columns());
	}

	@Override
	protected FormModel getEditFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new BuildingEditFormModelImpl(model.columns());
	}

	@Override
	protected String[] getKeys() {
		// TODO Auto-generated method stub
		return keys;
	}

	@Override
	protected TableModel getJSONTableModel() {
		// TODO Auto-generated method stub
		return new BuildingTableModelImpl();
	}
}