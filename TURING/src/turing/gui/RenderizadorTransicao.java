package turing.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;


/**
 * Renderizador para a JTable da tela Inserir Transição.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class RenderizadorTransicao implements javax.swing.table.TableCellRenderer {
    
    
    public static final String CELULA_VAZIA = "[nenhum]";
    
    
    /**
     * Obter o componente de renderização da célula da JTable. O componente nas
     * células da primeira coluna exibem a mesma cor de fundo que a dos cabeçalhos
     * das colunas, indicando que a celula não pode ser editada. Estas células
     * contém o número ordinal da fita. As demais células tem a cor de fundo 
     * padrão, e se o texto nelas for null, exibirá o texto "[nenhum]" indicando 
     * que nenhum valor foi seleciondo.
     * 
     * @param table JTable
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
        
        switch (column) {
            
            case 0 -> {
                textField.setText(String.valueOf(row + 1));
                textField.setBackground(new Color(240,240,240));
            }

            default -> {
                if (value != null) {
                    textField.setText(String.valueOf(value));
                } else {
                    textField.setText(CELULA_VAZIA);
                }
            }

 
        }
        
        return textField;
        
    }
    
    
}