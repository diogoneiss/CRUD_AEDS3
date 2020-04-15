import java.util.Scanner;

public class AmigoOculto {

	//o Scanner é atributo da classe pq todos os metodos usam ele.
	public static Scanner ler = new Scanner(System.in);

	public static void main(String[] args) {

		try {

			CRUD<Usuario> usuariosAmigoOculto = new CRUD<>("amigos", Usuario.class.getConstructor());
			int opcaoEscolhida;

			do {
				System.out.println("Amigo Oculto");
				System.out.println("================");
				System.out.println("1) Acesso ao sistema");
				System.out.println("2) Novo usuário");
				System.out.println("\n0) Sair");
				System.out.print("\nOpção: ");
				opcaoEscolhida = ler.nextInt();

				//puxar o \n
				ler.nextLine();

				if (opcaoEscolhida == 1) {
					acesso(usuariosAmigoOculto);
				} else if (opcaoEscolhida == 2) {
					novoUsuario(usuariosAmigoOculto);
				}
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
		System.out.println("\nACESSO AO SISTEMA");
		System.out.print("\nEmail: ");

		String email = ler.nextLine();

		if (amigoOculto.read(email) == null) {
			System.out.println("\nUsuário não cadastrado\n");
			System.out.println("Email passado para busca: " + email);
		} else {

			System.out.print("Senha: ");
			String senha = ler.nextLine();
			Usuario temp = amigoOculto.read(email);

			if (temp.getSenha().compareTo(senha) == 0) {
				System.out.println("\nMenu não finalizado, porém a senha está correta\n");
				System.out.println("Pressione qualquer tecla para continuar");
				String a = ler.nextLine();
				if (a != null) {
					return;
				}
			} else {
				System.out.println("\nSenha Incorreta");
				System.out.println("Pressione qualquer tecla para continuar");
				String a = ler.nextLine();
				if (a != null) {
					acesso(amigoOculto);
				}
			}
		}
	}

	/**
	 * Cadastro de novo usuario no crud
	 */
	public static void novoUsuario(CRUD<Usuario> amigoOculto) throws Exception {
		System.out.println("\nNOVO USUÁRIO");
		System.out.print("\nEmail: ");
		String email = ler.nextLine();
		if (email.equals("")) {
			System.out.println("\nUsuário Invalido");
		} else {
			if (amigoOculto.read(email) == null) {
				System.out.print("\nNome: ");
				String nome = ler.nextLine();
				System.out.print("\nSenha: ");
				String senha = ler.nextLine();

				System.out.println("\nEmail: " + email + " Nome: " + nome + " Senha: " + senha);
				System.out.print("Digite 1 se deseja confirmar o cadastro: ");

				int confirmar = ler.nextInt();
				ler.nextLine();

				if (confirmar == 1) {
					Usuario temp = new Usuario(nome, email, senha);
					int novoId = amigoOculto.create(temp);
					temp.setId(novoId);

					System.out.println("Usuário cadastrado com sucesso\n");
					System.out.println("Pressione qualquer tecla para continuar");
					String a = ler.nextLine();
					if (a != null) {
						return;
					}
				} else {
					System.out.println("Cadastro cancelado\n");
					System.out.println("Pressione qualquer tecla para continuar");
					String a = ler.nextLine();
					if (a != null) {
						return;
					}
				}
			} else {
				System.out.println("ERRO! Email já cadastrado");
				System.out.println("Pressione qualquer tecla para continuar");
				String a = ler.nextLine();
				if (a != null) {
					novoUsuario(amigoOculto);
				}
			}
		}
	}
}