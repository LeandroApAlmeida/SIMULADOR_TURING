package turing.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextField;


public class RenderizadorTransicao implements javax.swing.table.TableCellRenderer {
    
    
    public static final String CELULA_VAZIA = "[nenhum]";
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
    boolean isSelected, boolean hasFocus, int row, int column) {
        
        JTextField campoTexto = new JTextField();
        campoTexto.setHorizontalAlignment(JTextField.CENTER);
        campoTexto.setOpaque(true);
        campoTexto.setBorder(null);
        
        switch (column) {
            
            case 0 -> {
                campoTexto.setText(String.valueOf(row + 1));
                campoTexto.setBackground(new Color(240,240,240));
            }

            default -> {
                if (value != null) {
                    campoTexto.setText(String.valueOf(value));
                } else {
                    campoTexto.setText(CELULA_VAZIA);
                }
            }
 
        }
        
        return campoTexto;
        
    }
    
    
}