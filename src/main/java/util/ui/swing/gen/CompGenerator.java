package util.ui.swing.gen;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import util.comm.file.FileUtil;
import util.comm.lambda.DStream;
import util.ui.swing.comm.Util;

public class CompGenerator {

	public static String iconPath(String name) {
		return FileUtil.removeProtocol(CompGenerator.class.getClassLoader().getResource("resource/icon/" + name));
	}

	public static JButton genButton(String text, String normal_icon, String pressed_icon) {
		JButton button = new JButton(text);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusable(false);
		button.setDoubleBuffered(true);
		if (normal_icon != null)
			button.setIcon(new ImageIcon(FileUtil.removeProtocol(
					CompGenerator.class.getClassLoader().getResource("resource/icon/" + normal_icon + ".png"))));
		if (pressed_icon != null)
			button.setPressedIcon(new ImageIcon(FileUtil.removeProtocol(
					CompGenerator.class.getClassLoader().getResource("resource/icon/" + pressed_icon + ".png"))));
		return button;
	}

	public static JButton genAddButton(String text) {
		return genButton(text, "add-normal", "add-pressed");
	}

	public static JButton genDelButton(String text) {
		return genButton(text, "del-normal", "del-pressed");
	}
	
	public static Box genLabelInput(String name,Dimension input_size) {
		Box box=Box.createHorizontalBox();
		JLabel label=new JLabel(name);
		JTextField field=new JTextField();
		Util.addBatch(box, label,Box.createHorizontalStrut(10),field);
		return box;
	}

	public static JPopupMenu genPopupMenu(String[] icons, String[] icon_selected, ActionListener[] listeners,
			String... names) {
		Font font = new Font("宋体", Font.BOLD, 16);
		JPopupMenu popup = new JPopupMenu();
		@SuppressWarnings("rawtypes")
		ArrayList<Stream> streams = new ArrayList<>();
		streams.add(Arrays.stream(names));
		if (listeners != null) {
			streams.add(Arrays.stream(listeners));
		}

		if (icons != null) {
			streams.add(Arrays.stream(icons));
		}

		if (icon_selected != null) {
			streams.add(Arrays.stream(icon_selected));
		}

		DStream.newInstance().merge(streams.toArray(new Stream[0]))
				.map(null, null, p1 -> iconPath(p1.toString()), p2 -> iconPath(p2.toString())).mergeMap(args -> {
					JMenuItem item = new JMenuItem(args[0].toString());

					if (args.length > 1 && args[1] != null) {
						item.addActionListener((ActionListener) args[1]);
					}
					if (args.length > 2 && args[2] != null) {
						// System.out.println(args[1]);
						item.setIcon(new ImageIcon(args[1].toString()));
					}
					if (args.length > 3 && args[3] != null) {
						item.setSelectedIcon(new ImageIcon(args[1].toString()));
					}
					item.setFont(font);
					return item;
				}).map(item -> (JMenuItem) item).forEach(popup::add);
		return popup;
	}

	public static JMenuItem genIconMenuItem(String name, String normal_icon, String select_icon) {
		JMenuItem item = new JMenuItem(name);
		item.setIcon(new ImageIcon(iconPath(normal_icon)));
		item.setSelectedIcon(new ImageIcon(iconPath(select_icon)));
		return item;
	}

	public static Box genForm(ActionListener onsubmit,ActionListener oncancel,String...fields) {
		Box root=Box.createVerticalBox();
		Arrays.stream(fields).forEach(field->{
			Box box=genLabelInput(field, new Dimension(100, 30));
			root.add(box);
		});
		Box box=Box.createHorizontalBox();	
		JButton submit_button=new JButton("提交");
		JButton cancel_button=new JButton("取消");
		submit_button.addActionListener(onsubmit);
		submit_button.addActionListener(oncancel);
		Util.setPreSize(70, 30, submit_button,cancel_button);
		Util.addBatch(box, submit_button,cancel_button);
		return root;
	}

	public static void main(String... args) {
		System.out.println(CompGenerator.class.getClassLoader().getResource("resource"));
	}
}
