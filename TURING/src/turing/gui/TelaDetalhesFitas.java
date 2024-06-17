package turing.gui;

import javax.swing.table.DefaultTableModel;
import turing.classes.AlfabetoFita;
import turing.classes.Fita;
import turing.classes.MaquinaTuring;
import static turing.gui.Constantes.CELULAS_FITA;

public class TelaDetalhesFitas extends javax.swing.JDialog {
    

    public TelaDetalhesFitas(java.awt.Frame parent, MaquinaTuring maquinaTuring,
    int numeroFitas) {
        super(parent, true);
        initComponents();
        jtFitas.getTableHeader().setUI(null);
        setLocationRelativeTo(parent);
        imprimirFitas(maquinaTuring, numeroFitas);
    }
    
    
    private void imprimirFitas(MaquinaTuring maquinaTuring, int numeroFitas) {
        
        if (maquinaTuring != null) {
            
            Fita[] fitas = maquinaTuring.getFitas();

            String[][] listaFitas = new String[fitas.length][fitas[0].getComprimento()];
            String[] titulos = new String[fitas[0].getComprimento()];

            jtFitas.setModel(new DefaultTableModel(listaFitas, titulos) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

            for (int i = 0; i < jtFitas.getColumnCount(); i++) {
                jtFitas.getColumnModel().getColumn(i).setPreferredWidth(40);
                jtFitas.getColumnModel().getColumn(i).setMaxWidth(40);
            }

            for (int i = 0; i < jtFitas.getRowCount(); i++) {
                for (int j = 0; j < jtFitas.getColumnCount(); j++) {
                    jtFitas.setValueAt(
                        String.valueOf(AlfabetoFita.BRANCO),
                        i,
                        j
                    );
                }
            }

            for (int i = 0; i < jtFitas.getRowCount(); i++) {
                for (int j = 0; j < jtFitas.getColumnCount() ; j++) {
                    jtFitas.setValueAt(
                        fitas[i].getCelulas()[j].toString(),
                        i,
                        j
                    );
                }
            }
            
            RendererizadorFita renderer = new RendererizadorFita(null, false);

            for (int i = 0; i < jtFitas.getColumnCount(); i++) {
                jtFitas.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
            
            if (fitas[0].getComprimento() > CELULAS_FITA) {
                jtFitas.setAutoResizeMode(
                    javax.swing.JTable.AUTO_RESIZE_OFF
                );
            } else {
                jtFitas.setAutoResizeMode(
                    javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
                );
            }
            
        } else {
            
            String[][] listaFitas = new String[numeroFitas][25];
            String[] titulos = new String[25];

            jtFitas.setModel(new DefaultTableModel(listaFitas, titulos) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

            for (int i = 0; i < jtFitas.getColumnCount(); i++) {
                jtFitas.getColumnModel().getColumn(i).setPreferredWidth(40);
                jtFitas.getColumnModel().getColumn(i).setMaxWidth(40);
            }

            for (int i = 0; i < jtFitas.getRowCount(); i++) {
                for (int j = 0; j < jtFitas.getColumnCount(); j++) {
                    jtFitas.setValueAt(
                        String.valueOf(AlfabetoFita.BRANCO),
                        i,
                        j
                    );
                }
            }
            
            RendererizadorFita renderer = new RendererizadorFita(null, false);

            for (int i = 0; i < jtFitas.getColumnCount(); i++) {
                jtFitas.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
            
            jtFitas.setAutoResizeMode(
                javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
            );
            
        }
        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtFitas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DETALHES DAS FITAS");

        jtFitas.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jtFitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtFitas.setRowHeight(40);
        jtFitas.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(jtFitas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 930, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtFitas;
    // End of variables declaration//GEN-END:variables
}
