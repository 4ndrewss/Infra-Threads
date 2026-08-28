import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // A lista fica na MesaDeSom, que sincroniza o acesso: a Main altera a
        // lista enquanto a thread do Dashboard le ela para imprimir o status.
        MesaDeSom mesa = new MesaDeSom();
        Dashboard dashboard = new Dashboard(mesa);

        mesa.adicionar(new Instrumento("Violao", false));
        mesa.adicionar(new Instrumento("Guitarra", false));
        mesa.adicionar(new Instrumento("Bateria", false));
        mesa.adicionar(new Instrumento("Piano", false));
        mesa.adicionar(new Instrumento("Baixo", false));
        mesa.adicionar(new Instrumento("Violino", false));
        mesa.adicionar(new Instrumento("Sino", false));
        mesa.adicionar(new Instrumento("Harpa", false));

        for (Instrumento instrumentoAtual : mesa.listar()) {
            instrumentoAtual.iniciar();
        }

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("=================================");
            System.out.println("    Bem-vindo à Mesa de DJ");
            System.out.println("=================================");
            System.out.println("1 - Tocar instrumento");
            System.out.println("2 - Parar instrumento");
            System.out.println("3 - Listar instrumentos");
            System.out.println("4 - Dashboard ao vivo");
            System.out.println("0 - Sair");
            System.out.println("=================================");

            String opcao = scanner.nextLine();

            String instrumental = "";

            if(opcao.equals("1")){
                System.out.println("Qual instrumento voce quer tocar?");
                instrumental = scanner.nextLine();

                Instrumento instrumentoAtual = mesa.buscar(instrumental);

                if (instrumentoAtual == null) {
                    System.out.println("❌ Instrumento '" + instrumental + "' não encontrado na mesa.");
                } else if (!instrumentoAtual.isTocando()) {
                    instrumentoAtual.retomar();
                    System.out.println("▶ " + instrumentoAtual.getNome() + " retomado com sucesso!");
                } else {
                    System.out.println("⚠️ O instrumento '" + instrumentoAtual.getNome() + "' já está tocando!");
                }
            } else if(opcao.equals("2")){
                System.out.println("Qual instrumento voce quer parar?");
                instrumental = scanner.nextLine();

                Instrumento instrumentoAtual = mesa.buscar(instrumental);

                if (instrumentoAtual == null) {
                    System.out.println("❌ Instrumento '" + instrumental + "' não encontrado na mesa.");
                } else if (instrumentoAtual.isTocando()) {
                    instrumentoAtual.pausar();
                    System.out.println("⏸ " + instrumentoAtual.getNome() + " silenciado com sucesso.");
                } else {
                    System.out.println("⚠️ O instrumento '" + instrumentoAtual.getNome() + "' já está parado!");
                }
            } else if(opcao.equals("3")){
                System.out.println("--- Lista de Instrumentos Disponíveis ---");

                List<Instrumento> listaInstrumentos = mesa.listar();

                for(int i=0;i<listaInstrumentos.size();i++){
                    Instrumento instrumentoAtual = listaInstrumentos.get(i);

                    System.out.println("- " + instrumentoAtual.getNome());
                }
            } else if(opcao.equals("4")){
                // O dashboard assume o console ate o usuario apertar ENTER.
                dashboard.iniciar();
                scanner.nextLine();
                dashboard.parar();
            } else if(opcao.equals("0")){
                System.out.println("Encerrando o sistema do DJ... Até logo!");

                dashboard.parar();
                mesa.pararTodos();
                scanner.close();
                System.exit(0);
            } else{
                System.out.println("Opção inválida! Por favor, digite apenas um número entre 0 e 4.");
            }
        }
    }
}