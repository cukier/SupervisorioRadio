package cuki.opc;

import java.util.ArrayList;

import javafish.clients.opc.component.OpcItem;

public class ItensOPC {

	private String pivo;

	private OpcItem cicloAtual;
	private OpcItem nrSetores;
	private OpcItem contaFase;
	private OpcItem contaSetor;
	private OpcItem nrFases;
	private OpcItem laminaGet;
	private OpcItem tempoRestanteHoras;
	private OpcItem anguloAtual;
	private OpcItem word0;
	private OpcItem word4;
	private OpcItem word6;
	private OpcItem statusPivo;
	private OpcItem setorIndice;
	private OpcItem tempoRestanteMinutos;
	private OpcItem anguloSetor;
	private OpcItem[] itensList;

	public ItensOPC(String pivo) {
		this.pivo = pivo;
	}

	public OpcItem[] createItens(String pivo) {

		ArrayList<OpcItem> array = new ArrayList<OpcItem>(14);

		anguloSetor = new OpcItem(pivo + ".ASYNC.anguloSetor", true, "");
		array.add(anguloSetor);
		tempoRestanteMinutos = new OpcItem(
				pivo + ".ASYNC.tempoRestanteMinutos", true, "");
		array.add(tempoRestanteMinutos);
		cicloAtual = new OpcItem(pivo + ".ASYNC.cicloAtual", true, "");
		array.add(cicloAtual);
		nrSetores = new OpcItem(pivo + ".ASYNC.nrSetores", true, "");
		array.add(nrSetores);
		contaFase = new OpcItem(pivo + ".ASYNC.contaFase", true, "");
		array.add(contaFase);
		contaSetor = new OpcItem(pivo + ".ASYNC.contaSetor", true, "");
		array.add(contaSetor);
		nrFases = new OpcItem(pivo + ".ASYNC.nrFases", true, "");
		array.add(nrFases);
		laminaGet = new OpcItem(pivo + ".ASYNC.laminaGet", true, "");
		array.add(laminaGet);
		tempoRestanteHoras = new OpcItem(pivo + ".ASYNC.tempoRestanteHoras",
				true, "");
		array.add(tempoRestanteHoras);
		anguloAtual = new OpcItem(pivo + ".SYNC.anguloAtual", true, "");
		array.add(anguloAtual);
		word0 = new OpcItem(pivo + ".SYNC.Word0", true, "");
		array.add(word0);
		word4 = new OpcItem(pivo + ".SYNC.Word4", true, "");
		array.add(word4);
		word6 = new OpcItem(pivo + ".SYNC.Word6", true, "");
		array.add(word6);
		statusPivo = new OpcItem(pivo + ".ASYNC.statusPivo", true, "");
		array.add(statusPivo);
		setorIndice = new OpcItem(pivo + ".SYNC.setorIndice", true, "");
		array.add(setorIndice);

		itensList = new OpcItem[array.size()];
		array.toArray(itensList);

		return itensList;
	}

	public String getPivo() {
		return this.pivo;
	}

	public OpcItem getCicloAtual() {
		return cicloAtual;
	}

	public OpcItem getNrSetores() {
		return nrSetores;
	}

	public OpcItem getContaFase() {
		return contaFase;
	}

	public OpcItem getContaSetor() {
		return contaSetor;
	}

	public OpcItem getNrFases() {
		return nrFases;
	}

	public OpcItem getLaminaGet() {
		return laminaGet;
	}

	public OpcItem getTempoRestanteHoras() {
		return tempoRestanteHoras;
	}

	public OpcItem getAnguloAtual() {
		return anguloAtual;
	}

	public OpcItem getWord0() {
		return word0;
	}

	public OpcItem getWord4() {
		return word4;
	}

	public OpcItem getWord6() {
		return word6;
	}

	public OpcItem getStatusPivo() {
		return statusPivo;
	}

	public OpcItem getSetorIndice() {
		return setorIndice;
	}

	public OpcItem getTempoRestanteMinutos() {
		return tempoRestanteMinutos;
	}

	public OpcItem getAnguloSetor() {
		return anguloSetor;
	}

	public String toString() {

		StringBuffer sb = new StringBuffer();

		for (OpcItem item : itensList) {
			sb.append(item + "\n");
		}

		return sb.toString();
	}
}
