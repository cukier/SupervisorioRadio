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
import javafish.clients.opc.exception.SynchWriteException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.exception.UnableBrowseBranchException;
import javafish.clients.opc.exception.UnableBrowseLeafException;
import javafish.clients.opc.exception.UnableIBrowseException;
import javafish.clients.opc.exception.UnableRemoveGroupException;
import javafish.clients.opc.exception.VariantTypeException;
import javafish.clients.opc.variant.Variant;
import cuki.opc.ItensOPC;
import cuki.utils.BitField;

public class ServidorOPC {

	private String serverClientHandle;
	private String host;
	private String server;
	private JOpc jopc;
	private OpcGroup group;
	private String[] servidores;
	private ItensOPC[] itensPivo;
	public static final String sinalRuim = "sinal ruim";
	public static final String sinalMedio = "sinal médio";
	public static final String sinalBom = "sinal bom";

	public ServidorOPC() {
		this("localhost", "Atos.OPCConnect.1", "JOpcAtos1");
	}

	public ServidorOPC(String host, String server, String serverClientHandle) {

		this.host = host;
		this.server = server;
		this.serverClientHandle = serverClientHandle;

		jopc = new JOpc(this.host, this.server, this.serverClientHandle);
		group = new OpcGroup("group1", true, 500, 0.0f);
	}

	@SuppressWarnings("unused")
	private ArrayList<String> achaItensRecursivo(String leaf,
			ArrayList<String> lista, JOpcBrowser opcBrowser) {

		String[] itens = null;

		try {
			itens = opcBrowser.getOpcItems(leaf, true);
		} catch (UnableBrowseLeafException | UnableIBrowseException
				| UnableAddGroupException | UnableAddItemException e) {
			e.printStackTrace();
		}

		if (itens != null) {
			for (String item : itens) {
				lista.add(item);
			}
		}

		String[] branches = null;

		try {
			branches = opcBrowser.getOpcBranch(leaf);
		} catch (UnableBrowseBranchException | UnableIBrowseException e) {
			e.printStackTrace();
		}

		if (branches != null) {
			for (String brach : branches) {
				lista = achaItensRecursivo(brach, lista, opcBrowser);
			}
		}

		return lista;

	}

	private String[] getServers() throws ConnectivityException,
			UnableBrowseBranchException, UnableIBrowseException,
			CoUninitializeException {

		String[] retorno = null;

		JOpcBrowser opcBrowser = new JOpcBrowser(this.host, this.server,
				this.serverClientHandle);

		try {
			JOpcBrowser.coInitialize();
			System.out.println("Inializado JOpcBrowser");
		} catch (CoInitializeException e) {
			throw new CoInitializeException(
					"Falha ao buscar equipamentos de comunicação");
		}

		try {
			opcBrowser.connect();
		} catch (ConnectivityException e) {
			throw new ConnectivityException(this.host + " " + this.server + " "
					+ this.serverClientHandle);
		}

		try {
			retorno = opcBrowser.getOpcBranch("");
		} catch (UnableBrowseBranchException e) {
			throw new UnableBrowseBranchException(
					"Falha ao buscar equipamentos de comunicação (2)");
		} catch (UnableIBrowseException e) {
			throw new UnableIBrowseException(
					"Falha ao buscar equipamentos de comunicação (3)");
		}

		if (retorno != null) {
			for (String equimaneto : retorno) {
				String[] itens = null;
				try {
					itens = opcBrowser.getOpcItems(equimaneto, true);
				} catch (UnableBrowseLeafException e) {
					e.printStackTrace();
				} catch (UnableAddGroupException e) {
					e.printStackTrace();
				} catch (UnableAddItemException e) {
					e.printStackTrace();
				}

				if (itens != null) {

				}
			}
		}

		try {
			JOpcBrowser.coUninitialize();
			System.out.println("Encerrando JOpcBrowser");
		} catch (CoUninitializeException e) {
			throw new CoUninitializeException("Falha ao encerrar OPC Browser");
		}

		return retorno;
	}

	public void connectAndRegister() throws ConnectivityException,
			UnableAddGroupException, UnableAddItemException,
			UnableBrowseBranchException, UnableIBrowseException,
			CoUninitializeException {

		try {
			this.servidores = getServers();
		} catch (UnableBrowseBranchException e) {
			throw e;
		} catch (UnableIBrowseException e) {
			throw e;
		} catch (CoUninitializeException e) {
			throw e;
		}

		itensPivo = new ItensOPC[servidores.length];

		int cont = 0;

		for (String servidor : servidores) {

			itensPivo[cont] = new ItensOPC(servidor);

			for (OpcItem item : itensPivo[cont++].createItens(servidor))
				group.addItem(item);
		}

		jopc.addGroup(group);

		JOpc.coInitialize();

		try {
			jopc.connect();
			System.out.println("JOPC client is connected...");
		} catch (ConnectivityException e) {
			throw new ConnectivityException("Not Connected!");
		}

		try {
			jopc.registerGroups();
			System.out.println("OPCGroup are registered...");
		} catch (UnableAddGroupException e) {
			throw new UnableAddGroupException("No group added!");
		} catch (UnableAddItemException e) {
			throw new UnableAddItemException("No item added!");
		}
	}

	public String[] getServidores() {
		return this.servidores;
	}

	public void disconnect() throws ComponentNotFoundException,
			UnableRemoveGroupException {
		try {
			jopc.unregisterGroup(group);
		} catch (ComponentNotFoundException e) {
			throw new ComponentNotFoundException("Disconnect Error");
		} catch (UnableRemoveGroupException e) {
			throw new UnableRemoveGroupException("Remove group error");
		}

		JOpc.coUninitialize();

		jopc = null;

		System.out.println("Conexão encerrada");
	}

	public OpcGroup syncItens() throws ComponentNotFoundException,
			SynchReadException {
		group = jopc.synchReadGroup(group);
		return group;

	}

	private int parseItem(OpcItem item) throws NumberFormatException {

		int retorno = 0;
		for (String aux : item.toString().split(";")) {
			if (aux.contains("itemValue")) {
				for (String aux2 : aux.split(" = ")) {
					if (!aux2.contains("itemValue")) {
						try {
							if (item.getItemName().contains("ASYNC"))
								retorno = Integer.parseInt(aux2, 16);
							else
								retorno = Integer.parseInt(aux2);
						} catch (NumberFormatException e) {
							throw new NumberFormatException("Formato errado");
						}
					}
				}
			}
		}
		return retorno;
	}

	@SuppressWarnings("unused")
	private int getResponse2(OpcItem item) throws ComponentNotFoundException,
			SynchReadException {
		int retorno = 0;

		try {
			OpcItem response = jopc.synchReadItem(group, item);
			retorno = parseItem(response);
		} catch (ComponentNotFoundException e) {
			throw new ComponentNotFoundException(item.getItemName()
					+ ": No component found");
		} catch (SynchReadException e) {
			throw new SynchReadException(item.getItemName()
					+ " Synch read error");
		}

		return retorno;
	}

	private int getResponse(OpcItem item) {

		int retorno = 0;
		for (OpcItem aux : group.getItemsAsArray())
			if (aux.getItemName().equals(item.getItemName())) {
				// System.out.println(aux.toString());
				retorno = parseItem(aux);
			}
		return retorno;
	}

	private int indice(String pivo) {
		int cont = 0;
		for (ItensOPC item : itensPivo) {
			if (pivo.equals(item.getPivo()))
				break;
			else
				cont++;
		}
		return cont;
	}

	public int getTempoRestanteMinutos(String pivo)
			throws ComponentNotFoundException, SynchReadException {
		return getResponse(itensPivo[indice(pivo)].getTempoRestanteMinutos());
	}

	public int getanguloAtual(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getAnguloAtual());
	}

	public int getcicloAtual(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getCicloAtual());
	}

	public int getnrSetores(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getNrSetores());
	}

	public int getcontaFase(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getContaFase());
	}

	public int getcontaSetor(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getContaSetor());
	}

	public int getnrFases(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getNrFases());
	}

	public int getlaminaGet(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getLaminaGet());
	}

	public int gettempoRestanteHoras(String pivo)
			throws ComponentNotFoundException, SynchReadException,
			VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getTempoRestanteHoras());
	}

	public int getword0(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getWord0());
	}

	public int getword4(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getWord4());
	}

	public int getword6(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getWord6());
	}

	public int getstatusPivo(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getStatusPivo());
	}

	public int getsetorIndice(String pivo) throws ComponentNotFoundException,
			SynchReadException, VariantTypeException {
		return getResponse(itensPivo[indice(pivo)].getSetorIndice());
	}

	public int[] getAnguloSetor(String pivo) {
		int[] retorno = new int[6];
		for (OpcItem item : group.getItemsAsArray()) {
			if (item.getItemName().contains("anguloSetor")
					&& item.getItemName().contains(pivo)) {
				int cont = 0;
				for (String aux : item.getValue().getString()
						.split("(?<=\\G.{4})")) {
					try {
						retorno[cont++] = Integer.parseInt(aux, 16);
					} catch (NumberFormatException e) {
						retorno[cont++] = 360;
					}
				}
				break;
			}
		}
		return retorno;
	}

	public ItensOPC getItens(String pivo) {
		return itensPivo[indice(pivo)];
	}

	private void writeItem(OpcItem item, int value) throws SynchWriteException,
			ComponentNotFoundException {

		Variant varIn = new Variant(value);
		item.setValue(varIn);

		try {
			jopc.synchWriteItem(group, item);
		} catch (ComponentNotFoundException e) {
			throw new ComponentNotFoundException("Não Encontrado: "
					+ item.getItemName());
		} catch (SynchWriteException e) {
			throw new SynchWriteException("Erro de escrita "
					+ item.getItemName());
		}
	}

	public void writeSetorIndice(String pivo, int value)
			throws SynchWriteException, ComponentNotFoundException {
		try {
			writeItem(itensPivo[indice(pivo)].getSetorIndice(), value);
		} catch (ComponentNotFoundException e) {
			throw e;
		} catch (SynchWriteException e) {
			throw e;
		}
	}

	public void pararIrrigacao(String pivo) throws SynchReadException,
			ComponentNotFoundException, SynchWriteException {

		BitField word6;

		try {
			word6 = new BitField(getword6(pivo));
		} catch (ComponentNotFoundException e) {
			throw new ComponentNotFoundException("nao encotrada word 6");
		} catch (SynchReadException e) {
			throw new SynchReadException("erro ao ler word 6");
		} catch (VariantTypeException e) {
			throw new VariantTypeException("erro de convesao word 6");
		}

		word6.setBit(BitField.pararIrriga);
		writeItem(itensPivo[indice(pivo)].getWord6(), word6.getByte());
	}

	public void emEspera(String pivo) throws SynchReadException,
			ComponentNotFoundException, SynchWriteException {
		BitField word6;

		try {
			word6 = new BitField(getword6(pivo));
		} catch (ComponentNotFoundException e) {
			throw new ComponentNotFoundException("nao encotrada word 6");
		} catch (SynchReadException e) {
			throw new SynchReadException("erro ao ler word 6");
		} catch (VariantTypeException e) {
			throw new VariantTypeException("erro de convesao word 6");
		}

		word6.resetBit(BitField.emEspera);
		writeItem(itensPivo[indice(pivo)].getWord6(), word6.getByte());
	}

	public void iniciaIrrigacao(String pivo) throws SynchWriteException,
			ComponentNotFoundException, SynchReadException {

		BitField word4;

		try {
			word4 = new BitField(getword4(pivo));
		} catch (SynchReadException | VariantTypeException e) {
			throw new SynchReadException(pivo + " word4");
		}

		word4.setBit(BitField.inicioIrriga);
		writeItem(itensPivo[indice(pivo)].getWord4(), word4.getByte());
	}

	public void reverteSentido(String pivo) throws ComponentNotFoundException,
			SynchWriteException, SynchReadException {
		BitField word4 = null;

		try {
			word4 = new BitField(getword4(pivo));
		} catch (ComponentNotFoundException e) {
			throw new ComponentNotFoundException("word 4 não encontrado");
		} catch (SynchReadException e) {
			throw new SynchReadException("erro ao ler word4");
		} catch (VariantTypeException e) {
			throw new VariantTypeException("word 4 formato invalido");
		}

		if (word4 != null) {

			System.out.println("Sentdo antes: "
					+ word4.getBit(BitField.sentido));

			if (word4.getBit(BitField.sentido))
				word4.resetBit(BitField.sentido);
			else
				word4.setBit(BitField.sentido);

			System.out.println("Sentdo depois: "
					+ word4.getBit(BitField.sentido));

			try {
				writeItem(itensPivo[indice(pivo)].getWord4(), word4.getByte());
			} catch (ComponentNotFoundException | SynchWriteException e) {
				throw e;
			}
		}
	}

	private OpcItem[] getItensFromPivo(String pivo) {

		ArrayList<OpcItem> itensList = new ArrayList<OpcItem>();

		for (OpcItem item : group.getItemsAsArray()) {
			if (item.getItemName().contains(pivo))
				itensList.add(item);
		}

		OpcItem[] itens = new OpcItem[itensList.size()];
		itensList.toArray(itens);

		return itens;
	}

	public boolean[] getQuality(String pivo) {

		ArrayList<Boolean> qualityList = new ArrayList<Boolean>();

		for (OpcItem item : getItensFromPivo(pivo)) {
			if (item.getItemName().contains("ASYNC"))
				qualityList.add(item.isQuality());
		}

		boolean[] itens = new boolean[qualityList.size()];

		int cont = 0;
		for (Boolean aux : qualityList) {
			itens[cont++] = aux;
		}

		return itens;
	}

	public float getSignal(String pivo) {
		boolean[] qualidade = getQuality(pivo);
		int total = qualidade.length;

		int cont = 0;
		for (boolean ok : qualidade) {
			if (ok)
				cont++;
		}

		return (float) cont / total;
	}

	public OpcItem getItem(String pivo, OpcItem item) {
		for (OpcItem aux : group.getItemsAsArray()) {
			if (aux.getItemName().equals(item.getItemName())) {
				return aux;
			}
		}
		return null;
	}
}
