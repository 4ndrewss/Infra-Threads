public class Instrumento implements Runnable {
    private String nome;
    private volatile boolean tocando;
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

    @Override
    public void run() {
        while (true) {
            System.out.println("🎵 " + nome + " tocando...");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
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
