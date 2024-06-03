package turing;

import java.io.File;
import javax.swing.UIManager;
import turing.gui.TelaPrincipal;

/**
 * Classe de inicialização do programa. Implementa a função <i>public static 
 * void main(String[] args)</i>.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class Main {
    
    
    static {
        //Tradução das caixas de mensagens (JOptionPane).
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "OK");
    }


    /**
     * Ponto de entrada do programa. Trata a abertura de arquivo passado por 
     * linha de comando a partir do terminal de comandos.
     * 
     * <br><br>
     * 
     * Se o arranjo de argumentos tiver uma String, é subtendido que este parâmetro
     * é o caminho para um arquivo. É feita a validação, e o arquivo é aberto.
     * 
     * <br><br>
     * 
     * Para usar com esta opção, no terminal de comandos, deverá ser passado o
     * seguinte comando:
     * 
     * <br><br>
     * 
     * <BLOCKQUOTE>
     * > java.exe -jar "TURING.java" "D:\TesteTuring.asmt
     * </BLOCKQUOTE>
     * 
     * <br>
     * 
     * Caso nenhum parâmetro seja passado, vai abrir o programa no modo padrão.
     * 
     * @param args lista de argumentos para o programa digitados no prompt de 
     * comandos.
     */
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
        }
        
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