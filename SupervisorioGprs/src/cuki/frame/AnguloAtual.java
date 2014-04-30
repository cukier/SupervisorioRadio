package cuki.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AnguloAtual extends JPanel {

	private int larg = 240;
	private int alt = 60;
	private JLabel lblAngulo;
	private JButton BtoSentido;

	public AnguloAtual() {

		setLayout(new MigLayout("", "[46px,grow][95px,grow]", "[27px,grow]"));

		lblAngulo = new JLabel(" °");
		lblAngulo.setFont(new Font("Helvetica Neue Light", Font.PLAIN, 20));
		add(lblAngulo, "cell 0 0,alignx center,aligny center");

		BtoSentido = new JButton("Sentido",
				new ImageIcon(
						AnguloAtual.class
								.getResource("/cuki/frame/images/horario.jpg")));
		add(BtoSentido, "cell 1 0,alignx center,aligny center");
		setBorder(BorderFactory.createTitledBorder("Angulo Atual"));
		setForeground(Color.WHITE);
		setPreferredSize(new Dimension(larg, alt));

	}

	public void setAngulo(int angulo) {
		lblAngulo.setText(String.valueOf(angulo) + "°");
	}

	public void setSentido(Boolean sentido) {
		if (sentido) {
			// BtoSentido.setText("Sentido Horário");
			BtoSentido.setIcon(new ImageIcon(Status.class
					.getResource("/cuki/frame/images/horario.jpg")));
		} else {
			// BtoSentido.setText("Sentido Anti-Horário");
			BtoSentido.setIcon(new ImageIcon(Status.class
					.getResource("/cuki/frame/images/antihorario.jpg")));
		}
	}

	public JButton getBto() {
		return this.BtoSentido;
	}
}
