# Documentação do processo, Trabalho 3 (parte 3 de 4 da A2, Room)

## Como estava o projeto no Trabalho 2 e o que mudou

No Trabalho 2 o app já tinha as 7 telas, o NavHost, a barra inferior e as duas listas (tarefas e categorias) com adicionar e remover. O problema era que tudo ficava num objeto `AppDados`, com `mutableStateListOf`, então ao fechar o app os dados voltavam para os de exemplo.

Para o Trabalho 3 as telas e o fluxo de navegação continuaram os mesmos. O que mudou foi de onde vêm os dados:

- O objeto `AppDados` foi removido.
- As data classes `Tarefa` e `Categoria` viraram `@Entity`.
- Foram criados dois DAOs e a classe `AppDatabase`.
- Cada tela passou a buscar no banco quando abre, e cada ação (adicionar, concluir, editar, remover) grava no banco.
- Como o enunciado pede atualizar de verdade, os dois detalhes ganharam um pequeno formulário de edição (título e descrição na tarefa, nome na categoria).

## Entities, DAOs e Database

**Entities**

- `Tarefa` (tabela `tarefas`): `id`, `titulo`, `descricao`, `categoriaId`, `concluida`, `prazoDias`. São os mesmos campos do Trabalho 2, porque as telas já usavam todos eles.
- `Categoria` (tabela `categorias`): `id`, `nome`, `cor`. A cor era um `Color` do Compose, que o Room não sabe salvar, então passou a ser um `Long` (valor ARGB, como `0xFF6750A4`). Nas telas converto de volta com `Color(categoria.cor)`.
- Os dois `id` são `Int` com `@PrimaryKey(autoGenerate = true)` e valor padrão `0`, então o Room gera o id sozinho na inserção (não precisa mais do `proximoIdTarefa()` do Trabalho 2).

**DAOs**

Cada DAO tem os 4 métodos básicos (`inserir`, `buscarTodos`, `atualizar`, `deletar`), todos `suspend`.

**Database**

`AppDatabase` reúne as duas entities (`version = 1`) e expõe os dois DAOs. Usei o padrão Singleton visto em aula (`companion object` com `@Volatile` e `synchronized`). Também adicionei um `Callback` com `onCreate`, que roda só na primeira vez que o banco é criado e insere as categorias e tarefas de exemplo, para o app não abrir vazio.

## Decisões sobre as queries do DAO

Além dos 4 métodos básicos, acrescentei:

- `buscarPorId(id)` nos dois DAOs: usado nas telas de detalhe, que recebem só o id pela navegação e buscam o item direto no banco (em vez de receber o objeto pronto).
- `TarefaDao.buscarPorCategoria(categoriaId)`: usado no Detalhe da Categoria, que antes filtrava a lista em memória. Agora o filtro é feito pelo banco.

`buscarPorId` devolve `Tarefa?` / `Categoria?`, porque o item pode não existir mais (por exemplo, se foi removido). As telas de detalhe mostram "não existe mais" nesse caso.

## Dificuldades ao migrar do mutableStateListOf para o Room

- `Color` não é salvável no Room. Resolvi guardando a cor como `Long` e convertendo na hora de desenhar.
- No Trabalho 2 a lista era um estado do Compose e a tela atualizava sozinha ao adicionar ou remover. Com o Room, as funções do DAO só devolvem uma lista pronta, então depois de cada inserir, atualizar ou deletar eu chamo a busca de novo para atualizar o estado da tela.
- O enunciado pede que as chamadas ao banco não travem a interface, então as escritas (inserir, atualizar, deletar) ficam dentro de `CoroutineScope(Dispatchers.IO).launch`, e a busca inicial fica no `LaunchedEffect(Unit)`.
- Os ids deixaram de ser calculados por mim. Os dados de exemplo inseridos no `onCreate` usam ids 1, 2 e 3 para as categorias, e as tarefas apontam para esses ids.
- Remover uma categoria não remove as tarefas dela do banco (não coloquei chave estrangeira, porque o enunciado não pede). Essas tarefas continuam na lista de tarefas, só sem categoria para mostrar.
- O KSP precisa ser compatível com a versão do Kotlin (1.9.24), por isso o plugin está na versão `1.9.24-1.0.20`, declarado antes da dependência `room-compiler`.

## Prints e vídeo do app funcionando

Vídeo do app rodando, mostrando o teste de persistência (adicionar um item, fechar o app, reabrir e o item continuar lá): https://www.loom.com/share/1e7146f5baf047f8b3a052611f819053

## Dificuldades do trio

(Preencher com as dificuldades reais de cada integrante e como foram resolvidas.)
