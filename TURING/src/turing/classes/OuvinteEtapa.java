package turing.classes;

import java.util.Map;

public interface OuvinteEtapa {
    
    public void atualizarEtapa(Estado estadoAtual, Fita[] fitas, 
    Map<Integer, Integer> cursores, int indiceTransicaoAtual, 
    int numeroPassos, boolean cadeiaAceita, boolean finalizado);
    
}