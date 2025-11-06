package br.edu.icev.aed.forense;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;

public class SolucaoForense implements AnaliseForenseAvancada {

    //Construtor Vazio
    public SolucaoForense(){}


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

    @Override
    public Set<String> encontrarSessoesInvalidas(String caminhoArquivoCsv) throws IOException {
        //Cria o leitor do arquivo de logs, e se não encontrar o arquivo lança IOException.
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivoCsv))){
                
            //Pula o cabeçalho.    
            leitor.readLine();

            /* 
            * Mapa que guarda as sessões ativas por usuário.
            * Cada usuário tem sua própria pilha de sessões.
            */
            Map<String, Stack<String>> mapa = new HashMap<>();
                
            // Set que vai guardar as sessões inválidas para retornar
            Set<String> invalidas = new HashSet<>();
                
            // Atributos que serão usados para temporariamente guardar os dados no loop de leitura
            String userID, sessionID, actionType;
                
            // Pilha do usuario atual do loop de leitura
            Stack<String> pilhaAtual;
                
            //String pra guardar a linha atual do leitor
            String linha;

            //Começo do Loop de cada linha dos logs
            while((linha = leitor.readLine()) != null){
                 int c1 = linha.indexOf(',');
                 int c2 = linha.indexOf(',', c1 + 1);
                 int c3 = linha.indexOf(',', c2 + 1);
                 int c4 = linha.indexOf(',', c3 + 1);
                         
                userID     = linha.substring(c1 + 1, c2);
                sessionID  = linha.substring(c2 + 1, c3);
                actionType = linha.substring(c3 + 1, c4);
                
                pilhaAtual = mapa.computeIfAbsent(userID, k -> new Stack<>());
            
                switch (actionType) {
                    case "LOGIN":
                        //Se o usuario tiver sessões ativas quando o Login é realizado a sessão é adicionada às invalidas
                        if(!pilhaAtual.isEmpty()){
                            invalidas.add(sessionID);
                        } 
                        //Adiciona a sessão às sessões ativas do usuário
                        pilhaAtual.push(sessionID);
                        break;
                
                    case "LOGOUT":
                        //Guarda a ultima sessão ativa em topo, guardando null se não houver sessão ativa.
                        String topo = pilhaAtual.isEmpty() ? null : pilhaAtual.pop();
                        //
                        if(topo == null || !topo.equals(sessionID)){
                            invalidas.add(sessionID);
                        }
                        break;

                    default:
                        break;
                }
            }

            for (Stack<String> pilha : mapa.values()) {
                invalidas.addAll(pilha);
            }
            return invalidas;

        } catch (FileNotFoundException e){
            throw new IOException("Arquivo não encontrado ", e);
        }
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

        // Estrutura FIFO thread-safe
        Queue<String> fila = new ConcurrentLinkedQueue<>();

        //Cria o leitor do arquivo de logs, e se não encontrar o arquivo lança IOException.
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivoCsv))) {
        
        // Pula o cabeçalho
        leitor.readLine();

        String SESSIONID, actionType;

        //String pra guardar a linha atual do leitor
        String linha;
        while ((linha = leitor.readLine()) != null) {
            
            int c1 = linha.indexOf(',');
            int c2 = linha.indexOf(',', c1 + 1);
            int c3 = linha.indexOf(',', c2 + 1);
            int c4 = linha.indexOf(',', c3 + 1);
                         
            SESSIONID  = linha.substring(c2 + 1, c3);
            actionType = linha.substring(c3 + 1, c4);

            // Pega apenas o sessionId desejado
            if (!SESSIONID.equals(sessionId)) continue;
            fila.add(actionType);
            
        }
    } catch (FileNotFoundException e) {
        throw new IOException("Arquivo não encontrado", e);
    }
     //Retorna como LinkedList 
     return new LinkedList<>(fila);
    }

    @Override
    public List<Alerta> priorizarAlertas(String caminhoArquivoCsv, int n) throws IOException {
        
        if (n <= 0){
            return new ArrayList<>();
        }
        //Cria o leitor do arquivo de logs, e se não encontrar o arquivo lança IOException.
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivoCsv))){

            leitor.readLine();

            List<Alerta> listaAlerta = new ArrayList<>(n);
            Map<Integer, List<Alerta>> mapa = new HashMap<>();

            int                                      severity_level;
            long                       timestamp, bytes_transferred;
            String    userID, sessionID, actionType, linha, recurso;

            //Começo do Loop de cada linha dos logs
            while ((linha = leitor.readLine()) != null) {
                
                int c1 =   linha.indexOf(',');
                int c2 =   linha.indexOf(',', c1 + 1);
                int c3 =   linha.indexOf(',', c2 + 1);
                int c4 =   linha.indexOf(',', c3 + 1);
                int c5 =   linha.indexOf(',', c4 + 1);
                int c6 =   linha.indexOf(',', c5 + 1);
            
                // Extrai cada campo da linha
                 timestamp         = Long.parseLong(linha.substring(0, c1));
                 userID            = linha.substring(c1 + 1, c2);
                 sessionID         = linha.substring(c2 + 1, c3);
                 actionType        = linha.substring(c3 + 1, c4);
                 recurso           = linha.substring(c4 + 1, c5);
                 severity_level    = Integer.parseInt(linha.substring(c5 + 1, c6));
                 bytes_transferred = Long.parseLong(linha.substring(c6 + 1));
                
                 //Criação do Alerta
                 Alerta alerta = new Alerta (
                                timestamp, userID, sessionID, actionType,
                                recurso, severity_level, bytes_transferred
                            );
            
                //Adiciona o Alerta em sua devida lista, de acordo com seu Severity_Level.
                mapa.computeIfAbsent(severity_level, k-> new ArrayList<>()).add(alerta);
            }
            
            if(mapa.isEmpty()){
                return new ArrayList<>();
            }
            int grau = 10;
            while(listaAlerta.size() < n && grau > 0){
                List<Alerta> listaDoGrau = mapa.get(grau); if(listaDoGrau==null) { grau--; continue; }
                
                if(n-listaAlerta.size()>= listaDoGrau.size()){
                        listaAlerta.addAll(listaDoGrau);
                }   else {
                        listaAlerta.addAll(listaDoGrau.subList(0, n-listaAlerta.size()));
                }

                grau --;
            }

            return listaAlerta;


        }   catch (FileNotFoundException e){
            throw new IOException("Arquivo não encontrado ", e);
        }
    }


    @Override
    public Map<Long, Long> encontrarPicosTransferencia(String caminhoArquivoCsv) throws IOException {
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivoCsv))){

            leitor.readLine();

            Stack<String> leitorInv = new Stack<>();
            Stack<Map.Entry<Long,Long>> pilha = new Stack<>();
            Map<Long, Long> mapa = new HashMap<>();

            long  timestamp, bytes_transferred;
            String linha;

            while ((linha = leitor.readLine()) != null){
                int c1 =   linha.indexOf(',');
                int c2 =   linha.indexOf(',', c1 + 1);
                int c3 =   linha.indexOf(',', c2 + 1);
                int c4 =   linha.indexOf(',', c3 + 1);
                
                if (!linha.substring(c3+1, c4).equals("DATA_TRANSFER")){ continue; }

                leitorInv.push(linha);
            }

            //Começo do Loop Invertido
            while (!leitorInv.isEmpty()) {
                linha = leitorInv.pop();
                
                int c1 =   linha.indexOf(',');
                int c2 =   linha.indexOf(',', c1 + 1);
                int c3 =   linha.indexOf(',', c2 + 1);
                int c4 =   linha.indexOf(',', c3 + 1);
                int c5 =   linha.indexOf(',', c4 + 1);
                int c6 =   linha.indexOf(',', c5 + 1);
            
                // Extrai cada campo da linha
                 timestamp         = Long.parseLong(linha.substring(0, c1));
                 bytes_transferred = Long.parseLong(linha.substring(c6 + 1));

                 while (!pilha.isEmpty() && pilha.peek().getValue() < bytes_transferred){
                    pilha.pop();
                 }
                 
                 if (!pilha.isEmpty()){
                    mapa.put(timestamp, pilha.peek().getKey());
                 }
                 
                 pilha.push(new AbstractMap.SimpleEntry<>(timestamp, bytes_transferred));
            

            }
            
            return mapa;

        }
         catch (FileNotFoundException e){
            throw new IOException("Arquivo não encontrado ", e);
        }
        
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