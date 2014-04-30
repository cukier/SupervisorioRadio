package cuki;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
//import cuki.OpcConn;

@SuppressWarnings("serial")
public class Clock extends Applet implements Runnable {

	private volatile Thread timer;
	private int diam = 300;
	private Dimension windowSize = new Dimension(diam + 20, diam + 20);
	private Ellipse2D aro = new Ellipse2D.Double(
			(windowSize.getWidth() - diam) / 2,
			(windowSize.getHeight() - diam) / 2, diam, diam);
	private Point2D origem = new Point2D.Double(0, 0);
	private Point2D dest = new Point2D.Double(0, 0);
	private Point2D centro = new Point2D.Double(aro.getCenterX(),
			aro.getCenterY());
	private Line2D linha = new Line2D.Double(origem, centro);
	private double x = 0D;
	private double y = 0D;
	private int angulo = 0;
//	private OpcConn conector;

	public void init() {
		setSize(windowSize);
	}

	public void update(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(getBackground());
		g2.draw(linha);

		if (angulo == 360)
			angulo = 0;

		x = Math.cos(angulo * Math.PI / 180 - Math.PI / 2) * diam / 2
				+ centro.getX();
		y = Math.sin(angulo * Math.PI / 180 - Math.PI / 2) * diam / 2
				+ centro.getY();

		dest.setLocation(x, y);

		linha.setLine(centro, dest);
		g2.setColor(Color.BLACK);
		g2.draw(linha);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.draw(aro);
	}

	public void start() {
		timer = new Thread(this);
		timer.start();
	}

	public void stop() {
		timer = null;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public void run() {
		Thread me = Thread.currentThread();
		while (timer == me) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			repaint();
		}
	}
}
