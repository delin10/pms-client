package pms.client.ui.model.form;

import java.util.Map;

import javax.swing.JTable;

import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import util.ui.swing.bean.KV;
import util.ui.swing.model.table.TableSelector;

public class DepartmentEditFormModelImpl extends AbstractCommonFormModel {

	public DepartmentEditFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		this.disable(new String[] { "did" });
		setValue(values);
		TableSelector selector_contracts = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			UIHandler.handle_communities_table(Func.communities(), t);
			return null;
		});
		selector_contracts.comfirm(args -> {
			UIHandler.handle_table_selector("community_name", "community_name", args);
			return null;
		}, "0");
	}

}
