import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

class Sorteio {
    private static CRUD<Grupos> grupos;
    private static CRUD<Participacao> participacoes;
    private static ArvoreBMais_Int_Int indiceIndiretoGrupos;
    private static ArvoreBMais_Int_Int indiceIndiretoParticipacoes;

    public static void setBancosDados(CRUD<Grupos> gruposAmigos, CRUD<Participacao> participacoesAmigos) {
        grupos = gruposAmigos;
        participacoes = participacoesAmigos;
        indiceIndiretoGrupos = grupos.índiceIndiretoIntInt;
        indiceIndiretoParticipacoes = participacoes.índiceIndiretoIntInt;
    }

    public static void sortearGrupo(int idUsuarioAtual) throws Exception {
        int[] idsGrupos = indiceIndiretoGrupos.read(idUsuarioAtual);

        Grupos[] gruposAmigos = new Grupos[idsGrupos.length];

        for (int i = 0; i < gruposAmigos.length; i++) {
            gruposAmigos[i] = grupos.read(idsGrupos[i]);
        }

        HashMap<Integer, Integer> indiceTemporario = new HashMap<>();

        mostrarGruposValidos(gruposAmigos, indiceTemporario);

        Grupos grupoEscolhido = lerGrupo(indiceTemporario);

        if (grupoEscolhido != null) {
            int[] idsParticipacoes = indiceIndiretoParticipacoes.read(grupoEscolhido.getId());
            int[] idsParticipacoesEmbaralhado = embaralharArranjo(idsParticipacoes);

            for (int i = 0; i < idsParticipacoesEmbaralhado.length; i++) {
                Participacao participante;
                Participacao sorteado;

                if (i == idsParticipacoesEmbaralhado.length - 1) {
                    participante = participacoes.read(idsParticipacoesEmbaralhado[i]);
                    sorteado = participacoes.read(idsParticipacoesEmbaralhado[0]);
                } else {
                    participante = participacoes.read(idsParticipacoesEmbaralhado[i]);
                    sorteado = participacoes.read(idsParticipacoesEmbaralhado[i + 1]);
                }

                participante.setIdAmigo(sorteado.getId());

                participacoes.update(participante);
            }

            grupoEscolhido.setSorteado(true);

            grupos.update(grupoEscolhido);

            MyIO.println("Sorteio Concluído!\n");
        }
    }

    public static void mostrarGruposValidos(Grupos[] gruposAmigos, HashMap<Integer, Integer> indiceTemporario) throws Exception {
        Calendar dataAtual = Calendar.getInstance();

        int contador = 1;

        for (int i = 0; i < gruposAmigos.length; i++) {
            Calendar dataSorteio = Calendar.getInstance();

            dataSorteio.setTimeInMillis(gruposAmigos[i].getMomentoSorteio());

            if (gruposAmigos[i].getAtivo() && !gruposAmigos[i].getSorteado() && dataSorteio.get(Calendar.YEAR) <= dataAtual.get(Calendar.YEAR)) {
                if (dataSorteio.get(Calendar.MONTH) <= dataAtual.get(Calendar.MONTH) + 1) {
                    if (dataSorteio.get(Calendar.DATE) <= dataAtual.get(Calendar.DATE)) {
                        if (dataSorteio.get(Calendar.HOUR) <= dataAtual.get(Calendar.HOUR)) {
                            MyIO.println(i + 1 + ". Nome do Grupo: " + gruposAmigos[i].getNome());
                            MyIO.println("   Data do Sorteio: " + dataSorteio.get(Calendar.DATE) + "/" + dataSorteio.get(Calendar.MONTH) + "/" + dataSorteio.get(Calendar.YEAR) + " às " + dataSorteio.get(Calendar.HOUR) + " horas");

                            indiceTemporario.put(contador, gruposAmigos[i].getId());

                            contador++;

                            MyIO.println("");
                        }
                    }
                }
            }
        }
    }

    public static Grupos lerGrupo(HashMap<Integer, Integer> indiceTemporario) throws Exception {
        MyIO.print("Grupo: ");

        int posicaoGrupoEscolhido = MyIO.readInt();

        MyIO.println("");

        Grupos grupoEscolhido = null;

        if (posicaoGrupoEscolhido != 0) {
            int idGrupo = indiceTemporario.get(posicaoGrupoEscolhido);

            grupoEscolhido = grupos.read(idGrupo);

            Calendar dataSorteio = Calendar.getInstance();

            dataSorteio.setTimeInMillis(grupoEscolhido.getMomentoSorteio());

            MyIO.println(posicaoGrupoEscolhido + ". Nome do Grupo: " + grupoEscolhido.getNome());
            MyIO.println("   Data do Sorteio: " + dataSorteio.get(Calendar.DATE) + "/" + dataSorteio.get(Calendar.MONTH) + "/" + dataSorteio.get(Calendar.YEAR) + " às " + dataSorteio.get(Calendar.HOUR) + " horas");

            MyIO.println("");
        }

        return grupoEscolhido;
    }

    public static int[] embaralharArranjo(int[] arranjo) {
        Random gerador = new Random();

        for (int i = 0; i < arranjo.length; i++) {
            int posicaoAleatoria = gerador.nextInt(arranjo.length);
            int auxiliar = arranjo[i];

            arranjo[i] = arranjo[posicaoAleatoria];
            arranjo[posicaoAleatoria] = auxiliar;
        }

        return arranjo;
    }
}
