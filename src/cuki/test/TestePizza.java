package cuki.test;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import cuki.frame.Pizza;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TestePizza extends JFrame implements ActionListener {

	JPanel contentPane = new JPanel();
	Pizza pizza;
	private final JLabel lblNewLabel = new JLabel("Tamanho");
	private final JTextField tamanhoField = new JTextField();
	private final JLabel lblTamanho = new JLabel("Indice");
	private final JTextField indiceField = new JTextField();
	private final JLabel lblNrSetores = new JLabel("Nr Setores");
	private final JTextField nrSetoresField = new JTextField();
	private ArrayList<Integer> anguloFinalList = new ArrayList<Integer>(
			Arrays.asList(60, 60, 60, 60, 60, 60));
	private int indiceSetor = 0;
	private final JLabel lblArrayangulofinal = new JLabel("arrayAnguloFinal");

	public static void main(String[] args) {

		TestePizza frame = new TestePizza();

		frame.setVisible(true);

	}

	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		Iterator<Integer> iterator = integers.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next().intValue();
		}
		return ret;
	}

	public TestePizza() {

		pizza = new Pizza();
		pizza.setAnguloFInalSetores(convertIntegers(anguloFinalList));

		nrSetoresField.addActionListener(this);
		indiceField.addActionListener(this);
		tamanhoField.addActionListener(this);
		nrSetoresField.setColumns(10);
		indiceField.setColumns(10);
		tamanhoField.setColumns(10);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(pizza);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

		panel.add(lblNewLabel, "cell 0 0,grow");

		panel.add(tamanhoField, "cell 1 0,grow");

		panel.add(lblTamanho, "cell 0 1,grow");

		panel.add(indiceField, "cell 1 1,grow");

		panel.add(lblNrSetores, "cell 0 2,alignx trailing,growy");

		panel.add(nrSetoresField, "cell 1 2,grow");

		panel.add(lblArrayangulofinal, "cell 0 3 2 1,alignx center,growy");

		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nrSetoresField) {
			pizza.setNrSetores(Integer.valueOf(nrSetoresField.getText()));
		} else if (e.getSource() == indiceField) {
			indiceSetor = Integer.valueOf(indiceField.getText());
		} else if (e.getSource() == tamanhoField) {
			anguloFinalList.set(indiceSetor,
					Integer.valueOf(tamanhoField.getText()));
			pizza.setAnguloFInalSetores(convertIntegers(anguloFinalList));
		}
		lblArrayangulofinal.setText(anguloFinalList.toString());
		repaint();
	}

}
