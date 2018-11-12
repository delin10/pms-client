package test;

import java.awt.Font;

import javax.swing.JFrame;

import org.jdatepicker.JDatePicker;

public class Test extends JFrame{
	public static void main(String[] args) {
		JDatePicker picker=new JDatePicker();
		//JDatePanel panel=new JDatePanel();
		JFrame frame=new JFrame("日期选择器");
		
		frame.setSize(500, 500);
		frame.getContentPane().add(picker);
		frame.setVisible(true);
		
	}
}
