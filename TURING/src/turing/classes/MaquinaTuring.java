package turing.classes;


public interface MaquinaTuring {
    
    public void reiniciar();
    
    public void carregarPalavra(String palavra);
    
    public void executarPasso();
    
    public void adicionarOuvinte(OuvinteMaqTuring ouvinte);
    
    public boolean removerOuvinte(OuvinteMaqTuring ouvinte);
    
}