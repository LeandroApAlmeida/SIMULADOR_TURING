package turing.gui;

import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class JTextFieldCelula extends JTextField {

    
    private Border borda;
    
    private Icon icone;

    
    public JTextFieldCelula() {
        icone = null;
    }

    
    @Override
    public void setBorder(Border border) {
        this.borda = border;
        if (icone == null) {
            super.setBorder(border);
        } else {
            Border margem = BorderFactory.createEmptyBorder(0, this.getWidth()/2, 0, 0);
            super.setBorder(BorderFactory.createCompoundBorder(border, margem));
        }
    }

    
    @Override
    protected void paintComponent(Graphics graphics) {
        
        super.paintComponent(graphics);
        
        if (icone != null) {
            
            Insets insets = borda.getBorderInsets(this);
            
            icone.paintIcon(this, graphics, insets.left + 1, insets.top + 2);
            
        }
        
    }

    
    public void setIcone(Icon icone) {
        this.icone = icone;
    }
    
    
}