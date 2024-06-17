package turing.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static turing.classes.DirecaoMovimento.DIREITA;
import static turing.classes.DirecaoMovimento.ESQUERDA;

/**
 * Classe que implementa um modelo de Máquina de Turing com Múltiplas Fitas. O
 * modelo com múltiplas fitas tem uma Unidade de Controle, e para cada fita,
 * uma Cabeça de Leitura/Escrita. Cada fita do modelo é infinita à direita e
 * à esquerda.
 * 
 * <br><br>
 * 
 * Matemáticamente, ele é representado como:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * M = (Σ, Q, δ, q0, F, V, β, ⊛)
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Onde:
 * 
 * <br><br>
 * 
 * 
 * <BLOCKQUOTE>
 * 
 * <b>Σ</b> Alfabeto de símbolos de entrada;
 * 
 * <br><br>
 * 
 * <b>Q</b> Conjunto de estados possíveis da máquina;
 * 
 * <br><br>
 * 
 * <b>δ</b> Função de transição ou programa, tal que:
 * 
 * 
 * 
 * <br><br>
 * 
 * <b>q<sub>0</sub></b>: Estado inicial da máquina;
 * 
 * <br><br>
 * 
 * <b>F</b> Conjunto dos estados finais;
 * 
 * <br><br>
 * 
 * <b>V</b> Alfabeto auxiliar;
 * 
 * <br><br>
 * 
 * <b>β</b> Símbolo especial branco;
 * 
 * <br><br>
 * 
 * <b>⊛</b> Símbolo especial marcador de início da fita.
 * 
 * </BLOCKQUOTE>
 * 
 * 
 * @author Leandro Ap. de Almeida
 * @since 1.0
 */
public class MaquinaMultifitas implements MaquinaTuring {

    
    private final FuncaoTransicao funcaoTransicao;
    
    private final AlfabetoFita alfabetoFita;
    
    private final ConjuntoEstados conjuntoEstados;
    
    private final List<OuvinteEtapaSimulacao> ouvintes;
    
    private Fita[] fitas;
    
    private Map<Integer, Integer> cabecasLeitura;
    
    private Estado estadoAtual;
    
    private String palavra;
    
    private boolean emExecucao;
    
    private boolean aceita;
    
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
            map.put(i, fitas[i].getEnderecoAbsoluto(cabecasLeitura.get(i)));
        }
        return map;
    }

    
    @Override
    public void reiniciar() {
        
        boolean simboloInvalido = aceita = emExecucao = false;
        
        for (int i = 0; i < palavra.length(); i++) {
            Simbolo simbolo = alfabetoFita.getSimbolo(palavra.charAt(i));
            if (simbolo != null) {
                if (simbolo.isAuxiliar()) {
                    aceita = true;
                    break;
                }
            }
        }
        
        estadoAtual = conjuntoEstados.getEstadoInicial();

        int celulasAdicionais = palavra.length() > 25 ? 10 : 25 - (palavra.length());

        for (int i = 0; i < numeroFitas; i++) {
            fitas[i] = new Fita(
                alfabetoFita,
                true,
                palavra.length() + celulasAdicionais,
                1
            );
            cabecasLeitura.put(i, fitas[i].getCelulaInicial());
        }

        fitas[0].iniciar(palavra);

        numeroPassos = 0;

        if (!simboloInvalido) {
            char[] simbolosFitas = lerSimbolosFitas();
            indiceTransicao = funcaoTransicao.indiceDe(estadoAtual, simbolosFitas);
            emExecucao = true;      
        } else {
            indiceTransicao = -1;
            emExecucao = false;
        }

        for (OuvinteEtapaSimulacao ouvinte : ouvintes) {
            
            ouvinte.atualizarEtapaSimulacao(
                estadoAtual,
                fitas, 
                getIndicesRelativos(),
                indiceTransicao,
                numeroPassos,
                aceita,
                !emExecucao
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
                        celulasEsquerda = Math.abs(indicesRelativos.get(i) - 1);
                    } else if (indicesRelativos.get(i) >= fitas[i].getComprimento()) {
                        celulasDireita = (indicesRelativos.get(i) - fitas[i].getComprimento()) + 2;
                    }                 
                }
                
                if (celulasEsquerda != 0 || celulasDireita != 0) {
                    for (Fita fita : fitas) {
                        fita.redimensionar(celulasEsquerda, celulasDireita);
                    }
                }

                estadoAtual = transicao.getEstadoFinal();

                if (estadoAtual.isTerminal()) {
                    emExecucao = false;
                    aceita = true;
                } else {
                    simbolos = lerSimbolosFitas();
                    indiceTransicao = funcaoTransicao.indiceDe(estadoAtual, simbolos);
                }
                
            } else {
                emExecucao = false;
                aceita = false;
            }
        
        }
        
        for (OuvinteEtapaSimulacao ouvinte : ouvintes) {
            
            ouvinte.atualizarEtapaSimulacao(
                estadoAtual,
                fitas,
                getIndicesRelativos(),
                indiceTransicao,
                numeroPassos,
                aceita,
                !emExecucao
            );
            
        }
        
    }
    
    
    @Override
    public int getNumeroFitas() {
        return numeroFitas;
    }

    
    @Override
    public FuncaoTransicao getFuncaoTransicao() {
        return funcaoTransicao;
    }

    
    @Override
    public AlfabetoFita getAlfabetoFita() {
        return alfabetoFita;
    }

    
    @Override
    public ConjuntoEstados getConjuntoEstados() {
        return conjuntoEstados;
    }

    
    @Override
    public Fita[] getFitas() {
        return fitas;
    }

    
    @Override
    public Map<Integer, Integer> getCursores() {
        return cabecasLeitura;
    }

    
    @Override
    public Estado getEstadoAtual() {
        return estadoAtual;
    }

    
    @Override
    public int getNumeroPassos() {
        return numeroPassos;
    }

    
    @Override
    public String getPalavra() {
        return palavra;
    }

    
    @Override
    public boolean isAceita() {
        return aceita;
    }
    

    @Override
    public void adicionarOuvinte(OuvinteEtapaSimulacao ouvinte) {
        ouvintes.add(ouvinte);
    }

    
    @Override
    public boolean removerOuvinte(OuvinteEtapaSimulacao ouvinte) {
        return ouvintes.remove(ouvinte);
    }

    
}