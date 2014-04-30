package cuki.test;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusSlaveException;
import cuki.utils.ConnectionModbus;
import cuki.utils.DeviceModbus;
import cuki.utils.PollDevice;

public class TesteEscrita {

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

		TesteEscrita thread = new TesteEscrita();
		ConnectionModbus conManager = new ConnectionModbus("COM5");
		DeviceModbus dev = new DeviceModbus(1, 147);
		PollDevice poller = null;

		try {
			conManager.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (conManager.isDoorOpen()) {

			poller = new PollDevice(dev, conManager.getConection(), 40);

			TesteEscrita.delayMs(thread, 500);
			TesteEscrita.poll(poller, dev, conManager);

			int word2 = dev.getWord2();

			System.out.println("Word2:");
			System.out.println(Integer.toBinaryString(word2));

			word2 |= 64;
			word2 &= 0xFFFF;

			System.out.println("Escrever");
			System.out.println(Integer.toBinaryString(word2));

			try {
				poller.writeRegister(2, word2);
			} catch (ModbusSlaveException e) {
				e.printStackTrace();
			} catch (ModbusException e) {
				e.printStackTrace();
			}

			TesteEscrita.delayMs(thread, 500);
			TesteEscrita.poll(poller, dev, conManager);

			word2 = dev.getWord2();

			System.out.println("Word2:");
			System.out.println(Integer.toBinaryString(word2));

			word2 &= ~64;
			word2 &= 0xFFFF;

			System.out.println("Escrever");
			System.out.println(Integer.toBinaryString(word2));

			try {
				poller.writeRegister(2, word2);
			} catch (ModbusSlaveException e) {
				e.printStackTrace();
			} catch (ModbusException e) {
				e.printStackTrace();
			}

			TesteEscrita.delayMs(thread, 500);
			TesteEscrita.poll(poller, dev, conManager);

			word2 = dev.getWord2();

			System.out.println("Word2:");
			System.out.println(Integer.toBinaryString(word2));

			conManager.close();
			System.exit(0);
		}
	}
}
