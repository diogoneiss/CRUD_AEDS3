/**
 * Essa classe serve como controladora das funções do projeto e
 * segue o design pattern de um Singleton, isto é, uma classe de uma instância só.
 */
public class ControladorSingleton {

	private static ControladorSingleton controlador;
	public static int idUsuarioAtual = -1;

	private ControladorSingleton() {
		//construtor privado, já que ela é iniciada estáticamente
	}

	public static synchronized ControladorSingleton getInstance() {
		if (controlador == null) {
			controlador = new ControladorSingleton();
		}
		return controlador;
	}

	public static void salvarIdUsuarioAtual(int id) {
		idUsuarioAtual = id;
	}

	//Para evitar confusao ao finalizar as operacoes com um usuario deve-se resetar o id global
	public static void finalizarIdUsuarioAtual() {
		idUsuarioAtual = -1;
	}

	public int escolherOpcaoMenuInicial() {

		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("INÍCIO\n");
		System.out.println("Escolha a opção que deseja dentre as abaixo: ");
		System.out.println("1) Sugestões de presentes");
		System.out.println("2) Grupos");
		System.out.println("3) Usuários");
		System.out.println("Novos convites: 0");

		System.out.println("\n0) Sair");
		System.out.print("\nOpção: ");

		return MyIO.readInt();
	}

	public int escolherOpcaoSugestao() {


		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("INÍCIO > SUGESTÕES\n");

		System.out.println("Escolha a opção que deseja dentre as abaixo: ");

		System.out.println("1) Listar");
		System.out.println("2) Incluir");
		System.out.println("3) Alterar");
		System.out.println("4) Excluir");


		System.out.println("\n0) Retornar ao menu anterior");
		System.out.print("\nOpção: ");


		return MyIO.readInt();
	}

	public int escolherOpcaoUsuarios() {


		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("LOGIN USUÁRIOS\n");

		System.out.println("Escolha a opção que deseja dentre as abaixo: ");
		System.out.println("\n1) Acesso ao sistema");
		System.out.println("2) Criar novo usuário");


		System.out.println("\n0) Sair do programa");
		System.out.print("\nOpção: ");


		return MyIO.readInt();
	}
}
