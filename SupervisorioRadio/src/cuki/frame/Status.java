package cuki.frame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JMenuBar;

import cuki.to.ActionHandler;
import cuki.utils.IconSet;

import javax.swing.JLabel;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class Status extends JFrame {

	private JPanel contentPane;
	private Pizza pizza;
	private Mostrador mostrador;
	private JMenuBar menuBar;
	private JPanel pizzaPanel;

	public JButton configCon;
	public JButton connect;
	public JButton disconnect;
	public JButton pooling;
	public JButton database;
	public JButton btnStart;
	public JButton btnSentdido;
	public JButton btnLamina;

	private JPanel panelAnguloSentido;
	private JPanel panel_2;
	private JLabel lblAnguloAtualValor;
	private JPanel panel;

	public Status() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Supervisório Pivô Krebsfer");
		setBounds(100, 100, 450, 300);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		configCon = new JButton(IconSet.getConfigIcon());
		menuBar.add(configCon);

		connect = new JButton(IconSet.getConnectIcon());
		menuBar.add(connect);

		disconnect = new JButton(IconSet.getDisconnectIcon());
		menuBar.add(disconnect);

		pooling = new JButton(IconSet.getPoolingIcon());
		pooling.setVisible(false);
		menuBar.add(pooling);

		database = new JButton(IconSet.getDatabaseIcon());
		database.setVisible(false);
		menuBar.add(database);

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[322px,grow][grow]",
				"[332px,grow][grow]"));

		pizzaPanel = new JPanel();
		pizza = new Pizza();

		pizzaPanel.setBorder(new TitledBorder(null, pizza.getPivoName(),
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pizzaPanel.add(pizza);
		contentPane.add(pizzaPanel, "cell 0 0,alignx left,aligny top");

		btnLamina = new JButton();
		btnLamina.setEnabled(false);

		mostrador = new Mostrador(btnLamina);
		contentPane.add(mostrador, "cell 1 0,grow");

		panelAnguloSentido = new JPanel();
		contentPane.add(panelAnguloSentido, "cell 0 1,growx,aligny center");
		panelAnguloSentido.setLayout(new MigLayout("", "[213px][213px]",
				"[46px,grow]"));

		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "AnguloAtual",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAnguloSentido.add(panel_2, "cell 0 0,grow");

		lblAnguloAtualValor = new JLabel("0");
		panel_2.add(lblAnguloAtualValor);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Sentido", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelAnguloSentido.add(panel, "cell 1 0,grow");
		panel.setLayout(new BorderLayout(0, 0));

		btnSentdido = new JButton("Sentdio Hor\u00E1rio");
		btnSentdido.setEnabled(false);
		panel.add(btnSentdido);

		btnStart = new JButton("Lendo pivô...");
		contentPane.add(btnStart, "cell 1 1,grow");

		setConnectionStatus(false);

		pack();
	}

	public void setConnectionStatus(boolean isConected) {
		configCon.setEnabled(!isConected);
		connect.setEnabled(!isConected);
		disconnect.setEnabled(isConected);
		pooling.setEnabled(isConected);
		database.setEnabled(!isConected);
		mostrador.setVisible(isConected);
		pizzaPanel.setVisible(isConected);
		panelAnguloSentido.setVisible(isConected);
		btnStart.setVisible(isConected);
	}

	public void setAction(ActionHandler actionHandler) {
		configCon.addActionListener(actionHandler);
		connect.addActionListener(actionHandler);
		disconnect.addActionListener(actionHandler);
		pooling.addActionListener(actionHandler);
		database.addActionListener(actionHandler);
		btnStart.addActionListener(actionHandler);
		btnSentdido.addActionListener(actionHandler);
		btnLamina.addActionListener(actionHandler);
	}

	public void updateStatus(int anguloAtual, boolean sentido, int estado,
			int setorAtual, int setores, int fase, int fases, int lamina,
			int duracaoH, int duracaoM, int cicloAtual, int[] anguloSetores) {

		pizza.setAnguloAtual(anguloAtual);
		lblAnguloAtualValor.setText(String.valueOf(anguloAtual) + "°");
		btnSentdido.setText(sentido ? "Avanço" : "Reversão");
		mostrador.setEstado(estado);
		switch (estado) {
		case 0: //parado
		case 5: // alarmeSeguraca
			btnStart.setText("Iniciar Irrigação");
			btnSentdido.setEnabled(true);
			btnLamina.setEnabled(true);
			break;
		default:
		case 7: //irrigando
			btnStart.setText("Parar Irrigação");
			btnSentdido.setEnabled(false);
			btnLamina.setEnabled(false);
			break;
		}
		mostrador.setSetor(setorAtual, setores);
		pizza.setNrSetores(setores);
		mostrador.setFase(fase, fases);
		mostrador.setBruta(lamina);
		mostrador.setDuracao(duracaoH, duracaoM);
		mostrador.setCiclo(cicloAtual);
		pizza.setAnguloFInalSetores(anguloSetores);

		repaint();
	}
}
