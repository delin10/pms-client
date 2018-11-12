package pms.client.ui.impl;

import javax.swing.table.TableModel;

import pms.client.data.RuntimeStore;
import pms.client.handler.request.impl.ADQUPatternRequestImpl;
import pms.client.handler.request.impl.EmployeeRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.EomployeeTableModelImpl;
import pms.client.ui.model.form.EmployeeAddFormModelImpl;
import pms.client.ui.model.form.EmployeeEditFormModelImpl;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;

public class EmployeePanelImpl extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static EmployeeRequestImpl req = new EmployeeRequestImpl();
	private static String[] keys= {"eid"};

	@Override
	public SwitchPanel refresh() {
		RuntimeStore.runUI().refresh(req.query(), table.getModel());
		update();
		return this;
	}

	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "员工列表";
	}

	@Override
	protected ADQUPatternRequestImpl getRequest() {
		// TODO Auto-generated method stub
		return req;
	}

	@Override
	protected String getAddFormTitle() {
		// TODO Auto-generated method stub
		return "新增员工";
	}

	@Override
	protected String getEditFormTitle() {
		// TODO Auto-generated method stub
		return "编辑员工信息";
	}

	@Override
	protected FormModel getAddFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new EmployeeAddFormModelImpl(model.columns());
	}

	@Override
	protected FormModel getEditFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new EmployeeEditFormModelImpl(model.columns());
	}

	@Override
	protected String[] getKeys() {
		// TODO Auto-generated method stub
		return keys;
	}

	@Override
	protected TableModel getJSONTableModel() {
		// TODO Auto-generated method stub
		return new EomployeeTableModelImpl();
	}
}