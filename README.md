# CRUD AEDsIII
Projeto de um sistema para  Amigo Oculto, utilizando estruturas de dados aprendidas em AEDs III

# Como está estruturado?

### Classe genérica *CRUD*:
É o banco de dados em si. Ele recebe um template de classe, que obrigatoriamente deve implementar a interface ````Entidade````, e contêm estruturas relevantes para os códigos, 
além dos métodos ````create()````, ````read()````, ````read()````, ````delete()````.
### Classe *AmigoOculto*
Aqui contêm os métodos principais do programa, além dos métodos que trabalham sobre os dados. Para auxiliar na criação de menus e requisições de input, além do armazenamento de variáveis globais,
criei uma classe ````ControladorSingleton````, que basicamente só contêm esse tipo de método, que são chamados a partir de uma única instância da classe.

A main é dividia em seções, com indicações do tipo de Classe base que tal seção trabalha com. Futuramente farei um índice de métodos.

### Classes modeladoras de tipos
* ````Convites````: Convites para participar do amigo oculto
* ````Grupos````: Grupos de pessoas que participam de um mesmo encontro e sorteio
* ````Inscrição````: Cadastro de usuário a partir de um convite, é mais uma classe auxiliar que realmente um tipo de dado.
* ````Sugestão````: Sugestões de presentes para cada usuário
* ````Usuário````: Cada usuário do sistema 

### Classes de tipos de dados
* ````ArvoreBMais_Chave_Composta_String_Int````: Lista invertida, implementada para uso dos convites.
* ````ArvoreBMais_Int_Int````: Árvore usada para associar ids de usuário e ids de outro objeto, a fim de facilitar o retorno de todos os itens associados a um usuário
* ````ArvoreBMais_String_Int````: Árvore B+ que associa chave secundária, em string, e chave primária, em int. 
* ````HashExtensível````: HashExtensível usada para buscas individuais, através de funções de hash. Muito boa para índices indiretos.

### Classes de IO
A classe ````MYIO```` é utilizada para muitas operações de input/output, já que lida com erros de entrada, buffer e é menos verbosa de utilizar.

### Ambiente de testes
A classe ````AmigoOcultoTeste```` serve exclusivamente para testar implementação de novas funcionalidades, já que não possui menu nem nada. São implementados apenas os métodos necessários e a criação de instâncias
é *hard coded*, acelerando o desenvolvimento, teste e debugging.



# Responsáveis pelas partes finais da entrega e listagem de bugs conhecidos

## Parte 4 - Sugestão
Diogo

Bugs?
* Problema na listagem, estou trabalhando nisso

## Parte 5 - Grupos de amigos
Henrique

https://pucminas.instructure.com/courses/9019/pages/amigo-oculto-5-grupos

Bugs? 

## Parte 6 - Convites
Guilherme

https://pucminas.instructure.com/courses/9019/pages/amigo-oculto-6-convites

Bugs? 

## Parte 7 - Inscrição
Lorenzo

https://pucminas.instructure.com/courses/9019/pages/amigo-oculto-7-inscricao

Bugs?
