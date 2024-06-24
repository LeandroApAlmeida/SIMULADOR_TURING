package turing.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import turing.classes.AlfabetoFita;
import turing.classes.Simbolo;

/**
 * Renderizador da lista de símbolos do Alfabeto da Fita.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class RenderizadorAlfabeto extends DefaultListCellRenderer {

    
    /**Alfabeto da fita.*/
    private final AlfabetoFita alfabetoFita;

    
    /**
     * Constructor padrão.
     * 
     * @param alfabetoFita alfabeto da fita.
     */
    public RenderizadorAlfabeto(AlfabetoFita alfabetoFita) {
        setOpaque(true);
        this.alfabetoFita = alfabetoFita;
    }

    
    /**
     * Obter o componente de renderização do item da JList do alfabeto da fita.
     * No caso, vai configurar a cor de cada símbolo de acordo com o contexto:
     * 
     * <br><br>
     * 
     * <ul>
     * 
     * <li>Símbolos reservados. Os símbolos reservados devem ter a cor cinza claro,
     * simulando o estado de desabilitado, já que estes símbolos são fixos na lista.</li><br>
     * 
     * <li>Alfabeto de entrada. O alfabeto de entrada deve ter a cor de fonte em
     * preto.</li><br>
     * 
     * <li>Alfabeto auxiliar. O alfabeto auxiliar deve ter a cor de fonte em
     * azul.</li>
     * 
     * </ul>
     * 
     * @param list JList do alfabeto de entrada.
     * 
     * @param value valor do item na lista.
     * 
     * @param index índice do item na lista.
     * 
     * @param isSelected estatus de item selecionado.
     * 
     * @param cellHasFocus estatus de item em foco.
     * 
     * @return Componente de exibição do item na JList.
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
    int index, boolean isSelected, boolean cellHasFocus) {
        
        Simbolo simbolo = alfabetoFita.getSimbolo(index);   

        Color background = Color.WHITE;
        Color foreground = Color.BLACK;
        Font font = list.getFont();

        if (simbolo.isReservado()) {
            foreground = new Color(150, 150, 150);
        } else {
            if (simbolo.isAuxiliar()) {
                foreground = Color.BLUE;
            }
        }

        if (isSelected) {
            background = list.getSelectionBackground();
        }

        Component component = super.getListCellRendererComponent(
            list,
            value,
            index,
            isSelected,
            cellHasFocus
        );

        component.setForeground(foreground);
        component.setBackground(background);
        component.setFont(font);

        return component;
        
    }
   
    
}
