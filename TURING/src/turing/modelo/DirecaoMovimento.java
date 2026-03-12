package turing.modelo;

import static turing.programa.Tokens.SIMBOLO_MOV_DIREITA;
import static turing.programa.Tokens.SIMBOLO_MOV_ESQUERDA;
import static turing.programa.Tokens.SIMBOLO_MOV_NULO;

/**
 * Enum que representa a direção do movimento da Cabeça de Leitura/Escrita. Esta
 * implementação aceita os movimentos à Direita, à Esquerda e também nulo (Parado),
 * que mantém a Cabeça de Leitura/Escrita na mesma célula.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public enum DirecaoMovimento {
    
    
    /**Movimento para a direita (D).*/
    DIREITA(SIMBOLO_MOV_DIREITA),
    
    /**Movimento para a esquerda (E).*/
    ESQUERDA(SIMBOLO_MOV_ESQUERDA),
    
    /**Nenhum movimento - parado (P).*/
    PARADO(SIMBOLO_MOV_NULO);
    
    
    /**Identificador string da direção.*/
    private final String id;

    
    /**
     * Constructor padrão.
     * 
     * @param id identificador string da direção.
     */
    private DirecaoMovimento(char id) {
        this.id = new String(new char[] {id});
    }

    
    /**
     * Obter o identificador do movimento. Os identificadores são:
     * 
     * <br><br>
     * 
     * <ul>
     * 
     * <li>&nbsp;<b>D</b>: movimento para a direita.</li><br>
     * 
     * <li>&nbsp;<b>E</b>: movimento para a esquerda.</li><br>
     * 
     * <li>&nbsp;<b>P</b>: movimento nulo (parado).</li>
     * 
     * </ul>
     * 
     * @return identificador do movimento.
     */
    public String getId() {
        return id;
    }
    
    
    /**
     * Obter a direção com base no identificador do movimento. Os identificadores
     * são:
     * 
     * <br><br>
     * 
     * <ul>
     * 
     * <li>&nbsp;<b>D</b>: movimento para a direita.</li><br>
     * 
     * <li>&nbsp;<b>E</b>: movimento para a esquerda.</li><br>
     * 
     * <li>&nbsp;<b>P</b>: movimento nulo (parado).</li>
     * 
     * </ul>
     * 
     * @param id identificador string.
     * 
     * @return Direção do movimento, ou null, caso o identificador seja inválido.
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