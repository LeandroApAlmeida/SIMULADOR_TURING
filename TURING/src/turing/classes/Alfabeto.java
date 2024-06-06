package turing.classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe que representa um alfabeto da máquina de Turing. Um alfabeto é um 
 * conjunto de símbolos.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class Alfabeto implements Iterable<Simbolo> {
    
    
    /**Lista de todos os símbolos do alfabeto.*/
    protected final List<Simbolo> simbolos;
    
    /**Comparador utilizado para ordenar o alfabeto.*/
    protected final Comparator comparator;

    
    /**
     * Constructor padrão.
     */
    public Alfabeto() {
        simbolos = new ArrayList<>();
        comparator = new Comparator();
    }

    
    /**
     * Inserir um novo símbolo no alfabeto. Se o símbolo já foi inserido anteriormente,
     * não haverá duplicidade, e ele será ignorado.
     * 
     * @param simbolo novo símbolo a ser inserido.
     * 
     * @return Se true, o símbolo foi inserido. Se false, o símbolo não foi
     * inserido.
     */
    public boolean inserirSimbolo(Simbolo simbolo) {
        if (!simbolos.contains(simbolo)) {
            return simbolos.add(simbolo);
        } else {
            return false; 
        }
    }


    /**
     * Remover um símbolo do alfabeto.
     * 
     * @param simbolo símbolo a ser removido.
     * 
     * @return Se true, o símbolo foi removido. Se false, o símbolo não foi
     * removido.
     */
    public boolean removerSimbolo(Simbolo simbolo) {
        return simbolos.remove(simbolo);
    }
    
    
    /**
     * Alterar o caractere de um símbolo.
     * 
     * @param simbolo símbolo.
     * 
     * @param novoCaractere novo caractere do símbolo.
     * 
     * @return Se true, o símbolo foi alterado. Se false, o símbolo não foi alterado.
     */
    public boolean alterarSimbolo(Simbolo simbolo, char novoCaractere) {
        boolean existe = false;
        for (Simbolo simb : simbolos) {
           if (simb.getCaracter() == novoCaractere) {
               existe = true;
               break;
           } 
        }
        if (!existe) {
            simbolo.setCaracter(novoCaractere);
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * Obter o símbolo no índice especificado do alfabeto.
     * 
     * @param indice índice do símbolo.
     * 
     * @return Símbolo no índice especificado, ou null, caso o índice seja
     * inválido.
     */
    public Simbolo getSimbolo(int indice) {
        if (indice >= 0 && indice < simbolos.size()) {
            return simbolos.get(indice);
        } else {
            return null;
        }
    }
    
    
    /**
     * Obter o símbolo com o caractere especificado.
     * 
     * @param caractere caractere do símbolo.
     * 
     * @return Símbolo com o caractere especificado, ou null, caso o caractere
     * seja inválido.
     */
    public Simbolo getSimbolo(char caractere) {
        Simbolo simbolo = null;
        for (int i = 0; i < simbolos.size(); i++) {
            if (simbolos.get(i).getCaracter() == caractere) {
                simbolo = simbolos.get(i);
                break;
            }
        }
        return simbolo;
    }
    
    
    /**
     * Obter o número total de símbolos do alfabeto.
     * 
     * @return Número total de símbolos do alfabeto.
     */
    public int getComprimento() {
        return simbolos.size();
    }
    
    
    /**
     * Remover todos os símbolos do alfabeto.
     */
    public void esvaziar() {
        simbolos.clear();
    }
    
    
    /**
     * Iterator para uso em laços <b>foreach</b>. Exemplo:
     * 
     * <br><br>
     * 
     * <pre>
     * for (Simbolo simbolo: alfabeto) {
     *     System.out.println(simbolo);
     * }
     * </pre>
     * 
     * Sem este Iterator, poderia ser usado apenas o <b>for</b> padrão:
     * 
     * <br><br>
     * 
     * <pre>
     * for (int i = 0; i < alfabeto.getComprimento(); i++) {
     *     System.out.println(alfabeto.getSimbolo(i));
     * }
     * </pre>
     * 
     * @return instância de Iterator
     */
    @Override
    public Iterator<Simbolo> iterator() {
        return simbolos.iterator();
    }
    
    
    /**
     * Ordenar o alfabeto em ordem crescente dos caracteres dos símbolos.
     */
    public void ordenar() {
        simbolos.sort(comparator);
    }
    
    
    /**
     * Classe para ordenamento do alfabeto.
     */
    protected class Comparator implements java.util.Comparator<Simbolo> {

        @Override
        public int compare(Simbolo o1, Simbolo o2) {
            return Simbolo.compare(o1, o2);
        }
 
    }
    
    
}