package cuki.bin;

import java.awt.Color;

import javax.naming.SizeLimitExceededException;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import javafish.clients.opc.exception.CoUninitializeException;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.exception.UnableBrowseBranchException;
import javafish.clients.opc.exception.UnableIBrowseException;
import javafish.clients.opc.exception.UnableRemoveGroupException;
import javafish.clients.opc.exception.VariantTypeException;
import cuki.exception.NoPivotFoundException;
import cuki.frame.Selecionador;
import cuki.frame.Status;
import cuki.opc.ServidorOPC;

public class Pivo {

	private Selecionador selecionador = null;
	private ServidorOPC atos;
	private static final int EXIT_SUCESS = 0;

	public Pivo() throws NoPivotFoundException {

		atos = new ServidorOPC();

		try {
			atos.connectAndRegister();
		} catch (UnableAddGroupException e) {
			e.printStackTrace();
		} catch (UnableAddItemException e) {
			e.printStackTrace();
		} catch (UnableBrowseBranchException e) {
			e.printStackTrace();
		} catch (UnableIBrowseException e) {
			e.printStackTrace();
		} catch (ConnectivityException e) {
			e.printStackTrace();
		} catch (CoUninitializeException e) {
			e.printStackTrace();
		}

		String[] servidores = atos.getServidores();

		if (servidores.length > 0) {
			selecionador = new Selecionador(servidores, atos);
		} else {
			System.out
					.println("Não foi encontrando nenhum pivo. Foi instalado o servidor da ATOS?");
			throw new NoPivotFoundException("");
		}
	}

	private void loop() throws InterruptedException {

		while (true) {

			Thread.sleep(1000);

			if (selecionador.isFinalizarPrograma()) {
				System.out.println("Terminando Programa");
				return;
			}

			for (String pivo : selecionador.getPivos()) {
				Status status = selecionador.getStatusPivo(pivo);
				if (status != null) {
					try {
						atos.syncItens();

						Color cor = status.getMostrador().getSinal().getCor();
						status.getMostrador().setCor(
								status.getContentPane().getBackground());
						Thread.sleep(250);
						status.getMostrador().setCor(cor);

						status.getMostrador().setQualidade(
								atos.getSignal(pivo), 33);
						status.getOval().setAnguloFinal(
								atos.getAnguloSetor(pivo));
						status.setAngulo(atos.getanguloAtual(pivo));
						status.setword(atos.getword0(pivo),
								atos.getword4(pivo), atos.getword6(pivo));
						status.setAngulo(atos.getanguloAtual(pivo));
						status.setEstado(atos.getstatusPivo(pivo));
						status.getMostrador().setSetor(
								atos.getcontaSetor(pivo),
								atos.getnrSetores(pivo));
						status.getMostrador().setFase(atos.getcontaFase(pivo),
								atos.getnrFases(pivo));
						status.getMostrador().setBruta(atos.getlaminaGet(pivo));
						status.getMostrador().setDuracao(
								atos.gettempoRestanteHoras(pivo),
								atos.getTempoRestanteMinutos(pivo));
						status.getMostrador()
								.setCiclo(atos.getcicloAtual(pivo));
					} catch (ComponentNotFoundException e) {
						e.printStackTrace();
					} catch (VariantTypeException e) {
						e.printStackTrace();
					} catch (SynchReadException e) {
						e.printStackTrace();
					} catch (SizeLimitExceededException e) {
						e.printStackTrace();
					}
					status.repaint();
				}
			}
		}
	}

	public static void main(String[] args) throws NoPivotFoundException {

		Pivo lancador;

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}

		lancador = new Pivo();
		lancador.selecionador.setVisible(true);

		try {
			lancador.loop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			lancador.atos.disconnect();
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
		} catch (UnableRemoveGroupException e) {
			e.printStackTrace();
		}

		lancador.atos = null;
		lancador.selecionador = null;

		System.exit(Pivo.EXIT_SUCESS);
	}
}
