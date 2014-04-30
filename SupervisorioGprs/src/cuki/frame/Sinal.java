package cuki.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Sinal extends JPanel {

	private static int size = 20;
	private Color cor = Color.RED;

	public Sinal() {
		setPreferredSize(new Dimension(size, size));
		setBackground(Color.WHITE);
	}

	public Sinal(Color cor) {
		this();
		this.cor = cor;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Ellipse2D shape = new Ellipse2D.Double(0, 0, size, size);

		g2.setColor(this.cor);
		g2.fill(shape);
	}

	public void setCor(Color cor) {
		this.cor = cor;
	}

	public static void main(String[] args) throws InterruptedException {
		JFrame janela = new JFrame();
		Sinal sinal = new Sinal();

		janela.getContentPane().setBackground(Color.black);
		janela.add(sinal);
		janela.pack();
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setVisible(true);

		int cont = 0;
		Color cor = Color.RED;
		while (true) {
			if (cont == 0)
				cor = Color.RED;
			else if (cont == 1)
				cor = Color.YELLOW;
			else if (cont == 2) {
				cor = Color.GREEN;
				cont = -1;
			} else {
				cont = 0;
			}
			cont++;
			Thread.sleep(1000);
			sinal.setCor(cor);
			sinal.repaint();
		}
	}

	public Color getCor() {
		return this.cor;
	}
}
