import java.io.*;

public class Sugestao implements Entidade {
    private int idUsuario, idSugestao;
    private String produto, loja, observacoes;
    private float valor;

    public Sugestao(){
        idSugestao=idUsuario=-1;
        produto=loja=observacoes="";
        valor=-1;
    }

    public Sugestao(byte[] bytes){       
        try {
            this.fromByteArray(bytes);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void setId(int idSugestao){
        this.idSugestao=idSugestao;
    }
    public void setIdUsuario(int idUsuario){
        this.idUsuario=idUsuario;
    }
    public void setProduto(String produto){
        this.produto=produto;
    }
    public void setLoja(String loja){
        this.loja=loja;
    }
    public void setObservacoes(String observacoes){
        this.observacoes=observacoes;
    }
    public void setValor(float valor){
        this.valor=valor;
    }
    public int getId(){
        return this.idSugestao;
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
}
