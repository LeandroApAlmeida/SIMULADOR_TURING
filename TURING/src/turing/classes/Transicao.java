package turing.classes;

import java.util.List;

/**
 * Classe que representa uma transição da função de transição. Uma transição é
 * 
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class Transicao {
    
    
    private final Estado estadoInicial;
    
    private final Estado estadoFinal;
    
    private final List<ParametrosFita> parametrosFitas;

    
    public Transicao(Estado estadoInicial, Estado estadoFinal, 
    List<ParametrosFita> parametrosFitas) {
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
        this.parametrosFitas = parametrosFitas;
    }

    
    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    
    public Estado getEstadoFinal() {
        return estadoFinal;
    }

    
    public List<ParametrosFita> getParametrosFitas() {
        return parametrosFitas;
    }

    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == this) return true;
        
        if (obj instanceof Transicao transicao) {
            if (transicao.getEstadoInicial().equals(estadoInicial)) {
                boolean igual = true;
                for (int i = 0; i < parametrosFitas.size(); i++) {
                    if (!parametrosFitas.get(i).getSimboloLido().equals(transicao
                    .getParametrosFitas().get(i).getSimboloLido())) {
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

        for (int i = 0; i < parametrosFitas.size(); i++) {
            sb.append(", ");
            sb.append(parametrosFitas.get(i).getSimboloLido().getCaracter());
        }

        sb.append(" = ");

        sb.append(estadoFinal.getRotulo());

        for (int i = 0; i < parametrosFitas.size(); i++) {
            sb.append(", ");
            sb.append(parametrosFitas.get(i).getSimboloEscrito().getCaracter());
        }

        for (int i = 0; i < parametrosFitas.size(); i++) {
            sb.append(", ");
            sb.append(parametrosFitas.get(i).getDirecaoMovimento().getId());
        }
        
        return sb.toString();
        
    }

    
}
