package cuki.example;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogDemo {

	public static void main(String[] args) {
		JDialog dialog = new JDialog(new JFrame(), "No min max buttons");
		// necessary as setDefaultCloseOperation(EXIT_ON_CLOSE) is
		// not available for JDialogs.
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		});

		JLabel label = new JLabel("blah blah");
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 400));
		panel.add(label);

		dialog.add(panel);
		dialog.pack();
		dialog.setVisible(true);
	}
}
