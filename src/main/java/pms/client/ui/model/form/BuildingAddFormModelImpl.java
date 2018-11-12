package pms.client.ui.model.form;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;

import pms.client.data.RuntimeStore;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.CommunityRequestImpl;
import pms.client.ui.model.CommunitiesTableModelImpl;
import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.table.TableSelector;

public class BuildingAddFormModelImpl extends AbstractCommonFormModel{
	public BuildingAddFormModelImpl(KV[] kvs) {
		super(kvs);
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		Arrays.stream(fields()).forEach(System.out::printf);
		this.setType("community_name", FieldType.TABLE_SELECTOR);
		this.setType("building_type", FieldType.COMBOBOX);
		this.setValue("building_type", new KV[] {new KV().setTitle("商业房").setValue("商业房").setField("building_type"),new KV().setTitle("普通房").setValue("普通房").setField("building_type")});
		TableSelector selector = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new CommunitiesTableModelImpl());
			RuntimeStore.runUI().refresh(new CommunityRequestImpl().query(), t.getModel());;
			return null;
		});
		selector.comfirm(args -> {
			Map<String, String> mapper=new HashMap<>();
			mapper.put("name", "community_name");
			RuntimeStore.runUI().handle_table_selector(mapper, args);
			//UIHandler.handle_table_selector("community_name", "name", args);
			return null;
		}, "0");
		this.setValue("community_name", selector);
	}
	
}
