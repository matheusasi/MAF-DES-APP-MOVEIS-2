# Tarefas App

App de organização de tarefas por categoria, feito para o Trabalho 3 (parte 3 de 4 da A2, Persistência de Dados com Room) da matéria Desenvolvimento de Aplicativos Móveis. Ele é a continuação do Trabalho 2 (repositório anterior: https://github.com/matheusasi/MAF-DES-APP-MOVEIS), agora com os dados salvos de verdade no aparelho.

A ideia é simples: você cadastra categorias (Faculdade, Casa, Pessoal, etc) e cria tarefas dentro de cada categoria, marcando como concluídas, editando ou removendo quando quiser. O app mostra o progresso de cada categoria e quanto falta para o prazo de cada tarefa. Tudo fica guardado no banco, então os dados continuam lá depois de fechar e abrir o app.

## Telas do app

- Início: resumo com quantas tarefas estão pendentes e quantas categorias existem (números vindos do banco).
- Minhas Tarefas: lista de tarefas, com formulário para adicionar, checkbox para concluir e botão para remover.
- Detalhe da Tarefa: busca a tarefa direto no banco pelo id, mostra a categoria e o prazo, e permite editar título e descrição.
- Minhas Categorias: lista de categorias, com formulário para adicionar (nome e cor) e botão para remover.
- Detalhe da Categoria: busca a categoria pelo id, mostra as tarefas dela, a contagem de concluídas e permite editar o nome.
- Perfil: formulário para editar o nome do usuário.
- Sobre o App: informações gerais sobre o projeto.

## Persistência com Room

O código do banco fica em `app/src/main/java/com/exemplo/tarefasapp/dados/`:

- `Tarefa.kt` e `Categoria.kt`: as `@Entity` (tabelas `tarefas` e `categorias`), cada uma com `@PrimaryKey(autoGenerate = true)`.
- `TarefaDao.kt` e `CategoriaDao.kt`: os `@Dao`, com inserir, buscar todos, atualizar e deletar (todos `suspend`), mais `buscarPorId` nos dois e `buscarPorCategoria` no `TarefaDao`.
- `AppDatabase.kt`: o `@Database` (versão 1) no padrão Singleton. Na primeira vez que o banco é criado ele já recebe algumas tarefas e categorias de exemplo.

Toda chamada ao banco é feita dentro de `CoroutineScope(Dispatchers.IO).launch { ... }` (ou `LaunchedEffect(Unit)` para buscar ao abrir a tela), com `try/catch` e `Log.e`.

## Como rodar o projeto

1. Abrir a pasta do projeto no Android Studio (versão recente, com Jetpack Compose suportado).
2. Esperar o Gradle sincronizar (ele baixa Compose, Navigation, Room e o plugin KSP automaticamente).
3. Rodar em um emulador ou aparelho físico com Android 7.0 (API 24) ou superior.

Se preferir rodar pelo terminal:

```
./gradlew assembleDebug
```

O APK gerado fica em `app/build/outputs/apk/debug/`.

## Como testar a persistência

Adicione uma tarefa, feche o app completamente (tire ele dos apps recentes), abra de novo e confira que a tarefa continua na lista.

Vídeo do teste: https://www.loom.com/share/1e7146f5baf047f8b3a052611f819053

## Documentação do processo

O arquivo `DOCUMENTACAO.md`, na raiz do repositório, tem o registro das decisões, o que mudou desde o Trabalho 2 e os prints e vídeos do app rodando.
