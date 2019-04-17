# Corrida GymPass

#### O projeto foi desenvolvido em Java(v.8) para retornar o resultado e estatísticas de uma corrida.

Para iniciar basta executar a classe App.java

Dado um arquivo de log(corrida.log) com as voltas registradas na corrida, o software faz a leitura do mesmo
e retorna estatísticas como:
- Posição Chegada, Código Piloto, Nome Piloto, Qtde Voltas Completadas e Tempo Total de Prova
- Melhor volta de cada piloto
- Melhor volta da corrida
- Velocidade media dos pilotos na corrida
- DIferença de cada piloto em relação ao líder

Todo o resultado é consolidado num arquivo "resultadoCorrida.txt"

Por padrão, ambos arquivos estão no diretorio raiz do projeto, para quaisquer alterações no local e/ou nome dos arquivos, basta atualizar o arquivo conf.properties.
