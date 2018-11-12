package pms.client.ui.impl;

import javax.swing.table.TableModel;

import pms.client.data.RuntimeStore;
import pms.client.handler.request.impl.ADQUPatternRequestImpl;
import pms.client.handler.request.impl.CarRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.CarTableModelImpl;
import pms.client.ui.model.form.CarAddFormModelImpl;
import pms.client.ui.model.form.CarEditFormModelImpl;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;

public class CarPanelImpl extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static CarRequestImpl req=new CarRequestImpl();

	public SwitchPanel refresh() {
		// TODO Auto-generated method stub
		RuntimeStore.runUI().refresh(req.query(), table.getModel());
		update();
		return this;
	}


	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "业主汽车列表";
	}


	@Override
	protected ADQUPatternRequestImpl getRequest() {
		// TODO Auto-generated method stub
		return req;
	}


	@Override
	protected String getAddFormTitle() {
		// TODO Auto-generated method stub
		return "新增业主汽车信息";
	}


	@Override
	protected String getEditFormTitle() {
		// TODO Auto-generated method stub
		return "编辑业主汽车信息";
	}


	@Override
	protected FormModel getAddFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new CarAddFormModelImpl(model.columns());
	}


	@Override
	protected FormModel getEditFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new CarEditFormModelImpl(model.columns());
	}


	@Override
	protected String[] getKeys() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected TableModel getJSONTableModel() {
		// TODO Auto-generated method stub
		return new CarTableModelImpl();
	}

}
