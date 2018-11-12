package util.ui.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;	

public class RoundBorder implements Border{
	private Color color;
	private int arc_w = 5;
	private int arc_h = 5;
	private int padding_left=5;
	private int padding_top=5;
	private int padding_right=5;
	private int padding_bottom=5;
	
	public RoundBorder() {
		this.color = Color.GRAY;
	}
	
	public static RoundBorder create() {
		return new RoundBorder();
	}
	
	public Color getColor() {
		return color;
	}

	public RoundBorder setColor(Color color) {
		this.color = color;
		return this;
	}

	public int getArc_w() {
		return arc_w;
	}

	public RoundBorder setArc_w(int arc_w) {
		this.arc_w = arc_w;
		return this;
	}

	public int getArc_h() {
		return arc_h;
	}

	public RoundBorder setArc_h(int arc_h) {
		this.arc_h = arc_h;
		return this;
	}

	public RoundBorder(Color color) {// 有参数的构造方法
		this.color = color;
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(padding_top,padding_left,padding_bottom,padding_right);
	}
	
	public RoundBorder padding(int padding) {
		padding_top=padding_left=padding_bottom=padding_right=padding;
		return this;
	}

	public boolean isBorderOpaque() {
		return false;
	}

	// 实现Border（父类）方法
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.setColor(color);
		g.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, arc_w, arc_h);
	}
}
