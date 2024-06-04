package turing.classes;

/**
 * Conjunto de todos os estados da máquina de Turing (Q). Pertence ao conjunto
 * dos estados o estado inicial (q<sub>0</sub>) e o conjuntos dos estados
 * terminais (F).
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class ConjuntoEstados extends ListaEstados {

    
    /**
     * Obter o conjunto dos estados que não são terminais.
     * 
     * @return conjunto dos estados que não são terminais.
     */
    public ListaEstados getEstadosNaoTerminais() {
        ListaEstados estadosNaoTerminais = new ListaEstados();
        for (Estado estado : estados) {
            if (!estado.isTerminal()) {
                estadosNaoTerminais.inserirEstado(estado);
            }
        }
        return estadosNaoTerminais;
    }
    
    
    /**
     * Obter o conjunto dos estados terminais.
     * 
     * @return conjunto dos estados terminais.
     */
    public ListaEstados getEstadosTerminais() {
        ListaEstados estadosTerminais = new ListaEstados();
        for (Estado estado : estados) {
            if (estado.isTerminal()) {
                estadosTerminais.inserirEstado(estado);
            }
        }
        return estadosTerminais;
    }
    
    
    /**
     * Obter o estado inicial.
     * 
     * @return estado inicial.
     */
    public Estado getEstadoInicial() {
        Estado estadoInicial = null;
        for (Estado estado : estados) {
            if (estado.isInicial()) {
                estadoInicial = estado;
                break;
            }
        }
        return estadoInicial;
    }

    
    /**
     * Definir o estado inicial.
     * 
     * @param estadoInicial estado inicial.
     */
    public void setEstadoInicial(Estado estadoInicial) {
        for (Estado estado : estados) {
            if (estado.equals(estadoInicial)) {
                estado.setInicial(true);
            } else {
                estado.setInicial(false);
            }
        }
    }
    
    
    /**
     * Marcar/desmarcar um estado como terminal.
     * 
     * @param estado estado.
     * 
     * @param valor Se true, o estado é terminal. Se false, não é terminal.
     */
    public void setEstadoTerminal(Estado estado, boolean valor) {
        for (Estado estado2 : estados) {
            if (estado.equals(estado2)) {
                estado.setTerminal(valor);
            }
        }
    }
    
    
}