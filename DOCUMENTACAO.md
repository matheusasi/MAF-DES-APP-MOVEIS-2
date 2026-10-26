# Documentação do processo, Trabalho 2 (MAF)

Trabalho feito individualmente, por um único integrante do trio.

## Como estava no Trabalho 1 e o que mudou

No Trabalho 1 eu tinha as telas estáticas, feitas com Row, Column, Box e Scaffold. Os botões existiam e estavam estilizados, mas não trocavam de tela, e os dados ficavam fixos no próprio código.

Para o Trabalho 2 eu precisei transformar isso num app de verdade. Como o projeto antigo não estava mais disponível para mim (perdi o contexto ao mudar de turma), refiz o projeto do zero. As telas de Perfil e Sobre são a evolução das telas do Trabalho 1, agora navegáveis. A partir delas eu construí o restante do MAF: Início, as duas listas e os dois detalhes, todas conectadas por um NavHost central.

A principal mudança técnica é que agora os dados (tarefas e categorias) vivem numa lista reativa (`mutableStateListOf`) dentro de um objeto compartilhado, `AppDados`, em vez de ficarem presos dentro de cada tela. Isso foi necessário porque uma tela, como o detalhe de uma categoria, precisa enxergar as tarefas que foram criadas em outra tela.

## Por que essas telas

O tema escolhido foi um organizador de tarefas separadas por categoria, parecido com o exemplo de lista de tarefas visto em aula, só que com duas listas em vez de uma.

- Início: dá um panorama rápido assim que o app abre, mostrando quantas tarefas estão pendentes sem precisar entrar em nenhuma lista.
- Minhas Tarefas e Minhas Categorias: são o núcleo do trabalho, as duas listas pedidas no enunciado, cada uma com seu próprio tipo de dado.
- Detalhe da Tarefa e Detalhe da Categoria: mostram a informação específica de cada item, sem deixar a tela de lista cheia demais.
- Perfil e Sobre o App: são as telas que vieram do Trabalho 1, agora ligadas na navegação.

## Decisões de organização do código

Separei o projeto em pacotes por responsabilidade: `dados` (as data classes e o `AppDados`), `navegacao` (o objeto `Rotas` e o `AppNavigation`, com o NavHost), `telas` (uma tela por arquivo) e `componentes` (a barra inferior, usada em quatro telas).

Escolhi guardar as duas listas num objeto (`AppDados`) em vez de deixar cada tela com seu próprio estado. Assim consigo passar dado de verdade entre telas usando só o que foi visto em aula, sem ViewModel nem banco de dados. As rotas de detalhe recebem o id como parte da rota (`detalhe_tarefa/{tarefaId}`), do mesmo jeito que aparece nos slides de Navigation Compose. A tela de detalhe pega o id, converte para número e busca o item certo no `AppDados`.

Também decidi que cada tela monta o seu próprio `Scaffold`. As telas principais recebem a barra inferior e as de detalhe recebem a TopAppBar com voltar. Assim não precisei de lógica extra para esconder a barra conforme a rota atual.

## A complexidade extra no Detalhe

O enunciado pedia que pelo menos uma tela de detalhe fizesse mais do que só reexibir os campos. Eu coloquei essa complexidade nas duas:

- No Detalhe da Tarefa, busco a categoria vinculada na lista de categorias (combinando as duas listas) e calculo um texto com quantos dias faltam para o prazo.
- No Detalhe da Categoria, filtro as tarefas pelo id da categoria e calculo quantas já estão concluídas, mostrando a conta pronta na tela.

Escolhi essas duas porque uma tarefa sem a categoria dela fica incompleta, e uma categoria sem as tarefas dela também.

## Dificuldades

- Primeira build demorou muito. O Gradle precisou baixar o Build-Tools e a Platform 34 que não estavam instalados, e a build ficou vários minutos sem mostrar nada. Foi só esperar terminar.
- O Android Studio mostrava "Gradle sync needed, Unable to determine project Android Gradle Plugin (AGP) version". Resolvi clicando em Sync project e depois fazendo o Build pelo menu. Em alguns casos o build aparecia como cancelado logo no início, e o problema só sumiu depois que sincronizei de novo e invalidei o cache da IDE.
- O Android Studio avisou que o `TopAppBar` era experimental. Resolvi colocando `@OptIn(ExperimentalMaterial3Api::class)` nas telas que usam esse componente.
- O preview não aparecia de primeira. Ele só funcionou depois que o projeto foi compilado pela própria IDE (Build > Build Project).

## Prints e vídeo do app funcionando

Vídeo mostrando o app rodando no Pixel 6 (adicionar e remover tarefa e categoria, abrir os detalhes, navegar pela barra inferior e pelo Perfil):

https://www.loom.com/share/99c151dff67944a1a5f5bad82f142e53

Vídeo mostrando a remoção de uma categoria:

https://www.loom.com/share/5b8883bbab71474f9828f850a1183516
