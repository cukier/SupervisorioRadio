package cuki.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cuki.utils.PollDevice;
import net.miginfocom.swing.MigLayout;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;

@SuppressWarnings("serial")
public class LaminaSetter extends JDialog implements ActionListener {

	private PollDevice poller;

	private final JPanel contentPanel;
	private JComboBox<Float> comboLamina;
	// private JButton btnSalvar;

	private Float[] laminaArray;

	// private int[] laminaSetores;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LaminaSetter dialog = new LaminaSetter(4, 3, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LaminaSetter(int nrSetores, float laminaMinima, PollDevice poller) {

		this.poller = poller;

		// laminaSetores = new int[6];

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout());
		setContentPane(contentPanel);

		float passo = 0.5f;
		laminaArray = new Float[(int) (laminaMinima * 10 / passo) + 1];
		float lamina = laminaMinima;
		for (int cont = 0; cont <= (int) (laminaMinima * 10 / passo); cont++, lamina += passo) {
			laminaArray[cont] = lamina;
		}

		for (int cont = 1; cont <= nrSetores; cont++) {

			JLabel laminaLabel = new JLabel("L�mina Setor " + cont + " : ");
			contentPanel.add(laminaLabel);

			comboLamina = new JComboBox<Float>(laminaArray);
			comboLamina.setName(String.valueOf(cont));
			comboLamina.addActionListener(this);
			contentPanel.add(comboLamina, "wrap");
		}

		// btnSalvar = new JButton("Salvar");
		// btnSalvar.addActionListener(this);
		// contentPanel.add(btnSalvar, "span 2 1,alignx right");

		pack();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().getSimpleName().equals("JComboBox")) {

			JComboBox<Float> cb = (JComboBox<Float>) e.getSource();

			int lamina = (int) (float) cb.getSelectedItem();
			int index = Integer.valueOf(cb.getName());
			try {
				poller.setLamina(lamina, index);
			} catch (ModbusIOException e1) {
				e1.printStackTrace();
			} catch (ModbusSlaveException e1) {
				e1.printStackTrace();
			} catch (ModbusException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// System.out.println(lamina + " : " + index);

			// laminaSetores[Integer.valueOf(cb.getName()) - 1] = (int) (float)
			// cb
			// .getSelectedItem();

			// for (int aux : laminaSetores)
			// System.out.print(aux + " ");
			// System.out.println("");
		} else {
			// poller.setLamina(lamina, setor);
		}

	}
}
