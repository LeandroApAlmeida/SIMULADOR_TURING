package turing.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static turing.classes.DirecaoMovimento.DIREITA;
import static turing.classes.DirecaoMovimento.ESQUERDA;


/**
 * | a | b | c | # | _ | _ | _ | # | _ | _ | _ |
 * @author leand
 */
public class MaquinaPadrao implements MaquinaTuring {

    
    private Fita fita;
    
    private final AlfabetoFita alfabetoFita;
    
    private final ConjuntoEstados conjuntoEstados;
    
    private final FuncaoTransicao funcaoTransicao;
    
    private final Map<Integer, Integer> cursorSecao;
    
    private final List<OuvinteEtapaSimulacao> ouvintes;
    
    private int secaoAtual;
    
    private int numeroSecoes;
    
    private Estado estadoAtual;
    
    private String palavra;

    
    public MaquinaPadrao(AlfabetoFita alfabetoFita, ConjuntoEstados conjuntoEstados,
    FuncaoTransicao funcaoTransicao, int numeroSecoes) throws Exception {
        if (numeroSecoes > 0) {
            this.alfabetoFita = alfabetoFita;   
            this.conjuntoEstados = conjuntoEstados;
            this.funcaoTransicao = funcaoTransicao;
            this.numeroSecoes = numeroSecoes;
            this.cursorSecao = new HashMap<>();
            this.ouvintes = new ArrayList<>();
            this.secaoAtual = 0;
        } else {
            throw new Exception(
                "Número de fitas deve ser maior ou igual a 1."
            );
        }
    }
    

    @Override
    public void reiniciar() {
        
        estadoAtual = conjuntoEstados.getEstadoInicial();
        
        fita = new Fita(
            alfabetoFita,
            true,
            (palavra.length() * numeroSecoes) + numeroSecoes,
            0
        );
        
        fita.iniciar(palavra);
                
        if (numeroSecoes > 1) {
        
            int tamanhoSecao = palavra.length();
            
            for (int i = 1; i <= numeroSecoes; i++) {
                
                fita.escrever(
                    i * tamanhoSecao,
                    alfabetoFita.getSimboloDelimitador()
                );
                
            }
            
        }
        
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
    public void adicionarOuvinte(OuvinteEtapaSimulacao ouvinte) {
        ouvintes.add(ouvinte);
    }

    
    @Override
    public boolean removerOuvinte(OuvinteEtapaSimulacao ouvinte) {
        return ouvintes.remove(ouvinte);
    }

    @Override
    public FuncaoTransicao getFuncaoTransicao() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public AlfabetoFita getAlfabetoFita() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ConjuntoEstados getConjuntoEstados() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Fita[] getFitas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Map<Integer, Integer> getCursores() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Estado getEstadoAtual() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getPalavra() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getNumeroPassos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    

    @Override
    public int getNumeroFitas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isAceita() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}