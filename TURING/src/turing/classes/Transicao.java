package turing.classes;

import java.util.List;

/**
 * Classe que representa uma transição da função de transição. Uma transição é
 * definida como:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * Q × Γ<sup>k</sup> → Q × Γ<sup>k</sup> × {E, D, P}<sup>k</sup>
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
 * (q<sub>a</sub>, s<sub>1</sub>, s<sub>2</sub>, ... , s<sub>k</sub>) = 
 * (q<sub>n</sub>, g<sub>1</sub>, g<sub>2</sub>, ... , g<sub>k</sub>, 
 * d<sub>1</sub>, d<sub>2</sub>, ... , d<sub>k</sub>)
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
 * <b>q<sub>a</sub></b>: Estado atual.
 * 
 * <br><br>
 * 
 * <b>s<sub>1</sub>, s<sub>2</sub>, ..., s<sub>k</sub></b>: Símbolos lidos das 
 * fitas.
 * 
 * <br><br>
 * 
 * <b>q<sub>n</sub></b>: Novo estado.
 * 
 * <br><br>
 * 
 * <b>g<sub>1</sub>, g<sub>2</sub>, ..., g<sub>k</sub></b>: Símbolos gravados nas
 * fitas.
 * 
 * <br><br>
 * 
 * <b>d<sub>1</sub>, d<sub>2</sub>, ..., d<sub>k</sub></b>: Sentidos dos movimentos
 * das Cabeças de Leitura/Escrita nas fitas.
 * 
 * <br><br>
 * 
 * <b>k</b>: Número de fitas da máquina de Turing.
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Para uma Máquina de Turing padrão, com uma única fita, pode ser representada
 * como:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * Q × Γ → Q × Γ × {E, D, P}
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * ou:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * (q<sub>a</sub>, s) = (q<sub>n</sub>, g, d)
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Onde:
 * 
 * <br><br>
 * 
 * <blockquote>
 * 
 * <b>q<sub>a</sub></b>: Estado atual.
 * 
 * <br><br>
 * 
 * <b>s</b>: Símbolo lido da fita.
 * 
 * <br><br>
 * 
 * <b>q<sub>n</sub></b>: Novo estado.
 * 
 * <br><br>
 * 
 * <b>g</b>: Símbolo gravado na fita.
 * 
 * <br><br>
 * 
 * <b>d</b>: Direção do movimento da Cabeça de Leitura/Escrita.
 * 
 * </blockquote>
 * 
 * <br>
 * 
 * Esta modelagem da transição de um estado para outro de acordo com o(s) símbolo(s)
 * lido(s) da fita e do estado atual foi uma decisão de projeto. A função de
 * transição, representada pela classe {@link FuncaoTransicao}, implementa uma
 * lista destas transições que configuram o funcionamento da Máquina de Turing,
 * ou, em outras palavras, o equivalente ao programa da máquina.
 * 
 * <br><br>
 * 
 * Não confundir a notação de uma transição, que eu utilizei aqui para uma Máquina
 * de Turing com múltiplas fitas como: 
 * 
 * <br><br>
 * 
 * <blockquote>
 * Q × Γ<sup>k</sup> → Q × Γ<sup>k</sup> × {E, D, P}<sup>k</sup>
 * </blockquote>
 * 
 * <br>
 * 
 * e para uma Máquina de Turing com apenas uma única fita como:
 * 
 * <br><br>
 * 
 * <blockquote>
 * Q × Γ → Q × Γ × {E, D, P}
 * </blockquote>
 * 
 * <br>
 * 
 * com a notação utilizada para descrever uma Função de Transição, representada
 * para uma Máquina de Turing com múltiplas fitas como:
 * 
 * <br><br>
 * 
 * <blockquote>
 * δ : Q × Γ<sup>k</sup> → Q × Γ<sup>k</sup> × {E, D, P}<sup>k</sup>
 * </blockquote>
 * 
 * <br>
 * 
 * e para uma máquina de Turing como apenas uma única fita como:
 * 
 * <br><br>
 * 
 * <blockquote>
 * δ : Q × Γ → Q × Γ × {E, D, P}
 * </blockquote>
 * 
 * <br>
 * 
 * Em termos computacionais, enquanto a transição é o item da lista, a Função de
 * Transição é a lista de todas as transições.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class Transicao {
    
    
    /**Estado inicial da transição.*/
    private final Estado estadoInicial;
    
    /**Estado final da transição.*/
    private final Estado estadoFinal;
    
    /**Parâmetros de leitura/escrita da fita e movimento da Cabeça de Leitura/Escrita.*/
    private final List<ParametrosFita> parametrosFita;

    
    /**
     * Constructor padrão.
     * 
     * @param estadoInicial estado inicial a transição.
     * 
     * @param estadoFinal estado final da transição.
     * 
     * @param parametrosFita Parâmetros de leitura/escrita da fita e movimento
     * da Cabeça de Leitura/Escrita. 
     */
    public Transicao(Estado estadoInicial, Estado estadoFinal, 
    List<ParametrosFita> parametrosFita) {
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
        this.parametrosFita = parametrosFita;
    }

    
    /**
     * Obter o estado inicial da transição.
     * 
     * @return estado inicial da transição.
     */
    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    
    /**
     * Obter o estado final da transição.
     * 
     * @return estado final da transição.
     */
    public Estado getEstadoFinal() {
        return estadoFinal;
    }

    
    /**
     * Obter os parâmetros de leitura/escrita das fitas e a direção de movimento
     * da Cabeça de Leitura/Escrita.
     * 
     * @return parâmetros para a fita.
     */
    public List<ParametrosFita> getParametrosFita() {
        return parametrosFita;
    }

    
    /**
     * Reescrito para o critério de igualdade ser relativo ao estado atual e o(s)
     * símbolo(s) lido(s) da(s) fita(s). Estes dados indicam unicamente uma 
     * transição da Função de  Transição.
     * 
     * @param obj objeto a ser comparado.
     * 
     * @return Se true, o objeto comparado é equivalente ao objeto desta instância.
     * Se false, os objetos são diferentes.
     */
    @Override
    public boolean equals(Object obj) {
        
        if (obj == this) return true;
        
        if (obj instanceof Transicao transicao) {
            if (transicao.getEstadoInicial().equals(estadoInicial)) {
                boolean igual = true;
                for (int i = 0; i < parametrosFita.size(); i++) {
                    if (!parametrosFita.get(i).getSimboloLido().equals(transicao
                    .getParametrosFita().get(i).getSimboloLido())) {
                        igual = false;
                        break;
                    }
                }
                return igual;
            } else {
                return false;
            }
        } else {
            return false;
        }
 
    }

    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append(estadoInicial.getRotulo());

        for (int i = 0; i < parametrosFita.size(); i++) {
            sb.append(", ");
            sb.append(parametrosFita.get(i).getSimboloLido().getCaracter());
        }

        sb.append(" = ");

        sb.append(estadoFinal.getRotulo());

        for (int i = 0; i < parametrosFita.size(); i++) {
            sb.append(", ");
            sb.append(parametrosFita.get(i).getSimboloEscrito().getCaracter());
        }

        for (int i = 0; i < parametrosFita.size(); i++) {
            sb.append(", ");
            sb.append(parametrosFita.get(i).getDirecaoMovimento().getId());
        }
        
        return sb.toString();
        
    }

    
}