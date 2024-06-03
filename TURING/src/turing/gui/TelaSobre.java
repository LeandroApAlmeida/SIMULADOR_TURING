package turing.gui;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.JOptionPane;

public class TelaSobre extends javax.swing.JDialog {


    public TelaSobre(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
        Font font = jtfUrl.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        jtfUrl.setFont(font.deriveFont(attributes));
    }
    
    
    private void abrirURL() {
        try {
            String url_open = jtfUrl.getText();
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jtfUrl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SOBRE");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/images/Turing.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 3, 16)); // NOI18N
        jLabel2.setText("SIMULADOR DE MÁQUINA DE TURING");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel3.setText("Autor: ");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        jLabel4.setText("Leandro Ap. de Almeida");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel5.setText("Versão:");

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        jLabel6.setText("1.0 (02 de junho de 2024)");

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel8.setText("Disponível em: ");

        jtfUrl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jtfUrl.setForeground(java.awt.Color.blue);
        jtfUrl.setText("https://github.com/LeandroApAlmeida/SIMULADOR_TURING");
        jtfUrl.setToolTipText("Abrir a página");
        jtfUrl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtfUrl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jtfUrlMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jtfUrl))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel8)
                        .addGap(13, 13, 13)
                        .addComponent(jtfUrl))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtfUrlMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtfUrlMouseReleased
        abrirURL();
    }//GEN-LAST:event_jtfUrlMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jtfUrl;
    // End of variables declaration//GEN-END:variables
}
