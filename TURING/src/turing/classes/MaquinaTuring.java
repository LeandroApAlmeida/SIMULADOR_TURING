package turing.classes;


public interface MaquinaTuring {
    
    public void reiniciar();
    
    public void carregarPalavra(String palavra);
    
    public void executarPasso();
    
    public void adicionarOuvinte(OuvinteEtapa ouvinte);
    
    public boolean removerOuvinte(OuvinteEtapa ouvinte);
    
}