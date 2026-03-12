package turing.modelo;

/**
 * Classe que representa uma Máquina de Turing.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class MaquinaTuring {
    
    
    /**Nome da máquina de Turing.*/
    protected final String nome;
    
    /**Alfabeto da fita.*/
    protected final AlfabetoFita alfabetoFita;
    
    /**Estados da máquina de Turing.*/
    protected final ConjuntoEstados conjuntoEstados;
    
    /**Função de transição.*/
    protected final FuncaoTransicao funcaoTransicao;
    
    /**Número de fitas.*/
    protected final int numeroFitas;

    
    /**
     * Constructor padrão.
     * 
     * @param nome nome da máquina de Turing.
     * 
     * @param alfabetoFita alfabeto da fita.
     * 
     * @param conjuntoEstados estados da máquina de Turing.
     * 
     * @param funcaoTransicao função de transição.
     * 
     * @param numeroFitas número de fitas.
     */
    public MaquinaTuring(String nome, AlfabetoFita alfabetoFita, 
    ConjuntoEstados conjuntoEstados, FuncaoTransicao funcaoTransicao,
    int numeroFitas) {
        
        this.nome = nome;
        this.alfabetoFita = alfabetoFita;
        this.conjuntoEstados = conjuntoEstados;
        this.funcaoTransicao = funcaoTransicao;
        this.numeroFitas = numeroFitas;
        
    }

    
    /**
     * Obter o nome da máquina de Turing.
     * 
     * @return nome da máquina de Turing.
     */
    public String getNome() {
        return nome;
    }

    
    /**
     * Obter o alfabeto da fita.
     * 
     * @return alfabeto da fita.
     */
    public AlfabetoFita getAlfabetoFita() {
        return alfabetoFita;
    }

    
    /**
     * Obter os estados da máquina de Turing.
     * 
     * @return estados da máquina de Turing.
     */
    public ConjuntoEstados getConjuntoEstados() {
        return conjuntoEstados;
    }

    
    /**
     * Obter a função de transição.
     * 
     * @return função de transição.
     */
    public FuncaoTransicao getFuncaoTransicao() {
        return funcaoTransicao;
    }

    
    /**
     * Obter o número de fitas.
     * 
     * @return número de fitas.
     */
    public int getNumeroFitas() {
        return numeroFitas;
    }
    
    
}