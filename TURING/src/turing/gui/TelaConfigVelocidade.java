package turing.gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class TelaConfigVelocidade extends javax.swing.JDialog implements ChangeListener {

    
    private final OuvinteConfigSimulacaoAutomatica ouvinte;
    
    private final String titulo = "VELOCIDADE DE EXECUÇÃO: ";


    public TelaConfigVelocidade(java.awt.Frame parent, 
    OuvinteConfigSimulacaoAutomatica ouvinte, int tempoAtual) {
        super(parent, false);
        initComponents();
        this.ouvinte = ouvinte;
        jSlider1.setValue(tempoAtual);
        float tempo = ((float)tempoAtual / 1000);
        setTitle(titulo + String.format("%.2f", tempo) + " seg.");
        jSlider1.addChangeListener(this);
    }
    
    
    private void notificarOuvinte() {
        int tempoExecucao = (jSlider1.getValue() / 100) * 100;
        float tempo = ((float)tempoExecucao / 1000);
        setTitle(titulo + String.format("%.2f", tempo) + " seg.");
        ouvinte.velocidadeSimulacaoAutomaticaAtualizada(tempoExecucao);
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e) {
        int tempoExecucao = (jSlider1.getValue() / 100) * 100;
        float tempo = ((float)tempoExecucao / 1000);
        setTitle(titulo + String.format("%.2f", tempo) + " seg.");
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("VELOCIDADE DE EXECUÇÃO");
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        jSlider1.setMaximum(10000);
        jSlider1.setMinimum(100);
        jSlider1.setPaintTicks(true);
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider1MouseReleased(evt);
            }
        });
        jSlider1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSlider1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseReleased
        notificarOuvinte();
    }//GEN-LAST:event_jSlider1MouseReleased

    private void jSlider1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSlider1KeyReleased
        notificarOuvinte();
    }//GEN-LAST:event_jSlider1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider jSlider1;
    // End of variables declaration//GEN-END:variables


}
