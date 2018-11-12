package util.ui.swing.custom_comp;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import util.comm.file.FileUtil;
import util.comm.lambda.functon.SimpleTask;
import util.ui.swing.comm.Util;
import util.web.conn_httpclient.tools.Tools;

public class ImageViewer extends JLabel {
	private static final long serialVersionUID = 9168156603442442932L;
	private int IMAGE_W = 100;
	private int IMAGE_H = 100;
	private boolean status = false;

	private ImageViewer() {
		super();
	}

	public static ImageViewer create() {
		return new ImageViewer();
	}

	public ImageViewer init() {
		this.setDoubleBuffered(true);
		this.setFocusable(false);
		return this;
	}

	public ImageViewer setImageSize(int width, int height) {
		this.IMAGE_H = height;
		this.IMAGE_W = width;
		return this;
	}

	public ImageViewer setImage_nowait(InputStream stream) {
		byte[] bytes = FileUtil.getBytes(stream);
		Util.runUi(() -> {
			ImageIcon icon = new ImageIcon(bytes);
			// getwidth/height getsize获取渲染后的窗体大小
			// 获取到label的大小为0
			int rel_w = icon.getIconWidth();
			int rel_h = icon.getIconHeight();
			double rate_w = (1.0 * IMAGE_W) / rel_w;
			double rate_h = (1.0 * IMAGE_H) / rel_h;
			double rate = rate_w < rate_h ? rate_w : rate_h;
			icon = new ImageIcon(icon.getImage().getScaledInstance((int) (rel_w * rate), (int) (rel_h * rate),
					Image.SCALE_REPLICATE));
			super.setIcon(icon);
		});

		return this;
	}

	public ImageViewer setImage(int content_length, InputStream stream) {
		byte[] bytes = Tools.content_wait(content_length, stream);
		setImage(bytes, null);
		return this;
	}

	public ImageViewer setImage(byte[] bytes, SimpleTask callback) {
		this.setPreferredSize(new Dimension(IMAGE_W, IMAGE_H));
		this.setText("click here");
		Util.runUi(() -> {
			ImageIcon icon = new ImageIcon(bytes);
			System.out.println(icon.getImageLoadStatus() );
			if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
				// getwidth/height getsize获取渲染后的窗体大小
				// 获取到label的大小为0
				int rel_w = icon.getIconWidth();
				int rel_h = icon.getIconHeight();
				double rate_w = (1.0 * IMAGE_W) / rel_w;
				double rate_h = (1.0 * IMAGE_H) / rel_h;
				double rate = rate_w < rate_h ? rate_w : rate_h;
				Image image=icon.getImage();
				icon = new ImageIcon(icon.getImage().getScaledInstance((int) (rel_w * rate), (int) (rel_h * rate),
						Image.SCALE_REPLICATE));
				super.setIcon(icon);
				System.out.println("图片描述:"+icon.getImage().getWidth(null));
				setStatus(true);
			} else {
				setStatus(false);
			}
			if (callback != null) {
				callback.exec(status);
			}
		});

		return this;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
