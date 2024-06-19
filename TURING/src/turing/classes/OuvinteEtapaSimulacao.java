package turing.classes;

import java.util.Map;

/**
 * Interface que define um ouvinte de simulação de Máquina de Turing.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public interface OuvinteEtapaSimulacao {
    
    
    /**
     * Atualizar a etapa atual de simulação.
     * 
     * @param estadoAtual estado atual da Unidade de controle
     * 
     * @param fitas fitas da Máquina de Turing.
     * 
     * @param cursores cursores para as fitas da Máquina de Turing.
     * 
     * @param indiceTransicaoAtual índice da transição a ser executada no
     * passo atual. Caso seja -1, não foi encontrada uma transição.
     * 
     * @param numeroPassos número de passos da simulação.
     * 
     * @param palavraAceita estatus de palavra de entrada aceita. Se true, a
     * palavra é aceita. Se false, a palavra é rejeitada.
     * 
     * @param finalizado estatus de simulação finalizada. Se true, a simulação
     * foi finalizada. Se false, a simulação não foi finalizada.
     */
    public void atualizarEtapaSimulacao(Estado estadoAtual, Fita[] fitas, 
    Map<Integer, Integer> cursores, int indiceTransicaoAtual, 
    int numeroPassos, boolean palavraAceita, boolean finalizado);
    
    
}