package turing.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Componente JTextField que permite o desenho de ícone.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public final class JTextFieldIcone extends JTextField {

    
    /**Borda do componente.*/
    private Border borda;
    
    /**Ícone de componente.*/
    private Icon icone;

    
    /**
     * Constructor padrão.
     */
    public JTextFieldIcone() {
        icone = null;
        setHorizontalAlignment(JTextField.CENTER);
        setBorder(BorderFactory.createEmptyBorder());
        setText("");
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        setOpaque(true);
    }
    
    
    /**
     * Definir um ícone para o componente.
     * 
     * @param icone ícone para o componente. 
     */
    public void setIcone(Icon icone) {
        this.icone = icone;
    }

    
    /**
     * Definir a borda do componente.
     * 
     * @param border borda do componente.
     */
    @Override
    public void setBorder(Border border) {
        this.borda = border;
        super.setBorder(border);
    }

    
    /**
     * Redesenha o componente. Caso tenha definido um ícone, desenha o mesmo
     * no lado esquerdo do texto.
     * 
     * @param graphics opções gráficas do componente.
     */
    @Override
    protected void paintComponent(Graphics graphics) {  
        super.paintComponent(graphics);
        if (icone != null) {
            Insets insets = borda.getBorderInsets(this);
            icone.paintIcon(this, graphics, insets.left + 1, insets.top + 2);
        }
    }
    
    
}