import java.util.List;
import java.util.Scanner;

public class Main {
    private static final MesaDeSom mesa = new MesaDeSom();
    private static final Dashboard dashboard = new Dashboard(mesa);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean rodando = true;

        while (rodando) {
            exibirMenu();

            switch (lerTexto("Opção: ")) {
                case "1" -> tocarInstrumento();
                case "2" -> pararInstrumento();
                case "3" -> listarInstrumentos();
                case "4" -> abrirDashboard();
                case "0" -> rodando = false;
                default -> System.out.println("Opção inválida.");
            }
        }

        mesa.pararTodos();
        dashboard.parar();
        scanner.close();

        System.out.println("Mesa de DJ encerrada.");
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("=================================");
        System.out.println("   Bem-vindo à Mesa de DJ");
        System.out.println("=================================");
        System.out.println("1 - Tocar instrumento");
        System.out.println("2 - Parar instrumento");
        System.out.println("3 - Listar instrumentos");
        System.out.println("4 - Dashboard ao vivo");
        System.out.println("0 - Sair");
        System.out.println("=================================");
    }

    /**
     * Se o instrumento ainda nao existe na mesa, cria e sobe a thread dele.
     * Se ja existe, so volta a tocar.
     */
    private static void tocarInstrumento() {
        String nome = lerTexto("Nome do instrumento: ");

        if (nome.isEmpty()) {
            System.out.println("Nome não pode ser vazio.");
            return;
        }

        Instrumento instrumento = mesa.buscar(nome);

        if (instrumento == null) {
            instrumento = new Instrumento(nome, true);
            instrumento.iniciar();
            mesa.adicionar(instrumento);
            System.out.println(nome + " entrou na mesa e está tocando.");
            return;
        }

        instrumento.setTocando(true);
        System.out.println(instrumento.getNome() + " voltou a tocar.");
    }

    private static void pararInstrumento() {
        String nome = lerTexto("Nome do instrumento: ");
        Instrumento instrumento = mesa.buscar(nome);

        if (instrumento == null) {
            System.out.println("Instrumento não encontrado.");
            return;
        }

        instrumento.setTocando(false);
        System.out.println(instrumento.getNome() + " parou.");
    }

    private static void listarInstrumentos() {
        List<Instrumento> instrumentos = mesa.listar();

        if (instrumentos.isEmpty()) {
            System.out.println("Nenhum instrumento na mesa.");
            return;
        }

        for (Instrumento instrumento : instrumentos) {
            System.out.printf(
                    "- %-16s %-10s %d batidas%n",
                    instrumento.getNome(),
                    instrumento.isTocando() ? "TOCANDO" : "PARADO",
                    instrumento.getBatidas());
        }
    }

    /**
     * Liga a thread de monitoramento e segura aqui ate o usuario apertar ENTER.
     * Enquanto isso o dashboard e quem escreve no console.
     */
    private static void abrirDashboard() {
        dashboard.iniciar();
        scanner.nextLine();
        dashboard.parar();
    }

    private static String lerTexto(String rotulo) {
        System.out.print(rotulo);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "0";
    }
}
