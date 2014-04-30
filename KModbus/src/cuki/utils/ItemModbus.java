package cuki.utils;

public class ItemModbus {

	private int tamanho;
	private int endereco;
	private byte[] data;

	public ItemModbus(int endereco) {
		this.endereco = endereco;
		this.tamanho = MapeadorDevice.toTamanho(endereco);
		data = new byte[this.tamanho * 2];
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public int getEndereco() {
		return endereco;
	}

	public void setEndereco(int endereco) {
		this.endereco = endereco;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return this.data;
	}
}
