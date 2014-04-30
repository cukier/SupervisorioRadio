package cuki;

import java.util.Arrays;

import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
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
import cuki.opc.ServidorOPC;

public class ServidorOPCReadExample {

	@SuppressWarnings("unused")
	private static void printItens(ServidorOPC atos, String pivo) {
		try {
			System.out.println("Angulo Atual: " + atos.getanguloAtual(pivo));
			System.out.println("Ciculo Atual: " + atos.getcicloAtual(pivo));
			System.out.println("Conta Fase: " + atos.getcontaFase(pivo));
			System.out.println("Conta Setor: " + atos.getcontaSetor(pivo));
			System.out.println("Lâmina: " + atos.getlaminaGet(pivo));
			System.out.println("Nr. de fases: " + atos.getnrFases(pivo));
			System.out.println("Nr. de setores: " + atos.getnrSetores(pivo));
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
		} catch (SynchReadException e) {
			e.printStackTrace();
		} catch (VariantTypeException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		ServidorOPC atos = new ServidorOPC();
		String pivo = null;
		final int SERVER_NOT_FOUND = 1;

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

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
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
			System.exit(SERVER_NOT_FOUND);
		} else {
			pivo = servidores[0];
		}

		OpcGroup group = null;
		for (int cont = 0; cont < 360; cont++) {
			// while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				group = atos.syncItens();
			} catch (ComponentNotFoundException e) {
				e.printStackTrace();
			} catch (SynchReadException e) {
				e.printStackTrace();
			}

			System.out.println(group.toString());

			for (OpcItem item : group.getItemsAsArray()) {
				System.out.println(item.toString());
			}
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
