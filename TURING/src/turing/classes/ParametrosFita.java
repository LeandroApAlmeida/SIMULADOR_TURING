package turing.classes;

/**
 * Parâmetros para o cursor da Cabeça de Leitura/Escrita na fita em uma transição.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class ParametrosFita {
    
    
    /**Símbolo lido da fita.*/
    private final Simbolo simboloLido;
    
    /**Símbolo escrito na fita.*/
    private final Simbolo simboloEscrito;
    
    /**Direção do movimento da cabeça de leitura.*/
    private final DirecaoMovimento direcaoMovimento;

    
    /**
     * Constructor padrão.
     * 
     * @param simboloLido símbolo lido da fita.
     * 
     * @param simboloEscrito símbolo escrito na fita.
     * 
     * @param direcaoMovimento direção do movimento da cabeça de leitura.
     */
    public ParametrosFita(Simbolo simboloLido, Simbolo simboloEscrito, 
    DirecaoMovimento direcaoMovimento) {
        this.simboloLido = simboloLido;
        this.simboloEscrito = simboloEscrito;
        this.direcaoMovimento = direcaoMovimento;
    }

    
    /**
     * Obter o símbolo lido da fita.
     * 
     * @return símbolo lido da fita.
     */
    public Simbolo getSimboloLido() {
        return simboloLido;
    }

    
    /**
     * Obter o símbolo escrito na fita.
     * 
     * @return símbolo escrito na fita.
     */
    public Simbolo getSimboloEscrito() {
        return simboloEscrito;
    }

    
    /**
     * Obter a direção do movimento da cabeça de leitura.
     * 
     * @return direção do movimento da cabeça de leitura.
     */
    public DirecaoMovimento getDirecaoMovimento() {
        return direcaoMovimento;
    }
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(simboloLido.toString());
        sb.append(", ");
        sb.append(simboloEscrito.toString());
        sb.append(", ");
        sb.append(direcaoMovimento.getId());
        sb.append("]");
        return sb.toString();
    }

    
}
