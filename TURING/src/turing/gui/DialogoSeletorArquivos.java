package turing.gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Diálogo para abrir/salvar arquivos.
 * 
 * @since 1.0
 */
public final class DialogoSeletorArquivos extends JFileChooser {

    
    /**Flag para controle de execução do evento ActionPerformed somente na classe
    especializada.*/
    private boolean ignorarAcao;
    
    
    /**
     * Criar uma instância de <b>FileChooser</b>. Espera o título do diálogo
     * e os filtros de arquivos a serem visualizados.
     * @param tituloDialogo título para exibir na barra de títulos do diálogo.
     * @param filtro filtro padrão(obrigatório).
     * @param filtros demais filtros para o diálogo, se necessário.
     */
    public DialogoSeletorArquivos(String tituloDialogo, FileNameExtensionFilter filtro, 
    FileNameExtensionFilter... filtros) {
        
        setMultiSelectionEnabled(false);
        setAcceptAllFileFilterUsed(false);
        setDialogTitle(tituloDialogo);
        setFileFilter(filtro);
        
        for (FileNameExtensionFilter fileFilter : filtros) {
            setFileFilter(fileFilter);
        }
        
        addActionListener((ActionEvent e) -> {
            acaoPadraoExecutada(e);
        });
        
    }
    
    
    /**
     * Acionado ao clicar no botao "Salvar/abrir" no diálogo. Neste caso, não 
     * fecha o diálogo como na classe base, mas aciona o tratador de evento
     * apenas.
     */
    @Override
    public void approveSelection() {
        //Não vai ignorar o tratador de eventos.
        ignorarAcao = false;
        //Aciona o tratador de eventos.
        fireActionPerformed(APPROVE_SELECTION);        
    }

    
    /**
     * Sobrescrito para aceitar somente os filtros que são instância de
     * FileNameExtensionFilter da qual esta classe depende.
     * @param filter instância de {@link FileNameExtensionFilter}.
     */
    @Override
    public void setFileFilter(FileFilter filter) {
        if (filter instanceof FileNameExtensionFilter) {
            super.setFileFilter(filter);
        }   
    }

    
    /**
     * Evento disparado ao clicar em algum dos botões do diálogo (Abrir/Salvar/Cancelar).
     * @param evt evento disparado ao clicar no botão.
     */
    private void acaoPadraoExecutada(java.awt.event.ActionEvent evt) { 
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!ignorarAcao) {
            
            //Vai ignorar o tratamento de eventos, que agora só poderá
            //ser acionado na classe base JFileChooser.
            ignorarAcao = true;  
            
            if (evt.getActionCommand().equals(APPROVE_SELECTION)) {
                
                boolean selecaoAprovada = true;
                
                if (((JFileChooser) evt.getSource()).getDialogType() == SAVE_DIALOG) {
                    
                    //Abriu-se o diálogo para salvar um arquivo.
                    File arquivoSelecionado = getSelectedFile();
                    
                    //Verifica se o usuário digitou a extensão do arquivo.
                    FileNameExtensionFilter filtro = (FileNameExtensionFilter) 
                    getFileFilter();
                    
                    String[] extensoes = filtro.getExtensions();
                    boolean terminaCom = false;
                    
                    for (String extensao : extensoes) {
                        if (arquivoSelecionado.getAbsolutePath().endsWith("." + extensao)) {
                            terminaCom = true;
                        }
                    }
                    
                    if (!terminaCom) {
                        String nomeArquivo = arquivoSelecionado.getAbsolutePath() +
                        "." + extensoes[0];
                        //Completa com a extensão do arquivo.
                        arquivoSelecionado = new File(nomeArquivo);
                        setSelectedFile(arquivoSelecionado);
                    }
                    
                    //Verifica se há um arquivo com o mesmo nome.
                    if (arquivoSelecionado.exists()) {
                        //Pede a confirmação do usuário para sobrescrevê-lo.
                        int opt = javax.swing.JOptionPane.showConfirmDialog(this.getParent(),
                            arquivoSelecionado.getAbsolutePath() +
                            " já existe.\nSobrescrever o arquivo existente?",
                            "Atenção!",
                            javax.swing.JOptionPane.YES_NO_OPTION,
                            javax.swing.JOptionPane.QUESTION_MESSAGE
                        );
                        if (opt == javax.swing.JOptionPane.NO_OPTION) {
                            selecaoAprovada = false;
                        }
                    }
                    
                } else {
                    
                   File arquivoSelecionado = getSelectedFile();
                   if (!arquivoSelecionado.exists()) {
                       selecaoAprovada = false;
                   }
                   
                }
                
                if (selecaoAprovada) {
                    super.approveSelection(); 
                }
                
            }
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }

    
}