package pms.client.funcs;

import java.awt.Component;
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
import pms.client.ui.IndexFrame;
import pms.client.ui.LoginFrame;
import pms.client.ui.model.CommunitiesTableModelImpl;
import util.comm.array.CollectionUtil;
import util.comm.file.FileUtil;
import util.comm.lambda.DStream;
import util.comm.lambda.exception.Handler;
import util.comm.lambda.exception.SimpleExec;
import util.comm.lambda.functon.SimpleTask;
import util.ui.swing.bean.KV;
import util.ui.swing.comm.Util;
import util.ui.swing.custom_comp.ImageViewer;
import util.ui.swing.model.form.ComplexForm;
import util.ui.swing.model.form.ComplexForm2;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.form.Form;
import util.ui.swing.model.form.StandardForm;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;
import util.ui.swing.model.table.TableSelector;
import util.ui.swing.model.table.impl.StandardTableSelector;
import util.ui.swing.model.tree.TreeNodeGenerator;

public class UIHandler {
	private static JFrame root = null;
	private static final String CHARSET = "utf-8";
	private static JMenuBar bar = new JMenuBar();

	public static void initBar() {
		JMenu login_menu = new JMenu("登录选项");
		JMenu sys_menu = new JMenu("系统设置");
		JMenu about_menu = new JMenu("关于");
		JMenu login_id_item = new JMenu("未登录");
		JMenuItem logout_item = new JMenuItem("注销");
		Util.disable(login_id_item, logout_item);
		logout_item.addActionListener(e -> {
			HttpResponse response = Func.logout();
			if (response.getStatusLine().getStatusCode() != 200) {
				JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
			}
			Func.setUser(null);
			login_id_item.removeAll();
			transToLogin();
		});
		Util.addBatch(login_menu, login_id_item, logout_item);
		Util.addBatch(bar, login_menu, sys_menu, about_menu);
	}

	public static void transToLogin() {
		root.dispose();
		root = new LoginFrame().init();
		root.setJMenuBar(bar);
		root.setVisible(true);
		bar.getMenu(0).getItem(1).setEnabled(false);
	}

	public static void LoginHander(String id, String pwd, JFrame component) {
		HttpResponse response = Func.login_cookie(id);
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
					response = Func.login_id_pwd(id, pwd);
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
				Sys.updateCookie(id);
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
				UIHandler.setRoot(new IndexFrame().init());
				root.setVisible(true);
			} catch (UnsupportedOperationException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void generateTree(HttpResponse response, JTree tree, JFrame component) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(component, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
			transToLogin();
		} else {
			tree.setModel((TreeModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), CHARSET, false);
				JSONObject response_json = JSONObject.parseObject(json);
				System.out.println(json);
				Map<String, Object> conf = new HashMap<>();
				conf.put("json", response_json.getString("data"));
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

	public static JTree generateResourceSelectionTree(HttpResponse response, Component component) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(component, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
			transToLogin();
			return null;
		} else {
			return (JTree) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), CHARSET, false);
				String data_json = JSONObject.parseObject(json).getJSONObject("data").getString("all");
				System.out.println(data_json);
				Map<String, Object> conf = new HashMap<>();
				conf.put("json", data_json);
				conf.put("fid_field", "fid");
				conf.put("id_field", "id");
				conf.put("root_fid", "");
				conf.put("name_field", "name");
				return TreeNodeGenerator.genCheckedTree("资源列表", conf);
			}, Handler.PRINTTRACE);
		}
	}

	public static void handle_auth(String role_id, String[] ids) {
		HttpResponse response = Func.auth(JSON.toJSONString(ids), role_id);
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), CHARSET, false);
				System.out.println(json);
				return null;
			}, Handler.PRINTTRACE);
		}
	}

	public static void handler_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), CHARSET, false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("userid").setTitle("用户标识符"),
						new KV().setField("roleid").setTitle("角色标识符"), new KV().setField("role_name").setTitle("角色名"),
						new KV().setTitle("角色描述").setField("role_description") });
				return model;
			}, Handler.PRINTTRACE);
			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_roles_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), CHARSET, false);
				// System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("id").setTitle("角色标识符"),
						new KV().setField("name").setTitle("角色名字"), new KV().setField("description").setTitle("角色描述"),
						new KV().setTitle("是否可用").setField("available") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "不可用" : "可用";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);
			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_company_image(HttpResponse response, ImageViewer label) {
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

	public static void fill_form(HttpResponse response, String[] field, JTextField... ins) {
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

	public static void handle_backups_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), CHARSET, false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("name").setTitle("备份文件名称"),
						new KV().setField("path").setTitle("备份文件路径"),
						new KV().setField("last_modified").setTitle("备份创建时间") });
				return model;
			}, Handler.PRINTTRACE);
			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_communities_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = JSONObject.parseObject(FileUtil.readText(response.getEntity().getContent(), "utf-8", false)).getString("data");
				System.out.println(json);
				JSONTableModel model = new CommunitiesTableModelImpl();
				model.getData().parse(json);
				return model;
			}, Handler.PRINTTRACE);
			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_buildings_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = JSONObject
						.parseObject(FileUtil.readText(response.getEntity().getContent(), "utf-8", false))
						.getString("data");
				System.out.println(json);
				// JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(json);
				model.setColumns(new KV[] { new KV().setField("building_id").setTitle("建筑编号"),
						new KV().setField("community_name").setTitle("所在小区名字"),
						new KV().setField("building_type").setTitle("建筑类型"),
						new KV().setField("floor_area").setTitle("建筑面积"), new KV().setField("floor_num").setTitle("层数"),
						new KV().setField("direction").setTitle("建筑朝向"), new KV().setField("height").setTitle("楼高"),
						new KV().setField("crttime").setTitle("楼建筑时间"),
						new KV().setField("description").setTitle("楼描述") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);
			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_cars_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("building_id").setTitle("建筑编号"),
						new KV().setField("community_name").setTitle("所在小区名字"),
						new KV().setField("room_id").setTitle("所在房间号码"),
						new KV().setField("owner_id").setTitle("拥有者编号"), new KV().setField("label").setTitle("车牌"),
						new KV().setField("color").setTitle("车身颜色"), new KV().setField("car_id").setTitle("车辆编号") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			Util.runUi(() -> {
				// System.out.println("UI");
				table.setModel(model_);
				table.updateUI();
			});
		}
	}

	public static void handle_chargeforms_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("form_id").setTitle("账单编号"),
						new KV().setField("building_id").setTitle("建筑编号"),
						new KV().setField("community_name").setTitle("小区编号"),
						new KV().setField("room_id").setTitle("所在房间号码"), new KV().setField("floor_id").setTitle("楼层编号"),
						new KV().setField("owner_id").setTitle("账单拥有者编号"),
						new KV().setField("pay_way").setTitle("结算方式"), new KV().setField("item_id").setTitle("收费事件编号"),
						new KV().setField("item_num").setTitle("收费事件数量"),
						new KV().setField("start_time").setTitle("计费开始时间"),
						new KV().setField("end_time").setTitle("计费结束时间"),
						new KV().setField("receivable").setTitle("应收金额"),
						new KV().setField("real_receipt").setTitle("实收金额"), new KV().setField("balance").setTitle("余额"),
						new KV().setField("form_creater").setTitle("制单人"),
						new KV().setField("form_crttime").setTitle("制单时间") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);
			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_chargeitems_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("item_id").setTitle("收费事件编号"),
						new KV().setField("item_type").setTitle("收费类型"),
						new KV().setField("descript").setTitle("事件描述"), new KV().setField("fee").setTitle("费用") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_companies_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("info").setTitle("基本信息"),
						new KV().setField("description").setTitle("公司简介"),
						new KV().setField("legal_person").setTitle("公司法人"),
						new KV().setField("imgurl").setTitle("公司大照片"), new KV().setField("address").setTitle("公司位置"),
						new KV().setField("contact_tel").setTitle("公司联系电话"),
						new KV().setField("contact_email").setTitle("公司联系邮件") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			Util.runUi(() -> {
				// System.out.println("UI");
				table.setModel(model_);
				table.updateUI();
			});
		}
	}

	public static void handle_contracts_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("contract_id").setTitle("合同编号"),
						new KV().setField("crttime").setTitle("建立时间"), new KV().setField("deadtime").setTitle("到期时间"),
						new KV().setField("valid").setTitle("有效性"),
						new KV().setField("creator").setTitle("创建者") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);
			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_departments_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("did").setTitle("部门编号"),
						new KV().setField("community_name").setTitle("社区名字"),
						new KV().setField("name").setTitle("部门名字"), new KV().setField("manager").setTitle("管理者") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_devices_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("device_id").setTitle("设备编号"),
						new KV().setField("name").setTitle("设备名称"), new KV().setField("version").setTitle("设备型号"),
						new KV().setField("floor_id").setTitle("楼层编号"),
						new KV().setField("building_id").setTitle("楼宇编号"),
						new KV().setField("community_name").setTitle("社区名称"), new KV().setField("price").setTitle("价格"),
						new KV().setField("install_time").setTitle("安装时间"),
						new KV().setField("bad_time").setTitle("设备寿命"),
						new KV().setField("install_man").setTitle("安装人员"),
						new KV().setField("status").setTitle("设备状态") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_devicechecks_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("device_id").setTitle("设备编号"),
						new KV().setField("check_frequency").setTitle("检修频次"),
						new KV().setField("descript").setTitle("检修内容"), new KV().setField("result").setTitle("效果"),
						new KV().setField("checker").setTitle("检修人员") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			Util.runUi(() -> {
				// System.out.println("UI");
				table.setModel(model_);
				table.updateUI();
			});
		}
	}

	public static void handle_employees_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("eid").setTitle("员工编号"),
						new KV().setField("name").setTitle("员工姓名"), new KV().setField("sex").setTitle("性别"),
						new KV().setField("birthday").setTitle("出生年月"), new KV().setField("tel").setTitle("员工电话"),
						new KV().setField("did").setTitle("部门编号"), new KV().setField("description").setTitle("个人描述"),
						new KV().setField("contract_id").setTitle("合同编号"),
						new KV().setField("entry_time").setTitle("入职时间") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);
			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_owners_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("building_id").setTitle("楼宇编号"),
						new KV().setField("community_name").setTitle("社区名称"),
						new KV().setField("room_id").setTitle("房间编号"),
						new KV().setField("owner_id").setTitle("业主身份证号码"),
						new KV().setField("floor_id").setTitle("楼层编号"),
						new KV().setField("contract_id").setTitle("合同编号"), new KV().setField("name").setTitle("业主姓名"),
						new KV().setField("sex").setTitle("业主性别"), new KV().setField("age").setTitle("业主年龄"),
						new KV().setField("tel").setTitle("联系电话"), new KV().setField("hukou").setTitle("户口所在地"),
						new KV().setField("work_place").setTitle("工作单位"),
						new KV().setField("contract_address").setTitle("联系地址"),
						new KV().setField("postalcode").setTitle("邮政编码"),
						new KV().setField("check_in_time").setTitle("入住时间"),
						new KV().setField("pay_way").setTitle("付款方式"), new KV().setField("remark").setTitle("备注") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_ownerfamilies_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("building_id").setTitle("楼宇编号"),
						new KV().setField("community_name").setTitle("社区名称"),
						new KV().setField("room_id").setTitle("房间编号"),
						new KV().setField("owner_id").setTitle("业主身份证号码"),
						new KV().setField("floor_id").setTitle("楼层编号"),
						new KV().setField("member_id").setTitle("身份证号码"), new KV().setField("sex").setTitle("业主性别"),
						new KV().setField("relation").setTitle("与业主关系"), new KV().setField("hukou").setTitle("户口所在地"),
						new KV().setField("work_study_place").setTitle("工作学习单位"),
						new KV().setField("tel").setTitle("联系电话") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			Util.runUi(() -> {
				// System.out.println("UI");
				table.setModel(model_);
				table.updateUI();
			});
		}
	}

	public static void handle_pets_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("building_id").setTitle("楼宇编号"),
						new KV().setField("community_name").setTitle("社区名称"),
						new KV().setField("room_id").setTitle("房间编号"),
						new KV().setField("owner_id").setTitle("业主身份证号码"), new KV().setField("variety").setTitle("品种"),
						new KV().setField("tall").setTitle("体高"), new KV().setField("pet_id").setTitle("证号") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			Util.runUi(() -> {
				// System.out.println("UI");
				table.setModel(model_);
				table.updateUI();
			});
		}
	}

	public static void handle_repairreports_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("report_id").setTitle("楼宇编号"),
						new KV().setField("owner_id").setTitle("身份证号"), new KV().setField("receiver").setTitle("接收者"),
						new KV().setField("contract_tel").setTitle("联系电话"),
						new KV().setField("report_time").setTitle("报修时间"),
						new KV().setField("destribute_time").setTitle("分配时间"),
						new KV().setField("descript").setTitle("描述"), new KV().setField("worker").setTitle("修理人员"),
						new KV().setField("worker_tel").setTitle("修理人员联系电话"),
						new KV().setField("finished_time").setTitle("完成时间") });
				model.setFormatter((field, value) -> {
					if (field.equals("available")) {
						return value.equals("0") ? "涓嶅彲鐢�" : "鍙敤";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);

			Util.runUi(() -> {
				// System.out.println("UI");
				table.setModel(model_);
				table.updateUI();
			});
		}
	}

	public static void handle_rooms_table(HttpResponse response, JTable table) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			TableModel model_ = (TableModel) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject response_json = JSONObject.parseObject(json);
				JSONTableModel model = JSONTableModel.create().init();
				model.getData().parse(response_json.getString("data"));
				model.setColumns(new KV[] { new KV().setField("building_id").setTitle("楼宇编号"),
						new KV().setField("community_name").setTitle("社区名称"),
						new KV().setField("room_id").setTitle("房间编号"), new KV().setField("floor_id").setTitle("楼层编号"),
						new KV().setField("room_layout").setTitle("房型布置"),
						new KV().setField("room_area").setTitle("房间面积"),
						new KV().setField("room_type").setTitle("房间类型"), new KV().setField("room_use").setTitle("房间用途"),
						new KV().setField("is_vacancy").setTitle("是否空置"),
						new KV().setField("decorated").setTitle("是否装修") });
				model.setFormatter((field, value) -> {
					if (field.equals("is_vacancy")) {
						return value.equals("0") ? "否" : "是";
					} else if (field.equals("decorated")) {
						return value.equals("0") ? "否" : "是";
					}
					return value;
				});
				return model;
			}, Handler.PRINTTRACE);
			table.setModel(model_);
			Util.runUi(() -> {
				// System.out.println("UI");
				table.updateUI();
			});
		}
	}

	public static void handle_set_role(String user_id, String role_id) {
		HttpResponse response = Func.set_role(user_id, role_id);
		String json = Func.toJSON(response);
		if (json == null) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			System.out.println(json);
			JSONObject jo = JSONObject.parseObject(json);
			JOptionPane.showMessageDialog(root, jo.getString("msg"), "返回信息", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void handle_backup() {
		HttpResponse response = Func.backup();
		String json = Func.toJSON(response);
		if (json == null) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			System.out.println(json);
			JSONObject jo = JSONObject.parseObject(json);
			JOptionPane.showMessageDialog(root, jo.getString("msg"), "返回信息", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void handle_delete_backup(String[] paths) {
		HttpResponse response = Func.delete_backup(paths);
		String json = Func.toJSON(response);
		if (json == null) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			System.out.println(json);
			JSONObject jo = JSONObject.parseObject(json);
			JOptionPane.showMessageDialog(root, jo.getString("msg"), "返回信息", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void handle_retore_backup(String path) {
		HttpResponse response = Func.restore(path);
		String json = Func.toJSON(response);
		if (json == null) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			System.out.println(json);
			JSONObject jo = JSONObject.parseObject(json);
			JOptionPane.showMessageDialog(root, jo.getString("msg"), "返回信息", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void handle_delete_building(Map<String, Object>[] values) {
		HttpResponse response = Func.delete_building(values);
		String json = Func.toJSON(response);
		if (json == null) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			System.out.println(json);
			JSONObject jo = JSONObject.parseObject(json);
			JOptionPane.showMessageDialog(root, jo.getString("msg"), "返回信息", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void handle_delete_callback(HttpResponse response, SimpleTask callback, int[] selects) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				JSONObject jo = JSONObject.parseObject(json);
				JOptionPane.showMessageDialog(root, jo.getString("msg"), "返回信息", JOptionPane.INFORMATION_MESSAGE);
				callback.exec(selects);
				return null;
			}, Handler.PRINTTRACE);

		}
	}

	public static void handle_delete(String url, JTable table, String[] keys, SimpleTask callback) {
		int[] selects = table.getSelectedRows();
		JSONTableModel model = (JSONTableModel) table.getModel();
		if (selects.length != 0) {
			ArrayList<Map<String, Object>> select_rows = new ArrayList<>();
			Arrays.stream(selects).forEach(i -> {
				Map<String, Object> row = model.getValuesAt(i);
				select_rows.add(CollectionUtil.putIn(keys, row));
			});
			Func.exec(() -> {
				HttpResponse response = Func.post(url, JSON.toJSONString(select_rows));
				handle_delete_callback(response, callback, selects);
			});
		}
	}

	public static JFrame getRoot() {
		return root;
	}

	public static void setRoot(JFrame root) {
		root.setJMenuBar(bar);
		UIHandler.root = root;
	}

	public static boolean add_success_callback(HttpResponse response, SimpleTask task) {
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			return (boolean) SimpleExec.exec(data -> {
				String json = FileUtil.readText(response.getEntity().getContent(), "utf-8", false);
				System.out.println(json);
				JSONObject jo = JSONObject.parseObject(json);
				JOptionPane.showMessageDialog(root, jo.getString("msg"), "返回信息", JOptionPane.INFORMATION_MESSAGE);
				task.exec(true, json);
				return "0".equals(jo.getString("suc")) ? true : false;
			}, Handler.PRINTTRACE);

		}
	}

	@SuppressWarnings("unchecked")
	public static void showForm(String title, String uri, SimpleTask callback, KV... kvs) {
		JDialog dialog = new JDialog();
		Point root_loc = root.getLocation();
		dialog.setLocationRelativeTo(root);
		dialog.setSize(500, 600);
		dialog.setResizable(false);
		dialog.setLocation((int) root_loc.getX() + 250, (int) root_loc.getY() + 250);
		dialog.setModal(true);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
		StandardForm form = StandardForm.create(kvs).init(5, 20).initSize(500, new Dimension(250, 30));
		form.setPreferredSize(new Dimension(450, 500));
		// form.setBorder(BorderFactory.createRaisedBevelBorder());;
		form.onSubmit((e1, args1) -> {
			// 提交的事件
			Map<String, String> map = (Map<String, String>) args1[0];
			String json = JSON.toJSONString(map);
			HttpResponse response = Func.post(uri, json);
			callback.exec(response, json);
		}).onCanel((e, args) -> {
			dialog.dispose();
		});
		// 自带pack效果
		dialog.getContentPane().add(form.complete());
		dialog.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public static void showComplexForm(String title, String uri, SimpleTask callback, String[] combo,
			Map<String, Object> values, KV... kvs) {
		System.out.println(values);
		JDialog dialog = new JDialog();
		dialog.setTitle(title);
		Point root_loc = root.getLocation();
		dialog.setLocationRelativeTo(root);
		dialog.setSize(500, 600);
		dialog.setResizable(false);
		dialog.setLocation((int) root_loc.getX() + 250, (int) root_loc.getY() + 250);
		dialog.setModal(true);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
		Map<String, FieldType> mapper = new HashMap<>();
		if (combo != null) {
			Arrays.stream(combo).forEach(field -> mapper.put(field, FieldType.COMBOBOX));
		}

		ComplexForm form = ComplexForm.create(kvs, dialog).init_size(500, new Dimension(250, 30)).init_field(mapper)
				.init_gap(20, 5).build().fill(values);
		form.setPreferredSize(new Dimension(450, 500));
		// form.setBorder(BorderFactory.createRaisedBevelBorder());;
		form.onSubmit((e1, args1) -> {
			// 提交的事件
			Map<String, String> map = (Map<String, String>) args1[0];
			String json = JSON.toJSONString(map);
			HttpResponse response = Func.post(uri, json);
			callback.exec(response, json);
		}).onCanel((e, args) -> {
			dialog.dispose();
		});
		// 自带pack效果
		dialog.getContentPane().add(form);
		dialog.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public static void showComplexerForm(String title, String uri, SimpleTask callback, String[] combo,
			String[] table_selector_col, SimpleTask[] task, SimpleTask[] confirm, String table_show_col,
			Map<String, Object> values, String[] disables, KV... kvs) {
		System.out.println(values);
		JDialog dialog = new JDialog();
		dialog.setTitle(title);
		Point root_loc = root.getLocation();
		dialog.setLocationRelativeTo(root);
		dialog.setSize(500, 600);
		dialog.setResizable(false);
		dialog.setLocation((int) root_loc.getX() + 250, (int) root_loc.getY() + 250);
		dialog.setModal(true);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
		Map<String, FieldType> mapper = new HashMap<>();
		if (combo != null) {
			Arrays.stream(combo).forEach(field -> mapper.put(field, FieldType.COMBOBOX));
		}
		DStream.newInstance().merge(Arrays.stream(table_selector_col), Arrays.stream(task), Arrays.stream(confirm))
				.mergeMap(args -> {
					String field = "" + args[0];
					SimpleTask t = (SimpleTask) args[1];
					mapper.put(field, FieldType.TABLE_SELECTOR);
					Object value = values.get(field);
					// ((KV)value).setValue(selector(t));
					StandardTableSelector selector = (StandardTableSelector) selector(t);
					values.put(field, selector.comfirm((SimpleTask) args[2], root,
							((JSONTableModel) selector.getModel()).colIndex(table_show_col)));
					return null;
				});
		ComplexForm form = ComplexForm.create(kvs, dialog).init_size(500, new Dimension(250, 30)).init_field(mapper)
				.init_gap(20, 5).build().fill(values).disable(disables);
		form.setPreferredSize(new Dimension(450, 300));
		if (disables != null) {
			form.disable(disables);
		}
		// form.setBorder(BorderFactory.createRaisedBevelBorder());;
		form.onSubmit((e1, args1) -> {
			// 提交的事件
			Map<String, String> map = (Map<String, String>) args1[0];
			String json = JSON.toJSONString(map);
			HttpResponse response = Func.post(uri, json);
			callback.exec(response, json);
		}).onCanel((e, args) -> {
			dialog.dispose();
		});
		// 自带pack效果
		dialog.getContentPane().add(form);
		dialog.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public static void showComplexe2Form(SimpleTask callback, String title, String uri, FormModel model,
			String[] params) {
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
			StringBuilder paramsStr = new StringBuilder();
			if (params != null) {
				Arrays.stream(params).forEach(param -> {
					String value = map.get(param);
					if (value != null) {
						paramsStr.append("&");
						paramsStr.append(param);
						paramsStr.append("=");
						paramsStr.append(value);
					}
				});
			}
			HttpResponse response = Func.post(uri+paramsStr, json);
			callback.exec(response, json);
		}).onCanel((e, args) -> {
			dialog.dispose();
		});
		// 自带pack效果
		dialog.getContentPane().add(form);
		dialog.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public static void showComplexEditForm(JTable table, String title, String uri, SimpleTask callback, String[] combo,
			Map<String, Object> values, String[] disables, KV... kvs) {
		int selected = table.getSelectedRow();
		if (selected >= 0) {
			JDialog dialog = new JDialog();
			dialog.setTitle(title);
			Point root_loc = root.getLocation();
			dialog.setLocationRelativeTo(root);
			dialog.setSize(500, 600);
			dialog.setResizable(false);
			dialog.setLocation((int) root_loc.getX() + 250, (int) root_loc.getY() + 250);
			dialog.setModal(true);
			dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
			Map<String, FieldType> mapper = new HashMap<>();
			if (combo != null) {
				Arrays.stream(combo).forEach(field -> mapper.put(field, FieldType.COMBOBOX));
			}

			ComplexForm form = ComplexForm.create(kvs, dialog).init_size(500, new Dimension(250, 30)).init_field(mapper)
					.init_gap(20, 5).build().disable(disables)
					.fill(((JSONTableModel) table.getModel()).getValuesAt(selected)).fill(values);
			form.setPreferredSize(new Dimension(450, 500));
			// form.setBorder(BorderFactory.createRaisedBevelBorder());;
			form.onSubmit((e1, args1) -> {
				// 提交的事件
				Map<String, String> map = (Map<String, String>) args1[0];
				String json = JSON.toJSONString(map);
				HttpResponse response = Func.post(uri, json);
				add_success_callback(response, args2 -> {
					return callback.exec(args2[1], json);
				});

			}).onCanel((e, args) -> {
				dialog.dispose();
			});
			// 自带pack效果
			dialog.getContentPane().add(form);
			dialog.setVisible(true);
		}
	}

	public static void refresh(JTable table, String uri) {
		HttpResponse response = Func.get(uri);
		if (response.getStatusLine().getStatusCode() != 200) {
			JOptionPane.showMessageDialog(root, "请求错误", "错误信息", JOptionPane.ERROR_MESSAGE);
		} else {
			String json = Func.toJSON(response);
			JSONObject jo = JSONObject.parseObject(json);
			String rows = jo.getString("data");
			JSONTableModel model = (JSONTableModel) table.getModel();
			model.getData().parse(rows);
		}
	}

	@SuppressWarnings("unchecked")
	public static void showEditForm(JTable table, String title, String uri, SimpleTask callback, KV[] kvs,
			String[] disables) {
		int selected = table.getSelectedRow();
		if (selected >= 0) {
			JDialog dialog = new JDialog(root);
			Point root_loc = root.getLocation();
			dialog.setLocationRelativeTo(root);
			dialog.setLocation((int) root_loc.getX() + 250, (int) root_loc.getY() + 250);
			dialog.setSize(500, 600);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
			StandardForm form = StandardForm.create(kvs).init(5, 20).disable(disables)
					.initSize(500, new Dimension(250, 30))
					.fill(((JSONTableModel) table.getModel()).getValuesAt(selected));
			form.setPreferredSize(new Dimension(450, 500));
			form.onSubmit((e1, args1) -> {
				// 提交的事件
				Map<String, String> map = (Map<String, String>) args1[0];
				String json = JSON.toJSONString(map);
				HttpResponse response = Func.post(uri, json);
				add_success_callback(response, callback);
			}).onCanel((e, args) -> {
				dialog.dispose();
			});
			form.onCanel((e, args) -> {
				dialog.dispose();
			});
			// 自带pack效果
			dialog.getContentPane().add(form.complete());
			dialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(root, "请选择行", "编辑信息", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void showTableSector(SimpleTask init_table_task, SimpleTask confirm_task, String table_show_col) {
		JDialog dialog = new JDialog();
		if (root != null) {
			Point point = root.getLocation();
			dialog.setLocationRelativeTo(root);
			dialog.setLocation((int) point.getX() + 250, (int) point.getY() + 250);
		}
		dialog.setSize(500, 500);
		dialog.setModal(true);
		dialog.setLayout(new FlowLayout());
		StandardTableSelector selector = (StandardTableSelector) selector(init_table_task);
		selector.comfirm(confirm_task, ((JSONTableModel) selector.getModel()).colIndex(table_show_col));
		dialog.add(selector.update());
		dialog.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public static void handle_table_selector(String target_field, String src_field, Object... args) {
		int index = (int) args[0];
		JSONTableModel tm = (JSONTableModel) args[1];
		Map<String, Object> attrs = (Map<String, Object>) args[2];
		ComplexForm2 form = (ComplexForm2) attrs.get("_form_");
		JTextField input = (JTextField) form.getField(target_field).input();
		input.setText(tm.getValuesAt(index).get(src_field).toString());
	}

	public static TableSelector selector(SimpleTask task) {
		return StandardTableSelector.create().size(400, 400).init_table_by(task).header_size(150, 30).build();
	}

	public static KV[] toKVs(String field, String[] items) {
		return Arrays.stream(items).map(item -> new KV().setField(field).setValue(item).setTitle(item))
				.toArray(KV[]::new);
	}
}
