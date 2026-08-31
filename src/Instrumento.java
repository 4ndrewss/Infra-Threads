public class Instrumento implements Runnable {
    public static final long INTERVALO_PADRAO_MS = 1000;
    public static final long INTERVALO_MINIMO_MS = 50;
    public static final long INTERVALO_MAXIMO_MS = 5000;

    private String nome;
    private Thread thread;
    private volatile boolean pausado;
    
    // volatile porque quem escreve e a thread da Main e quem le e a thread do instrumento
    private volatile long batidas = 0;
    private volatile long intervaloMs = INTERVALO_PADRAO_MS;

    public Instrumento(String nome, boolean tocando) {
        this.nome = nome;
        this.pausado = !tocando;
    }

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

    public synchronized void parar() {
        pausado = true;
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

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
                // Le o intervalo a cada volta
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

    public void setIntervaloMs(long intervaloMs) {
        if (intervaloMs < INTERVALO_MINIMO_MS || intervaloMs > INTERVALO_MAXIMO_MS) {
            throw new IllegalArgumentException(
                    "Intervalo deve estar entre " + INTERVALO_MINIMO_MS + " e " + INTERVALO_MAXIMO_MS + " ms.");
        }
        this.intervaloMs = intervaloMs;
    }

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