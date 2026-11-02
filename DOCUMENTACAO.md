# Documentação do processo, Trabalho 4 (parte 4 de 4 da A2, Arquitetura MVVM)

Este arquivo registra o Trabalho 4. O registro do Trabalho 3 (Room) continua mais abaixo, na seção "Trabalho 3".

## Como estava o projeto no Trabalho 3 e o que mudou na reorganização

No Trabalho 3 o Room já funcionava, mas as telas faziam tudo sozinhas: cada `@Composable` pegava o `AppDatabase`, chamava o `@Dao`, abria `CoroutineScope(Dispatchers.IO).launch` e guardava os dados em várias variáveis `remember`. Depois de cada inserir, atualizar ou deletar a tela tinha uma função `carregar()` para buscar a lista de novo.

No Trabalho 4 nenhuma tela, campo ou funcionalidade foi criada. O app faz exatamente o que fazia antes (adicionar, listar, atualizar, remover e persistir), só que dividido em camadas:

- **Model**: as `@Entity` `Tarefa` e `Categoria`, os `@Dao` e o `AppDatabase` (pacote `data`).
- **Repository**: um por Entity, é o único que fala com o DAO (pacote `data.repository`).
- **ViewModel**: guarda o estado da tela e chama o Repository (pacote `viewmodel`).
- **View**: as telas, que só mostram o `uiState` e chamam métodos do ViewModel (pacote `ui`).

O que mudou, na prática:

- Os `@Dao` agora devolvem `Flow<List<...>>` no `buscarTodos()`. O Room avisa sozinho quando a tabela muda, então a função `carregar()` das telas deixou de existir.
- `buscarPorId` e `buscarPorCategoria` saíram dos DAOs. As telas de detalhe agora procuram o item na lista que já vem no `uiState`, e o Detalhe da Categoria filtra as tarefas dela a partir dessa mesma lista.
- As telas não têm mais `dao`, `CoroutineScope` nem `Dispatchers.IO`. As chamadas com `try/catch` e `Log.e` foram para os ViewModels.
- As funções locais (`suspend fun carregar()` dentro do `@Composable`) foram removidas. O que elas faziam agora é feito pelo `init` do ViewModel.
- Os `@Preview` das telas que usam o banco foram removidos, porque um preview não tem Room para ler.

## Como dividi os pacotes

Dentro de `app/src/main/java/com/exemplo/tarefasapp/`:

| Pacote | O que tem | Por que ficou aqui |
| --- | --- | --- |
| `ui` | As telas (`HomeScreen`, `ListaTarefasScreen`, `ListaCategoriasScreen`, `DetalheTarefaScreen`, `DetalheCategoriaScreen`, `PerfilScreen`, `SobreScreen`), mais `ui.componentes` (`BottomBar`), `ui.navegacao` (`AppNavigation`, `Rotas`) e `ui.theme` | Tudo que desenha ou navega entre telas. Nenhum desses arquivos tem lógica de banco |
| `data` | `Tarefa`, `Categoria`, `TarefaDao`, `CategoriaDao`, `AppDatabase` | Exatamente os arquivos do Trabalho 3, só movidos (antes o pacote chamava `dados`) |
| `data.repository` | `TarefaRepository`, `CategoriaRepository` | Novos. Ficam entre o DAO e o ViewModel |
| `viewmodel` | `TarefasViewModel`, `CategoriasViewModel`, `TarefasUiState`, `CategoriasUiState`, `TarefasViewModelFactory`, `CategoriasViewModelFactory` | Novos. O estado da tela e as ações |

Os pacotes `telas`, `componentes` e `navegacao` foram juntados dentro de `ui`, porque eram todos "interface".

## Por que o Repository existe

Antes, quem chamava o `@Dao` era a própria tela, então a tela dependia do Room. Agora a tela só conhece o ViewModel, o ViewModel só conhece o Repository e só o Repository conhece o DAO. Cada Repository só repassa as chamadas (`buscarTodos`, `inserir`, `atualizar`, `deletar`) para o DAO, sem lógica nova.

O que isso resolve: se um dia os dados vierem de outro lugar além do Room, por exemplo uma API, só o Repository muda. O ViewModel e as telas continuam iguais.

O banco e os dois Repositories são criados uma vez só na `MainActivity` e entregues para a navegação, que repassa para cada tela.

## O que tem dentro de cada UiState e por que

**`TarefasUiState`** (usado em Início, Minhas Tarefas e Detalhe da Tarefa):

- `tarefas`: a lista de tarefas, para as telas desenharem.
- `categorias`: a lista de categorias, porque cada tarefa mostra a cor e o nome da categoria dela, e o formulário de nova tarefa e a troca de categoria precisam das opções.
- `carregando`: começa `true` e vira `false` quando o Room entrega a primeira lista. Serve para o Detalhe não mostrar "não existe mais" antes de os dados chegarem.
- `tarefasPendentes`: calculado a partir de `tarefas`, é o número que aparece no Início.

**`CategoriasUiState`** (usado em Minhas Categorias e Detalhe da Categoria):

- `categorias`: a lista de categorias.
- `tarefas`: para contar quantas tarefas cada categoria tem e quantas estão concluídas.
- `carregando`: mesmo motivo do outro.

Os campos de texto dos formulários (título, descrição, nome) continuaram como estado local da tela, porque só importam enquanto a pessoa está digitando e só vão para o ViewModel quando ela clica em salvar ou adicionar.

Cada ViewModel segue o padrão pedido: `private val _uiState = MutableStateFlow(...)` e `val uiState: StateFlow<...> = _uiState.asStateFlow()`. As telas coletam com `collectAsStateWithLifecycle()`.

## ViewModelFactory

O Android não sabe criar um ViewModel que recebe parâmetro no construtor, então cada ViewModel tem a sua `ViewModelProvider.Factory` (`TarefasViewModelFactory` e `CategoriasViewModelFactory`), que entrega os Repositories certos. Nas telas o ViewModel é obtido com `viewModel(factory = ...)`.

## Dificuldades e o que quebrou na reorganização

Depois de reorganizar tudo o projeto compilava normalmente, mas o app fechava logo ao abrir, antes de mostrar qualquer tela. Ou seja, o que quebrou foi o app inteiro, não uma funcionalidade só.

- **Como encontrei:** como o Gradle não acusava erro, rodei o app no emulador e li o `logcat` filtrando por `AndroidRuntime`. O erro era `IllegalStateException: CompositionLocal LocalLifecycleOwner not present`, vindo do `collectAsStateWithLifecycle()` na `HomeScreen`.
- **Causa:** eu tinha colocado as bibliotecas `lifecycle-viewmodel-compose` e `lifecycle-runtime-compose` na versão 2.8.0. Essa versão espera um `LocalLifecycleOwner` que só as versões mais novas do Compose fornecem, e o projeto usa o Compose BOM de 2024.05. Como o erro só acontece com o app rodando, a compilação passava.
- **Como corrigi:** troquei as duas dependências para a versão 2.7.0 no `app/build.gradle.kts`, que é compatível com o Compose do projeto. Depois disso o app voltou a abrir e a funcionar como no Trabalho 3.

Pontos que mudaram de comportamento por causa da arquitetura, para conferir no teste:

- Os campos de edição das telas de detalhe começam com os valores da tarefa/categoria e só são reiniciados quando o item muda.
- As telas de detalhe não buscam mais pelo id no banco, elas procuram na lista do `uiState`.

## Prints e vídeo do app funcionando

Vídeo do app rodando com a arquitetura MVVM, mostrando que tudo continua funcionando como no Trabalho 3 (adicionar, listar, atualizar, remover e persistir): https://www.loom.com/share/fda559960eec4a7e9aaab56e84e1eab1

## Dificuldades

Fiz este trabalho sozinho. A principal dificuldade foi o app não abrir depois da reorganização, por causa da versão das bibliotecas de lifecycle. O problema e a solução estão descritos na seção "Dificuldades e o que quebrou na reorganização", acima.

---

# Trabalho 3 (parte 3 de 4 da A2, Room)

### Como estava o projeto no Trabalho 2 e o que mudou

No Trabalho 2 o app já tinha as 7 telas, o NavHost, a barra inferior e as duas listas (tarefas e categorias) com adicionar e remover. O problema era que tudo ficava num objeto `AppDados`, com `mutableStateListOf`, então ao fechar o app os dados voltavam para os de exemplo.

Para o Trabalho 3 as telas e o fluxo de navegação continuaram os mesmos. O que mudou foi de onde vêm os dados:

- O objeto `AppDados` foi removido.
- As data classes `Tarefa` e `Categoria` viraram `@Entity`.
- Foram criados dois DAOs e a classe `AppDatabase`.
- Cada tela passou a buscar no banco quando abre, e cada ação (adicionar, concluir, editar, remover) grava no banco.
- Como o enunciado pede atualizar de verdade, os dois detalhes ganharam um pequeno formulário de edição (título e descrição na tarefa, nome na categoria).

### Entities, DAOs e Database

**Entities**

- `Tarefa` (tabela `tarefas`): `id`, `titulo`, `descricao`, `categoriaId`, `concluida`, `prazoDias`. São os mesmos campos do Trabalho 2, porque as telas já usavam todos eles.
- `Categoria` (tabela `categorias`): `id`, `nome`, `cor`. A cor era um `Color` do Compose, que o Room não sabe salvar, então passou a ser um `Long` (valor ARGB, como `0xFF6750A4`). Nas telas converto de volta com `Color(categoria.cor)`.
- Os dois `id` são `Int` com `@PrimaryKey(autoGenerate = true)` e valor padrão `0`, então o Room gera o id sozinho na inserção (não precisa mais do `proximoIdTarefa()` do Trabalho 2).

**DAOs**

Cada DAO tem os 4 métodos básicos (`inserir`, `buscarTodos`, `atualizar`, `deletar`), todos `suspend`.

**Database**

`AppDatabase` reúne as duas entities (`version = 1`) e expõe os dois DAOs. Usei o padrão Singleton visto em aula (`companion object` com `@Volatile` e `synchronized`). Também adicionei um `Callback` com `onCreate`, que roda só na primeira vez que o banco é criado e insere as categorias e tarefas de exemplo, para o app não abrir vazio.

### Decisões sobre as queries do DAO

Além dos 4 métodos básicos, acrescentei:

- `buscarPorId(id)` nos dois DAOs: usado nas telas de detalhe, que recebem só o id pela navegação e buscam o item direto no banco (em vez de receber o objeto pronto).
- `TarefaDao.buscarPorCategoria(categoriaId)`: usado no Detalhe da Categoria, que antes filtrava a lista em memória. Agora o filtro é feito pelo banco.

`buscarPorId` devolve `Tarefa?` / `Categoria?`, porque o item pode não existir mais (por exemplo, se foi removido). As telas de detalhe mostram "não existe mais" nesse caso.

### Dificuldades ao migrar do mutableStateListOf para o Room

- `Color` não é salvável no Room. Resolvi guardando a cor como `Long` e convertendo na hora de desenhar.
- No Trabalho 2 a lista era um estado do Compose e a tela atualizava sozinha ao adicionar ou remover. Com o Room, as funções do DAO só devolvem uma lista pronta, então depois de cada inserir, atualizar ou deletar eu chamo a busca de novo para atualizar o estado da tela.
- O enunciado pede que as chamadas ao banco não travem a interface, então as escritas (inserir, atualizar, deletar) ficam dentro de `CoroutineScope(Dispatchers.IO).launch`, e a busca inicial fica no `LaunchedEffect(Unit)`.
- Os ids deixaram de ser calculados por mim. Os dados de exemplo inseridos no `onCreate` usam ids 1, 2 e 3 para as categorias, e as tarefas apontam para esses ids.
- Remover uma categoria não remove as tarefas dela do banco (não coloquei chave estrangeira, porque o enunciado não pede). Essas tarefas continuam na lista de tarefas, só sem categoria para mostrar.
- O KSP precisa ser compatível com a versão do Kotlin (1.9.24), por isso o plugin está na versão `1.9.24-1.0.20`, declarado antes da dependência `room-compiler`.

### Prints e vídeo do app funcionando

Vídeo do app rodando, mostrando o teste de persistência (adicionar um item, fechar o app, reabrir e o item continuar lá): https://www.loom.com/share/1e7146f5baf047f8b3a052611f819053

### Dificuldades

Fiz este trabalho sozinho. As dificuldades da migração para o Room estão descritas na seção "Dificuldades ao migrar do mutableStateListOf para o Room", acima.
