package cuki.frame;

import java.awt.ComponentOrientation;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import cuki.utils.BitField;
import net.miginfocom.swing.MigLayout;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class IO extends JFrame {

	private static final Dimension panelDimension = new Dimension(300, 300);
	private static final String desc = "Não Conectado";
	private static final String lig = "Ligado";
	private static final String des = "Desligado";
	private static final String sim = "Sim";
	private static final String nao = "Não";
	private static final String ac = "ACIONADO";
	// entradas
	private JLabel SYNC;
	private JLabel SEGURANCA;
	private JLabel PRESSOSTATO;
	private JLabel AUTOMATICO;
	private JLabel ZERO;
	// saidas
	private JLabel AVANCO;
	private JLabel REVERSAO;
	private JLabel BOOSTER;
	private JLabel B_INJETORA;
	private JLabel ULTIMA_TORRE;
	private JLabel BDAGUA;
	// variaveis
	private JLabel PICOENERGETICO;
	private JLabel HORARIOTRABALHO;
	private JLabel MOVSECO;
	private JLabel SETORRESTRITO;
	private JLabel EMESPERA;
	private JLabel TURNOREGA;
	private JLabel ALARMEALINHAMENTO;
	private JLabel ALARMEPRESSAO;

	public IO(String pivo) {

		MigLayout layout = new MigLayout("", "[grow,right][grow,left]",
				"[grow][grow][grow][grow][grow]");
		SYNC = new JLabel(desc);
		SEGURANCA = new JLabel(desc);
		PRESSOSTATO = new JLabel(desc);
		AUTOMATICO = new JLabel(desc);
		ZERO = new JLabel(desc);

		JPanel panel = new JPanel();
		panel.setPreferredSize(panelDimension);
		panel.setBorder(BorderFactory.createTitledBorder("Entradas do PLC"));
		panel.setLayout(layout);
		panel.add(new JLabel("Sync: "), "");
		panel.add(SYNC, "wrap");
		panel.add(new JLabel("Segurança: "));
		panel.add(SEGURANCA, "wrap");
		panel.add(new JLabel("Pressostato: "));
		panel.add(PRESSOSTATO, "wrap");
		panel.add(new JLabel("Automático: "));
		panel.add(AUTOMATICO, "wrap");
		panel.add(new JLabel("Zero Encoder: "));
		panel.add(ZERO);
		getContentPane().add(panel);

		layout = new MigLayout("", "[grow,right][grow,left]",
				"[grow][grow][grow][grow][grow][grow]");
		AVANCO = new JLabel(desc);
		REVERSAO = new JLabel(desc);
		BOOSTER = new JLabel(desc);
		B_INJETORA = new JLabel(desc);
		ULTIMA_TORRE = new JLabel(desc);
		BDAGUA = new JLabel(desc);

		panel = new JPanel();
		panel.setPreferredSize(panelDimension);
		panel.setBorder(BorderFactory.createTitledBorder("Saidas do PLC"));
		panel.setLayout(layout);
		panel.add(new JLabel("Avanço: "), "");
		panel.add(AVANCO, "wrap");
		panel.add(new JLabel("Revesão: "), "");
		panel.add(REVERSAO, "wrap");
		panel.add(new JLabel("Bomba Booster: "), "");
		panel.add(BOOSTER, "wrap");
		panel.add(new JLabel("Bomba Injetora: "), "");
		panel.add(B_INJETORA, "wrap");
		panel.add(new JLabel("Ultima Torre: "), "");
		panel.add(ULTIMA_TORRE, "wrap");
		panel.add(new JLabel("Bomba D'Água: "), "");
		panel.add(BDAGUA);
		getContentPane().add(panel);

		layout = new MigLayout("", "[grow,right][grow,left]",
				"[grow][grow][grow][grow][grow][grow][grow][grow]");
		PICOENERGETICO = new JLabel(desc);
		HORARIOTRABALHO = new JLabel(desc);
		MOVSECO = new JLabel(desc);
		SETORRESTRITO = new JLabel(desc);
		EMESPERA = new JLabel(desc);
		TURNOREGA = new JLabel(desc);
		ALARMEALINHAMENTO = new JLabel(desc);
		ALARMEPRESSAO = new JLabel(desc);

		panel = new JPanel();
		panel.setPreferredSize(panelDimension);
		panel.setBorder(BorderFactory.createTitledBorder("Variávei do PLC"));
		panel.setLayout(layout);
		panel.add(new JLabel("Pico Energético: "), "");
		panel.add(PICOENERGETICO, "wrap");
		panel.add(new JLabel("Horário de Trabalho: "), "");
		panel.add(HORARIOTRABALHO, "wrap");
		panel.add(new JLabel("Movimento a Seco: "), "");
		panel.add(MOVSECO, "wrap");
		panel.add(new JLabel("Último Setor Restrito: "), "");
		panel.add(SETORRESTRITO, "wrap");
		panel.add(new JLabel("Em Espera: "), "");
		panel.add(EMESPERA, "wrap");
		panel.add(new JLabel("Turno de Rega: "), "");
		panel.add(TURNOREGA, "wrap");
		panel.add(new JLabel("Alarme Segurança: "), "");
		panel.add(ALARMEALINHAMENTO, "wrap");
		panel.add(new JLabel("Alarme Pressão: "), "");
		panel.add(ALARMEPRESSAO);
		getContentPane().add(panel);

		FlowLayout flowlayout = new FlowLayout();

		getContentPane().setLayout(flowlayout);
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Entras / Saidas " + pivo);
		pack();
		setVisible(true);
	}

	public void setBytes(int iWord0, int iWord4, int iWord6) {

		BitField word0 = new BitField(iWord0);

		AUTOMATICO.setText(word0.getBit(BitField.saidaAutomatico) ? lig : des);
		PICOENERGETICO.setText(word0.getBit(BitField.picoEnergetico) ? sim
				: nao);
		HORARIOTRABALHO.setText(word0.getBit(BitField.picoEnergetico) ? sim
				: nao);
		MOVSECO.setText(word0.getBit(BitField.partidaSeca) ? sim : nao);
		BOOSTER.setText(word0.getBit(BitField.bBoosterOk) ? lig : des);
		BDAGUA.setText(word0.getBit(BitField.saidaBDagua) ? lig : des);

		BitField word4 = new BitField(iWord4);

		AVANCO.setText(word4.getBit(BitField.sAvanco) ? lig : des);
		REVERSAO.setText(word4.getBit(BitField.sReversao) ? lig : des);
		ULTIMA_TORRE.setText(word4.getBit(BitField.sUltimaTorre) ? lig : des);
		B_INJETORA.setText(word4.getBit(BitField.sBombaInjetora) ? lig : des);
		SEGURANCA.setText(word4.getBit(BitField.saidaSeguranca) ? lig : des);
		SETORRESTRITO.setText(word4.getBit(BitField.ultimoSetorRestrito) ? lig
				: des);
		PRESSOSTATO.setText(word4.getBit(BitField.pressostatoOk) ? sim : nao);

		BitField word6 = new BitField(iWord6);

		EMESPERA.setText(word6.getBit(BitField.emEspera) ? sim : nao);
		TURNOREGA.setText(word6.getBit(BitField.turnoRegaOk) ? sim : nao);
		ALARMEALINHAMENTO.setText(word6.getBit(BitField.alarmeAlinhamento) ? ac
				: des);
		ALARMEPRESSAO.setText(word6.getBit(BitField.alarmePressao) ? ac : des);
	}
}
