package turing.gui;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import turing.classes.Estado;
import turing.classes.ConjuntoEstados;

public class TelaInserirEstado extends javax.swing.JDialog {

    
    private ConjuntoEstados conjuntoEstados;
    
    private Estado estado;
    
    private boolean cancelado;
    
    private boolean modoEdicao;


    public TelaInserirEstado(java.awt.Frame parent, ConjuntoEstados conjuntoEstados) {
        
        super(parent, true);
        
        this.conjuntoEstados = conjuntoEstados;
        cancelado = true;
        modoEdicao = false;
        
        initComponents();
        setLocationRelativeTo(parent);
        
    }
    
    
    public TelaInserirEstado(java.awt.Frame parent, Estado estado,
    ConjuntoEstados conjuntoEstados) {
        
        this(parent, conjuntoEstados);
        
        this.estado = estado;
        modoEdicao = true;
        
        jtaEstado.setText(estado.getRotulo());
        jcbEstadoTerminal.setSelected(estado.isTerminal());
        
        setTitle("ALTERAR ESTADO");
        jbInserir.setText("Alterar");
        jlFrase.setText("Estado:");
        
    }
    
    
    private void inserir() {
        
        String texto = jtaEstado.getText();
        boolean terminal = jcbEstadoTerminal.isSelected();
        
        boolean erro = false;
        
        if (!texto.isEmpty()) {
            
            try {

                if (!modoEdicao) {
                    
                    Estado[] listaEstados = null;

                    if (texto.contains(",")) {

                        texto = texto.replace(" ", "");

                        String[] secoes = texto.split(",");

                        listaEstados = new Estado[secoes.length];

                        for (int i = 0; i < secoes.length; i++) {
                            if (Estado.rotuloValido(secoes[i])) {
                                listaEstados[i] = new Estado(secoes[i], terminal);
                            } else {
                                erro = true;
                                break;
                            }
                        }

                    } else {

                        if (Estado.rotuloValido(texto)) {
                            listaEstados = new Estado[1];
                            listaEstados[0] = new Estado(texto, terminal);
                        } else {
                            erro = true;
                        }

                    }
                    
                    if (!erro) {
                        if (listaEstados != null) {
                            for (Estado est : listaEstados) {
                                conjuntoEstados.inserirEstado(est);
                            }
                        }
                    }

                } else {

                    if (Estado.rotuloValido(texto)) {
                        conjuntoEstados.alterarEstado(estado, texto);
                        conjuntoEstados.setEstadoTerminal(estado, terminal);
                    } else {
                        erro = true;
                    }

                }

                if (!erro) {

                    cancelado = false;
                    setVisible(false);

                } else {

                    JOptionPane.showMessageDialog(this,
                        """
                        Sintaxe incorreta. O rótulo de um estado deve, obrigatóriamente,
                        iniciar com a letra "q" seguida de um conjunto de letras e/ou números.

                        Para entrar com mais do que um estado de uma vez, separe cada
                        estado por vírgula, com ou sem espaço depois.
                        """,
                        "Erro",
                        JOptionPane.OK_OPTION
                    );

                }
            
            } catch (Exception ex) {
                
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.OK_OPTION
                );
                
            }
            
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Precisa digitar o estado",
                "Erro",
                JOptionPane.OK_OPTION
            );
            
        } 
        
    }
    
    
    private void cancelar() {
        setVisible(false);
    }

    
    public boolean isCancelado() {
        return cancelado;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlFrase = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaEstado = new javax.swing.JTextArea();
        jbCancelar = new javax.swing.JButton();
        jbInserir = new javax.swing.JButton();
        jcbEstadoTerminal = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("INSERIR ESTADOS");
        setResizable(false);

        jlFrase.setText("Estados (para inserir mais do que um estado de uma vez, separe-os com vírgula):");

        jtaEstado.setColumns(20);
        jtaEstado.setRows(5);
        jtaEstado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtaEstadoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtaEstadoKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtaEstado);

        jbCancelar.setText("Cancelar");
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });

        jbInserir.setText("Inserir");
        jbInserir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbInserirActionPerformed(evt);
            }
        });

        jcbEstadoTerminal.setText("Estado Terminal");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jlFrase)
                        .addGap(0, 151, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jcbEstadoTerminal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbInserir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlFrase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancelar)
                    .addComponent(jbInserir)
                    .addComponent(jcbEstadoTerminal))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbInserirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbInserirActionPerformed
        inserir();
    }//GEN-LAST:event_jbInserirActionPerformed

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_jbCancelarActionPerformed

    private void jtaEstadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtaEstadoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            inserir();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelar();
        }
    }//GEN-LAST:event_jtaEstadoKeyPressed

    private void jtaEstadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtaEstadoKeyReleased
        if (jtaEstado.getText().contains("\n")) {
            jtaEstado.setText(jtaEstado.getText().replace("\n", ""));
        }
    }//GEN-LAST:event_jtaEstadoKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbInserir;
    private javax.swing.JCheckBox jcbEstadoTerminal;
    private javax.swing.JLabel jlFrase;
    private javax.swing.JTextArea jtaEstado;
    // End of variables declaration//GEN-END:variables
}
