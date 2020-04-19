import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AmigoOculto {

	//o Scanner é atributo da classe pq todos os metodos usam ele.
	public static Scanner ler = new Scanner(System.in);

	public static void main(String[] args) {

		try {
			//Singleton controlador
			ControladorSingleton controladorPrograma = ControladorSingleton.getInstance();

			//bancos de dados
			CRUD<Usuario> usuariosAmigoOculto = new CRUD<>("amigos", Usuario.class.getConstructor());
			CRUD<Sugestao> sugestaoAmigoOculto = new CRUD<>("sugestoes", Sugestao.class.getConstructor());

			int opcaoEscolhida;

			//Menu principal, em que os usuarios criam ou logam.
			do {
				opcaoEscolhida = controladorPrograma.escolherOpcaoUsuarios();

				if (opcaoEscolhida == 1) {
					int idTemp = acesso(usuariosAmigoOculto);
					if (idTemp != -1) {
						ControladorSingleton.salvarIdUsuarioAtual(idTemp);
					}
				} else if (opcaoEscolhida == 2)
					novoUsuario(usuariosAmigoOculto);
				else if (opcaoEscolhida == 0)
					System.out.println("Retornando..");
				else
					System.out.println("Opção inválida.. retornando ao menu principal");


				//referente ao menu de sugestões. O usuário deve estar logado.
				if (ControladorSingleton.idUsuarioAtual != -1) {
					int opcaoEscolhidaSugestoes = -1;
					do {
						opcaoEscolhidaSugestoes = controladorPrograma.escolherOpcaoSugestao();
						switch (opcaoEscolhidaSugestoes) {
							//listar
							case 1: {
								break;
							}
							//incluir
							case 2: {
								incluirSugestoes(sugestaoAmigoOculto);
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
								break;
							}
							default: {
								System.out.println("Opção inválida inserida, retornando..");
							}

						}

						System.out.print("Para voltar ao menu principal, aperte [0].\n Para continuar no menu de operações, aperte qualquer outro número.\n Opção: ");
						opcaoEscolhidaSugestoes = ler.nextInt();

					} while (opcaoEscolhidaSugestoes != 0);

				} else if (opcaoEscolhida == 0)
					System.out.println("Saindo..");


			} while (opcaoEscolhida != 0);


		} catch (NoSuchMethodException noSuchMethodException) {
			noSuchMethodException.printStackTrace();
		} catch (InputMismatchException erroInput) {
			System.out.println("Você digitou um caractere inválido e o programa crashou por isso.");
			erroInput.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		//fim do programa
		ler.close();

	}

	/**
	 * Listagem
	 * Na maioria dos sistemas que oferecem relatórios baseados em algum tipo de filtro, é comum encontrarmos arquivos auxiliares para a geração desses relatórios.
	 * Caso contrário, seria necessário percorrer todo o arquivo principal e analisar cada um dos registros para ver qual atende aos critérios do filtro.
	 * <p>
	 * No nosso caso, o filtro é o atributo idUsuário, isto é, só listaremos as sugestões que
	 * tiverem o valor do atributo idUsuário igual ao ID do usuário que estiver usando o sistema.
	 * <p>
	 * Como estrutura de dados para esse arquivo auxiliar, usaremos uma Árvore B+.
	 * As árvores B+ são estruturas ordenadas que aceleram consideravelmente processos como as listagens.
	 * <p>
	 * A sua Árvore B+ deverá armazenar apenas dois valores para cada sugestão cadastrada no arquivo: o par idUsuário e idSugestão.
	 * Para que tudo funcione adequadamente, é importante que a árvore permita repetições de idUsuário,  pois um usuário pode ter mais de uma sugestão cadastrada. No entanto, não deve ser permitida a repetição do par de chaves [idUsuário, idSugestão].
	 * <p>
	 * Sua árvore já deve oferecer um método para retornar todos os valores de idSugestão cuja primeira chave seja a de um idUsuário especificado.
	 * <p>
	 * A partir dessa lista, você deve consultar cada idSugestão no arquivo de sugestões, usando o índice direto desse arquivo (método read(ID)) e apresentá-la na tela.
	 * <p>
	 * Os passos dessa operação, portanto, são:
	 * <p>
	 * Obter a lista de IDs de sugestões na Árvore B+ usando o ID do usuário;
	 * Para cada ID nessa lista,
	 * Obter os dados da sugestão usando o método read(ID) do CRUD;
	 * Apresentar os dados da sugestão na tela.
	 * Atenção: as sugestões na tela devem ser apresentadas numeradas sequencialmente. O ID da sugestão e o ID do usuário não devem ser apresentados,
	 * pois esses IDs são dados para uso interno pelo sistema. Sua tela deve ficar parecida com esta:
	 * <p>
	 * Todas as operações deverão ser realizadas a partir do menu de sugestões, que pode ter essa aparência:
	 * <p>
	 * MINHAS SUGESTÕES
	 * <p>
	 * 1. Camisa polo branca
	 * Hering
	 * R$ 35,00
	 * Tamanho P
	 */

	public static void listarSugestoes(CRUD<Sugestao> amigoOculto) {
		//terei que procurar todas as sugestoes e, se elas baterem com o id, listar
		int idUser = ControladorSingleton.idUsuarioAtual;


	}

	//o método deve inserir na árvore int int && no banco.
	public static void incluirSugestoes(CRUD<Sugestao> amigoOculto) {

		System.out.print("Produto: ");
		String produto = ler.nextLine();
		if (produto.equals(""))
			return;
		System.out.print("Loja: ");
		String loja = ler.nextLine();
		System.out.print("Preço: ");
		float preco = ler.nextFloat();
		ler.nextLine();
		System.out.print("Observações: ");
		String observacoes = ler.nextLine();

		//confirmacao de cadastro
		System.out.println("Confirme a criação do produto apertando [1]. Caso contrário você retornará ao menu anterior.\nOpção: ");
		if (ler.nextInt() != 1)
			return;


		Sugestao temp = new Sugestao(produto, loja, preco, observacoes, ControladorSingleton.idUsuarioAtual);

		try {
			int idSug = amigoOculto.create(temp);
			temp.setId(idSug);
			amigoOculto.índiceIndiretoIntInt.create(temp.getIdUsuario(), temp.getId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void alterarSugestoes(CRUD<Sugestao> amigoOculto) {

	}

	public static void excluirSugestoes(CRUD<Sugestao> amigoOculto) {

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

		String email = ler.nextLine();

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
				String senha = ler.nextLine();
				Usuario temp = amigoOculto.read(email);

				//verificar se a senha é correta
				if (temp.getSenha().compareTo(senha) == 0) {
					System.out.println("Senha correta");
					idUsuario = temp.getId();

				} else {
					System.out.println("\nSenha Incorreta...");
					System.out.println("Se quiser retornar ao acesso para tentar de novo, digite [0]." +
							  "\nPara tentar a senha de novo, digite [1]. " +
							  "\nPara voltar ao menu aperte qualquer outra tecla.");

					int a = ler.nextInt();
					ler.nextLine();
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
		String email = ler.nextLine();

		//email.replace("\n", "");

		if (!email.equals("")) {
			if (amigoOculto.read(email) == null) {
				System.out.print("Nome: ");
				String nome = ler.nextLine();
				System.out.print("Senha: ");
				String senha = ler.nextLine();

				System.out.printf("Dados que você digitou: \nEmail:\t[%s]\nNome:\t[%s]\nSenha:\t[%s]\n", email, nome, senha);
				System.out.print("Digite 1 se deseja confirmar o cadastro: ");

				int confirmar = ler.nextInt();
				ler.nextLine();

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
		ler.nextLine();
	}
}