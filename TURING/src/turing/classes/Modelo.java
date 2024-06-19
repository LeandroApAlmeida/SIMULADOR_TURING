package turing.classes;

/**
 * Modelo de máquina de Turing utilizado. Como há muitas variações, implemento
 * dois modelos determinísticos específicos.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public enum Modelo {
    
    
    /**Modelo padrão, determinístico, com uma fita infinita à esquerda à direita*/
    PADRAO("padrao"),
    
    /**Modelo com k fitas, determinístico, cada fita sendo infinita à esquerda e à direita.*/
    MULTIFITAS("multifitas");
    
    
    /**Código do modelo.*/
    private final String codModelo;

    
    /**
     * Constructor padrão.
     * 
     * @param codModelo código do modelo.
     */
    private Modelo(String codModelo) {
        this.codModelo = codModelo;
    }
    
    
    /**
     * Obter o código do modelo.
     * 
     * @return código do modelo.
     */
    public String getCodigo() {
        return codModelo;
    }
    
    
    /**
     * Obter o modelo a partir do código.
     * 
     * @param codModelo código do modelo.
     * 
     * @return modelo da máquina de Turing, ou null, caso o código seja
     * inválido.
     */
    public static Modelo getModelo(String codModelo) {
        Modelo modelo = null;
        if (codModelo.equals(PADRAO.codModelo)) {
            modelo = PADRAO;
        } else if (codModelo.equals(MULTIFITAS.codModelo)) {
            modelo = MULTIFITAS;
        }
        return modelo;
    }
    
    
}
