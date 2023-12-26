import java.net.*;
import java.io.*;
import java.util.*;
import sd23.*;

public class Server {

    private static final int PORTA = 4999;
    private static Map<String, String> usuarios = new HashMap<>();
    private static Queue<String> filaTarefas = new LinkedList<>();

    public static void main(String args[]) throws IOException {

        ServerSocket ss = null;
        try{
            ss = new ServerSocket(PORTA);
            System.out.println("Servidor de Autenticação iniciado na porta " + PORTA);

            while (true) {
                Socket s = ss.accept();
                new Thread(new AutenticacaoHandler(s)).start();
                System.out.println("Client connected");

                try {
                    // obter a tarefa de ficheiro, socket, etc...
                    byte[] job = new byte[1000];

                    adicionarTarefaAFila();
        
                    // executar a tarefa
                    byte[] output = JobFunction.execute(job);
        
                    // utilizar o resultado ou reportar o erro
                    System.out.println("success, returned "+output.length+" bytes");

                    System.out.println(obterNumeroTarefasPendentes() + " tarefas pendentes");

                    removerTarefaDaFila();

                } catch (JobFunctionException e) {
                    System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ss != null && !ss.isClosed()) {
                    ss.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }    
    }

    private static class AutenticacaoHandler implements Runnable {

        private Socket clienteSocket;
        private PrintWriter out;
        private BufferedReader in;

        public AutenticacaoHandler(Socket clienteSocket) {
            this.clienteSocket = clienteSocket;
        }

        public void run() {
            try {
                out = new PrintWriter(clienteSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));

                // Receber ação do cliente (registo ou autenticação)
                String acao = in.readLine();

                if ("registo".equals(acao)) {
                    registrarUsuario();
                } else if ("autenticacao".equals(acao)) {
                    autenticarUsuario();
                }

                String acdois = in.readLine();
                if ("1".equals(acdois)){
                    out.println("Olá");

                }else if ("9".equals(acdois)){
                    clienteSocket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clienteSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void registrarUsuario() throws IOException {
            String nomeUsuario = in.readLine();
            String senha = in.readLine();
            usuarios.put(nomeUsuario, senha);
            out.println("Registo bem-sucedido para o usuário: " + nomeUsuario);
        }

        private void autenticarUsuario() throws IOException {
            String nomeUsuario = in.readLine();
            String senha = in.readLine();

            if (usuarios.containsKey(nomeUsuario) && usuarios.get(nomeUsuario).equals(senha)) {
                out.println("Autenticação bem-sucedida para o usuário: " + nomeUsuario);
            } else {
                out.println("Falha na autenticação. Verifique o nome de usuário e a senha.");
            }
        }
    }
    private static synchronized void adicionarTarefaAFila() {
        // Adicionar a tarefa à fila
        filaTarefas.add("Nova Tarefa");
    }

    private static synchronized void removerTarefaDaFila() {
        // Remover a tarefa da fila
        filaTarefas.poll();
    }

    private static synchronized int obterNumeroTarefasPendentes() {
        // Obter o número de tarefas pendentes na fila
        return filaTarefas.size();
    }
}