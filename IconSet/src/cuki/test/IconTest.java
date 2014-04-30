package cuki.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cuki.utils.IconSet;

public class IconTest {

	private static int iconNr = 0;
	private static JButton btn = null;
	private static JLabel label = null;

	private static void setNr(int nr) {
		iconNr = nr;
	}

	private static int getNr() {
		return IconTest.iconNr;
	}

	public static void main(String[] args) {

		ImageIcon icon = null;

		try {
			icon = IconSet.getIcon(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		btn = new JButton(icon);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IconTest.setNr(IconTest.getNr() + 1);
				try {
					btn.setIcon(IconSet.getIcon(IconTest.getNr()));
					label.setText(IconSet.getIcon(IconTest.getNr())
							.getDescription());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText("Bomb");

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(btn, BorderLayout.NORTH);
		panel.add(label, BorderLayout.SOUTH);

		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

}
