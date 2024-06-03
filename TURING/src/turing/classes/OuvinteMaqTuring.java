package turing.classes;

import java.util.Map;

public interface OuvinteMaqTuring {
    
    public void atualizarMaqTuring(Estado estadoAtual, Fita[] fitas, 
    Map<Integer, Integer> cursores, int indiceTransicaoAtual, 
    int numeroPassos, boolean cadeiaAceita, boolean cadeiaRejeitada);
    
}