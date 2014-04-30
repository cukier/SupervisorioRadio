package cuki.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.naming.SizeLimitExceededException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Oval extends JPanel {

	private int angulo = 0;
	private int larg = 240;
	private int alt = 240;
	private int diam = 200;
	private int[] anguloFinal = { 60, 120, 180, 240, 300, 360 };
	private boolean setorRestrito = false;

	public Oval() {
		setBorder(BorderFactory.createTitledBorder("Pivo nr 1"));
		setForeground(Color.WHITE);
		setPreferredSize(new Dimension(larg, alt));
	}

	private int getDistX(int angulo, int diam) {
		return (int) ((this.larg + diam * Math.sin(angulo * Math.PI / 180)) / 2);
	}

	private int getDistY(int angulo, int diam) {
		return (int) ((this.alt - diam * Math.cos(angulo * Math.PI / 180)) / 2);
	}

	private void desenhaLinha(Graphics2D g2, Ellipse2D aro, int angulo) {

		Line2D linha = new Line2D.Double(aro.getCenterX(), aro.getCenterY(),
				getDistX(angulo, this.diam), getDistY(angulo, this.diam));

		g2.draw(linha);

	}

	private void desenhaTexto(Graphics2D g2, Ellipse2D aro, int anguloMaior,
			int anguloMenor, String texto) {
		int angulo;
		if (anguloMaior > anguloMenor) {
			angulo = (anguloMaior - anguloMenor) / 2;
			angulo += anguloMenor;
		} else
			angulo = 0;
		g2.drawString(texto,
				getDistX(angulo, this.diam / 2) - 3 * texto.length(),
				getDistY(angulo, this.diam / 2));
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Ellipse2D aro = new Ellipse2D.Double((larg - diam) / 2,
				(alt - diam) / 2, diam, diam);

		g2.setPaint(Color.GREEN);
		g2.fill(aro);
		g2.draw(aro);

		g2.setPaint(Color.RED);
		desenhaLinha(g2, aro, angulo);

		g2.setPaint(Color.DARK_GRAY);
		for (int cont = 0, acumulador = 0; cont < anguloFinal.length; cont++) {
			if (anguloFinal[cont] != 360) {
				acumulador += anguloFinal[cont];
				if (setorRestrito
						&& ((cont == anguloFinal.length - 1) || (anguloFinal[cont + 1] == 360))) {
					Arc2D arco = new Arc2D.Double(aro.getBounds2D(),
							acumulador - 270, anguloFinal[cont], Arc2D.PIE);

					g2.setPaint(Color.GRAY);
					g2.fill(arco);
					g2.draw(arco);
				}
				if (acumulador == 360)
					acumulador = 0;
				desenhaLinha(g2, aro, acumulador);
				desenhaTexto(g2, aro, acumulador, acumulador
						- anguloFinal[cont],
						"Setor " + String.valueOf(cont + 1));
			}
		}

	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public void setAnguloFinal(int[] anguloFinal)
			throws SizeLimitExceededException {
		if (anguloFinal.length != 6)
			throw new SizeLimitExceededException(
					"Variavel angulo final deve ter 6 elementos");
		else {
			this.anguloFinal = anguloFinal;
		}
	}

	public void setSetorRestrito(boolean value) {
		this.setorRestrito = value;
	}
}
