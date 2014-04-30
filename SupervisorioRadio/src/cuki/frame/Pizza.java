package cuki.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Pizza extends JPanel {

	private int anguloAtual = 0;
	private int nrSetores = 1;
	private boolean setorRestrito = false;
	private int[] anguloFinalSetores = { 60, 120, 180, 240, 300, 360 };
	private Dimension pivoSize = new Dimension(300, 300);
	private String pivoName = "Pivo Krebsfer";
	private Arc2D arco = null;

	public Pizza() {
		setPreferredSize(pivoSize);
		if (nrSetores == 1)
			setorRestrito = false;
	}

	private int getDistX(int angulo) {
		return (int) ((this.pivoSize.width + this.pivoSize.width
				* Math.sin(angulo * Math.PI / 180)) / 2);
	}

	private int getDistY(int angulo) {
		return (int) ((this.pivoSize.height - this.pivoSize.height
				* Math.cos(angulo * Math.PI / 180)) / 2);
	}

	private Line2D getLinha(Graphics2D g2, Ellipse2D aro, int angulo) {

		return new Line2D.Double(aro.getCenterX(), aro.getCenterY(),
				getDistX(angulo), getDistY(angulo));

	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Ellipse2D aro = new Ellipse2D.Double(0, 0, pivoSize.width,
				pivoSize.height);
		g2.setColor(new Color(0, 128, 128));
		// g2.fill(aro);
		g2.draw(aro);

		if (nrSetores > 1) {
			int acumulo = 0;
			for (int cont = 0; cont < nrSetores; cont++) {

				int tamanho = anguloFinalSetores[cont];

				arco = new Arc2D.Double(aro.getBounds2D(), 90 - acumulo, -1
						* tamanho, Arc2D.PIE);
				acumulo += anguloFinalSetores[cont];

				switch (cont) {
				case 0:
					g2.setPaint(new Color(123, 96, 90));
					break;
				case 1:
					g2.setPaint(new Color(39, 160, 90));
					break;
				case 2:
					g2.setPaint(new Color(126, 64, 128));
					break;
				case 3:
					g2.setPaint(new Color(159, 131, 32));
					break;
				case 4:
					g2.setPaint(new Color(66, 103, 125));
					break;
				case 5:
					g2.setPaint(new Color(165, 44, 27));
					break;
				}

				if (setorRestrito && cont == nrSetores - 1)
					g2.setPaint(Color.BLACK);
				g2.fill(arco);
				g2.draw(arco);
			}
		}
		g2.setColor(Color.RED);
		g2.draw(getLinha(g2, aro, this.anguloAtual));
	}

	public String getPivoName() {
		return this.pivoName;
	}

	public void setPivoName(String name) {
		this.pivoName = name;
	}

	public void setAnguloFInalSetores(int[] value) {
		this.anguloFinalSetores = value;
	}

	public void setAnguloAtual(int value) {
		this.anguloAtual = value;
	}

	public void setSetorRestrito(boolean value) {
		this.setorRestrito = value;
	}

	public void setNrSetores(int value) {
		if (value >= 1 && value <= 6)
			this.nrSetores = value;
		else
			this.nrSetores = 1;
	}

	public void setPieType(int value) {
		arco.setArcType(value);
	}

	public void nrSetores(Integer valueOf) {
		this.nrSetores = valueOf;
	}

}
