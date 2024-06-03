package turing.classes;

/**
 * Configurações para montagem de uma máquina de Turing. Inclui todos os parâmetros
 * da máquina como o alfabeta da fita, o conjunto dos estados, a função de transição.
 * 
 * @author Leandro Ap. de Almeida
 */
public class ConfigMaqTuring {
    
    
    /**Nome.*/
    private final String nome;
    
    /**Alfabeto da fita.*/
    private final AlfabetoFita alfabetoFita;
    
    /**Estados*/
    private final ConjuntoEstados conjuntoEstados;
    
    /**Função de transição.*/
    private final FuncaoTransicao funcaoTransicao;
    
    /**Número de fitas.*/
    private final int numeroFitas;

    
    /**
     * Constructor padrão.
     * 
     * @param nome nome.
     * 
     * @param alfabetoFita alfabeto da fita.
     * 
     * @param conjuntoEstados estados.
     * 
     * @param funcaoTransicao função de transição.
     * 
     * @param numeroFitas número de fitas.
     * 
     * @param modelo modelo da máquina de Turing.
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
     * Obter o nome.
     * @return nome.
     */
    public String getNome() {
        return nome;
    }

    
    /**
     * Obter o alfabeto da fita.
     * @return alfabeto da fita.
     */
    public AlfabetoFita getAlfabetoFita() {
        return alfabetoFita;
    }

    
    /**
     * Obter o conjunto estados.
     * @return conjunto dos estados.
     */
    public ConjuntoEstados getConjuntoEstados() {
        return conjuntoEstados;
    }

    
    /**
     * Obter a função de transição.
     * @return função de transição.
     */
    public FuncaoTransicao getFuncaoTransicao() {
        return funcaoTransicao;
    }

    
    /**
     * Obter o número de fitas.
     * @return número de fitas.
     */
    public int getNumeroFitas() {
        return numeroFitas;
    }

    
}