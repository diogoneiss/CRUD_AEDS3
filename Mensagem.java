import java.io.*;

public class Mensagem implements Entidade{
    private int id, idGrupo, idCriador;
    private String mensagem;
    public Mensagem(){
        id=idGrupo=idCriador=-1;
        mensagem="";
    }
    public Mensagem(byte[] x) throws IOException{
        this.fromByteArray(x);
    }
    public Mensagem(int id, int idGrupo, int idCriador, String mensagem) {
        this.setId(id);
        this.setIdGrupo(idGrupo);
        this.setIdCriador(idCriador);
        this.setMensagem(mensagem);
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setIdGrupo(int x){
        this.idGrupo=x;
    }
    public void setIdCriador(int x){
        this.idCriador=x;
    }
    public void setMensagem(String x){
        this.mensagem=x;
    }
    public int getIdGrupo(){
        return this.idGrupo;
    }
    public int getIdCriador(){
        return this.idCriador;
    }
    public String getMensagem(){
        return this.mensagem;
    }
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(this.getId());
        saida.writeInt(this.getIdGrupo());
        saida.writeInt(this.getIdCriador());
        saida.writeUTF(this.getMensagem());
        return dados.toByteArray();
    }
    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);
        this.setId(entrada.readInt());
        this.setIdGrupo(entrada.readInt());
        this.setIdCriador(entrada.readInt());
        this.setMensagem(entrada.readUTF());
    }
    public String chaveSecundaria() {
		return this.id + "|" + this.idGrupo + "|" + this.idCriador;
	}
}