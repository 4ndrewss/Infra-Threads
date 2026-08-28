public class Instrumento implements Runnable {
    private String nome;
    private Thread thread;
    private volatile boolean pausado;
    private volatile long batidas;

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public long getBatidas() {
        return batidas;
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
