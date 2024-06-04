package turing.classes;

import java.util.Arrays;

/**
 * Fita da máquina de Turing. Uma fita é um arranjo de células e pode ser
 * infinita à esquerda e à direita, finita à esquerda e infinita à direita,
 * ou finita à esquerda e à direita, dependendo do modelo de máquina de Turing
 * implementado. Neste simulador não será implementado nenhum modelo com fita
 * limitada à direita e à esquerda, portando, tal modelo não será codificado.
 * 
 * <br><br>
 * 
 * Uma máquina de turing pode ter uma ou mais fitas. Cada célula em uma fita
 * inicialmente contém um símbolo de branco, indicando que não há qualquer
 * símbolo do alfabeto de entrada ou do alfabeto auxiliar gravado nela. 
 * 
 * <br><br>
 * 
 * Esta fita simula a propriedade de infinitude de uma fita da máquina de Turing.
 * Obviamente que em se tratando de computadores reais, isto não  é possível. O
 * que mais próximo chegamos disso, é limitar a quantidade de células ao intervalo
 * de valores possíveis de uma variável int (Integer) e atrelar o tamanho de uma
 * fita a este parâmetro.
 * 
 * <br><br>
 * 
 * Valores integer em java são representados por 4 bytes. Para indexar arranjos,
 * são permitidos apenas a porção de números inteiros, logo, temos disponível a
 * seguinte quantidade de valores distintos na faixa positiva:
 * 
 * <br><br>
 * 
 * 
 * <BLOCKQUOTE>
 * 
 * 2<sup>31</sup> - 1 = 2.147.483.647
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Este será o tamanho máximo de nossa fita. A posição inicial do cursor na fita
 * estabeleci que ficará no "centro", ou seja, na posição:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * (2<sup>31</sup> - 1)/2 = 1.073.741.824
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Obviamente, este endereço é "virtual". Não vou alocar um arranjo com mais de
 * um bilhão de posições na memória do computador. O arranjo real tem um tamanho
 * limitado, definido no constructor da classe, geralmente com o tamanho da cadeia
 * de entrada mais algumas células para evitar muitos redimensionamentos. Conforme
 * se torna necessário adicionar células à esquerda ou à direita, este arranjo 
 * inicial vai sendo redimensionado. Assim como eu estabeleci um endereço virtual
 * que marca a posição inicial do cursor no endereço 1.073.741.824, que seria o 
 * centro de um arranjo idexado por integer, faço o mesmo com o arranjo real, 
 * estabelecendo uma célula pivô aonde o cursor inicializa nela, e a partir da 
 * qual vou alocando células à esquerda e à direita dela, conforme necessário.
 * 
 * <br><br>
 *
 * <pre >
 *      Fita Virtual. O cursor aponta inicialmente para a posição 1.073.741.824,
 *      que demarca o centro da fita com 2.147.483.647 posições. Este valor é
 *      o mais próximo de "infinito" que podemos chegar com um computador real,
 *      usando arranjos da linguagem Java.
 * 
 *      <                    * 1.073.741.824      >
 *     ______________________________________________
 * ... _|_____|_____|_____|_____|_____|_____|_____|__ ...
 * 
 * 
 * 
 *      Fita real. O cursor aponta inicialmente para a posição 1. Conforme o
 *      simulador processa o programa, ele pode alocar espaço à esquerda e à
 *      direita desta posição. Esta posição sempre será apontada, e é chamada
 *      de célula pivô. Igual à fita virtual, ela delimita uma posição a partir
 *      da qual a fita poderá crescer em ambas as direções a partir dela.
 * 
 *      <      * 1                                >
 *     ______________________________________________
 *    |_____|_____|_____|_____|_____|_____|_____|____|
 * </pre>
 * 
 * <br>
 * 
 * Para escrever e ler a fita, você deve sempre fazê-lo por meio do endereço
 * virtual. Logo, a primeira posição da fita, aonde o cursor deve ser posicionado
 * inicialmente será será na célula de endereço 1.073.741.824. Na prática, este
 * endereço virtual corresponde ao endereço 1 no arranjo realmente alocado na
 * memória. Se a cabeça de leitura move o cursor para a direita para ler e gravar
 * na célula, então na fita virtual corresponde ao endereço 1.073.741.825, e no
 * arranjo real 2. Se o cursor vai para a esquerda, na fita virtual corresponde
 * ao endereço 1.073.741.823 e na fita real 0. Mais um movimento da cabeça de 
 * leitura para a esquerda estando o endereço real em 0, e eu teria -1 e sairia
 * do limites do arranjo. Logo, se mover para a esquerda, eu preciso alocar mais
 * uma célula nesta direção, copiar todos os elementos à direita dela, fazê-la 
 * ficar com o marcador de branco e mudar a célula pivô para uma casa adiante,
 * matendo a referência de que célula eu iniciei apontando para ela. O mesmo se
 * a cabeça de leitura for posicionada para um endereço além da extremidade
 * posterior da fita. Preciso alocar mais uma célula, colocar um marcador branco
 * nela, mas dessa vez os símbolos não são movidos nem a célula pivô muda de posição.
 * 
 * <br><br>
 * 
 * O redimensionamento é feito automáticamente ao ler ou escrever a fita. Ela se 
 * autoredimensiona para manter a consistência dos endereços virtuais e ter células
 * em cada posição que o cursor é movido, teoricamente até a celula virtual 0 e 
 * a célula virtual 2.147.483.647. O arranjo real vai crescendo conforme necessário
 * para o processamento do programa incrito na função de transição.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public final class Fita {
    
    
    /**Limite máximo de células da fita*/
    private final int NUMERO_MAX_CELULAS = Integer.MAX_VALUE;
    
    /**Posição inicial na fita (ao centro dela).*/
    private final int POSICAO_INICIAL = NUMERO_MAX_CELULAS / 2;
    
    /**Alfabeto dos símbolos que podem ser gravados na fita.*/
    private final AlfabetoFita alfabetoFita;
    
    /**Indicador de que a fita é infinita à esquerda.*/
    private final boolean infinitaEsquerda;
    
    /**Arranjo de células da fita.*/
    private Simbolo[] celulas;
    
    /**
     * Célula pivô. Ela estabelece o vínculo entre um endereço virtual, que é
     * delimitado por todos os valores positivos de um número inteiro de 32
     * bits o que dá 2.147.483.647 valores, e o endereço real, que aponta para
     * uma posição inicialmente definida de um arranjo.
     */
    private int celulaPivo;

    
    /**
     * Constructor padrão. Inicializa a fita com o número especificado de 
     * células.
     * 
     * @param alfabetoFita alfabeto da fita.
     * 
     * @param infinitaEsquerda indicador de fita infinita à esquerda. Se true, a
     * fita é infinita à esquerda. Se false, a fita é finita à esquerda.
     * 
     * @param tamanhoInicial tamanho inicial da fita. Se houver necessidade,
     * o arranjo será redimensionado, aumentando o número de casas à esquerda
     * ou à direita da célula pivô.
     * 
     * @param celulaPivo Endereço da célula pivô no arranjo inicial, no caso da
     * fita ser infinita à esquerda. Caso a fita seja finita, então o pivô sempre
     * estará em 0, pois a fita não crescerá para a esquerda.
     */
    public Fita(AlfabetoFita alfabetoFita, boolean infinitaEsquerda, int tamanhoInicial,
    int celulaPivo) {
        int numeroCelulas = tamanhoInicial;
        this.celulas = new Simbolo[numeroCelulas];        
        this.alfabetoFita = alfabetoFita;
        this.infinitaEsquerda = infinitaEsquerda;
        if (infinitaEsquerda) {
            if (celulaPivo >= 0 && celulaPivo < tamanhoInicial) {
                this.celulaPivo = celulaPivo;
            } else {
                this.celulaPivo = 0;
            }
        } else {
            this.celulaPivo = 0;
        }
        limpar();
    }
    
    
    /**
     * Inicializa a fita com a cadeia de entrada, a partir da posição mais à 
     * esquerda, delimitada pela célula pivô. Todas as demais células da fita
     * são preenchidas com branco.
     * 
     * @param palavra cadeia de entrada.
     */
    public void iniciar(String palavra) {
        limpar();
        for (int i = 0; i < palavra.length(); i++) {
            Simbolo simbolo = alfabetoFita.getSimbolo(palavra.charAt(i));
            if (simbolo == null) {
                simbolo = new Simbolo(palavra.charAt(i), false);
            }
            escrever(POSICAO_INICIAL + i, simbolo);
        }
    }
    
    
    public void limpar() {
        for (int i = 0; i < celulas.length; i++) {
            celulas[i] = alfabetoFita.getSimboloBranco();
        }
    }
    
    
    /**
     * Escrever um símbolo em uma célula da fita.
     * 
     * @param celula célula da fita.
     * 
     * @param simbolo símbolo a escrever.
     * 
     * @return Se true, o símbolo foi escrito. Se false, o símbolo não foi
     * escrito.
     */
    public boolean escrever(int celula, Simbolo simbolo) {
        
        int indice = celulaPivo;
        int deslocamento = 0;
        
        if (infinitaEsquerda) {
            if (celula > POSICAO_INICIAL) {
                deslocamento = celula - POSICAO_INICIAL;
                indice = celulaPivo + deslocamento;
            } if (celula < POSICAO_INICIAL) {
                deslocamento = POSICAO_INICIAL - celula;
                indice = celulaPivo - deslocamento;
            }
        }

        if (indice >= 0 && indice < celulas.length) {
            celulas[indice] = simbolo;
            return true;
        } else {
            
            if (indice < 0) {
                
                if (infinitaEsquerda) {
                    
                    int num = deslocamento - celulaPivo;
                    
                    Simbolo[] celulasTmp = new Simbolo[celulas.length + num];
                    
                    for (int i = 0; i < celulasTmp.length; i++) {
                        celulasTmp[i] = alfabetoFita.getSimboloBranco();
                    }
                    
                    System.arraycopy(celulas, 0, celulasTmp, num, celulas.length);
                    
                    celulaPivo += num;
                    
                    celulas = celulasTmp;
                    
                    celulas[num - 1] = simbolo;
                    
                    return true;
                    
                } else {
                    return false;
                }
                
            } else {
                
                int indiceAtual = celulas.length;
                
                celulas = Arrays.copyOf(celulas, celulas.length + (indice - indiceAtual));
                
                for (int i = indiceAtual; i < celulas.length; i++) {
                    celulas[i] = alfabetoFita.getSimboloBranco();  
                }
                
                celulas[indiceAtual] = simbolo;
                
                return true;
                
            }
            
        }
        
    }
    
    
    /**
     * Ler um símbolo em uma célula da fita.
     * 
     * @param celula célula da fita.
     * 
     * @return Símbolo lido, ou null, caso o índice da célula esteja fora 
     * da faixa.
     */
    public Simbolo ler(int celula) {
        
        int indice = celulaPivo;
        int deslocamento = 0;
        
        if (infinitaEsquerda) {
            if (celula > POSICAO_INICIAL) {
                deslocamento = celula - POSICAO_INICIAL;
                indice = celulaPivo + deslocamento;
            } if (celula < POSICAO_INICIAL) {
                deslocamento = POSICAO_INICIAL - celula;
                indice = celulaPivo - deslocamento;
            }
        }

        if (indice >= 0 && indice < celulas.length) {
            return celulas[indice];
        } else {
            
            if (indice < 0) {
                
                if (infinitaEsquerda) {
                    
                    int num = deslocamento - celulaPivo;
                    
                    Simbolo[] celulasTmp = new Simbolo[celulas.length + num];
                    
                    for (int i = 0; i < celulasTmp.length; i++) {
                        celulasTmp[i] = alfabetoFita.getSimboloBranco();
                    }
                    
                    System.arraycopy(celulas, 0, celulasTmp, num, celulas.length);
                    
                    celulaPivo += num;
                    
                    celulas = celulasTmp;
                    
                    return celulas[0];
                    
                } else {
                    return null;
                }
                
            } else {
                
                int indiceAtual = celulas.length;
                
                celulas = Arrays.copyOf(celulas, celulas.length + (indice - indiceAtual));
                
                for (int i = indiceAtual; i < celulas.length; i++) {
                    celulas[i] = alfabetoFita.getSimboloBranco();  
                }
                
                return celulas[indiceAtual];
                
            }
            
        }
        
    }
    
    
    public void ajustar(int qtdCelulas) {
        
        int valor = Math.abs(qtdCelulas);
        
        Simbolo[] celulasTmp = new Simbolo[celulas.length + valor];

        if (qtdCelulas < 0) {
            System.arraycopy(celulas, 0, celulasTmp, valor , celulas.length);
            for (int i = 0; i < valor; i++) {
                celulasTmp[i] = alfabetoFita.getSimboloBranco();
            }
            celulaPivo = celulaPivo + valor;
        } else {
            System.arraycopy(celulas, 0, celulasTmp, 0 , celulas.length);
             for (int i = celulas.length; i < celulasTmp.length; i++) {
                celulasTmp[i] = alfabetoFita.getSimboloBranco();
            }
        }
        
        celulas = celulasTmp;

    }
    
    
    public int getPosicaoRelativa(int celula) {
        
        int indice = celulaPivo;
        int deslocamento;
        
        if (infinitaEsquerda) {
            if (celula > POSICAO_INICIAL) {
                deslocamento = celula - POSICAO_INICIAL;
                indice = celulaPivo + deslocamento;
            } if (celula < POSICAO_INICIAL) {
                deslocamento = POSICAO_INICIAL - celula;
                indice = celulaPivo - deslocamento;
            }
        }
        
        return indice;
        
    }
    
    
    public Simbolo[] getCelulas() {
        return celulas;
    }
    
    
    public int getComprimento() {
        return celulas.length;
    }
    
    
    public int getCelulaInicial() {
        if (infinitaEsquerda) {
            return POSICAO_INICIAL;
        } else {
            return 0;
        }
    }
    
    
    public int getCelulaPivo() {
        return celulaPivo;
    }

    
    /**
     * Indicador de fita infinita à esquerda.
     * @return Se true, a fita é infinita à esquerda. Se false, a fita é finita
     * à esquerda.
     */
    public boolean isInfinitaEsquerda() {
        return infinitaEsquerda;
    }
    
    
}