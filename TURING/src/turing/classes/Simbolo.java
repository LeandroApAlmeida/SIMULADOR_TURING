package turing.classes;

/**
 * Classe que representa um símbolo do alfabeto da fita.
 * 
 * @author Leandro Ap. de Almeida
 */
public class Simbolo {
    
    
    /**Caractere do símbolo.*/
    private char caractere;
    
    /**Estatus de símbolo do alfabeto auxiliar.*/
    private boolean auxiliar;
    
    /**Estatus de símbolo reservado.*/
    private boolean reservado;
    
    /**Indicador de ponto virtual sobre o símbolo.*/
    private boolean ponto;

    
    /**
     * Constructor padrão.
     * 
     * @param caracter caractere do símbolo.
     * 
     * @param auxiliar estatus de símbolo do alfabeto auxiliar.
     * 
     * @param reservado estatus de símbolo reservado.
     */
    protected Simbolo(char caracter, boolean auxiliar, boolean reservado) {
        this.caractere = caracter;
        this.auxiliar = auxiliar;
        this.reservado = reservado;
        this.ponto = false;
    }
    
    
    /**
     * Constructor de símbolo não reservado.
     * 
     * @param caracter caractere do símbolo.
     * 
     * @param auxiliar estatus de símbolo do alfabeto auxiliar.
     */
    public Simbolo(char caracter, boolean auxiliar) {
        this(caracter, auxiliar, false);
    }

    
    /**
     * Obter o caractere do símbolo.
     * 
     * @return caractere do símbolo.
     */
    public char getCaracter() {
        return caractere;
    }

    
    /**
     * Obter o estatus de símbolo do alfabeto auxiliar.
     * 
     * @return Se true, é símbolo do alfabeto auxiliar. Se false, não é
     * símbolo do alfabeto auxiliar.
     */
    public boolean isAuxiliar() {
        return auxiliar;
    }

    
    /**
     * Obter o estatus de símbolo reservado.
     * 
     * @return Se true, é símbolo reservado. Se false, não é símbolo reservado.
     */
    public boolean isReservado() {
        return reservado;
    }

    
    /**
     * Obter a indicação de que o ponto virtual está sobre o símbolo.
     * 
     * @return Se true, o ponto virtual está sobre o símbolo. Se false, o ponto
     * virtual não está sobre o símbolo.
     */
    public boolean isPonto() {
        return ponto;
    }

    
    /**
     * Definir o caractere do símbolo.
     * 
     * @param caracter caractere do símbolo.
     */
    protected void setCaracter(char caracter) {
        if (!reservado) {
            this.caractere = caracter;
        }
    }

    
    /**
     * Definir o estatus de símbolo do alfabeto auxiliar.
     * 
     * @param auxiliar Se true, é símbolo do alfabeto auxiliar. Se false, não é
     * símbolo do alfabeto auxiliar.
     */
    protected void setAuxiliar(boolean auxiliar) {
        if (!reservado) {
            this.auxiliar = auxiliar;
        }
    }

    
    /**
     * Definir que o ponto virtual está sobre o símbolo.
     * 
     * @param ponto Se true, o ponto virtual está sobre o símbolo. Se false,
     * o ponto virtual não está sobre o símbolo.
     */
    protected void setPonto(boolean ponto) {
        this.ponto = ponto;
    }


    /**
     * Sobrescrito para usar o caractere do símbolo como critério de igualdade.
     * 
     * @param obj objeto a ser comparado.
     * 
     * @return Se true, o objeto passado é o mesmo. Se false, o objeto passado
     * não é o mesmo.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Simbolo simbolo) {
            return simbolo.caractere == this.caractere;
        } else {
            return false;
        }
    }

    
    /**
     * Sobrescrito para retornar o caractere do símbolo.
     * @return caractere do símbolo.
     */
    @Override
    public String toString() {
        return String.valueOf(caractere);
    }
    
    
    public static int compare(Simbolo s1, Simbolo s2) {
        return (int)s1.getCaracter() - (int)s2.caractere;
    }

    
}
