package cuki.utils;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.comm.CommPortIdentifier;

public class PortLister {

	public static String[] getPorts() {

		@SuppressWarnings("rawtypes")
		Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();

		if (portIdentifiers == null)
			return null;

		CommPortIdentifier portId = null;
		ArrayList<String> portList = new ArrayList<>();
		while (portIdentifiers.hasMoreElements()) {
			portId = (CommPortIdentifier) portIdentifiers.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
				portList.add(portId.getName());
		}

		String[] ret = new String[portList.size()];
		if (!portList.isEmpty()) {
			portList.toArray(ret);
		}

		return ret;
	}
}
