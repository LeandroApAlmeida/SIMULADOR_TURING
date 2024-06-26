package turing.gui;

import javax.swing.JOptionPane;
import turing.arquivo.ArquivoAjuda;

/**
 * Tela para exibição do texto de ajuda ao usuário, inserido como recurso no
 * .jar do projeto.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class TelaAjuda extends javax.swing.JDialog {


    /**
     * Constructor padrão.
     * 
     * @param parent tela proprietária.
     * 
     * @param titulo título do conteúdo de ajuda.
     * 
     * @param arquivo caminho do arquivo de recurso. 
     */
    public TelaAjuda(java.awt.Frame parent, String titulo, String arquivo) {
        
        super(parent, true);
        
        initComponents();
        
        setLocationRelativeTo(parent);
        
        jlTitle.setText(" " + titulo);
        
        try {
            String html = new ArquivoAjuda(arquivo).ler();
            jtAjuda.setText(html);
            jtAjuda.setCaretPosition(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Erro",
                JOptionPane.OK_OPTION
            );
        }
        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jtAjuda = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        jlTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AJUDA");
        setResizable(false);

        jtAjuda.setEditable(false);
        jtAjuda.setContentType("text/html"); // NOI18N
        jtAjuda.setText("");
        jtAjuda.setFocusable(false);
        jScrollPane2.setViewportView(jtAjuda);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jlTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlTitle.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlTitle;
    private javax.swing.JTextPane jtAjuda;
    // End of variables declaration//GEN-END:variables
}
