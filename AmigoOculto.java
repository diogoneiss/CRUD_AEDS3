import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.InputMismatchException;

public class AmigoOculto {

    //Singleton controlador
    private static final ControladorSingleton controladorPrograma = ControladorSingleton.getInstance();

    public static void main(String[] args) {

        MyIO.setCharset("UTF-8");

        try {

            //bancos de dados
            CRUD<Usuario> usuariosAmigoOculto = new CRUD<>("amigos", Usuario.class.getConstructor());
            CRUD<Sugestao> sugestaoAmigoOculto = new CRUD<>("sugestoes", Sugestao.class.getConstructor());
            CRUD<Grupos> gruposAmigoOculto = new CRUD<>("grupos", Grupos.class.getConstructor());
            CRUD<Convites> conviteAmigoOculto = new CRUD<>("convite", Convites.class.getConstructor());

            //setar os bancos na classe estática das inscricoes
            Inscricao.setBancos(usuariosAmigoOculto, gruposAmigoOculto, conviteAmigoOculto);

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
                        switch (opcaoMenuInicial) {
                            //sugestões
                            case 1: {
                                int opcaoEscolhidaSugestoes;
                                do {

                                    opcaoEscolhidaSugestoes = menuLogadoSugestao(sugestaoAmigoOculto);

                                    //saída do menu logado
                                } while (opcaoEscolhidaSugestoes != 0);
                                break;
                            }
                            //grupos
                            case 2: {
                                int opcaoEscolhidaGrupos = 0;
                                do {
                                    opcaoEscolhidaGrupos = menuLogadoGrupos(gruposAmigoOculto, conviteAmigoOculto);
                                } while (opcaoEscolhidaGrupos != 0);
                                break;
                            }
                            //Usuários
                            case 3: {
                                break;
                            }
                            //sair
                            case 0: {
                                break;
                            }
                            default: {
                                System.out.println("Opção inválida inserida, retornando..");
                            }
                        }


                        //como saímos do menu logado não é necessário mais armazenar o id do usuario atual
                        controladorPrograma.finalizarIdUsuarioAtual();
                    } while (opcaoMenuInicial != 0);
                }

                //despedir se o usuario quiser sair do programa.
                if (opcaoEscolhidaMenuLogin == 0)
                    System.out.println("Saindo..");


                //fim do do-while principal, ou seja, fim do programa.
            } while (opcaoEscolhidaMenuLogin != 0);


        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        } catch (InputMismatchException erroInput) {
            System.out.println("Você digitou um caractere inválido e o programa crashou por isso.");
            erroInput.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int menuInicial(Usuario userLogado) throws Exception {
        int opcaoEscolhida = controladorPrograma.escolherOpcaoMenuInicial(userLogado);
        return opcaoEscolhida;
    }
// ============== MÉTODOS DE CONVITE =================================================================================

    public static void cancelamentoConvite(CRUD<Convites> amigoOculto, CRUD<Grupos> gruposAmigoOculto) throws Exception {
        MyIO.println("ESCOLHA O GRUPO: ");
        int[] idsGrupos = gruposAmigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        int i = 1;
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
                int j = 1;
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
                            Convites novo = new Convites(temp.getId(), temp.getIdGrupo(), temp.getEmail(), temp.getMomentoConvite(), byte3);
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
            opcao = 0;
        }

        pressioneTeclaParaContinuar();
    }

    public static void emissaoConvite(CRUD<Convites> amigoOculto, CRUD<Grupos> gruposAmigoOculto) throws Exception {
        MyIO.println("ESCOLHA O GRUPO: ");
        int[] idsGrupos = gruposAmigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        int i = 1;
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
                    if (email == "") {
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
                                Convites temp = new Convites(id, idsGrupos[opcao - 1], email, momento.getTimeInMillis(), byte0);
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
                        Convites temp = new Convites(id, idsGrupos[opcao - 1], email, momento.getTimeInMillis(), byte0);
                        try {
                            id = amigoOculto.create(temp);
                        } catch (Exception e) {
                            MyIO.println("Erro");
                        }
                        amigoOculto.índiceIndireto.create(temp.chaveSecundaria(), temp.getId());
                        amigoOculto.índiceIndiretoIntInt.create(controladorPrograma.getIdUsuarioAtual(), id);
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
        for (int i = 1; i <= idsGrupos.length; i++) {
            Grupos temp = gruposAmigoOculto.read(idsGrupos[i - 1]);
            if (temp.getAtivo()) {
                MyIO.println(i + ". " + temp.getNome());
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
                for (int i = 1; i <= chavesConvites.length; i++) {
                    Convites temp = amigoOculto.read(chavesConvites[i]);
                    String tempestado = "";
                    if (temp.getEstado() == 0) tempestado = "pendente";
                    else if (temp.getEstado() == 1) tempestado = "aceito";
                    else if (temp.getEstado() == 2) tempestado = "recusado";
                    else if (temp.getEstado() == 3) tempestado = "cancelado";
                    Calendar momento = Calendar.getInstance();
                    momento.setTimeInMillis(temp.getMomentoConvite());
                    MyIO.println(i + ". " + temp.getEmail() + " (" + momento.get(Calendar.DATE) + "/" + momento.get(Calendar.MONTH) + "/" + momento.get(Calendar.YEAR) + " " + momento.get(Calendar.HOUR_OF_DAY) + " - " + tempestado + ")");
                }
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcao = 0;
        }
        pressioneTeclaParaContinuar();
    }

    public static int menuLogadoConvite(CRUD<Convites> amigoOculto, CRUD<Grupos> gruposAmigoOculto) throws Exception {
        int opcao;
        try {
            opcao = controladorPrograma.convites();
            switch (opcao) {
                //Listagem de convites
                case 1: {
                    listagemConvite(amigoOculto, gruposAmigoOculto);
                    break;
                }
                //Emissão de convies
                case 2: {
                    emissaoConvite(amigoOculto, gruposAmigoOculto);
                    break;
                }
                //Cancelamento de convies
                case 3: {
                    cancelamentoConvite(amigoOculto, gruposAmigoOculto);
                    break;
                }
                //saída
                case 0: {
                    System.out.println("Retornando..");
                    return 0;
                }
                default: {
                    System.out.println("Opção inválida inserida, retornando..");
                }
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcao = 0;
        }
        return opcao;
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
            while (valido == false) {
                int dia, mes, ano, hora;
                Calendar atual = Calendar.getInstance();
                Calendar novo = Calendar.getInstance();
                do {
                    MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                    String temp = MyIO.readLine();
                    String[] aux = temp.split(" ");
                    ano = Integer.parseInt(aux[0]);
                    mes = Integer.parseInt(aux[1]);
                    dia = Integer.parseInt(aux[2]);
                    hora = Integer.parseInt(aux[3]);
                    if ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23)) {
                        MyIO.println("Data invalida!!!");
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
            while (valido == false) {
                int dia, mes, ano, hora;
                Calendar sorteio = Calendar.getInstance();
                sorteio.setTimeInMillis(dataSorteio);
                Calendar novo = Calendar.getInstance();
                do {
                    MyIO.println("Digite o ano, o mes, o dia e a hora do encontro respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                    String temp = MyIO.readLine();
                    String[] aux = temp.split(" ");
                    ano = Integer.parseInt(aux[0]);
                    mes = Integer.parseInt(aux[1]);
                    dia = Integer.parseInt(aux[2]);
                    hora = Integer.parseInt(aux[3]);
                    if ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23)) {
                        MyIO.println("Data invalida!!!");
                    }
                } while ((ano < 2020) || (mes < 0 || mes > 12) || (dia < 0 || dia > 31) || (hora < 0 || hora > 23));
                novo.set(ano, mes, dia, hora, 0);
                if (novo.compareTo(sorteio) > 0) {
                    valido = true;
                    dataEncontro = novo.getTimeInMillis();
                } else {
                    MyIO.println("Data antes do sorteio, invalida!");
                }
            }
            String local;
            do {
                MyIO.println("Digite o local do encontro: ");
                local = MyIO.readLine();
            } while (local == "");
            String observacoes;
            MyIO.println("Digite obserções adcionais: ");
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
    }

    public static void listarGrupos(CRUD<Grupos> amigoOculto) throws Exception {
        int[] idsGrupos = amigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        for (int i = 0; i < idsGrupos.length; i++) {
            Grupos temp = amigoOculto.read(idsGrupos[i]);
            if (temp.getAtivo()) {
                MyIO.println(i + ". " + temp.getNome());
            }
        }
        pressioneTeclaParaContinuar();
    }

    public static void alterarGrupos(CRUD<Grupos> amigoOculto) throws Exception {
        int[] idsGrupos = amigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        for (int i = 0; i < idsGrupos.length; i++) {
            Grupos temp = amigoOculto.read(idsGrupos[i]);
            if (temp.getAtivo()) {
                MyIO.print(i + ". ");
                temp.mostrar();
            }
        }
        int alteracoes = 0;
        MyIO.println("Digite o numero do grupo que deseja alterar ou 0 para sair: ");
        int alterar = MyIO.readInt();
        if (alterar >= 0) {
            if (alterar < idsGrupos.length) {
                Grupos temp = amigoOculto.read(idsGrupos[alterar]);
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
                while (valido == false) {
                    int dia, mes, ano, hora;
                    dia = mes = ano = hora = 0;
                    Calendar atual = Calendar.getInstance();
                    Calendar novo = Calendar.getInstance();
                    MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                    String data = MyIO.readLine();
                    if (data.compareTo("") == 0) {
                        valido = true;
                        dataSorteio = temp.getMomentoSorteio();
                    } else {
                        do {
                            alteracoes++;
                            String[] aux = data.split(" ");
                            ano = Integer.parseInt(aux[0]);
                            mes = Integer.parseInt(aux[1]);
                            dia = Integer.parseInt(aux[2]);
                            hora = Integer.parseInt(aux[3]);
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
                float valor;
                if (s.compareTo("") == 0) {
                    valor = temp.getValor();
                } else {
                    alteracoes++;
                    valor = Float.parseFloat(s);
                }
                valido = false;
                long dataEncontro = 0;
                while (valido == false) {
                    int dia, mes, ano, hora;
                    dia = mes = ano = hora = 0;
                    Calendar atual = Calendar.getInstance();
                    Calendar novo = Calendar.getInstance();
                    MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
                    String data = MyIO.readLine();
                    if (data.compareTo("") == 0) {
                        valido = true;
                        dataEncontro = temp.getMomentoSorteio();
                    } else {
                        do {
                            alteracoes++;
                            String[] aux = data.split(" ");
                            ano = Integer.parseInt(aux[0]);
                            mes = Integer.parseInt(aux[1]);
                            dia = Integer.parseInt(aux[2]);
                            hora = Integer.parseInt(aux[3]);
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
                            MyIO.println("Data antes da atual, invalida!");
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
                        MyIO.println("Usuário alterado com sucesso!");
                    } else {
                        MyIO.println("Nenhuma alteração foi feita");
                    }
                }
            } else {
                MyIO.println("Número invalido");
            }
        }
    }

    public static void desativarGrupo(CRUD<Grupos> amigoOculto) throws Exception {
        int[] idsGrupos = amigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
        for (int i = 0; i < idsGrupos.length; i++) {
            Grupos temp = amigoOculto.read(idsGrupos[i]);
            if (temp.getAtivo()) {
                MyIO.print(i + ". ");
                temp.mostrar();
            }
        }
        MyIO.println("Digite o numero do grupo que deseja desativar");
        int i = MyIO.readInt();
        if (i >= 0 && i < idsGrupos.length) {
            Grupos desativar = amigoOculto.read(idsGrupos[i]);
            desativar.mostrar();
            MyIO.println("Digite 1 se deseja confirmar a desativação: ");
            int escolha = MyIO.readInt();
            if (escolha == 1) {
                desativar.setAtivo(false);
                amigoOculto.update(desativar);
                MyIO.println("Desativação efetuada com sucesso");
            }
        }
    }

    public static int escolherOpcaoGrupos(CRUD<Grupos> amigoOculto) throws Exception {
        int opcao = 0;
        try {
            opcao = controladorPrograma.escolherOpcaoGrupos();
            switch (opcao) {
                //listar
                case 1: {
                    listarGrupos(amigoOculto);
                    break;
                }
                //incluir
                case 2: {
                    criarGrupo(amigoOculto);
                    break;
                }
                //alterar
                case 3: {
                    alterarGrupos(amigoOculto);
                    break;
                }
                //desativar
                case 4: {
                    desativarGrupo(amigoOculto);
                    break;
                }
                //saída
                case 0: {
                    return 0;
                }
                default: {
                    System.out.println("Opção inválida inserida, retornando..");
                }

            }

        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcao = 0;
        }
        return opcao;
    }

    public static int gerenciamentoGrupos(CRUD<Grupos> amigoOculto, CRUD<Convites> amigoOcultoc) throws Exception {
        int opcao;
        try {
            opcao = controladorPrograma.gerenciamentoGrupos();
            switch (opcao) {
                //gerenciamento de grupos
                case 1: {
                    int opcaoGrupos;
                    do {
                        opcaoGrupos = escolherOpcaoGrupos(amigoOculto);
                    } while (opcaoGrupos != 0);
                    break;
                }
                //convites
                case 2: {
                    menuLogadoConvite(amigoOcultoc, amigoOculto);
                    break;
                }
                //participantes
                case 3: {

                    break;
                }
                //sorteio
                case 4: {

                    break;
                }
                //saída
                case 0: {
                    System.out.println("Retornando..");
                    return 0;
                }
                default: {
                    System.out.println("Opção inválida inserida, retornando..");
                }
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcao = 0;
        }
        return opcao;
    }

    public static int menuLogadoGrupos(CRUD<Grupos> amigoOculto, CRUD<Convites> amigoOcultoc) throws Exception {
        int opcao;
        try {
            opcao = controladorPrograma.Grupos();
            switch (opcao) {
                //Criação e gerenciamento de grupos
                case 1: {
                    gerenciamentoGrupos(amigoOculto, amigoOcultoc);
                    break;
                }
                //Participação nos grupos
                case 2: {

                    break;
                }
                //saída
                case 0: {
                    System.out.println("Retornando..");
                    return 0;
                }
                default: {
                    System.out.println("Opção inválida inserida, retornando..");
                }
            }
        } catch (InputMismatchException erroInput) {
            System.out.println("Você inseriu um caractere inválido onde era para ser inserido um número. Retornando ao menu principal");
            opcao = 0;
        }
        return opcao;
    }


    // ============== MÉTODOS DE SUGESTÕES =================================================================================


    public static int menuLogadoSugestao(CRUD<Sugestao> amigoOculto) {
        int opcaoEscolhidaSugestoes = 0;
        try {
            opcaoEscolhidaSugestoes = controladorPrograma.escolherOpcaoSugestao();
            switch (opcaoEscolhidaSugestoes) {
                //listar
                case 1: {
                    listarSugestoes(amigoOculto);
                    break;
                }
                //incluir
                case 2: {
                    incluirSugestoes(amigoOculto);
                    break;
                }
                //alterar
                case 3: {
                    break;
                }
                //excluir
                case 4: {
                    break;
                }
                //saída
                case 0: {
                    System.out.println("Retornando..");
                    return 0;
                }
                default: {
                    System.out.println("Opção inválida inserida, retornando..");
                }

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
        } catch (IOException e) {
            e.printStackTrace();
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
        } catch (IOException e) {
            e.printStackTrace();
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

        } catch (IOException e) {
            e.printStackTrace();
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
        int[] idsSugestoes = new int[0];
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static Sugestao alterarSugestaoEspecifica(Sugestao antiga) {

        Sugestao nova = new Sugestao();
        String entrada = "";

        Boolean alterado = false;

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
            case 1: {
                int idTemp = acesso(amigoOculto);
                if (idTemp != -1) {
                    controladorPrograma.salvarIdUsuarioAtual(idTemp);
                }
                break;
            }
            case 2: {
                novoUsuario(amigoOculto);
                break;
            }
            case 0: {
                break;
            }
            default:
                System.out.println("Opção inválida.. retornando ao menu principal");
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
                    pressioneTeclaParaContinuar();

                } else {
                    System.out.println("Cadastro cancelado\n");
                    pressioneTeclaParaContinuar();

                }
            } else {
                System.out.println("ERRO! Email já cadastrado. Retornando ao menu");

            }

        } else {
            System.out.println("\nUsuário Invalido");
        }
    }

    public static void pressioneTeclaParaContinuar() {
        System.out.print("Pressione qualquer tecla para continuar..");
        MyIO.readChar();
    }
}
