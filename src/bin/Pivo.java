package bin;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
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
	static PollDevice poller = null;

	public static void setConnection(String[] connProperties) {
		conManager.setParameters(connProperties[0], connProperties[1],
				connProperties[2], connProperties[3], connProperties[4]);
		System.out.println(connProperties[0]);
		System.out.println(connProperties[1]);
	}

	public static void updateFrame() throws ModbusIOException,
			ModbusSlaveException, ModbusException {

		System.out.println("Angulo Atual: " + plc.getAnguloAtual());
		System.out.println("Sentido: " + plc.getSentido());
		System.out.println("Estado: " + plc.getEstado());
		System.out.println("Setor Atual: " + plc.getSetorAtual());
		System.out.println("Nr Setores: " + plc.getNrSetores());
		System.out.println("Fase Atual: " + plc.getContaFase());
		System.out.println("Numero de Fases: " + plc.getNrFases());
		System.out.println("Lâmina Atual: " + plc.getLaminaAtual());
		System.out.printf("Tempo Restante: %02d:%02d\n",
				plc.getTempoRestanteHoras(), plc.getTempoRestanteMinutos());
		System.out.println("Ciclo Atual: " + plc.getCicloAtual());

		frame.updateStatus(plc.getAnguloAtual(), plc.getSentido(),
				plc.getEstado(), plc.getSetorAtual(), plc.getNrSetores(),
				plc.getContaFase(), plc.getNrFases(), plc.getLaminaAtual(),
				plc.getTempoRestanteHoras(), plc.getTempoRestanteMinutos(),
				plc.getCicloAtual(), plc.getAnguloSetores());
	}

	public static void open() throws Exception {

		conManager.open();

		if (conManager.isDoorOpen()) {
			System.out.println("Conectado a " + conManager.getPortName()
					+ " @ " + conManager.getBaudRate() + " : "
					+ conManager.getDataBits() + conManager.getParity()
					+ conManager.getStopBits());
			poller = new PollDevice(plc, conManager.getConection(), 40);
			System.out.println("Criado poller " + poller.getClass());
			poller.setDebug(false);
			frame.setConnectionStatus(true);
			handler.setPoller(poller);
			poller.start();
		}

	}

	public static void close() {

		poller.interrupt();
		poller = null;
		conManager.close();
		frame.setConnectionStatus(false);
		System.out.println("Conexão Fechada");

	}

	public static void main(String[] args) {

		Pivo thread = new Pivo();

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}

		String[] portas = PortLister.getPorts();

		if (portas.length != 0)
			conManager = new ConnectionModbus(portas[0]);
		else
			conManager = new ConnectionModbus("COM1");

		plc = new DeviceModbus(1, 147);
		System.out.println("Criado dispositivo plc" + plc.getDeviceID() + " "
				+ plc.getNrItens() + " ites");

		frame = new Status();
		handler = new ActionHandler(frame, conManager, plc);

		frame.setAction(handler);
		frame.setVisible(true);

		while (true) {

			if (conManager.isDoorOpen() && poller != null) {
				try {
					Pivo.updateFrame();
				} catch (ModbusIOException e1) {
					System.out.println("1");
					conManager.close();
					e1.printStackTrace();
				} catch (ModbusSlaveException e1) {
					System.out.println("2");
					e1.printStackTrace();
				} catch (ModbusException e1) {
					System.out.println("3");
					e1.printStackTrace();
				}

				synchronized (thread) {
					try {
						thread.wait(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} else if (!conManager.isDoorOpen()) {

			}
		}
	}
}
