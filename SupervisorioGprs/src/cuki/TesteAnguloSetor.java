package cuki;

import java.util.Arrays;

import javafish.clients.opc.exception.CoUninitializeException;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.exception.UnableBrowseBranchException;
import javafish.clients.opc.exception.UnableIBrowseException;
import javafish.clients.opc.exception.UnableRemoveGroupException;
import cuki.opc.ServidorOPC;

public class TesteAnguloSetor {

	public static void main(String[] args) throws InterruptedException {

		ServidorOPC atos = new ServidorOPC();

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
		} catch (CoUninitializeException e) {
			e.printStackTrace();
		} catch (ConnectivityException e) {
			e.printStackTrace();
		}

		String[] servidores = atos.getServidores();
		System.out.println(Arrays.asList(servidores));
		String pivo = null;

		if (servidores == null) {
			System.out.println("no servers");
			System.exit(1);
		} else {
			for (String aux : servidores) {
				if (aux.equals("PivoKrebsfer"))
					pivo = aux;
			}
			if (pivo == null) {
				System.out.println("No PivoKrebser");
				System.exit(2);
			}
		}

		for (int cont = 0; cont < 10; cont++) {
			Thread.sleep(1000);
			try {
				atos.syncItens();
			} catch (ComponentNotFoundException e) {
				e.printStackTrace();
			} catch (SynchReadException e) {
				e.printStackTrace();
			}

			int[] anguloSetor = atos.getAnguloSetor(pivo);

			for (int aux : anguloSetor) {
				System.out.println(aux);
			}

			System.out.println("");
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
