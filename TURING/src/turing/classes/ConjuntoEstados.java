package turing.classes;

/**
 * Conjunto de todos os estados da máquina de Turing (Q).
 * 
 * @author Leandro Ap. de Almeida
 */
public class ConjuntoEstados extends ListaEstados {

    
    @Override
    public boolean inserirEstado(Estado estado) {
        if (super.inserirEstado(estado)) {
            //ordenar();
            return true;
        } else {
            return false;
        }
    }

    
    /**
     * Obter o conjunto dos estados que não são terminais.
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
        //ordenar();
    }
    
    
    /**
     * Marcar/desmarcar um estado como terminal.
     * @param estado estado.
     * @param valor Se true, o estado é terminal. Se false, não é terminal.
     */
    public void setEstadoTerminal(Estado estado, boolean valor) {
        for (Estado estado2 : estados) {
            if (estado.equals(estado2)) {
                estado.setTerminal(valor);
            }
        }
        //ordenar();
    }
    
    
    @Override
    public void ordenar() {
        
        ListaEstados estadosTerminais = getEstadosTerminais();
        
        ListaEstados estadosNTerminais = getEstadosNaoTerminais();
        
        estadosTerminais.ordenar();
        
        estadosNTerminais.ordenar();
        
        estados.clear();
        
        estados.addAll(estadosNTerminais.estados);
        
        estados.addAll(estadosTerminais.estados);
        
        int indice = -1;
        
        for (int i = 0; i < estados.size(); i++) {
            if (estados.get(i).isInicial()) {
                indice = i;
                break;
            }
        }
        
        if (indice > 0) {
            Estado estado = estados.get(indice);
            estados.remove(estado);
            estados.add(0, estado);
        }
       
    }
    
    
}