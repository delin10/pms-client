package pms.client.ui.impl;

import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import pms.client.data.RuntimeStore;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.RoleRequestImpl;
import pms.client.handler.request.impl.UserRequestImpl;
import pms.client.ui.AbstractMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.RoleTableModelImpl;
import pms.client.ui.model.UserTableModelImpl;
import util.ui.swing.gen.CompGenerator;
import util.ui.swing.model.table.JSONTableModel;

public class UserPanelImpl extends AbstractMainPanel {
	private static final long serialVersionUID = 3241477721407875745L;
	private static UserRequestImpl req=new UserRequestImpl();
	@Override
	protected TableModel getJSONTableModel() {
		// TODO Auto-generated method stub
		return new UserTableModelImpl();
	}
	@Override
	protected AbstractMainPanel initTable() {
		// TODO Auto-generated method stub
		RuntimeStore.exec(() -> {
			table.setModel(new UserTableModelImpl());
			refresh();
			JSONTableModel model = (JSONTableModel) table.getModel();

			ActionListener[] listeners = new ActionListener[] { e -> {
				int selected = table.getSelectedRow();
				if (selected != -1) {
					Map<String, Object> map = model.getValuesAt(selected);
					UIHandler.showTableSector(args -> {
						JTable t = (JTable) args[0];
						t.setModel(new RoleTableModelImpl());
						RuntimeStore.runUI().refresh(new RoleRequestImpl().query(), t.getModel());
						return null;
					}, args1 -> {
						JSONTableModel m = (JSONTableModel) args1[1];
						int i = (int) args1[0];
						String role_id = (String) m.getValuesAt(i).get("id");
						String user_id = (String) model.getValuesAt(selected).get("userid");
						System.out.println(model.getValuesAt(selected));
						System.out.println("role_id=" + role_id);
						System.out.println("user_id=" + user_id);
						req.authRole(user_id,role_id);
						return null;
					}, "description");
				}
			} };
			table.setComponentPopupMenu(CompGenerator.genPopupMenu(null, null, listeners, "修改角色"));
		});
		return this;
	}
	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SwitchPanel refresh() {
		// TODO Auto-generated method stub
		RuntimeStore.runUI().refresh(req.getUsers(), table.getModel());
		update();
		return this;
	}

}
