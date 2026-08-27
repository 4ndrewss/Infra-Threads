import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Instrumento> listaInstrumentos = new ArrayList<>();

        Instrumento instrumento1 = new Instrumento("Violao", false);
        Instrumento instrumento2 = new Instrumento("Guitarra", false);
        Instrumento instrumento3 = new Instrumento("Bateria", false);
        Instrumento instrumento4 = new Instrumento("Piano", false);
        Instrumento instrumento5 = new Instrumento("Baixo", false);
        Instrumento instrumento6 = new Instrumento("Violino", false);
        Instrumento instrumento7 = new Instrumento("Sino", false);
        Instrumento instrumento8 = new Instrumento("Harpa", false);

        listaInstrumentos.add(instrumento1);
        listaInstrumentos.add(instrumento2);
        listaInstrumentos.add(instrumento3);
        listaInstrumentos.add(instrumento4);
        listaInstrumentos.add(instrumento5);
        listaInstrumentos.add(instrumento6);
        listaInstrumentos.add(instrumento7);
        listaInstrumentos.add(instrumento8);

        for(int i=0;i<listaInstrumentos.size();i++){
            Instrumento instrumentoAtual = listaInstrumentos.get(i);
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
            System.out.println("0 - Sair");
            System.out.println("=================================");

            String opcao = scanner.nextLine();

            String instrumental = "";

            if(opcao.equals("1")){
                System.out.println("Qual instrumento voce quer tocar?");
                instrumental = scanner.nextLine();
                boolean encontrado = false;

                for(int i=0;i<listaInstrumentos.size();i++){
                    Instrumento instrumentoAtual = listaInstrumentos.get(i);

                    if (instrumentoAtual.getNome().equalsIgnoreCase(instrumental)) {
                        encontrado = true;

                        if (!instrumentoAtual.isTocando()) {
                            instrumentoAtual.retomar();
                            System.out.println("▶ " + instrumentoAtual.getNome() + " retomado com sucesso!");
                        } else {
                            System.out
                                    .println("⚠️ O instrumento '" + instrumentoAtual.getNome() + "' já está tocando!");
                        }
                        break;
                    }
                }

                if (encontrado == false) {
                    System.out.println("❌ Instrumento '" + instrumental + "' não encontrado na mesa.");
                }
            } else if(opcao.equals("2")){
                System.out.println("Qual instrumento voce quer parar?");
                instrumental = scanner.nextLine();
                boolean encontrado = false;

                for(int i=0;i<listaInstrumentos.size();i++){
                    Instrumento instrumentoAtual = listaInstrumentos.get(i);

                    if (instrumentoAtual.getNome().equalsIgnoreCase(instrumental)) {
                        encontrado = true;

                        if (instrumentoAtual.isTocando()) {
                            instrumentoAtual.pausar();
                            System.out.println("⏸ " + instrumentoAtual.getNome() + " silenciado com sucesso.");
                        } else {
                            System.out.println("⚠️ O instrumento '" + instrumentoAtual.getNome() + "' já está parado!");
                        }
                        break;
                    }
                }

                if (encontrado == false) {
                    System.out.println("❌ Instrumento '" + instrumental + "' não encontrado na mesa.");
                }
            } else if(opcao.equals("3")){
                System.out.println("--- Lista de Instrumentos Disponíveis ---");
                for(int i=0;i<listaInstrumentos.size();i++){
                    Instrumento instrumentoAtual = listaInstrumentos.get(i);
                    
                    System.out.println("- " + instrumentoAtual.getNome());
                }
            } else if(opcao.equals("0")){
                System.out.println("Encerrando o sistema do DJ... Até logo!");
    
                scanner.close();
                System.exit(0);
            } else{
                System.out.println("Opção inválida! Por favor, digite apenas um número entre 0 e 3.");
            }
        }
    }
}