package turing.classes;

import java.util.Map;


public interface MaquinaTuring {
    
    public void reiniciar();
    
    public void carregarPalavra(String palavra);
    
    public void executarPasso();
    
    public int getNumeroFitas();
    
    public FuncaoTransicao getFuncaoTransicao();

    public AlfabetoFita getAlfabetoFita();

    public ConjuntoEstados getConjuntoEstados();

    public Fita[] getFitas();

    public Map<Integer, Integer> getCursores();

    public Estado getEstadoAtual();
    
    public int getNumeroPassos();
    
    public String getPalavra();
    
    public boolean isAceita();
    
    public void adicionarOuvinte(OuvinteEtapaSimulacao ouvinte);
    
    public boolean removerOuvinte(OuvinteEtapaSimulacao ouvinte);
    
}