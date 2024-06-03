package turing.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Alfabeto da fita (Γ). 
 * 
 * <br><br>
 * 
 * O alfabeto da fita contém o alfabeto de entrada (Σ), o alfabeto auxiliar (V),
 * mais todos os símbolos reservados, como o símbolo de branco (β), o símbolo de 
 * início da fita (⊛) e o símbolo de delimitador de seções (#). É o total de
 * símbolos que podem ser inscritos na(s) fita(s) da máquina de Turing.
 * 
 * @author Leandro Ap. Almeida
 * 
 * @since 1.0
 */
public class AlfabetoFita extends Alfabeto {
    
    
    /**Caractere de início da fita, que a delimita à esquerda.*/
    public static final char INICIO_FITA = '*';
    
    /**Caractere de branco, para marcar as células que estão vazias.*/
    public static final char BRANCO = '_';
    
    /**Caractere de delimitador de seções de uma fita.*/
    public static final char DELIMITADOR_SECAO = '#';
    
    /**Conjunto dos símbolos reservados.*/
    private final List<Simbolo> simbolosReservados;

    
    /**
     * Constructor padrão. Inicializa o alfabeto.
     */
    public AlfabetoFita() {
        
        simbolosReservados = new ArrayList<>();
        
        simbolosReservados.add(new Simbolo(INICIO_FITA, false, true));
        simbolosReservados.add(new Simbolo(BRANCO, false, true));
        simbolosReservados.add(new Simbolo(DELIMITADOR_SECAO, false, true));
        
        simbolos.addAll(simbolosReservados);
        
    }

    
    /**
     * Inserir um novo símbolo no alfabeto. Após a inserção, faz o ordenamento
     * nesta sequência: Símbolos reservados > Alfabeto de entrada > Alfabeto
     * Auxiliar.
     * 
     * @param simbolo símbolo a ser inserido.
     * 
     * @return Se true, o símbolo foi inserido. Se false, o símbolo não foi
     * inserido.
     */
    @Override
    public boolean inserirSimbolo(Simbolo simbolo) {
        if (super.inserirSimbolo(simbolo)) {
            ordenar();
            return true;
        } else {
            return false;
        }
    }

    
    /**
     * Remover um símbolo do alfabeto. Só será permitido remover símbolos do
     * alfabeto de entrada e do alfabeto auxiliar. Não é permitido remover os
     * símbolos reservados.
     * 
     * @param simbolo símbolo a ser removido.
     * 
     * @return Se true, o símbolo foi removido. Se false, o símbolo não foi
     * removido.
     * 
     */
    @Override
    public boolean removerSimbolo(Simbolo simbolo) {
        if (!simbolo.isReservado()) {    
            return super.removerSimbolo(simbolo);
        } else {
            return false;
        }
    }
    
    
    /**
     * Obter o alfabeto de entrada.
     * 
     * @return alfabeto de entrada.
     */
    public Alfabeto getAlfabetoEntrada() {
        Alfabeto alfabetoEntrada = new Alfabeto();
        for (Simbolo simbolo : simbolos) {
            if (!simbolo.isReservado()) {
                if (!simbolo.isAuxiliar()) {
                    alfabetoEntrada.inserirSimbolo(simbolo);
                }
            }
        }
        return alfabetoEntrada;
    }
    
    
    /**
     * Obter o alfabeto auxiliar.
     * 
     * @return alfabeto auxiliar.
     */
    public Alfabeto getAlfabetoAuxiliar() {
        Alfabeto alfabetoAuxiliar = new Alfabeto();
        for (Simbolo simbolo : simbolos) {
            if (!simbolo.isReservado()) {
                if (simbolo.isAuxiliar()) {
                    alfabetoAuxiliar.inserirSimbolo(simbolo);
                }
            }
        }
        return alfabetoAuxiliar;
    }
    
    
    /**
     * Obter os símbolos reservados.
     * 
     * @return símbolos reservados.
     */
    public List<Simbolo> getSimbolosReservados() {
        List<Simbolo> reservados = new ArrayList<>(); 
        for (Simbolo simbolo : simbolos) {
            if (simbolo.isReservado()) {
                reservados.add(simbolo);
            }
        }
        return reservados;
    }
    
    
    /**
     * Obter o símbolo de início da fita.
     * 
     * @return símbolo de início da fita.
     */
    public Simbolo getSimboloInicio() {
        return simbolos.get(0);
    }
    
    
    /**
     * Obter o símbolo de branco.
     * 
     * @return símbolo de branco.
     */
    public Simbolo getSimboloBranco() {
        return simbolos.get(1);
    }
    
    
    /**
     * Obter o símbolo de delimitador de seção na fita.
     * 
     * @return símbolo de delimitador de seção na fita.
     */
    public Simbolo getSimboloDelimitador() {
        return simbolos.get(2);
    }
    
    
    /**
     * Definir um símbolo como pertencente ou não ao alfabeto auxiliar.
     * 
     * @param simbolo símbolo.
     * 
     * @param valor Se true, o símbolo pertencerá ao alfabeto auxiliar. Se false,
     * o símbolo não pertencerá ao alfabeto auxiliar.
     */
    public void setSimboloAuxiliar(Simbolo simbolo, boolean valor) {
        if (!simbolosReservados.contains(simbolo)) {
            for (int i = 0; i < simbolos.size(); i++) {
                if (simbolo.getCaracter() == simbolos.get(i).getCaracter()) {
                    simbolos.get(i).setAuxiliar(valor);
                    break;
                } 
            }
            ordenar();
        }
    }

    
    /**
     * Remover todos os símbolos do alfabeto de entrada e do alfabeto auxiliar.
     */
    @Override
    public void esvaziar() {
        super.esvaziar();
        simbolos.addAll(simbolosReservados);
    }

    
    /**
     * Ordena o alfabeto da fita. O ordenamento se dará da seguinte forma:
     * 
     * <br><br>
     * 
     * <ol>
     * 
     * <li>Símbolos reservados</li>
     * 
     * <br>
     * 
     * <li>Alfabeto de entrada</li>
     * 
     * <br>
     * 
     * <li>Alfabeto auxiliar</li>
     * 
     * </ol>
     * 
     * <br>
     * 
     * Cada um destes conjuntos estará ordenado em ordem crescente.
     */
    @Override
    public void ordenar() {
        
        Alfabeto alfabetoEntrada = getAlfabetoEntrada();
        Alfabeto alfabetoAuxiliar = getAlfabetoAuxiliar();

        alfabetoEntrada.ordenar();
        alfabetoAuxiliar.ordenar();
        
        simbolos.clear();
        
        simbolos.addAll(simbolosReservados);
        
        simbolos.addAll(alfabetoEntrada.simbolos);
        
        simbolos.addAll(alfabetoAuxiliar.simbolos);
         
    }

    
}