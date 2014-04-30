package cuki;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.naming.SizeLimitExceededException;
import javax.swing.JButton;
import javax.swing.JFrame;

import cuki.frame.Oval;

@SuppressWarnings("serial")
public class TesteOval extends JFrame implements ActionListener {

	Oval pizza = new Oval();
	int[] anguloFinal = { 60, 60, 60, 60, 60, 60 };
	JButton btn = new JButton("Setor Restrito");
	boolean setorRestrito = false;

	public TesteOval() {

		pizza.setSetorRestrito(setorRestrito);
		try {
			pizza.setAnguloFinal(anguloFinal);
		} catch (SizeLimitExceededException e) {
			e.printStackTrace();
		}

		btn.addActionListener(this);
		add(pizza);
		getContentPane().add(btn, BorderLayout.PAGE_END);
		pack();

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		pizza.setSetorRestrito(setorRestrito);
		try {
			pizza.setAnguloFinal(anguloFinal);
		} catch (SizeLimitExceededException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setorRestrito = !setorRestrito;
		repaint();
	}

	public static void main(String[] args) {
		new TesteOval();
	}
}
