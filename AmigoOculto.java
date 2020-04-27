import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Calendar;

public class AmigoOculto {

	//Singleton controlador
	private static final ControladorSingleton controladorPrograma = ControladorSingleton.getInstance();

	public static void main(String[] args) {

		try {

			//bancos de dados
			CRUD<Usuario> usuariosAmigoOculto = new CRUD<>("amigos", Usuario.class.getConstructor());
			CRUD<Sugestao> sugestaoAmigoOculto = new CRUD<>("sugestoes", Sugestao.class.getConstructor());
			CRUD<Grupos> gruposAmigoOculto = new CRUD<>("Grupos", Grupos.class.getConstructor());

			int opcaoEscolhidaMenuLogin;

			//Menu principal, em que os usuarios criam ou logam.
			do {

				opcaoEscolhidaMenuLogin = menuLogin(usuariosAmigoOculto);

				// VERIFICAÇÃO SE O USUÁRIO LOGOU
				if (controladorPrograma.getIdUsuarioAtual() != -1) {
					int opcaoMenuInicial;
					do{
						opcaoMenuInicial=menuInicial();
						switch(opcaoMenuInicial){
							//sugestões
							case 1: {
								int opcaoEscolhidaSugestoes;
								do {

									opcaoEscolhidaSugestoes = menuLogado(sugestaoAmigoOculto);

									//saída do menu logado
								} while (opcaoEscolhidaSugestoes != 0);
								break;
							}
							//grupos
							case 2:{
								int opcaoEscolhidaGrupos;
								do{
									opcaoEscolhidaGrupos=menuLogadoGrupos(gruposAmigoOculto);
								}while(opcaoEscolhidaGrupos!=0);
								break;
							}
							//Usuários
							case 3:{
								break;
							}
							//sair
							case 0:{
								break;
							}
							default: {
								System.out.println("Opção inválida inserida, retornando..");
							}
						}


						//como saímos do menu logado não é necessário mais armazenar o id do usuario atual
						controladorPrograma.finalizarIdUsuarioAtual();
					}while(opcaoMenuInicial!=0);
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
	public static int menuInicial(){
		int opcaoEscolhida=controladorPrograma.escolherOpcaoMenuInicial();
		return opcaoEscolhida;
	}
	public static void criarGrupo(CRUD<Grupos> amigoOculto)throws Exception{
		MyIO.println("Digite o nome do grupo: ");
		String nome=MyIO.readLine();
		if(nome.compareTo("")==0 || nome.compareTo(" ")==0){
			MyIO.println("Nome invalido, retornando ao menu de gerenciamento de grupos");
		}
		else{
			boolean valido=false;
			long dataSorteio=0;
			while(valido==false){
				int dia, mes, ano, hora;
				Calendar atual = Calendar.getInstance();
				Calendar novo= Calendar.getInstance();
				do{
					MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
					String temp=MyIO.readLine();
					String[] aux=temp.split(" ");
					ano=Integer.parseInt(aux[0]);
					mes=Integer.parseInt(aux[1]);
					dia=Integer.parseInt(aux[2]);
					hora=Integer.parseInt(aux[3]);
					if((ano<2020) || (mes<0 || mes>12) || (dia<0 || dia>31) || (hora<0 || hora>23)){
						MyIO.println("Data invalida!!!");
					}
				}while((ano<2020) || (mes<0 || mes>12) || (dia<0 || dia>31) || (hora<0 || hora>23));
				novo.set(ano,mes,dia,hora,0);
				if(novo.compareTo(atual)>0){
					valido=true;
					dataSorteio=novo.getTimeInMillis();
				}
				else{
					MyIO.println("Data antes da atual, invalida!");
				}
			}
			float valor;
			do{
				MyIO.println("Informe o valor médio do presente: ");
				valor=MyIO.readFloat();
			}while(valor<0);
			long dataEncontro=0;
			valido=false;
			while(valido==false){
				int dia, mes, ano, hora;
				Calendar sorteio = Calendar.getInstance();
				sorteio.setTimeInMillis(dataSorteio);
				Calendar novo= Calendar.getInstance();
				do{
					MyIO.println("Digite o ano, o mes, o dia e a hora do encontro respectivamente seperados um espaço(ex: ano mes dia hora) : ");
					String temp=MyIO.readLine();
					String[] aux=temp.split(" ");
					ano=Integer.parseInt(aux[0]);
					mes=Integer.parseInt(aux[1]);
					dia=Integer.parseInt(aux[2]);
					hora=Integer.parseInt(aux[3]);
					if((ano<2020) || (mes<0 || mes>12) || (dia<0 || dia>31) || (hora<0 || hora>23)){
						MyIO.println("Data invalida!!!");
					}
				}while((ano<2020) || (mes<0 || mes>12) || (dia<0 || dia>31) || (hora<0 || hora>23));
				novo.set(ano,mes,dia,hora,0);
				if(novo.compareTo(sorteio)>0){
					valido=true;
					dataEncontro=novo.getTimeInMillis();
				}
				else{
					MyIO.println("Data antes do sorteio, invalida!");
				}
			}
			String local;
			do{
				MyIO.println("Digite o local do encontro: ");
				local=MyIO.readLine();
			}while(local=="");
			String observacoes;
			MyIO.println("Digite obserções adcionais: ");
			observacoes=MyIO.readLine();
			MyIO.println("Digite 1 se deseja confirmar a inscrição: ");
			int confirmar=MyIO.readInt();
			if(confirmar==1){
				int id=-1;
				Grupos temp=new Grupos(-1,controladorPrograma.getIdUsuarioAtual(),nome,local,observacoes,dataSorteio,dataEncontro,valor,false,true);
				try{
					id = amigoOculto.create(temp);

				}catch(Exception e ){
					MyIO.println("Erro");
				}
				amigoOculto.índiceIndiretoIntInt.create(controladorPrograma.getIdUsuarioAtual(),id);
			}
		}
	}
	public static void listarGrupos(CRUD<Grupos> amigoOculto) throws Exception{
		int[] idsGrupos=amigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
		for(int i=0;i<idsGrupos.length;i++){
			Grupos temp=amigoOculto.read(idsGrupos[i]);
			if(temp.getAtivo()){
				MyIO.println(i+". "+temp.getNome());
			}
		}
		pressioneTeclaParaContinuar();
	}
	public static void alterarGrupos(CRUD<Grupos> amigoOculto) throws Exception{
		int[] idsGrupos=amigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
		for(int i=0;i<idsGrupos.length;i++){
			Grupos temp=amigoOculto.read(idsGrupos[i]);
			if(temp.getAtivo()){
				MyIO.print(i+". ");
				temp.mostrar();
			}
		}
		int alteracoes=0;
		MyIO.println("Digite o numero do grupo que deseja alterar ou 0 para sair: ");
		int alterar=MyIO.readInt();
		if(alterar>=0){
			if(alterar<idsGrupos.length){
				Grupos temp=amigoOculto.read(idsGrupos[alterar]);
				temp.mostrar();
				MyIO.println("Digite o novo nome: ");
				String nome=MyIO.readLine();
				if(nome.compareTo("")==0){
					nome=temp.getNome();
				}
				else {
					alteracoes++;
				}
				boolean valido=false;
				long dataSorteio=0;
				while(valido==false){
					int dia,mes,ano,hora;
					dia= mes= ano= hora=0;
					Calendar atual = Calendar.getInstance();
					Calendar novo= Calendar.getInstance();
					MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
					String data=MyIO.readLine();
					if(data.compareTo("")==0){
						valido=true;
						dataSorteio=temp.getMomentoSorteio();
					}
					else{
						do{
							alteracoes++;
							String[] aux=data.split(" ");
							ano=Integer.parseInt(aux[0]);
							mes=Integer.parseInt(aux[1]);
							dia=Integer.parseInt(aux[2]);
							hora=Integer.parseInt(aux[3]);
							if((ano<2020) || (mes<0 || mes>12) || (dia<0 || dia>31) || (hora<0 || hora>23)){
								MyIO.println("Data invalida!!!");
								MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
								data=MyIO.readLine();
							}
						}while((ano<2020) || (mes<0 || mes>12) || (dia<0 || dia>31) || (hora<0 || hora>23));
						novo.set(ano,mes,dia,hora,0);
						if(novo.compareTo(atual)>0){
							valido=true;
							dataSorteio=novo.getTimeInMillis();
						}
						else{
							MyIO.println("Data antes da atual, invalida!");
						}
					}
				}
				MyIO.println("Digite o valor medio dos presentes: ");
				String s=MyIO.readString();
				float valor;
				if(s.compareTo("")==0){
					valor=temp.getValor();
				}
				else{
					alteracoes++;
					valor=Float.parseFloat(s);
				}
				valido=false;
				long dataEncontro=0;
				while(valido==false){
					int dia,mes,ano,hora;
					dia= mes= ano= hora=0;
					Calendar atual = Calendar.getInstance();
					Calendar novo= Calendar.getInstance();
					MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
					String data=MyIO.readLine();
					if(data.compareTo("")==0){
						valido=true;
						dataEncontro=temp.getMomentoSorteio();
					}
					else{
						do{
							alteracoes++;
							String[] aux=data.split(" ");
							ano=Integer.parseInt(aux[0]);
							mes=Integer.parseInt(aux[1]);
							dia=Integer.parseInt(aux[2]);
							hora=Integer.parseInt(aux[3]);
							if((ano<2020) || (mes<0 || mes>12) || (dia<0 || dia>31) || (hora<0 || hora>23)){
								MyIO.println("Data invalida!!!");
								MyIO.println("Digite o ano, o mes, o dia e a hora do sorteio respectivamente seperados um espaço(ex: ano mes dia hora) : ");
								data=MyIO.readLine();
							}
						}while((ano<2020) || (mes<0 || mes>12) || (dia<0 || dia>31) || (hora<0 || hora>23));
						novo.set(ano,mes,dia,hora,0);
						if(novo.compareTo(atual)>0){
							valido=true;
							dataEncontro=novo.getTimeInMillis();
						}
						else{
							MyIO.println("Data antes da atual, invalida!");
						}
					}
				}
				MyIO.println("Digite o local de encontro: ");
				String local=MyIO.readLine();
				if(local.compareTo("")==0){
					local=temp.getLocalEncontro();
				}
				else {
					alteracoes++;
				}
				MyIO.println("Digite observações adcionais");
				String observacoes=MyIO.readLine();
				if(observacoes.compareTo("")==0){
					observacoes=temp.getObservacoes();
				}
				else {
					alteracoes++;
				}
				if(alteracoes==0){
					MyIO.println("Nenhuma alteração foi feita");
				}
				else{
					MyIO.println("Digite 1 se realmente deseja alterar o grupo: ");
					int escolha =MyIO.readInt();
					if(escolha==1){
						Grupos novo=new Grupos(temp.getId(),controladorPrograma.getIdUsuarioAtual(),nome,local,observacoes,dataSorteio,dataEncontro,valor,false,true);
						amigoOculto.update(novo);
						MyIO.println("Usuário alterado com sucesso!");
					}
					else {
						MyIO.println("Nenhuma alteração foi feita");
					}
				}
			}
			else{
				MyIO.println("Número invalido");
			}
		}
	}
	public static void desativarGrupo(CRUD<Grupos> amigoOculto) throws Exception{
		int[] idsGrupos=amigoOculto.índiceIndiretoIntInt.read(controladorPrograma.getIdUsuarioAtual());
		for(int i=0;i<idsGrupos.length;i++){
			Grupos temp=amigoOculto.read(idsGrupos[i]);
			if(temp.getAtivo()){
				MyIO.print(i+". ");
				temp.mostrar();
			}
		}
		MyIO.println("Digite o numero do grupo que deseja desativar");
		int i=MyIO.readInt();
		if(i>=0 && i<idsGrupos.length){
			Grupos desativar=amigoOculto.read(idsGrupos[i]);
			desativar.mostrar();
			MyIO.println("Digite 1 se deseja confirmar a desativação: ");
			int escolha=MyIO.readInt();
			if(escolha==1){
				desativar.setAtivo(false);
				amigoOculto.update(desativar);
				MyIO.println("Desativação efetuada com sucesso");
			}
		}
	}
	public static int escolherOpcaoGrupos(CRUD<Grupos> amigoOculto) throws Exception{
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
	public static int gerenciamentoGrupos(CRUD<Grupos> amigoOculto)throws Exception{
		int opcao;
		try {
			opcao = controladorPrograma.gerenciamentoGrupos();
			switch (opcao) {
				//gerenciamento de grupos
				case 1: {
					int opcaoGrupos;
					do{
						opcaoGrupos=escolherOpcaoGrupos(amigoOculto);
					}while(opcaoGrupos!=0);
					break;
				}
				//convites
				case 2: {
					
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
	public static int menuLogadoGrupos(CRUD<Grupos> amigoOculto) throws Exception{
		int opcao;
		try {
			opcao = controladorPrograma.Grupos();
			switch (opcao) {
				//Criação e gerenciamento de grupos
				case 1: {
					gerenciamentoGrupos(amigoOculto);
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
	public static int menuLogado(CRUD<Sugestao> amigoOculto) {
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

	// TODO: Resolver problema com a listagem e nulls.
	public static void listarSugestoes(CRUD<Sugestao> amigoOculto) {
		//terei que procurar todas as sugestoes e, se elas baterem com o id, listar
		int idUser = controladorPrograma.getIdUsuarioAtual();
		try {
			System.out.println("MINHAS SUGESTÕES");

			//metodo retorna um int[] com todas os ids batendo a c1
			int[] idsSugestoes = amigoOculto.índiceIndiretoIntInt.read(idUser);
			int contador = 0;

			System.out.println("Tamanho do array de ids encontradas: " + idsSugestoes.length);

			System.out.print("ID das sugestões: ");
			for (int j = 0; j < idsSugestoes.length; j++) {
				System.out.print(j + "\t");
			}
			System.out.println();

			//loop que vai procurar os n elementos retornados dentro do banco e printar os valores
			for (int i = 0; i < idsSugestoes.length; i++, contador++) {
				Sugestao temp = amigoOculto.read(i);

				if (temp != null) {
					System.out.println(i + ". " + temp.getProduto());
					System.out.println(temp.getLoja());
					System.out.printf("R$ %.2f\n", temp.getValor());
					System.out.println(temp.getObservacoes());
				} else {
					System.out.println("Temp é null para i = " + i);
					contador--;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

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

			Sugestao temp2 = amigoOculto.read(idSug);

			//System.out.printf("Produto do temp1: %s\nproduto do temp2: %s\n", temp.getProduto(), temp2.getProduto());

			//adicionar a arvore B int int
			amigoOculto.índiceIndiretoIntInt.create(temp.getIdUsuario(), temp.getId());
			System.out.printf("Acabei de adicionar a sugestão de [%s], com idSug [%d] e idUser [%d] na árvore..\n", temp.getProduto(), temp.getId(), temp.getIdUsuario());
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
