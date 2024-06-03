package turing.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellRenderer;
import turing.classes.AlfabetoFita;
import turing.classes.ConjuntoEstados;
import turing.classes.DirecaoMovimento;
import turing.classes.Estado;
import turing.classes.FuncaoTransicao;
import turing.classes.Modelo;
import turing.classes.ParametrosFita;
import turing.classes.Simbolo;
import turing.classes.Transicao;
import static turing.gui.Constantes.formatarSimbolos;
import static turing.gui.Constantes.reverterSimbolos;

public class TelaInserirTransicao extends javax.swing.JDialog {
    
    
    private final AlfabetoFita alfabetoFita;
    
    private final ConjuntoEstados conjuntoEstados;
    
    private final FuncaoTransicao funcaoTransicao;
    
    private final int numeroFitas;
    
    private final Modelo modelo;
    
    private boolean cancelado;
    
    private boolean modoEdicao;
    

    public TelaInserirTransicao(java.awt.Frame parent, AlfabetoFita alfabetoFita,
    ConjuntoEstados conjuntoEstados, FuncaoTransicao funcaoTransicao, int numeroFitas,
    Modelo modelo) {
        
        super(parent, true);
        
        this.alfabetoFita = alfabetoFita;
        this.conjuntoEstados = conjuntoEstados;
        this.funcaoTransicao = funcaoTransicao;
        this.numeroFitas = numeroFitas;
        this.modelo = modelo;
        cancelado = true;
        modoEdicao = false;
        
        initComponents();
        
        setLocationRelativeTo(parent);
        
        configurarControles();
        
        configurarJTable();
        
    }
    
    
    private void configurarControles() {
        
        if (modelo == Modelo.MULTIFITAS) {
            jspNumeroLinhas.setEnabled(false);
            jlSecoes.setText("Fitas:");
        } else {
            jlSecoes.setText("Seções:");
        }
        
        String[] estados = new String[conjuntoEstados.getComprimento()];

        for (int i = 0; i < conjuntoEstados.getComprimento(); i++) {
            estados[i] = conjuntoEstados.getEstado(i).toString();
        }
        
        jcbEstadoAtual.setModel(
            new javax.swing.DefaultComboBoxModel<>(
                estados
            )
        );
        
        jcbEstadoFinal.setModel(
            new javax.swing.DefaultComboBoxModel<>(
                estados
            )
        );
        
        jspNumeroLinhas.setValue(numeroFitas);

    }
    
    
    private void configurarJTable() {
        
        String[] titulosColunas = new String [] {
            modelo == Modelo.MULTIFITAS ? "Fita" : "Seção",
            "Ler",
            "Gravar",
            "Mover para"
        };
        
        String[] alfabeto = new String[alfabetoFita.getComprimento() + 1];
        alfabeto[0] = RenderizadorTransicao.CELULA_VAZIA;
        
        Object[][] dadosTabela = new Object[(int)jspNumeroLinhas.getValue()][titulosColunas.length];
        
        for (int i = 1; i < alfabetoFita.getComprimento() + 1; i++) {
            alfabeto[i] = formatarSimbolos(alfabetoFita.getSimbolo(i-1).toString());
        }
        
        for (int i = 0; i < numeroFitas; i++) {
            for (int j = 0; j < titulosColunas.length; j++) {
                dadosTabela[i][j] = RenderizadorTransicao.CELULA_VAZIA;
            }
        }
        
        jtMovimento.setModel(
            new javax.swing.table.DefaultTableModel(dadosTabela, titulosColunas) {
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex > 0;
                }
            }
        
        );
        
        TableCellRenderer renderer = new RenderizadorTransicao();
        
        class JComboboxAlfabeto extends JComboBox<String> {
            public JComboboxAlfabeto() {
                setModel(new DefaultComboBoxModel(alfabeto));
            } 
        }
        
        class JComboboxMovimento extends JComboBox<String> {
            public JComboboxMovimento() {
                String[] valores = new String[DirecaoMovimento.values().length + 1];
                valores[0] = RenderizadorTransicao.CELULA_VAZIA;
                for (int i = 0; i < DirecaoMovimento.values().length; i++) {
                    valores[i + 1] = DirecaoMovimento.values()[i].getId();
                }
                setModel(new DefaultComboBoxModel(valores));
            } 
        }
        
        for (int i = 0; i < titulosColunas.length; i++) {
            
            jtMovimento.getColumnModel().getColumn(i).setCellRenderer(renderer);
            
            switch (i) {
                
                case 1 -> {
                    jtMovimento.getColumnModel().getColumn(i).setCellEditor(
                        new DefaultCellEditor(new JComboboxAlfabeto())
                    );
                }
                
                case 2 -> {
                    jtMovimento.getColumnModel().getColumn(i).setCellEditor(
                        new DefaultCellEditor(new JComboboxAlfabeto())
                    );
                }
                
                case 3 -> {
                    jtMovimento.getColumnModel().getColumn(i).setCellEditor(
                        new DefaultCellEditor(new JComboboxMovimento())
                    );
                }
                
            }
            
        }
        
    }
    
    
    private void inserir() {
        
        Estado estadoInicial = conjuntoEstados.getEstado(jcbEstadoAtual.getSelectedIndex());
        Estado estadoFinal = conjuntoEstados.getEstado(jcbEstadoFinal.getSelectedIndex());
        List<ParametrosFita> parametrosFitas = new ArrayList<>();
        
        boolean erro = (estadoInicial == null || estadoFinal == null);
        
        if (!erro) {
            for (int i = 0; i < jtMovimento.getRowCount(); i++) {
                for (int j = 1; j < jtMovimento.getColumnCount(); j++) {
                    if (jtMovimento.getValueAt(i, j).equals(RenderizadorTransicao.CELULA_VAZIA)) {
                        erro = true;
                        break;
                    }
                }
                if (erro) {
                    break;
                }
            }
        }

        if (!erro) {

            for (int i = 0; i < jtMovimento.getRowCount(); i++) {

                Simbolo simboloLido = alfabetoFita.getSimbolo(
                    (reverterSimbolos((String)jtMovimento.getValueAt(i, 1))).charAt(0)
                );

                Simbolo simboloEscrito = alfabetoFita.getSimbolo(
                    (reverterSimbolos((String)jtMovimento.getValueAt(i, 2))).charAt(0)
                );

                DirecaoMovimento direcao = DirecaoMovimento.getDirecao(
                    ((String)jtMovimento.getValueAt(i, 3))
                );

                parametrosFitas.add(new ParametrosFita(
                        simboloLido,
                        simboloEscrito,
                        direcao
                    )
                );

            }

            funcaoTransicao.adicionarTransicao(new Transicao(
                    estadoInicial,
                    estadoFinal,
                    parametrosFitas
                )
            );
            
            setVisible(false);
        
        } else {

            JOptionPane.showMessageDialog(this,
                "Defina completamente a transição para prosseguir.",
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
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jcbEstadoAtual = new javax.swing.JComboBox<>();
        jcbEstadoFinal = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtMovimento = new javax.swing.JTable();
        jlSecoes = new javax.swing.JLabel();
        jspNumeroLinhas = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("INSERIR FUNÇÃO DE TRANSIÇÃO");
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("    Transição    "));

        jLabel4.setText("Estado Inicial:");

        jcbEstadoAtual.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jcbEstadoFinal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Estado Final:");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        jLabel3.setText("-->");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jcbEstadoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jcbEstadoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(206, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbEstadoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbEstadoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtMovimento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtMovimento.setRowHeight(25);
        jtMovimento.setRowSelectionAllowed(false);
        jtMovimento.getTableHeader().setResizingAllowed(false);
        jtMovimento.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jtMovimento);

        jlSecoes.setText("Seções:");

        jspNumeroLinhas.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jspNumeroLinhas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspNumeroLinhasStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jlSecoes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jspNumeroLinhas, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbInserir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancelar)
                    .addComponent(jbInserir)
                    .addComponent(jspNumeroLinhas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlSecoes))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jspNumeroLinhasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspNumeroLinhasStateChanged
        configurarJTable();
    }//GEN-LAST:event_jspNumeroLinhasStateChanged

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_jbCancelarActionPerformed

    private void jbInserirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbInserirActionPerformed
        inserir();
    }//GEN-LAST:event_jbInserirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbInserir;
    private javax.swing.JComboBox<String> jcbEstadoAtual;
    private javax.swing.JComboBox<String> jcbEstadoFinal;
    private javax.swing.JLabel jlSecoes;
    private javax.swing.JSpinner jspNumeroLinhas;
    private javax.swing.JTable jtMovimento;
    // End of variables declaration//GEN-END:variables
}
