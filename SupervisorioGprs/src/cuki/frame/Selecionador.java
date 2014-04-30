package cuki.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.SynchWriteException;
import net.miginfocom.swing.MigLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import cuki.opc.ServidorOPC;

@SuppressWarnings("serial")
public class Selecionador extends JFrame {

	private JButton btnAbrirpivo;
	private Status[] listaStatusPivo;
	private boolean finalizarPrograma = false;
	private String[] pivos;
	private int cont;
	private ServidorOPC servidorOPC;

	public Selecionador(String[] servidores, ServidorOPC servidorOPC) {

		this.pivos = servidores;
		this.listaStatusPivo = new Status[servidores.length];
		this.servidorOPC = servidorOPC;

		if (servidores.length > 0 && servidores != null) {
			getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));
			cont = 0;
			for (String servidor : servidores) {
				btnAbrirpivo = new JButton(servidor);
				btnAbrirpivo.setIcon(new ImageIcon(Selecionador.class
						.getResource("/cuki/frame/images/pivoIcon.jpg")));
				getContentPane().add(btnAbrirpivo, "wrap");
				btnAbrirpivo.addActionListener(new BtnListener(servidor));
			}
		}
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				finalizarPrograma = true;
				dispose();
			}
		});
		setTitle("Krebs 4001 - Acesso Remoto");
		pack();
	}

	public Status getStatusPivo(String pivo) {
		for (Status retorno : listaStatusPivo) {
			if (retorno != null && retorno.getPivoName().equals(pivo))
				return retorno;
		}
		return null;
	}

	public Status[] getStatusPivo() {
		return this.listaStatusPivo;
	}

	private class BtnListener implements ActionListener {

		private String pivo;

		public BtnListener(String pivo) {
			this.pivo = pivo;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Status status : listaStatusPivo) {
				if (status == null) {
					Status statusPivo = new Status(pivo);
					statusPivo.setLocation(getWidth() + statusPivo.getCont()
							* 30, statusPivo.getCont() * 30);
					statusPivo.getAnguloAtual().getBto()
							.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									try {
										servidorOPC.reverteSentido(pivo);
									} catch (ComponentNotFoundException e1) {
										e1.printStackTrace();
									} catch (SynchWriteException e1) {
										e1.printStackTrace();
									} catch (SynchReadException e1) {
										e1.printStackTrace();
									}

								}
							});
					statusPivo.getMostrador().getBto()
							.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									int estado = getStatusPivo(pivo)
											.getEstado();
									switch (estado) {
									case 0:
									case 5:
									case 8:
										try {
											servidorOPC.iniciaIrrigacao(pivo);
										} catch (ComponentNotFoundException e1) {
											e1.printStackTrace();
										} catch (SynchWriteException e1) {
											e1.printStackTrace();
										} catch (SynchReadException e1) {
											e1.printStackTrace();
										}
										System.out
												.println("Irrigacao Iniciada");
										break;
									case 1:
									case 2:
									case 3:
									case 4:
									case 7:
									case 9:
										try {
											servidorOPC.pararIrrigacao(pivo);
										} catch (ComponentNotFoundException e1) {
											e1.printStackTrace();
										} catch (SynchReadException e1) {
											e1.printStackTrace();
										} catch (SynchWriteException e1) {
											e1.printStackTrace();
										}
										System.out.println("Irrigacao Parada");
										break;
									default:
										break;
									}
								}
							});
					listaStatusPivo[cont++] = statusPivo;
					statusPivo.setVisible(true);
					break;
				} else if (status.getPivoName().equals(pivo)) {
					status.setVisible(true);
					break;
				}
			}
		}
	}

	public boolean isFinalizarPrograma() {
		return finalizarPrograma;
	}

	public String[] getPivos() {
		return this.pivos;
	}
}
