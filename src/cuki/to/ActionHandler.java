package cuki.to;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import bin.Pivo;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import cuki.frame.ConnProp;
import cuki.frame.LaminaSetter;
import cuki.frame.Status;
import cuki.utils.ConnectionModbus;
import cuki.utils.DeviceModbus;
import cuki.utils.PollDevice;

public class ActionHandler implements ActionListener {

	private Status frame = null;
	private ConnectionModbus conn = null;
	private DeviceModbus device = null;

	private boolean cmdFechar = false;
	private PollDevice poller;

	public ActionHandler(Status frame, ConnectionModbus conn,
			DeviceModbus device) {
		this.conn = conn;
		this.frame = frame;
		this.device = device;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == frame.configCon) {
			if (conn.getPortName() == null)
				conn.setPortName("COM1");
			JDialog prop = new ConnProp(conn.getPortName(), conn.getBaudRate(),
					conn.getParity(), conn.getDataBits(), conn.getStopBits());
			prop.setLocation(
					(frame.getLocation().x + frame.getSize().width / 2)
							- prop.getSize().width / 2,
					(frame.getLocation().y + frame.getSize().height / 2)
							- prop.getSize().height / 2);
			prop.setVisible(true);

		} else if (e.getSource() == frame.connect) {

			try {
				conn.open();
				cmdFechar = false;
			} catch (Exception e1) {
				String sentence = "Não é possível conectar "
						+ conn.getPortName();
				System.out.println(sentence);
				JOptionPane.showMessageDialog(frame, sentence);
				conn.close();
				System.out.println("fechada");
				cmdFechar = true;
			}

		} else if (e.getSource() == frame.database) {

			System.out.println("Database");

		} else if (e.getSource() == frame.disconnect) {

			poller = null;
			conn.close();
			cmdFechar = true;
			frame.setConnectionStatus(false);

		} else if (e.getSource() == frame.pooling) {

			System.out.println("Pooling");

		} else if (e.getSource() == frame.btnStart) {

			boolean estado = device.getEstadoIrrigacao();
			try {
				poller.setIrrigacao(!estado);
			} catch (ModbusIOException e1) {
				e1.printStackTrace();
			} catch (ModbusSlaveException e1) {
				e1.printStackTrace();
			} catch (ModbusException e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == frame.btnSentdido) {

			boolean velhoSentido = device.getSentido();
			try {
				poller.setSentido(!velhoSentido);
				Pivo.updateFrame();
			} catch (ModbusIOException e1) {
				e1.printStackTrace();
			} catch (ModbusSlaveException e1) {
				e1.printStackTrace();
			} catch (ModbusException e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == frame.btnLamina) {

			LaminaSetter laminaSetter = new LaminaSetter(device.getNrSetores(),
					device.getLaminaMinima(), poller);
			laminaSetter.setVisible(true);

		}

	}

	public void setPoller(PollDevice poller) {
		this.poller = poller;
	}

	public boolean isCmdFechar() {
		return cmdFechar;
	}
}
