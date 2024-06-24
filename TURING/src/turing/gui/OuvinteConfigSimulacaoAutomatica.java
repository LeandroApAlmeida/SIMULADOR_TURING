package turing.gui;


/**
 * Interface que define um ouvinte de configuração de simulação automática. A
 * simulação automática ocorre quando o código que executa cada passo da 
 * simulação, ou seja, a aplicação da função de transição para a palavra de
 * entrada a partir do estado q<sub>0</sub>, é executado dentro do Thread de um 
 * {@link java.util.Timer}, sendo que este Timer executa o código do Thread
 * em um intervalo de tempo pré-definido, até ser parado.
 * 
 * <br><br>
 * 
 * Pode ocorrer a configuração para que o intervalo de tempo do Timer seja maior 
 * ou menor, diminuindo ou aumentando a velocidade de simulação, respectivamente,
 * e quando isto acontecer, a classe que implementa esta interface será notificada
 * sobre esta mudança, tendo de reconfigurar o timer para refletir o novo estado.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public interface OuvinteConfigSimulacaoAutomatica {
    
    
    /**
     * Notifica o ouvinte que o tempo de execução do Thread da classe
     * {@link java.util.Timer} foi modificado. Por exemplo:
     * 
     * <br><br>
     * 
     * <blockquote>
     * 
     * Anterior: 2000 (2.000 ms = 2 s)
     * 
     * <br>
     * 
     * Novo: 500 (500 ms = 0,5 s)
     * 
     * </blockquote>
     * 
     * <br>
     * 
     * Neste caso, o novo valor de tempo de execução (500 ms) será passado no 
     * parâmetro <b>novoValor</b> do método, e a classe que implementa esta 
     * interface deverá descartar o Timer atual e gerar outro com base neste 
     * novo parâmetro.
     * 
     * @param novoValor novo valor para o tempo de execução do Thread.
     */
    public void velocidadeSimulacaoAutomaticaAtualizada(int novoValor);
    
    
}
