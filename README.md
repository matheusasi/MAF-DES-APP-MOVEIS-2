# Tarefas App

App de organização de tarefas por categoria, feito para o Trabalho 2 (MAF, Mínimo Aplicativo Funcional) da matéria Desenvolvimento de Aplicativos Móveis.

A ideia é simples: você cadastra categorias (Faculdade, Casa, Pessoal, etc) e cria tarefas dentro de cada categoria, marcando como concluídas ou removendo quando quiser. O app mostra o progresso de cada categoria e quanto falta para o prazo de cada tarefa.

## Telas do app

- Início: resumo com quantas tarefas estão pendentes e quantas categorias existem.
- Minhas Tarefas: lista de tarefas, com formulário para adicionar, checkbox para concluir e botão para remover.
- Detalhe da Tarefa: mostra a tarefa, a categoria vinculada e quantos dias faltam para o prazo.
- Minhas Categorias: lista de categorias, com formulário para adicionar (nome e cor) e botão para remover.
- Detalhe da Categoria: mostra a categoria e todas as tarefas vinculadas a ela, com a contagem de quantas já foram concluídas.
- Perfil: formulário para editar o nome do usuário.
- Sobre o App: informações gerais sobre o projeto.

A navegação entre as telas é feita com Navigation Compose, usando o objeto `Rotas` (`app/src/main/java/com/exemplo/tarefasapp/navegacao/Rotas.kt`) e o `NavHost` central em `AppNavigation.kt`. A barra inferior (bottom bar) fica fixa nas telas principais (Início, Tarefas, Categorias e Perfil).

## Como rodar o projeto

1. Abrir a pasta do projeto no Android Studio (versão recente, com Jetpack Compose suportado).
2. Esperar o Gradle sincronizar (ele vai baixar as dependências do Compose e do Navigation automaticamente).
3. Rodar em um emulador ou aparelho físico com Android 7.0 (API 24) ou superior.

Se preferir rodar pelo terminal:

```
./gradlew assembleDebug
```

O APK gerado fica em `app/build/outputs/apk/debug/`.

## Sem persistência de dados

Os dados (tarefas e categorias) ficam guardados em memória, no objeto `AppDados`. Isso quer dizer que, ao fechar o app, tudo volta para os dados de exemplo. Isso é esperado para esta entrega, pois persistência é assunto de um trabalho futuro da matéria.

## Documentação do processo

O arquivo `DOCUMENTACAO.md`, na raiz do repositório, tem o registro das decisões, o que mudou desde o Trabalho 1 e os prints do app rodando.
