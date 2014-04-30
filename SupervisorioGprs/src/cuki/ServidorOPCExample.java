package cuki;

import java.util.Arrays;

import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.SynchWriteException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.exception.UnableBrowseBranchException;
import javafish.clients.opc.exception.UnableIBrowseException;
import javafish.clients.opc.exception.UnableRemoveGroupException;
import cuki.opc.ServidorOPC;

public class ServidorOPCExample {

	private static void loop(ServidorOPC atos, String pivo)
			throws InterruptedException {
		while (true) {
			Thread.sleep(1000);

			try {
				atos.syncItens();
			} catch (ComponentNotFoundException e) {
				e.printStackTrace();
			} catch (SynchReadException e) {
				e.printStackTrace();
			}

			System.out.println("Verificando sinal");
			float sinal = atos.getSignal(pivo);

			if (sinal > 0.66)
				break;
			else
				System.out.println(sinal);
		}
	}

	public static void main(String[] args) throws InterruptedException {

		ServidorOPC atos = new ServidorOPC();
		String pivo = null;
		final int SERVIDOR_NOT_FOUND = 1;

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
		}

		String[] servidores = null;
		servidores = atos.getServidores();

		System.out.println(Arrays.asList(servidores));

		if (servidores == null || servidores.length > 1) {
			try {
				atos.disconnect();
			} catch (ComponentNotFoundException e) {
				e.printStackTrace();
			} catch (UnableRemoveGroupException e) {
				e.printStackTrace();
			}
			System.out.println("Mudar configuracao do servidor ATOS");
			System.exit(SERVIDOR_NOT_FOUND);
		} else {
			pivo = servidores[0];
		}

		loop(atos, pivo);

		Thread.sleep(200);

		try {
			System.out.println("enviando comando");
			atos.pararIrrigacao(pivo);
			// atos.iniciaIrrigacao(pivo);
			// atos.setSentdido(pivo);
		} catch (ComponentNotFoundException e1) {
			e1.printStackTrace();
		} catch (SynchReadException e1) {
			e1.printStackTrace();
		} catch (SynchWriteException e1) {
			e1.printStackTrace();
		}

		try {
			atos.disconnect();
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
		} catch (UnableRemoveGroupException e) {
			e.printStackTrace();
		}
	}
}
