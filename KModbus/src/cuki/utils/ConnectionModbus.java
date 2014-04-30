package cuki.utils;

import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

public class ConnectionModbus {

	private SerialParameters params;
	private SerialConnection con;

	public ConnectionModbus(String portName) {

		params = new SerialParameters();

		params.setPortName(portName);
		params.setBaudRate(9600);
		params.setDatabits(8);
		params.setParity("None");
		params.setStopbits(1);
		params.setEncoding("rtu");
		params.setEcho(false);

		con = new SerialConnection(params);
	}

	public void setParameters(String portName, String baudRate,
			String dataBits, String parity, String stopBits) {
		params.setPortName(portName);
		params.setBaudRate(baudRate);
		params.setDatabits(dataBits);
		params.setParity(parity);
		params.setStopbits(stopBits);
	}

	public void open() throws Exception {
		con.open();
		// doorOpen = true;
	}

	public void close() {
		if (con != null) {
			con.close();
			// doorOpen = false;
		}
	}

	public boolean isDoorOpen() {
		// return this.doorOpen;
		return con.isOpen();
	}

	public SerialConnection getConection() {
		return this.con;
	}

	public String getPortName() {
		return params.getPortName();
	}

	public String getBaudRate() {
		return params.getBaudRateString();
	}

	public String getParity() {
		return params.getParityString();
	}

	public String getDataBits() {
		return params.getDatabitsString();
	}

	public String getStopBits() {
		return params.getStopbitsString();
	}
}
