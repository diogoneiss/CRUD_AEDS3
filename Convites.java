import java.io.*;

public class Convites implements Entidade{
    private int idConvite, idGrupo;
    private String email;
    private long momentoConvite;
    private byte estado;
    public Convites(){
        idConvite= idGrupo=-1;
        email="";
        momentoConvite=-1;
        estado=0;
    }
    public Convites(int idC, int idG, String em, long moment, byte est){
        idConvite= idC;
	idGrupo= idG;
        email=em;
        momentoConvite=moment;
        estado=est;
    }
    public Convites(byte[] bytes) throws IOException{
        this.fromByteArray(bytes);
    }
    public void setId(int idConvite){
        this.idConvite=idConvite;
    }
    public int getId(){
        return this.idConvite;
    }
    public void setIdGrupo(int idGrupo){
        this.idGrupo=idGrupo;
    }
    public int getIdGrupo(){
        return this.idGrupo;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setMomentoConvite(long momentoConvite){
        this.momentoConvite=momentoConvite;
    }
    public long getMomentoConvite(){
        return this.momentoConvite;
    }
    public void setEstado(byte estado){
        this.estado=estado;
    }
    public byte getEstado(){
        return this.estado;
    }
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(idConvite);
        saida.writeInt(idGrupo);
        saida.writeUTF(email);
        saida.writeLong(momentoConvite);
        saida.writeByte(estado);
        return dados.toByteArray();
    }
    public void fromByteArray(byte[] bytes) throws IOException{
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);
        this.setId(entrada.readInt());
        this.setIdGrupo(entrada.readInt());
        this.setEmail(entrada.readUTF());
        this.setMomentoConvite(entrada.readLong());
        this.setEstado(entrada.readByte());
    }
    public String chaveSecundaria(){
        return this.idGrupo + "|" + this.email;
    }

}
