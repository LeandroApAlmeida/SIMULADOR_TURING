package turing.classes;

import java.util.HashMap;
import java.util.Map;
import static turing.classes.DirecaoMovimento.DIREITA;
import static turing.classes.DirecaoMovimento.ESQUERDA;


public class MaquinaPadrao implements MaquinaTuring {

    
    private final Fita fita;
    
    private final AlfabetoFita alfabetoFita;
    
    private final ConjuntoEstados conjuntoEstados;
    
    private final FuncaoTransicao funcaoTransicao;
    
    private final Map<Integer, Integer> cursorSecao;
    
    
    private int secaoAtual;
    
    private Estado estadoAtual;
    
    private String palavra;

    
    public MaquinaPadrao(AlfabetoFita alfabetoFita, ConjuntoEstados conjuntoEstados,
    FuncaoTransicao funcaoTransicao) {
        this.alfabetoFita = alfabetoFita;   
        this.conjuntoEstados = conjuntoEstados;
        this.funcaoTransicao = funcaoTransicao;     
        this.fita = new Fita(alfabetoFita, true, 0, 0);
        this.cursorSecao = new HashMap<>();
        this.secaoAtual = 0;
    }
    

    @Override
    public void reiniciar() {
        
        estadoAtual = conjuntoEstados.getEstadoInicial();
        
        fita.iniciar(palavra);
        
        if (palavra.contains(String.valueOf(AlfabetoFita.DELIMITADOR_SECAO))) {
            
        } else {
            cursorSecao.put(secaoAtual, 1);
        }
        
    }

    
    @Override
    public void carregarPalavra(String palavra) {
        this.palavra = palavra;
        reiniciar();
    }

    
    @Override
    public void executarPasso() {
        
        Simbolo simbolo = fita.ler(cursorSecao.get(secaoAtual));
        
        Transicao transicao = funcaoTransicao.getTransicao(
            estadoAtual,
            simbolo.getCaracter()
        );
        
        if (transicao != null) {
            
            estadoAtual = transicao.getEstadoFinal();
            
            switch (transicao.getParametrosFitas().get(secaoAtual).getDirecaoMovimento()) {
                case DIREITA -> cursorSecao.put(secaoAtual, cursorSecao.get(secaoAtual) + 1);
                case ESQUERDA -> cursorSecao.put(secaoAtual, cursorSecao.get(secaoAtual) - 1);
            }

            if (cursorSecao.get(secaoAtual) > 0) {

            } else {
                // Termina. Não tem como ir mais para a esquerda.
            }

        } else {
            // Termina. Não há transição.
        }
        
    }

    
    @Override
    public void adicionarOuvinte(OuvinteEtapa ouvinte) {
        
    }

    
    @Override
    public boolean removerOuvinte(OuvinteEtapa ouvinte) {
        return true;
    }
    

    
}