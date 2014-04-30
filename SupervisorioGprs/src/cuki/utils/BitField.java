package cuki.utils;

public class BitField {

	private int value;
	// %MB4 e %MB5
	public static final int zerarContador = 0;
	public static final int sAvanco = 1;
	public static final int sReversao = 2;
	public static final int sUltimaTorre = 3;
	public static final int sBombaInjetora = 4;
	public static final int saidaSeguranca = 5;
	public static final int inicioIrriga = 6;
	public static final int sentido = 7;
	public static final int regimeFuncionamento = 8;
	public static final int alterarProgramacao = 9;
	public static final int salvaFaseAtiva = 10;
	public static final int BinjetoraSet = 11;
	public static final int BinjetoraGet = 12;
	public static final int salvaBInjetora = 13;
	public static final int saidaPressostato = 14;
	public static final int ultimoSetorRestrito = 15;
	// %MB0
	public static final int saidaAutomatico = 0;
	public static final int AjustaHora = 1;
	public static final int picoEnergetico = 2;
	public static final int horarioTrabalhoOk = 3;
	public static final int pressostatoOk = 4;
	public static final int partidaSeca = 5;
	public static final int sBombaBooster = 6;
	public static final int saidaBDagua = 7;
	// %MB6
	public static final int bBoosterOk = 0;
	public static final int bInjetoraAux = 1;
	public static final int alarmeAlinhamento = 2;
	public static final int alarmePressao = 3;
	public static final int pararIrriga = 4;
	public static final int erroLaminaZero = 5;
	public static final int emEspera = 6;
	public static final int turnoRegaOk = 7;
	// %MB269
	public static final int bBoosterEn = 0;
	public static final int MM_PORCENTO = 0;
	public static final int salvarSetor = 0;
	public static final int carregarSetor = 0;
	public static final int horarioTrabalhoEn = 0;
	public static final int voltarSeco = 0;

	public BitField(int value) {
		this.value = value;
	}

	public BitField setBit(int bitNr) throws IndexOutOfBoundsException {
		if (bitNr <= 15)
			this.value |= 1 << bitNr;
		else
			throw new IndexOutOfBoundsException(
					"Index deve ser igual ou nemor que 15. Recebido: " + bitNr);
		return this;
	}

	public void resetBit(int bitNr) {
		this.value &= ~(1 << bitNr);
	}

	public boolean getBit(int bitNr) {
		return ((this.value & (1 << bitNr)) != 0);
	}

	public int getByte() {
		return this.value;
	}

	public void setByte(int value) throws IllegalArgumentException {
		if (value <= 65535)
			this.value = value;
		else
			throw new NumberFormatException(
					"O valor deve ser menor ou igual a 65535");
	}

	public String toString() {
		String retorno = null;

		retorno = "15: " + String.valueOf(getBit(15));
		for (int cont = 14; cont >= 0; --cont) {
			retorno += " " + cont + ":" + String.valueOf(getBit(cont));
		}

		return retorno;
	}
}
