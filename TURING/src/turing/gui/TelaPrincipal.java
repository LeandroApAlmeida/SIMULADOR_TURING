package turing.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;
import turing.arquivo.ArquivoTexto;
import turing.classes.AlfabetoFita;
import turing.classes.GeradorCodigo;
import turing.classes.Estado;
import turing.classes.FuncaoTransicao;
import turing.classes.Compilador;
import turing.classes.ConfigMaqTuring;
import turing.classes.ConjuntoEstados;
import static turing.classes.Constantes.CABECALHO_PROGRAMA;
import turing.classes.DirecaoMovimento;
import turing.classes.Fita;
import turing.classes.MaquinaMultifitas;
import turing.classes.MaquinaPadrao;
import turing.classes.MaquinaTuring;
import turing.classes.Modelo;
import static turing.classes.Modelo.MULTIFITAS;
import static turing.classes.Modelo.PADRAO;
import turing.classes.Simbolo;
import turing.classes.Transicao;
import static turing.gui.ComponenteNumeroLinha.ALINHAMENTO_CENTRALIZADO;
import static turing.gui.Formatacao.formatarSimbolos;
import turing.classes.OuvinteEtapaSimulacao;
import static turing.classes.Constantes.SIMBOLO_BRANCO;
import static turing.gui.Sufixos.SUFIXO_CURSOR;
import static turing.gui.Sufixos.SUFIXO_CEL_PIVO;
import static turing.classes.Constantes.TAMANHO_FITA;


/**
 * Tela principal do Simulador de Máquina de Turing.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */

public class TelaPrincipal extends javax.swing.JFrame implements OuvinteEtapaSimulacao,
OuvinteConfigSimulacaoAutomatica {

    
    /**Título da tela.*/
    private final String titulo = "SIMULADOR DE MÁQUINA DE TURING";
    
    /**Filtro para arquivos do simulador.*/
    private final FileNameExtensionFilter filtroArquivo;
    
    /**Gerenciador de desfazer/refazer alterações no texto.*/
    private final UndoManager undoManager;
    
    /**Modelo para a lista do menu popup da seção [Programa] do arquivo.*/
    private final DefaultListModel<String> modeloLista;
    
    /**Alfabeto da Fita.*/
    private AlfabetoFita alfabetoFita;
    
    /**Conjunto dos Estados.*/
    private ConjuntoEstados conjuntoEstados;
    
    /**Função de Transição.*/
    private FuncaoTransicao funcaoTransicao;
    
    /**Modelo de Máquina de Turing simulado.*/
    private Modelo modelo;
    
    /**Instância do modelo de Máquina de Turing simulado.*/
    private MaquinaTuring maquinaTuring;
    
    /**Arquivo aberto no editor de código.*/
    private ArquivoTexto arquivo;
    
    /**Timer para simulação automática.*/
    private Timer timer;
    
    /**Tempo default de simulação automática.*/
    private int tempoExecucao = 1000;
    
    /**Nome da Máquina de Turing simulada.*/
    private String nome;
    
    /**Status de arquivo aberto no editor de código.*/
    private boolean arquivoAberto;
    
    /**Status de simulador em execução.*/
    private boolean emExecucao;
    
    /**Status de simulação automática.*/
    private boolean simulacaoAutomatica;
    
    /**Status de simulação automática pausada.*/
    private boolean emPausa;
    
    /**Status de processo de compilação.*/
    private boolean compilando;
    
    /**Status de mudança do texto aberto no editor.*/
    private boolean houveMudancaTexto;
    
    /**Status de compilação pendente.*/
    private boolean compilacaoPendente;
    
    /**Comprimento do texto salvo no arquivo.*/
    private int comprimentoTextoArquivo;
    
    /**Comprimento do texto aberto no editor.*/
    private int comprimentoTextoCompilacao;
    
    /**Controle de entrada no Thread do Timer.*/
    private boolean atualizandoEtapaSimulacao;
    
    
    public TelaPrincipal() {
        
        initComponents();
        
        alfabetoFita = new AlfabetoFita();
        conjuntoEstados = new ConjuntoEstados();
        funcaoTransicao = new FuncaoTransicao();
        undoManager = new UndoManager();
        modelo = Modelo.MULTIFITAS;
        arquivo = null;
        arquivoAberto = false;
        compilando = false;
        emExecucao = false;
        emPausa = false;
        houveMudancaTexto = false;
        compilacaoPendente = false;
        comprimentoTextoArquivo = 0;
        comprimentoTextoCompilacao = 0;
        nome = "Máquina de Turing";
		
        filtroArquivo = new FileNameExtensionFilter(
            "Arquivo para Simulador da Máquina de Turing (*.asmt)",
            "asmt"
        );
	
        Font font = jtfSobre.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        jtfSobre.setFont(font.deriveFont(attributes));
		
        modeloLista = new DefaultListModel<>();
        jtFitas.getTableHeader().setUI(null);
        jppAutocompletar.add(jpAutocompletar);
        jlAutocompletar.setModel(modeloLista);
		
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        setTitle(titulo);
        
        configurarControlesSimulador();
        
        configurarEditorCodigo();

        configurarFitasModeloSelecionado();

        listarAlfabetoFita();
        listarConjuntoEstados();
        listarFuncaoTransicao();
        
        gerarCodigoPrograma();
        
        compilarArquivo();

    }
    
    
    
    
// CONFIGURAÇÃO DO EDITOR DE CÓDIGO DO PROGRAMA ----------------------------- //   
    
    
    /**
     * Configurar o editor de código do IDE. As configurações aplicadas são
     * visuais (número das linhas à direita) e também de ação, como teclas
     * de atalhos e opção de desfazer/refazer edição de texto.
     */
    private void configurarEditorCodigo() {
        
        // Configura o componenteLinha de exibição de números de linhas à esquerda
        // do editor. O componenteLinha será acoplado ao JScrollPane do editor e
        // será renderizado nele.
        
        ComponenteNumeroLinha componenteLinha = new ComponenteNumeroLinha(
            jtaEditor,
            ALINHAMENTO_CENTRALIZADO  
        );
        
        jspEditor.setRowHeaderView(componenteLinha);

        // Configura os atalhos de teclado do editor:
        //
        // CTRL + S: Salvar o arquivo.
        // CTRL + O: Abrir um arquivo.
        // CTRL + R: Compilar o programa.
        // CTRL + C: Copiar o texto selecionado para a área de transferência.
        // CTRL + V: Colar o texto da área de transferência para a posição do
        // cursor.
        // CTRL + Z: Desfazer edição do texto.
        // CTRL + W: Refazer edição do texto.
        // CTRL + ESPAÇO: Exibir menu de contexto na seção [Programa].
        
        jtaEditor.getActionMap().put("salvar_arquivo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarArquivo();
            }
        });
        
        jtaEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
            "salvar_arquivo"
        );
        
        jtaEditor.getActionMap().put("abrir_arquivo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirArquivo();
            }
        });
        
        jtaEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK),
            "abrir_arquivo"
        );
        
        jtaEditor.getActionMap().put("compilar_arquivo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compilarArquivo();
            }
        });
        
        jtaEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK),
            "compilar_arquivo"
        );
        
        jtaEditor.getActionMap().put("desfazer_edicao", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desfazerAlteracaoTexto();
            }
        });
        
        jtaEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK),
            "desfazer_edicao"
        );
        
        jtaEditor.getActionMap().put("refazer_edicao", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refazerAlteracaoTexto();
            }
        });
        
        jtaEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK),
            "refazer_edicao"
        );
        
        jtaEditor.getActionMap().put("copiar_texto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copiarTextoParaAreaTransferencia();
            }
        });
        
        jtaEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK),
            "copiar_texto"
        );
        
        jtaEditor.getActionMap().put("colar_texto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colarTextoDaAreaTranferencia();
            }
        });
        
        jtaEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
            "colar_texto"
        );
        
        jtaEditor.getActionMap().put("exibir_menu_transicao", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirMenuTransicao();
            }
        });
        
        jtaEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK),
            "exibir_menu_transicao"
        );
        
        // Habilita as ações de desfazer e refazer a digitação no editor.
        
        jtaEditor.getDocument().addUndoableEditListener(undoManager);
        
    }
    
    
    /**
     * Abrir um arquivo no editor. Será exibido um diálogo para que o
     * usuário localize o arquivo a ser aberto.
     */
    private void abrirArquivo() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (verificarMudancasNoTextoEProsseguir()) {
            
            DialogoArquivo dialogoArquivo = new DialogoArquivo(
                "Abrir Arquivo",
                filtroArquivo
            );

            int opc = dialogoArquivo.showOpenDialog(this);

            if (opc == DialogoArquivo.APPROVE_OPTION) {
                
                abrirArquivo(dialogoArquivo.getSelectedFile());

            } 
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }
    
    
    /**
     * Abrir o arquivo no editor. É realizada a compilação após a abertura, para
     * obter os parâmetros para a construção da Máquina de Turing.
     * 
     * <br><br>
     * 
     * O arquivo é identificado pelo caminho passado como parâmetro para o método.
     * 
     * @param file caminho do arquivo.
     */
    public void abrirArquivo(File file) {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (verificarMudancasNoTextoEProsseguir()) {

            fecharArquivo();

            arquivo = new ArquivoTexto(file);

            try {

                jtaEditor.setText(arquivo.ler());

                compilarArquivo();

                comprimentoTextoArquivo = jtaEditor.getText().length();

                houveMudancaTexto = false;
                compilando = false;
                emExecucao = false;
                arquivoAberto = true;
                
                jbFecharArquivo.setEnabled(true);
                jbSalvarArquivo.setEnabled(false);
                
                jtaEditor.setCaretPosition(0);

            } catch (Exception ex) {
                arquivo = null;
                arquivoAberto = false;
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.OK_OPTION
                );
            }
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }
    
    
    /**
     * Salvar o texto do editor no arquivo. Neste caso, temos duas situações:
     * 
     * <br><br>
     * 
     * <ol>
     * 
     * <li>
     * Caso não há um arquivo aberto, então um novo arquivo deverá ser criado.
     * Neste caso, vai exibir o diálogo para o usuário selecionar o local do
     * arquivo e salvá-lo.
     * </li>
     * 
     * <br>
     * 
     * <li>
     * Caso há um arquivo aberto, então vai apenas reescrever o texto que está
     * no editor no mesmo.
     * </li>
     * 
     * </ol>
     */
    private void salvarArquivo() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        if (!arquivoAberto) {
            
            DialogoArquivo dialogoArquivo = new DialogoArquivo(
                "Salvar Arquivo",
                filtroArquivo
            );

            int opc = dialogoArquivo.showSaveDialog(this);

            if (opc == DialogoArquivo.APPROVE_OPTION) {

                File file = dialogoArquivo.getSelectedFile();
                
                arquivo = new ArquivoTexto(file);
                
                try {
                    
                    arquivo.gravar(jtaEditor.getText());
                    
                    jbSalvarArquivo.setEnabled(false);
                    jbFecharArquivo.setEnabled(true);
                    
                    arquivoAberto = true;
                    
                    definirTituloTela();
                    
                } catch (Exception ex) {
                    arquivo = null;
                    arquivoAberto = false;
                    JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erro",
                        JOptionPane.OK_OPTION
                    );
                }

            }
            
        } else {
            
            try {
                arquivo.gravar(jtaEditor.getText());
                jbSalvarArquivo.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.OK_OPTION
                );
            }
            
        }
        
        houveMudancaTexto = false;
        
        comprimentoTextoArquivo = jtaEditor.getText().length();
        
        jtaEditor.requestFocus();
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }
    
    
    /**
     * Fechar o arquivo aberto no editor.
     */
    private void fecharArquivo() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (arquivoAberto) {
        
            if (verificarMudancasNoTextoEProsseguir()) {

                arquivo = null;
                alfabetoFita.esvaziar();
                conjuntoEstados.esvaziar();
                funcaoTransicao.esvaziar();

                jbFecharArquivo.setEnabled(false);
                jbSalvarArquivo.setEnabled(true);
                
                compilacaoPendente = false;

                listarAlfabetoFita();
                listarConjuntoEstados();
                listarFuncaoTransicao();
                
                jtaEditor.setText("");

                gerarCodigoPrograma();
                
                compilarArquivo();

                arquivoAberto = false;

            }
        
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Compilar o arquivo fazendo as verificações sintáticas e semânticas. Em
     * caso de não ocorrência de erros, mapeia os campos do arquivo para a 
     * interface do simulador. Caso contrário, exibe as mensagens de erro na
     * sintaxe do arquivo e inviabiliza a edição visual dos campos.
     */
    private void compilarArquivo() {
        
        new Thread() {
            
            @Override
            public void run() {
                   
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                
                compilando = true;
                
                configurarControlesSimulador();

                jtaCompilacao.setText("Compilado...");

                try {

                    ConfigMaqTuring configuracoes = new Compilador().executar(
                        jtaEditor.getText()
                    );

                    alfabetoFita = configuracoes.getAlfabetoFita();
                    conjuntoEstados = configuracoes.getConjuntoEstados();
                    funcaoTransicao = configuracoes.getFuncaoTransicao();
                    jspNumeroFitas.setValue(configuracoes.getNumeroFitas());
                    
                    modeloLista.removeAllElements();
                    
                    for (Estado estado : conjuntoEstados) {
                        modeloLista.addElement(estado.getRotulo());
                    }
                    
                    jlAutocompletar.setModel(modeloLista);
                    
                    if (configuracoes.getNome() != null) {
                        if (!configuracoes.getNome().isEmpty() && 
                        !configuracoes.getNome().isBlank()) {
                            nome = configuracoes.getNome();
                        } else {
                            nome = "Máquina de Turing";
                        }
                    } else {
                        nome = "Máquina de Turing";
                    }

                    jtaCompilacao.setText("\u22B3 Compilado com sucesso!");
                    jbCompilar.setEnabled(false);

                    compilacaoPendente = false;

                    comprimentoTextoCompilacao = jtaEditor.getText().length();
                    
                    definirTituloTela();

                } catch (Exception ex) {
                    jtaCompilacao.setText("Erros encontrados: \n\n" + ex.getMessage());
                    alfabetoFita.esvaziar();
                    conjuntoEstados.esvaziar();
                    funcaoTransicao.esvaziar();
                }
                
                compilando = false;

                listarConjuntoEstados();
                listarAlfabetoFita();
                listarFuncaoTransicao();
                
                configurarBarraFerramentasEditor();
                configurarBarraFerramentasSimulador();
                
                jtaEditor.requestFocus();
                
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                
            }
            
        }.start();
        
    }
    
    
    /**
     * Desfazer as alterações no texto do editor.
     */
    private void desfazerAlteracaoTexto() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (undoManager.canUndo()) {
            try {
                undoManager.undo();
                verificarMudancasTexto();
            } catch (Exception ex) {
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    /**
     * Refazer as alterações no texto do editor.
     */
    private void refazerAlteracaoTexto() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (undoManager.canRedo()) {
            try {
                undoManager.redo();
                verificarMudancasTexto();
            } catch (Exception ex) {   
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    /**
     * Copiar o texto selecionado no editor para a área de transferência.
     */
    private void copiarTextoParaAreaTransferencia() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        String myString = jtaEditor.getSelectedText();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    /**
     * Colar o texto da área de transferência para a posição do cursor. Caso
     * algum texto esteja selecionado, apaga e cola sobre o mesmo.
     */
    private void colarTextoDaAreaTranferencia() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
            DataFlavor dataFlavor = DataFlavor.stringFlavor;
            if (systemClipboard.isDataFlavorAvailable(dataFlavor)) {
                String text = (String) systemClipboard.getData(dataFlavor);
                jtaEditor.replaceSelection(text);
                verificarMudancasTexto();
            }
        } catch (Exception ex) {
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    /**
     * Verificar se houve mudanças no texto ainda não salvas no arquivo. Se houve,
     * exibe o diálogo solicitando ao usuário que sejam salvas as mudanças. Em
     * caso de o mesmo selecionar <i>Sim</i> (salvar as mudanças no arquivo) ou
     * <i>Não</i> (não salvar as mudanças no arquivo), a ação que solicitou esta
     * verificação deve prosseguir, o que é indicado pelo retorno <i>true</i> do
     * método. Se o usuário selecionar <i>Cancelar</i>, a ação que solicitou a
     * verificação não deve prosseguir.
     * 
     * @return Se <i>true</i>, a ação que solicitou a verificação deve prosseguir.
     * Se <i>false</i>, ela deve ser cancelada.
     */
    private boolean verificarMudancasNoTextoEProsseguir() {
        
        boolean prosseguir = true;
        
        if (arquivoAberto) {
        
            if (houveMudancaTexto) {

                int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Salvar as alterações no arquivo?",
                    "Atenção!",
                    JOptionPane.YES_NO_CANCEL_OPTION
                );

                if (opcao == JOptionPane.YES_OPTION) {

                    try {
                        arquivo.gravar(jtaEditor.getText());
                    } catch (Exception ex) {
                        prosseguir = false;
                        JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Erro",
                            JOptionPane.OK_OPTION
                        );
                    }

                } else if (opcao == JOptionPane.CANCEL_OPTION) {

                    prosseguir = false;

                } 

            }
        
        }
        
        return prosseguir;
        
    }
    
    
    /**
     * Verificar se houve mudanças no texto desde a última vez que este foi
     * salvo no arquivo. A verificação é baseada em alguma edição que mudou
     * momentâneamente o tamanho do texto, seja apagando caracteres ou inserindo.
     */
    private void verificarMudancasTexto() {
        
        int comprimentoAtual = jtaEditor.getText().length();
        
        if (compilacaoPendente == false) {
            if (comprimentoAtual != comprimentoTextoCompilacao) {
                compilacaoPendente = true;
                jbCompilar.setEnabled(true);
                listarConjuntoEstados();
                listarAlfabetoFita();
                listarFuncaoTransicao();
                configurarBarraFerramentasEditor();
                configurarBarraFerramentasSimulador();
            }
        }
        
        if (houveMudancaTexto == false) {    
            if (comprimentoAtual != comprimentoTextoArquivo) {
                houveMudancaTexto = true;
                jbSalvarArquivo.setEnabled(true);
            }
        }
        
    }
    
    
    /**
     * Contar a quantidade de vírgulas que há na string passada.
     * 
     * @param string string a ser analizada.
     * 
     * @return Quantidade de vírgulas na string.
     */
    private int contarVirgulasString(String string) {
        int contagem = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ',') {
                contagem++;
            }
        }
        return contagem;
    }
    
    
    /**
     * Exibir o menu suspenso quando o cursor de texto está na seção [Programa].
     * O objetivo do menu é facilitar a digitação das transições, por exemplo,
     * no caso de estados com rótulo com muitos caracteres, pode-se digitar na
     * lista os caracteres iniciais que o estado é selecionado.
     * 
     * <br><br>
     * 
     * Neste método é identificado em que campo da transição o cursor se encontra
     * e exibe o menu com os valores adequados. Por exemplo, se o cursor se encontra
     * no campo estado atual, ele exibe a lista de estados. Se se encontra no campo
     * símbolo lido, ele exibe a lista de símbolos do alfabeto da fita. Caso se
     * encontre no campo direção do movimento, exibe a lista com as direções. Deste
     * modo, o menu se torna contextual auxiliando o trabalho de digitação.
     */
    private void exibirMenuTransicao() {
        
        int posicaoCabecalho = jtaEditor.getText().indexOf(CABECALHO_PROGRAMA);
        
        if (posicaoCabecalho >= 0) {
            
            try {

                int posicaoCursor = jtaEditor.getCaretPosition();
                int linhaCursor = jtaEditor.getLineOfOffset(posicaoCursor);
                int linhaCabecalho = jtaEditor.getLineOfOffset(posicaoCabecalho);

                if (linhaCursor > linhaCabecalho) {

                    Document doc = jtaEditor.getDocument();
                    Element root = doc.getDefaultRootElement();
                    Element element = root.getElement(linhaCursor);
                    
                    int inicioLinha = element.getStartOffset();
                    int finalLinha = element.getEndOffset();
                    
                    int campo;
                    
                    String substring = jtaEditor.getText().substring(
                        inicioLinha,
                        posicaoCursor
                    );
                    
                    if (substring.contains("=")) {
                        
                        int posicaoIgual = -1;
                        
                        for (int i = inicioLinha; i < finalLinha; i++) {
                            if (jtaEditor.getText().charAt(i) == '=') {
                                posicaoIgual = i;
                                break;
                            }
                        }
                        
                        if (posicaoCursor < posicaoIgual) {
                            int contagem = contarVirgulasString(substring);
                            if (contagem == 0) {
                                campo = 1;
                            } else {
                                campo = 2;
                            }
                        } else {
                            int numFitas = (int) jspNumeroFitas.getValue();
                            int contagem = contarVirgulasString(
                                jtaEditor.getText().substring(
                                    posicaoIgual,
                                    posicaoCursor
                                )
                            );
                            if (contagem == 0) {
                                campo = 1;
                            } else if (contagem < numFitas + 1) {
                                campo = 2;
                            } else {
                                campo = 3;
                            }
                        }
                        
                    } else {
                        int contagem = contarVirgulasString(substring);
                        if (contagem == 0) {
                            campo = 1;
                        } else {
                            campo = 2;
                        }
                    }
                    
                    modeloLista.removeAllElements();
                    
                    switch (campo) {
                        
                        case 1 -> {
                            for (Estado estado : conjuntoEstados) {
                                modeloLista.addElement(estado.getRotulo());
                            }
                        }
                        
                        case 2 -> {
                            for (Simbolo simbolo : alfabetoFita) {
                                modeloLista.addElement(simbolo.toString());
                            }
                        }
                        
                        case 3 -> {
                            for (DirecaoMovimento direcao : DirecaoMovimento.values()) {
                                modeloLista.addElement(direcao.getId());
                            }
                        }
                        
                    }
                    
                    Rectangle caretRect = jtaEditor.modelToView(posicaoCursor);
                    
                    jppAutocompletar.show(
                        jtaEditor,
                        caretRect.x + 10,
                        caretRect.y
                    );
                    
                    jlAutocompletar.setSelectedIndex(-1);
                    
                    jlAutocompletar.scrollRectToVisible(
                        new Rectangle(jlAutocompletar.getCellBounds(0, 0))
                    );
                    
                    jlAutocompletar.requestFocus();

                }

            } catch (Exception ex) {
            }
            
        }
        
    }
    
    
    /**
     * Inserir o item selecionado no menu suspenso na posição atual do cursor.
     */
    private void inserirItemSelecionadoMenuTransicao() {
        int posicaoAtual = jtaEditor.getCaretPosition();
        String estadoSelecionado = jlAutocompletar.getSelectedValue();
        jtaEditor.replaceRange(
            estadoSelecionado,
            posicaoAtual, posicaoAtual
        );
        jtaEditor.requestFocus();
        jtaEditor.setCaretPosition(posicaoAtual + estadoSelecionado.length());
        jppAutocompletar.setVisible(false);
    }
    
    
    
    
// CONFIGURAÇÃO DOS CONTROLES DO EDITOR E DO SIMULADOR ---------------------- //
    
    
    /**
     * Listar os símbolos do alfabeto da fita, preenchendo e configurando a
     * visualização da lista.
     */
    private void listarAlfabetoFita() {

        if (!compilacaoPendente) {
        
            List<String> listaSimbolos = new ArrayList(alfabetoFita.getComprimento());

            for (Simbolo simbolo : alfabetoFita) { 
                StringBuilder sb = new StringBuilder();
                if (!simbolo.isReservado()) {
                    if (simbolo.isAuxiliar()) {
                        sb.append("* ");
                    } else {
                        sb.append("  ");
                    }
                } else {
                    sb.append("  ");                
                }
                sb.append(formatarSimbolos(simbolo.toString()));
                listaSimbolos.add(sb.toString());
            }

            jlAlfabeto.setCellRenderer(new RenderizadorAlfabeto(alfabetoFita));
            DefaultListModel model = new DefaultListModel();
            model.addAll(listaSimbolos);
            jlAlfabeto.setModel(model);
        
        }
        
        configurarBotoesAlfabetoFita();
        
    }
        
    
    /**
     * Listar os estados do conjunto de estados, preenchendo e configurando a
     * visualização da lista.
     */
    private void listarConjuntoEstados() {

        if (!compilacaoPendente) {
        
            List<String> listaEstados = new ArrayList<>(conjuntoEstados.getComprimento());

            for (Estado estado : conjuntoEstados) {
                StringBuilder sb = new StringBuilder();
                
                if (estado.isTerminal()) {
                    sb.append("\u229A");
                }

                if (estado.isInicial()) {
                    sb.append("\u22B3");
                }

                if (!estado.isInicial() && !estado.isTerminal()) {
                    sb.append("  ");
                } else {
                    if (!(estado.isInicial() && estado.isTerminal())) {
                        sb.append(" ");
                    }
                }

                sb.append(estado.getRotulo());
                listaEstados.add(sb.toString());
            }

            DefaultListModel model = new DefaultListModel();
            model.addAll(listaEstados);
            jlEstados.setModel(model);
        
        } 
        
        configurarBotoesConjuntoEstados();
        
    }
    
    
    /**
     * Listar as transições da função de transição, preenchendo e configurando a
     * visualização da lista.
     */
    private void listarFuncaoTransicao() {
        
        if (!compilacaoPendente) {
        
            List<String> listaTransicoes = new ArrayList<>(funcaoTransicao.getComprimento());

            for (Transicao transicao : funcaoTransicao) {
                listaTransicoes.add(formatarSimbolos(transicao.toString()));
            }

            DefaultListModel model = new DefaultListModel();
            model.addAll(listaTransicoes);
            jlTransicoes.setModel(model);
        
        }
        
        configurarBotoesFuncaoTransicao();
        
    }
        
    
    /**
     * Configurar os controles da barra de ferramentas do editor de código.
     */
    private void configurarBarraFerramentasEditor() {
        if (compilando || emExecucao) {
            jtaEditor.setEnabled(false);
            jmiCopiar.setEnabled(false);
            jmiColar.setEnabled(false);
            jbAbrirArquivo.setEnabled(false);
            jbSalvarArquivo.setEnabled(false);
            jbFecharArquivo.setEnabled(false);
            jbCompilar.setEnabled(false);
            jbDesfazer.setEnabled(false);
            jbRefazer.setEnabled(false);
            jbCopiar.setEnabled(false);
            jbColar.setEnabled(false);
        } else {
            jtaEditor.setEnabled(true);
            jmiCopiar.setEnabled(true);
            jmiColar.setEnabled(true);
            jbAbrirArquivo.setEnabled(true);
            jbSalvarArquivo.setEnabled(houveMudancaTexto);
            jbFecharArquivo.setEnabled(arquivoAberto);
            jbCompilar.setEnabled(compilacaoPendente);
            jbDesfazer.setEnabled(true);
            jbRefazer.setEnabled(true);
            jbCopiar.setEnabled(true);
            jbColar.setEnabled(true);
        }
    }
    
    
    /**
     * Configurar os controles da barra de ferramentas de execução do simulador,
     * na parte central da tela.
     */
    private void configurarBarraFerramentasSimulador() {
        if (compilando) {
            jbExecutar.setEnabled(false);
            jbPausar.setEnabled(false);
            jbParar.setEnabled(false);
            jbVelocidade.setEnabled(false);
            jbExecutarPasso.setEnabled(false);
            jbReiniciar.setEnabled(false);
            jrbPadrao.setEnabled(false);
            jrbMultifita.setEnabled(false);
            jrbMoverCursor.setEnabled(false);
            jrbMoverFita.setEnabled(false);
            jbCarregarPalavra.setEnabled(false);
        } else {
            jtfPalavra.setForeground(Color.BLACK);
            Font font = new Font(
                jtfPalavra.getFont().getName(),
                Font.PLAIN,
                jtfPalavra.getFont().getSize()
            );
            jtfPalavra.setFont(font);
            if (emExecucao) {
                jtfPalavra.setFocusable(false);
                jbExecutar.setEnabled(!simulacaoAutomatica);
                jbPausar.setEnabled(!emPausa && simulacaoAutomatica);
                jbParar.setEnabled(true);
                jbVelocidade.setEnabled(true);
                jbExecutarPasso.setEnabled(!simulacaoAutomatica);
                jbCarregarPalavra.setEnabled(false);
                jtfPalavra.setEditable(false);
                jbReiniciar.setEnabled(true);
                jrbPadrao.setEnabled(false);
                jrbMultifita.setEnabled(false);
                jrbMoverCursor.setEnabled(false);
                jrbMoverFita.setEnabled(false);
            } else {
                jtfPalavra.setFocusable(true);
                jtfPalavra.setEditable(true);
                jbCarregarPalavra.setEnabled(!compilacaoPendente);
                jbExecutar.setEnabled(false);
                jbPausar.setEnabled(false);
                jbParar.setEnabled(false);
                jbVelocidade.setEnabled(false);
                jbExecutarPasso.setEnabled(false);
                jbReiniciar.setEnabled(false);
                jrbPadrao.setEnabled(true);
                jrbMultifita.setEnabled(true);
                jrbMoverCursor.setEnabled(true);
                jrbMoverFita.setEnabled(true);
            } 
        } 
    }
    
    
    /**
     * Configurar os botões do alfabeto da fita.
     */
    private void configurarBotoesAlfabetoFita() {
        if (compilando || emExecucao) {
            jlAlfabeto.setEnabled(false);
            jbInserirSimbolo.setEnabled(false);
            jbRemoverSimbolo.setEnabled(false);
            jbEditarSimbolo.setEnabled(false);
            jbAlfabetoAuxiliar.setEnabled(false);
            jbAlfabetoAjuda.setEnabled(false);
        } else {
            jlAlfabeto.setEnabled(!compilacaoPendente);
            jbInserirSimbolo.setEnabled(!compilacaoPendente);
            jlAlfabeto.setSelectionInterval(0, 0);
            jbAlfabetoAjuda.setEnabled(true);
            boolean contemSimbolosEntrada = false;
            for (int i = 0; i < alfabetoFita.getComprimento(); i++) {
                Simbolo s = alfabetoFita.getSimbolo(i);
                if (!s.isReservado()) {
                    contemSimbolosEntrada = true;
                    break;
                }
            }
            if (contemSimbolosEntrada) {
                jbRemoverSimbolo.setEnabled(!compilacaoPendente);
                jbEditarSimbolo.setEnabled(!compilacaoPendente);
                jbAlfabetoAuxiliar.setEnabled(!compilacaoPendente);
            } else {
                jbRemoverSimbolo.setEnabled(false);
                jbEditarSimbolo.setEnabled(false);
                jbAlfabetoAuxiliar.setEnabled(false);
            }
        }
    }
    
    
    /**
     * Configurar os botões do conjunto de estados.
     */
    private void configurarBotoesConjuntoEstados() {
        if (compilando || emExecucao) {
            jlEstados.setEnabled(false);
            jbInserirEstado.setEnabled(false);
            jbRemoverEstado.setEnabled(false);
            jbEditarEstado.setEnabled(false);
            jbSetEstadoInicial.setEnabled(false);
            jbSetEstadoFinal.setEnabled(false);
            jbEstadosAjuda.setEnabled(false);
        } else {
            jlEstados.setEnabled(!compilacaoPendente);
            jbInserirEstado.setEnabled(!compilacaoPendente);
            jbEstadosAjuda.setEnabled(true);
            if (jlEstados.getModel().getSize() > 0) {
                jlEstados.setSelectionInterval(0, 0);
                jbRemoverEstado.setEnabled(!compilacaoPendente);
                jbEditarEstado.setEnabled(!compilacaoPendente);
                jbSetEstadoInicial.setEnabled(!compilacaoPendente);
                jbSetEstadoFinal.setEnabled(!compilacaoPendente);
            } else {
                jbRemoverEstado.setEnabled(false);
                jbEditarEstado.setEnabled(false);
                jbSetEstadoInicial.setEnabled(false);
                jbSetEstadoFinal.setEnabled(false);
            }
        }
    }
    
    
    /**
     * Configurar os botões da função de transição.
     */
    private void configurarBotoesFuncaoTransicao() {
        if (compilando || emExecucao) {
            jlTransicoes.setEnabled(false);
            jbInserirTransicao.setEnabled(false);
            jbRemoverTransicao.setEnabled(false);
            jbMoverTransicaoCima.setEnabled(false);
            jbMoverTransicaoBaixo.setEnabled(false);
            jbFuncaoTransicaoAjuda.setEnabled(false);
            jspNumeroFitas.setEnabled(false);
            jlTransicoes.setSelectionBackground(Color.BLACK);
        } else {
            jlTransicoes.setEnabled(!compilacaoPendente);
            jlTransicoes.setSelectionBackground(new Color(245,245,245));
            jbInserirTransicao.setEnabled(!compilacaoPendente);
            jbFuncaoTransicaoAjuda.setEnabled(true);
            if (jlTransicoes.getModel().getSize() > 0) {
                jlTransicoes.setSelectionInterval(0, 0);
                jbRemoverTransicao.setEnabled(!compilacaoPendente);
                jbMoverTransicaoCima.setEnabled(!compilacaoPendente);
                jbMoverTransicaoBaixo.setEnabled(!compilacaoPendente);
                jspNumeroFitas.setEnabled(false);
            } else {
                jbRemoverTransicao.setEnabled(false);
                jbMoverTransicaoCima.setEnabled(false);
                jbMoverTransicaoBaixo.setEnabled(false);
                jspNumeroFitas.setEnabled(false);
                jspNumeroFitas.setEnabled(!compilacaoPendente);
            }
        }
    }


    /**
     * Configurar todos os controles do simulador.
     */
    private void configurarControlesSimulador() {
        
        configurarBarraFerramentasEditor();
        
        configurarBarraFerramentasSimulador();
        
        configurarBotoesAlfabetoFita();
        
        configurarBotoesConjuntoEstados();
        
        configurarBotoesFuncaoTransicao();
        
    }

    
    
    
// DEFINIÇÃO DOS PARÂMETROS PARA A MÁQUINA DE TURING ------------------------ //    
        
    
    /**
     * Inserir símbolos do alfabeto da fita.
     */
    private void inserirSimbolos() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {
        
            TelaInserirSimbolo telaInserirSimbolo = new TelaInserirSimbolo(
                this,
                alfabetoFita
            );
            
            telaInserirSimbolo.setVisible(true);

            if (!telaInserirSimbolo.isCancelado()) {
                listarAlfabetoFita();
                gerarCodigoPrograma();
            }
        
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
               
    }
    
    
    /**
     * Remover o símbolo selecionado na lista do alfabeto da fita.
     */
    private void removerSimboloSelecionado() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {        
            
            int indice = jlAlfabeto.getSelectedIndex();
            
            if (indice >= 0) {
                
                Simbolo simbolo = alfabetoFita.getSimbolo(indice);
                
                if (!simbolo.isReservado()) {
                    alfabetoFita.removerSimbolo(simbolo);
                    listarAlfabetoFita();
                    gerarCodigoPrograma();
                    jlAlfabeto.setSelectedIndex(indice-1);
                }
                
            }
            
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Editar o símbolo selecionado na lista do alfabeto da fita.
     */
    private void editarSimboloSelecionado() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {
        
            int indice = jlAlfabeto.getSelectedIndex();

            if (indice >= 0) {

                Simbolo simbolo = alfabetoFita.getSimbolo(indice);

                if (!simbolo.isReservado()) {

                    TelaInserirSimbolo telaInserirSimbolo = new TelaInserirSimbolo(
                        this,
                        simbolo,
                        alfabetoFita
                    );
                    
                    telaInserirSimbolo.setVisible(true);

                    if (!telaInserirSimbolo.isCancelado()) {
                        listarAlfabetoFita();
                        gerarCodigoPrograma();
                        jlAlfabeto.setSelectedIndex(indice);
                    }

                }

            }
        
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Definir o símbolo selecionado na lista do alfabeto da fita como
     * pertencente ou não ao alfabeto auxiliar.
     */
    private void definirSimboloSelecionadoComoAuxiliar() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {
        
            int indice = jlAlfabeto.getSelectedIndex();

            if (indice >= 0) {

                Simbolo simbolo = alfabetoFita.getSimbolo(indice);

                if (!simbolo.isReservado()) {
                    alfabetoFita.setSimboloAuxiliar(simbolo, !simbolo.isAuxiliar());
                    listarAlfabetoFita();
                    gerarCodigoPrograma();
                    jlAlfabeto.setSelectedIndex(indice);
                }

            }
        
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Inserir estados.
     */
    private void inserirEstados() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {
        
            TelaInserirEstado telaInserirEstado = new TelaInserirEstado(
                this,
                conjuntoEstados
            );
            
            telaInserirEstado.setVisible(true);

            if (!telaInserirEstado.isCancelado()) {
                listarConjuntoEstados();
                gerarCodigoPrograma();
            }
        
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Remover o estado selecionado na lista do conjunto de estados.
     */
    private void removerEstadoSelecionado() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (!compilacaoPendente) {
            int indice = jlEstados.getSelectedIndex();
            if (indice >= 0) {
                Estado estado = conjuntoEstados.getEstado(indice);
                conjuntoEstados.removerEstado(estado);
                listarConjuntoEstados();
                gerarCodigoPrograma();
                if (indice > 0) {
                    jlEstados.setSelectedIndex(indice-1);
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    /**
     * Editar o estado selecionado na lista do conjunto de estados.
     */
    private void editarEstadoSelecionado() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {
        
            int indice = jlEstados.getSelectedIndex();

            if (indice >= 0) {

                Estado estado = conjuntoEstados.getEstado(indice);

                TelaInserirEstado telaInserirEstado = new TelaInserirEstado(
                    this,
                    estado,
                    conjuntoEstados
                );
                
                telaInserirEstado.setVisible(true);

                if (!telaInserirEstado.isCancelado()) {
                    listarConjuntoEstados();
                    gerarCodigoPrograma();
                    jlEstados.setSelectedIndex(indice);
                }

            }
        
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Definir o estado selecionado na lista do conjunto de estados como estado
     * inicial.
     */
    private void definirEstadoSelecionadoComoInicial() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {

            int indice = jlEstados.getSelectedIndex();

            if (indice >= 0) {

                Estado estadoInicial = conjuntoEstados.getEstado(indice);
                
                conjuntoEstados.setEstadoInicial(estadoInicial);
                
                listarConjuntoEstados();

                gerarCodigoPrograma();

                jlEstados.setSelectedIndex(indice);

            }

        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Definir o estado selecionado na lista do conjunto de estados como
     * estado terminal.
     */
    private void definirEstadoSelecionadoComoTerminal() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {
        
            int indice = jlEstados.getSelectedIndex();

            if (indice >= 0) {

                Estado estado = conjuntoEstados.getEstado(indice);
                
                conjuntoEstados.setEstadoTerminal(estado, !estado.isTerminal());

                listarConjuntoEstados();

                gerarCodigoPrograma();

                jlEstados.setSelectedIndex(indice);

            }
        
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }

    
    /**
     * Inserir uma transição da função de transição.
     */
    private void inserirTransicao() {
     
        TelaInserirTransicao telaInserirTransicao = new TelaInserirTransicao(
            this,
            alfabetoFita,
            conjuntoEstados,
            funcaoTransicao,
            (int)jspNumeroFitas.getValue()
        );
        
        telaInserirTransicao.setVisible(true);
        
        if (telaInserirTransicao.isCancelado()) {
            
            listarFuncaoTransicao();
            
            gerarCodigoPrograma();
            
        }
        
    }
    
    
    /**
     * Remover a transição selecionada na lista da função de transição.
     */
    private void removerTransicaoSelecionada() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {
        
            int indice = jlTransicoes.getSelectedIndex();

            if (indice >= 0) {
                
                Transicao transicao = funcaoTransicao.getTransicao(indice);
                
                if (funcaoTransicao.removerTransicao(transicao)) {

                    listarFuncaoTransicao();

                    gerarCodigoPrograma();

                    jlTransicoes.setSelectedIndex(indice - 1);
                
                }

            }
        
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Mover para cima a transição selecionada na lista da função de transição.
     */
    private void moverTransicaoSelecionadaParaCima() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {
        
            int indice = jlTransicoes.getSelectedIndex();

            if (indice >= 0) {
                
                if (funcaoTransicao.moverTransicaoParaCima(indice)) {

                    listarFuncaoTransicao();

                    gerarCodigoPrograma();

                    jlTransicoes.setSelectedIndex(indice - 1);
                
                }

            }
        
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Mover para baixo a transição selecionada na lista da função de transição.
     */
    private void moverTransicaoSelecionadaParaBaixo() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (!compilacaoPendente) {
        
            int indice = jlTransicoes.getSelectedIndex();

            if (indice >= 0) {
                
                if (funcaoTransicao.moverTransicaoParaBaixo(indice)) {

                    listarFuncaoTransicao();

                    gerarCodigoPrograma();

                    jlTransicoes.setSelectedIndex(indice + 1);

                }

            }
        
        } else {
            
            JOptionPane.showMessageDialog(
                this,
                "Compile o programa para prosseguir.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    /**
     * Gerar o código do programa no editor com base nos parâmetros definidos
     * nos campos da interface gráfica de usuário.
     */
    private void gerarCodigoPrograma() {
        
        comprimentoTextoCompilacao = jtaEditor.getText().length();
        jbCompilar.setEnabled(false);
        jbSalvarArquivo.setEnabled(true);
        houveMudancaTexto = true;
        
        try {
            
            ConfigMaqTuring confMaqTuring = new ConfigMaqTuring(
                nome,
                alfabetoFita,
                conjuntoEstados,
                funcaoTransicao,
                (int)jspNumeroFitas.getValue()
            );
            
            GeradorCodigo geradorCodigo = new GeradorCodigo();
            
            String codigoPrograma = geradorCodigo.executar(
                confMaqTuring,
                jtaEditor.getText()
            );
            
            jtaEditor.setText(codigoPrograma);
            
            undoManager.discardAllEdits();
            
            jtaEditor.setCaretPosition(0);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Erro",
                JOptionPane.OK_OPTION
            );
        }
        
    }
    
    
    
    
// CONTROLE DE EXECUÇÃO DO SIMULADOR ---------------------------------------- //
    
    
    /**
     * Carregar a palavra de entrada na primeira fita do simulador.
     */
    private void carregarPalavraEntrada() {
        
        if (maquinaTuring == null) {
            
            if (jlTransicoes.getModel().getSize() > 0) {
                    
                modelo = jrbPadrao.isSelected() ? Modelo.PADRAO : Modelo.MULTIFITAS;

                try {

                    switch (modelo) {

                        case PADRAO -> maquinaTuring = new MaquinaPadrao(
                            alfabetoFita,
                            conjuntoEstados,
                            funcaoTransicao,
                            (int)jspNumeroFitas.getValue()
                        );

                        case MULTIFITAS -> maquinaTuring = new MaquinaMultifitas(
                            alfabetoFita,
                            conjuntoEstados,
                            funcaoTransicao,
                            (int)jspNumeroFitas.getValue()
                        );

                    }

                    maquinaTuring.adicionarOuvinte(this);

                    maquinaTuring.carregarPalavra(jtfPalavra.getText());

                    emExecucao = true;

                    simulacaoAutomatica = false;

                    emPausa = false;

                    configurarControlesSimulador();

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );

                }
            
            }
            
        }
        
    }
       
    
    /**
     * Iniciar a simulação automática da Máquina de Turing.
     */
    private void iniciarSimulacaoAutomatica() {
        
        if (maquinaTuring != null) {
            
            simulacaoAutomatica = true;
            emPausa = false;
            atualizandoEtapaSimulacao = false;
            
            configurarBarraFerramentasSimulador();
            
            executarSimulacaoAutomatica();
            
        }
        
    }
    
    
    /**
     * Executar a simulação automática dentro do ciclo de em um componente Timer.
     * Caso mude a velocidade de simulação, o tiver deve ser renovado com as
     * novas configurações.
     */
    private synchronized void executarSimulacaoAutomatica() {
        
        if (emExecucao) {
            
            if (simulacaoAutomatica) {

                if (timer != null)  {
                    timer.cancel();
                    timer = null;
                }

                timer = new Timer();

                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (maquinaTuring != null) {
                            if (!atualizandoEtapaSimulacao) {
                                atualizandoEtapaSimulacao = true;
                                maquinaTuring.executarPasso();
                                atualizandoEtapaSimulacao = false;
                            }
                        }
                    }
                };

                timer.schedule(
                    task,
                    tempoExecucao,
                    tempoExecucao
                );
            
            }
        
        }
            
    }
    
    
    /**
     * Pausar a execução automática do simulador.
     */
    private void pausarSimulacaoAutomatica() {
        if (maquinaTuring != null) {
            emPausa = true;
            simulacaoAutomatica = false;
            configurarBarraFerramentasSimulador();
            timer.cancel();
        }
    }
    
    
    /**
     * Executar manualmente o próximo passo do programa.
     */
    private void executarPassoSimulacao() {
        if (maquinaTuring != null) {
            maquinaTuring.executarPasso();
        }
    }
    
    
    /**
     * Encerrar a execução do simulador.
     */
    private void encerrarSimulacao() {
        if (maquinaTuring != null) {
            if (timer != null)  timer.cancel();
            emExecucao = false;
            simulacaoAutomatica = false;
            emPausa = false;
            maquinaTuring.removerOuvinte(this);
            maquinaTuring = null;
            jtfEstadoAtual.setText("");
            jtfNumPassos.setText("");
            jtfResultado.setText("");
            configurarControlesSimulador();
            configurarFitasModeloSelecionado();
        }
    }
    
    
    /**
     * Reiniciar a simulação para a palavra de entrada.
     */
    private void reiniciarSimulacao() {
        if (maquinaTuring != null) {
            if (timer != null) timer.cancel();
            emExecucao = true;
            simulacaoAutomatica = false;
            emPausa = false;
            configurarBarraFerramentasSimulador();
            maquinaTuring.reiniciar();
        }
    }
    
    
    /**
     * Configurar o tempo de execução de cada etapa da simulação. 
     */
    private void configurarVelocidadeSimulacaoAutomatica() {
        
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        
        int x = (int) b.getX() - 350;
        int y = (int) b.getY() - 45;
        
        TelaConfigVelocidadeSimulacao telaConfigVelocidade = new TelaConfigVelocidadeSimulacao(
            this,
            this,
            tempoExecucao
        );
        
        telaConfigVelocidade.setLocation(x, y);
        
        telaConfigVelocidade.setVisible(true);

    }
    
    
    /**
     * Atualizar o tempo de execução da etapa do simulador. Este método responde
     * às alterações de tempo realizadas no diálogo {@link TelaConfigVelocidadeSimulacao}.
     * 
     * @param novoValor novo valor de tempo de execução.
     */
    @Override
    public void velocidadeSimulacaoAutomaticaAtualizada(int novoValor) {
        if (novoValor != tempoExecucao) {
            tempoExecucao = novoValor;
            executarSimulacaoAutomatica();
        }
    }
    
    

    
// CONFIGURAÇÃO DAS FITAS --------------------------------------------------- //    
    
    
    /**
     * Configurar as fitas com espaços em branco de acordo com o modelo de Máquina
     * de Turing selecionado.
     * 
     * <br><br>
     * 
     * <i>
     * <b>Obs.: </b> As fitas dependem de renderizadores especializados para 
     * serem configuradas com a visualização adequada, de acordo com o conteúdo
     * texto de cada célula.
     * </i>
     */
    private void configurarFitasModeloSelecionado() {
        
        modelo = jrbPadrao.isSelected() ? Modelo.PADRAO : Modelo.MULTIFITAS;
        
        int numeroFitas = jrbPadrao.isSelected() ? 1 : (int) jspNumeroFitas.getValue();
        int numeroCelulas = TAMANHO_FITA;
        
        String[][] celulas = new String[numeroFitas][numeroCelulas];
        String[] titulos = new String[numeroCelulas];
        
        for (int i = 0; i < numeroFitas; i++) {
            for (int j = 0; j < numeroCelulas; j++) {
                celulas[i][j] = String.valueOf(SIMBOLO_BRANCO);
            }
        }
        
        jtFitas.setModel(new DefaultTableModel(celulas, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        RendererizadorFita renderer = new RendererizadorFita(jrbMoverCursor.isSelected());

        for (int i = 0; i < jtFitas.getColumnCount(); i++) {
            jtFitas.getColumnModel().getColumn(i).setCellRenderer(renderer);
        } 

        for (int i = 0; i < jtFitas.getColumnCount(); i++) {
            jtFitas.getColumnModel().getColumn(i).setPreferredWidth(80);
            jtFitas.getColumnModel().getColumn(i).setMaxWidth(80);
        }
        
        if (jrbMoverFita.isSelected()) {
            jtFitas.setAutoResizeMode(
                javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
            );
        } else {
            jtFitas.setAutoResizeMode(
                javax.swing.JTable.AUTO_RESIZE_OFF
            );
        }
        
    }


    /**
     * Configurar as fitas de acordo com o estado atual de simulação. Há dois
     * modos de visualização das fitas:
     * 
     * <br><br>
     * 
     * <b>Cursores se movendo sobre as fitas:</b> Neste modo de visualização, as 
     * fitas permanecem fixas e os cursores se movem sobre elas. É o modo como
     * Turing concebeu inicialmenteo modelo.
     * 
     * <br><br>
     * 
     * <b>Fitas se movendo sob os cursores: </b>  Neste modo de visualização, os
     * cursores permanecem fixos, e são as fitas que se movem sob estes.
     * 
     * <br><br>
     * 
     * O estado das fitas é atualizada após a execução de cada etapa da simulação
     * da Máquina de Turing para a palavra de entrada.
     * 
     * <br><br>
     * 
     * <i>
     * <b>Obs.: </b> As fitas dependem de renderizadores especializados para 
     * serem configuradas com a visualização adequada, de acordo com o conteúdo
     * texto de cada célula.
     * </i>
     * 
     * @param fitas fitas da Máquina de Turing.
     * 
     * @param cursores cursores posicionados sobre as fitas.
     */
    private void configurarFitas(Fita[] fitas, Map<Integer, Integer> cursores) {
        
        if (jrbMoverFita.isSelected()) {
            
            // Cursores fixos e fitas se movem.
            
            int colCursor = 11;
            
            for (int i = 0; i < jtFitas.getRowCount(); i++) {
                for (int j = 0; j < jtFitas.getColumnCount(); j++) {
                    jtFitas.setValueAt(String.valueOf(SIMBOLO_BRANCO), i, j);
                }
            }
            
            for (int i = 0; i < cursores.size(); i++) {
                
                int cursor = cursores.get(i);
                
                String s = fitas[i].getCelulas()[cursor].toString() + SUFIXO_CURSOR;
                
                jtFitas.setValueAt(s, i, colCursor);
                
                for (int j = colCursor - 1, k = cursor - 1; j >= 0 && k >= 0; j--, k--) {
                    if (k == fitas[i].getCelulaPivo()) {
                        s = fitas[i].getCelulas()[k].toString() + SUFIXO_CEL_PIVO;
                    } else {
                        s = fitas[i].getCelulas()[k].toString();
                    }
                    jtFitas.setValueAt(s, i, j);
                }
                
                for (int j = colCursor + 1, k = cursor + 1; j < TAMANHO_FITA &&
                k < fitas[i].getComprimento(); j++, k++) {
                    if (k == fitas[i].getCelulaPivo()) {
                        s = fitas[i].getCelulas()[k].toString() + SUFIXO_CEL_PIVO;
                    } else {
                        s = fitas[i].getCelulas()[k].toString();
                    }
                    jtFitas.setValueAt(s, i, j);
                }
                
            }

        } else {
            
            // Fitas fixas e cursores se movem.

            if (jtFitas.getColumnCount() != fitas[0].getComprimento()) {

                String[][] listaFitas = new String[fitas.length][fitas[0].getComprimento()];
                String[] titulos = new String[fitas[0].getComprimento()];

                jtFitas.setModel(new DefaultTableModel(listaFitas, titulos) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });

                RendererizadorFita renderer = new RendererizadorFita(jrbMoverCursor.isSelected());

                for (int i = 0; i < jtFitas.getColumnCount(); i++) {
                    jtFitas.getColumnModel().getColumn(i).setCellRenderer(renderer);
                } 

                for (int i = 0; i < jtFitas.getColumnCount(); i++) {
                    jtFitas.getColumnModel().getColumn(i).setPreferredWidth(80);
                    jtFitas.getColumnModel().getColumn(i).setMaxWidth(80);
                }

                jtFitas.setAutoResizeMode(
                    javax.swing.JTable.AUTO_RESIZE_OFF
                );

            } 

            for (int i = 0; i < jtFitas.getRowCount(); i++) {
                for (int j = 0; j < jtFitas.getColumnCount(); j++) {
                    jtFitas.setValueAt(String.valueOf(SIMBOLO_BRANCO), i, j);
                }
            }

            for (int i = 0; i < jtFitas.getRowCount(); i++) {
                for (int j = 0; j < jtFitas.getColumnCount() ; j++) {
                    String s;
                    if (j == cursores.get(i)) {
                        s = fitas[i].getCelulas()[j].toString() + SUFIXO_CURSOR;
                    } else if (j == fitas[i].getCelulaPivo()) {
                        s = fitas[i].getCelulas()[j].toString() + SUFIXO_CEL_PIVO;
                    } else {
                        s = fitas[i].getCelulas()[j].toString();
                    }
                    jtFitas.setValueAt(
                        s,
                        i,
                        j
                    );
                }
            }
        
        }

    }
    
    
    /**
     * Atualizar a configuração das fitas. Este método é notificado após a execução
     * de cada etapa da simulação da Máquina de Turing. Basicamente o que ele faz
     * é configurar as fitas, apontar para a próxima transição a ser executada na
     * função de transição, e se o programa finalizou, indicar se a palavra de 
     * entrada foi aceita ou não.
     * 
     * @param estadoAtual estado atual da Unidade de Controle.
     * 
     * @param fitas fitas da Máquina de Turing gravadas com símbolos pela Cabeça
     * de Leitura/Escrita.
     * 
     * @param cursores cursores indicando as posições das Cabeças de Leitura/Escrita
     * nas células das fitas.
     * 
     * @param indiceTransicaoAtual índice da transição a ser executada no passo
     * atual.
     * 
     * @param numeroPassos número de passos realizados pela Máquina de Turing.
     * 
     * @param cadeiaAceita status de cadeia de entrada aceita.
     * 
     * @param finalizado status de programa finalizado.
     */
    @Override
    public void atualizarEtapaSimulacao(Estado estadoAtual, Fita[] fitas, 
    Map<Integer, Integer> cursores, int indiceTransicaoAtual, int numeroPassos,
    boolean cadeiaAceita, boolean finalizado) {
        
        configurarFitas(fitas, cursores);
        
        if (indiceTransicaoAtual >= 0) {
            jlTransicoes.setSelectedIndex(indiceTransicaoAtual);
            jlTransicoes.scrollRectToVisible(
                new Rectangle(
                    jlTransicoes.getCellBounds(
                        indiceTransicaoAtual,
                        indiceTransicaoAtual
                    )
                )
            );
        } else {
            jlTransicoes.clearSelection();
        }
        
        jtfEstadoAtual.setText(estadoAtual.toString());
        jtfNumPassos.setText(String.valueOf(numeroPassos));
        
        if (finalizado) {
        
            if (cadeiaAceita) {
                emExecucao = false;
                if (timer != null) timer.cancel();
                jtfResultado.setText("ACEITA");
                jbExecutar.setEnabled(false);
                jbPausar.setEnabled(false);
                jbExecutarPasso.setEnabled(false);
                jbVelocidade.setEnabled(false);
                jtfPalavra.setForeground(new Color(0,128,0));
                Font font = new Font(
                    jtfPalavra.getFont().getName(),
                    Font.BOLD,
                    jtfPalavra.getFont().getSize()
                );
                jtfPalavra.setFont(font);
                jtfResultado.setForeground(new Color(0,128,0));
            } else {
                emExecucao = false;
                if (timer != null) timer.cancel();
                jtfResultado.setText("REJEITA");
                jbExecutar.setEnabled(false);
                jbPausar.setEnabled(false);
                jbExecutarPasso.setEnabled(false);
                jbVelocidade.setEnabled(false);
                jtfPalavra.setForeground(Color.red);
                Font font = new Font(
                    jtfPalavra.getFont().getName(),
                    Font.BOLD,
                    jtfPalavra.getFont().getSize()
                );
                jtfPalavra.setFont(font);
                jtfResultado.setForeground(Color.red);
            } 
        
        } else {
            jtfResultado.setForeground(Color.BLACK);
            jtfResultado.setText("EXECUTANDO");
            jtfResultado.setForeground(Color.BLACK);
        }
        
        repaint();
        
    }
    
    
    /**
     * Exibir o diálogo Detalhes das Fitas. Se o usuário optar pela visualização 
     * das fitas tendo como base os cursores fixos e elas se movendo sob estes,
     * terá um número definido de células nas fitas e alguns símbolos poderão
     * ficar ocultos. O diálogo exibe todo o conteúdo nas fitas.
     */
    private void exibirDetalhesFitas() {
        new TelaDetalhesFitas(
            this,
            maquinaTuring,
            (int) jspNumeroFitas.getValue()
        ).setVisible(true);
    }
    
    
    
    
// OUTROS MÉTODOS ----------------------------------------------------------- //  
        
    
    /**
     * Mudar o título da tela.
     */
    private void definirTituloTela() {
        if (nome != null) {
            if (jtpSimulador.getSelectedIndex() == 0) {
                setTitle(
                    titulo + "  [ PROGRAMA: " + 
                    nome.toUpperCase() + " ]"
                );
            } else {
                if (arquivoAberto) {
                    setTitle(
                        titulo + " - " + 
                        arquivo.getArquivo().getAbsolutePath()
                    );
                } else {
                    setTitle(titulo);
                }
            }
        }
    }

    
    /**
     * Exibir um arquivo de ajuda inserido como recurso no .jar do projeto.
     * 
     * @param titulo título do diálogo de ajuda.
     * 
     * @param arquivo arquivo de ajuda a ser visualizado.
     */
    private void exibirAjuda(String titulo, String arquivo) {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        new TelaAjuda(
            this,
            titulo,
            arquivo
        ).setVisible(true);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    /**
     * Exibir a tela de créditos do simulador.
     */
    private void exibirTelaSobre() {
        new TelaSobre(this).setVisible(true);
    }
    
    
    /**
     * Encerrar o programa. Antes de fechar, verifica se tem um arquivo aberto
     * com modificações pendentes de serem salvas.
     */
    private void fecharTela() {
        if (verificarMudancasNoTextoEProsseguir()) {
            System.exit(0);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jppEditor = new javax.swing.JPopupMenu();
        jmiCopiar = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jmiColar = new javax.swing.JMenuItem();
        btgModelo = new javax.swing.ButtonGroup();
        jppFitas = new javax.swing.JPopupMenu();
        jmiDetalhesFitas = new javax.swing.JMenuItem();
        jpAutocompletar = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jlAutocompletar = new javax.swing.JList<>();
        jppAutocompletar = new javax.swing.JPopupMenu();
        btgOpcoesFita = new javax.swing.ButtonGroup();
        jtpSimulador = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlAlfabeto = new javax.swing.JList<>();
        jbInserirSimbolo = new javax.swing.JButton();
        jbRemoverSimbolo = new javax.swing.JButton();
        jbEditarSimbolo = new javax.swing.JButton();
        jbAlfabetoAuxiliar = new javax.swing.JButton();
        jbAlfabetoAjuda = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jbInserirTransicao = new javax.swing.JButton();
        jbRemoverTransicao = new javax.swing.JButton();
        jspNumeroFitas = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jbMoverTransicaoCima = new javax.swing.JButton();
        jbMoverTransicaoBaixo = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jlTransicoes = new javax.swing.JList<>();
        jbFuncaoTransicaoAjuda = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jlEstados = new javax.swing.JList<>();
        jbInserirEstado = new javax.swing.JButton();
        jbRemoverEstado = new javax.swing.JButton();
        jbEditarEstado = new javax.swing.JButton();
        jbSetEstadoInicial = new javax.swing.JButton();
        jbSetEstadoFinal = new javax.swing.JButton();
        jbEstadosAjuda = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jtfPalavra = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtFitas = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel5 = new javax.swing.JLabel();
        jbExecutar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jbPausar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jbReiniciar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jbVelocidade = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jbExecutarPasso = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jbParar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtfNumPassos = new javax.swing.JLabel();
        jtfEstadoAtual = new javax.swing.JLabel();
        jtfResultado = new javax.swing.JLabel();
        jbCarregarPalavra = new javax.swing.JButton();
        jrbPadrao = new javax.swing.JRadioButton();
        jrbMultifita = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jtfSobre = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jrbMoverFita = new javax.swing.JRadioButton();
        jrbMoverCursor = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jbCompilar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jbAbrirArquivo = new javax.swing.JButton();
        jbSalvarArquivo = new javax.swing.JButton();
        jbFecharArquivo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jbDesfazer = new javax.swing.JButton();
        jbRefazer = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jbCopiar = new javax.swing.JButton();
        jbColar = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaCompilacao = new javax.swing.JTextArea();
        jspEditor = new javax.swing.JScrollPane();
        jtaEditor = new javax.swing.JTextArea();

        jmiCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/copy_icon.png"))); // NOI18N
        jmiCopiar.setText("Copiar");
        jmiCopiar.setPreferredSize(new java.awt.Dimension(160, 36));
        jmiCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCopiarActionPerformed(evt);
            }
        });
        jppEditor.add(jmiCopiar);
        jppEditor.add(jSeparator4);

        jmiColar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/paste_icon.png"))); // NOI18N
        jmiColar.setText("Colar");
        jmiColar.setToolTipText("");
        jmiColar.setPreferredSize(new java.awt.Dimension(160, 36));
        jmiColar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiColarActionPerformed(evt);
            }
        });
        jppEditor.add(jmiColar);

        jmiDetalhesFitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/details_icon.png"))); // NOI18N
        jmiDetalhesFitas.setText("Mostrar Detalhes");
        jmiDetalhesFitas.setPreferredSize(new java.awt.Dimension(160, 36));
        jmiDetalhesFitas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiDetalhesFitasActionPerformed(evt);
            }
        });
        jppFitas.add(jmiDetalhesFitas);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jlAutocompletar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jlAutocompletar.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlAutocompletar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlAutocompletarMouseClicked(evt);
            }
        });
        jlAutocompletar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jlAutocompletarKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jlAutocompletar);

        javax.swing.GroupLayout jpAutocompletarLayout = new javax.swing.GroupLayout(jpAutocompletar);
        jpAutocompletar.setLayout(jpAutocompletarLayout);
        jpAutocompletarLayout.setHorizontalGroup(
            jpAutocompletarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
        );
        jpAutocompletarLayout.setVerticalGroup(
            jpAutocompletarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jppAutocompletar.setFocusable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jtpSimulador.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jtpSimuladorStateChanged(evt);
            }
        });

        jSplitPane1.setDividerLocation(340);
        jSplitPane1.setDividerSize(4);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("    Alfabeto da Fita    "));
        jPanel1.setPreferredSize(new java.awt.Dimension(308, 234));

        jlAlfabeto.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 18)); // NOI18N
        jlAlfabeto.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlAlfabeto.setSelectionBackground(new java.awt.Color(245, 245, 245));
        jlAlfabeto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlAlfabetoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jlAlfabeto);

        jbInserirSimbolo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/pluss_icon.png"))); // NOI18N
        jbInserirSimbolo.setPreferredSize(new java.awt.Dimension(35, 25));
        jbInserirSimbolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbInserirSimboloActionPerformed(evt);
            }
        });

        jbRemoverSimbolo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/minus_icon.png"))); // NOI18N
        jbRemoverSimbolo.setPreferredSize(new java.awt.Dimension(35, 25));
        jbRemoverSimbolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoverSimboloActionPerformed(evt);
            }
        });

        jbEditarSimbolo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/pencil_icon.png"))); // NOI18N
        jbEditarSimbolo.setPreferredSize(new java.awt.Dimension(35, 25));
        jbEditarSimbolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEditarSimboloActionPerformed(evt);
            }
        });

        jbAlfabetoAuxiliar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/a.png"))); // NOI18N
        jbAlfabetoAuxiliar.setPreferredSize(new java.awt.Dimension(35, 25));
        jbAlfabetoAuxiliar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAlfabetoAuxiliarActionPerformed(evt);
            }
        });

        jbAlfabetoAjuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/help_icon.png"))); // NOI18N
        jbAlfabetoAjuda.setPreferredSize(new java.awt.Dimension(35, 25));
        jbAlfabetoAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAlfabetoAjudaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jbInserirSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRemoverSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbEditarSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbAlfabetoAuxiliar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbAlfabetoAjuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbInserirSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbRemoverSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEditarSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbAlfabetoAuxiliar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbAlfabetoAjuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("    Função de Transição    "));

        jbInserirTransicao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/pluss_icon.png"))); // NOI18N
        jbInserirTransicao.setPreferredSize(new java.awt.Dimension(35, 25));
        jbInserirTransicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbInserirTransicaoActionPerformed(evt);
            }
        });

        jbRemoverTransicao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/minus_icon.png"))); // NOI18N
        jbRemoverTransicao.setPreferredSize(new java.awt.Dimension(35, 25));
        jbRemoverTransicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoverTransicaoActionPerformed(evt);
            }
        });

        jspNumeroFitas.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jspNumeroFitas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspNumeroFitasStateChanged(evt);
            }
        });

        jLabel1.setText("Fitas:");

        jbMoverTransicaoCima.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/up_icon.png"))); // NOI18N
        jbMoverTransicaoCima.setPreferredSize(new java.awt.Dimension(35, 25));
        jbMoverTransicaoCima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbMoverTransicaoCimaActionPerformed(evt);
            }
        });

        jbMoverTransicaoBaixo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/down_icon.png"))); // NOI18N
        jbMoverTransicaoBaixo.setPreferredSize(new java.awt.Dimension(35, 25));
        jbMoverTransicaoBaixo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbMoverTransicaoBaixoActionPerformed(evt);
            }
        });

        jlTransicoes.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 18)); // NOI18N
        jlTransicoes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlTransicoes.setSelectionBackground(new java.awt.Color(245, 245, 245));
        jScrollPane5.setViewportView(jlTransicoes);

        jbFuncaoTransicaoAjuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/help_icon.png"))); // NOI18N
        jbFuncaoTransicaoAjuda.setPreferredSize(new java.awt.Dimension(35, 25));
        jbFuncaoTransicaoAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFuncaoTransicaoAjudaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jbInserirTransicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRemoverTransicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbMoverTransicaoCima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbMoverTransicaoBaixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbFuncaoTransicaoAjuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jspNumeroFitas, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbRemoverTransicao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbInserirTransicao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jspNumeroFitas)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbMoverTransicaoCima, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbMoverTransicaoBaixo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbFuncaoTransicaoAjuda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("    Estados   "));

        jlEstados.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 18)); // NOI18N
        jlEstados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlEstados.setSelectionBackground(new java.awt.Color(245, 245, 245));
        jlEstados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlEstadosMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jlEstados);

        jbInserirEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/pluss_icon.png"))); // NOI18N
        jbInserirEstado.setPreferredSize(new java.awt.Dimension(35, 25));
        jbInserirEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbInserirEstadoActionPerformed(evt);
            }
        });

        jbRemoverEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/minus_icon.png"))); // NOI18N
        jbRemoverEstado.setPreferredSize(new java.awt.Dimension(35, 25));
        jbRemoverEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoverEstadoActionPerformed(evt);
            }
        });

        jbEditarEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/pencil_icon.png"))); // NOI18N
        jbEditarEstado.setPreferredSize(new java.awt.Dimension(35, 25));
        jbEditarEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEditarEstadoActionPerformed(evt);
            }
        });

        jbSetEstadoInicial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/state_1_icon.png"))); // NOI18N
        jbSetEstadoInicial.setPreferredSize(new java.awt.Dimension(35, 25));
        jbSetEstadoInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSetEstadoInicialActionPerformed(evt);
            }
        });

        jbSetEstadoFinal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/initial_icon.png"))); // NOI18N
        jbSetEstadoFinal.setPreferredSize(new java.awt.Dimension(35, 25));
        jbSetEstadoFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSetEstadoFinalActionPerformed(evt);
            }
        });

        jbEstadosAjuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/help_icon.png"))); // NOI18N
        jbEstadosAjuda.setPreferredSize(new java.awt.Dimension(35, 25));
        jbEstadosAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEstadosAjudaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jbInserirEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(jbRemoverEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbEditarEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbSetEstadoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbSetEstadoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbEstadosAjuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbInserirEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbRemoverEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEditarEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSetEstadoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSetEstadoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEstadosAjuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSplitPane1.setTopComponent(jPanel3);

        jtfPalavra.setEditable(false);
        jtfPalavra.setBackground(new java.awt.Color(255, 255, 255));
        jtfPalavra.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jtfPalavra.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jtfPalavra.setPreferredSize(new java.awt.Dimension(7, 29));
        jtfPalavra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfPalavraKeyReleased(evt);
            }
        });

        jtFitas.setBackground(new java.awt.Color(240, 240, 240));
        jtFitas.setFont(new java.awt.Font("DejaVu Sans", 0, 28)); // NOI18N
        jtFitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtFitas.setComponentPopupMenu(jppFitas);
        jtFitas.setFillsViewportHeight(true);
        jtFitas.setFocusable(false);
        jtFitas.setRowHeight(40);
        jtFitas.setRowSelectionAllowed(false);
        jtFitas.getTableHeader().setResizingAllowed(false);
        jtFitas.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jtFitas);

        jLabel2.setText("Palavra:");
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel2.setMaximumSize(new java.awt.Dimension(50, 22));
        jLabel2.setMinimumSize(new java.awt.Dimension(50, 22));
        jLabel2.setPreferredSize(new java.awt.Dimension(50, 22));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(508, 80));
        jToolBar1.setMinimumSize(new java.awt.Dimension(502, 80));
        jToolBar1.setPreferredSize(new java.awt.Dimension(379, 80));

        jLabel5.setText(" ");
        jToolBar1.add(jLabel5);

        jbExecutar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/start_icon.png"))); // NOI18N
        jbExecutar.setText("Executar");
        jbExecutar.setToolTipText("Executar simulação");
        jbExecutar.setFocusable(false);
        jbExecutar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbExecutar.setMaximumSize(new java.awt.Dimension(100, 70));
        jbExecutar.setMinimumSize(new java.awt.Dimension(100, 70));
        jbExecutar.setPreferredSize(new java.awt.Dimension(100, 70));
        jbExecutar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbExecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExecutarActionPerformed(evt);
            }
        });
        jToolBar1.add(jbExecutar);

        jLabel8.setText(" ");
        jToolBar1.add(jLabel8);

        jbPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/pause_icon.png"))); // NOI18N
        jbPausar.setText("Pausar");
        jbPausar.setToolTipText("Pausar simulação");
        jbPausar.setFocusable(false);
        jbPausar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbPausar.setMaximumSize(new java.awt.Dimension(100, 70));
        jbPausar.setMinimumSize(new java.awt.Dimension(100, 70));
        jbPausar.setPreferredSize(new java.awt.Dimension(100, 70));
        jbPausar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbPausar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPausarActionPerformed(evt);
            }
        });
        jToolBar1.add(jbPausar);

        jLabel12.setText(" ");
        jToolBar1.add(jLabel12);

        jbReiniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/first_icon.png"))); // NOI18N
        jbReiniciar.setText("Reiniciar");
        jbReiniciar.setFocusable(false);
        jbReiniciar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbReiniciar.setMaximumSize(new java.awt.Dimension(100, 70));
        jbReiniciar.setMinimumSize(new java.awt.Dimension(100, 70));
        jbReiniciar.setPreferredSize(new java.awt.Dimension(100, 70));
        jbReiniciar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbReiniciarActionPerformed(evt);
            }
        });
        jToolBar1.add(jbReiniciar);

        jLabel13.setText(" ");
        jToolBar1.add(jLabel13);

        jbVelocidade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/clock_icon.png"))); // NOI18N
        jbVelocidade.setText("Velocidade");
        jbVelocidade.setToolTipText("Velocidade da simulação");
        jbVelocidade.setFocusable(false);
        jbVelocidade.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbVelocidade.setMaximumSize(new java.awt.Dimension(100, 70));
        jbVelocidade.setMinimumSize(new java.awt.Dimension(100, 70));
        jbVelocidade.setPreferredSize(new java.awt.Dimension(100, 70));
        jbVelocidade.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbVelocidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbVelocidadeActionPerformed(evt);
            }
        });
        jToolBar1.add(jbVelocidade);

        jLabel10.setText(" ");
        jToolBar1.add(jLabel10);

        jbExecutarPasso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/step_icon.png"))); // NOI18N
        jbExecutarPasso.setText("Passo");
        jbExecutarPasso.setToolTipText("Executar passo a passo");
        jbExecutarPasso.setFocusable(false);
        jbExecutarPasso.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbExecutarPasso.setMaximumSize(new java.awt.Dimension(100, 70));
        jbExecutarPasso.setMinimumSize(new java.awt.Dimension(100, 70));
        jbExecutarPasso.setPreferredSize(new java.awt.Dimension(100, 70));
        jbExecutarPasso.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbExecutarPasso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExecutarPassoActionPerformed(evt);
            }
        });
        jToolBar1.add(jbExecutarPasso);

        jLabel11.setText(" ");
        jToolBar1.add(jLabel11);

        jbParar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/poweroff_icon.png"))); // NOI18N
        jbParar.setText("Encerrar");
        jbParar.setToolTipText("Parar simulação");
        jbParar.setFocusable(false);
        jbParar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbParar.setMaximumSize(new java.awt.Dimension(100, 70));
        jbParar.setMinimumSize(new java.awt.Dimension(100, 70));
        jbParar.setPreferredSize(new java.awt.Dimension(100, 70));
        jbParar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPararActionPerformed(evt);
            }
        });
        jToolBar1.add(jbParar);

        jLabel6.setText(" ");
        jToolBar1.add(jLabel6);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 102));
        jLabel4.setText("PASSOS:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 102));
        jLabel3.setText("ESTADO ATUAL:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 102));
        jLabel7.setText("RESULTADO:");

        jtfNumPassos.setText(" ");

        jtfEstadoAtual.setText(" ");

        jtfResultado.setText(" ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addComponent(jtfNumPassos, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel3)
                .addGap(12, 12, 12)
                .addComponent(jtfEstadoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel7)
                .addGap(12, 12, 12)
                .addComponent(jtfResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtfNumPassos)
                    .addComponent(jtfEstadoAtual)
                    .addComponent(jtfResultado))
                .addGap(25, 25, 25))
        );

        jToolBar1.add(jPanel2);

        jbCarregarPalavra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/down_icon.png"))); // NOI18N
        jbCarregarPalavra.setText(" Carregar");
        jbCarregarPalavra.setPreferredSize(new java.awt.Dimension(77, 24));
        jbCarregarPalavra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCarregarPalavraActionPerformed(evt);
            }
        });

        btgModelo.add(jrbPadrao);
        jrbPadrao.setText("Padrão");
        jrbPadrao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbPadraoActionPerformed(evt);
            }
        });

        btgModelo.add(jrbMultifita);
        jrbMultifita.setSelected(true);
        jrbMultifita.setText("Multifitas");
        jrbMultifita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbMultifitaActionPerformed(evt);
            }
        });

        jLabel9.setText("Modelo:");

        jtfSobre.setForeground(java.awt.Color.blue);
        jtfSobre.setText("Sobre o simulador");
        jtfSobre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtfSobre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jtfSobreMouseReleased(evt);
            }
        });

        jLabel14.setText("Opções:");

        btgOpcoesFita.add(jrbMoverFita);
        jrbMoverFita.setText("Mover a fita");
        jrbMoverFita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbMoverFitaActionPerformed(evt);
            }
        });

        btgOpcoesFita.add(jrbMoverCursor);
        jrbMoverCursor.setSelected(true);
        jrbMoverCursor.setText("Mover o cursor");
        jrbMoverCursor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbMoverCursorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1310, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jtfPalavra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jbCarregarPalavra, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(12, 12, 12)
                        .addComponent(jrbPadrao)
                        .addGap(18, 18, 18)
                        .addComponent(jrbMultifita)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jrbMoverCursor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jrbMoverFita)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtfSobre)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfPalavra, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbCarregarPalavra, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbPadrao)
                    .addComponent(jrbMultifita)
                    .addComponent(jLabel9)
                    .addComponent(jtfSobre)
                    .addComponent(jLabel14)
                    .addComponent(jrbMoverFita)
                    .addComponent(jrbMoverCursor))
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel6);

        jtpSimulador.addTab("Máquina de Turing", jSplitPane1);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setMaximumSize(new java.awt.Dimension(202, 80));
        jToolBar2.setMinimumSize(new java.awt.Dimension(202, 80));
        jToolBar2.setPreferredSize(new java.awt.Dimension(100, 80));

        jbCompilar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/compile_icon.png"))); // NOI18N
        jbCompilar.setText("Compilar");
        jbCompilar.setFocusable(false);
        jbCompilar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCompilar.setMaximumSize(new java.awt.Dimension(100, 70));
        jbCompilar.setMinimumSize(new java.awt.Dimension(100, 70));
        jbCompilar.setPreferredSize(new java.awt.Dimension(100, 70));
        jbCompilar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCompilarActionPerformed(evt);
            }
        });
        jToolBar2.add(jbCompilar);
        jToolBar2.add(jSeparator2);

        jbAbrirArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/open_file_icon.png"))); // NOI18N
        jbAbrirArquivo.setText("Abrir");
        jbAbrirArquivo.setToolTipText("Abrir arquivo (CTRL + O)");
        jbAbrirArquivo.setFocusable(false);
        jbAbrirArquivo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAbrirArquivo.setMaximumSize(new java.awt.Dimension(100, 70));
        jbAbrirArquivo.setMinimumSize(new java.awt.Dimension(100, 70));
        jbAbrirArquivo.setPreferredSize(new java.awt.Dimension(90, 70));
        jbAbrirArquivo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbAbrirArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAbrirArquivoActionPerformed(evt);
            }
        });
        jToolBar2.add(jbAbrirArquivo);

        jbSalvarArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/save_file_icon_2.png"))); // NOI18N
        jbSalvarArquivo.setText("Salvar");
        jbSalvarArquivo.setToolTipText("Salvar arquivo (CTRL + S)");
        jbSalvarArquivo.setFocusable(false);
        jbSalvarArquivo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbSalvarArquivo.setMaximumSize(new java.awt.Dimension(100, 70));
        jbSalvarArquivo.setMinimumSize(new java.awt.Dimension(100, 70));
        jbSalvarArquivo.setPreferredSize(new java.awt.Dimension(90, 70));
        jbSalvarArquivo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbSalvarArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalvarArquivoActionPerformed(evt);
            }
        });
        jToolBar2.add(jbSalvarArquivo);

        jbFecharArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/close_file_icon.png"))); // NOI18N
        jbFecharArquivo.setText("Fechar");
        jbFecharArquivo.setFocusable(false);
        jbFecharArquivo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbFecharArquivo.setMaximumSize(new java.awt.Dimension(100, 70));
        jbFecharArquivo.setMinimumSize(new java.awt.Dimension(100, 70));
        jbFecharArquivo.setPreferredSize(new java.awt.Dimension(100, 70));
        jbFecharArquivo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbFecharArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFecharArquivoActionPerformed(evt);
            }
        });
        jToolBar2.add(jbFecharArquivo);
        jToolBar2.add(jSeparator1);

        jbDesfazer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/undo_icon.png"))); // NOI18N
        jbDesfazer.setText("Desfazer");
        jbDesfazer.setFocusable(false);
        jbDesfazer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbDesfazer.setMaximumSize(new java.awt.Dimension(100, 70));
        jbDesfazer.setMinimumSize(new java.awt.Dimension(100, 70));
        jbDesfazer.setPreferredSize(new java.awt.Dimension(100, 70));
        jbDesfazer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbDesfazer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDesfazerActionPerformed(evt);
            }
        });
        jToolBar2.add(jbDesfazer);

        jbRefazer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/redo_icon.png"))); // NOI18N
        jbRefazer.setText("Refazer");
        jbRefazer.setFocusable(false);
        jbRefazer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbRefazer.setMaximumSize(new java.awt.Dimension(100, 70));
        jbRefazer.setMinimumSize(new java.awt.Dimension(100, 70));
        jbRefazer.setPreferredSize(new java.awt.Dimension(100, 70));
        jbRefazer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbRefazer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRefazerActionPerformed(evt);
            }
        });
        jToolBar2.add(jbRefazer);
        jToolBar2.add(jSeparator3);

        jbCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/copy_icon.png"))); // NOI18N
        jbCopiar.setText("Copiar");
        jbCopiar.setFocusable(false);
        jbCopiar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCopiar.setMaximumSize(new java.awt.Dimension(100, 70));
        jbCopiar.setMinimumSize(new java.awt.Dimension(100, 70));
        jbCopiar.setPreferredSize(new java.awt.Dimension(100, 70));
        jbCopiar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCopiarActionPerformed(evt);
            }
        });
        jToolBar2.add(jbCopiar);

        jbColar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turing/icones/paste_icon.png"))); // NOI18N
        jbColar.setText("Colar");
        jbColar.setFocusable(false);
        jbColar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbColar.setMaximumSize(new java.awt.Dimension(100, 70));
        jbColar.setMinimumSize(new java.awt.Dimension(100, 70));
        jbColar.setPreferredSize(new java.awt.Dimension(100, 70));
        jbColar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbColar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbColarActionPerformed(evt);
            }
        });
        jToolBar2.add(jbColar);

        jSplitPane2.setDividerLocation(500);
        jSplitPane2.setDividerSize(4);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jtaCompilacao.setEditable(false);
        jtaCompilacao.setColumns(20);
        jtaCompilacao.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        jtaCompilacao.setRows(5);
        jScrollPane2.setViewportView(jtaCompilacao);

        jSplitPane2.setRightComponent(jScrollPane2);

        jspEditor.setColumnHeaderView(null);

        jtaEditor.setColumns(20);
        jtaEditor.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 12)); // NOI18N
        jtaEditor.setRows(5);
        jtaEditor.setTabSize(4);
        jtaEditor.setComponentPopupMenu(jppEditor);
        jtaEditor.setMargin(new java.awt.Insets(2, 2, 200, 2));
        jtaEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtaEditorKeyReleased(evt);
            }
        });
        jspEditor.setViewportView(jtaEditor);

        jSplitPane2.setLeftComponent(jspEditor);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 1336, Short.MAX_VALUE)
            .addComponent(jSplitPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE))
        );

        jtpSimulador.addTab("Arquivo", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtpSimulador)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtpSimulador)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbSetEstadoFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSetEstadoFinalActionPerformed
        definirEstadoSelecionadoComoInicial();
    }//GEN-LAST:event_jbSetEstadoFinalActionPerformed

    private void jbEditarEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEditarEstadoActionPerformed
        editarEstadoSelecionado();
    }//GEN-LAST:event_jbEditarEstadoActionPerformed

    private void jbRemoverTransicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoverTransicaoActionPerformed
        removerTransicaoSelecionada();
    }//GEN-LAST:event_jbRemoverTransicaoActionPerformed

    private void jbInserirSimboloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbInserirSimboloActionPerformed
        inserirSimbolos();
    }//GEN-LAST:event_jbInserirSimboloActionPerformed

    private void jbRemoverSimboloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoverSimboloActionPerformed
        removerSimboloSelecionado();
    }//GEN-LAST:event_jbRemoverSimboloActionPerformed

    private void jbEditarSimboloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEditarSimboloActionPerformed
        editarSimboloSelecionado();
    }//GEN-LAST:event_jbEditarSimboloActionPerformed

    private void jbInserirEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbInserirEstadoActionPerformed
        inserirEstados();
    }//GEN-LAST:event_jbInserirEstadoActionPerformed

    private void jbRemoverEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoverEstadoActionPerformed
        removerEstadoSelecionado();
    }//GEN-LAST:event_jbRemoverEstadoActionPerformed

    private void jbSetEstadoInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSetEstadoInicialActionPerformed
        definirEstadoSelecionadoComoTerminal();
    }//GEN-LAST:event_jbSetEstadoInicialActionPerformed

    private void jbAlfabetoAuxiliarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAlfabetoAuxiliarActionPerformed
        definirSimboloSelecionadoComoAuxiliar();
    }//GEN-LAST:event_jbAlfabetoAuxiliarActionPerformed

    private void jbMoverTransicaoCimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbMoverTransicaoCimaActionPerformed
       moverTransicaoSelecionadaParaCima();
    }//GEN-LAST:event_jbMoverTransicaoCimaActionPerformed

    private void jbMoverTransicaoBaixoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbMoverTransicaoBaixoActionPerformed
        moverTransicaoSelecionadaParaBaixo();
    }//GEN-LAST:event_jbMoverTransicaoBaixoActionPerformed

    private void jbInserirTransicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbInserirTransicaoActionPerformed
        inserirTransicao();
    }//GEN-LAST:event_jbInserirTransicaoActionPerformed

    private void jbAlfabetoAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAlfabetoAjudaActionPerformed
        exibirAjuda("Alfabeto da Fita", "/turing/ajuda/AlfabetoFita.html");
    }//GEN-LAST:event_jbAlfabetoAjudaActionPerformed

    private void jbEstadosAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEstadosAjudaActionPerformed
        exibirAjuda("Conjunto dos Estados", "/turing/ajuda/ConjuntoEstados.html");
    }//GEN-LAST:event_jbEstadosAjudaActionPerformed

    private void jbFuncaoTransicaoAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFuncaoTransicaoAjudaActionPerformed
        exibirAjuda("Função de Transição", "/turing/ajuda/FuncaoTransicao.html");
    }//GEN-LAST:event_jbFuncaoTransicaoAjudaActionPerformed

    private void jbSalvarArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarArquivoActionPerformed
        salvarArquivo();
    }//GEN-LAST:event_jbSalvarArquivoActionPerformed

    private void jbAbrirArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAbrirArquivoActionPerformed
        abrirArquivo();
    }//GEN-LAST:event_jbAbrirArquivoActionPerformed

    private void jbFecharArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFecharArquivoActionPerformed
        fecharArquivo();
    }//GEN-LAST:event_jbFecharArquivoActionPerformed

    private void jbCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCompilarActionPerformed
        compilarArquivo();
    }//GEN-LAST:event_jbCompilarActionPerformed

    private void jbDesfazerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDesfazerActionPerformed
        desfazerAlteracaoTexto();
    }//GEN-LAST:event_jbDesfazerActionPerformed

    private void jbRefazerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRefazerActionPerformed
        refazerAlteracaoTexto();
    }//GEN-LAST:event_jbRefazerActionPerformed

    private void jbCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCopiarActionPerformed
        copiarTextoParaAreaTransferencia();
    }//GEN-LAST:event_jbCopiarActionPerformed

    private void jbColarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbColarActionPerformed
        colarTextoDaAreaTranferencia();
    }//GEN-LAST:event_jbColarActionPerformed

    private void jmiCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCopiarActionPerformed
        copiarTextoParaAreaTransferencia();
    }//GEN-LAST:event_jmiCopiarActionPerformed

    private void jmiColarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiColarActionPerformed
        colarTextoDaAreaTranferencia();
    }//GEN-LAST:event_jmiColarActionPerformed

    private void jtaEditorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtaEditorKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (jppAutocompletar.isVisible()) {
                jppAutocompletar.setVisible(false);
            }
        }
        verificarMudancasTexto();
    }//GEN-LAST:event_jtaEditorKeyReleased

    private void jlAlfabetoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlAlfabetoMouseClicked
        if (evt.getClickCount() == 2) {
            definirSimboloSelecionadoComoAuxiliar();
        }
    }//GEN-LAST:event_jlAlfabetoMouseClicked

    private void jlEstadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlEstadosMouseClicked
        if (evt.getClickCount() == 2) {
            definirEstadoSelecionadoComoTerminal();
        }
    }//GEN-LAST:event_jlEstadosMouseClicked

    private void jbExecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExecutarActionPerformed
       iniciarSimulacaoAutomatica();
    }//GEN-LAST:event_jbExecutarActionPerformed

    private void jbExecutarPassoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExecutarPassoActionPerformed
        executarPassoSimulacao();
    }//GEN-LAST:event_jbExecutarPassoActionPerformed

    private void jbPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPararActionPerformed
        encerrarSimulacao();
    }//GEN-LAST:event_jbPararActionPerformed

    private void jbCarregarPalavraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCarregarPalavraActionPerformed
        carregarPalavraEntrada();
    }//GEN-LAST:event_jbCarregarPalavraActionPerformed

    private void jbPausarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPausarActionPerformed
        pausarSimulacaoAutomatica();
    }//GEN-LAST:event_jbPausarActionPerformed

    private void jtfPalavraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfPalavraKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            carregarPalavraEntrada();
        }
    }//GEN-LAST:event_jtfPalavraKeyReleased

    private void jbVelocidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbVelocidadeActionPerformed
        configurarVelocidadeSimulacaoAutomatica();
    }//GEN-LAST:event_jbVelocidadeActionPerformed

    private void jbReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbReiniciarActionPerformed
        reiniciarSimulacao();
    }//GEN-LAST:event_jbReiniciarActionPerformed

    private void jspNumeroFitasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspNumeroFitasStateChanged
        configurarFitasModeloSelecionado();
    }//GEN-LAST:event_jspNumeroFitasStateChanged

    private void jrbMultifitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbMultifitaActionPerformed
        configurarFitasModeloSelecionado();
    }//GEN-LAST:event_jrbMultifitaActionPerformed

    private void jrbPadraoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbPadraoActionPerformed
        configurarFitasModeloSelecionado();
    }//GEN-LAST:event_jrbPadraoActionPerformed

    private void jtfSobreMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtfSobreMouseReleased
        exibirTelaSobre();
    }//GEN-LAST:event_jtfSobreMouseReleased

    private void jtpSimuladorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jtpSimuladorStateChanged
        definirTituloTela();
    }//GEN-LAST:event_jtpSimuladorStateChanged

    private void jmiDetalhesFitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiDetalhesFitasActionPerformed
        exibirDetalhesFitas();
    }//GEN-LAST:event_jmiDetalhesFitasActionPerformed

    private void jlAutocompletarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jlAutocompletarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            inserirItemSelecionadoMenuTransicao();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            jppAutocompletar.setVisible(false);
            jtaEditor.requestFocus();
        }
    }//GEN-LAST:event_jlAutocompletarKeyReleased

    private void jlAutocompletarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlAutocompletarMouseClicked
        if (evt.getClickCount() == 2) {
            inserirItemSelecionadoMenuTransicao();
        }
    }//GEN-LAST:event_jlAutocompletarMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        fecharTela();
    }//GEN-LAST:event_formWindowClosing

    private void jrbMoverCursorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbMoverCursorActionPerformed
        configurarFitasModeloSelecionado();
    }//GEN-LAST:event_jrbMoverCursorActionPerformed

    private void jrbMoverFitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbMoverFitaActionPerformed
        configurarFitasModeloSelecionado();
    }//GEN-LAST:event_jrbMoverFitaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgModelo;
    private javax.swing.ButtonGroup btgOpcoesFita;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JButton jbAbrirArquivo;
    private javax.swing.JButton jbAlfabetoAjuda;
    private javax.swing.JButton jbAlfabetoAuxiliar;
    private javax.swing.JButton jbCarregarPalavra;
    private javax.swing.JButton jbColar;
    private javax.swing.JButton jbCompilar;
    private javax.swing.JButton jbCopiar;
    private javax.swing.JButton jbDesfazer;
    private javax.swing.JButton jbEditarEstado;
    private javax.swing.JButton jbEditarSimbolo;
    private javax.swing.JButton jbEstadosAjuda;
    private javax.swing.JButton jbExecutar;
    private javax.swing.JButton jbExecutarPasso;
    private javax.swing.JButton jbFecharArquivo;
    private javax.swing.JButton jbFuncaoTransicaoAjuda;
    private javax.swing.JButton jbInserirEstado;
    private javax.swing.JButton jbInserirSimbolo;
    private javax.swing.JButton jbInserirTransicao;
    private javax.swing.JButton jbMoverTransicaoBaixo;
    private javax.swing.JButton jbMoverTransicaoCima;
    private javax.swing.JButton jbParar;
    private javax.swing.JButton jbPausar;
    private javax.swing.JButton jbRefazer;
    private javax.swing.JButton jbReiniciar;
    private javax.swing.JButton jbRemoverEstado;
    private javax.swing.JButton jbRemoverSimbolo;
    private javax.swing.JButton jbRemoverTransicao;
    private javax.swing.JButton jbSalvarArquivo;
    private javax.swing.JButton jbSetEstadoFinal;
    private javax.swing.JButton jbSetEstadoInicial;
    private javax.swing.JButton jbVelocidade;
    private javax.swing.JList<String> jlAlfabeto;
    private javax.swing.JList<String> jlAutocompletar;
    private javax.swing.JList<String> jlEstados;
    private javax.swing.JList<String> jlTransicoes;
    private javax.swing.JMenuItem jmiColar;
    private javax.swing.JMenuItem jmiCopiar;
    private javax.swing.JMenuItem jmiDetalhesFitas;
    private javax.swing.JPanel jpAutocompletar;
    private javax.swing.JPopupMenu jppAutocompletar;
    private javax.swing.JPopupMenu jppEditor;
    private javax.swing.JPopupMenu jppFitas;
    private javax.swing.JRadioButton jrbMoverCursor;
    private javax.swing.JRadioButton jrbMoverFita;
    private javax.swing.JRadioButton jrbMultifita;
    private javax.swing.JRadioButton jrbPadrao;
    private javax.swing.JScrollPane jspEditor;
    private javax.swing.JSpinner jspNumeroFitas;
    private javax.swing.JTable jtFitas;
    private javax.swing.JTextArea jtaCompilacao;
    private javax.swing.JTextArea jtaEditor;
    private javax.swing.JLabel jtfEstadoAtual;
    private javax.swing.JLabel jtfNumPassos;
    private javax.swing.JTextField jtfPalavra;
    private javax.swing.JLabel jtfResultado;
    private javax.swing.JLabel jtfSobre;
    private javax.swing.JTabbedPane jtpSimulador;
    // End of variables declaration//GEN-END:variables


}
