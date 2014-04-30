package cuki.utils;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
//import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
//import net.wimpi.modbus.msg.WriteMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.procimg.SimpleRegister;

public class PollDevice {

	private ReadMultipleRegistersRequest req;
	// private WriteMultipleRegistersRequest do_mulReq;
	private WriteSingleRegisterRequest do_req;
	private ModbusSerialTransaction trans;
	private DeviceModbus device;
	SerialConnection con;

	private int qtdMem = 0;
	private int chunckSize = 50;
	private boolean debug = false;

	public PollDevice(DeviceModbus device, SerialConnection con, int chunckSize) {

		this.qtdMem = device.getMemQtd();
		this.chunckSize = chunckSize;
		this.device = device;
		this.con = con;

		ModbusCoupler.getReference().setUnitID(1);

		req = new ReadMultipleRegistersRequest();
		req.setUnitID(device.getDeviceID());
		req.setHeadless();

		do_req = new WriteSingleRegisterRequest();
		do_req.setUnitID(device.getDeviceID());
		do_req.setHeadless();

		// do_mulReq = new WriteMultipleRegistersRequest();

		trans = new ModbusSerialTransaction(con);
		trans.setTransDelayMS(1000);
		trans.setRetries(5);
	}

	// private WriteMultipleRegistersResponse wirteMultiTransaction(int
	// endereco,
	// int[] data) throws ModbusIOException, ModbusSlaveException,
	// ModbusException {
	//
	// SimpleRegister[] reg = new SimpleRegister[data.length];
	//
	// do_mulReq.setReference(endereco);
	// do_mulReq.setRegisters(reg);
	//
	// trans.setRequest(do_mulReq);
	// trans.execute();
	//
	// return (WriteMultipleRegistersResponse) trans.getResponse();
	// }

	// public void writeMultipleRegister(int endereco, int[] data)
	// throws ModbusIOException, ModbusSlaveException, ModbusException {
	//
	// WriteMultipleRegistersResponse res = (WriteMultipleRegistersResponse)
	// wirteMultiTransaction(
	// endereco, data);
	//
	// if (debug) {
	// System.out.println("=====writeMultipleRegister======");
	// System.out.println(res.getDataLength() + "\t\tData Length");
	// System.out.println(res.getFunctionCode() + "\t\tFuncion Code");
	// System.out.println(res.getHexMessage() + "\t\tHex Message");
	// System.out.println(res.getOutputLength() + "\t\tOutput Length");
	// System.out.println(res.getProtocolID() + "\t\tProtocol Id");
	// System.out.println(res.getReference() + "\t\tReference");
	// System.out.println(res.getTransactionID() + "\t\tTransaction Id");
	// System.out.println(res.getWordCount() + "\t\tWord Count");
	// System.out.println(res.getUnitID() + "\t\tUnit Id");
	// }
	// }

	private WriteSingleRegisterResponse writeTransaction(int endereco, int data)
			throws ModbusIOException, ModbusSlaveException, ModbusException {

		SimpleRegister reg = new SimpleRegister(data);
		ModbusSerialTransaction nTrasn = new ModbusSerialTransaction(con);

		do_req.setReference(endereco);
		do_req.setDataLength(1);
		do_req.setRegister(reg);

		nTrasn.setRequest(do_req);
		nTrasn.setTransDelayMS(trans.getTransDelayMS());
		nTrasn.setRetries(trans.getRetries());
		nTrasn.execute();

		// trans.setRequest(do_req);
		// trans.execute();

		return (WriteSingleRegisterResponse) nTrasn.getResponse();
		// return (WriteSingleRegisterResponse) trans.getResponse();

	}

	public void writeRegister(int endereco, int data) throws ModbusIOException,
			ModbusSlaveException, ModbusException {

		WriteSingleRegisterResponse res = (WriteSingleRegisterResponse) writeTransaction(
				endereco, data);

		if (debug) {
			System.out.println("=====writeRegister======");
			System.out.println(res.getDataLength() + "Data Length");
			System.out.println(res.getFunctionCode() + "Funcion Code");
			System.out.println(res.getHexMessage() + "Hex Message");
			System.out.println(res.getOutputLength() + "Output Length");
			System.out.println(res.getProtocolID() + "Protocol Id");
			System.out.println(res.getReference() + "Reference");
			System.out.println(res.getRegisterValue() + "Register Value");
			System.out.println(res.getTransactionID() + "Transaction Id");
			System.out.println(res.getUnitID() + "Unit Id");
		}

	}

	private ReadMultipleRegistersResponse executeTransaction(int endereco,
			int tamanho) throws ModbusException {
		ModbusSerialTransaction nTrasn = new ModbusSerialTransaction(con);

		req.setReference(endereco);
		req.setWordCount(tamanho);

		nTrasn.setRequest(req);
		nTrasn.setTransDelayMS(trans.getTransDelayMS());
		nTrasn.setRetries(trans.getRetries());
		nTrasn.execute();

		// trans.setRequest(req);
		// trans.execute();

		// return (ReadMultipleRegistersResponse) trans.getResponse();
		return (ReadMultipleRegistersResponse) nTrasn.getResponse();

	}

	private byte toByte(int word, int index) {
		return (byte) ((word & ((0xFF) << (index * 8))) >> (index * 8));
	}

	public void poll(ItemModbus[] itens) throws ModbusException {

		int[] chunck = new int[qtdMem];

		if (debug) {
			System.out.println("==========Polling==========");
		}

		for (int cont = 0; cont <= (int) qtdMem / chunckSize; cont++) {
			int aux = 0;
			if (cont != (int) qtdMem / chunckSize)
				aux = chunckSize;
			else
				aux = qtdMem % chunckSize;
			ReadMultipleRegistersResponse res = executeTransaction(cont
					* chunckSize, aux);
			for (int contBuffer = 0; contBuffer < res.getWordCount(); contBuffer++) {
				chunck[contBuffer + (cont * chunckSize)] = res
						.getRegisterValue(contBuffer);
				if (debug) {
					System.out.println("Word "
							+ (contBuffer + (cont * chunckSize)) + " : "
							+ res.getRegisterValue(contBuffer));
				}
			}
		}

		int acm = 0;
		for (ItemModbus item : itens) {

			if (item.getEndereco() >= qtdMem)
				return;

			byte[] data = new byte[item.getData().length];

			for (int cont = 0; cont < item.getTamanho(); cont++, acm++) {
				data[2 * cont + 1] = toByte(chunck[acm], 1);
				data[2 * cont] = toByte(chunck[acm], 0);
			}
			item.setData(data);
		}
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setLamina(int lamina, int setor) throws ModbusIOException,
			ModbusSlaveException, ModbusException, InterruptedException {

		writeRegister(setor, 30);
		Thread.sleep(100);

		writeRegister(device.getFaseIndice() + 51, lamina);

	}

	private int setBool(boolean value, int bit, int data, int endereco)
			throws ModbusIOException, ModbusSlaveException, ModbusException {
		int index = 1 << bit;

		data = (value ? data | index : data & ~index) & 0xFFFF;

		writeRegister(endereco, data);

		return data;
	}

	public void setSentido(boolean value) throws ModbusIOException,
			ModbusSlaveException, ModbusException {
		setBool(!device.getSentido(), 7, device.getWord2(), 2);
	}

	public void setIrrigacao(boolean value) throws ModbusIOException,
			ModbusSlaveException, ModbusException {
		int endereco = value ? 2 : 6;
		int bit = value ? 6 : 4;
		int data = value ? device.getWord2() : device.getWord6();
		setBool(true, bit, data, endereco);
	}
}
