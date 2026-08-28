import java.util.List;

/**
 * Thread separada de monitoramento: a cada 2 segundos limpa a tela e imprime o
 * status de todos os instrumentos da mesa.
 *
 * Roda como daemon para nao segurar o programa aberto quando a Main terminar.
 */
public class Dashboard implements Runnable {
    private static final long INTERVALO_MS = 2000;
    private static final String LIMPAR_TELA = "\033[H\033[2J\033[3J";

    private final MesaDeSom mesa;
    private volatile boolean ativo;
    private Thread thread;

    public Dashboard(MesaDeSom mesa) {
        this.mesa = mesa;
    }

    /**
     * Liga o monitoramento. Chamar mais de uma vez nao tem efeito.
     */
    public synchronized void iniciar() {
        if (thread == null) {
            ativo = true;
            thread = new Thread(this, "dashboard");
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * Desliga o monitoramento e libera a tela para o menu.
     */
    public synchronized void parar() {
        ativo = false;

        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    @Override
    public void run() {
        while (ativo) {
            imprimir(mesa.listar());

            try {
                Thread.sleep(INTERVALO_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
     * Monta o quadro inteiro em memoria e joga no console de uma vez so, para
     * o refresh nao aparecer picotado na tela.
     */
    private void imprimir(List<Instrumento> instrumentos) {
        StringBuilder quadro = new StringBuilder();

        quadro.append(LIMPAR_TELA);
        quadro.append("=================================================\n");
        quadro.append("   DASHBOARD AO VIVO - MESA DE DJ\n");
        quadro.append("=================================================\n");

        if (instrumentos.isEmpty()) {
            quadro.append("   Nenhum instrumento na mesa.\n");
        } else {
            quadro.append(String.format("   %-14s %-10s %-8s %s%n", "INSTRUMENTO", "STATUS", "BPM", "BATIDAS"));
            quadro.append("-------------------------------------------------\n");

            for (Instrumento instrumento : instrumentos) {
                String status = instrumento.isTocando() ? "TOCANDO" : "PARADO";
                String icone = instrumento.isTocando() ? "🎵" : "⏸";

                quadro.append(String.format(
                        "%s  %-14s %-10s %-8d %d%n",
                        icone,
                        instrumento.getNome(),
                        status,
                        instrumento.getBpm(),
                        instrumento.getBatidas()));
            }
        }

        quadro.append("=================================================\n");
        quadro.append("   Instrumentos: ").append(instrumentos.size());
        quadro.append("   |   atualiza a cada 2s\n");
        quadro.append("=================================================\n");
        quadro.append("Pressione ENTER para voltar ao menu.\n");

        System.out.print(quadro);
        System.out.flush();
    }
}