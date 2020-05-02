import java.util.Calendar;

class Inscricao {
    private static ArvoreBMais_ChaveComposta_String_Int indiceIndireto;
    private static CRUD<Convites> convites;
    private static CRUD<Grupos> grupos;
    private static CRUD<Usuario> usuarios;

    public static void setBancos(CRUD<Usuario> amigosU, CRUD<Grupos> amigosG, CRUD<Convites> amigosC) {
        convites = amigosC;
        grupos = amigosG;
        usuarios = amigosU;
        indiceIndireto = amigosC.indiceInvertido;
    }

    public static void visualizarNovosConvites(Usuario usuario) throws Exception {
        int[] idsConvites = indiceIndireto.read(usuario.chaveSecundaria());

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

                            // TODO: Criar um Novo Registro de Participação por meio do método create() do CRUD de Participação

                            convites.indiceInvertido.delete(usuario.chaveSecundaria(), idsConvites[conviteEscolhido - 1]);

                            MyIO.println("Convite aceito com sucesso!\n");

                            visualizarNovosConvites(usuario);

                            break;
                        case 'R':
                            atualizarEstadoConvite(idsConvites[conviteEscolhido - 1], 2);

                            convites.indiceInvertido.delete(usuario.chaveSecundaria(), idsConvites[conviteEscolhido - 1]);

                            MyIO.println("Convite recusado com sucesso!\n");

                            visualizarNovosConvites(usuario);

                            break;
                        case 'V':
                            visualizarNovosConvites(usuario);

                            break;
                        default:
                            MyIO.println("Escolha inválida!\n");
                    }
                } while (erroEscolha);
            }
        } else {
            MyIO.println("Você não possui novos convites!\n");
            AmigoOculto.pressioneTeclaParaContinuar();
        }
    }

    public static int getTotalConvites(Usuario usuario) throws Exception {
        int total;

        try {
            total = indiceIndireto.read(usuario.chaveSecundaria()).length;
        } catch (NullPointerException e){
            total = 0;
        }

        return total;
    }

    private static void lerConvite(int idConvite, int numeroConvite) throws Exception {
        Convites convite = convites.read(idConvite);

        long momentoConviteMS = convite.getMomentoConvite();

        String[] auxiliar_1 = convite.chaveSecundaria().split("\\|");

        int idGrupo = Integer.parseInt(auxiliar_1[0]);

        Grupos grupo = grupos.read(idGrupo);

        String[] auxiliar_2 = grupo.chaveSecundaria().split("\\|");

        String nomeGrupo = auxiliar_2[1];

        int idAdministrador = Integer.parseInt(auxiliar_2[0]);

        Usuario administrador = usuarios.read(idAdministrador);

        String nomeAdministrador = administrador.getNome();

        escreverConvite(numeroConvite, nomeGrupo, momentoConviteMS, nomeAdministrador);
    }

    private static void escreverConvite(int numeroConvite, String nomeGrupo, long momentoConviteMS, String nomeAdministrador) {
        Calendar momentoConvite = Calendar.getInstance();

        momentoConvite.setTimeInMillis(momentoConviteMS);

        MyIO.println(numeroConvite + ". " + nomeGrupo);
        MyIO.println("   Convidado em " + momentoConvite.get(Calendar.DATE) + "/" + momentoConvite.get(Calendar.MONTH) + "/" + momentoConvite.get(Calendar.YEAR) + " às " + momentoConvite.get(Calendar.HOUR) + " horas");
        MyIO.println("   por " + nomeAdministrador);
    }

    private static void atualizarEstadoConvite(int idConvite, int estadoConvite) throws Exception {
        Convites convite = convites.read(idConvite);

        convite.setEstado((byte) estadoConvite);

        convites.update(convite);
    }
}
