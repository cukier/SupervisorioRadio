package cuki.opc;

import java.util.ArrayList;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.browser.JOpcBrowser;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.CoInitializeException;
import javafish.clients.opc.exception.CoUninitializeException;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.exception.UnableBrowseBranchException;
import javafish.clients.opc.exception.UnableBrowseLeafException;
import javafish.clients.opc.exception.UnableIBrowseException;

public class OpcConn {

	private String serverClientHandle;
	private String host = "localhost";
	private String server = null;
	private static int id = 0;
	private JOpc jopc = null;
	OpcGroup group = null;

	public OpcConn(String host, String server) {
		this.host = host;
		this.server = server;
		serverClientHandle = "JOPCAtos" + String.valueOf(id++);
	}

	private String[] getBranch(String branch, JOpcBrowser opcBrowser)
			throws UnableBrowseBranchException, UnableIBrowseException {

		String[] retorno = null;

		try {
			retorno = opcBrowser.getOpcBranch(branch);
		} catch (UnableBrowseBranchException e) {
			throw new UnableBrowseBranchException(branch);
		} catch (UnableIBrowseException e) {
			throw new UnableIBrowseException(branch);
		}
		return retorno;
	}

	private String[] getIten(String leaf, JOpcBrowser opcBrowser)
			throws UnableBrowseLeafException, UnableIBrowseException,
			UnableAddGroupException, UnableAddItemException {
		String[] retorno = null;

		try {
			retorno = opcBrowser.getOpcItems(leaf, true);
		} catch (UnableBrowseLeafException e) {
			throw new UnableBrowseLeafException(leaf);
		} catch (UnableIBrowseException e) {
			throw new UnableIBrowseException(leaf);
		} catch (UnableAddGroupException e) {
			throw new UnableAddGroupException(leaf);
		} catch (UnableAddItemException e) {
			throw new UnableAddItemException(leaf);
		}

		return retorno;
	}

	private String[] getAllItens(String branch, String aux,
			JOpcBrowser opcBrowser) throws UnableBrowseBranchException,
			UnableIBrowseException, UnableAddGroupException,
			UnableBrowseLeafException, UnableAddItemException {

		String[] branches = null;
		String[] retorno = null;

		try {
			branches = getBranch(branch, opcBrowser);
		} catch (UnableBrowseBranchException e) {
			// throw new UnableBrowseBranchException(branch);
			System.out.println("Adicionado " + aux);
		} catch (UnableIBrowseException e) {
			throw new UnableIBrowseException(branch);
		}

		if (branches != null) {
			aux = branch;
			ArrayList<String> pool = new ArrayList<String>();
			for (String i : branches) {
				String[] otherItens = null;
				try {
					otherItens = getAllItens(i, aux, opcBrowser);
				} catch (UnableBrowseBranchException e) {
					// throw new UnableBrowseBranchException(i);
				} catch (UnableIBrowseException e) {
					throw new UnableIBrowseException(i);
				}
				if (otherItens != null) {
					// if (otherItens != null && otherItens[0].contains(";")) {
					for (String j : otherItens) {
						if (j.contains(";")) {
							String[] split = j.split("; ");
							pool.add(split[2]);
						} else
							pool.add(j);
					}
				}
			}
			retorno = null;
			retorno = new String[pool.size()];
			pool.toArray(retorno);
		} else if (!branch.equals("Read Error")
				&& !branch.equals("Configured Aliases")) {
			String leafPath = "";
			try {
				leafPath = (aux != "") ? aux + "." + branch : branch;
				retorno = getIten(leafPath, opcBrowser);
			} catch (UnableBrowseLeafException e) {
				throw new UnableBrowseLeafException(leafPath);
			} catch (UnableIBrowseException e) {
				throw new UnableIBrowseException(leafPath);
			} catch (UnableAddGroupException e) {
				throw new UnableAddGroupException(leafPath);
			} catch (UnableAddItemException e) {
				throw new UnableAddItemException(leafPath);
			}
		}
		return retorno;
	}

	public void registerAllItens() throws ConnectivityException,
			UnableBrowseBranchException, UnableIBrowseException,
			UnableAddGroupException, UnableBrowseLeafException,
			UnableAddItemException {

		String[] itens = null;
		JOpcBrowser opcBrowser = new JOpcBrowser(this.host, this.server,
				this.serverClientHandle);

		try {
			JOpcBrowser.coInitialize();
		} catch (CoInitializeException e) {
			throw new CoInitializeException(this.host + " " + this.server + " "
					+ this.serverClientHandle);
		}

		try {
			opcBrowser.connect();
		} catch (ConnectivityException e) {
			throw new ConnectivityException(this.host + " " + this.server + " "
					+ this.serverClientHandle);
		}

		try {
			itens = getAllItens("", null, opcBrowser);
		} catch (UnableBrowseBranchException e) {
			throw new UnableBrowseBranchException(this.host + " " + this.server
					+ " " + this.serverClientHandle);
		} catch (UnableIBrowseException e) {
			throw new UnableIBrowseException(this.host + " " + this.server
					+ " " + this.serverClientHandle);
		} catch (UnableAddGroupException e) {
			throw new UnableAddGroupException(this.host + " " + this.server
					+ " " + this.serverClientHandle);
		} catch (UnableBrowseLeafException e) {
			throw new UnableBrowseLeafException(this.host + " " + this.server
					+ " " + this.serverClientHandle);
		} catch (UnableAddItemException e) {
			throw new UnableAddItemException(this.host + " " + this.server
					+ " " + this.serverClientHandle);
		}

		try {
			JOpcBrowser.coUninitialize();
		} catch (CoUninitializeException e) {
			throw new CoUninitializeException(this.host + " " + this.server
					+ " " + this.serverClientHandle);
		}
		opcBrowser = null;

		try {
			JOpc.coInitialize();
		} catch (CoInitializeException e) {
			throw new CoInitializeException(this.host + " " + this.server + " "
					+ this.serverClientHandle);
		}

		jopc = new JOpc(this.host, this.server, this.serverClientHandle);
		group = new OpcGroup("group1", true, 10, 0.0f);

		for (String s : itens) {
			group.addItem(new OpcItem(s, true, ""));
		}

		jopc.addGroup(group);

		jopc.connect();
		System.out.println("JOPC client is connected...");

		jopc.registerGroups();
		System.out.println("OPCGroup are registered...");
	}

	public OpcGroup synchResponse() throws ComponentNotFoundException,
			SynchReadException {

		OpcGroup responseGroup = null;

		try {
			responseGroup = jopc.synchReadGroup(group);
			// System.out.println(responseGroup);
		} catch (ComponentNotFoundException e) {
			throw new ComponentNotFoundException(group.toString());
		} catch (SynchReadException e) {
			throw new SynchReadException(group.toString());
		}

		return responseGroup;
	}

	public void disconn() {
		JOpc.coUninitialize();
		jopc = null;
	}

	public String[] getEquipamentos() {
		ArrayList<String> array = new ArrayList<String>();

		int cont = 0;
		for (String aux : group.toString().split("itemName = ")) {
			int cont2 = 0;
			for (String aux2 : aux.split(";")) {
				if (cont > 0 && cont2 == 0) {
					if (array.isEmpty()
							|| !aux2.split("\\.")[0].equals(array.get(array
									.size() - 1))) {
						array.add(aux2.split("\\.")[0]);
					}
				}
				cont2++;
			}
			cont++;
		}
		String[] retorno = new String[array.size()];
		array.toArray(retorno);
		return retorno;
	}
}