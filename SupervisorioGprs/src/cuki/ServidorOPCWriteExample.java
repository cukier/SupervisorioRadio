package cuki;

import java.util.Arrays;

import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.exception.UnableBrowseBranchException;
import javafish.clients.opc.exception.UnableIBrowseException;
import javafish.clients.opc.exception.UnableRemoveGroupException;
import cuki.opc.ServidorOPC;

public class ServidorOPCWriteExample {

	private static void loop(ServidorOPC atos, String pivo, OpcItem item)
			throws InterruptedException, ComponentNotFoundException,
			SynchReadException {
		while (true) {
			// for (int cont = 0; cont < 120; cont++) {

			Thread.sleep(1000);
			atos.syncItens();
			// item = atos.getItem(pivo, atos.getItens(pivo).getWord6());
			// System.out.println(item);

			// boolean ok = false;
			//
			// for (boolean aux : atos.getQuality(pivo)) {
			// System.out.print(aux + " ");
			// ok &= aux;
			// }
			// System.out.print(": " + ok);
			// System.out.println("");
			//
			// if (ok)
			// break;

			float sinal = atos.getSignal(pivo);
			System.out.println(sinal);
			if (sinal > 0.33)
				break;
		}
	}

	public static void main(String[] args) throws Exception {

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

		OpcItem item = null;
		OpcGroup group = null;

		try {
			group = atos.syncItens();
		} catch (ComponentNotFoundException e2) {
			e2.printStackTrace();
		} catch (SynchReadException e2) {
			e2.printStackTrace();
		}
		for (OpcItem aux : group.getItems()) {
			if (aux.getItemName().contains("Word4")) {
				item = aux;
			}
		}

		try {
			loop(atos, pivo, item);
		} catch (ComponentNotFoundException | SynchReadException e2) {
			throw new Exception("Loop error\n" + e2.getMessage());
		}

		System.out.println("Escrevendo em " + item);

		Thread.sleep(200);

		// atos.iniciaIrrigacao(pivo);
		// atos.setSentdido(pivo);
		atos.pararIrrigacao(pivo);

		try {
			atos.disconnect();
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
		} catch (UnableRemoveGroupException e) {
			e.printStackTrace();
		}
	}
}
