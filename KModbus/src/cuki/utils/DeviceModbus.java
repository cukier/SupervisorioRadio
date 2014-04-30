package cuki.utils;

import java.io.Serializable;
import java.util.ArrayList;

import net.wimpi.modbus.util.ModbusUtil;

@SuppressWarnings("serial")
public class DeviceModbus implements Serializable {

	private int deviceID;
	private ItemModbus[] itens;
	private int qtdMem;

	public DeviceModbus(int deviceID, int qtdMem) {

		ArrayList<ItemModbus> list = new ArrayList<>();

		this.deviceID = deviceID;

		for (int enderecoMemoria = 0; enderecoMemoria <= qtdMem; enderecoMemoria += MapeadorDevice
				.toTamanho(enderecoMemoria)) {
			list.add(new ItemModbus(enderecoMemoria));
		}
		itens = new ItemModbus[list.size()];
		list.toArray(itens);
		this.qtdMem = qtdMem;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	public int getDeviceID() {
		return this.deviceID;
	}

	public int getNrItens() {
		return itens.length;
	}

	public ItemModbus[] getItens() {
		return this.itens;
	}

	public void setValue(byte[] value, int pos) {
		itens[pos].setData(value);
	}

	public byte[] getValue(int pos) {
		return itens[pos].getData();
	}

	public static Short[] toShort(String str) {

		ArrayList<Short> list = new ArrayList<Short>();

		for (String s : str.split("(?<=\\G.{2})"))
			list.add((short) (((s.getBytes()[1] & 0xFF) << 8) | (s.getBytes()[0] & 0xFF)));

		Short[] ret = list.toArray(new Short[list.size()]);

		return ret;
	}

	public int getMemQtd() {
		return this.qtdMem;
	}

	private ItemModbus getItemModbus(int addr) {
		for (ItemModbus i : itens) {
			if (i.getEndereco() == addr)
				return i;
		}
		return null;
	}

	private int toInt(byte[] value) {
		if (value.length == 2) {
			return ((value[1] & 0xFF) << 8) | (value[0] & 0xFF);
		}
		return 0;
	}

	private boolean getBool(int addr, int index) {
		int mask = 1 << index;
		int value = toInt(getItemModbus(addr).getData());
		return ((value & mask) == mask);
	}

	private int getInt(int addr) {
		return toInt(getItemModbus(addr).getData());
	}

	public int getAnguloAtual() {
		return getInt(14);

	}

	public boolean getSentido() {
		return getBool(2, 7);
	}

	public boolean getEstadoIrrigacao() {
		return getBool(2, 6);
	}

	public int getEstado() {
		return getInt(146);
	}

	public int getSetorAtual() {
		return getInt(15);
	}

	public int getNrSetores() {
		return getInt(27);
	}

	public int getContaFase() {
		return getInt(13);
	}

	public int getNrFases() {
		return getInt(46);
	}

	public int getLaminaAtual() {
		return getInt(32);
	}

	public int getTempoRestanteHoras() {
		return getInt(138);
	}

	public int getTempoRestanteMinutos() {
		return getInt(139);
	}

	public int getCicloAtual() {
		return getInt(135);
	}

	public int getWord2() {
		return getInt(2);
	}

	public int getWord6() {
		return getInt(6);
	}

	public int getFaseIndice() {
		return getInt(31);
	}

	private double getFloat(int endereco) {

		byte[] data = getItemModbus(endereco).getData();
		byte[] dataAux = { 0, 0, 0, 0, 0, 0, 0, 0 };

		for (int cont = 0; cont < data.length; cont++) {
			dataAux[7 - cont] = data[3 - cont];
		}

		return (double) ModbusUtil.registersToDouble(dataAux);
	}

	public float getLaminaMinima() {
		return (float) getFloat(126);
	}

	public int[] getAnguloSetores() {

		int[] ret = new int[6];

		for (int cont = 0; cont < 6; cont++) {
			ret[cont] = toInt(getItemModbus(cont + 40).getData());
		}

		return ret;
	}

}
