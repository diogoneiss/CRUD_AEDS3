import java.io.*;
import java.util.HashMap;

public class Participacao implements Entidade {
    private int idParticipacao;
    private int idUsuario;
    private int idGrupo;
    private int idAmigo;

    public static ArvoreBMais_Int_Int arvoreIntIntGrupoParticipacao;

    static {
        try {
            arvoreIntIntGrupoParticipacao = new ArvoreBMais_Int_Int(10,
                        "dados" + "/" + "arvoreBIntInt." + "indiceIndiretoParticipacao1" + ".idx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArvoreBMais_Int_Int arvoreIntIntUsuarioParticipacao;

    static {
        try {
            arvoreIntIntUsuarioParticipacao = new ArvoreBMais_Int_Int(10,
                        "dados" + "/" + "arvoreBIntInt." + "indiceIndiretoParticipacao2" + ".idx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Participacao(int idUsuario, int idGrupo, int idAmigo){
        setIdUsuario(idUsuario);
        setIdGrupo(idGrupo);
        setIdAmigo(idAmigo);
    }
    public Participacao(byte[] bytes) {
        try{
            this.fromByteArray(bytes);
        }catch(Exception e){
            MyIO.println(e.getMessage());
        }
    }

    public static void inserir(Participacao temp) throws IOException {
            arvoreIntIntGrupoParticipacao.create(temp.getIdGrupo(), temp.getId());
            arvoreIntIntUsuarioParticipacao.create(temp.getIdUsuario(), temp.getId());
    }

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

    public void listarParticipacoes() throws Exception {
        CRUD<Grupos> bancoGrupos = Inscricao.grupos;
        CRUD<Participacao> bancoParticipacao = Inscricao.partipacao;
        CRUD<Usuario> bancoUsuario = Inscricao.usuarios;


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
        int idInserido = 0;
        MyIO.print("Digite o número do grupo que você quer gerenciar.\nOpção: ");
        idInserido = MyIO.readInt();


        // passo3
        if(idInserido == 0){
            MyIO.println("Ok, retornando");
        }
        else{
            // passo 4
            // retirando o 1 pq antes eu adicionei 1, de modo a evitar que o primeiro elemento fosse 0
            Grupos temp = bancoGrupos.read(listaIdsGrupos[idInserido-1]);
            MyIO.println("Grupo escolhido será mostrado abaixo.");
            if(temp == null){
                MyIO.println("O grupo que você inseriu é inválido, provavelmente você inseriu um valor inválido. Retornando.");
            }
            else{
                // passo 5
                temp.mostrar();
                MyIO.println(temp.estaSorteado());

                //passo 6
                //hora de ler as participacoes, buscando o id do grupo inserido, dentro de uma das árvores estáticas da classe.
                // a árvore b+ int int serve pra isso.
                int[] participacoesDoUser = Participacao.arvoreIntIntGrupoParticipacao.read(idInserido-1);

                for (int i = 0; i < participacoesDoUser.length; i++) {
                    Participacao tempP = bancoParticipacao.read(participacoesDoUser[i]);

                    //evitar null ptr
                    if(tempP != null) {

                        //pegando dados do usuario
                        Usuario tempU = bancoUsuario.read(tempP.getIdUsuario());

                        if (tempU != null)
                            MyIO.println(tempU.toString());
                    }
                }
            }

        }
    }
    public void removerParticipante() throws Exception {
        CRUD<Grupos> bancoGrupos = Inscricao.grupos;
        CRUD<Participacao> bancoParticipacao = Inscricao.partipacao;
        CRUD<Usuario> bancoUsuario = Inscricao.usuarios;

        HashMap<Integer, Integer> presenteadosPor = new HashMap<>();


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
        int idInserido = 0;
        MyIO.print("Digite o número do grupo que você quer gerenciar.\nOpção: ");
        idInserido = MyIO.readInt();


        // passo3
        if(idInserido == 0){
            MyIO.println("Ok, retornando");
        }
        else{
            // passo 4
            // retirando o 1 pq antes eu adicionei 1, de modo a evitar que o primeiro elemento fosse 0
            Grupos tempG = bancoGrupos.read(listaIdsGrupos[idInserido-1]);
            MyIO.println("Grupo escolhido será mostrado abaixo.");
            if(tempG == null){
                MyIO.println("O grupo que você inseriu é inválido, provavelmente você inseriu um valor inválido. Retornando.");
            }
            else{
                // passo 5
                tempG.mostrar();
                MyIO.println(tempG.estaSorteado());

                //passo 6
                //hora de ler as participacoes, buscando o id do grupo inserido, dentro de uma das árvores estáticas da classe.
                // a árvore b+ int int serve pra isso.
                int[] participacoesDoUser = Participacao.arvoreIntIntGrupoParticipacao.read(idInserido-1);

                for (int i = 0; i < participacoesDoUser.length; i++) {
                    Participacao tempP = bancoParticipacao.read(participacoesDoUser[i]);

                    //evitar null ptr
                    if(tempP != null) {

                        //pegando dados do usuario
                        Usuario tempU = bancoUsuario.read(tempP.getIdUsuario());

                        if (tempU != null) {
                            MyIO.println("Id: " + (i+1)+ tempU.toString());
                            if(tempG.getSorteado()){
                                // passo 7.4.1
                                presenteadosPor.put(tempP.getIdUsuario(), tempP.getIdAmigo());
                            }
                        }
                    }
                }

                //passo 8

                MyIO.print("Insira o usuario da participacao que voce deseja remover, baseado na lista acima.\nOpção: ");
                int idEscolhidoUsuario = MyIO.readInt();


                int idParticipacaoComUserASerRemovido = participacoesDoUser[idEscolhidoUsuario-1];

                //participacao com o usuario que será removido do grupo.
                Participacao removido = bancoParticipacao.read(idParticipacaoComUserASerRemovido);

                // passo 9
                if(tempG.getSorteado()){


                    int amigoPrejudicado = removido.getIdAmigo();

                    int amigoQuePresentearia = presenteadosPor.get(amigoPrejudicado);

                    // pegar a participacao que será atualizada.
                    /**
                     * Antigamente:
                     *
                     * Removido -> AmigoPrejudicado
                     * Usuario que nao tem a quem presentear -> Removido
                     *
                     * Agora:
                     *
                     * Usuario que nao tem a quem presentear -> Amigo Prejudicado
                     */
                    Participacao tempP = bancoParticipacao.read(amigoQuePresentearia);

                    tempP.setIdAmigo(amigoPrejudicado);

                    bancoParticipacao.update(tempP);
                }
                // passo 10
                bancoParticipacao.delete(removido.getId());
                arvoreIntIntUsuarioParticipacao.delete(removido.getIdUsuario(), removido.getId());
                arvoreIntIntGrupoParticipacao.delete(tempG.getId(), removido.getId());
                MyIO.println("Pronto! remoção concluída.");

            }
        }
    }

}
