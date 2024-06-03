package turing;

import java.io.File;
import javax.swing.UIManager;
import turing.gui.TelaPrincipal;

public class Main {
    
    static {
        //Tradução das caixas de mensagens (JOptionPane).
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "OK");   
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            TelaPrincipal telaPrincipal = new TelaPrincipal();
            if (args.length == 1) {
                File file = new File(args[0]);
                if (file.exists()) {
                    telaPrincipal.abrirArquivo(file);
                }
            }
            telaPrincipal.setVisible(true);
        });
    }
    
}