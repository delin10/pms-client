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

public class RoomAddFormModelImpl extends AbstractCommonFormModel {

	public RoomAddFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		this.setType("is_vacancy", FieldType.COMBOBOX);
		this.setType("decorated", FieldType.COMBOBOX);
		this.setType("building_id", FieldType.TABLE_SELECTOR);
		this.disable(new String[] {"community_name"});
		KV decorated_first = new KV().setTitle("是").setValue("1").setField("decorated_1");
		KV is_vacancy_first = new KV().setTitle("是").setValue("1").setField("is_vacancy_1");
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

		this.setValue("decorated",
				new KV[] { decorated_first, new KV().setTitle("否").setValue("0").setField("decorated_0") });
		this.setValue("is_vacancy",
				new KV[] { is_vacancy_first, new KV().setTitle("否").setValue("0").setField("is_vacancy_0") });
		this.setValue("community_name", "这个域在选择楼宇编号中自动填写");
		this.setValue("building_id", selector_building);
	}

}
