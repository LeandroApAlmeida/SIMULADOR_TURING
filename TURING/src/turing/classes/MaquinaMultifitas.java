package turing.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static turing.classes.DirecaoMovimento.DIREITA;
import static turing.classes.DirecaoMovimento.ESQUERDA;

public class MaquinaMultifitas implements MaquinaTuring {

    
    private final FuncaoTransicao funcaoTransicao;
    
    private final AlfabetoFita alfabetoFita;
    
    private final ConjuntoEstados conjuntoEstados;
    
    private final List<OuvinteMaqTuring> ouvintes;
    
    private Fita[] fitas;
    
    private Map<Integer, Integer> cabecasLeitura;
    
    private Estado estadoAtual;
    
    private String palavra;
    
    private boolean emExecucao;
    
    private boolean aceita;
    
    private boolean rejeita;
    
    private int numeroPassos;
    
    private int indiceTransicao;
    
    private int numeroFitas;

    
    public MaquinaMultifitas(AlfabetoFita alfabetoFita, ConjuntoEstados conjuntoEstados,
    FuncaoTransicao funcaoTransicao, int numeroFitas) throws Exception {
        if (numeroFitas > 0) {
            this.ouvintes = new ArrayList<>();
            this.funcaoTransicao = funcaoTransicao;
            this.alfabetoFita = alfabetoFita;
            this.conjuntoEstados = conjuntoEstados;
            this.numeroFitas = numeroFitas;
            this.cabecasLeitura = new HashMap<>();
            this.fitas = new Fita[numeroFitas];
            this.emExecucao = false;
        } else {
            throw new Exception(
                "Número de fitas deve ser maior ou igual a 1."
            ); 
        }
    }
    
    
    private char[] lerSimbolosFitas() {
        char[] simbolosFitas = new char[fitas.length];
        for (int i = 0; i < fitas.length; i++) {
            simbolosFitas[i] = fitas[i].ler(cabecasLeitura.get(i)).getCaracter();
        }
        return simbolosFitas;
    }
    
    
    private Map<Integer, Integer> getIndicesRelativos() {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < cabecasLeitura.size(); i++) {
            map.put(i, fitas[i].getPosicaoRelativa(cabecasLeitura.get(i)));
        }
        return map;
    }

    
    @Override
    public void reiniciar() {
        
        aceita = false;
        rejeita = false;
        emExecucao = true;
        numeroPassos = 0;
        
        estadoAtual = conjuntoEstados.getEstadoInicial();
        
        int celulasAdicionais = palavra.length() > 25 ? 10 : 25 - (palavra.length());
        
        for (int i = 0; i < numeroFitas; i++) {
            fitas[i] = new Fita(alfabetoFita, true, palavra.length() + celulasAdicionais, 1);
            cabecasLeitura.put(i, fitas[i].getCelulaInicial());
        }
        
        fitas[0].iniciar(palavra);
        
        char[] simbolosFitas = lerSimbolosFitas();
        
        indiceTransicao = funcaoTransicao.indiceDe(estadoAtual, simbolosFitas);

        for (OuvinteMaqTuring ouvinte : ouvintes) {
            
            ouvinte.atualizarMaqTuring(estadoAtual,
                fitas, 
                getIndicesRelativos(),
                indiceTransicao,
                numeroPassos,
                aceita,
                rejeita
            );
            
        }
        
    }

    
    @Override
    public void carregarPalavra(String palavra) {
        this.palavra = palavra;
        reiniciar();
    }

    
    @Override
    public void executarPasso() {

        if (emExecucao) {
            
            numeroPassos++;
            
            char[] simbolos = lerSimbolosFitas();

            Transicao transicao = funcaoTransicao.getTransicao(estadoAtual, simbolos);

            if (transicao != null) {

                List<ParametrosFita> paramsFita = transicao.getParametrosFitas();

                for (int i = 0; i < paramsFita.size(); i++) {
                    
                    ParametrosFita params = paramsFita.get(i);
                    
                    fitas[i].escrever(cabecasLeitura.get(i), params.getSimboloEscrito());
                    
                    switch (params.getDirecaoMovimento()) {
                        case DIREITA -> cabecasLeitura.put(i, cabecasLeitura.get(i) + 1);
                        case ESQUERDA -> cabecasLeitura.put(i, cabecasLeitura.get(i) - 1);
                    }
                    
                }
                
                Map<Integer, Integer> indicesRelativos = getIndicesRelativos();
                
                int celulasEsquerda = 0;
                int celulasDireita = 0;
                
                for (int i = 0; i < indicesRelativos.size(); i++) {
                    if (indicesRelativos.get(i) <= 0) {
                        celulasEsquerda = indicesRelativos.get(i) - 1;
                    } else if (indicesRelativos.get(i) >= fitas[i].getComprimento()) {
                        celulasDireita = (indicesRelativos.get(i) - fitas[i].getComprimento()) + 1;
                    }                 
                }
                
                if (celulasEsquerda != 0 || celulasDireita != 0) {
                    for (Fita fita : fitas) {
                        if (celulasEsquerda != 0) {
                            fita.ajustar(celulasEsquerda);
                        }
                        if (celulasDireita != 0) {
                            fita.ajustar(celulasDireita);
                        }
                    }
                }

                estadoAtual = transicao.getEstadoFinal();

                if (estadoAtual.isTerminal()) {
                    emExecucao = false;
                    aceita = true;
                    rejeita = false;
                } else {
                    simbolos = lerSimbolosFitas();
                    indiceTransicao = funcaoTransicao.indiceDe(estadoAtual, simbolos);
                }
                
            } else {
                emExecucao = false;
                aceita = false;
                rejeita = true;
            }
        
        }
        
        for (OuvinteMaqTuring ouvinte : ouvintes) {
            
            ouvinte.atualizarMaqTuring(
                estadoAtual,
                fitas,
                getIndicesRelativos(),
                indiceTransicao,
                numeroPassos,
                aceita,
                rejeita
            );
            
        }
        
    }

    
    @Override
    public void adicionarOuvinte(OuvinteMaqTuring ouvinte) {
        ouvintes.add(ouvinte);
    }

    
    @Override
    public boolean removerOuvinte(OuvinteMaqTuring ouvinte) {
        return ouvintes.remove(ouvinte);
    }

    
}