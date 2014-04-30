package cuki;

import javax.swing.JFrame;

import cuki.frame.Mostrador;

@SuppressWarnings("serial")
public class TesteMostrador extends JFrame {

	public TesteMostrador() {
		Mostrador mostrador = new Mostrador();
		add(mostrador);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new TesteMostrador();
	}
}
