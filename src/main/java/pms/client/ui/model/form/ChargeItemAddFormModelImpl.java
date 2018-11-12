package pms.client.ui.model.form;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;

import pms.client.data.RuntimeStore;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.BuildingRequestImpl;
import pms.client.ui.model.BuildingTableModelImpl;
import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.table.TableSelector;

public class ChargeItemAddFormModelImpl extends AbstractCommonFormModel {
	public ChargeItemAddFormModelImpl(KV[] kvs) {
		super(kvs);
	}

	@Override
	public void init(Map<String, Object> values) {
		this.disable(new String[] {"community_name"});
		this.setType("building_id", FieldType.TABLE_SELECTOR);

		TableSelector selector_building = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new BuildingTableModelImpl());
			RuntimeStore.runUI().refresh(new BuildingRequestImpl().query(), t.getModel());;
			return null;
		});
		
		selector_building.comfirm(args -> {
			Map<String, String> mapper=new HashMap<>();
			mapper.put("community_name", "community_name");
			mapper.put("building_id", "building_id");
			RuntimeStore.runUI().handle_table_selector(mapper, args);
			return null;
		}, "0");

		this.setValue("building_id", selector_building);
	}
}
