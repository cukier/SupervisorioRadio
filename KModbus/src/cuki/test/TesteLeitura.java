package cuki.test;

import net.wimpi.modbus.ModbusException;
import cuki.utils.ConnectionModbus;
import cuki.utils.DeviceModbus;
import cuki.utils.ItemModbus;
import cuki.utils.PollDevice;

public class TesteLeitura {

	public static void main(String[] args) {

		ConnectionModbus conManager = new ConnectionModbus("COM2");
		DeviceModbus dev = new DeviceModbus(1, 147);
		PollDevice poller = null;
		int cont = 1;

		try {
			conManager.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (conManager.isDoorOpen()) {

			poller = new PollDevice(dev, conManager.getConection(), 40);

			while (cont-- > 0) {

				try {
					poller.poll(dev.getItens());
				} catch (ModbusException e) {
					conManager.close();
					break;
				}

				for (ItemModbus i : dev.getItens()) {
					System.out.println("======= Word " + i.getEndereco()
							+ " @ " + i.getTamanho() + " ========");
					for (byte b : i.getData())
						System.out.printf("%3d ", b);
					System.out.println("");
				}

				System.out.println("Angulo Atual: " + dev.getAnguloAtual());
				System.out.println("Sentido: " + dev.getSentido());
				System.out.println("Estado: " + dev.getEstado());
				System.out.println("Setor Atual: " + dev.getSetorAtual());
				System.out.println("Nr Setores: " + dev.getNrSetores());
				System.out.println("Fase Atual: " + dev.getContaFase());
				System.out.println("Numero de Fases: " + dev.getNrFases());
				System.out.println("Lâmina Atual: " + dev.getLaminaAtual());
				System.out.printf("Tempo Restante: %02d:%02d\n",
						dev.getTempoRestanteHoras(),
						dev.getTempoRestanteMinutos());
				System.out.println("Ciclo Atual: " + dev.getCicloAtual());

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {

				}

			}
			conManager.close();
			System.out
					.println("Porta " + conManager.getPortName() + " fechada");
			System.exit(0);

		}
	}
}
