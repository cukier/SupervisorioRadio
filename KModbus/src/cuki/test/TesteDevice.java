package cuki.test;

import cuki.utils.DeviceModbus;

public class TesteDevice {

	public static void main(String[] args) {

		DeviceModbus dev = new DeviceModbus(1, 148);

		System.out.println("Dispositivo " + dev.getDeviceID() + "criado com "
				+ dev.getNrItens() + " itens.");

		for (int cont = 0; cont < dev.getNrItens(); cont++) {
			// System.out.println("Item: " + cont + " Tipo: "
			// + MapeadorDevice.toTipo(dev.getItem(cont).getEndereco())
			// + " End: " + dev.getItem(cont).getEndereco() + " tamanho: "
			// + dev.getItem(cont).getTamanho());
		}

	}

}
