import java.io.*;
import java.util.Calendar;  
 
public class Grupos implements Entidade{
    private int idGrupo, idUsuario;
    private String nome,localEncontro, observacoes;
    private long momentoSorteio, momentoEncontro;
    private float valor;
    private boolean sorteado, ativo;

    public Grupos() {
        idGrupo = idUsuario = -1;
        nome=localEncontro= observacoes="";
        momentoSorteio= momentoEncontro=-1;
        valor=-1;
        sorteado=false;
        ativo=false;
    }
    public Grupos(byte[] bytes) {
        try{
        this.fromByteArray(bytes);
        }catch(Exception e){
            MyIO.println(e.getMessage());
        }
    }
    public Grupos(int idGrupo,int IdUsuario,String nome,String localEncontro,String observacoes,long momentoSorteio,long momentoEncontro,float valor,boolean sorteado, boolean ativo){
        this.setId(idGrupo);
        this.setIdUsuario(idUsuario);
        this.setNome(nome);
        this.setLocalEncontro(localEncontro);
        this.setObservacoes(observacoes);
        this.setAtivo(ativo);
        this.setMomentoEncontro(momentoEncontro);
        this.setMomentoSorteio(momentoSorteio);
        this.setValor(valor);
        this.setSorteado(sorteado);
    }
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(idGrupo);
        saida.writeInt(idUsuario);
        saida.writeUTF(nome);
        saida.writeUTF(localEncontro);
        saida.writeUTF(observacoes);
        saida.writeLong(momentoSorteio);
        saida.writeLong(momentoEncontro);
        saida.writeFloat(valor);
        saida.writeBoolean(sorteado);
        saida.writeBoolean(ativo);
        return dados.toByteArray();
    }
    public void fromByteArray(byte[] bytes) throws IOException{
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);
        this.setId(entrada.readInt());
        this.setIdUsuario(entrada.readInt());
        this.setNome(entrada.readUTF());
        this.setLocalEncontro(entrada.readUTF());
        this.setObservacoes(entrada.readUTF());
        this.setMomentoSorteio(entrada.readLong());
        this.setMomentoEncontro(entrada.readLong());
        this.setValor(entrada.readFloat());
        this.setSorteado(entrada.readBoolean());
        this.setAtivo(entrada.readBoolean());
    }
    public String chaveSecundaria(){
        return this.idUsuario + "|" + this.nome;
    }
    public void setSorteado(boolean x){
        this.sorteado=x;
    }
    public void setAtivo(boolean x){
        this.ativo=x;
    }
    public String getNome(){
        return nome;
    }
    public boolean getAtivo(){
        return this.ativo;
    }
    public void setId(int idGrupo){
        this.idGrupo=idGrupo;
    }
    public int getId(){
        return this.idGrupo;
    }
    public void setIdUsuario(int idUsuario){
        this.idUsuario=idUsuario;
    }
    public void setNome(String x){
        this.nome=x;
    } 
    public void setLocalEncontro(String x){
        this.localEncontro=x;
    }
    public String getLocalEncontro(){
        return this.localEncontro;
    }
    public void setObservacoes(String x){
        this.observacoes=x;
    }
    public String getObservacoes(){
        return this.observacoes;
    }
    public void setMomentoSorteio(long x){
        this.momentoSorteio=x;
    }
    public long getMomentoSorteio(){
        return this.momentoSorteio;
    }
    public void setMomentoEncontro(long x){
        this.momentoEncontro=x;
    }
    public long getMomentoEncontro(){
        return this.momentoEncontro;
    }
    public void setValor(float valor){
        this.valor=valor;
    }
    public float getValor(){
        return this.valor;
    }
    public void mostrar(){
        Calendar sorteio = Calendar.getInstance();
		sorteio.setTimeInMillis(getMomentoSorteio());
		Calendar encontro = Calendar.getInstance();
		encontro.setTimeInMillis(getMomentoEncontro());
		MyIO.println(getNome()+" | Dia do Sorteio: "+sorteio.get(Calendar.DATE)+" do "+sorteio.get(Calendar.MONTH)+ " de "+sorteio.get(Calendar.YEAR)+" as "+sorteio.get(Calendar.HOUR)+" horas "+" | dia do encontro "+encontro.get(Calendar.DATE)+" do "+encontro.get(Calendar.MONTH)+ " de "+encontro.get(Calendar.YEAR)+" as "+encontro.get(Calendar.HOUR)+" horas "+" | local de encontro: "+getLocalEncontro()+ " | valor: "+getValor()+"  | Observações: "+getObservacoes());
    }

}
