package pms.client.ui.errorRest;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import pms.client.data.RuntimeStore;
import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.CommunityRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import util.ui.swing.gen.CompGenerator;
import util.ui.swing.model.table.JSONTableModel;

public class ComunityPanelImpl extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static CommunityRequestImpl req=new CommunityRequestImpl();

	public BasicMainPanel initTable() {
		Func.exec(() -> {
			UIHandler.handle_communities_table(Func.communities(), table);
			JSONTableModel model = (JSONTableModel) table.getModel();
			Map<String, Object> init_values=new HashMap<>();
			ActionListener[] listeners = new ActionListener[] { e -> {
				int selected = table.getSelectedRow();
				if (selected != -1) {
					//编写
					UIHandler.showComplexEditForm(table, "编辑社区", "community?action=update", args -> {
						String json = (String) args[1];
						System.out.println(json);
						model.setValueAt(json, table.getSelectedRow());
						update();
						return null;
					}, new String[] { }, null, new String[] {"name"},model.columns());
				}
			}, e -> {

			}, e -> {
				UIHandler.showComplexForm("新增社区", "community?action=add", (args) -> {
					HttpResponse response = (HttpResponse) args[0];
					UIHandler.add_success_callback(response, args1 -> {
						String json = (String) args[1];
						System.out.println(json);
						model.getData().add(json);
						update();
						return null;
					});
					return null;
				}, new String[] { }, init_values, ((JSONTableModel) table.getModel()).columns());
			} };
			table.setComponentPopupMenu(CompGenerator.genPopupMenu(null, null, listeners, "编辑", "删除", "新增"));
		});
		return this;
	}

	@Override
	public SwitchPanel refresh() {
		// TODO Auto-generated method stub
		RuntimeStore.runUI().refresh(req.query(), table.getModel());;
		return this;
	}


	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "楼房列表";
	}

}
