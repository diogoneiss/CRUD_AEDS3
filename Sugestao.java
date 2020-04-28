import java.io.*;

public class Sugestao implements Entidade, Cloneable {
	private int idUsuario, idSugestao;
	private String produto, loja, observacoes;
	private float valor;

	public Sugestao() {
		idSugestao = idUsuario = -1;
		produto = loja = observacoes = "";
		valor = -1;
	}

	public Sugestao(byte[] bytes) {
		try {
			this.fromByteArray(bytes);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.out.println("Deu pau na hora de criar a sugestão pelo byte array");
		}

	}

	public Sugestao(String produto, String loja, float valor, String observacoes, int idUsuario) {
		idSugestao = -1;
		setLoja(loja);
		setProduto(produto);
		setValor(valor);
		setObservacoes(observacoes);
		setIdUsuario(idUsuario);
	}
	public Sugestao clone() throws CloneNotSupportedException {
		return (Sugestao) super.clone();
	}


	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * Id da sugestão.
	 *
	 * @return int id da sugestao
	 */
	public int getId() {
		return idSugestao;
	}

	public void setId(int idSugestao) {
		this.idSugestao = idSugestao;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public String getLoja() {
		return loja;
	}

	public void setLoja(String loja) {
		this.loja = loja;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public String chaveSecundaria() {
		return this.idUsuario + "|" + this.produto;
	}

	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream dados = new ByteArrayOutputStream();
		DataOutputStream saida = new DataOutputStream(dados);
		saida.writeInt(this.idSugestao);
		saida.writeInt(this.idUsuario);
		saida.writeUTF(this.produto);
		saida.writeUTF(this.loja);
		saida.writeUTF(this.observacoes);
		saida.writeFloat(this.valor);
		return dados.toByteArray();
	}

	public void fromByteArray(byte[] bytes) throws IOException {
		ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
		DataInputStream entrada = new DataInputStream(dados);
		this.setId(entrada.readInt());
		this.setIdUsuario(entrada.readInt());
		this.setProduto(entrada.readUTF());
		this.setLoja(entrada.readUTF());
		this.setObservacoes(entrada.readUTF());
		this.setValor(entrada.readFloat());
	}

	@Override
	public String toString(){
		
		String concatenated = String.format(
			"%s\n%s\nR$ %.2f\n%s\n",
			/*1*/this.getProduto() ,
			/*2*/this.getLoja(),
			/*3*/this.getValor(),
			/*4*/this.getObservacoes()
		);
		return concatenated;
	}
}
