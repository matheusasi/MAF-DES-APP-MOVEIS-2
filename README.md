# Tarefas App

App de organização de tarefas por categoria, feito para o Trabalho 4 (parte 4 de 4 da A2, Arquitetura MVVM) da matéria Desenvolvimento de Aplicativos Móveis. Ele é a continuação do Trabalho 3 (Room), que por sua vez continuou o Trabalho 2 (repositório anterior: https://github.com/matheusasi/MAF-DES-APP-MOVEIS). Neste trabalho nenhuma funcionalidade nova foi criada: o código foi reorganizado em camadas (Model, Repository, ViewModel e View).

A ideia é simples: você cadastra categorias (Faculdade, Casa, Pessoal, etc) e cria tarefas dentro de cada categoria, marcando como concluídas, editando ou removendo quando quiser. O app mostra o progresso de cada categoria e quanto falta para o prazo de cada tarefa. Tudo fica guardado no banco, então os dados continuam lá depois de fechar e abrir o app.

## Telas do app

- Início: resumo com quantas tarefas estão pendentes e quantas categorias existem.
- Minhas Tarefas: lista de tarefas, com formulário para adicionar, checkbox para concluir e botão para remover.
- Detalhe da Tarefa: mostra a categoria e o prazo, permite trocar a categoria e editar título e descrição.
- Minhas Categorias: lista de categorias, com formulário para adicionar (nome e cor) e botão para remover.
- Detalhe da Categoria: mostra as tarefas dela, a contagem de concluídas e permite editar o nome.
- Perfil: formulário para editar o nome do usuário.
- Sobre o App: informações gerais sobre o projeto.

## Arquitetura MVVM

O código fica em `app/src/main/java/com/exemplo/tarefasapp/`:

- `ui`: as telas (View). Só leem o `uiState` e chamam métodos do ViewModel. Dentro dele ficam também `ui/componentes` (barra inferior), `ui/navegacao` (rotas e NavHost) e `ui/theme`.
- `data`: Model. As `@Entity` (`Tarefa`, `Categoria`), os `@Dao` (`TarefaDao`, `CategoriaDao`) e o `@Database` (`AppDatabase`). O `buscarTodos()` dos DAOs devolve `Flow<List<...>>`, então o Room atualiza a lista sozinho quando a tabela muda.
- `data/repository`: `TarefaRepository` e `CategoriaRepository`. São a única fonte de dados e os únicos que falam com o DAO.
- `viewmodel`: `TarefasViewModel` e `CategoriasViewModel`, cada um com seu `UiState` (`_uiState` privado e `uiState` público como `StateFlow`) e sua `ViewModelFactory`, que entrega os Repositories.

O fluxo é: tela, ViewModel, Repository, DAO. O banco e os Repositories são criados na `MainActivity` e repassados pela navegação para as telas.

## Como rodar o projeto

1. Abrir a pasta do projeto no Android Studio (versão recente, com Jetpack Compose suportado).
2. Esperar o Gradle sincronizar (ele baixa Compose, Navigation, Lifecycle, Room e o plugin KSP automaticamente).
3. Rodar em um emulador ou aparelho físico com Android 7.0 (API 24) ou superior.

Se preferir rodar pelo terminal:

```
./gradlew assembleDebug
```

O APK gerado fica em `app/build/outputs/apk/debug/`.

## Como testar a persistência

Adicione uma tarefa, feche o app completamente (tire ele dos apps recentes), abra de novo e confira que a tarefa continua na lista.

Vídeo do app funcionando com a arquitetura MVVM (Trabalho 4): https://www.loom.com/share/fda559960eec4a7e9aaab56e84e1eab1

Vídeo do teste de persistência do Trabalho 3: https://www.loom.com/share/1e7146f5baf047f8b3a052611f819053

## Documentação do processo

O arquivo `DOCUMENTACAO.md`, na raiz do repositório, tem o registro das decisões do Trabalho 4 (pacotes, Repository, UiState, Factory) e, mais abaixo, o do Trabalho 3.
