package turing.classes;

/**
 * Configurações para montagem de uma máquina de Turing. Inclui todos os parâmetros
 * da máquina como o alfabeta da fita, o conjunto dos estados e a função de transição.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class ConfigMaqTuring {
    
    
    /**Nome da máquina de Turing.*/
    private final String nome;
    
    /**Alfabeto da fita.*/
    private final AlfabetoFita alfabetoFita;
    
    /**Estados da máquina de Turing.*/
    private final ConjuntoEstados conjuntoEstados;
    
    /**Função de transição.*/
    private final FuncaoTransicao funcaoTransicao;
    
    /**Número de fitas.*/
    private final int numeroFitas;

    
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
    public ConfigMaqTuring(String nome, AlfabetoFita alfabetoFita, 
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