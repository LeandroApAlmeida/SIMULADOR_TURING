package turing.classes;

import java.util.Map;

public interface OuvinteEtapaSimulacao {
    
    public void atualizarEtapaSimulacao(Estado estadoAtual, Fita[] fitas, 
    Map<Integer, Integer> cursores, int indiceTransicaoAtual, 
    int numeroPassos, boolean cadeiaAceita, boolean finalizado);
    
}