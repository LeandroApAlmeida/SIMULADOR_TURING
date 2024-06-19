 package turing.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static turing.classes.DirecaoMovimento.DIREITA;
import static turing.classes.DirecaoMovimento.ESQUERDA;
import static turing.classes.Constantes.TAMANHO_FITA;

/**
 * Classe que implementa um modelo de Máquina de Turing com Múltiplas Fitas. O
 * modelo com múltiplas fitas tem uma Unidade de Controle, e para cada fita,
 * uma Cabeça de Leitura/Escrita. Cada fita do modelo é infinita à direita e
 * à esquerda.
 * 
 * <br><br>
 * 
 * <img src="data:image/jpeg;base64,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" />
 * 
 * <br><br>
 * 
 * Formalmente, ele é representado como:
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
 * <b>q<sub>0</sub></b> Estado inicial da máquina;
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
 * <br>
 * 
 * A Máquina de Turing com múltiplas fitas é equivalenta à Máquina de Turing 
 * padrão, com exceção que na função de transição a máquina fará a leitura de
 * todas as k fitas simultâneamente, sendo k maior ou igual a 1, para realizar
 * a transição com base no estado atual.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class MaquinaMultifitas implements MaquinaTuring {

    
    /**Função de transição.*/
    private final FuncaoTransicao funcaoTransicao;
    
    /**Alfabeto da fita.*/
    private final AlfabetoFita alfabetoFita;
    
    /**Conjunto de estados.*/
    private final ConjuntoEstados conjuntoEstados;
    
    /**Ouvintes da simulação da máquina de Turing.*/
    private final List<OuvinteEtapaSimulacao> ouvintes;
    
    /**Fitas da máquina*/
    private Fita[] fitas;
    
    /**Cursores das Cabeças de Leitura/Escrita, uma para cada fita.*/
    private Map<Integer, Integer> cursoresFitas;
    
    /**Estado atual apontado pela Unidade de Controle.*/
    private Estado estadoAtual;
    
    /**Palavra de entrada a ser processada.*/
    private String palavra;
    
    /**Estatus de excução da simulação.*/
    private boolean emExecucao;
    
    /**Estatus de palavra aceita/rejeitada pela máquina*/
    private boolean aceita;
    
    /**Número de passos executados na simulação.*/
    private int numeroPassos;
    
    /**Índice da transição atual.*/
    private int indiceTransicao;
    
    /**Número de fitas da máquina.*/
    private int numeroFitas;

    
    /**
     * Constructor padrão.
     * 
     * @param alfabetoFita alfabeto da fita.
     * 
     * @param conjuntoEstados conjunto dos estados.
     * 
     * @param funcaoTransicao função de transição.
     * 
     * @param numeroFitas número de fitas da máquina.
     * 
     * @throws Exception erro na definição dos parâmetros.
     */
    public MaquinaMultifitas(AlfabetoFita alfabetoFita, ConjuntoEstados conjuntoEstados,
    FuncaoTransicao funcaoTransicao, int numeroFitas) throws Exception {
        if (numeroFitas > 0) {
            this.ouvintes = new ArrayList<>();
            this.funcaoTransicao = funcaoTransicao;
            this.alfabetoFita = alfabetoFita;
            this.conjuntoEstados = conjuntoEstados;
            this.numeroFitas = numeroFitas;
            this.cursoresFitas = new HashMap<>();
            this.fitas = new Fita[numeroFitas];
            this.emExecucao = false;
        } else {
            throw new Exception(
                "Número de fitas deve ser maior ou igual a 1."
            ); 
        }
    }
    
    
    /**
     * Ler os símbolos sob os cursores das Cabeças de Leitura/Escrita. 
     * 
     * @return símbolos sob as as Cabeças de Leitura/Escrita
     */
    private char[] lerSimbolosFitas() {
        char[] simbolosFitas = new char[fitas.length];
        for (int i = 0; i < fitas.length; i++) {
            simbolosFitas[i] = fitas[i].ler(cursoresFitas.get(i)).getCaracter();
        }
        return simbolosFitas;
    }
    
    
    /**
     * Obter os índices absolutos dos cursores apontando para os arranjos reais
     * das fitas na memória. Para ler e gravar nas fitas é necessário ser feito
     * por meio de índices virtais. Mas para imprimir o arranjo que está na memória,
     * é necessário informar os índices absolutos.
     * 
     * @return índices absolutos dos cursores para os arranjos em memória.
     */
    private Map<Integer, Integer> getIndicesAbsolutos() {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < cursoresFitas.size(); i++) {
            map.put(i, fitas[i].getEnderecoAbsoluto(cursoresFitas.get(i)));
        }
        return map;
    }

    
    /**
     * Reiniciar a simulação de processamento da palavra de entrada. Posiciona
     * os símbolos da palavra de entrada na primeira fita, zera todas as demais
     * fitas e reinicia o processo do zero, posicionando a Unidade de Controle
     * no estado inicial (q<sub>0</sub>). Notifica todos os ouvintes do processo
     * de simulação.
     */
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

        int celulasAdicionais = palavra.length() > TAMANHO_FITA ? 10 : 
        TAMANHO_FITA - (palavra.length());

        for (int i = 0; i < numeroFitas; i++) {
            fitas[i] = new Fita(
                alfabetoFita,
                true,
                palavra.length() + celulasAdicionais,
                1
            );
            cursoresFitas.put(i, fitas[i].getCelulaInicial());
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
            
            ouvinte.atualizarEtapaSimulacao(estadoAtual,
                fitas, 
                getIndicesAbsolutos(),
                indiceTransicao,
                numeroPassos,
                aceita,
                !emExecucao
            );
            
        }
        
    }

    
    /**
     * Carregar a palavra de entrada na primeira fita.
     * 
     * @param palavra palavra de entrada.
     */
    @Override
    public void carregarPalavra(String palavra) {
        this.palavra = palavra;
        reiniciar();
    }

    
    /**
     * Executar um passo da simulação de uma máquina de Turing. A execução de um
     * passo seguirá o seguinte roteiro:
     * 
     * <br><br>
     * 
     * <ol>
     * 
     * <li>Inclementa o número de passos da simulação.</li><br>
     * 
     * <li>Lê os símbolos nas células sob os cursores das Cabeças de 
     * Leitura/Escrita.</li><br>
     * 
     * <li>Verifica se há uma transição definida na função de transição para 
     * o estado corrente e os símbolos lidos das fitas. Se não houver, a máquina
     * para e notifica que a palavra de entrada foi rejeitada.</li><br>
     * 
     * <li>Escreve os símbolos nas células sob os cursores das Cabeças de 
     * Leitura/Escrita de acordo com o estado atual e símbolos lidos, definidos
     * na função de transição.</li><br>
     * 
     * <li>Move as Cabeças de Leitura/Escrita de cada fita de acordo com o
     * estado atual e símbolos lidos, definidos na função de transição.</li><br>
     * 
     * <li>Redimensiona as fitas, caso precise alocar células à esquerda ou à
     * direita na próxima execução do algoritmo.</li> <br>
     * 
     * <li>Verifica se atingiu um estado terminal. Se atingiu, a máquina para
     * e notifica que a palavra de entrada foi aceita.</li><br>
     * 
     * </ol>
     * 
     * Basicamente se executa o processamento da Máquina de Turing para a função
     * de transição definida e a palavra de entrada e redimensiona as fitas de
     * acordo com a necessidade de alocação de memória (todas as fitas terão o
     * mesmo número de células). Caso uma condição de parada seja verificada, a
     * máquina para e notifica os ouvintes se a palavra de entrada foi aceita
     * ou foi rejeitada.
     * 
     * <br><br>
     * 
     * Como com qualquer linguagem de programação, pode ocorrer de a máquina
     * entrar em um LOOP infinito e nunca parar.
     */
    @Override
    public void executarPasso() {

        if (emExecucao) {
            
            // Inclementa o número de passos da simulação.
            numeroPassos++;
            
            // Obtém a transição para o estado corrente e os símbolos lidos
            // das fitas sob os cursores das Cabeças de Leitura/Escrita.
            
            char[] simbolosLidos = lerSimbolosFitas();

            Transicao transicao = funcaoTransicao.getTransicao(estadoAtual, simbolosLidos);

            if (transicao != null) {
                
                // Escreve os símbolos nas fitas e move as Cabeças de Leitura/Escrita
                // de acordo com a transição definida para o estado corrente e os
                // símbolos lidos das fitas.

                List<ParametrosFita> paramsFita = transicao.getParametrosFitas();

                for (int i = 0; i < paramsFita.size(); i++) {
                    
                    ParametrosFita params = paramsFita.get(i);
                    
                    fitas[i].escrever(cursoresFitas.get(i), params.getSimboloEscrito());
                    
                    switch (params.getDirecaoMovimento()) {
                        case DIREITA -> cursoresFitas.put(i, cursoresFitas.get(i) + 1);
                        case ESQUERDA -> cursoresFitas.put(i, cursoresFitas.get(i) - 1);
                    }
                    
                }
                
                // Redimensiona as fitas caso alguma delas precise alocar mais
                // células à esquerda ou à direita na próxima execução do algoritmo.
                // Isto faz com que todas as fitas tenham o mesmo número de células
                // sempre ao longo da simulação.
                
                Map<Integer, Integer> indicesRelativos = getIndicesAbsolutos();
                
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
                
                // Verifica se o novo estado corrente pertence ao conjunto
                // dos estados terminais. Se ele pertencer, a máquina para
                // e a palavra de entrada é aceita.

                estadoAtual = transicao.getEstadoFinal();

                if (estadoAtual.isTerminal()) {
                    emExecucao = false;
                    aceita = true;
                } else {
                    simbolosLidos = lerSimbolosFitas();
                    indiceTransicao = funcaoTransicao.indiceDe(estadoAtual, simbolosLidos);
                }
                
            } else {
                
                // Não há uma transição definida para o estado atual e
                // símbolos lidos. Neste caso a máquina para e a palavra de 
                // entrada é rejeitada.
                
                emExecucao = false;
                aceita = false;
                
            }
        
        }
        
        for (OuvinteEtapaSimulacao ouvinte : ouvintes) {
            
            ouvinte.atualizarEtapaSimulacao(estadoAtual,
                fitas,
                getIndicesAbsolutos(),
                indiceTransicao,
                numeroPassos,
                aceita,
                !emExecucao
            );
            
        }
        
    }
    
    
    /**
     * Obter o número de fitas da máquina.
     * 
     * @return Número de fitas da máquina.
     */
    @Override
    public int getNumeroFitas() {
        return numeroFitas;
    }

    
    /**
     * Obter a função de transição.
     * 
     * @return Função de transição.
     */
    @Override
    public FuncaoTransicao getFuncaoTransicao() {
        return funcaoTransicao;
    }

    
    /**
     * Obter o alfabeto da fita.
     * 
     * @return Alfabeto da fita.
     */
    @Override
    public AlfabetoFita getAlfabetoFita() {
        return alfabetoFita;
    }

    
    /**
     * Obter o conjunto dos estados.
     * 
     * @return Conjunto dos estados. 
     */
    @Override
    public ConjuntoEstados getConjuntoEstados() {
        return conjuntoEstados;
    }

    
    /**
     * Obter as fitas da máquina.
     * 
     * @return Fitas da máquina.
     */
    @Override
    public Fita[] getFitas() {
        return fitas;
    }


    /**
     * Obter os cursores para as fitas da máquina.
     * 
     * @return Cursores para as fitas da máquina.
     */
    @Override
    public Map<Integer, Integer> getCursores() {
        return cursoresFitas;
    }

    
    /**
     * Obter o estado atual da Unidade de Controle.
     * 
     * @return Estado atual da Unidade de Controle.
     */
    @Override
    public Estado getEstadoAtual() {
        return estadoAtual;
    }


    /**
     * Obter o número de passos da simulação.
     * 
     * @return Número de passos da simulação.
     */
    @Override
    public int getNumeroPassos() {
        return numeroPassos;
    }

    
    /**
     * Obter a palavra de entrada.
     * 
     * @return palavra de entrada.
     */
    @Override
    public String getPalavra() {
        return palavra;
    }

    
    /**
     * Estatus de palavra de entrada aceita.
     * 
     * @return Se true, a palavra foi aceita. Se false, a palavra foi rejeitada.
     */
    @Override
    public boolean isAceita() {
        return aceita;
    }
    

    /**
     * Adicionar um ouvinte do processo de simulação.
     * 
     * @param ouvinte ouvinte a ser adicionado.
     */
    @Override
    public void adicionarOuvinte(OuvinteEtapaSimulacao ouvinte) {
        ouvintes.add(ouvinte);
    }

    
    /**
     * Remover um ouvinte do processo de simulação.
     * 
     * @param ouvinte ouvinte a ser removido.
     * 
     * @return Se true, o ouvinte foi removido. Se false, ele não foi removido.
     */
    @Override
    public boolean removerOuvinte(OuvinteEtapaSimulacao ouvinte) {
        return ouvintes.remove(ouvinte);
    }

    
}