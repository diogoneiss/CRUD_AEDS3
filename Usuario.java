import java.io.*;

public class Usuario{
    private int id;
    private String nome, email, senha;
    public Usuario(int id, String nome,String email, String senha) {
        this.setId(id);
        this.setEmail(email);
        this.setNome(nome);
        this.setSenha(senha);
    }
    public void setId( int id) {
        this.id = id;
    }

    public void setNome( String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getId() {
        return this.id;
    }

    public String chaveSecundaria() {
        return this.email;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(this.id);
        saida.writeUTF(this.nome);
        saida.writeUTF(this.email);
        saida.writeUTF(this.senha);
        return dados.toByteArray();
    }

    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);
        this.setId(entrada.readInt());
        this.setNome(entrada.readUTF());
        this.setEmail(entrada.readUTF());
        this.setSenha(entrada.readUTF());
      }
}
class Crud{
    RandomAccessFile usuarios;
    public Crud() throws Exception{
        usuarios=new RandomAccessFile("usuarios", "rw");
        if (usuarios.length()==0) {
            usuarios.writeInt(0);
        }
    }
    public int creat(String nome, String email, String senha)throws Exception{
        usuarios.seek(0);
        int id=usuarios.readInt()+1;//ler o ultimo id usado e somar um para obter o novo id
        Usuario temp = new Usuario(id,nome,email,senha);
        long indereco=usuarios.length();
        usuarios.seek(usuarios.length());//aponta o ponteiro para o fim do arquivo
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeChar(' ');//escreve a lapide
        saida.writeInt(temp.toByteArray().length);
        usuarios.write(dados.toByteArray());//escreve o tamanho do registro
        usuarios.write(temp.toByteArray());//escreve o registro
        usuarios.seek(0);
        usuarios.writeInt(id);//atualiza o id
        //idexar
        return id;
    }
}