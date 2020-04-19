import java.util.Scanner;

/**
 * Essa classe serve como controladora das funções do projeto e
 * segue o design pattern de um Singleton, isto é, uma classe de uma instância só.
 */
public class ControladorSingleton {
	public static Scanner in = new Scanner(System.in);
	private static ControladorSingleton controlador;

	private ControladorSingleton() {
		//construtor privado, já que ela é iniciada estáticamente
	}

	public static synchronized ControladorSingleton getInstance() {
		if (controlador == null) {
			controlador = new ControladorSingleton();
		}
		return controlador;
	}

	public int escolherOpcao() {

		System.out.println("Escolha a opção que deseja dentre as abaixo: ");
		System.out.println("Amigo Oculto");
		System.out.println("================");
		System.out.println("1) Acesso ao sistema");
		System.out.println("2) Novo usuário");
		System.out.println("\n0) Sair");
		System.out.print("\nOpção: ");
		int op = in.nextInt();

		//ler \n
		in.next();

		return op;
	}
}
