package pms.client.ui.model.form;

import java.util.Map;

import javax.swing.JTable;

import pms.client.data.RuntimeStore;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.CommunityRequestImpl;
import pms.client.handler.request.impl.ContractRequestImpl;
import pms.client.handler.request.impl.DepartmentRequestImpl;
import pms.client.ui.model.CommunitiesTableModelImpl;
import pms.client.ui.model.ContractTableModelImpl;
import pms.client.ui.model.DepartmentTableModelImpl;
import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.table.TableSelector;

public class EmployeeAddFormModelImpl extends AbstractCommonFormModel {

	public EmployeeAddFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		this.setType("did", FieldType.TABLE_SELECTOR);
		this.setType("contract_id", FieldType.TABLE_SELECTOR);
		TableSelector selector_contracts= UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new ContractTableModelImpl());
			RuntimeStore.runUI().refresh(new ContractRequestImpl().query(), t.getModel());;
			return null;
		});
		TableSelector selector_departments = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new DepartmentTableModelImpl());
			RuntimeStore.runUI().refresh(new DepartmentRequestImpl().query(), t.getModel());;
			return null;
		});
		selector_contracts.comfirm(args -> {
			UIHandler.handle_table_selector("contract_id", "contract_id", args);
			return null;
		}, "0");
		selector_departments.comfirm(args -> {
			UIHandler.handle_table_selector("did", "did", args);
			return null;
		}, "0");

		this.setValue("contract_id", selector_contracts);
		this.setValue("did", selector_departments);
	}

}
