package cuki.utils;

import net.wimpi.modbus.util.ModbusUtil;

public class MapeadorDevice {

	public static final int tipoByte = 0;
	public static final int tipoInt = 1;
	public static final int tipoFloat = 2;
	public static final int tipoString = 3;

	public static final int toTipo(int endereco) {
		// if ((endereco >= 0 && endereco <= 7) || endereco == 268
		// || endereco == 269) {
		// return MapeadorDevice.tipoByte;
		// } else if (endereco == 92 || endereco == 108) {
		if (endereco == 92 || endereco == 108) {
			return MapeadorDevice.tipoString;
		} else if (endereco == 24 || endereco == 34 || endereco == 38
				|| endereco == 48 || (endereco >= 64 && endereco <= 74)
				|| endereco == 78 || endereco == 124 || endereco == 126
				|| endereco == 142) {
			return MapeadorDevice.tipoFloat;
		} else
			return MapeadorDevice.tipoInt;
	}

	public static final int toTamanho(int endereco) {
		int tipo = toTipo(endereco);
		if (tipo == MapeadorDevice.tipoString)
			return 16;
		else if (tipo == MapeadorDevice.tipoFloat)
			return 2;
		else
			return 1;
	}

	public static int shortToInt(byte[] in) {
		int ret = 0;
		byte[] aux = new byte[4];

		for (int cont = 0; cont < 2; cont++) {
			aux[3 - cont] = in[1 - cont];
		}

		aux[0] = aux[1] = 0;

		ret = ModbusUtil.registersToInt(aux);

		return ret;
	}

}
