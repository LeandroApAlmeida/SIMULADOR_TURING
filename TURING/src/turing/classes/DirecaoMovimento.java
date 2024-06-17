package turing.classes;

/**
 * Direção do movimento da cabeça de leitura. Esta implementação aceita os
 * movimentos à Direita, à Esquerda e também Parado, que mantém a Cabeça de
 * Leitura/Escrita na mesma célula.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public enum DirecaoMovimento {
    
    
    /**Movimento para a direita (D).*/
    DIREITA("D"),
    
    /**Movimento para a esquerda (E).*/
    ESQUERDA("E"),
    
    /**Nenhum movimento - parado (P).*/
    PARADO("P");
    
    
    /**Identificador string da direção.*/
    private final String id;

    
    /**
     * Constructor padrão.
     * 
     * @param id identificador string da direçao.
     */
    private DirecaoMovimento(String id) {
        this.id = id;
    }

    
    /**
     * Obter o identificador string da direçao.
     * 
     * @return identificador string da direção.
     */
    public String getId() {
        return id;
    }
    
    
    /**
     * Obter a direção com base no identificador string.
     * 
     * @param id identificador string.
     * 
     * @return Direção do movimento, ou null, caso o identificador string
     * seja inválido.
     */
    public static DirecaoMovimento getDirecao(String id) {
        DirecaoMovimento direcao = null;
        if (id.equals(DIREITA.id)) {
            direcao = DIREITA;
        } else if (id.equals(ESQUERDA.id)) {
            direcao = ESQUERDA;
        } else if (id.equals(PARADO.id)) {
            direcao = PARADO;
        }
        return direcao;
    }
    
    
}