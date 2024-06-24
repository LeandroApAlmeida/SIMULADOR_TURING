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
import turing.classes.ParametrosFita;
import turing.classes.Simbolo;
import turing.classes.Transicao;
import static turing.gui.Formatacao.formatarSimbolos;
import static turing.gui.Formatacao.reverterSimbolos;
import static turing.gui.RenderizadorTransicao.CELULA_VAZIA;

/**
 * Tela para a inserção de uma transição da função de transição. Uma transição
 * tem o seguinte formato:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * δ(q<sub>a</sub>, s<sub>1</sub>, s<sub>2</sub>, ... , s<sub>k</sub>) = 
 * (q<sub>n</sub>, g<sub>1</sub>, g<sub>2</sub>, ... , g<sub>k</sub>, 
 * d<sub>1</sub>, d<sub>2</sub>, ... , d<sub>k</sub>)
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Onde:
 * 
 * <br><br>
 * 
 * <BLOCKQUOTE>
 * 
 * <b>q<sub>a</sub></b>: Estado atual.
 * 
 * <br><br>
 * 
 * <b>s<sub>1</sub>, s<sub>2</sub>, ..., s<sub>k</sub></b>: Símbolos lidos das 
 * fitas.
 * 
 * <br><br>
 * 
 * <b>q<sub>n</sub></b>: Novo estado.
 * 
 * <br><br>
 * 
 * <b>g<sub>1</sub>, g<sub>2</sub>, ..., g<sub>k</sub></b>: Símbolos gravados nas
 * fitas.
 * 
 * <br><br>
 * 
 * <b>d<sub>1</sub>, d<sub>2</sub>, ..., d<sub>k</sub></b>: Sentidos dos movimentos
 * das Cabeças de Leitura/Escrita nas fitas.
 * 
 * <br><br>
 * 
 * <b>k</b>: Número de fitas da máquina de Turing.
 * 
 * </BLOCKQUOTE>
 * 
 * <br>
 * 
 * Basicamente, um transição considera o estado atual da Unidade de Controle e
 * os símbolos lidos das fitas para determinar o novo estado e os símbolos a
 * serem gravados nas fitas, nas mesmas células que foram realizadas as leituras,
 * e também a direção do movimento das Cabeças de Leitura/Escrita.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class TelaInserirTransicao extends javax.swing.JDialog {
    
    
    /**Alfabeto da fita.*/
    private final AlfabetoFita alfabetoFita;
    
    /**Conjunto dos estados.*/
    private final ConjuntoEstados conjuntoEstados;
    
    /**Função de transição.*/
    private final FuncaoTransicao funcaoTransicao;
    
    /**Número de fitas.*/
    private final int numeroFitas;
    
    /**Status de edição cancelada.*/
    private boolean cancelado;
    
    /**Status de modo de edição.*/
    private boolean modoEdicao;
    

    /**
     * Instanciar a tela no modo de inserção de novas transições.
     * 
     * @param parent tela proprietária.
     * 
     * @param alfabetoFita alfabeto da fita.
     * 
     * @param conjuntoEstados conjunto dos estados.
     * 
     * @param funcaoTransicao função de transição.
     *  
     * @param numeroFitas número de fitas da Máquina de Turing.
     */
    public TelaInserirTransicao(java.awt.Frame parent, AlfabetoFita alfabetoFita,
    ConjuntoEstados conjuntoEstados, FuncaoTransicao funcaoTransicao, int numeroFitas) {
        
        super(parent, true);
        
        this.alfabetoFita = alfabetoFita;
        this.conjuntoEstados = conjuntoEstados;
        this.funcaoTransicao = funcaoTransicao;
        this.numeroFitas = numeroFitas;
        cancelado = true;
        modoEdicao = false;
        
        initComponents();
        
        setLocationRelativeTo(parent);
        
        configurarControlesEstados();
        
        configurarJTable();
        
    }
    
    
    /**
     * Configurar os controles de lista de estados para determinar o estado
     * atual e o novo estado da transição.
     */
    private void configurarControlesEstados() {
        
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

    }
    
    
    /**
     * Configurar a JTable para a definição dos símbolos lidos das fitas, os
     * símbolos a serem escritos e a direção do movimento das Cabeças de
     * Leitura/Escrita.
     */
    private void configurarJTable() {
        
        String[] titulosColunas = new String [] {
            "Fita",
            "Ler",
            "Gravar",
            "Mover para"
        };
        
        String[] alfabeto = new String[alfabetoFita.getComprimento() + 1];
        alfabeto[0] = CELULA_VAZIA;
        
        Object[][] dadosTabela = new Object[numeroFitas][titulosColunas.length];
        
        for (int i = 1; i < alfabetoFita.getComprimento() + 1; i++) {
            alfabeto[i] = formatarSimbolos(alfabetoFita.getSimbolo(i-1).toString());
        }
        
        for (int i = 0; i < numeroFitas; i++) {
            for (int j = 0; j < titulosColunas.length; j++) {
                dadosTabela[i][j] = CELULA_VAZIA;
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
                valores[0] = CELULA_VAZIA;
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
    
    
    /**
     * Inserir a transição na Função de Transição. Para inserir, todos os valores
     * na Jtable devem estar definidos, pois eles são requeridos para a escrita
     * da transição.
     */
    private void inserir() {
        
        Estado estadoInicial = conjuntoEstados.getEstado(jcbEstadoAtual.getSelectedIndex());
        Estado estadoFinal = conjuntoEstados.getEstado(jcbEstadoFinal.getSelectedIndex());
        List<ParametrosFita> paramsFita = new ArrayList<>();
        
        boolean erro = (estadoInicial == null || estadoFinal == null);
        
        if (!erro) {
            for (int i = 0; i < jtMovimento.getRowCount(); i++) {
                for (int j = 1; j < jtMovimento.getColumnCount(); j++) {
                    if (jtMovimento.getValueAt(i, j).equals(CELULA_VAZIA)) {
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

                paramsFita.add(
                    new ParametrosFita(
                        simboloLido,
                        simboloEscrito,
                        direcao
                    )
                );

            }

            funcaoTransicao.adicionarTransicao(new Transicao(
                    estadoInicial,
                    estadoFinal,
                    paramsFita
                )
            );
            
            cancelado = false;
            
            setVisible(false);
        
        } else {

            JOptionPane.showMessageDialog(this,
                "Defina completamente a transição para prosseguir.",
                "Erro",
                JOptionPane.OK_OPTION
            );
            
        }
        
    }
    
    
    /**
     * Cancelar a edição.
     */
    private void cancelar() {
        setVisible(false);
    }

    
     /**
     * Status de edição cancelada.
     * 
     * @return Se true, a edição foi cancelada. Se false, a edição não foi
     * cancelada.
     */
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
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("INSERIR TRANSIÇÃO");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                    .addComponent(jbInserir))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JTable jtMovimento;
    // End of variables declaration//GEN-END:variables
}
