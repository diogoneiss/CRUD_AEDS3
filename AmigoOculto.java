import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.InputMismatchException;

@SuppressWarnings("SpellCheckingInspection")
public class AmigoOculto {

    //Singleton controlador
    public static final ControladorSingleton controladorPrograma = ControladorSingleton.getInstance();

    public static void main(String[] args) {

        MyIO.setCharset("UTF-8");

        try {

            //bancos de dados
            CRUD<Usuario> usuariosAmigoOculto = new CRUD<>("amigos", Usuario.class.getConstructor());
            CRUD<Sugestao> sugestaoAmigoOculto = new CRUD<>("sugestoes", Sugestao.class.getConstructor());
            CRUD<Grupos> gruposAmigoOculto = new CRUD<>("grupos", Grupos.class.getConstructor());
            CRUD<Convites> conviteAmigoOculto = new CRUD<>("convite", Convites.class.getConstructor());
            CRUD<Mensagem> mensagemAmigoOculto = new CRUD<>("mensgem", Mensagem.class.getConstructor());
            CRUD<Participacao> participacaoAmigoOculto = new CRUD<>("participacao", Participacao.class.getConstructor());

            //setar os bancos na classe estática das inscricoes
            Inscricao.setBancos(usuariosAmigoOculto, gruposAmigoOculto, conviteAmigoOculto, participacaoAmigoOculto);
            Sorteio.setBancosDados(gruposAmigoOculto, participacaoAmigoOculto);

            int opcaoEscolhidaMenuLogin;

            //Menu principal, em que os usuarios criam ou logam.
            do {

                opcaoEscolhidaMenuLogin = menuLogin(usuariosAmigoOculto);

                // VERIFICAÇÃO SE O USUÁRIO LOGOU
                // Caso tenha logado, procedemos ao menu inicial de fato
                if (controladorPrograma.getIdUsuarioAtual() != -1) {
                    int opcaoMenuInicial;
                    do {
                        opcaoMenuInicial = menuInicial(usuariosAmigoOculto.read(controladorPrograma.getIdUsuarioAtual()));
                        //sugestões
                        //grupos
                        // convites
                        //sair
                        switch (opcaoMenuInicial) {
                            case 1 -> {
                                int opcaoEscolhidaSugestoes;
                                do {

                                    opcaoEscolhidaSugestoes = menuLogadoSugestao(sugestaoAmigoOculto);

                                    //saída do menu logado
                                } while (opcaoEscolhidaSugestoes != 0);
                            }
                            case 2 -> {
                                int opcaoEscolhidaGrupos;
                                do {
                                    opcaoEscolhidaGrupos = menuLogadoGrupos(gruposAmigoOculto, conviteAmigoOculto, usuariosAmigoOculto, participacaoAmigoOculto, mensagemAmigoOculto, sugestaoAmigoOculto);
                                } while (opcaoEscolhidaGrupos != 0);
                                controladorPrograma.finalizarIdGrupoAtual();
                            }
                            case 3 -> Inscricao.visualizarNovosConvites(usuariosAmigoOculto.read(controladorPrograma.getIdUsuarioAtual()));
                            case 0 -> {
                            }
                            default -> System.out.println("Opção inválida inserida, retornando..");
                        }


                    } while (opcaoMenuInicial != 0);
                    //como saímos do menu logado não é necessário mais armazenar o id do usuario atual
                    controladorPrograma.finalizarIdUsuarioAtual();
                }

                //despedir se o usuario quiser sair do programa.
                if (opcaoEscolhidaMenuLogin == 0)
                    System.out.println("Saindo..");


                //fim do do-while principal, ou seja, fim do programa.
            } while (opcaoEscolhidaMenuLogin != 0);


        } catch (InputMismatchException erroInput) {
            System.out.println("Você digitou um caractere inválido e o programa crashou por isso.");
            erroInput.printStackTrace();
        } catch (Exception noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        }
    }

    public static int menuInicial(Usuario userLogado) {
        return controladorPrograma.escolherOpcaoMenuInicial(userLogado);
    }

    // ============== MÉTODOS DE PARTICIPAÇÃO =================================================================================
    public static void visualizarParticipantes(CRUD<Participacao> amigoOculto, CRUD<Usuario> usuariosAmigoOculto) {
        int idGrupo = controladorPrograma.getIdGrupoAtual();
        try {
            System.out.println("PARTICIPANTES");

            //metodo retorna um int[] com todas os ids batendo a c1
            int[] idsParticipantes = amigoOculto.índiceIndiretoIntInt.read(idGrupo);
            int contador = 0;

            for (int i = 1; i <= idsParticipantes.length; i++, contador++) {
                Usuario temp = usuariosAmigoOculto.read(i);

                if (temp != null) {
                    System.out.println(i + ". " + temp.getNome() + " email: " + temp.getEmail());

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pressioneTeclaParaContinuar();

    }

    public static void visualizarSorteado(CRUD<Participacao> amigoOculto, CRUD<Usuario> usuariosAmigoOculto, CRUD<Sugestao> sugestaoAmigoOculto) {
        int idGrupo = controladorPrograma.getIdGrupoAtual();
        int idUser = controladorPrograma.getIdUsuarioAtual();
        try {
            if(amigoOculto.read(idUser)!=null && usuariosAmigoOculto.read(amigoOculto.read(idUser).getIdAmigo())!=null){
                Participacao temp = amigoOculto.read(idUser);
                int idAmigo = temp.getIdAmigo();
                Usuario tmp = usuariosAmigoOculto.read(idAmigo);
                System.out.println(tmp.getNome());

                System.out.println("SUGESTÕES");
                //metodo retorna um int[] com todas os ids batendo a c1
                int[] idsSugestoes = sugestaoAmigoOculto.índiceIndiretoIntInt.read(idAmigo);
                int contador = 0;

                //loop que vai procurar os n elementos retornados dentro do banco e printar os valores
                for (int i = 1; i <= idsSugestoes.length; i++, contador++) {
                    Sugestao temps = sugestaoAmigoOculto.read(i);

                    if (temps != null) {
                        //chamada do metodo toString da sugestão
                        System.out.println(i + ". " + temps);
                    }
                }
            }else{
                System.out.println("SORTEIO AINDA NÃO REALIZADO");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pressioneTeclaParaContinuar();
    }

    public static void menuLogadoParticipacao(CRUD<Participacao> amigoOculto, CRUD<Usuario> usuarioAmigoOculto, CRUD<Mensagem> mensagemAmigoOculto, CRUD<Grupos> gruposAmigoOculto, CRUD<Sugestao> sugestaoAmigoOculto) throws Exception {
        int opcao;
        try{
            System.out.println("ESCOLHA UM DOS GRUPOS QUE VOCÊ PARTICIPA: ");
            int[] idsGrupos = gruposAmigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
            for (int i = 0; i < idsGrupos.length; i++) {
                Grupos temp = gruposAmigoOculto.read(idsGrupos[i]);
                if (temp.getAtivo()) {
                    MyIO.println((i + 1) + ". " + temp.getNome());
                }
            }
            int tempID;
            do{
                MyIO.print("Grupo Numero: ");
                tempID = MyIO.readInt();
                if((tempID>0 && tempID<=idsGrupos.length))
                    controladorPrograma.salvarIdGrupoAtual(idsGrupos[tempID-1]);
                else
                    System.out.println("Opa! Grupo inválido. Repetindo. Você digitou o grupo "+tempID);

            }while(tempID<0 || tempID>idsGrupos.length);
            //System.out.println("O id do grupo que você escolheu anteriormente é "+(controladorPrograma.getIdGrupoAtual()));

            opcao = controladorPrograma.Participacao(gruposAmigoOculto);
            //Visualizar participantes do grupo
            //Visualizar amigo sorteado
            //Visualizar amigo sorteado
            //saída
            switch (opcao) {
                case 1 -> visualizarParticipantes(amigoOculto, usuarioAmigoOculto);
                case 2 -> visualizarSorteado(amigoOculto, usuarioAmigoOculto, sugestaoAmigoOculto);
                case 3 -> menuMensgens(mensagemAmigoOculto, usuarioAmigoOculto);
                case 0 -> System.out.println("Retornando..");
                default -> System.out.println("Opção inválida inserida, retornando..");
            }
        }catch(InputMismatchException erroInput){
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
        }
    }


    public static void informacoesGrupo(CRUD<Grupos> gruposAmigoOculto) {
        int idGrupo = controladorPrograma.getIdGrupoAtual();
        //MyIO.println("bbb"+idGrupo);
        Calendar hoje = Calendar.getInstance();
        try {
            Grupos temp = gruposAmigoOculto.read(idGrupo);
            Calendar sorteio = Calendar.getInstance();
            sorteio.setTimeInMillis(temp.getMomentoSorteio());
            Calendar encontro = Calendar.getInstance();
            encontro.setTimeInMillis(temp.getMomentoSorteio());
            if (temp != null) {
                System.out.println(temp.getNome());
                if (hoje.getTimeInMillis() >= sorteio.getTimeInMillis())
                    System.out.println("O sorteio ocorreu dia " + sorteio.get(Calendar.DATE) + "/" + sorteio.get(Calendar.MONTH) + "/" + sorteio.get(Calendar.YEAR) + ", às " + sorteio.get(Calendar.HOUR) + ".");
                else
                    System.out.println("O sorteio ocorrerá dia " + sorteio.get(Calendar.DATE) + "/" + sorteio.get(Calendar.MONTH) + "/" + sorteio.get(Calendar.YEAR) + ", às " + sorteio.get(Calendar.HOUR) + ".");

                System.out.println("Os presentes devem ter valor aproximado de R$" + temp.getValor());

                if (hoje.getTimeInMillis() >= encontro.getTimeInMillis())
                    System.out.println("O encontro ocorreu dia " + encontro.get(Calendar.DATE) + "/" + encontro.get(Calendar.MONTH) + "/" + encontro.get(Calendar.YEAR) + ", às " + encontro.get(Calendar.HOUR) + ".");
                else
                    System.out.println("O encontro ocorrerá dia " + encontro.get(Calendar.DATE) + "/" + encontro.get(Calendar.MONTH) + "/" + encontro.get(Calendar.YEAR) + ", às " + encontro.get(Calendar.HOUR) + ", em " + temp.getLocalEncontro());
                if (!temp.getObservacoes().equals("") && !temp.getObservacoes().equals(" "))
                    System.out.println("\nObservações:\n"+temp.getObservacoes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pressioneTeclaParaContinuar();
    }

    // ============== MÉTODOS DE MENSAGENS =================================================================================
    public static void menuMensgens(CRUD<Mensagem> mensagemAmigoOculto, CRUD<Usuario> usuarioAmigoOculto) throws Exception {
        int escolha;
        do {
            escolha = controladorPrograma.lerEnviarMensagem();
            if (escolha == 1) {
                int[] idsMensagens = mensagemAmigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdGrupoAtual());
                int i = idsMensagens.length - 1;
                int op = 1;
                if(idsMensagens.length==0) {
                    MyIO.println("Nenhuma Mensagem foi enviada nesse grupo");
                    pressioneTeclaParaContinuar();
                }
                else{
                    while (i >= 0 && op == 1) {
                        int cont = 0;
                        while (cont < 5 && i >= 0) {
                            Mensagem temp = mensagemAmigoOculto.read(idsMensagens[i--]);
                            Usuario usuario = usuarioAmigoOculto.read(temp.getIdCriador());
                            MyIO.println(usuario.getNome() + " enviou:");
                            MyIO.println(temp.getMensagem() + "\n");
                            cont++;
                        }
                        if (i >= 0) {
                            do {
                                MyIO.println("Digite 1 para ir para a proxima pagina de mensagens ou 0 para voltar ao menu anterior: ");
                                op = MyIO.readInt();
                            } while (op != 0 && op != 1);
                        }
                        else{
                            pressioneTeclaParaContinuar();
                        }
                    }
                }
            }
            if (escolha == 2) {
                MyIO.println("Digite a mensagem que deseja enviar: ");
                String mensagem = MyIO.readLine();
                Mensagem temp = new Mensagem(-1, controladorPrograma.getIdGrupoAtual(), controladorPrograma.getIdUsuarioAtual(), mensagem);
                int id = mensagemAmigoOculto.create(temp);
                mensagemAmigoOculto.índiceIndiretoIntInt.create(controladorPrograma.getIdGrupoAtual(), id);
            }
        } while (escolha != 0);
    }
// ============== MÉTODOS DE CONVITE =================================================================================

    public static void cancelamentoConvite(CRUD<Convites> amigoOculto, CRUD<Grupos> gruposAmigoOculto) throws Exception {
        MyIO.println("ESCOLHA O GRUPO: ");
        int[] idsGrupos = gruposAmigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        int i;
        for (i = 1; i <= idsGrupos.length; i++) {
            Grupos temp = gruposAmigoOculto.read(idsGrupos[i - 1]);
            Calendar atual = Calendar.getInstance();
            Calendar data = Calendar.getInstance();
            data.setTimeInMillis(temp.getMomentoSorteio());
            if (temp.getAtivo() && temp.getMomentoSorteio() > atual.getTimeInMillis()) {
                MyIO.println(i + ". " + temp.getNome());
                MyIO.println("Local: " + temp.getLocalEncontro());
                MyIO.println("Momento do sorteio:" + data.get(Calendar.DATE) + "/" + data.get(Calendar.MONTH) + "/" + data.get(Calendar.YEAR) + " " + data.get(Calendar.HOUR_OF_DAY));
            }
        }
        MyIO.print("Grupo: ");
        int opcao;
        try {
            opcao = MyIO.readInt();
            if (opcao < 0 || opcao > i) {
                MyIO.println("Opção inválida");
                opcao = 0;
            }
            if (opcao == 0) {
                System.out.println("Retornando..");
                menuLogadoConvite(amigoOculto, gruposAmigoOculto);
                return;
            } else {
                Grupos grupo = gruposAmigoOculto.read(idsGrupos[opcao - 1]);
                MyIO.println("Grupo: " + grupo.getNome());
                int[] idsConvites = amigoOculto.índiceIndiretoIntInt.read(idsGrupos[opcao - 1]);
                int j;
                for (j = 1; j <= idsConvites.length; j++) {
                    Convites temp = amigoOculto.read(idsConvites[j - 1]);
                    if (temp.getMomentoConvite() == 0) {
                        MyIO.println(i + ". " + temp.getEmail());
                    }
                }
                MyIO.print("Convite: ");
                int opcaocv;
                try {
                    opcaocv = MyIO.readInt();
                    if (opcaocv < 0 || opcaocv > j) {
                        MyIO.println("Opção inválida");
                        opcaocv = 0;
                    }
                    if (opcaocv == 0) {
                        System.out.println("Retornando..");
                        menuLogadoConvite(amigoOculto, gruposAmigoOculto);
                        return;
                    } else if (opcaocv > 0 && opcaocv <= j) {
                        Convites temp = amigoOculto.read(idsConvites[opcaocv - 1]);
                        MyIO.println("Deseja realmente cancelar o convite para " + temp.getEmail() + "? 1 para sim, 0 para nao");
                        if (MyIO.readInt() != 1) {
                            MyIO.println("Retornando ao menu");
                            menuLogadoConvite(amigoOculto, gruposAmigoOculto);
                            return;
                        } else {
                            byte byte3 = 3;
                            Convites novo = new Convites(temp.getId(), temp.getIdGrupo(), controladorPrograma.getIdGrupoAtual(), temp.getEmail(), temp.getMomentoConvite(), byte3);
                            amigoOculto.update(novo);
                            amigoOculto.índiceIndireto.delete(temp.chaveSecundaria());
                            MyIO.println("Convite cancelado com sucesso");
                            MyIO.println("Retornando ao menu");
                            menuLogadoConvite(amigoOculto, gruposAmigoOculto);
                            return;
                        }
                    }
                } catch (InputMismatchException erroInput) {
                    System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
                    opcaocv = 0;
                }
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
        }

        pressioneTeclaParaContinuar();
    }

    public static void emissaoConvite(CRUD<Convites> amigoOculto, CRUD<Grupos> gruposAmigoOculto) throws Exception {
        MyIO.println("ESCOLHA O GRUPO: ");
        int[] idsGrupos = gruposAmigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        int i;
        for (i = 1; i <= idsGrupos.length; i++) {
            Grupos temp = gruposAmigoOculto.read(idsGrupos[i - 1]);
            Calendar atual = Calendar.getInstance();
            Calendar data = Calendar.getInstance();
            data.setTimeInMillis(temp.getMomentoSorteio());
            if (temp.getAtivo() && temp.getMomentoSorteio() > atual.getTimeInMillis()) {
                MyIO.println(i + ". " + temp.getNome());
                MyIO.println("Local: " + temp.getLocalEncontro());
                MyIO.println("Momento do sorteio:" + data.get(Calendar.DATE) + "/" + data.get(Calendar.MONTH) + "/" + data.get(Calendar.YEAR) + " " + data.get(Calendar.HOUR_OF_DAY));
            }
        }
        MyIO.print("Grupo: ");
        int opcao;
        try {
            opcao = MyIO.readInt();
            if (opcao < 0 || opcao > i) {
                MyIO.println("Opção inválida");
                opcao = 0;
            }
            if (opcao == 0) {
                System.out.println("Retornando..");
                menuLogadoConvite(amigoOculto, gruposAmigoOculto);
                return;
            } else {
                Grupos grupo = gruposAmigoOculto.read(idsGrupos[opcao - 1]);
                MyIO.println("Grupo: " + grupo.getNome());

                int contador = 1;
                while (contador == 1) {
                    MyIO.println("Digite o email do convidado: ");
                    String email = MyIO.readLine();
                    int idconvite = amigoOculto.índiceIndireto.read(idsGrupos[opcao - 1] + "|" + email);
                    if (email.equals("")) {
                        MyIO.println("Email em branco, retornando ao menu de convites");
                        menuLogadoConvite(amigoOculto, gruposAmigoOculto);
                        return;
                    } else if (idconvite != -1) {
                        if (amigoOculto.read(idconvite).getEstado() == 0 || amigoOculto.read(idconvite).getEstado() == 1) {
                            MyIO.println("Convite já emitido para esse email");
                        } else {
                            MyIO.println("Convite já emitido para esse email e recusado ou cancelado, deseja reenviar? 1 para sim, 0 para não");
                            if (MyIO.readInt() == 1) {
                                Calendar momento = Calendar.getInstance();
                                int id = -1;
                                byte byte0 = 0;
                                Convites temp = new Convites(id, idsGrupos[opcao - 1], controladorPrograma.getIdGrupoAtual(), email, momento.getTimeInMillis(), byte0);
                                try {
                                    id = amigoOculto.create(temp);
                                } catch (Exception e) {
                                    MyIO.println("Erro");
                                }
                                amigoOculto.índiceIndireto.create(temp.chaveSecundaria(), temp.getId());
                                contador = 0;
                            }
                        }
                    } else {
                        Calendar momento = Calendar.getInstance();
                        int id = -1;
                        byte byte0 = 0;
                        Convites temp = new Convites(id, idsGrupos[opcao - 1], controladorPrograma.getIdGrupoAtual(), email, momento.getTimeInMillis(), byte0);
                        try {
                            id = amigoOculto.create(temp);
                        } catch (Exception e) {
                            MyIO.println("Erro");
                        }
                        amigoOculto.índiceIndireto.create(temp.chaveSecundaria(), temp.getId());
                        amigoOculto.índiceIndiretoIntInt.create(controladorPrograma.getIdUsuarioAtual(), id);

                        String[] auxiliar = temp.chaveSecundaria().split("\\|");

                        amigoOculto.indiceInvertido.create(auxiliar[1], temp.getId());
                        contador = 0;
                    }
                }
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcao = 0;
        }
        pressioneTeclaParaContinuar();
    }

    public static void listagemConvite(CRUD<Convites> amigoOculto, CRUD<Grupos> gruposAmigoOculto) throws Exception {
        MyIO.println("ESCOLHA O GRUPO: ");
        int[] idsGrupos = gruposAmigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        for (int i = 0; i < idsGrupos.length; i++) {
            Grupos temp = gruposAmigoOculto.read(idsGrupos[i]);
            if (temp.getAtivo()) {
                MyIO.println(i + 1 + ". " + temp.getNome());
            }
        }
        MyIO.print("Grupo: ");
        int opcao;
        try {
            opcao = MyIO.readInt();
            if (opcao <= 0 || opcao > idsGrupos.length) {
                MyIO.println("Opção inválida");
            } else {
                int[] chavesConvites = amigoOculto.índiceIndiretoIntInt.read(idsGrupos[idsGrupos.length - 1]);
                for (int i = 0; i < chavesConvites.length; i++) {
                    Convites temp = amigoOculto.read(chavesConvites[i]);
                    String tempestado = "";
                    if (temp.getEstado() == 0) tempestado = "pendente";
                    else if (temp.getEstado() == 1) tempestado = "aceito";
                    else if (temp.getEstado() == 2) tempestado = "recusado";
                    else if (temp.getEstado() == 3) tempestado = "cancelado";
                    Calendar momento = Calendar.getInstance();
                    momento.setTimeInMillis(temp.getMomentoConvite());
                    MyIO.println(i + 1 + ". " + temp.getEmail() + " (" + momento.get(Calendar.DATE) + "/" + momento.get(Calendar.MONTH) + "/" + momento.get(Calendar.YEAR) + " " + momento.get(Calendar.HOUR_OF_DAY) + " - " + tempestado + ")");
                }
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
        }
        pressioneTeclaParaContinuar();
    }

    public static void menuLogadoConvite(CRUD<Convites> amigoOculto, CRUD<Grupos> gruposAmigoOculto) throws Exception {
        int opcao;
        try {
            opcao = controladorPrograma.convites();
            //Listagem de convites
            //Emissão de convies
            //Cancelamento de convies
            //saída
            switch (opcao) {
                case 1 -> listagemConvite(amigoOculto, gruposAmigoOculto);
                case 2 -> emissaoConvite(amigoOculto, gruposAmigoOculto);
                case 3 -> cancelamentoConvite(amigoOculto, gruposAmigoOculto);
                case 0 -> System.out.println("Retornando..");
                default -> System.out.println("Opção inválida inserida, retornando..");
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
        }
    }

    // ============== MÉTODOS DE GRUPO =================================================================================


    public static void criarGrupo(CRUD<Grupos> amigoOculto) throws Exception {
        MyIO.println("Digite o nome do grupo: ");
        String nome = MyIO.readLine();
        if (nome.compareTo("") == 0 || nome.compareTo(" ") == 0) {
            MyIO.println("Nome invalido, retornando ao menu de gerenciamento de grupos");
        } else {
            boolean valido = false;
            long dataSorteio = 0;
            while (!valido) {
                int dia, mes, ano, hora;
                dia = mes = ano = hora = -1;
                Calendar atual = Calendar.getInstance();
                Calendar novo = Calendar.getInstance();
                do {
                    MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                    dia = mes = ano = hora = -1;
                    String temp = MyIO.readLine();
                    String[] aux = temp.split(" ");
                    if (vereficaData(aux[0])) {
                        ano = Integer.parseInt(aux[0]);
                        if (vereficaData(aux[1])) {
                            mes = Integer.parseInt(aux[1]);
                            if (vereficaData(aux[2])) {
                                dia = Integer.parseInt(aux[2]);
                                if (vereficaData(aux[3])) hora = Integer.parseInt(aux[3]);
                            }
                        }
                    }
                    if ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23)) {
                        MyIO.println("Data invalida!!! Atente-se ao formato");
                    }
                } while ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23));
                novo.set(ano, mes, dia, hora, 0);
                if (novo.compareTo(atual) > 0) {
                    valido = true;
                    dataSorteio = novo.getTimeInMillis();
                } else {
                    MyIO.println("Data antes da atual, invalida!");
                }
            }
            float valor;
            do {
                MyIO.println("Informe o valor médio do presente: ");
                valor = MyIO.readFloat();
            } while (valor < 0);
            long dataEncontro = 0;
            valido = false;
            while (!valido) {
                int dia, mes, ano, hora;
                dia = mes = ano = hora = -1;
                Calendar sorteio = Calendar.getInstance();
                sorteio.setTimeInMillis(dataSorteio);
                Calendar novo = Calendar.getInstance();
                do {
                    dia = mes = ano = hora = -1;
                    MyIO.println("Digite o ano, o mes, o dia e a hora do encontro respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                    String temp = MyIO.readLine();
                    String[] aux = temp.split(" ");
                    if (vereficaData(aux[0])) {
                        ano = Integer.parseInt(aux[0]);
                        if (vereficaData(aux[1])) {
                            mes = Integer.parseInt(aux[1]);
                            if (vereficaData(aux[2])) {
                                dia = Integer.parseInt(aux[2]);
                                if (vereficaData(aux[3])) hora = Integer.parseInt(aux[3]);
                            }
                        }
                    }
                    if ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23)) {
                        MyIO.println("Data invalida!!!");
                    }
                } while ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23));
                novo.set(ano, mes, dia, hora, 0);
                if (novo.compareTo(sorteio) > 0) {
                    valido = true;
                    dataEncontro = novo.getTimeInMillis();
                } else {
                    MyIO.println("Data antes do sorteio, invalida! O encontro tem que ser feito depois do sorteio..");
                }
            }
            String local;
            do {
                MyIO.println("Digite o local do encontro: ");
                local = MyIO.readLine();
            } while (local.equals(""));
            String observacoes;
            MyIO.println("Digite observações adicionais: ");
            observacoes = MyIO.readLine();
            MyIO.println("Digite 1 se deseja confirmar a inscrição: ");
            int confirmar = MyIO.readInt();
            if (confirmar == 1) {
                int id = -1;
                Grupos temp = new Grupos(-1, controladorPrograma.getIdUsuarioAtual(), nome, local, observacoes, dataSorteio, dataEncontro, valor, false, true);
                try {
                    id = amigoOculto.create(temp);

                } catch (Exception e) {
                    MyIO.println("Erro");
                }
                amigoOculto.índiceIndiretoIntInt.create(controladorPrograma.getIdUsuarioAtual(), id);
            }
        }
        pressioneTeclaParaContinuar();
    }

    public static void listarGrupos(CRUD<Grupos> amigoOculto) throws Exception {
        int[] idsGrupos = amigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        int nGruposValidos = 0;
        for (int idsGrupo : idsGrupos) {
            Grupos temp = amigoOculto.read(idsGrupo);
            if (temp.getAtivo()) {
                MyIO.println(nGruposValidos + ". " + temp.getNome());
                nGruposValidos++;
            }
        }
        pressioneTeclaParaContinuar();
    }

    public static void alterarGrupos(CRUD<Grupos> amigoOculto) throws Exception {
        int[] idsGrupos = amigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        int nGruposValidos = 0;
        for (int idsGrupo : idsGrupos) {
            Grupos temp = amigoOculto.read(idsGrupo);
            if (temp.getAtivo()) {
                MyIO.print(nGruposValidos + ". ");
                temp.mostrar();
                nGruposValidos++;
            }
        }
        int alteracoes = 0;
        MyIO.println("Digite o numero do grupo que deseja alterar ou -1 para sair: ");
        int alterar = MyIO.readInt();
        if (alterar >= 0) {
            int alterarReal = 0;
            int cont = 0;
            while (cont != alterar) {
                Grupos temp = amigoOculto.read(idsGrupos[alterarReal++]);
                if (temp.getAtivo()) cont++;
            }
            if (alterar < idsGrupos.length) {
                Grupos temp = amigoOculto.read(idsGrupos[alterarReal]);
                temp.mostrar();
                MyIO.println("Digite o novo nome: ");
                String nome = MyIO.readLine();
                if (nome.compareTo("") == 0) {
                    nome = temp.getNome();
                } else {
                    alteracoes++;
                }
                boolean valido = false;
                long dataSorteio = 0;
                while (!valido) {
                    int dia, mes, ano, hora;
                    dia = mes = ano = hora = -1;
                    Calendar atual = Calendar.getInstance();
                    Calendar novo = Calendar.getInstance();
                    MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                    String data = MyIO.readLine();
                    if (data.compareTo("") == 0) {
                        valido = true;
                        dataSorteio = temp.getMomentoSorteio();
                    } else {
                        do {
                            dia = mes = ano = hora = -1;
                            alteracoes++;
                            String[] aux = data.split(" ");
                            if (vereficaData(aux[0])) {
                                ano = Integer.parseInt(aux[0]);
                                if (vereficaData(aux[1])) {
                                    mes = Integer.parseInt(aux[1]);
                                    if (vereficaData(aux[2])) {
                                        dia = Integer.parseInt(aux[2]);
                                        if (vereficaData(aux[3])) hora = Integer.parseInt(aux[3]);
                                    }
                                }
                            }
                            if ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23)) {
                                MyIO.println("Data invalida!!!");
                                MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                                data = MyIO.readLine();
                            }
                        } while ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23));
                        novo.set(ano, mes, dia, hora, 0);
                        if (novo.compareTo(atual) > 0) {
                            valido = true;
                            dataSorteio = novo.getTimeInMillis();
                        } else {
                            MyIO.println("Data antes da atual, invalida!");
                        }
                    }
                }
                MyIO.println("Digite o valor medio dos presentes: ");
                String s = MyIO.readString();
                float valor = -1;
                do {
                    if (s.compareTo("") == 0) {
                        valor = temp.getValor();
                    } else if (vereficaData(s)) {
                        alteracoes++;
                        valor = Float.parseFloat(s);
                    }
                } while (s.compareTo("") != 0 && valor < 0);
                valido = false;
                long dataEncontro = 0;
                while (!valido) {
                    int dia, mes, ano, hora;
                    dia = mes = ano = hora = -1;
                    Calendar atual = Calendar.getInstance();
                    Calendar novo = Calendar.getInstance();
                    MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                    String data = MyIO.readLine();
                    if (data.compareTo("") == 0) {
                        valido = true;
                        dataEncontro = temp.getMomentoSorteio();
                    } else {
                        do {
                            dia = mes = ano = hora = -1;
                            alteracoes++;
                            String[] aux = data.split(" ");
                            if (vereficaData(aux[0])) {
                                ano = Integer.parseInt(aux[0]);
                                if (vereficaData(aux[1])) {
                                    mes = Integer.parseInt(aux[1]);
                                    if (vereficaData(aux[2])) {
                                        dia = Integer.parseInt(aux[2]);
                                        if (vereficaData(aux[3])) hora = Integer.parseInt(aux[3]);
                                    }
                                }
                            }
                            if ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23)) {
                                MyIO.println("Data invalida!!!");
                                MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                                data = MyIO.readLine();
                            }
                        } while ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23));
                        novo.set(ano, mes, dia, hora, 0);
                        if (novo.compareTo(atual) > 0) {
                            valido = true;
                            dataEncontro = novo.getTimeInMillis();
                        } else {
                            MyIO.println("Data antes da atual, invalida! O encontro deve ser feito após o sorteio..");
                        }
                    }
                }
                MyIO.println("Digite o local de encontro: ");
                String local = MyIO.readLine();
                if (local.compareTo("") == 0) {
                    local = temp.getLocalEncontro();
                } else {
                    alteracoes++;
                }
                MyIO.println("Digite observações adcionais");
                String observacoes = MyIO.readLine();
                if (observacoes.compareTo("") == 0) {
                    observacoes = temp.getObservacoes();
                } else {
                    alteracoes++;
                }
                if (alteracoes == 0) {
                    MyIO.println("Nenhuma alteração foi feita");
                } else {
                    MyIO.println("Digite 1 se realmente deseja alterar o grupo: ");
                    int escolha = MyIO.readInt();
                    if (escolha == 1) {
                        Grupos novo = new Grupos(temp.getId(), controladorPrograma.getIdUsuarioAtual(), nome, local, observacoes, dataSorteio, dataEncontro, valor, false, true);
                        amigoOculto.update(novo);
                        MyIO.println("Grupo alterado com sucesso!");
                    } else {
                        MyIO.println("Nenhuma alteração foi feita");
                    }
                }
            } else {
                MyIO.println("Número invalido");
            }
        }
        pressioneTeclaParaContinuar();
    }

    public static void desativarGrupo(CRUD<Grupos> amigoOculto) throws Exception {
        int[] idsGrupos = amigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        int nGruposValidos = 0;
        for (int idsGrupo : idsGrupos) {
            Grupos temp = amigoOculto.read(idsGrupo);
            if (temp.getAtivo()) {
                MyIO.print(nGruposValidos + ". ");
                temp.mostrar();
                nGruposValidos++;
            }
        }
        MyIO.println("Digite o numero do grupo que deseja desativar");
        int i = MyIO.readInt();
        if (i >= 0 && i < idsGrupos.length) {
            int alterarReal = 0;
            int cont = 0;
            while (cont != i) {
                Grupos temp = amigoOculto.read(idsGrupos[alterarReal++]);
                if (temp.getAtivo()) cont++;
            }
            Grupos desativar = amigoOculto.read(idsGrupos[alterarReal]);
            desativar.mostrar();
            MyIO.println("Digite 1 se deseja confirmar a desativação: ");
            int escolha = MyIO.readInt();
            if (escolha == 1) {
                desativar.setAtivo(false);
                amigoOculto.update(desativar);
                MyIO.println("Desativação efetuada com sucesso");
            }
        } else {
            MyIO.println("Opção invalida");
        }
        pressioneTeclaParaContinuar();
    }

    public static boolean vereficaData(String s) {
        boolean resp = true;
        int i = 0;
        while (i < s.length() && resp) {
            if (!Character.isDigit(s.charAt(i++))) resp = false;
        }
        return resp;
    }

    public static int escolherOpcaoGrupos(CRUD<Grupos> amigoOculto) throws Exception {
        int opcao;
        try {
            opcao = controladorPrograma.escolherOpcaoGrupos();
            //listar
            //incluir
            //alterar
            //desativar
            //saída
            switch (opcao) {
                case 1 -> listarGrupos(amigoOculto);
                case 2 -> criarGrupo(amigoOculto);
                case 3 -> alterarGrupos(amigoOculto);
                case 4 -> desativarGrupo(amigoOculto);
                case 0 -> {
                    return 0;
                }
                default -> System.out.println("Opção inválida inserida, retornando..");
            }

        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcao = 0;
        }
        return opcao;
    }

    public static void gerenciamentoGrupos(CRUD<Grupos> amigoOculto, CRUD<Convites> amigoOcultoc) throws Exception {
        int opcao;
        try {
            opcao = controladorPrograma.gerenciamentoGrupos();
            //gerenciamento de grupos
            //convites
            //participantes
            //sorteio
            //saída
            switch (opcao) {
                case 1 -> {
                    int opcaoGrupos;
                    do {
                        opcaoGrupos = escolherOpcaoGrupos(amigoOculto);
                    } while (opcaoGrupos != 0);

                }
                case 2 -> menuLogadoConvite(amigoOcultoc, amigoOculto);
                case 3 -> Participacao.menuGerenciamentoParticipacao(controladorPrograma.getIdUsuarioAtual());
                case 4 -> Sorteio.sortearGrupo(controladorPrograma.getIdUsuarioAtual());
                case 0 -> System.out.println("Retornando..");
                default -> System.out.println("Opção inválida inserida, retornando..");
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcao = 0;
        }
    }

    public static int menuLogadoGrupos(CRUD<Grupos> amigoOculto, CRUD<Convites> amigoOcultoc, CRUD<Usuario> amigoOcultoU, CRUD<Participacao> amigoOcultoP, CRUD<Mensagem> amigoOcultoM, CRUD<Sugestao> sugestaoAmigoOculto) throws Exception {
        int opcao;
        try {
            opcao = controladorPrograma.Grupos();
            //Criação e gerenciamento de grupos
            //Participação nos grupos
            //saída
            switch (opcao) {
                case 1 -> gerenciamentoGrupos(amigoOculto, amigoOcultoc);
                case 2 -> menuLogadoParticipacao(amigoOcultoP, amigoOcultoU, amigoOcultoM, amigoOculto, sugestaoAmigoOculto);
                case 0 -> {
                    System.out.println("Retornando..");
                    return 0;
                }
                default -> System.out.println("Opção inválida inserida, retornando..");
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcao = 0;
        }

        return opcao;
    }


    // ============== MÉTODOS DE SUGESTÕES =================================================================================


    public static int menuLogadoSugestao(CRUD<Sugestao> amigoOculto) {
        int opcaoEscolhidaSugestoes;
        try {
            opcaoEscolhidaSugestoes = controladorPrograma.escolherOpcaoSugestao();
            //listar
            //incluir
            //alterar
            //excluir
            //saída
            switch (opcaoEscolhidaSugestoes) {
                case 1 -> listarSugestoes(amigoOculto);
                case 2 -> incluirSugestoes(amigoOculto);
                case 3 -> {
                }
                case 4 -> {
                }
                case 0 -> {
                    System.out.println("Retornando..");
                    return 0;
                }
                default -> System.out.println("Opção inválida inserida, retornando..");
            }
            System.out.print("Para voltar ao menu principal, aperte [0]." +
                    "\nPara continuar no menu de operações, aperte qualquer outro número." +
                    "\n Opção: ");
            opcaoEscolhidaSugestoes = MyIO.readInt();

        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcaoEscolhidaSugestoes = 0;
        }

        return opcaoEscolhidaSugestoes;
    }

    // TODO: Resolver problema com a listagem e nulls.
    public static void listarSugestoes(CRUD<Sugestao> amigoOculto) {
        //terei que procurar todas as sugestoes e, se elas baterem com o id, listar
        int idUser = controladorPrograma.getIdUsuarioAtual();
        try {
            System.out.println("MINHAS SUGESTÕES");

            //metodo retorna um int[] com todas os ids batendo a c1
            int[] idsSugestoes = amigoOculto.índiceIndiretoIntInt.read(idUser);
            int contador = 0;

            //loop que vai procurar os n elementos retornados dentro do banco e printar os valores
            for (int i = 1; i <= idsSugestoes.length; i++, contador++) {
                Sugestao temp = amigoOculto.read(i);

                if (temp != null) {
                    //chamada do metodo toString da sugestão
                    System.out.println(i + ". " + temp);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pressioneTeclaParaContinuar();

    }

    //o método deve inserir na árvore int int && no banco.
    public static void incluirSugestoes(CRUD<Sugestao> amigoOculto) {

        System.out.print("Produto: ");
        String produto = MyIO.readLine();
        if (produto.equals(""))
            return;
        System.out.print("Loja: ");
        String loja = MyIO.readLine();
        System.out.print("Preço: ");
        String precoString = MyIO.readLine();

        //limpar a string inserida
        if (precoString.contains(" ")) {
            precoString = precoString.replace(" ", "");
        }
        if (precoString.contains("R$")) {
            precoString = precoString.replace("R$", "");
        }
        if (precoString.contains("reais")) {
            precoString = precoString.replace("reais", "");
        }
        if (precoString.contains(",")) {
            precoString = precoString.replace(",", ".");
        }

        float preco = Float.parseFloat(precoString);

        System.out.print("Observações: ");
        String observacoes = MyIO.readLine();

        //confirmacao de cadastro
        System.out.println("Confirme a criação do produto apertando [1]. \nCaso contrário, você retornará ao menu anterior.\nOpção: ");
        if (!MyIO.readLine().equals("1"))
            return;

        Sugestao temp = new Sugestao(produto, loja, preco, observacoes, controladorPrograma.getIdUsuarioAtual());

        try {
            //efetivamente criar no banco
            int idSug = amigoOculto.create(temp);
            temp.setId(idSug);


            //adicionar a arvore B int int
            amigoOculto.índiceIndiretoIntInt.create(temp.getIdUsuario(), temp.getId());
            // System.out.printf("Acabei de adicionar a sugestão de [%s], com idSug [%d] e idUser [%d] na árvore..\n", temp.getProduto(), temp.getId(), temp.getIdUsuario());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Mesmo método que o anterior, só que apenas para uso interno do código.
     *
     * @param amigoOculto banco de dados de sugestoes
     * @param jaCriada    objeto sugestao pronto para ser inserido.
     */
    private static void incluirSugestoes(CRUD<Sugestao> amigoOculto, Sugestao jaCriada) {


        try {
            //efetivamente criar no banco
            int idSug = amigoOculto.create(jaCriada);
            jaCriada.setId(idSug);

            //adicionar a arvore B int int
            amigoOculto.índiceIndiretoIntInt.create(jaCriada.getIdUsuario(), jaCriada.getId());
            //System.out.printf("Acabei de adicionar a sugestão de [%s], com idSug [%d] e idUser [%d] na árvore..\n", jaCriada.getProduto(), jaCriada.getId(), jaCriada.getIdUsuario());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void alterarSugestoes(CRUD<Sugestao> amigoOculto) {

        // menu com o displayzinho
        controladorPrograma.menuSugestoesAlteracao();


        //mapeamento de inteiros para sugestões
        HashMap<Integer, Sugestao> mapaSugestoes = new HashMap<>();

        int idUser = controladorPrograma.getIdUsuarioAtual();

        //metodo retorna um int[] com todas os ids batendo a c1, que é o id do user atual
        int[] idsSugestoes;
        try {
            idsSugestoes = amigoOculto.índiceIndiretoIntInt.read(idUser);

            int numeroMascarado = 1;

            //adicionar no hashmap e em seguida listar
            for (int i = 0; i < idsSugestoes.length; i++, numeroMascarado++) {
                Sugestao temp = amigoOculto.read(idsSugestoes[i]);
                mapaSugestoes.put(numeroMascarado, temp);
                MyIO.print("Sugestão " + numeroMascarado + ": " + temp);
            }

            //pronto, todos os dados estão no hashmap. Basta perguntar ao usuario qual sugestão ele deseja alterar

            int opcao = controladorPrograma.escolherOpcaoAlteracaoSugestao();

            //saida
            if (opcao == 0) {
                MyIO.println("Ok, retornando.");
            }

            //vendo se a opção está no dominio de opceos validas
            else if (opcao <= idsSugestoes.length) {
                Sugestao escolhida = mapaSugestoes.get(opcao);

                Sugestao retiradaDoCrud = amigoOculto.read(escolhida.getId());

                if (escolhida == retiradaDoCrud) {
                    MyIO.println("Elas são a mesma referência, nao precisa fazer o read, Diogo.");
                }

                // pedir ao usuario as alterações e, se necessário, inserí-las num outro objeto.
                // caso ele não queira alterar, será retornada um clone do objeto passado por referência.
                Sugestao inserir = alterarSugestaoEspecifica(retiradaDoCrud);

                excluirSugestoes(amigoOculto, retiradaDoCrud);

                //incluir no indice e crud
                incluirSugestoes(amigoOculto, inserir);
            }
            //numero digitado muito alto
            else {
                throw new Exception("número da opção inválido");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static Sugestao alterarSugestaoEspecifica(Sugestao antiga) {

        Sugestao nova = new Sugestao();
        String entrada;

        boolean alterado = false;

        System.out.println("Altere agora as opções. Para manter do jeito que está, basta não escrever nada.");


        // Produto

        MyIO.print("Produto atual: " + antiga.getProduto() + "Novo nome do produto: ");

        entrada = MyIO.readLine();

        if (entrada.equals("") || entrada.equals(" ") || entrada.equals("\n")) {
            //não alterado, seta o nome antigo
            nova.setProduto(antiga.getProduto());
        } else {
            nova.setProduto(entrada);
            alterado = true;
        }

        // Loja

        MyIO.print("Loja atual: " + antiga.getLoja() + "Novo nome da loja: ");

        entrada = MyIO.readLine();

        if (entrada.equals("") || entrada.equals(" ") || entrada.equals("\n")) {
            //não alterado, seta o nome antigo
            nova.setLoja(antiga.getLoja());
        } else {
            nova.setLoja(entrada);
            alterado = true;
        }

        // Preço

        MyIO.print("Preço atual: " + antiga.getValor() + "Novo valor: ");

        float novoValor = MyIO.readFloat();

        if (novoValor == 0) {
            //não alterado, seta o nome antigo
            nova.setValor(antiga.getValor());
        } else {
            nova.setValor(novoValor);
            alterado = true;
        }

        // Observações

        MyIO.print("Observação atual atual: " + antiga.getLoja() + "\nNova observação: ");

        entrada = MyIO.readLine();

        if (entrada.equals("") || entrada.equals(" ") || entrada.equals("\n")) {
            //não alterado, seta o nome antigo
            nova.setObservacoes(antiga.getObservacoes());
        } else {
            nova.setObservacoes(entrada);
            alterado = true;
        }

        if (alterado) {
            MyIO.print("Digite [0] para confirmar a alteração. Caso contrário voltaremos ao menu anterior, sem nenhuma alteração feita.\n Opção: ");

            //usuário não quis confirmar a alteração
            if (MyIO.readInt() != 0) {
                //clonado a antiga, já que vou apagá-la
                try {
                    nova = antiga.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                nova.setId(-1);
            }
        }

        return nova;
    }

    public static void excluirSugestoes(CRUD<Sugestao> amigoOculto, Sugestao removida) {

        try {

            amigoOculto.delete(removida.getId());
            amigoOculto.índiceIndiretoIntInt.delete(removida.getIdUsuario(), removida.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void excluirSugestoes(CRUD<Sugestao> amigoOculto) {

        //terei que procurar todas as sugestoes e, se elas baterem com o id, listar
        int idUser = controladorPrograma.getIdUsuarioAtual();
        try {
            System.out.println("Exclusão de sugestões.\nListaremos abaixo suas sugestões");

            //metodo retorna um int[] com todas os ids batendo a c1
            int[] idsSugestoes = amigoOculto.índiceIndiretoIntInt.read(idUser);
            int contador = 0;

            //loop que vai procurar os n elementos retornados dentro do banco e printar os valores
            for (int i = 1; i <= idsSugestoes.length; i++, contador++) {
                Sugestao temp = amigoOculto.read(i);

                if (temp != null) {
                    //chamada do metodo toString da sugestão
                    System.out.println(i + ". " + temp);

                }
            }
            MyIO.print("Digite o número da sugestão que quer excluir ou [0] para cancelar: ");
            int excluir = MyIO.readInt();

            if (excluir == 0) {
                // nao faz nada
                MyIO.println("Ok, remoção cancelada. Retornando");
            } else if (excluir > 0 && excluir <= idsSugestoes.length) {


                Sugestao excluida = amigoOculto.read(idsSugestoes[excluir - 1]);
                MyIO.println("Sugestão escolhida: " + excluida);
                MyIO.print("Para confirmar  exclusão, digite [0]. Se não, não faremos nada. Opção: ");
                int confirmacao = MyIO.readInt();
                //chamada do método privado de exclusao

                if (confirmacao == 0) {
                    excluirSugestoes(amigoOculto, excluida);
                } else
                    MyIO.println("Ok, remoção cancelada. Retornando");
            } else
                throw new Exception("Entrada fora de domínio.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // ============== MÉTODOS DE USUÁRIO =================================================================================

    /**
     * Métódo que contêm o menu de login, com opções de criar ou fazer login
     *
     * @param amigoOculto banco de dados dos usuarios
     * @return opção escolhida no menu
     * @throws Exception por problemas nos metodos do crud
     */
    public static int menuLogin(CRUD<Usuario> amigoOculto) throws Exception {
        int opcaoEscolhida = controladorPrograma.escolherOpcaoUsuarios();
        switch (opcaoEscolhida) {
            case 1 -> {
                int idTemp = acesso(amigoOculto);
                if (idTemp != -1) {
                    controladorPrograma.salvarIdUsuarioAtual(idTemp);
                }
            }
            case 2 -> novoUsuario(amigoOculto);
            case 0 -> {
            }
            default -> System.out.println("Opção inválida.. retornando ao menu principal");
        }
        return opcaoEscolhida;
    }

    /**
     * Consulta de usuarios do sistema
     * Chave: email
     *
     * @return id do usuario. Se ele nao logar retornará -1
     */
    public static int acesso(CRUD<Usuario> amigoOculto) throws Exception {
        System.out.println("ACESSO AO SISTEMA");
        System.out.print("Digite seu email: ");
        int idUsuario = -1;

        String email = MyIO.readLine();

        //buscar email dentro do banco e, se não encontrar, será igual a null
        if (amigoOculto.read(email) == null) {
            System.out.println("\nUsuário não cadastrado\n");
            System.out.println("Email passado para busca: " + email);
            pressioneTeclaParaContinuar();
        } else {

            //repetir inserção de senha.
            int continuarTentando;
            do {
                continuarTentando = 0;

                System.out.print("Senha: ");
                String senha = MyIO.readLine();
                Usuario temp = amigoOculto.read(email);

                //verificar se a senha é correta
                if (temp.getSenha().compareTo(senha) == 0) {
                    System.out.println("Senha correta");
                    idUsuario = temp.getId();
                    pressioneTeclaParaContinuar();
                } else {
                    System.out.println("\nSenha Incorreta...");
                    System.out.println("Se quiser retornar ao acesso para tentar de novo, digite [0]." +
                            "\nPara tentar a senha de novo, digite [1]. " +
                            "\nPara voltar ao menu aperte qualquer outra tecla.");

                    int a = MyIO.readInt();

                    if (a == 0) {
                        acesso(amigoOculto);
                    } else if (a == 1)
                        continuarTentando = 1;
                }
            } while (continuarTentando == 1);
        }
        return idUsuario;
    }

    /**
     * Cadastro de novo usuario no crud
     */
    public static void novoUsuario(CRUD<Usuario> amigoOculto) throws Exception {
        System.out.println("NOVO USUÁRIO");
        System.out.print("Email: ");
        String email = MyIO.readLine();

        //email.replace("\n", "");

        if (!email.equals("")) {
            if (amigoOculto.read(email) == null) {
                System.out.print("Nome: ");
                String nome = MyIO.readLine();
                System.out.print("Senha: ");
                String senha = MyIO.readLine();

                System.out.printf("Dados que você digitou: \nEmail:\t[%s]\nNome:\t[%s]\nSenha:\t[%s]\n", email, nome, senha);
                System.out.print("Digite 1 se deseja confirmar o cadastro: ");

                int confirmar = MyIO.readInt();

                if (confirmar == 1) {
                    Usuario temp = new Usuario(nome, email, senha);
                    int novoId = amigoOculto.create(temp);
                    temp.setId(novoId);

                    System.out.println("Usuário cadastrado com sucesso\n");

                } else {
                    System.out.println("Cadastro cancelado\n");

                }
            } else {
                System.out.println("ERRO! Email já cadastrado. Retornando ao menu");

            }

        } else {
            System.out.println("\nUsuário Invalido");
        }
        pressioneTeclaParaContinuar();
    }

    public static void pressioneTeclaParaContinuar() {
        System.out.print("Pressione qualquer tecla para continuar..");
        MyIO.readChar();
    }
}
