package cuki.test;

import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

public class TestePorta {

	public static void main(String[] args) {

		Boolean conectou = false;

		SerialParameters params = new SerialParameters();

		params.setPortName("COM8");
		params.setBaudRate(9600);
		params.setDatabits(8);
		params.setParity("None");
		params.setStopbits(1);
		params.setEncoding("rtu");
		params.setEcho(false);

		SerialConnection con = new SerialConnection(params);

		try {
			con.open();
			conectou = true;
		} catch (Exception e) {
			e.printStackTrace();
			conectou = false;
		}

		if (con.isOpen())
			System.out.println("Porta " + params.getPortName() + " aberta.");
		else
			System.out.println("Problemas ao abrir " + params.getPortName()
					+ ".");

		con.close();

		if (con.isOpen())
			System.out.println("Problema ao fechar " + params.getPortName()
					+ ".");
		else if (conectou) {
			System.out.println("Porta " + params.getPortName()
					+ " fechada com sucesso.");
			conectou = false;
		}

		System.exit(0);
	}
}
