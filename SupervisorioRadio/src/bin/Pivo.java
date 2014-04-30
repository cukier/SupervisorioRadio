package bin;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.wimpi.modbus.ModbusException;
import cuki.frame.Status;
import cuki.to.ActionHandler;
import cuki.utils.ConnectionModbus;
import cuki.utils.DeviceModbus;
import cuki.utils.PollDevice;
import cuki.utils.PortLister;

public class Pivo {

	static ConnectionModbus conManager = null;
	static Status frame = null;
	static ActionHandler handler = null;
	static DeviceModbus plc = null;

	static public void setConnection(String[] connProperties) {
		conManager.setParameters(connProperties[0], connProperties[1],
				connProperties[2], connProperties[3], connProperties[4]);
		System.out.println(connProperties[0]);
		System.out.println(connProperties[1]);
	}

	public static void main(String[] args) {

		Pivo thread = new Pivo();
		PollDevice poller = null;

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}

		String[] ports = PortLister.getPorts();

		for (String porta : ports) {
			System.out.println(porta);
		}

		if (ports.length == 0) {
			System.out.println("Não existe porta serial");
			System.exit(2);
		} else {
			for (String aux : ports)
				System.out.println(aux);
		}
		conManager = new ConnectionModbus(ports[0]);

		plc = new DeviceModbus(1, 147);
		System.out.println("Criado dispositivo plc" + plc.getDeviceID() + " "
				+ plc.getNrItens() + " ites");

		frame = new Status();
		handler = new ActionHandler(frame, conManager, plc);

		frame.setAction(handler);
		frame.setVisible(true);

		boolean ctrl = true;
		while (true) {
			if (ctrl && conManager.isDoorOpen()) {
				ctrl = false;
				System.out.println("Conectado a " + conManager.getPortName()
						+ " @ " + conManager.getBaudRate() + " : "
						+ conManager.getDataBits() + conManager.getParity()
						+ conManager.getStopBits());
				poller = new PollDevice(plc, conManager.getConection(), 40);
				System.out.println("Criado poller " + poller.getClass());
				poller.setDebug(false);
				frame.setConnectionStatus(true);
				handler.setPoller(poller);
			} else if (!ctrl && !conManager.isDoorOpen()) {
				ctrl = true;
				poller = null;
				System.out.println("Desconectado");
			}
			if (conManager.isDoorOpen() && poller != null) {
				try {
					poller.poll(plc.getItens());
				} catch (ModbusException e) {
					if (!handler.isCmdFechar()) {
						conManager.close();
						frame.setConnectionStatus(false);
						JOptionPane.showMessageDialog(frame,
								"Nenhum retorno do plc");
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
					conManager.close();
					frame.setConnectionStatus(false);
					System.out.println("Nenhum plc conectado");
				}

				System.out.println("Angulo Atual: " + plc.getAnguloAtual());
				System.out.println("Sentido: " + plc.getSentido());
				System.out.println("Estado: " + plc.getEstado());
				System.out.println("Setor Atual: " + plc.getSetorAtual());
				System.out.println("Nr Setores: " + plc.getNrSetores());
				System.out.println("Fase Atual: " + plc.getContaFase());
				System.out.println("Numero de Fases: " + plc.getNrFases());
				System.out.println("Lâmina Atual: " + plc.getLaminaAtual());
				System.out.printf("Tempo Restante: %02d:%02d\n",
						plc.getTempoRestanteHoras(),
						plc.getTempoRestanteMinutos());
				System.out.println("Ciclo Atual: " + plc.getCicloAtual());

				frame.updateStatus(plc.getAnguloAtual(), plc.getSentido(),
						plc.getEstado(), plc.getSetorAtual(),
						plc.getNrSetores(), plc.getContaFase(),
						plc.getNrFases(), plc.getLaminaAtual(),
						plc.getTempoRestanteHoras(),
						plc.getTempoRestanteMinutos(), plc.getCicloAtual(),
						plc.getAnguloSetores());

				synchronized (thread) {
					try {
						thread.wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
