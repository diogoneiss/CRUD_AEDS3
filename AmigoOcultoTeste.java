import java.io.IOException;
import java.util.InputMismatchException;


public class AmigoOcultoTeste {

    //Singleton controlador
    private static final ControladorSingleton controladorPrograma = ControladorSingleton.getInstance();

    public static void main(String[] args) {

        try {

            // criação do banco de dados
            CRUD<Usuario> usuariosAmigoOculto = new CRUD<>("amigos", Usuario.class.getConstructor());
            CRUD<Sugestao> sugestaoAmigoOculto = new CRUD<>("sugestoes", Sugestao.class.getConstructor());

            int opcaoEscolhidaMenuLogin = 0;


            if(usuariosAmigoOculto.read(2) == null){

                //criar objetos preenchidos, pra nao precisar de menu de criação.
                Usuario preencher1 = new Usuario("Diogo", "diogo.oliveiran@gmail.com", "1234");
                Usuario preencher2 = new Usuario("Lorenzo", "lorenzo@hotmail.com", "4321");

                int id1 = usuariosAmigoOculto.create(preencher1);
                preencher1.setId(id1);

                int id2 = usuariosAmigoOculto.create(preencher2);
                preencher1.setId(id2);


                //considera id2 como o usuário atual. Para pegar a id do usuario atual,
                // basta fazer controlador programa . getIdUsuarioAtual()
                controladorPrograma.salvarIdUsuarioAtual(id2);

            }
            else
                controladorPrograma.salvarIdUsuarioAtual(2);


            //Menu da classe, coloque as opções num switch / if elses aqui e depois passe pra uma função
            do {
               opcaoEscolhidaMenuLogin = controladorPrograma.escolherOpcaoSugestao();

               switch (opcaoEscolhidaMenuLogin) {
                   case 1:
                       AmigoOculto.listarSugestoes(sugestaoAmigoOculto);
                       break;
               
                    case 2:
                       AmigoOculto.incluirSugestoes(sugestaoAmigoOculto);
                       break;
                       
                    case 3:
                       AmigoOculto.alterarSugestoes(sugestaoAmigoOculto);
                       break;
                   case 4:
                       AmigoOculto.excluirSugestoes(sugestaoAmigoOculto);
                       break;

                   default:
                       break;
               }

            } while (opcaoEscolhidaMenuLogin != 0);


            //try catches gerados pela minha IDE
        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        } catch (InputMismatchException erroInput) {
            System.out.println("Você digitou um caractere inválido e o programa crashou por isso.");
            erroInput.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ------------------------------------------------------------
    // início dos seus métodos










    //  fim dos seus métodos custom
    // -----------------------------------------------------------------------------------

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