package pms.client.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import pms.client.data.RuntimeStore;
import pms.client.data.system.BasicSystem;
import pms.client.funcs.Func;
import pms.client.funcs.Sys;
import pms.client.handler.ui.BasicUIHandler;
import util.ui.swing.border.RoundBorder;
import util.ui.swing.comm.Util;
import util.ui.swing.event.input.InputMethodAdapter;

public class LoginFrame extends JFrame {
	private static final long serialVersionUID = -9098365400340251338L;
	private JLabel logo;
	private JLabel id_label;
	private JComboBox<String> id;
	private JLabel pwd_label;
	private JPasswordField pwd;
	private JButton login_button;
	private JCheckBox auto_login;
	private int BUTTON_WIDTH = 100;
	private int BUTTON_HEIGHT = 30;
	private int TEXT_FIELD_WIDTH = 200;
	private int TEXT_FIELD_HEIGHT = 50;
	private int LABEL_WIDTH = 50;
	private int LEFT_ANCHOR = 0;

	public LoginFrame init() {
		this.setTitle("物业管理系统-登录窗口");
		this.setSize(300, 300);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		logo = new JLabel("物业管理系统");
		id_label = new JLabel("账号");
		id = new JComboBox<>();
		pwd_label = new JLabel("密码");
		pwd = new JPasswordField();
		login_button = new JButton("登录");
		auto_login = new JCheckBox("自动登录");
		//logo.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		// 设置logo
		logo.setFont(new Font("宋体", Font.PLAIN, 24));

		id_label.setSize(LABEL_WIDTH, TEXT_FIELD_HEIGHT);
		// test
		// id.addItem("a");
		id.setEditable(true);
		id.setBorder(RoundBorder.create().setArc_h(5).setArc_w(5).padding(0));
		pwd.setBorder(new RoundBorder().setArc_h(5).setArc_w(5).padding(5));
		login_button.setBorder(new RoundBorder().setArc_h(5).setArc_w(5).padding(5));
		Util.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT, id, pwd);
		Util.setSize(BUTTON_WIDTH, BUTTON_HEIGHT, login_button, auto_login);

		// 添加监听器
		login_button.addActionListener(e -> {
			login_button.setEnabled(false);
			String id = this.id.getEditor().getItem().toString();
			if (!Util.isEmpty(pwd) && !id.isEmpty()) {
				Func.exec(() -> {
					String password = String.valueOf(pwd.getPassword());
					RuntimeStore.runUI().handle_login(id, password, this);
					boolean contains = false;
					for (int i = 0; i < this.id.getItemCount(); ++i) {
						if (this.id.getItemAt(i).equals(id)) {
							contains = true;
							break;
						}
					}
					if (!contains) {
						Util.runUi(() -> {
							this.id.addItem(id);
							// this.id.updateUI();
						});

					}
					Sys.storeIdAndPwd(id, password);
					Util.runUi(() -> {
						login_button.setEnabled(true);
					});
				});

			} else {
				JOptionPane.showMessageDialog(this, "密码或账号不能为空", "信息", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		Box full_box = Box.createVerticalBox();
		Box logo_box = Box.createHorizontalBox();
		Box id_box = Box.createHorizontalBox();
		Box pwd_box = Box.createHorizontalBox();
		Box button_box = Box.createHorizontalBox();

		logo_box.add(logo);

		id_box.add(Box.createHorizontalStrut(LEFT_ANCHOR));
		id_box.add(id_label);
		id_box.add(Box.createHorizontalStrut(5));
		id_box.add(id);

		pwd_box.add(Box.createHorizontalStrut(LEFT_ANCHOR));
		pwd_box.add(pwd_label);
		pwd_box.add(Box.createHorizontalStrut(5));
		pwd_box.add(pwd);

		button_box.add(Box.createHorizontalStrut(LEFT_ANCHOR));
		button_box.add(login_button);
		button_box.add(auto_login);

		full_box.add(Box.createVerticalStrut(30));
		full_box.add(logo_box);
		full_box.add(Box.createVerticalStrut(50));
		full_box.add(id_box);
		full_box.add(Box.createVerticalStrut(5));
		full_box.add(pwd_box);
		full_box.add(Box.createVerticalStrut(20));
		full_box.add(button_box);

		this.add(full_box);
		initAutoLogin();
		init_id_history();

		return this;
	}

	public void initAutoLogin() {
		String auto_login = Sys.getAutoLogin();
		// 进行自动登录初始化,
		if (auto_login == null || auto_login.equals("false")) {
			this.auto_login.setSelected(false);
		} else if (auto_login.equals("true")) {
			this.auto_login.setSelected(true);
		}

		this.auto_login.addActionListener(e -> {
			Sys.setAutoLogin(this.auto_login.isSelected());
		});
	}

	public void init_id_history() {
		Arrays.stream(BasicSystem.getIds()).forEach(id::addItem);
		String now = id.getEditor().getItem().toString();
		if (!now.isEmpty()) {
			pwd.setText(Sys.getPwd(now));
		}
		id.addItemListener(e -> {
			String id = e.getItem().toString();
			// System.out.println(id);
			pwd.setText(Sys.getPwd(id));
		});
		id.addInputMethodListener(InputMethodAdapter.create().setTextChangedCallback((e) -> {

		}));
	}

	public void auto_login() {

	}

	public static void main(String... args) {
		BasicUIHandler.setRoot(new LoginFrame().init());
		BasicUIHandler.getRoot().setVisible(true);
	}

}
