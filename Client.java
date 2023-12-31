import java.net.*;
import java.io.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORTA = 4999;
    public static void main(String args[]) throws IOException {
        try {
            Socket s = new Socket(HOST, PORTA);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Escolha a ação: registo ou autenticacao");
            String acao = consoleInput.readLine();
            out.println(acao);

            System.out.println("Digite o nome de usuário:");
            String nomeUsuario = consoleInput.readLine();
            out.println(nomeUsuario);

            System.out.println("Digite a senha:");
            String senha = consoleInput.readLine();
            out.println(senha);

            String resposta = in.readLine();
            System.out.println(resposta);


            while (true){
                System.out.println("Escolha a ação: 1 para dizer Olá; 9 para sair.");
                String acdois = consoleInput.readLine();
                out.println(acdois);

                if ("9".equals(acdois)) {
                    break; // Sair do loop se o usuário escolher "9"
                }

            }

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}