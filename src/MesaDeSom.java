import java.util.ArrayList;
import java.util.List;

/**
 * Guarda a lista de instrumentos compartilhada entre as threads.
 *
 * A Main altera a lista (adicionar/remover) enquanto a thread do Dashboard le
 * a lista para imprimir o status, entao todo acesso passa por metodo
 * synchronized. Sem isso a leitura poderia pegar a lista no meio de uma
 * alteracao e quebrar com ConcurrentModificationException.
 */
public class MesaDeSom {
    private final List<Instrumento> instrumentos = new ArrayList<>();

    public synchronized void adicionar(Instrumento instrumento) {
        instrumentos.add(instrumento);
    }

    public synchronized boolean remover(String nome) {
        Instrumento instrumento = buscar(nome);

        if (instrumento == null) {
            return false;
        }

        instrumento.parar();
        return instrumentos.remove(instrumento);
    }

    public synchronized Instrumento buscar(String nome) {
        for (Instrumento instrumento : instrumentos) {
            if (instrumento.getNome().equalsIgnoreCase(nome)) {
                return instrumento;
            }
        }

        return null;
    }

    /**
     * Devolve uma copia da lista. Assim quem chama (o Dashboard) itera fora do
     * lock, e a Main continua livre para alterar a lista original.
     */
    public synchronized List<Instrumento> listar() {
        return new ArrayList<>(instrumentos);
    }

    public synchronized int quantidade() {
        return instrumentos.size();
    }

    public synchronized void pararTodos() {
        for (Instrumento instrumento : instrumentos) {
            instrumento.parar();
        }
    }
}
