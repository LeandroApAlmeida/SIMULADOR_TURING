package turing.classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe que representa uma lista de estados.
 * 
 * @author Leandro Ap. de Almeida
 */
public class ListaEstados implements Iterable<Estado> {
    
    
    /**Lista dos estados.*/
    protected final List<Estado> estados;
    
    /**Comparador utilizado para ordenar os estados.*/
    protected final Comparator comparator;

    
    /**
     * Constructor padrão.
     */
    public ListaEstados() {
        estados = new ArrayList<>();
        comparator = new Comparator();
    }
    
    
    /**
     * Inserir um novo estado. Se o estado já foi inserido anteriormente, não 
     * haverá duplicidade, e ele será ignorado.
     * 
     * @param estado novo estado a ser inserido.
     * 
     * @return Se true, o estado foi inserido. Se false, o estado não foi
     * inserido.
     */
    public boolean inserirEstado(Estado estado) {
        if (!estados.contains(estado)) {
            estados.add(estado);
            ordenar();
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * Remover um estado.
     * 
     * @param estado estado a ser removido.
     * 
     * @return Se true, o estado foi removido. Se false, o estado não foi removido.
     */
    public boolean removerEstado(Estado estado) {
        return estados.remove(estado);
    }
    
    
    /**
     * Alterar o rótulo de um estado.
     * 
     * @param estado estado.
     * 
     * @param novoRotulo novo rótulo do estado.
     * 
     * @return Se true, o estado foi alterado. Se False, o estado não foi alterado.
     * 
     * @throws Exception rótulo do estado incorreto.
     */
    public boolean alterarEstado(Estado estado, String novoRotulo) throws Exception {
        boolean existe = false;
        for (Estado est : estados) {
            if (est.getRotulo().equals(novoRotulo)) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            return estado.setRotulo(novoRotulo);
        } else {
            return false;
        }
    }
    
    
    /**
     * Obter o estado no índice especificado.
     * 
     * @param indice índice do estado.
     * 
     * @return estado no índice especificado, ou null, caso o índice seja
     * inválido.
     */
    public Estado getEstado(int indice) {
        if (indice >= 0 && indice < estados.size()) {
            return estados.get(indice);
        } else {
            return null;
        }
    }
    
    
    /**
     * Obter o estado com o rótulo especificado.
     * 
     * @param rotulo rótulo do estado.
     * 
     * @return estado com o rótulo especificado, ou null, caso o rótulo seja
     * inválido.
     */
    public Estado getEstado(String rotulo) {
        Estado estado = null;
        for (int i = 0; i < estados.size(); i++) {
            if (estados.get(i).getRotulo().equals(rotulo)) {
                estado = estados.get(i);
                break;
            }
        }
        return estado;
    }
    
    
    /**
     * Obter o número total de estados.
     * @return número total de estados.
     */
    public int getComprimento() {
        return estados.size();
    }
    
    
    /**
     * Verificar se um estado pertence ao conjunto.
     * 
     * @param estado estado.
     * 
     * @return Se true, o estado pertence ao conjunto. Se false, o estado não 
     * pertence ao conjunto.
     */
    public boolean contemEstado(Estado estado) {
        return estados.contains(estado);
    }
    
    
    /**
     * Remover todos os estados.
     */
    public void esvaziar() {
        estados.clear();
    }
    
    
    public void ordenar() {
        estados.sort(comparator);
    }
    
    
    /**
     * Iterator para uso em laços <b>foreach</b>. Exemplo:
     * 
     * <br><br>
     * 
     * <pre>
     * for (Estado estado: estados) {
     *     System.out.println(estado);
     * }
     * </pre>
     * 
     * Sem este Iterator, poderia ser usado apenas o <b>for</b> padrão:
     * 
     * <br><br>
     * 
     * <pre>
     * for (int i = 0; i < estados.getComprimento; i++) {
     System.out.println(estados.getEstado(i));
 }
 </pre>
     * 
     * @return instância de Iterator
     */
    @Override
    public Iterator<Estado> iterator() {
        return estados.iterator();
    }
    
    
    /**
     * Classe para ordenamento dos estados.
     */
    protected class Comparator implements java.util.Comparator<Estado> {

        @Override
        public int compare(Estado e1, Estado e2) {
            return e1.getRotulo().compareTo(e2.getRotulo());
        }
 
    }
    
    
}
