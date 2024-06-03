package turing.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTextField;
import turing.classes.AlfabetoFita;
import static turing.gui.Constantes.COR_CURSOR_FITA;
import static turing.gui.Constantes.formatarSimbolos;

/**
 *
 * @author leand
 */
public class RendererizadorFita implements javax.swing.table.TableCellRenderer {
    
    
    private Icon icone;
    
    private String branco;

    
    public RendererizadorFita() {
        
        branco = String.valueOf(AlfabetoFita.BRANCO);
        
        try {
            icone = new ImageIcon(getClass().getResource("/turing/icones/cursor_icon_2.png"));
        } catch (Exception ex) {
            icone = null;
        }
        
    }
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
    boolean isSelected, boolean hasFocus, int row, int column) {
        
        JTextFieldCelula textField = new JTextFieldCelula();
        
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setForeground(Color.BLACK);
        textField.setFont(table.getFont());
        textField.setOpaque(true);
        String text = (String)value;
        
        if (text != null) {
        
            if (text.contains("*")) {
                
                textField.setForeground(Color.BLACK);
                
                Font font = new Font(
                    table.getFont().getFontName(),
                    Font.BOLD,
                    table.getFont().getSize()
                );
                
                textField.setFont(font);
                
                text = formatarSimbolos(text.replace("*", ""));
                
                textField.setBackground(COR_CURSOR_FITA);
                textField.setText(text);
                textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                
                if (icone != null) {
                    textField.setIcone(icone);
                }
                
            } else {
                
                if (text.contains(branco)) {
                    textField.setForeground(new Color(220, 220, 220));
                }
                
                text = formatarSimbolos(text);
                
                textField.setBackground(Color.WHITE);
                textField.setText(text);
                textField.setBorder(BorderFactory.createEmptyBorder());
                
            }
        
        } else {
            
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
            textField.setText(text);
            textField.setBorder(BorderFactory.createEmptyBorder());
            
        }
        
        return textField;
        
    }
    
    
}
