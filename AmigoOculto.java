import java.util.Scanner;

public class AmigoOculto {

	//o Scanner é atributo da classe pq todos os metodos usam ele.
	public static Scanner ler = new Scanner(System.in);

	public static void main(String[] args) {

		try {
			ControladorSingleton controladorPrograma = ControladorSingleton.getInstance();
			CRUD<Usuario> usuariosAmigoOculto = new CRUD<>("amigos", Usuario.class.getConstructor());

			int opcaoEscolhida;

			do {
				opcaoEscolhida = controladorPrograma.escolherOpcao();

				if (opcaoEscolhida == 1)
					acesso(usuariosAmigoOculto);

				else if (opcaoEscolhida == 2)
					novoUsuario(usuariosAmigoOculto);

				else if (opcaoEscolhida == 0)
					System.out.println("Saindo..");

				else
					System.out.println("Entrada inválida inserida. Reiniciando.");


			} while (opcaoEscolhida != 0);

			//fim do programa
			ler.close();
		} catch (Exception e) {
			System.out.println("erro");
			e.printStackTrace();
		}
	}

	/**
	 * Consulta de usuarios do sistema
	 * Chave: email
	 */
	public static void acesso(CRUD<Usuario> amigoOculto) throws Exception {
		System.out.println("ACESSO AO SISTEMA");
		System.out.print("Digite seu email: ");

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
					System.out.println("\nMenu não finalizado, porém a senha está correta\n");
					pressioneTeclaParaContinuar();

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