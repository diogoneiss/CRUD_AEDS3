/*
    TODO: Fazer as devidas alterações na função main() para verificar se existem Novos Convites
    Alterações na função main() que eu sugiro:
    Antes de escrever o Menu de Início,
    criar um objeto da classe Inscrição e verificar se idsConvites.length > 0,
    caso ela retorne verdadeiro, escrever "Novos Convites: " + idsConvites.length,
    caso não, escrever "Novos Convites: 0",
    Quando o usuário escolher essa opção, evocar a função visualizarNovosContives()
 */

/*
    Eu não pude compilar o código pelo fato da ausência da classe Convites
    Se aparecer algum erro é só me avisar
    Apague esse comentário e tudo acima dele assim que o projeto estiver pronto
 */

class Inscricao {
    private ArvoreBMais_ChaveComposta_String_Int indiceInvertido;
    private CRUD<Convites> convites;
    private CRUD<Grupos> grupos;
    private CRUD<Usuario> usuarios;
    private Usuario usuario;
    public int[] idsConvites;

    public Inscricao(Usuario usuario) throws Exception {
        this.usuario = usuario;
        this.idsConvites = indiceInvertido.read(this.usuario.chaveSecundaria());
    }

    public void visualizarNovosConvites() throws Exception {
        if (idsConvites.length > 0) {
            MyIO.println("Você possui novos convites!");
            MyIO.println("Escolha qual convite deseja aceitar ou recusar:\n");

            for (int i = 0; i < idsConvites.length; i++) {
                lerConvite(idsConvites[i], i + 1);
            }

            MyIO.print("Convite ou Zero [0] para voltar ao menu principal: ");
            int conviteEscolhido = MyIO.readInt();

            if (conviteEscolhido != 0) {
                lerConvite(idsConvites[conviteEscolhido - 1], conviteEscolhido);

                char escolha;
                boolean erroEscolha;

                do {
                    MyIO.println("Aceitar [A] ou Recusar [R] esse convite ou Voltar [V] aos convites: ");
                    escolha = MyIO.readChar();
                    escolha = Character.toUpperCase(escolha);

                    erroEscolha = escolha != 'A' && escolha != 'R' && escolha != 'V';

                    switch (escolha) {
                        case 'A':
                            atualizarEstadoConvite(idsConvites[conviteEscolhido - 1], 1);

                            // TODO: Criar um Novo Registro de Participação por meio do método create() do CRUD de Participação <- Não sei se é pra fazer

                            indiceInvertido.delete(usuario.chaveSecundaria(), idsConvites[conviteEscolhido - 1]);

                            MyIO.println("Convite aceito com sucesso!\n");

                            visualizarNovosConvites();

                            break;
                        case 'R':
                            atualizarEstadoConvite(idsConvites[conviteEscolhido - 1], 2);

                            indiceInvertido.delete(usuario.chaveSecundaria(), idsConvites[conviteEscolhido - 1]);

                            MyIO.println("Convite recusado com sucesso!\n");

                            visualizarNovosConvites();

                            break;
                        case 'V':
                            visualizarNovosConvites();

                            break;
                        default:
                            MyIO.println("Escolha inválida!\n");
                    }
                } while (erroEscolha);
            }
        } else {
            MyIO.println("Você não possui novos convites!\n");
        }
    }

    private void lerConvite(int idConvite, int numeroConvite) throws Exception {
        Convites convite = convites.read(idConvite);

        String[] auxiliar_1 = convite.chaveSecundaria().split("|");

        int idGrupo = Integer.parseInt(auxiliar_1[0]);
        long momentoConvite = convite.getMomento();

        Grupos grupo = grupos.read(idGrupo);

        String[] auxiliar_2 = grupo.chaveSecundaria().split("|");

        int idAdministrador = Integer.parseInt(auxiliar_2[0]);

        Usuario administrador = usuarios.read(idAdministrador);

        String nomeGrupo = grupo.getNome();
        String nomeAdministrador = administrador.getNome();

        escreverConvite(numeroConvite, nomeGrupo, momentoConvite, nomeAdministrador);
    }

    private void escreverConvite(int numeroConvite, String nomeGrupo, long momentoConvite, String nomeAdministrador) {
        MyIO.println(numeroConvite + ". " + nomeGrupo);
        // TODO: Manejar o formato da Data/Hora <- Preciso ver como está na classe Convites
        MyIO.println("   Convidado em " + momentoConvite);
        MyIO.println("   por " + nomeAdministrador);
    }

    private void atualizarEstadoConvite(int idConvite, int estadoConvite) throws Exception {
        Convites convite = convites.read(idConvite);

        convite.setEstado(estadoConvite);

        convites.update(convite);
    }
}