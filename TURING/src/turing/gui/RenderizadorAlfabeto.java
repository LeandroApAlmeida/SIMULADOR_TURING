package turing.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import turing.classes.AlfabetoFita;
import turing.classes.Simbolo;

public class RenderizadorAlfabeto extends DefaultListCellRenderer {
    
    
    private static final long serialVersionUID = 1L;
    
    private final AlfabetoFita alfabetoFita;

    
    public RenderizadorAlfabeto(AlfabetoFita alfabetoFita) {
        setOpaque(true);
        this.alfabetoFita = alfabetoFita;
    }

    
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
