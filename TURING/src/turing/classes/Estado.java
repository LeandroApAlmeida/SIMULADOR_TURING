package turing.classes;

/**
 * Classe que representa um estado da máquina de Turing.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class Estado {
    
    
    /**Rótulo do estado.*/
    private String rotulo;
    
    /**Estatus de estado terminal.*/
    private boolean terminal;
    
    /**Estatus de estado inicial.*/
    private boolean inicial;

    
    /**
     * Constructor da classe.
     * 
     * @param rotulo rótulo do estado.
     * 
     * @param terminal estatus de estado terminal.
     * 
     * @param inicial estatus de estado inicial.
     * 
     * @throws Exception erro na sintaxe do rótulo do estado.
     */
    public Estado(String rotulo, boolean terminal, boolean inicial) throws Exception {
        if (rotuloValido(rotulo)) {
            this.rotulo = rotulo;
            this.terminal = terminal;
            this.inicial = inicial;
        } else {
            throw new Exception("Sintaxe do rótulo do estado incorreta.");
        }
    }
    
    
    /**
     * Constructor da classe.
     * 
     * @param rotulo rótulo do estado.
     * 
     * @param terminal estatus de estado terminal.
     * 
     * @throws Exception erro na sintaxe do rótulo do estado.
     */
    public Estado(String rotulo, boolean terminal) throws Exception {
        this(rotulo, terminal, false);
    }
    

    /**
     * Constructor da classe.
     * 
     * @param rotulo rótulo do estado.
     * 
     * @throws Exception erro na sintaxe do rótulo do estado.
     */
    public Estado(String rotulo) throws Exception {
        this(rotulo, false, false);
    }


    /**
     * Obter o rótulo do estado.
     * 
     * @return rótulo do estado.
     */
    public String getRotulo() {
        return rotulo;
    }

    
    /**
     * Obter o estatus de estado terminal.
     * 
     * @return Se true, o estado é terminal. Se false, o estado não é terminal.
     */
    public boolean isTerminal() {
        return terminal;
    }

    
    /**
     * Obter o estatus de estado inicial.
     * 
     * @return Se true, o estado é inicial. Se false, o estado não é inicial.
     */
    public boolean isInicial() {
        return inicial;
    }

    
    /**
     * Definir o rótulo do estado.
     * 
     * @param rotulo rótulo do estado.
     * 
     * @throws Exception erro na sintaxe do rótulo do estado.
     */
    protected boolean setRotulo(String rotulo) throws Exception {
        if (rotuloValido(rotulo)) {
            this.rotulo = rotulo;
            return true;
        } else {
            return false;
        }
    }

    
    /**
     * Definir o estatus de estado terminal.
     * 
     * @param terminal Se true, o estado é terminal. Se false, o estado não é
     * terminal.
     */
    protected void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    
    /**
     * Definir o estatus de estado inicial.
     * 
     * @param inicial Se true, o estado é inicial. Se false, o estado não é 
     * inicial.
     */
    protected void setInicial(boolean inicial) {
        this.inicial = inicial;
    }
    
    
    /**
     * Validar a sintaxe do rótulo de um estado. O rótulo de um estado deve iniciar
     * com a letra 'q', seguida de letras maiúsculas ou minúsculas e números.
     * 
     * <br><br>
     * 
     * Exemplo:
     * 
     * <br><br>
     * 
     * <BLOCKQUOTE>
     * 
     * q0, q1, qInicial, qfinal, qAceita1
     * 
     * </BLOCKQUOTE>
     * 
     * <br>
     * 
     * São exemplos de rótulos de estado incorretos:
     * 
     * <br><br>
     * 
     * <BLOCKQUOTE>
     * 
     * q, a1, q#1, qfinal*, q_0
     * 
     * </BLOCKQUOTE>
     * 
     * @param rotuloEstado rótulo do estado.
     * 
     * @return Se true, a sintaxe está correta. Se false, a sintaxe está incorreta.
     */
    public static boolean rotuloValido(String rotuloEstado) {
        
        if (!rotuloEstado.startsWith("q")) {
            return false;
        }
        
        if (rotuloEstado.length() <= 1) {
            return false;
        }

        boolean sintaxeCorreta = true;

        boolean numeral;
        boolean maiuscula;
        boolean minuscula;
        
        for (int i = 1; i < rotuloEstado.length(); i++) {
            
            int codigoUTF8 = (int) rotuloEstado.charAt(i);
            
            
            numeral = (codigoUTF8 >= 48 && codigoUTF8 <= 57);
            maiuscula = (codigoUTF8 >= 65 && codigoUTF8 <= 90);
            minuscula = (codigoUTF8 >= 97 && codigoUTF8 <= 122);
            
            if (!numeral && !maiuscula && !minuscula) {
                sintaxeCorreta = false;
                break;
            }
            
        }
        
        return sintaxeCorreta;
        
    }

    
    /**
     * Sobrescrito para usar o rótulo do estado como critério de igualdade.
     * 
     * @param obj objeto a ser comparado.
     * 
     * @return Se true, o objeto passado é o mesmo. Se false, o objeto passado
     * não é o mesmo.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Estado estado) {
            return estado.rotulo.equals(this.rotulo);
        } else {
            return false;
        }
    }

    
    /**
     * Sobrescrito para retornar o rótulo do estado.
     * @return rótulo do estado.
     */
    @Override
    public String toString() {
        return rotulo;
    }

    
}