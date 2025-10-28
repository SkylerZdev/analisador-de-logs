package br.edu.icev.aed.forense;
import java.util.*;
import java.io.*;

public class SolucaoForense implements AnaliseForenseAvancada {

        @Override
    public Set<String> encontrarSessoesInvalidas(String caminhoArquivoCsv) throws IOException {
        /**
        * Desafio 1 (Pilha): Encontra sessões de usuário que foram corrompidas ou
        * deixadas abertas, indicando uma possível falha ou ataque. Uma sessão é
        * inválida se um usuário tenta um novo LOGIN antes de um LOGOUT, ou se a sessão
        * termina sem um LOGOUT correspondente.
        *
        * @param caminhoArquivoCsv O caminho para o arquivo de logs.
        * @return Um Set contendo os IDs de todas as sessões (SESSION_ID) inválidas.
        * @throws IOException Se ocorrer um erro de leitura do arquivo.
        */
        throw new UnsupportedOperationException("Unimplemented method 'encontrarSessoesInvalidas'");
    }

    @Override
     public List<String> reconstruirLinhaTempo(String caminhoArquivoCsv, String sessionId) throws IOException {
        /**
        * Desafio 2 (Fila): Reconstrói a sequência exata de ações de um usuário dentro
        * de uma sessão específica, da primeira à última ação.
        *
        * @param caminhoArquivoCsv O caminho para o arquivo de logs.
        * @param sessionId         O ID da sessão a ser reconstruída.
        * @return Uma List<String> contendo os ACTION_TYPE na ordem cronológica em que
        *         ocorreram dentro da sessão. Retorna uma lista vazia se a sessão não
        *         for encontrada.
        * @throws IOException Se ocorrer um erro de leitura do arquivo.
        */
        throw new UnsupportedOperationException("Unimplemented method 'reconstruirLinhaTempo'");
    }

    @Override
    public List<Alerta> priorizarAlertas(String caminhoArquivoCsv, int n) throws IOException {
        /**
        * Desafio 3 (Fila de Prioridade): Identifica os N eventos de maior risco de
        * acordo com a equipe de resposta a incidentes. O risco é determinado pelo
        * campo SEVERITY_LEVEL.
        *
        * @param caminhoArquivoCsv O caminho para o arquivo de logs.
        * @param n                 O número de eventos de risco a serem retornados.
        * @return Uma List<Alerta> contendo os 'n' alertas mais severos, ordenados do
        *         mais severo para o menos severo. Em caso de mesma severidade, a
        *         ordem não importa.
        * @throws IOException Se ocorrer um erro de leitura do arquivo.
        */
        throw new UnsupportedOperationException("Unimplemented method 'priorizarAlertas'");
    }

    @Override
    public Map<Long, Long> encontrarPicosTransferencia(String caminhoArquivoCsv) throws IOException {
        /**
        * Desafio 4 (Pilha Monotônica): Detecta anomalias em transferências de dados.
        * Para cada evento de transferência, encontra o próximo evento no tempo que
        * envolveu uma transferência de dados MAIOR. Isso ajuda a identificar
        * escalonamentos súbitos na exfiltração de dados.
        *
        * @param caminhoArquivoCsv O caminho para o arquivo de logs.
        * @return Um Map<Long, Long> onde a chave é o TIMESTAMP de um evento e o valor
        *         é o TIMESTAMP do próximo evento com BYTES_TRANSFERRED maior. Se não
        *         houver evento maior subsequente, a chave não deve estar no mapa.
        * @throws IOException Se ocorrer um erro de leitura do arquivo.
        */
        throw new UnsupportedOperationException("Unimplemented method 'encontrarPicosTransferencia'");
    }

    @Override
    public Optional<List<String>> rastrearContaminacao(String caminhoArquivoCsv, String recursoInicial,String recursoAlvo) throws IOException {
        /**
        * Desafio 5 (Grafo): Mapeia o caminho de contaminação do invasor através do
        * sistema, mostrando como ele se moveu de um recurso para outro. O caminho é a
        * sequência mais curta de recursos acessados entre o ponto de entrada e o alvo
        * final.
        *
        * @param caminhoArquivoCsv O caminho para o arquivo de logs.
        * @param recursoInicial    O ponto de entrada do ataque (ex.: "/usr/bin/sshd").
        * @param recursoAlvo       O alvo final do ataque (ex.: "/var/secrets/key.dat").
        * @return Um Optional<List<String>> contendo a sequência de recursos que formam
        *         o caminho mais curto. Retorna Optional.empty() se não houver caminho.
        * @throws IOException Se ocorrer um erro de leitura do arquivo.
        */
        throw new UnsupportedOperationException("Unimplemented method 'rastrearContaminacao'");
    }



}