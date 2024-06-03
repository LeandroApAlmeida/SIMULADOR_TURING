package turing.gui;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import turing.classes.AlfabetoFita;
import turing.classes.Simbolo;

public class TelaInserirSimbolo extends javax.swing.JDialog {
    

    private AlfabetoFita alfabetoFita;
    
    private Simbolo simbolo;
    
    private boolean cancelado;
    
    private boolean modoEdicao;

    
    public TelaInserirSimbolo(java.awt.Frame parent, AlfabetoFita alfabetoFita) {
        
        super(parent, true);
        
        this.alfabetoFita = alfabetoFita;
        cancelado = true;
        modoEdicao = false;
        
        initComponents();
        setLocationRelativeTo(parent);
        
    }
    
    
    public TelaInserirSimbolo(java.awt.Frame parent, Simbolo simbolo, 
    AlfabetoFita alfabetoFita) {
        
        this(parent, alfabetoFita);
        
        this.simbolo = simbolo;
        modoEdicao = true;
        
        jtaSimbolo.setText(new String(new char[] {this.simbolo.getCaracter()}));
        jcbAlfabetoAuxiliar.setSelected(simbolo.isAuxiliar());
        
        setTitle("ALTERAR SÍMBOLO");
        jbInserir.setText("Alterar");
        jlFrase.setText("Simbolo:");
        
    }
    
    
    private void inserir() {
        
        String texto = jtaSimbolo.getText();
        boolean auxiliar = jcbAlfabetoAuxiliar.isSelected();
        
        boolean erro = false;
        
        if (!texto.isEmpty()) {

            if (!modoEdicao) {
                
                Simbolo[] simbolos = null;
            
                if (texto.contains(",") && texto.length() > 1) {

                    boolean contemVirgula = false;
                    boolean contemEspaco = false;
                    
                    int indice = 0;

                    texto = texto.replace(" ", "");
                    
                    // Tratamento de vírgula no começo da string.
                    
                    if (texto.startsWith(",")) {
                        if (texto.charAt(1) != ',') {
                            // Corrigindo vírgula no início, seguida de símbolo.
                            // Exemplo: ,a,b
                            texto = texto.substring(1, texto.length());
                        } else {
                            // Inserção de vírgula e espaço na gramática.
                            int contVirg = 0;
                            for (int i = 0; i < texto.length(); i++) {
                                if (texto.charAt(i) == ',') {
                                    contVirg++;
                                } else {
                                    break;
                                }
                            }
                            switch (contVirg) {
                                case 2 -> contemEspaco = true;
                                case 3, 4 -> contemVirgula = true;
                                default -> {
                                    contemVirgula = true;
                                    contemEspaco = true;
                                }
                            }
                            texto = texto.substring(contVirg, texto.length());
                        }
                    }
                    
                    // Tratamento de vírgula no meio da string.
                    
                    if (texto.contains(",,,,,")) {
                        // A sequência ,,,,, significa espaço e vírgula (,-,-, + ,-,).
                        texto = texto.replace(",,,,,", ",");
                        contemVirgula = true;
                        contemEspaco = true;
                    }

                    if (texto.contains(",,,")) {
                        // A sequência ,,, significa vírgula (,-,-,).
                        texto = texto.replace(",,,", ",");
                        contemVirgula = true;
                    }

                    if (texto.contains(",,")) {
                        // A sequência ,, significa espaço (,-,).
                        texto = texto.replace(",,", ",");
                        contemEspaco = true;
                    }

                    String[] secoes = texto.length() > 0 ? texto.split(",") : new String[0];

                    if (contemVirgula || contemEspaco) {

                        if (contemVirgula && contemEspaco) {
                            simbolos = new Simbolo[secoes.length + 2];
                            simbolos[indice++] = new Simbolo(',', auxiliar);
                            simbolos[indice++] = new Simbolo(' ', auxiliar);
                        } else {
                            simbolos = new Simbolo[secoes.length + 1];
                            if (contemVirgula) {
                                simbolos[indice++] = new Simbolo(',', auxiliar);
                            } else {
                                simbolos[indice++] = new Simbolo(' ', auxiliar);
                            }
                        }

                    } else {
                        simbolos = new Simbolo[secoes.length];
                    }

                    for (String secao : secoes) {
                        if (secao.length() == 1) {
                            simbolos[indice++] = new Simbolo(secao.charAt(0), auxiliar);
                        } else {
                            erro = true;
                            break;
                        }
                    }

                } else {
                    
                    if (texto.length() == 1) {
                        simbolos = new Simbolo[1];
                        simbolos[0] = new Simbolo(
                            texto.charAt(0),
                            auxiliar
                        );
                    } else {
                        erro = true;
                    }
                    
                }
                
                if (!erro) {
                    if (simbolos != null) {
                        for (Simbolo simb : simbolos) {
                            alfabetoFita.inserirSimbolo(simb);
                        }
                    }
                }
            
            } else {
                
                if (texto.length() == 1) {
                    alfabetoFita.alterarSimbolo(simbolo, texto.charAt(0));
                    alfabetoFita.setSimboloAuxiliar(simbolo, auxiliar);
                } else {
                    erro = true;
                }
                
            }
            
            if (!erro) {

                cancelado = false;
                setVisible(false);

            } else {

                if (!modoEdicao) {
                
                    JOptionPane.showMessageDialog(this,
                        """
                        Sintaxe incorreta. Para entrar com mais do que um símbolo de
                        uma vez, separe cada símbolo por vírgula, com ou sem espaço
                        depois. 

                        Use ",,," para inserir vírgula e ",," para inserir espaço no
                        alfabeto.

                        Cada símbolo deve conter somente um caractere.
                        """,
                        "Erro",
                        JOptionPane.OK_OPTION
                    );
                
                } else {
                    
                    JOptionPane.showMessageDialog(this,
                        """
                        Sintaxe do símbolo incorreta. Um símbolo deve ser composto
                        de somente 1 caractere.
                        """,
                        "Erro",
                        JOptionPane.OK_OPTION
                    );
                    
                }
                
            }
            
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Precisa digitar o símbolo",
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

        jbCancelar = new javax.swing.JButton();
        jbInserir = new javax.swing.JButton();
        jlFrase = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaSimbolo = new javax.swing.JTextArea();
        jcbAlfabetoAuxiliar = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("INSERIR SÍMBOLOS");
        setResizable(false);

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

        jlFrase.setText("Simbolo: (para inserir mais do que um símbolo de uma vez, separe-os com vírgula)");

        jtaSimbolo.setColumns(20);
        jtaSimbolo.setRows(5);
        jtaSimbolo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtaSimboloKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtaSimboloKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtaSimbolo);

        jcbAlfabetoAuxiliar.setText("Alfabeto Auxiliar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jlFrase)
                        .addGap(0, 139, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jcbAlfabetoAuxiliar)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancelar)
                    .addComponent(jbInserir)
                    .addComponent(jcbAlfabetoAuxiliar))
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

    private void jtaSimboloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtaSimboloKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            inserir();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelar();
        }
    }//GEN-LAST:event_jtaSimboloKeyPressed

    private void jtaSimboloKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtaSimboloKeyReleased
        if (jtaSimbolo.getText().contains("\n")) {
            jtaSimbolo.setText(jtaSimbolo.getText().replace("\n", ""));
        }
    }//GEN-LAST:event_jtaSimboloKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbInserir;
    private javax.swing.JCheckBox jcbAlfabetoAuxiliar;
    private javax.swing.JLabel jlFrase;
    private javax.swing.JTextArea jtaSimbolo;
    // End of variables declaration//GEN-END:variables
}
