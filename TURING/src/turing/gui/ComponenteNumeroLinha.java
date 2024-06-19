package turing.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * Componente para exibição dos números das linhas no editor de código do
 * programa. O componente é acoplado ao JScroller do editor passando a ser
 * renderizado por este, no lado esquerdo do editor.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public final class ComponenteNumeroLinha extends JComponent {


    /**Alinha os números das linhas à esquerda.*/
    public static final int ALINHAMENTO_ESQUERDA = 0;
    /**Alinha os números das linhas à direita.*/
    public static final int ALINHAMENTO_DIREITA = 1;
    /**Alinha os números das linhas ao centro.*/
    public static final int ALINHAMENTO_CENTRALIZADO = 2;
    
    /**Preenchimento horizontal do componente.*/
    private static final int PREENCHIMENTO_HORIZONTAL = 6;
    /**Preenchimento vertical do componente.*/
    private static final int PREENCHIMENTO_VERTICAL = 3;

    /**Componente JTextArea do editor.*/
    private final JTextArea jTextArea;
    
    /**Alinhamento do número da linha no componente.*/
    private final int alinhamento;
    
    
    /**
     * Constructor padrão. O componente será ajustado de acordo com as mudanças
     * no componente JTextArea, imprimindo o número correspondente de linhas de 
     * acordo com o número de linhas deste.
     * 
     * @param jTextArea componente JTextArea do editor.
     * 
     * @param alinhamento alinhamento do número da linha no componente.
     */
    public ComponenteNumeroLinha(JTextArea jTextArea, int alinhamento){
        
        super();
        
        this.alinhamento = alinhamento;
        
        this.jTextArea = jTextArea;
        
        ajustar();
        
        repaint();
        
        this.jTextArea.getDocument().addDocumentListener(
            new DocumentListener() {

                @Override
                public void changedUpdate(DocumentEvent arg0) {
                    ajustar();
                }

                @Override
                public void insertUpdate(DocumentEvent arg0) {
                    ajustar();
                }

                @Override
                public void removeUpdate(DocumentEvent arg0) {
                    ajustar();
                }
    
            }    
        );
        
    }

    
    /**
     * Ajustar o componente para atualizar o número de linhas correspondente ao
     * número de linhas do componente JTextArea.
     */
    private void ajustar(){

        int numeroLinhas = jTextArea.getLineCount();

        if (getGraphics() == null){
            return;
        }

        int comprimento = jTextArea.getGraphics().getFontMetrics().stringWidth(String
        .valueOf(numeroLinhas)) + 2 * PREENCHIMENTO_HORIZONTAL;

        JComponent componente = (JComponent)getParent();

        if (componente == null) {
            return;
        }

        Dimension dimensao = componente.getPreferredSize();

        if (componente instanceof JViewport janela) {
            Component proprietario = janela.getParent();
            if (proprietario != null && proprietario instanceof JScrollPane){
                JScrollPane scroller = (JScrollPane)janela.getParent();
                dimensao = scroller.getViewport().getView().getPreferredSize();
            }
        }

        if (comprimento > getPreferredSize().width || comprimento < getPreferredSize().width){
            setPreferredSize(
                new Dimension(
                    comprimento + 2 * PREENCHIMENTO_HORIZONTAL,
                    dimensao.height
                )
            );
            revalidate();
            repaint();
        }

    }


    /**
     * Ao renderizar o componente, imprime o número relativo de cada linha, 
     * correspondendo ao número da linha no componente JTextArea.
     * 
     * @param g opções gráficas do componente.
     */
    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setColor(new Color(240, 240, 240));

        g2d.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(getForeground());
        
        for (int i = 0; i < jTextArea.getLineCount(); i++){

            Rectangle retangulo;
            
            try {
                retangulo = jTextArea.modelToView2D(
                    jTextArea.getLineStartOffset(i)
                ).getBounds();
            } catch(BadLocationException e){
                retangulo = new Rectangle();
            }
            
            String texto = String.valueOf(i + 1);

            int y = retangulo.y + retangulo.height - PREENCHIMENTO_VERTICAL;
            int x = PREENCHIMENTO_HORIZONTAL;

            switch (alinhamento){

                case ALINHAMENTO_DIREITA -> {
                    x = getPreferredSize().width - g.getFontMetrics()
                    .stringWidth(texto) - PREENCHIMENTO_HORIZONTAL;
                }

                case ALINHAMENTO_CENTRALIZADO -> {
                    x = getPreferredSize().width/2 - g.getFontMetrics()
                    .stringWidth(texto)/2;
                }

            }

            g2d.drawString(texto, x, y);

        }

    }


}