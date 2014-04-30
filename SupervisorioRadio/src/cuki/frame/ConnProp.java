package cuki.frame;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JComboBox;

import bin.Pivo;
import cuki.utils.IconSet;
import cuki.utils.PortLister;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ConnProp extends JDialog implements ActionListener {

	private JPanel contentPane;
	@SuppressWarnings("rawtypes")
	JComboBox comboPortName = null;
	@SuppressWarnings("rawtypes")
	JComboBox comboBaudRate = null;
	@SuppressWarnings("rawtypes")
	JComboBox comboParity = null;
	@SuppressWarnings("rawtypes")
	JComboBox comboDataBits = null;
	@SuppressWarnings("rawtypes")
	JComboBox comboStopBits = null;
	JButton btnSalvar = null;
	JButton btnCancelar = null;
	private String[] connPropeties = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnProp frame = new ConnProp("COM1", "9600", "Nenhum",
							"5", "2");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings("null")
	private int getIndex(String[] array, String str) {
		int cont = 0;
		for (String aux : array) {
			if (aux.equals(str))
				return cont;
			cont++;
		}
		return (Integer) null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ConnProp(String _portName, String _baudRate, String _parity,
			String _dataBits, String _stopBits) {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Parametros de Conexão");
		setLocation(100, 100);
		setResizable(false);

		connPropeties = new String[5];
		connPropeties[0] = _portName;
		connPropeties[1] = _baudRate;
		connPropeties[2] = _parity;
		connPropeties[3] = _dataBits;
		connPropeties[4] = _stopBits;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("",
				"[pref!,grow,center][pref!,grow,center]",
				"[center][center][][][][][grow]"));

		JLabel portNameLabel = new JLabel("Porta :");
		contentPane.add(portNameLabel, "cell 0 0,alignx trailing");

		String[] ports = PortLister.getPorts();
		if (connPropeties[0].equals(""))
			connPropeties[0] = ports[0];
		comboPortName = new JComboBox(ports);
		try {
			comboPortName.setSelectedIndex(getIndex(ports, _portName));
		} catch (NullPointerException e) {
		}
		comboPortName.setEditable(true);
		comboPortName.addActionListener(this);
		contentPane.add(comboPortName, "cell 1 0,growx,aligny center");

		JLabel lblBaudRate = new JLabel("Velocidade :");
		contentPane.add(lblBaudRate, "cell 0 1,alignx trailing");

		String[] baudRates = { "1200", "9600", "19200", "38400", "57600",
				"115200" };
		comboBaudRate = new JComboBox(baudRates);
		try {
			comboBaudRate.setSelectedIndex(getIndex(baudRates, _baudRate));
		} catch (NullPointerException e) {
		}
		comboBaudRate.setEditable(true);
		contentPane.add(comboBaudRate, "cell 1 1,growx,aligny center");
		comboBaudRate.addActionListener(this);

		JLabel lblParity = new JLabel("Paridade :");
		contentPane.add(lblParity, "cell 0 2,alignx trailing");

		String[] parity = { "Nenhum", "Par", "Ímpar" };
		comboParity = new JComboBox(parity);
		try {
			comboParity.setSelectedIndex(getIndex(parity, _parity));
		} catch (NullPointerException e) {
		}
		contentPane.add(comboParity, "cell 1 2,growx,aligny center");
		comboParity.addActionListener(this);

		JLabel lblStopBits = new JLabel("Bits de parada :");
		contentPane.add(lblStopBits, "cell 0 3,alignx trailing");

		String[] stopBits = { "1", "2" };
		comboStopBits = new JComboBox(stopBits);
		try {
			comboStopBits.setSelectedIndex(getIndex(stopBits, _stopBits));
		} catch (NullPointerException e) {
		}
		contentPane.add(comboStopBits, "cell 1 3,growx,aligny center");
		comboStopBits.addActionListener(this);

		JLabel lblDataBits = new JLabel("Bits de dado :");
		contentPane.add(lblDataBits, "cell 0 4,alignx trailing");

		String[] dataBits = { "8", "7", "6", "5" };
		comboDataBits = new JComboBox(dataBits);
		try {
			comboDataBits.setSelectedIndex(getIndex(dataBits, _dataBits));
		} catch (NullPointerException e) {
		}
		contentPane.add(comboDataBits, "cell 1 4,growx,aligny center");
		comboDataBits.addActionListener(this);

		btnSalvar = new JButton(IconSet.getSaveIcon());
		contentPane.add(btnSalvar, "cell 0 5");
		btnSalvar.setText(IconSet.getSaveIcon().getDescription());
		btnSalvar.addActionListener(this);

		btnCancelar = new JButton(IconSet.getCancelIcon());
		contentPane.add(btnCancelar, "cell 1 5");
		btnCancelar.setText(IconSet.getCancelIcon().getDescription());
		btnCancelar.addActionListener(this);

		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().equals(JButton.class)) {
			if (e.getSource() == btnCancelar) {
				dispose();
			} else if (e.getSource() == btnSalvar) {
				Pivo.setConnection(connPropeties);
				dispose();
			}
		} else if (e.getSource().getClass().equals(JComboBox.class)) {

			@SuppressWarnings("rawtypes")
			JComboBox cb = (JComboBox) e.getSource();

			if (e.getSource() == comboBaudRate) {
				connPropeties[1] = (String) cb.getSelectedItem();
				System.out.println(connPropeties[1]);
			} else if (e.getSource() == comboDataBits) {
				connPropeties[3] = (String) cb.getSelectedItem();
			} else if (e.getSource() == comboParity) {
				connPropeties[2] = (String) cb.getSelectedItem();
			} else if (e.getSource() == comboPortName) {
				connPropeties[0] = (String) cb.getSelectedItem();
			} else if (e.getSource() == comboStopBits) {
				connPropeties[4] = (String) cb.getSelectedItem();
			}
		}
	}
}
