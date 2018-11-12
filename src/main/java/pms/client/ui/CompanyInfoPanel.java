package pms.client.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import jdk.nashorn.internal.ir.BreakableNode;
import pms.client.data.RuntimeStore;
import pms.client.funcs.Func;
import pms.client.handler.request.FilePostRequest;
import pms.client.handler.request.impl.CompanyRequestImpl;
import pms.client.handler.ui.BasicUIHandler;
import pms.client.ui.intef.SwitchPanel;
import util.comm.file.FileUtil;
import util.comm.lambda.AntiFinalVar;
import util.comm.string.StringIgnoreCaseWrapper;
import util.ui.swing.border.RoundBorder;
import util.ui.swing.comm.Util;
import util.ui.swing.custom_comp.ImageViewer;
import util.ui.swing.gen.CompGenerator;

public class CompanyInfoPanel extends SwitchPanel {
	private static final long serialVersionUID = 6308469801781909775L;
	private final Font font = new Font("华文楷体", Font.BOLD, 16);
	private Box root_box = Box.createVerticalBox();
	private ImageViewer image = ImageViewer.create();
	private JLabel title = new JLabel("公司基本信息");
	private JLabel name = new JLabel("公司名称");
	private JLabel description = new JLabel("公司描述");
	private JLabel addr = new JLabel("公司位置");
	private JLabel tel = new JLabel("公司电话");
	private JLabel email = new JLabel("公司邮件");
	private JTextField name_input = new JTextField();
	private JTextField description_input = new JTextField();
	private JTextField addr_input = new JTextField();
	private JTextField tel_input = new JTextField();
	private JTextField email_input = new JTextField();
	private JButton edit_button = CompGenerator.genButton("", "edit-normal-16px", "edit-pressed-16px");
	private JButton submit_button = new JButton("提交");
	private JButton reset_button = new JButton("取消");
	public CompanyRequestImpl requestImpl = new CompanyRequestImpl();
	private FilePostRequest uploader=new FilePostRequest();

	public CompanyInfoPanel init() {
		int label_w = 80;
		int label_h = 50;
		int in_w = 200;
		int in_h = 50;
		Box image_box = Box.createHorizontalBox();
		Box title_box = Box.createHorizontalBox();
		Box name_box = Box.createHorizontalBox();
		Box description_box = Box.createHorizontalBox();
		Box addr_box = Box.createHorizontalBox();
		Box tel_box = Box.createHorizontalBox();
		Box email_box = Box.createHorizontalBox();
		Box bottom_button_box = Box.createHorizontalBox();
		Util.setSize(70, 30, submit_button, reset_button);
		Util.setSize(label_w, label_h, name, description, addr, tel, email);
		Util.setAlign(JLabel.RIGHT_ALIGNMENT, name, description, addr, tel, email);
		Util.setMaxSize(in_w, in_h, name_input, description_input, addr_input, tel_input, email_input);
		Util.setBorder(RoundBorder.create().setArc_h(5).setArc_w(5), name_input, description_input, addr_input,
				tel_input, email_input);
		Util.disable(submit_button, reset_button);
		Util.batch((comp, args) -> {
			comp.setFont((Font) args[0]);
		}, new Object[] { font }, name_input, description_input, addr_input, tel_input, email_input);
		Util.disable(name_input, description_input, addr_input, tel_input, email_input);
		edit_button.setDisabledIcon(new ImageIcon(ClassLoader.getSystemResource("pms/client/icon/edit-pressed.png")));
		edit_button.addActionListener(e -> {
			int option = JOptionPane.showConfirmDialog(this, "确认修改公司基本信息?", "确认信息", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				edit_button.setEnabled(false);
				Util.enable(submit_button, reset_button);
				Util.enable(name_input, description_input, addr_input, tel_input, email_input);
			}

		});
		submit_button.addActionListener(e -> {
			String json = Util.map(new String[] { "1", "2", "3", "4", "5" }, name_input, description_input, addr_input,
					tel_input, email_input);
			System.out.println(json);
		});

		reset_button.addActionListener(e -> {
			Util.runUi(() -> {
				Util.resume(0, name_input, description_input, addr_input, tel_input, email_input);
				edit_button.setEnabled(true);
				Util.disable(submit_button, reset_button);
			});
		});

		Util.record(null, name_input, description_input, addr_input, tel_input, email_input);

		image.setImageSize(300, 300);

		image.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int selectConfirm = JOptionPane.showConfirmDialog(BasicUIHandler.getRoot(), "是否需要更换图片?", "更换图片",
							JOptionPane.OK_CANCEL_OPTION);
					if (selectConfirm == JOptionPane.OK_OPTION) {
						AntiFinalVar isImage = AntiFinalVar.create();
						AntiFinalVar imgBytes = AntiFinalVar.create();
						JFileChooser chooser = new JFileChooser();
						HashSet<StringIgnoreCaseWrapper> exts = new HashSet<>();
						exts.add(StringIgnoreCaseWrapper.create().parse("jpg"));
						exts.add(StringIgnoreCaseWrapper.create().parse("png"));
						exts.add(StringIgnoreCaseWrapper.create().parse("jepg"));
						chooser.setFileFilter(new FileFilter() {
							@Override
							public String getDescription() {
								// TODO Auto-generated method stub
								return "图片格式" + exts;
							}

							@Override
							public boolean accept(File f) {
								// TODO Auto-generated method stub
								if (f.isDirectory()) {
									return true;
								}
								String name = f.getName();
								String ext = name.substring(name.lastIndexOf(".") + 1);
								System.out.println(ext);
								if (exts.contains(StringIgnoreCaseWrapper.create().parse(ext))) {
									return true;
								}
								return false;
							}
						});
						JTextArea textArea = new JTextArea();
						ImageViewer viewer = ImageViewer.create();
						viewer.setImageSize(200, 200);
						textArea.setPreferredSize(new Dimension(100, 100));
						// chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						chooser.setAcceptAllFileFilterUsed(true);
						chooser.addPropertyChangeListener(event -> {
							if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
								File f = (File) event.getNewValue();
								if (f == null) {
									viewer.setIcon(null);
									return;
								}
								RuntimeStore.exec(() -> {
									byte[] bytes = FileUtil.getBytes(f);
									viewer.setImage(bytes, args -> {
										isImage.parse(viewer.isStatus());
										if (viewer.isStatus()) {
											imgBytes.parse(bytes);
											chooser.setAccessory(viewer);
											Util.runUi(() -> {
												chooser.updateUI();
											});
										} else {
											chooser.setAccessory(textArea);
											textArea.setText(FileUtil.readText(bytes));
										}
										return null;
									});
								});
								// viewer.setIcon(icon);
							}
						});
						// chooser.addMouseListener(l);
						while (true) {
							int selectChooser = chooser.showOpenDialog(BasicUIHandler.getRoot());
							if (selectChooser == JFileChooser.APPROVE_OPTION) {
								File file = chooser.getSelectedFile();
								if (file != null && isImage.getBoolean()) {
									RuntimeStore.exec(()->{
										RuntimeStore.runUI().handle_response(uploader.upload((byte[])imgBytes.getObject()));
									});
									break;
								} else {
									JOptionPane.showMessageDialog(BasicUIHandler.getRoot(), "未选择图片或选中不是图片", "选中信息",
											JOptionPane.INFORMATION_MESSAGE);
								}
							}else {
								break;
							}
						}
					}
				}
			}
		});
		// box里添加了box不要添加其他组件
		// name_box.setBorder(BorderFactory.createLoweredBevelBorder());
		// image_box.setBorder(BorderFactory.createLoweredBevelBorder());
		// root_box.setBorder(BorderFactory.createLoweredBevelBorder());
		Util.addBatch(title_box, title);
		title_box.add(edit_button, Box.RIGHT_ALIGNMENT);
		Util.addBatch(name_box, name, Box.createHorizontalStrut(20), name_input);
		Util.addBatch(description_box, description, Box.createHorizontalStrut(20), description_input);
		Util.addBatch(addr_box, addr, Box.createHorizontalStrut(20), addr_input);
		Util.addBatch(tel_box, tel, Box.createHorizontalStrut(20), tel_input);
		Util.addBatch(email_box, email, Box.createHorizontalStrut(20), email_input);
		Util.addBatch(bottom_button_box, submit_button, Box.createHorizontalStrut(20), reset_button);
		image_box.add(image, Box.CENTER_ALIGNMENT);

		Util.addBatch(root_box, Box.createVerticalStrut(100), title_box, Box.createVerticalStrut(20), image_box,
				Box.createVerticalStrut(20), title_box, Box.createVerticalStrut(20), name_box,
				Box.createVerticalStrut(20), description_box, Box.createVerticalStrut(20), addr_box,
				Box.createVerticalStrut(20), tel_box, Box.createVerticalStrut(20), email_box,
				Box.createVerticalStrut(20), bottom_button_box);
		this.add(root_box);
		// root_box.setBorder(BorderFactory.createLoweredBevelBorder());
		return this;
	}

	public CompanyInfoPanel initImageViewer() {
		Func.exec(() -> {
			this.setVisible(true);
			RuntimeStore.runUI().handle_company_image(requestImpl.company_image(), image);

		});
		return this;
	}

	public CompanyInfoPanel initInfo() {
		Func.exec(() -> {
			RuntimeStore.runUI().fill_form(requestImpl.company_info(),
					new String[] { "info", "description", "address", "contact_tel", "contact_email" }, name_input,
					description_input, addr_input, tel_input, email_input);
		});
		return this;
	}

	@Override
	public SwitchPanel switchIn(JComponent container, Runnable run) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Util.runUi(() -> {
			container.removeAll();
			container.add(this);
			// table.updateUI();
			// scroll.updateUI();
			// root.updateUI();
			this.updateUI();
			container.updateUI();
			if (run != null) {
				run.run();
			}
		});
		return this;
	}

	@Override
	public SwitchPanel refresh() {
		return initImageViewer().initInfo();
	}
}
