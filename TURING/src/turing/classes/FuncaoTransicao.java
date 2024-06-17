package turing.classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Função de transição para a máquina de Turing (δ). 
 * 
 * <br><br>
 * 
 * Traçando um paralelo com um computador real, a função de transição seria o 
 * programa executado, que faz o computador realizar alguma tarefa.
 * 
 * <br><br>
 * 
 * A função de transição:
 * 
 * <br><br>
 * 
 * considera
 * 
 * <ul>
 * <li>estado corrente</li>
 * <li>símbolo(s) lido da(s) fita(s).</li>
 * </ul>
 * 
 * <br>
 * 
 * Para determinar
 * 
 * <ul>
 * <li>novo estado</li>
 * <li>símbolo(s) a ser(em) gravado(s)</li>
 * <li>sentido de movimento da(s) cabeça(s) de leitura, onde esquerda é representado
 * por <b>E</b>, direita é representado por <b>D</b> e parado por <b>P</b>.</li>
 * </ul>
 * 
 * <br>
 * 
 * Assim, tem-se que:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * δ : Q × Γ<sup>k</sup> → Q × Γ<sup>k</sup> × {E, D, P}<sup>k</sup>
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Em outros termos:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * δ(E<sub>Atual</sub>, S<sub>1</sub>, S<sub>2</sub>, ... , S<sub>k</sub>) = 
 * (E<sub>Novo</sub>, G<sub>1</sub>, G<sub>2</sub>, ... , G<sub>k</sub>, 
 * M<sub>1</sub>, M<sub>2</sub>, ... , M<sub>k</sub>)
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Onde:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * <b>E<sub>Atual</sub></b>: Estado atual.
 * 
 * <br><br>
 * 
 * <b>S<sub>n</sub></b>: Símbolo lido da fita n.
 * 
 * <br><br>
 * 
 * <b>E<sub>Novo</sub></b>: Novo estado.
 * 
 * <br><br>
 * 
 * <b>G<sub>n</sub></b>: Símbolo gravado na fita n.
 * 
 * <br><br>
 * 
 * <b>M<sub>n</sub></b>: Sentido do movimento da cabeça de leitura na fita n
 * 
 * <br><br>
 * 
 * <b>k</b>: Número de fitas da máquina de Turing.
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Estas transições configuram o estado da máquina, e fazem a Cabeça de
 * Leitura/Escrita ler/gravar símbolos e se mover para alguma direção. Em certo
 * sentido, elas funcionam como instruções em linguagem de baixo nível em 
 * computadores reais, gravando conteúdo em memória e buscando novas instruções
 * para serem executadas até o programa ser encerrado (ou entrar em LOOP).
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class FuncaoTransicao implements Iterable<Transicao> {
    
    
    /**Transições da função de transição.*/
    private final List<Transicao> transicoes;

    
    /**
     * Constructor padrão.
     */
    public FuncaoTransicao() {
        transicoes = new ArrayList<>();
    }


    /**
     * Adicionar uma transição.
     * 
     * @param transicao transição a ser adicionada.
     * 
     * @return Se true, a transição foi adicionada. Se false, a transição não
     * foi adicionada.
     */
    public boolean adicionarTransicao(Transicao transicao) {
        if (!transicoes.contains(transicao)) {
            transicoes.add(transicao);
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * Remover uma transição.
     * 
     * @param transicao transição a ser removida.
     * 
     * @return Se true, a transição foi removida. Se false, a transição não
     * foi removida.
     */
    public boolean removerTransicao(Transicao transicao) {
        return transicoes.remove(transicao);
    }
    
    
    /**
     * Deslocar a transição para cima.
     * 
     * @param indice índice da transição.
     * 
     * @return Se true, a transição foi deslocada. Se false, a transição não
     * foi deslocada.
     */
    public boolean moverTransicaoParaCima(int indice) {
        if (indice > 0) {
            Transicao transicaoAux = transicoes.get(indice);
            transicoes.set(indice, transicoes.get(indice-1));
            transicoes.set(indice - 1, transicaoAux);
            return true;
        } else {
            return false;
        } 
    }
    
    
    /**
     * Deslocar a transição para baixo.
     * 
     * @param indice índice da transição.
     * 
     * @return Se true, a transição foi deslocada. Se false, a transição não
     * foi deslocada.
     */
    public boolean moverTransicaoParaBaixo(int indice) {
        if (indice < transicoes.size() - 1) {
            Transicao transicaoAux = transicoes.get(indice);
            transicoes.set(indice, transicoes.get(indice+1));
            transicoes.set(indice + 1, transicaoAux);
            return true;
        } else {
            return false;
        } 
    }
    
    
    /**
     * Obter a transição identificada pelo estado inicial e os símbolos lidos
     * nas fitas.
     * 
     * @param estadoInicial estado inicial.
     * 
     * @param simbolosLidos símbolos lidos da fita.
     * 
     * @return Transição relativa ao estado inicial e símbolos, ou null, caso
     * não exista uma transição com esta identificação.
     */
    public Transicao getTransicao(Estado estadoInicial, char... simbolosLidos) {
        
        Transicao transicao = null;
        
        StringBuilder sb = new StringBuilder();
        sb.append(estadoInicial.getRotulo());
        sb.append(simbolosLidos);
        
        for (int i = 0; i < transicoes.size(); i++) {
            
            Transicao transicao2 = transicoes.get(i);
            
            StringBuilder sb2 = new StringBuilder();
            sb2.append(transicao2.getEstadoInicial().getRotulo());
            
            for (ParametrosFita paramsFita : transicao2.getParametrosFitas()) {
                sb2.append(paramsFita.getSimboloLido());
            }

            if (sb2.toString().equals(sb.toString())) {
                transicao = transicao2;
                break;
            }
            
        }        

        return transicao;
        
    }
    
    
    /**
     * Obter a transição no índice especificado.
     * 
     * @param indice índice da transição.
     * 
     * @return Transição no índice especificado, ou null, caso o índice esteja
     * fora da faixa.
     */
    public Transicao getTransicao(int indice) {
        if (indice >= 0 && indice < transicoes.size()) {
            return transicoes.get(indice);
        } else {
            return null;
        }
    }
    
    
    /**
     * Obter o número total de transições na função de transição.
     * @return Número total de transições.
     */
    public int getComprimento() {
        return transicoes.size();
    }
    
    
    /**
     * Remover todas as transições da função de transição.
     */
    public void esvaziar() {
        transicoes.clear();
    }
    
    
    public int indiceDe(Estado estado, char... simbolos) {
        int indiceTransicao = -1;
        boolean erro;
        for (int i = 0; i < transicoes.size(); i++) {
            Transicao transicao2 = transicoes.get(i);
            if (transicao2.getEstadoInicial().equals(estado)) {
                if (simbolos.length == transicao2.getParametrosFitas().size()) {
                    erro = false;
                    for (int j = 0; j < simbolos.length; j++) {
                        if (simbolos[j] != transicao2.getParametrosFitas()
                        .get(j).getSimboloLido().getCaracter()) {
                            erro = true;
                            break;
                        }
                    }
                    if (!erro) {
                        indiceTransicao = i;
                        break;
                    }
                }
            }
        }
        return indiceTransicao;
    }

    
    @Override
    public Iterator<Transicao> iterator() {
        return transicoes.iterator();
    }
    
    
}
