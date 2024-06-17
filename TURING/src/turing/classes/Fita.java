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
 * Esta classe simula a propriedade de infinito de uma fita da máquina de Turing.
 * Obviamente que em se tratando de computadores reais, isto não  é possível. O
 * mais próximo que chegamos disso é limitar a quantidade de células ao intervalo
 * de valores possíveis de uma variável int (Integer) e atrelar o tamanho de uma
 * fita a este parâmetro.
 * 
 * <br><br>
 * 
 * Valores integer em java são representados por 4 bytes. Para indexar arranjos,
 * são permitidos apenas a porção de números positivos, logo, temos disponível a
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
 * Este será o tamanho máximo da nossa fita. A posição inicial do cursor na fita
 * está estabelecido que ficará no "centro", ou seja, na posição:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * (2<sup>31</sup> - 1) / 2 = 1.073.741.824
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Obviamente, este endereço é "virtual". Não vai alocar um arranjo com mais de
 * dois bilhões de posições na memória do computador. O arranjo real tem um tamanho
 * limitado, definido no constructor da classe, geralmente com o tamanho da cadeia
 * de entrada mais algumas células para evitar muitos redimensionamentos. Conforme
 * se torna necessário adicionar células à esquerda ou à direita, este arranjo 
 * inicial vai sendo redimensionado. Assim como foi estabelecido um endereço virtual
 * que marca a posição inicial do cursor no endereço 1.073.741.824, que seria o 
 * centro de um arranjo idexado por integer, metade do "infinito" o mesmo é feito 
 * com o arranjo real, estabelecendo uma célula pivô aonde o cursor inicializa 
 * nela, e a partir da qual vai alocando células à esquerda e à direita dela, 
 * conforme necessário.
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * <i>Fita Virtual</i>. O cursor aponta inicialmente para a posição 1.073.741.824, que 
 * demarca o centro da fita com 2.147.483.647 posições. Este valor é o mais próximo
 * de "infinito" que podemos chegar com um computador real, usando arranjos da 
 * linguagem Java.
 * 
 * <br><br>
 * 
 * <table width="800" cellpadding="2" cellspacing="0" style="background: transparent; 
 * page-break-before: always">
 * 
 *  <tr style="background: transparent">
 * 
 *      <td height="30" style="border-top: 1px solid #000000; border-bottom: 1px solid #000000;
 *      border-left: none; border-right: none; padding: 0.05cm 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">...</font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000;
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">1.073.741.822</font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000; 
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">1.073.741.823</font></p>
 *      </td>
 * 
 *      <td bgcolor="#2a6099" style="background: #2a6099; border-top: 1px solid #000000;
 *      border-bottom: 1px solid #000000; border-left: 1px solid #000000; 
 *      border-right: none; padding-top: 0.05cm; padding-bottom: 0.05cm; 
 *      padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 14pt" color="#ffffff">
 *      <b>1.073.741.824</b></font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000; 
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">1.073.741.825</font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000; 
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">1.073.741.826</font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000;
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">...</font></p>
 *      </td>
 * 
 *  </tr>
 * 
 * </table>
 * 
 * <br><br>
 * 
 * <i>Fita real</i>. No exemplo o cursor aponta para a posição 1. Conforme o simulador 
 * processa o programa, ele pode alocar espaço à esquerda e à direita desta posição.
 * Esta posição é chamada de célula pivô. Igual à fita virtual, ela delimita uma
 * posição a partir da qual a fita poderá crescer em ambas as direções a partir
 * dela.
 * 
 * <br><br>
 * 
 * <table width="800" cellpadding="2" cellspacing="0" style="background: transparent; 
 * page-break-before: always">
 * 
 *  <tr style="background: transparent">
 * 
 *      <td height="30" style="border-top: 1px solid #000000; border-bottom: 1px solid #000000; 
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">0</font></p>
 *      </td>
 * 
 *      <td bgcolor="#2a6099" style="background: #2a6099; border-top: 1px solid #000000;
 *      border-bottom: 1px solid #000000; border-left: 1px solid #000000; 
 *      border-right: none; padding-top: 0.05cm; padding-bottom: 0.05cm; 
 *      padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 14pt" color="#ffffff"><b>1</b></font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000; 
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">2</font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000;
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">3</font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000; 
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">4</font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000; 
 *      border-left: 1px solid #000000; border-right: none; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">5</font></p>
 *      </td>
 * 
 *      <td style="border-top: 1px solid #000000; border-bottom: 1px solid #000000;
 *      border-left: 1px solid #000000; border-right: 1px; padding-top: 0.05cm;
 *      padding-bottom: 0.05cm; padding-left: 0.05cm; padding-right: 0.05cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">...</font></p>
 *      </td>
 * 
 *      <td style="border: 1px solid #000000; padding: 0.05cm">
 *      <p align="center"><font size="2" style="font-size: 12pt">n</font></p>
        </td>
 * 
 *  </tr>
 * 
 * </table>
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Para escrever e ler a fita, você deve sempre fazê-lo por meio do endereço
 * virtual. Logo, a primeira posição da fita, aonde o cursor deve ser posicionado
 * inicialmente será na célula de endereço 1.073.741.824. Na prática, este
 * endereço virtual corresponde ao endereço 1 no arranjo realmente alocado na
 * memória do exemplo. Se a cabeça de leitura move o cursor para a direita para
 * ler e gravar na célula, então na fita virtual corresponde ao endereço 
 * 1.073.741.825, e no arranjo real 2. Se o cursor vai para a esquerda, na fita
 * virtual corresponde ao endereço 1.073.741.823 e na fita real 0. Mais um movimento
 * da cabeça de leitura para a esquerda estando o endereço real em 0, se teria 
 * -1 e sairia dos limites do arranjo. Logo, se mover para a esquerda, é preciso
 * alocar mais uma célula nesta direção, copiar todos os elementos à direita dela,
 * fazê-la ficar com o marcador de branco e mudar a célula pivô para uma casa adiante,
 * matendo a referência de que célula iniciou-se apontando para ela. O mesmo se
 * a cabeça de leitura for posicionada para um endereço além da extremidade
 * posterior da fita. É preciso alocar mais uma célula, colocar um marcador branco
 * nela, mas dessa vez os símbolos não são movidos nem a célula pivô muda de posição.
 * 
 * <br><br>
 * 
 * O redimensionamento é feito automáticamente ao ler ou escrever a fita. Ela se 
 * autoredimensiona para manter a consistência dos endereços virtuais e ter células
 * em cada posição que o cursor é movido, teoricamente até a celula virtual 0 à 
 * esquerda e a célula virtual 2.147.483.647 à direita, que é o limite máximo de
 * células da fita. O arranjo real vai crescendo conforme necessário para o 
 * processamento do programa incrito na função de transição.
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
     * Inicializa a fita com a cadeia de entrada, a partir do endereço virtual
     * 1.073.741.824. Todas as demais células da fita são preenchidas com branco.
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
    
    
    /**
     * Escrever um símbolo em uma célula da fita. Caso a célula não exista, será 
     * redimensionado o arranjo, à esquerda ou à direita da extremidade da fita
     * e atribuído um símbolo de branco para cada nova célula criada e escrito
     * o símbolo na célula referenciada.
     * 
     * @param celula endereço virtual da célula.
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
     * Ler um símbolo em uma célula da fita. Caso a célula não exista, será 
     * redimensionado o arranjo, à esquerda ou à direita da extremidade da fita
     * e atribuído um símbolo de branco a cada nova célula criada.
     * 
     * @param celula endereço virtual da célula.
     * 
     * @return Símbolo lido, ou null, caso o índice da célula esteja fora da faixa.
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
    
    
    /**
     * Redimensionar a fita, adicionando células à esquerda ou à direita nas
     * extremidades. 
     * 
     * @param qtdEsquerda quantidade de células a adicionar à esquerda da fita.
     * 
     * @param qtdDireita quantidade de células a adicionar à direita da fita.
     * 
     * @return Se true, a fita foi redimensionada. Se false, a fita não foi
     * redimensionada.
     */
    public boolean redimensionar(int qtdEsquerda, int qtdDireita) {
        
        if (qtdEsquerda >= 0 && qtdDireita >= 0) {
        
            Simbolo[] celulasTmp = new Simbolo[celulas.length + qtdDireita +
            qtdEsquerda];

            boolean deslocou =  false;

            if (qtdEsquerda > 0) {
                System.arraycopy(celulas, 0, celulasTmp, qtdEsquerda , celulas.length);
                for (int i = 0; i < qtdEsquerda; i++) {
                    celulasTmp[i] = alfabetoFita.getSimboloBranco();
                }
                celulaPivo = celulaPivo + qtdEsquerda;
                deslocou = true;
            } 

            if (qtdDireita > 0) {
                if (!deslocou) {
                    System.arraycopy(celulas, 0, celulasTmp, 0 , celulas.length);
                }
                for (int i = celulas.length; i < celulasTmp.length; i++) {
                    celulasTmp[i] = alfabetoFita.getSimboloBranco();
                }
            }

            celulas = celulasTmp;
            
            return true;
        
        } else {
            
            return false;
            
        }

    }
    
    
    public boolean redimensionar(int posicao, int qtdEsquerda, int qtdDireita) {
        
        if (qtdEsquerda >= 0 && qtdDireita >= 0) {
        
            Simbolo[] celulasTmp = new Simbolo[celulas.length + qtdDireita +
            qtdEsquerda];

            boolean deslocou =  false;

            if (qtdEsquerda > 0) {
                System.arraycopy(celulas, 0, celulasTmp, qtdEsquerda , celulas.length);
                for (int i = 0; i < qtdEsquerda; i++) {
                    celulasTmp[i] = alfabetoFita.getSimboloBranco();
                }
                celulaPivo = celulaPivo + qtdEsquerda;
                deslocou = true;
            } 

            if (qtdDireita > 0) {
                if (!deslocou) {
                    System.arraycopy(celulas, 0, celulasTmp, 0 , celulas.length);
                }
                for (int i = celulas.length; i < celulasTmp.length; i++) {
                    celulasTmp[i] = alfabetoFita.getSimboloBranco();
                }
            }

            celulas = celulasTmp;
            
            return true;
        
        } else {
            
            return false;
            
        }

    }
    
    
    /**
     * Marcar todas as células da fita com marcador de branco.
     */
    public void limpar() {
        for (int i = 0; i < celulas.length; i++) {
            celulas[i] = alfabetoFita.getSimboloBranco();
        }
    }
    
    
    /**
     * Obter o endereço virtual da primeira célula da fita. Se a fita for infinita
     * à esquerda, a posição será 1.073.741.824. Se a fita for finita à esquerda,
     * a posição será 0.
     * 
     * @return Índice virtual da primeira célula da fita.
     */
    public int getCelulaInicial() {
        if (infinitaEsquerda) {
            return POSICAO_INICIAL;
        } else {
            return 0;
        }
    }
    
    
    /**
     * Obter o endereço absoluto da célula pivô.
     * 
     * @return endereço absoluto da célula pivô.
     */
    public int getCelulaPivo() {
        return celulaPivo;
    }
    
    
    /**
     * Obter o endereço absoluto de uma célula no arranjo.
     * 
     * @param celula endereço virtual da célula.
     * 
     * @return endereço absoluto da célula no arranjo em memória.
     */
    public int getEnderecoAbsoluto(int celula) {
        
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
    
    
    /**
     * Obter as células da fita.
     * 
     * @return células da fita. 
     */
    public Simbolo[] getCelulas() {
        return celulas.clone();
    }
    
    
    /**
     * Obter o número de células da fita em expansão.
     * 
     * @return número de células da fita em expansão.
     */
    public int getComprimento() {
        return celulas.length;
    }

    
    /**
     * Indicador de fita infinita à esquerda.
     * 
     * @return Se true, a fita é infinita à esquerda. Se false, a fita é finita
     * à esquerda.
     */
    public boolean isInfinitaEsquerda() {
        return infinitaEsquerda;
    }
    
    
}