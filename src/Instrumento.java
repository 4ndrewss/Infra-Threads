public class Instrumento implements Runnable {
    private String nome;
    private volatile boolean tocando;
    private volatile long batidas;
    private Thread thread;

    public Instrumento(String nome, boolean tocando) {
        this.nome = nome;
        this.tocando = tocando;
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

    /**
     * Encerra a thread do instrumento.
     */
    public synchronized void parar() {
        tocando = false;

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
            if (tocando) {
                batidas++;
            }

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
        return tocando;
    }

    public void setTocando(boolean tocando) {
        this.tocando = tocando;
    }
}
