package cuki.exemple;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

public class SerialMaster {
	public static void main(String[] args) {

		SerialParameters params = new SerialParameters();

		params.setPortName("COM8");
		params.setBaudRate(9600);
		params.setDatabits(8);
		params.setParity("None");
		params.setStopbits(1);
		params.setEncoding("rtu");
		params.setEcho(false);

		SerialConnection con = new SerialConnection(params);

		if (con.isOpen()) {
			System.out.println("Porta " + con.getSerialPort()
					+ " aberta, fechando...");
			con.close();
		}

		try {
			con.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (con.isOpen()) {

			// ModbusCoupler.createModbusCoupler(null);
			ModbusCoupler.getReference().setUnitID(1);

			ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest();
			req.setUnitID(1);
			req.setReference(0);
			req.setWordCount(10);
			req.setHeadless();

			ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
			trans.setRequest(req);
			trans.setTransDelayMS(100);
			trans.setRetries(10);

			try {
				trans.execute();
			} catch (ModbusIOException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (ModbusSlaveException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (ModbusException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Resposta:");

			ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans
					.getResponse();

			for (int n = 0; n < res.getWordCount(); n++) {
				System.out.println(res.getRegisterValue(n));
			}
			System.out.println("");
		}

		con.close();
		System.out.println("Programa encerrado");
		System.exit(0);
	}
}
