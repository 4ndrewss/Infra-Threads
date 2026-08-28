public class Instrumento implements Runnable {
    public static final long INTERVALO_PADRAO_MS = 1000;
    public static final long INTERVALO_MINIMO_MS = 50;
    public static final long INTERVALO_MAXIMO_MS = 5000;

    private String nome;
    private Thread thread;
    private volatile boolean pausado;
    private volatile long batidas;
    // volatile porque quem escreve e a thread da Main e quem le e a thread do
    // instrumento: sem isso ela poderia ficar com o valor velho em cache.
    private volatile long intervaloMs = INTERVALO_PADRAO_MS;

    public Instrumento(String nome, boolean tocando) {
        this.nome = nome;
        this.pausado = !tocando;
    }

    /**
     * Inicia a thread do instrumento. Chamar mais de uma vez nao tem efeito.
     */
    public synchronized void iniciar() {
        if (thread == null) {
            thread = new Thread(this, nome);
            thread.start();
        }
    }

    public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void retomar() {
        pausado = false;
        notify();
    }

    /**
     * Encerra a thread do instrumento. O interrupt tira a thread do wait() ou
     * do sleep(), e o run() sai do laco.
     */
    public synchronized void parar() {
        pausado = true;

        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    /**
     * O instrumento nao imprime nada: quem escreve no console e o Dashboard.
     * Aqui so contamos as batidas enquanto ele estiver tocando.
     */
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (pausado) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            batidas++;

            try {
                // Le o intervalo a cada volta, entao um "bpm bateria 500" ja
                // vale na proxima batida.
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public long getBatidas() {
        return batidas;
    }

    public long getIntervaloMs() {
        return intervaloMs;
    }

    /**
     * Muda a velocidade do instrumento em tempo de execucao. O valor e o
     * intervalo entre batidas, em milissegundos: quanto menor, mais rapido.
     */
    public void setIntervaloMs(long intervaloMs) {
        if (intervaloMs < INTERVALO_MINIMO_MS || intervaloMs > INTERVALO_MAXIMO_MS) {
            throw new IllegalArgumentException(
                    "Intervalo deve estar entre " + INTERVALO_MINIMO_MS + " e " + INTERVALO_MAXIMO_MS + " ms.");
        }

        this.intervaloMs = intervaloMs;
    }

    /**
     * Batidas por minuto correspondentes ao intervalo atual, so para exibir.
     */
    public long getBpm() {
        return 60000 / intervaloMs;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isTocando() {
        return !pausado;
    }
}
