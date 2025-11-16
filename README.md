Auto Strong - Algoritmo de Carga Inteligente para Máquinas de Exercício
📌 Sobre o Projeto

O Auto Strong é um protótipo de algoritmo desenvolvido para máquinas de exercício inteligentes — equipamentos capazes de ajustar automaticamente a carga aplicando resistência ativa, sem a necessidade de anilhas, halteres ou pesos físicos.

A motivação veio de um problema real encontrado em muitas academias públicas no Brasil: dificuldades de manutenção, vandalismo, risco de acidentes e falta de controle sobre a carga utilizada pelos praticantes.

Este projeto demonstra, em Java, como um sistema pode usar apenas a força aplicada na primeira repetição para estimar a capacidade do usuário e regular dinamicamente a resistência ao longo de todo o movimento, respeitando o tipo de treino escolhido:

Força

Hipertrofia

Resistência

O algoritmo foi estruturado de forma simples, modular e totalmente orientada a objetos, permitindo futuras integrações com sensores físicos (Arduino, ESP32, STM32, Raspberry Pi etc.).

🧠 Como o algoritmo funciona?

O usuário seleciona o tipo de treino desejado.

Um sensor registra a força aplicada na primeira repetição.

O sistema identifica:

faixa ideal de repetições (de acordo com o objetivo)

força inicial máxima

projeção de fadiga

O algoritmo calcula a carga virtual ideal para que o usuário atinja a falha nas últimas repetições da faixa alvo.

Durante o treino, o sistema ajusta a resistência dinamicamente, mantendo:

segurança

progressão

eficiência fisiológica

Nenhum input humano além da força inicial e do objetivo é necessário.

🏗️ Estrutura do Projeto
/src
 └── org.example
       ├── Main.java
       └── trainingtypes
              ├── Forca.java
              ├── Hipertrofia.java
              └── Resistencia.java


Cada classe contém:

faixa de repetições ideais

lógica de cálculo de carga

projeção de fadiga

ajuste automático da força oposta

🚀 Possíveis Evoluções Futuras

O projeto abre caminho para várias melhorias:

🔩 Integração com Hardware

Sensores de força (célula de carga, strain gauge)

Atuação com motor elétrico ou pistão hidráulico inteligente

Comunicação MQTT/Bluetooth/Wi-Fi

Controladores dedicados (ESP32, STM32, Raspberry Pi)

📈 Personalização com Machine Learning

Análise do histórico do usuário

Ajuste automático baseado em performance passada

Detecção de padrões de falha

Predição de cargas ideais por perfil

🏥 Impacto na Saúde e Qualidade de Vida

Equipamentos como este poderiam:

Reduzir drasticamente lesões por excesso de carga

Tornar academias públicas mais seguras e acessíveis

Diminuir o sedentarismo ao facilitar o uso dos equipamentos

Democratizar treinos de força com inteligência embutida

Eliminar riscos associados ao uso de pesos soltos

Além disso, cidades poderiam implementar controle de acesso por cartão, permitir monitoramento remoto e promover programas públicos de saúde baseados em dados reais de uso.

💡 Objetivo do Projeto

Este repositório é apenas o primeiro passo conceitual, focado na lógica matemático-física e na estrutura necessária para que máquinas de treino inteligentes funcionem sem pesos físicos tradicionais.

A ideia maior é mostrar que:

é possível criar uma academia pública mais segura, moderna e acessível,

usando algoritmos,

sensores,

e criatividade aplicada à solução de problemas sociais.
