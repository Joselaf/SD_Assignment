import java.net.*;
import java.io.*;
import java.util.*;
import sd23.JobFunction;
import sd23.JobFunctionException;

public class Server {

    private static final int PORTA = 4999;
    private static Map<String, String> usuarios = new HashMap<>();

    public static void main(String args[]) throws IOException {
        // try {
        //     // obter a tarefa de ficheiro, socket, etc...
        //     byte[] job = new byte[1000];

        //     // executar a tarefa
        //     byte[] output = JobFunction.execute(job);

        //     // utilizar o resultado ou reportar o erro
        //     System.err.println("success, returned "+output.length+" bytes");
        // } catch (JobFunctionException e) {
        //     System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
        // }

        ServerSocket ss = null;
        try{
            ss = new ServerSocket(PORTA);
            System.out.println("Servidor de Autenticação iniciado na porta " + PORTA);

            while (true) {
                Socket s = ss.accept();
                new Thread(new AutenticacaoHandler(s)).start();
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

        System.out.println("Client connected");
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
}