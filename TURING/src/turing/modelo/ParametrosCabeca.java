package turing.modelo;

/**
 * Classe para definição dos parâmetros para a Cabeça de Leitura/Escrita em uma
 * transição. Estes parâmetros são símbolo lido da fita, símbolo escrito na fita
 * e direção do movimento da Cabeça de Leitura/Escrita.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class ParametrosCabeca {
    
    
    /**Símbolo lido da fita.*/
    private final Simbolo simboloLido;
    
    /**Símbolo escrito na fita.*/
    private final Simbolo simboloEscrito;
    
    /**Direção do movimento da cabeça de leitura/escrita.*/
    private final DirecaoMovimento direcaoMovimento;

    
    /**
     * Constructor padrão.
     * 
     * @param simboloLido símbolo lido da fita.
     * 
     * @param simboloEscrito símbolo escrito na fita.
     * 
     * @param direcaoMovimento direção do movimento da cabeça de leitura/escrita.
     */
    public ParametrosCabeca(Simbolo simboloLido, Simbolo simboloEscrito, 
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
     * Obter a direção do movimento da cabeça de leitura/escrita.
     * 
     * @return direção do movimento da cabeça de leitura/escrita.
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
