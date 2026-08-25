public class Instrumento {
    private String nome;
    private boolean tocando;

    public Instrumento(String nome, boolean tocando) {
        this.nome = nome;
        this.tocando = tocando;
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
