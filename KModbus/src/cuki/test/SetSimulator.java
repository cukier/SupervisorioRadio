package cuki.test;

import net.wimpi.modbus.ModbusException;
import cuki.utils.ConnectionModbus;
import cuki.utils.DeviceModbus;
import cuki.utils.PollDevice;

public class SetSimulator {

	public static void delayMs(Object thread, int mils) {
		synchronized (thread) {
			try {
				thread.wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(2);
			}
		}
	}

	public static void poll(PollDevice poller, DeviceModbus device,
			ConnectionModbus conManager) {
		try {
			poller.poll(device.getItens());
		} catch (ModbusException e) {
			e.printStackTrace();
			System.out.println("Impossivel iniciar polling");
			conManager.close();
			System.exit(1);
		}
	}

	public static void main(String[] args) {

		ConnectionModbus conManager = new ConnectionModbus("COM8");
		// DeviceModbus dev = new DeviceModbus(1, 147);
		// PollDevice poller = null;

		try {
			conManager.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (conManager.isDoorOpen()) {

			// poller = new PollDevice(dev, conManager.getConection(), 40);

			int[] data = new int[200];

			for (int cont = 0; cont < data.length; cont++) {
				data[cont] = cont;
			}

			// try {
			// poller.writeMultipleRegister(0, data);
			// } catch (ModbusIOException e) {
			// e.printStackTrace();
			// } catch (ModbusSlaveException e) {
			// e.printStackTrace();
			// } catch (ModbusException e) {
			// e.printStackTrace();
			// }

			conManager.close();
			System.exit(0);
		}
	}
}
