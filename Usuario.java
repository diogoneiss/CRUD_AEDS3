import java.io.*;

public class Usuario implements Entidade {
    private int id;
    private String nome, email, senha;

    public Usuario() {
        id = -1;
        nome = email = senha = "";
    }

    /**
     * Construtor com bytes passados por param
     * 
     * @param bytes
     */
    public Usuario(byte[] bytes){       
        try {
            this.fromByteArray(bytes);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public Usuario(int id, String nome, String email, String senha) {
        this.setId(id);
        this.setEmail(email);
        this.setNome(nome);
        this.setSenha(senha);
    }

    /**
     * Método sem id, usado exclusivamente para criação de objs temporários
     *
     * @param nome
     * @param email
     * @param senha
     */
    public Usuario(String nome, String email, String senha) {
        this.setEmail(email);
        this.setNome(nome);
        this.setSenha(senha);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
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
    public String getEmail() {
        return this.email;
    }
    public String getNome() {
        return this.nome;
    }
    public String getSenha() {
        return this.senha;
    }



    public String chaveSecundaria() {
        return getEmail();
    }

    /**
     * Conversão de usuário para byteArray
     * @return array de bytes com os dados escritos
     * @throws IOException
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(this.id);
        saida.writeUTF(this.nome);
        saida.writeUTF(this.email);
        saida.writeUTF(this.senha);
        return dados.toByteArray();
    }

    /**
     * Leitura de um byte array no usuário vazio.
     * 
     * @param bytes
     * @throws IOException
     */
    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);
        this.setId(entrada.readInt());
        this.setNome(entrada.readUTF());
        this.setEmail(entrada.readUTF());
        this.setSenha(entrada.readUTF());
    }

    // pra poder system.out.print
    public String toString() {
        String retorno = "Nome: " + this.nome + '\n';
        retorno += "Email: " + this.email + '\n';

        return retorno;
    }
}