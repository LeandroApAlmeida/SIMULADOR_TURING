package turing.classes;

import java.util.Map;


/**
 * Interface que define uma Máquina de Turing determinística.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public interface MaquinaTuring {
    
    
    /**
     * Reiniciar a simulação com base na palavra de entrada.
     */
    public void reiniciar();
    
    
    /**
     * Carregar a palavra de entrada na primeira fita.
     * 
     * @param palavra palavra de entrada.
     */
    public void carregarPalavra(String palavra);
    
    
    /**
     * Executar passo da simulação.
     */
    public void executarPasso();
    
    
    /**
     * Obter o número de fitas da máquina.
     * 
     * @return Número de fitas da máquina.
     */
    public int getNumeroFitas();
    
    
    /**
     * Obter a função de transição.
     * 
     * @return Função de transição.
     */
    public FuncaoTransicao getFuncaoTransicao();

    
    /**
     * Obter o alfabeto da fita.
     * 
     * @return Alfabeto da fita.
     */
    public AlfabetoFita getAlfabetoFita();

    
    /**
     * Obter o conjunto dos estados.
     * 
     * @return conjunto dos estados.
     */
    public ConjuntoEstados getConjuntoEstados();

    
    /**
     * Obter as fitas da máquina.
     * 
     * @return fitas da máquina.
     */
    public Fita[] getFitas();

    
    /**
     * Obter os cursores para as fitas da máquina.
     * 
     * @return cursores para as fitas da máquina.
     */
    public Map<Integer, Integer> getCursores();

    
    /**
     * Obter o estado atual da Unidade de Controle.
     * 
     * @return Estado atual da Unidade de Controle.
     */
    public Estado getEstadoAtual();
    
    
    /**
     * Obter o número de passos da simulação.
     * 
     * @return Número de passos da simulação.
     */
    public int getNumeroPassos();
    
    
    /**
     * Obter a palavra de entrada.
     * 
     * @return Palavra de entrada.
     */
    public String getPalavra();
    
    
    /**
     * Estatus de palavra de entrada aceita.
     * 
     * @return Se true, a palavra foi aceita. Se false, a palavra foi rejeitada.
     */
    public boolean isAceita();
    
    
    /**
     * Adicionar um ouvinte do processo de simulação.
     * 
     * @param ouvinte ouvinte a ser adicionado.
     */
    public void adicionarOuvinte(OuvinteEtapaSimulacao ouvinte);
   
    
    /**
     * Remover um ouvinte do processo de simulação.
     * 
     * @param ouvinte ouvinte a ser removido.
     * 
     * @return Se true, o ouvinte foi removido. Se false, ele não foi removido.
     */
    public boolean removerOuvinte(OuvinteEtapaSimulacao ouvinte);
    
    
}