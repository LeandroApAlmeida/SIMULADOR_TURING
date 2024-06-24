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
 * <p align="center">
 * 
 * <img src="https://github.com/LeandroApAlmeida/SIMULADOR_TURING/blob/master/RES/MaquinaTuringMultifitas.png?raw=true"/>
 * <br>
 * <i>Modelo de Máquina de Turing com múltiplas fitas</i>
 * 
 * </p>
 * 
 * <br><br>
 * 
 * <h3>Modelo Formal</h3>
 * 
 * <br>
 * 
 * Uma Máquina de Turing Multifitas é uma 8-upla:
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
 * δ : Q × Γ<sup>k</sup> → Q × Γ<sup>k</sup> × {E, D, P}<sup>k</sup>
 * 
 * <br><br>
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
 * Ela é representada da mesma forma que uma Máquina de Turing Padrão, com exceção
 * da função de transição, que agora depende do estado corrente da Unidade de
 * Controle e dos símbolos lidos de todas as fitas para determinar o novo estado
 * e que símbolos gravar em cada fita e a direção do movimento da cabeça de leitura
 * em cada fita.
 * 
 * <br><br>
 * 
 * Desta forma, a função de transição para uma Máquina de Turing com múltiplas 
 * fitas pode ser expressa também como:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * δ(q<sub>a</sub>, l<sub>1</sub>, l<sub>2</sub>, ... , l<sub>k</sub>) = 
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
 * <b>l<sub>1</sub>, l<sub>2</sub>, ..., l<sub>k</sub></b>: Símbolos lidos das 
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
 * <b>k</b>: Número de fitas da máquina de Turing (k >= 1).
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
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
    private Map<Integer, Integer> cursores;
    
    /**Estado atual apontado pela Unidade de Controle.*/
    private Estado estadoAtual;
    
    /**Palavra de entrada a ser processada.*/
    private String palavra;
    
    /**Status de excução da simulação.*/
    private boolean emExecucao;
    
    /**Status de palavra aceita/rejeitada pela máquina*/
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
            this.cursores = new HashMap<>();
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
            simbolosFitas[i] = fitas[i].ler(cursores.get(i)).getCaracter();
        }
        return simbolosFitas;
    }
    
    
    /**
     * Obter os índices absolutos dos cursores apontando para os arranjos reais
     * das fitas na memória. Para ler e gravar nas fitas é necessário ser feito
     * por meio de índices virtuais. Mas para imprimir o arranjo que está na
     * memória, é necessário informar os índices absolutos.
     * 
     * @return índices absolutos dos cursores para os arranjos em memória.
     */
    private Map<Integer, Integer> getIndicesAbsolutos() {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < cursores.size(); i++) {
            map.put(i, fitas[i].getEnderecoAbsoluto(cursores.get(i)));
        }
        return map;
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
            cursores.put(i, fitas[i].getCelulaInicial());
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

                List<ParametrosFita> paramsFita = transicao.getParametrosFita();

                for (int i = 0; i < paramsFita.size(); i++) {
                    
                    ParametrosFita params = paramsFita.get(i);
                    
                    fitas[i].escrever(cursores.get(i), params.getSimboloEscrito());
                    
                    switch (params.getDirecaoMovimento()) {
                        case DIREITA -> cursores.put(i, cursores.get(i) + 1);
                        case ESQUERDA -> cursores.put(i, cursores.get(i) - 1);
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
        return cursores;
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
     * Status de palavra de entrada aceita.
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