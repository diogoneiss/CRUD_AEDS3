import java.util.Scanner;

public class AmigoOculto {

    //Um objeto do tipo "Scanner" é um atributo da classe pois ele é utilizado em todos os métodos
    public static Scanner leitor = new Scanner(System.in);

    public static void main(String args[]) {
        
        try {
            CRUD amigoOculto = new CRUD("amigos");
            int entrada;

            do {
                System.out.println("\nAMIGO OCULTO 1.0");
                System.out.println("================");
                System.out.println("\nACESSO");
                System.out.println("\n1) Acesso ao Sistema");
                System.out.println("2) Novo Usuário (Primeiro Acesso)");
                System.out.println("\n0) Sair");
                System.out.print("\nOpção: ");

                entrada = leitor.nextInt();

                leitor.nextLine();

                if (entrada == 1) {
                    acesso(amigoOculto);
                } else if (entrada == 2) {
                    novoUsuario(amigoOculto);
                }
            } while (entrada != 0);
            
            leitor.close();
        } catch (Exception e) {
            System.out.println("\nErro!");
            e.printStackTrace();
        }
    }

    public static void acesso(CRUD amigoOculto) throws Exception{
        
        System.out.println("\nACESSO AO SISTEMA");
        System.out.print("\nEmail: ");
        
        String email = leitor.nextLine();
        
        if (amigoOculto.read(email) == null){
            System.out.println("\nUsuário não cadastrado!");
        } else {
            System.out.print("Senha: ");
            
            String senha = leitor.nextLine();
            Usuario temp = amigoOculto.read(email);
        
            if (temp.getSenha().compareTo(senha) == 0) {
                System.out.println("\nSenha correta!");
                System.out.println("\nPressione a tecla \"ENTER\" para continuar: ");
                
                String str = leitor.nextLine();
                
                if (str != null) {
                    return;
                }
            } else {
                System.out.println("\nSenha incorreta!");
                System.out.println("\nPressione a tecla \"ENTER\" para continuar: ");
                
                String str = leitor.nextLine();
                
                if (str != null) {
                    acesso(amigoOculto);
                }
            }
        }
    }

    public static void novoUsuario(CRUD amigoOculto) throws Exception {
        
        System.out.println("\nNOVO USUÁRIO");
        System.out.print("\nEmail: ");
       
        String email = leitor.nextLine();
        
        if (email == "") {
            System.out.println("\nEmail inválido!");
        } else {
            if (amigoOculto.read(email) == null) {
                System.out.print("Nome: ");
                
                String nome = leitor.nextLine();
                
                System.out.print("Senha: ");
                
                String senha = leitor.nextLine();
                
                System.out.println("\nEmail: " + email + "\nNome: " + nome + "\nSenha: " + senha);
                System.out.print("\nDigite [1] caso queira confirmar o cadastro: ");
                
                int confirmacaoCadastro = leitor.nextInt();
                
                leitor.nextLine();
                
                if (confirmacaoCadastro == 1) {
                    amigoOculto.create(nome, email, senha);
                    
                    System.out.println("\nUsuário cadastrado com sucesso!");
                    System.out.println("\nPressione a tecla \"ENTER\" para continuar: ");
                
                    String str = leitor.nextLine();
                    
                    if (str != null) {
                        return;
                    }
                } else {
                    System.out.println("\nCadastro cancelado!\n");
                    System.out.println("Pressione a tecla \"ENTER\" para continuar: ");
                
                    String str = leitor.nextLine();
                
                    if (str != null) {
                        return;
                    }
                }
            } else {
                System.out.println("\nEmail já cadastrado no sistema!");
                System.out.println("\nPressione a tecla \"ENTER\" para continuar: ");
                
                String str = leitor.nextLine();
                
                if (str != null) {
                    novoUsuario(amigoOculto);
                }
            }
        }
    }
}