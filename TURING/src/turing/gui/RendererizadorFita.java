package turing.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import static turing.gui.Formatacao.formatarSimbolos;
import static turing.classes.Constantes.SIMBOLO_BRANCO;
import static turing.gui.Sufixos.SUFIXO_CURSOR;
import static turing.gui.Sufixos.SUFIXO_CEL_PIVO;
import static turing.gui.Sufixos.SUFIXO_PONTO;

/**
 * Renderizador da JTable que representa as fitas da Máquina de Turing. 
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class RendererizadorFita implements javax.swing.table.TableCellRenderer {
    
    
    /**Ícone de seta.*/
    private Icon iconeSeta;
    
    /**Ícone de ponto.*/
    private Icon iconePonto;
    
    /**Símbolo de branco.*/
    private String branco;

    
    /**
     * Constructor padrão.
     * 
     * @param iconePadrao Estatus de ícone padrão. Se true, usa o ícone padrão.
     * Se false, usa o ícone menor.
     */
    public RendererizadorFita(boolean iconePadrao) {
        
        branco = String.valueOf(SIMBOLO_BRANCO);
        
        try {
            if (iconePadrao) {
                iconeSeta = new ImageIcon(getClass().getResource("/turing/icones/cursor_icon_2.png"));
            } else {
                iconeSeta = new ImageIcon(getClass().getResource("/turing/icones/cursor_icon_13.png"));
            }
            iconePonto = new ImageIcon(getClass().getResource("/turing/icones/dot_icon.png"));
        } catch (Exception ex) {
            iconeSeta = null;
        }
        
    }
    
    
    /**
     * Obter o componente de renderização da célula da JTable. O componente será
     * configurado de acordo com a seguinte regra:
     * 
     * <br><br>
     * 
     * <ul>
     * 
     * <li>&nbsp;Cada célula com símbolo de branco recebe o caractere β, tem 
     * borda vazia e texto em cinza claro para deixar com menos destaque.</li><br>
     * 
     * <li>&nbsp;A célula em que o cursor da Cabeça de Leitura/Escrita está
     * posicionado tem uma borda preta grossa, um ícone de seta à esquerda do 
     * texto e o caractere do símbolo em negrito.</li><br>
     * 
     * <li>&nbsp;A célula pivô (onde o cursor estava posicionado no início) tem 
     * uma borda azul fina. Ela serve para dar impressão de movimento quando é
     * a fita que se movimenta sob o cursor da Cabeça de Leitura/Escrita e marca
     * a expansão da fita quando é o cursor que se movimenta sobre a fita e esta
     * muda de tamanho.</li><br>
     * 
     * <li>&nbsp;A célula de símbolo com ponto tem uma borda preta grossa,
     * um ícone de ponto à esquerda do texto e o caractere do símbolo em
     * negrito.</li><br>
     * 
     * </ul>
     * 
     * @param table JTable que representa as fitas.
     * 
     * @param value texto na célula.
     * 
     * @param isSelected estatus de célula selecionada.
     * 
     * @param hasFocus estatus de célula em foco.
     * 
     * @param row índice da linha.
     * 
     * @param column índice da coluna.
     * 
     * @return Componente para renderização da célula.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
    boolean isSelected, boolean hasFocus, int row, int column) {
        
        JTextFieldIcone textField = new JTextFieldIcone();
        textField.setFont(table.getFont());
        String text = (String)value;

        if (text != null) {
        
            if (text.endsWith(SUFIXO_CURSOR) || text.endsWith(SUFIXO_PONTO)) {
                
                Font font = new Font(
                    table.getFont().getFontName(),
                    Font.BOLD,
                    table.getFont().getSize()
                );
                
                textField.setFont(font);

                textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                
                if (text.endsWith(SUFIXO_CURSOR)) {
                    if (iconeSeta != null) {
                        textField.setIcone(iconeSeta);
                    }
                } else {
                    if (iconePonto != null) {
                        textField.setIcone(iconePonto);
                    }
                }
                
            } else {
                
                if (text.endsWith(SUFIXO_CEL_PIVO)) {
                    
                    textField.setBorder(
                        BorderFactory.createLineBorder(
                            Color.BLUE,
                            1,
                            false
                        )
                    );
                    
                    text = text.replace(SUFIXO_CEL_PIVO, "");
                    
                }
                
                if (text.contains(branco)) {
                    textField.setForeground(new Color(220, 220, 220));
                }
                
            }
            
            text = formatarSimbolos(text);
            textField.setText(text);
        
        }
        
        return textField;
        
    }
    
    
}
