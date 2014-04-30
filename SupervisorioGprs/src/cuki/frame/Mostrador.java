package cuki.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class Mostrador extends JPanel {

	private int larg = 350;
	private int alt = 300;
	private JLabel estado = null;
	private JLabel setor = null;
	private JLabel fase = null;
	private JLabel bruta = null;
	private JLabel duracao = null;
	private JLabel ciclo = null;
	private JLabel qualidade = null;
	private JButton bto = null;
	private Sinal sinal = null;
	private final Font font = new Font("Helvetica Neue Light", Font.PLAIN, 20);

	public Mostrador() {

		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("bottom:default:grow"), }));

		qualidade = new JLabel("Sinal do Modem: ");
		qualidade.setFont(font);
		add(qualidade, "2, 2, center, default");

		sinal = new Sinal(Color.RED);
		add(sinal, "2, 2, right, default");

		estado = new JLabel("Estado: ");
		estado.setFont(font);
		add(estado, "2, 4, center, default");

		setor = new JLabel("Setor: ");
		setor.setFont(font);
		add(setor, "2, 6, center, default");

		fase = new JLabel("Fase: ");
		fase.setFont(font);
		add(fase, "2, 8, center, default");

		bruta = new JLabel("Bruta: ");
		bruta.setFont(font);
		add(bruta, "2, 10, center, default");

		duracao = new JLabel("Duração: ");
		duracao.setFont(font);
		add(duracao, "2, 12, center, default");

		ciclo = new JLabel("Ciclo: ");
		ciclo.setFont(font);
		add(ciclo, "2, 14, center, default");

		bto = new JButton("Não Conectado");
		bto.setFont(font);
		add(bto, "2, 16, center, default");

		setBorder(BorderFactory.createTitledBorder("Situação Atual do Pivô"));
		setForeground(Color.WHITE);
		setPreferredSize(new Dimension(larg, alt));
	}

	public void setEstado(int estado) {
		String str = "Não conectado";

		switch (estado) {
		case 0:
			str = "Parado";
			this.estado.setForeground(Color.RED);
			bto.setText("Iniciar Irrigação");
			break;
		case 1:
			str = "Motobomba";
			this.estado.setForeground(Color.GREEN);
			break;
		case 2:
			str = "Pressurizando";
			this.estado.setForeground(Color.GREEN);
			break;
		case 3:
			str = "Irrigando";
			this.estado.setForeground(Color.GREEN);
			break;
		case 4:
			str = "Pico energético";
			this.estado.setForeground(Color.RED);
			break;
		case 5:
			str = "ALARME ACIONADO!!!";
			this.estado.setForeground(Color.RED);
			break;
		case 7:
			str = "Horário de trabalho";
			this.estado.setForeground(Color.RED);
			bto.setText("Parar Irrigação");
			break;
		case 8:
			str = "Irrigação terminada";
			this.estado.setForeground(Color.RED);
			break;
		case 9:
			str = "Pivô em movimento";
			this.estado.setForeground(Color.GREEN);
			break;
		default:
			str = "Parâmetro inválido";
			this.estado.setForeground(Color.RED);
		}

		this.estado.setText("Estado: " + str);
		repaint();
	}

	public void setSetor(int setorAtual, int nrSetores) {
		this.setor.setText("Setor: " + String.valueOf(setorAtual) + "/"
				+ String.valueOf(nrSetores));
	}

	public void setFase(int faseAtual, int nrFases) {
		this.fase.setText("Fase: " + String.valueOf(faseAtual) + "/"
				+ String.valueOf(nrFases));
	}

	public void setBruta(int bruta) {
		this.bruta.setText("Bruta: " + String.valueOf(bruta) + " mm");
	}

	public void setDuracao(int duracaoH, int duracaoM) {
		this.duracao.setText("Duracação: " + String.valueOf(duracaoH) + "h"
				+ String.valueOf(duracaoM));
	}

	public void setCiclo(int ciclo) {
		this.ciclo.setText("Ciclo: " + String.valueOf(ciclo));
	}

	public void setBto(String bto) {
		this.bto.setText(bto);
	}

	public JButton getBto() {
		return this.bto;
	}

	public void setQualidade(float iSinal, int porcento) {

		if (porcento > 33)
			porcento = 33;

		float aux = (float) porcento / 100;

		Color cor = Color.RED;

		if (iSinal > (1 - aux)) {
			cor = Color.GREEN;
		} else if (iSinal > aux && iSinal <= (1 - aux)) {
			cor = Color.YELLOW;
		} else if (iSinal <= aux) {
			cor = Color.RED;
		}

		setCor(cor);
		this.qualidade.setText("Sinal do Modem: " + String.valueOf(iSinal));
	}

	public void setCor(Color cor) {
		sinal.setCor(cor);
		sinal.repaint();
	}

	public Sinal getSinal() {
		return this.sinal;
	}
}
