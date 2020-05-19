import java.io.*;

public class Participacao implements Entidade {
    private int idParticipacao;
    private int idUsuario;
    private int idGrupo;
    private int idAmigo;

    public int getIdParticipacao() {
        return idParticipacao;
    }

    public void setIdParticipacao(int idParticipacao) {
        this.idParticipacao = idParticipacao;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(int idAmigo) {
        this.idAmigo = idAmigo;
    }



    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(idParticipacao);
        saida.writeInt(idUsuario);
        saida.writeInt(idGrupo);
        saida.writeInt(idAmigo);

        return dados.toByteArray();
    }

    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);

        this.setIdParticipacao(entrada.readInt());
        this.setIdUsuario(entrada.readInt());
        this.setIdGrupo(entrada.readInt());
        this.setIdAmigo(entrada.readInt());
    }

    public int getId() {
        return getIdParticipacao();
    }

    public void setId(int id) {
        setIdParticipacao(id);
    }

    public String chaveSecundaria() {
        return this.idUsuario+"|"+this.idGrupo;
    }

    public void listarParticipacoes(CRUD<Grupos> bancoGrupos, CRUD<Participacao> bancoParticipacao, CRUD<Usuario> bancoUsuario) throws Exception {
        int[] listaIdsGrupos = bancoGrupos.índiceIndiretoIntInt.read(this.getIdUsuario());

        //já li a lista de ids de grupos associados ao usuario, basta iterar uma  um


        for(int i = 0; i<listaIdsGrupos.length; i++){
            Grupos temp = bancoGrupos.read(listaIdsGrupos[i]);

            //evitar null ptr e verificar se está ativo
            if(temp != null && temp.getAtivo()){

                MyIO.println("Grupo nº "+(i+1));
                temp.mostrar();
            }
        }

        //TODO pergunta ao usuario qual grupo ele quer gerenciar, irei inserir dps

        int idInserido = 0;
        if(idInserido == 0){
            MyIO.println("Ok, retornando");
        }
        else{
            //retirando o 1 pq antes eu adicionei 1, de modo a evitar que o primeiro elemento fosse 0
            Grupos temp = bancoGrupos.read(listaIdsGrupos[idInserido-1]);
            MyIO.println("Grupo escolhido será mostrado abaixo.");
            if(temp == null){
                MyIO.println("O grupo que você inseriu é inválido, provavelmente você inseriu um valor inválido. Retornando.");
            }
            else{
                temp.mostrar();
                MyIO.println(temp.estaSorteado());
                //hora de ler as participacoes
            }

        }
    }

}
