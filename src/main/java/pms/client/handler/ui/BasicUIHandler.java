package pms.client.handler.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import pms.client.bean.User;
import pms.client.data.system.BasicSystem;
import pms.client.funcs.Func;
import pms.client.handler.request.impl.LoginRequestImpl;
import pms.client.ui.IndexFrame;
import pms.client.ui.LoginFrame;
import util.comm.array.CollectionUtil;
import util.comm.file.FileUtil;
import util.comm.lambda.exception.Handler;
import util.comm.lambda.exception.SimpleExec;
import util.comm.lambda.functon.SimpleTask;
import util.ui.swing.comm.Util;
import util.ui.swing.custom_comp.ImageViewer;
import util.ui.swing.model.form.ComplexForm2;
import util.ui.swing.model.form.Form;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;
import util.ui.swing.model.tree.TreeNodeGenerator;

public class BasicUIHandler {
	private static JFrame root = null;
	private static final String CHARSET = "utf-8";
	private static JMenuBar bar = new JMenuBar();
	private static final String USER_KEY = "_USER_";
	private static LoginRequestImpl req=new LoginRequestImpl();
	static {
		initBar();
	}

	/**
	 * initialize Frame JMenuBar , normally only one time
	 */
	public static void initBar() {
		JMenu login_menu = new JMenu("登录选项");
		JMenu sys_menu = new JMenu("系统设置");
		JMenu about_menu = new JMenu("关于");
		JMenu login_id_item = new JMenu("未登录");
		JMenuItem logout_item = new JMenuItem("注销");
		Util.disable(login_id_item, logout_item);
		logout_item.addActionListener(e -> {
			HttpResponse response = LoginRequestImpl.logout();
			if (response.getStatusLine().getStatusCode() != 200) {
				JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
			}
			//RuntimeStore.put(USER_KEY, null);
			login_id_item.removeAll();
			transToLogin();
		});
		Util.addBatch(login_menu, login_id_item, logout_item);
		Util.addBatch(bar, login_menu, sys_menu, about_menu);
	}

	/**
	 * skip to loginFrame disable relative menu and clear some record
	 */
	public static void transToLogin() {
		root.dispose();
		root = new LoginFrame().init();
		root.setJMenuBar(bar);
		root.setVisible(true);
		bar.getMenu(0).getItem(1).setEnabled(false);
	}
	
	public void handle_login(String id, String pwd, JFrame component) {
		HttpResponse response = req.login_cookie(id,BasicSystem.cookieOf(id));
		if (response != null) {
			Arrays.stream(response.getAllHeaders()).map(header -> header.getName() + "=" + header.getValue())
					.forEach(System.out::println);
		}
		System.out.println(response);
		if (response != null && response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(component, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				JSONObject json = response != null
						? JSONObject.parseObject(FileUtil.readText(response.getEntity().getContent(), CHARSET, false))
						: null;
				System.out.println(json);
				if (json == null || json.getString("suc").equals("-1")) {
					response = req.login_id_pwd(id, pwd);
					if (response == null || response.getStatusLine().getStatusCode() != 200) {
						JOptionPane.showMessageDialog(component, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						json = JSONObject
								.parseObject(FileUtil.readText(response.getEntity().getContent(), CHARSET, false));
						if (json.getString("suc").equals("-1")) {
							JOptionPane.showMessageDialog(component, json.getString("msg"), "错误信息",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}

				// 系统处理部分
				BasicSystem.updateCookie(id,req.cookies());
				JMenu login_id_item = (JMenu) bar.getMenu(0).getMenuComponent(0);
				JMenuItem logout_item = bar.getMenu(0).getItem(1);
				login_id_item.setText("当前账号:" + id);
				logout_item.setEnabled(true);
				login_id_item.setEnabled(true);
				JSONObject data = json.getJSONObject("data");
				String role = data.getString("role");
				login_id_item.add(new JMenuItem("角色:" + role));
				if (!"系统管理员".equals(role)) {
					login_id_item.add(new JMenuItem("居住小区:" + data.getString("community")));
					login_id_item.add(new JMenuItem("居住楼号:" + data.getString("building")));
					login_id_item.add(new JMenuItem("居住楼层:" + data.getString("floor")));
					login_id_item.add(new JMenuItem("居住房号:" + data.getString("room")));

				}
				data.put("id", id);
				User user = data.toJavaObject(User.class);
				Func.setUser(user);

				JOptionPane.showMessageDialog(component, "登录成功", "信息", JOptionPane.INFORMATION_MESSAGE);
				component.dispose();
				setRoot(new IndexFrame().init());
				root.setVisible(true);
				System.out.println(root);
			} catch (UnsupportedOperationException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void generateTree(HttpResponse response, JTree tree, JFrame component) {
		JSONObject jo=handle_response(response);
		if (jo!=null){
			tree.setModel((TreeModel) SimpleExec.exec(data -> {
				Map<String, Object> conf = new HashMap<>();
				conf.put("json", jo.getString("data"));
				conf.put("fid_field", "fid");
				conf.put("id_field", "id");
				conf.put("root_fid", "");
				conf.put("name_field", "name");
				return new DefaultTreeModel(TreeNodeGenerator.generator("功能菜单", conf));
			}, Handler.PRINTTRACE));
			Util.runUi(() -> {
				component.repaint();
				;
			});
		}
	}
	
	public void handle_company_image(HttpResponse response, ImageViewer label) {
		try {
			int content_length = Integer.parseInt(response.getHeaders("Content-Length")[0].getValue());
			Header[] headers = response.getHeaders("Content-Type");
			if (headers.length != 0 && headers[0].equals("application/json;charset=utf-8")) {

			} else {
				label.setImage(content_length, response.getEntity().getContent());
			}
			// Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fill_form(HttpResponse response, String[] field, JTextField... ins) {
		if (response == null || response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), CHARSET, false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				for (int i = 0; i < ins.length; ++i) {
					ins[i].setText(response_json.getString(field[i]));
				}
				Util.runUi(() -> {
					Util.updateUI(ins);
				});
				return null;
			}, Handler.PRINTTRACE);
		}
	}


	/**
	 * handle the response
	 * 
	 * @param response
	 * @return
	 */
	public JSONObject handle_response(HttpResponse response) {
		if (response == null || response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
			return null;
		} else {
			return (JSONObject) SimpleExec.exec(data -> {
				JSONObject json = JSONObject
						.parseObject(FileUtil.readText(response.getEntity().getContent(), CHARSET, false));
				System.out.println(json);
				return json;
			}, Handler.PRINTTRACE);
		}
	}

	/**
	 * tansform response to table model
	 * 
	 * @param response
	 * @param table
	 * @param change_model
	 */
	public void handle_table(HttpResponse response, JTable table, boolean change_model) {
		JSONTableModel model = change_model ? JSONTableModel.create().init() : (JSONTableModel) table.getModel();
		refresh(response, model);
		table.setModel(model);
		Util.runUi(() -> {
			table.updateUI();
		});
	}

	/**
	 * @param model
	 * @param selects
	 * @param keys
	 * @return
	 */
	public ArrayList<Map<String, Object>> select_keys_values(JSONTableModel model, int[] selects, String[] keys) {
		if (selects.length != 0) {
			ArrayList<Map<String, Object>> select_rows = new ArrayList<>();
			Arrays.stream(selects).forEach(i -> {
				Map<String, Object> row = model.getValuesAt(i);
				select_rows.add(CollectionUtil.putIn(keys, row));
			});
			return select_rows;
		}
		return null;
	}

	/**
	 * @param model
	 * @param selects
	 */
	public void handle_table_delete(JSONTableModel model, int[] selects) {
		Arrays.stream(selects).forEach(model::remove);
	}

	public void handle_delete_reponse(HttpResponse response, JSONTableModel model, int[] selects) {
		JSONObject jo = handle_response(response);
		if (jo != null) {
			String suc = jo.getString("suc");
			String msg = jo.getString("msg");
			if ("0".equals(suc)) {
				Arrays.stream(selects).forEach(i -> {
					model.getData().remove(i);
				});
				JOptionPane.showMessageDialog(root, msg, "成功信息", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(root, msg, "错误信息", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void handleBackupResponse(HttpResponse response) {
		JSONObject jo=handle_response(response);
		if ("0".equals(jo.getString("suc"))) {
			JOptionPane.showMessageDialog(root, "备份成功", "数据库信息", JOptionPane.INFORMATION_MESSAGE);
		}else {
			JOptionPane.showMessageDialog(root, "备份失败", "数据库信息", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void handleRestoreResponse(HttpResponse response) {
		JSONObject jo=handle_response(response);
		if ("0".equals(jo.getString("suc"))) {
			JOptionPane.showMessageDialog(root, "还原成功", "数据库信息", JOptionPane.INFORMATION_MESSAGE);
		}else {
			JOptionPane.showMessageDialog(root, "还原失败", "数据库信息", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * @param callback
	 *            发送请求后的回调函数
	 * @param title
	 *            显示的表单的标题
	 * @param submit
	 *            提交按钮的事件
	 * @param model
	 *            表单的数据模型
	 */
	@SuppressWarnings("unchecked")
	public void showComplexe2Form(SimpleTask callback, String title, SimpleTask submit, FormModel model) {
		JDialog dialog = new JDialog();
		dialog.setTitle(title);
		Point root_loc = root.getLocation();
		dialog.setLocationRelativeTo(root);
		dialog.setSize(500, 600);
		dialog.setResizable(false);
		dialog.setLocation((int) root_loc.getX() + 250, (int) root_loc.getY() + 250);
		dialog.setModal(true);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
		Form form = new ComplexForm2().buildByModel(model);
		form.setPreferredSize(new Dimension(450, 500));
		form.onSubmit((e1, args1) -> {
			// 提交的事件
			Map<String, String> map = (Map<String, String>) args1[0];
			String json = JSON.toJSONString(map);
			System.out.println("BasicHandler form的键值对:"+map);
			// 提交回掉函数可用参数 args[0]为表格选择行的map
			HttpResponse response = (HttpResponse) submit.exec(map);
			// 请求完成回掉函数可用参数 args[0]response对象 args[1]为选择行的json字符串
			callback.exec(response, json);
		}).onCanel((e, args) -> {
			dialog.dispose();
		});
		// 自带pack效果
		dialog.getContentPane().add(form);
		dialog.setVisible(true);
	}
	
	@SuppressWarnings("unchecked")
	public void handle_table_selector(Map<String, String> mapper,Object... args) {
		int index = (int) args[0];
		JSONTableModel tm = (JSONTableModel) args[1];
		Map<String, Object> attrs = (Map<String, Object>) args[2];
		ComplexForm2 form = (ComplexForm2) attrs.get("_form_");
		mapper.entrySet().stream().forEach(entry->{
			String  src_field=entry.getKey();
			String target_field=entry.getValue();
			JTextField input = (JTextField) form.getField(target_field).input();
			System.out.println(target_field+"=>"+input);
			input.setText(tm.getValuesAt(index).get(src_field).toString());
		});
	}

	/**
	 * 刷新表格数据，但是不进行数据模型的替换
	 * 
	 * @param response
	 * @param model
	 */
	public void refresh(HttpResponse response, TableModel model_) {
		JSONTableModel model = (JSONTableModel) model_;
		JSONObject json = handle_response(response);
		if (json == null) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			if ("0".equals(json.getString("suc"))) {
				model.getData().parse(json.getString("data"));
			}
		}
	}

	public static JFrame getRoot() {
		return root;
	}

	public static void setRoot(JFrame root) {
		root.setJMenuBar(bar);
		BasicUIHandler.root = root;
	}
}
