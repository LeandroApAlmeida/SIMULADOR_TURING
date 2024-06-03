package turing.classes;

import java.util.Arrays;

/**
 * Fita da máquina de Turing. Uma fita é um arranjo de células e pode ser
 * infinita à esquerda e à direita, finita à esquerda e infinita à direita,
 * ou finita à esquerda e à direita, dependendo do modelo de máquina de Turing
 * implementado. Neste código não será implementado nenhum modelo com fita
 * limitada à direita e à esquerda, portando, este modelo não será codificado.
 * 
 * <br><br>
 * 
 * Uma máquina de turing pode ter uma ou mais fitas. Cada célula em uma fita
 * inicialmente contém um símbolo de branco, indicando que não há qualquer
 * símbolo do alfabeto de entrada ou do alfabeto auxiliar gravado nela. 
 * 
 * <br><br>
 * 
 * A fita é auto dimensionável. Caso se tente ler ou escrever uma célula além 
 * dos limites do arranjo de células, a classe redimensiona o arranjo para
 * adicionar a célula no índice requerido.
 * 
 * @author Leandro Ap. de Almeida
 */
public final class Fita {
    
    
    /**Limite máximo de células da fita*/
    private final int NUMERO_MAX_CELULAS = Integer.MAX_VALUE;
    
    /**Posição inicial na fita.*/
    private final int POSICAO_INICIAL = NUMERO_MAX_CELULAS / 2;
    
    /**Alfabeto dos símbolos que podem ser gravados na fita.*/
    private final AlfabetoFita alfabetoFita;
    
    /**Indicador de que a fita é infinita à esquerda.*/
    private final boolean infinitaEsquerda;
    
    /**Células da fita.*/
    private Simbolo[] celulas;
    
    /**
     * Célula pivô. Ela estabelece o vínculo entre um endereço virtual, que é
     * delimitado por todos os valores positivos de um número inteiro de 32
     * bits o que dá 2.147.483.647 valores, e o endereço real, que aponta para
     * uma posição inicialmente definida de um arranjo.
     */
    private int celulaPivo;

    
    /**
     * Constructor padrão. Inicializa a fita.
     * 
     * @param alfabetoFita alfabeto da fita.
     * 
     * @param infinitaEsquerda indicador de fita infinita à esquerda. Se true, a
     * fita é infinita à esquerda. Se false, a fita é finita à esquerda.
     * 
     * @param tamanhoInicial tamanho inicial do arranjo. Se houver necessidade,
     * o arranjo será redimensionado, aumentando o número de casas à esquerda
     * ou à direita da célula pivô.
     * 
     */
    public Fita(AlfabetoFita alfabetoFita, boolean infinitaEsquerda, int tamanhoInicial,
    int celulasAdicionais) {
        int numeroCelulas = tamanhoInicial + celulasAdicionais;
        this.celulas = new Simbolo[numeroCelulas];        
        this.alfabetoFita = alfabetoFita;
        this.infinitaEsquerda = infinitaEsquerda;
        if (infinitaEsquerda) {
            if (celulasAdicionais > 0) {
                this.celulaPivo = 1;
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
     * esquerda, delimitada pelo símbolo de início da fita. Todas as demais células
     * da fita são preenchidas com branco.
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
                
                celulas[celulas.length - 1] = simbolo;
                
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
                
                return celulas[celulas.length - 1];
                
            }
            
        }
        
    }
    
    
    public void ajustar(int qtdCelulas) {
        
        int valor = Math.abs(qtdCelulas);
        
        Simbolo[] celulasTmp = new Simbolo[celulas.length + valor];
        
        for (int i = 0; i < celulasTmp.length; i++) {
            celulasTmp[i] = alfabetoFita.getSimboloBranco();
        }
        
        if (qtdCelulas < 0) {
            System.arraycopy(celulas, 0, celulasTmp, valor , celulas.length);
            celulaPivo = celulaPivo + valor;
        } else {
            System.arraycopy(celulas, 0, celulasTmp, 0 , celulas.length);
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

    
    /**
     * Indicador de fita infinita à esquerda.
     * @return Se true, a fita é infinita à esquerda. Se false, a fita é finita
     * à esquerda.
     */
    public boolean isInfinitaEsquerda() {
        return infinitaEsquerda;
    }
    
    
}