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
            System.out.println("---------------------------------");
            System.out.println("add <nome>          - entra na mesa tocando");
            System.out.println("bpm <nome> <ms>     - muda a velocidade");
            System.out.println("=================================");

            String opcao = scanner.nextLine().trim();

            String instrumental = "";

            // Os comandos de texto convivem com o menu numerico: a primeira
            // palavra decide se e comando ou opcao.
            String[] partes = opcao.split("\\s+");

            if(partes[0].equalsIgnoreCase("add")){
                adicionarInstrumento(mesa, partes);
            } else if(partes[0].equalsIgnoreCase("bpm")){
                mudarVelocidade(mesa, partes);
            } else if(opcao.equals("1")){
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
                System.out.println("Opção inválida! Digite um número entre 0 e 4, ou 'add' / 'bpm'.");
            }
        }
    }

    /**
     * Comando "add guitarra": cria o instrumento e sobe a thread dele com o
     * programa ja rodando. A MesaDeSom sincroniza a insercao, entao o Dashboard
     * pode estar lendo a lista nesse exato momento sem quebrar.
     */
    private static void adicionarInstrumento(MesaDeSom mesa, String[] partes) {
        if (partes.length < 2) {
            System.out.println("Uso: add <nome do instrumento>");
            return;
        }

        String nome = partes[1];

        if (mesa.buscar(nome) != null) {
            System.out.println("⚠️ '" + nome + "' já está na mesa.");
            return;
        }

        Instrumento novo = new Instrumento(nome, true);
        novo.iniciar();
        mesa.adicionar(novo);

        System.out.println("➕ " + nome + " entrou na mesa e já está tocando.");
    }

    /**
     * Comando "bpm bateria 500": muda o intervalo entre as batidas em
     * milissegundos, com o instrumento tocando.
     */
    private static void mudarVelocidade(MesaDeSom mesa, String[] partes) {
        if (partes.length < 3) {
            System.out.println("Uso: bpm <nome do instrumento> <intervalo em ms>");
            return;
        }

        Instrumento instrumento = mesa.buscar(partes[1]);

        if (instrumento == null) {
            System.out.println("❌ Instrumento '" + partes[1] + "' não encontrado na mesa.");
            return;
        }

        try {
            instrumento.setIntervaloMs(Long.parseLong(partes[2]));
            System.out.println("⏩ " + instrumento.getNome() + " agora bate a cada "
                    + instrumento.getIntervaloMs() + "ms (" + instrumento.getBpm() + " BPM).");
        } catch (NumberFormatException e) {
            System.out.println("❌ '" + partes[2] + "' não é um número.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}