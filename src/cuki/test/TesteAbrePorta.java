package cuki.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import cuki.utils.ConnectionModbus;

@SuppressWarnings("serial")
public class TesteAbrePorta extends JFrame implements ActionListener {

	ConnectionModbus conManager;

	JButton btnAbrir;
	JButton btnFechar;
	JTextField conName;
	String doorName = "COM1";

	public TesteAbrePorta() {

		conManager = new ConnectionModbus(doorName);

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);

		contentPane.setLayout(new MigLayout());

		btnAbrir = new JButton("Abrir");
		btnAbrir.addActionListener(this);
		contentPane.add(btnAbrir);

		btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(this);
		contentPane.add(btnFechar, "wrap");

		JLabel lblPorName = new JLabel("Porta: ");
		contentPane.add(lblPorName);

		conName = new JTextField();
		conName.setColumns(5);
		conName.addActionListener(this);
		contentPane.add(conName);

		pack();

	}

	public void setConName(String conName) {
		conManager.setPortName(conName);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnAbrir) {

			// conManager = new ConnectionModbus(doorName);
			if (!conManager.isDoorOpen()) {
				try {
					conManager.open();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else
				System.out.println("Porta " + conManager.getPortName()
						+ " aberta");
		} else if (e.getSource() == btnFechar) {
			conManager.close();

		} else if (e.getSource() == conName) {
			if (conManager.isDoorOpen())
				conManager.close();
			conManager.setPortName(conName.getText());
			this.doorName = conName.getText();
		}

		if (conManager.isDoorOpen())
			System.out.println(conManager.getPortName() + " aberta");
		else
			System.out.println(conManager.getPortName() + " fechada");

	}

	public static void main(String[] args) {
		JFrame frame = new TesteAbrePorta();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
