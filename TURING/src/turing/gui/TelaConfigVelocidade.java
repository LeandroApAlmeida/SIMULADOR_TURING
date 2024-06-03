package turing.gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class TelaConfigVelocidade extends javax.swing.JDialog implements ChangeListener {

    
    private final OuvinteTemporizador ouvinte;

    
    public TelaConfigVelocidade(java.awt.Frame parent, OuvinteTemporizador ouvinte, 
    int tempoAtual) {
        super(parent, false);
        initComponents();
        this.ouvinte = ouvinte;
        jSlider1.setValue(tempoAtual / 100);
        jSlider1.addChangeListener(this);
    }
    
    
    
    @Override
    public void stateChanged(ChangeEvent e) {
        int tempoExecucao = (jSlider1.getMaximum() / jSlider1.getValue()) * 100;
        ouvinte.temporizadorAtualizado(tempoExecucao);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("VELOCIDADE DE EXECUÇÃO");
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        jSlider1.setMaximum(30);
        jSlider1.setMinimum(1);
        jSlider1.setPaintTicks(true);
        jSlider1.setValue(1);

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider jSlider1;
    // End of variables declaration//GEN-END:variables

}
