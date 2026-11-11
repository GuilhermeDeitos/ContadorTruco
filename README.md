# ContadorTruco

Aplicativo Android para acompanhar a pontuação de partidas de Truco entre dois jogadores ou equipes.

Projeto desenvolvido como **trabalho acadêmico da pós-graduação em Programação para Dispositivos Móveis da Universidade Tecnológica Federal do Paraná (UTFPR), Campus Pato Branco**. A atividade aborda componentes visuais básicos, tratamento de eventos e navegação entre telas no Android.

## Objetivo acadêmico

Praticar o desenvolvimento de interfaces com XML, o uso de Kotlin e View Binding, o tratamento de cliques e a troca de informações entre Activities. O aplicativo também recebeu melhorias de organização, persistência de dados e apresentação visual além da proposta inicial.

## Funcionalidades

- Adição de 1, 3, 6, 9 ou 12 pontos para cada jogador.
- Identificação do vencedor ao atingir ou ultrapassar 12 pontos.
- Diálogo com o resultado e opção de iniciar uma nova partida ou desfazer a última pontuação.
- Histórico com a quantidade de partidas vencidas por jogador.
- Personalização dos nomes dos jogadores ou equipes.
- Reinício de pontos, vitórias e nomes, com confirmação.
- Salvamento local do estado do jogo para retomar a sessão.
- Temas claro e escuro, com textos em português e inglês.

## Tecnologias

- Kotlin.
- Android Views e layouts XML.
- View Binding.
- Material Components e tema Material 3.
- ConstraintLayout.
- Activity Result API para o retorno dos nomes editados.
- SharedPreferences para armazenamento local.
- JUnit e Espresso para testes.

## Organização

| Componente | Responsabilidade |
| --- | --- |
| `MainActivity` | Placar, ações dos jogadores e navegação |
| `HistoricoActivity` | Consulta às vitórias acumuladas |
| `NomesActivity` | Edição e confirmação dos nomes |
| `Partida` | Estado do jogo e regras de pontuação, vitória e desfazer |
| `PartidaStorage` | Salvamento e recuperação da sessão |

## Como executar

1. Clone o repositório e abra a pasta do projeto no Android Studio.
2. Instale o Android SDK API 37 pelo SDK Manager, caso necessário.
3. Aguarde a sincronização do Gradle e o download das dependências.
4. Selecione um emulador ou dispositivo com Android 13 (API 33) ou superior.
5. Execute o módulo `app`.

O aplicativo funciona localmente, sem cadastro ou servidor. Os dados da sessão permanecem no dispositivo; o histórico representa vitórias acumuladas, não uma lista de todas as partidas anteriores.

## Testes

O projeto contém testes unitários das regras de jogo e testes instrumentados das telas e do armazenamento. Os testes instrumentados precisam de um dispositivo ou emulador conectado.

Para executar pelo terminal no Windows:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat connectedDebugAndroidTest
```

## Contexto

Este repositório registra um projeto de aprendizagem desenvolvido durante a pós-graduação. Seu propósito é demonstrar a aplicação dos conceitos estudados e a evolução do trabalho acadêmico.
