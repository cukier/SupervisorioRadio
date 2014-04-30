package cuki.exemple;

import java.util.Enumeration;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;

public class TesteSerialPort {

	public static void main(String[] args) {

		String wantdePortName = "COM8";

		@SuppressWarnings("rawtypes")
		Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();

		CommPortIdentifier portId = null;

		while (portIdentifiers.hasMoreElements()) {
			CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers
					.nextElement();
			if (pid.getPortType() == CommPortIdentifier.PORT_SERIAL
					&& pid.getName().equalsIgnoreCase(wantdePortName)) {
				portId = pid;
				break;
			}
		}

		if (portId == null) {
			System.err.println("Could not find serial port " + wantdePortName);
			System.exit(1);
		}

		SerialPort port = null;

		try {
			port = (SerialPort) portId.open("nam", 10000);
		} catch (PortInUseException e) {
			System.err.println("Port already in use: " + e);
			System.exit(1);
		}

		if (port != null) {
			System.out.println("Porta aberta com sucesso: " + port.getName());
			port.close();
		}
	}
}
