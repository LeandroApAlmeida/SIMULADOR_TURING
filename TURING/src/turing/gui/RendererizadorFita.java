package turing.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTextField;
import turing.classes.Fita;
import static turing.gui.Formatacao.formatarSimbolos;
import static turing.classes.Constantes.SIMBOLO_BRANCO;
import static turing.gui.Sufixos.SUFIXO_CURSOR;
import static turing.gui.Sufixos.SUFIXO_CEL_PIVO;

/**
 *
 * @author leand
 */
public class RendererizadorFita implements javax.swing.table.TableCellRenderer {
    
    
    private Icon icone;
    
    private String branco;
    
    private Fita fita;

    
    public RendererizadorFita(Fita fita, boolean iconePadrao) {
        
        this.fita = fita;
        
        branco = String.valueOf(SIMBOLO_BRANCO);
        
        try {
            if (iconePadrao) {
                icone = new ImageIcon(getClass().getResource("/turing/icones/cursor_icon_2.png"));
            } else {
                icone = new ImageIcon(getClass().getResource("/turing/icones/cursor_icon_13.png"));
            }
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
        
            if (text.contains(SUFIXO_CURSOR)) {
                
                textField.setForeground(Color.BLACK);
                
                Font font = new Font(
                    table.getFont().getFontName(),
                    Font.BOLD,
                    table.getFont().getSize()
                );
                
                textField.setFont(font);
                
                text = formatarSimbolos(text.replace(SUFIXO_CURSOR, ""));
                
                textField.setBackground(Color.WHITE);
                textField.setText(text);
                textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                
                if (icone != null) {
                    
                    textField.setIcone(icone);
                    
                }
                
            } else {
                
                if (text.contains(SUFIXO_CEL_PIVO)) {
                    
                    textField.setBorder(
                        BorderFactory.createLineBorder(
                            Color.BLUE,
                            1,
                            false
                        )
                    );
                    
                    text = text.replace(SUFIXO_CEL_PIVO, "");
                    
                } else {
                    
                    textField.setBorder(BorderFactory.createEmptyBorder());
                    
                }
                
                if (text.contains(branco)) {
                    textField.setForeground(new Color(220, 220, 220));
                }
                
                text = formatarSimbolos(text);
                
                textField.setBackground(Color.WHITE);

                textField.setText(text);
                
            }
        
        } else {
            
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
            textField.setText(text);
            textField.setBorder(BorderFactory.createEmptyBorder());
            
        }
        
        return textField;
        
    }
    
    
    public Fita getFita() {
        return this.fita;
    }
    
    
}
