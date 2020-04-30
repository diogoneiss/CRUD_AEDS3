/**
 * Essa classe serve como controladora das funções do projeto e
 * segue o design pattern de um Singleton, isto é, uma classe de uma instância só.
 */
public class ControladorSingleton {

	private static ControladorSingleton controlador;
	private int idUsuarioAtual = -1;

	private ControladorSingleton() {
		//construtor privado, já que ela é iniciada estáticamente
	}

	public static synchronized ControladorSingleton getInstance() {
		if (controlador == null) {
			controlador = new ControladorSingleton();
		}
		return controlador;
	}
	public int getIdUsuarioAtual(){
		return this.idUsuarioAtual;
	}

	public  void salvarIdUsuarioAtual(int id) {
		idUsuarioAtual = id;
	}

	//Para evitar confusao ao finalizar as operacoes com um usuario deve-se resetar o id global
	public  void finalizarIdUsuarioAtual() {
		idUsuarioAtual = -1;
	}

	public int escolherOpcaoMenuInicial(Usuario userLogado) {

		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("INÍCIO\n");
		System.out.println("Escolha a opção que deseja dentre as abaixo: ");
		System.out.println("1) Sugestões de presentes");
		System.out.println("2) Grupos");

		try {
			System.out.println("3) Novos convites: "+ Inscricao.getTotalConvites(userLogado));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\n0) Sair");
		System.out.print("\nOpção: ");

		return MyIO.readInt();
	}

	public int Grupos(){

		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("INÍCIO > GRUPOS \n");
		System.out.println("Escolha a opção que deseja dentre as abaixo: ");
		System.out.println("1) Criação e gerenciamento de grupos");
		System.out.println("2) Participação nos grupos");
		System.out.println("\n0) Retornar ao menu principal");
		System.out.print("\nOpção: ");

		return MyIO.readInt();
	}
	public int gerenciamentoGrupos(){

		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("INÍCIO > GRUPOS > GERENCIAMENTO DE GRUPOS \n");
		System.out.println("Escolha a opção que deseja dentre as abaixo: ");
		System.out.println("1) Grupos");
		System.out.println("2) Convites");
		System.out.println("3) Participantes");
		System.out.println("4) Sorteio");
		System.out.println("\n0) Retornar ao menu de grupos");
		System.out.print("\nOpção: ");

		return MyIO.readInt();
	}

	public int escolherOpcaoGrupos() {


		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("INÍCIO > INÍCIO > GRUPOS > GERENCIAMENTO DE GRUPOS > Grupos \n");

		System.out.println("Escolha a opção que deseja dentre as abaixo: ");

		System.out.println("1) Listar");
		System.out.println("2) Incluir");
		System.out.println("3) Alterar");
		System.out.println("4) Desativar");


		System.out.println("\n0) Retornar ao menu de grupos");
		System.out.print("\nOpção: ");


		return MyIO.readInt();
	}

	public int convites(){

		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("INÍCIO > GRUPOS > GERENCIAMENTO DE GRUPOS > CONVITES\n");
		System.out.println("Escolha a opção que deseja dentre as abaixo: ");
		System.out.println("1) Listagem de convites");
		System.out.println("2) Emissão de convites");
		System.out.println("3) Cancelamento de convites");

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

	public void menuSugestoesAlteracao() {


		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("INÍCIO > SUGESTÕES > ALTERAÇÃO\n");
		System.out.println("\nPrintando na tela as sugestões registradas ao seu usuário.\n");


	}

	public int escolherOpcaoAlteracaoSugestao() {


		System.out.println("Amigo Oculto 1.0");
		System.out.println("================\n");
		System.out.println("INÍCIO > SUGESTÕES > ALTERAÇÃO\n");

		System.out.println("Escolha qual opção na lista acima você deseja alterar ou\ndigite [0] para não alterar nada e retornar");

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
