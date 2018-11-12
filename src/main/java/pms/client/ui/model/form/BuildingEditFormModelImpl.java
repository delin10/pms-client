package pms.client.ui.model.form;

import java.util.Map;

import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;

public class BuildingEditFormModelImpl extends AbstractCommonFormModel {

	public BuildingEditFormModelImpl(KV[] kvs) {
		super(kvs);
	}

	@Override
	public void init(Map<String, Object> values) {
		this.disable(new String[] {"building_id","community_name"});
		this.setType("building_type", FieldType.COMBOBOX);
		values.entrySet().forEach(entry->{
			this.setValue(entry.getKey(), entry.getValue());
		});
		this.setValue("building_type", new KV[] {new KV().setTitle("商业房").setValue("商业房").setField("building_type"),new KV().setTitle("普通房").setValue("普通房").setField("building_type")});
	}

}
