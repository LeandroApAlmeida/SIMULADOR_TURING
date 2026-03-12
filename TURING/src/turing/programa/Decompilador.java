package turing.programa;

import turing.modelo.MaquinaTuring;
import turing.modelo.Alfabeto;
import turing.modelo.AlfabetoFita;
import turing.modelo.ConjuntoEstados;
import turing.modelo.Estado;
import turing.modelo.FuncaoTransicao;
import turing.modelo.Simbolo;
import turing.modelo.Transicao;
import static turing.programa.Tokens.CABECALHO_DESCRICAO;
import static turing.programa.Tokens.CABECALHO_PARAMETROS;
import static turing.programa.Tokens.CABECALHO_PROGRAMA;
import static turing.programa.Tokens.CAMPO_ALFABETO_AUXILIAR;
import static turing.programa.Tokens.CAMPO_ALFABETO_ENTRADA;
import static turing.programa.Tokens.CAMPO_COMENTARIO;
import static turing.programa.Tokens.CAMPO_ESTADOS;
import static turing.programa.Tokens.CAMPO_ESTADOS_TERMINAIS;
import static turing.programa.Tokens.CAMPO_ESTADO_INICIAL;
import static turing.programa.Tokens.CAMPO_NOME;
import static turing.programa.Tokens.CAMPO_NUMERO_FITAS;
import static turing.programa.Tokens.SIMBOLO_ESPACO;
import static turing.programa.Tokens.SIMBOLO_VIRGULA;

/**
 * Decompilador, que exerce o papel inverso do compilador. Enquanto o compilador
 * gera os parâmetros para a Máquina de Turing a partir do código do programa, o
 * decompilador gera o código do programa a partir dos parâmetros definidos na 
 * interface gráfica de usuário do simulador.
 * 
 * <br><br>
 * 
 * Como o simulador foi projetado para ter também a opção de configurar os parâmetros
 * da máquina de Turing através da interface gráfica de usuário, foi necessário
 * criar esta classe para obter estes parâmetros e injetar os dados no mesmo
 * código que está aberto no editor. O código aberto no editor e os parâmetros
 * na interface gráfica de usuário não podem divergir, logo, só é permitido
 * inserir dados pelos campos da interface se o código aberto no editor estiver
 * compilado.
 * 
 * <br><br>
 * 
 * No caso do código compilado, toda vez que se alterar os parâmetros para a
 * máquina de Turing pela interface gráfica, é necessário atualizar os campos 
 * na respectiva seção do arquivo aberto. Esta classe não vai mudar o template 
 * que o usuário criou, mantendo comentários, alinhamento dos campos, etc. A 
 * única coisa que ela irá fazer, será injetar nos campos respectivos as mudanças
 * realizadas.
 * 
 * @author Leandro Ap. de Almeida.
 * 
 * @since 1.0
 */
public class Decompilador {
    
    
    /**
     * Verificar se a linha inicia com o rótulo do campo pesquisado.
     * 
     * @param linha linha
     * 
     * @param campo campo pesquisado.
     * 
     * @param cabecalho identifica se é um campo de cabeçalho ou não.
     * 
     * @return Se true, a linha inicia com o rótulo. Se false, a linha não
     * inicia.
     */
    private boolean contemCampo(String linha, String campo) {
        
        boolean campoCabecalho = campo.equals(CABECALHO_DESCRICAO) ||
        campo.equals(CABECALHO_PARAMETROS) || campo.equals(CABECALHO_PROGRAMA);
        
        // Verifica se o campo a ser verificado é o comentário.
        
        if (!campo.equals(CAMPO_COMENTARIO)) {
            
            // Em caso de o campo não ser o comentário...
        
            // Obtém o rótulo do campo à esquerda de "=", caso não seja um cabeçalho
            // de seção, ou o próprio cabeçalho.
        
            String campoPesquisado = (
                !campoCabecalho ? 
                campo.substring(0, campo.indexOf("=")) :
                campo
            );
            
            // Verifica se a linha contém o campo pesquisado.
            
            if (linha.contains(campoPesquisado)) {
                
                // Remove espaços, tabulações e comentários.
            
                String nova = normalizar(linha);
                
                if (!nova.startsWith(CAMPO_COMENTARIO)) {
                
                    // Se a linha não é um comentário, verifica se ela inicia
                    // com o rótulo do campo pesquisado ou, no caso de cabeçalho
                    // de seção, se a linha é o rótulo.
                    
                    if (!campoCabecalho) {
                        return nova.startsWith(campo);
                    } else {
                        return nova.equals(campo);
                    }
                    
                } else {
                    
                    // Se a linha é um comentário, a verificação retorna false,
                    // pois o campo pesquisado fará parte deste.
                    
                    return false;
                
                }
                
            } else {
                
                // Se a linha não contém o campo, retorna false.
                
                return false;
                
            }
            
        } else {
            
            // Em caso do campo pesquisado ser um comentário, retorna true se a 
            // linha começa com "//".
        
            String nova = normalizar(linha);
            
            return nova.startsWith(CAMPO_COMENTARIO);
        
        }
        
    }
    
    
    /**
     * Eliminar todos os espaços e tabulações em uma linha.
     * 
     * @param linha linha.
     * 
     * @return texto da linha, sem espaços e tabulações.
     */
    private String normalizar(String linha) {
        return linha.trim().replace(" ", "").replace("\u0009", "");
    }
    
    
    /**
     * Verificar se um caractere é espaço ou tabulação.
     * 
     * @param caractere caractere
     * 
     * @return Se true, o caractere é espaço ou tabulação. Se false, o caractere
     * não é espaço ou tabulação.
     */
    private boolean isCaractereEspaco(char caractere) {
        return caractere == ' ' || caractere == '\u0009';
    }
    
    
    /**
     * Gerar o código do programa para a máquina de Turing com base nas
     * configurações da interface gráfica de usuário.
     * 
     * <br><br>
     * 
     * O código do programa tem o seguinte formato:
     * 
     * <br><br> 
     * 
     * <pre>
     * 
     * // Exemplo de programa que verifica se o número em binário é divisível
     * // por 3. Exemplo: 101 (não), 1001 (sim), 1100 (sim).
     * 
     * [Descricao]
     * 
     *     Nome =  Número Binário divisível por 3
     * 
     * [Parametros]
     * 
     *     AlfabetoEntrada = {0, 1}
     *     AlfabetoAuxiliar = {}
     *     Estados = {q0, q1, q2, q3}
     *     EstadoInicial = q0
     *     EstadosTerminais = {q3}
     *     NumeroFitas = 1
     * 
     * [Programa]
     * 
     *     q0, 0 = q0, 0, D
     *     q0, 1 = q1, 1, D
     *     q1, 0 = q2, 0, D
     *     q1, 1 = q0, 1, D
     *     q2, 0 = q1, 0, D
     *     q2, 1 = q2, 1, D
     *     q0, _ = q3, _, P
     * 
     * </pre>
     * 
     * Onde:
     * 
     * <br><br>
     * 
     * <b>//</b>: Toda linha que inicia com // é uma linha de comentário. O comentário
     * pode estar numa linha exclusiva, a exemplo do código acima, ou pode estar
     * adiante de um campo ou linha de instrução da função de transição.
     *
     * <br><br>
     * 
     * <b>[Descricao]</b>: Cabeçalho da seção de Descricao. A seção Descricao
     * serve para identificar o programa a ser executado. Obrigatóriamente, deve
     * ser a primeira seção do código do programa. Ela tem os seguintes campos: 
     * 
     * <br><br>
     * 
     * 
     * <ul>
     * 
     * <li>&nbsp;<b>Nome = </b>: Nome do programa. No exemplo acima: Número 
     * Binário divisível por 3</li>
     * 
     * </ul>
     * 
     * <br>
     * 
     * <b>[Parametros]</b>: Cabeçalho da seção Parâmetros. Tem as informações 
     * necessárias para a construção da máquina de Turing. A seção Parametros 
     * deve, obrigatóriamente, ser a segunda seção do código do programa. Ela
     * tem os seguintes campos:
     * 
     * <br><br>
     * 
     * <ul>
     * 
     * <li>&nbsp;<b>AlfabetoEntrada = </b>: Alfabeto de entrada. Este campo usa
     * a notação de conjunto { ... }. Cada símbolo do alfabeto deve ser separado
     * por vírgula em caso de mais de um símbolo. No exemplo acima: { 0, 1 }.</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>AlfabetoAuxiliar = </b>: Alfabeto auxiliar. Este campo usa a 
     * notação de conjunto { ... }. Cada símbolo do alfabeto deve ser separado 
     * por vírgula em caso de mais de um símbolo. No exemplo acima: { }.</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>Estados = </b>: Conjunto dos Estados. Este campo usa a notação
     * de conjunto { ... }. Cada estado do conjunto deve ser separado por vírgula
     * em caso de mais de um estado. No exemplo acima: { q0, q1, q2, q3 }.</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>EstadoInicial = </b>: Estado inicial. Este estado, obrigatóriamente,
     * deve pertencer ao conjunto dos estados. No exemplo acima: q0</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>EstadosTerminais = </b>: Conjunto dos Estados terminais. Este 
     * campo usa a notação de conjunto { ... }. Cada estado do conjunto deve ser 
     * separado por vírgula em caso de mais de um estado. Cada um dos estados 
     * deste conjunto deve, obrigatóriamente, pertencer ao conjunto dos estados.
     * No exemplo acima: { q3 }</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>NumeroFitas = </b>: Número de fitas. No exemplo acima: 1</li>
     * 
     * </ul>
     * 
     * <br>
     * 
     * <b>[Programa]</b>: Cabeçalho da seção Programa. A seção Programa contém as
     * instruções da função de transição, o equivalente ao programa da máquina de 
     * Turing. A seção Programa deve, obrigatóriamente, ser a terceira seção do
     * código do programa. Ela têm os seguintes campos em cada linha de instrução:
     * 
     * <br><br>
     * 
     * <ul>
     * 
     * <li><b>Estado atual:</b> estado atual da transição. No exemplo acima, na
     * primeira linha, o estado atual é q0 </li> 
     * 
     * <br>
     * 
     * <li><b>Símbolo lido 1, símbolo lido 2, ..., símbolo lido n: </b> símbolos
     * lidos das fitas de 1 a n. No exemplo acima, na primeira linha, o símbolo lido
     * é 0 </li>
     * 
     * <br>
     * 
     * <li><b>Novo estado:</b> novo estado da transição. No exemplo acima, na 
     * primeira linha, o novo estado é q0 </li> 
     * 
     * <br>
     * 
     * <li><b>Símbolo escrito 1, símbolo escrito 2, ..., símbolo escrito n: </b> 
     * símbolos escritos nas fitas de 1 a n. No exemplo acima, na primeira linha,
     * o símbolo escrito é 0 </li>
     * 
     * <br>
     * 
     * <li><b>Direção do movimento: </b> direção do movimento da cabeça de 
     * leitura/escrita. No exemplo acima, na primeira linha, a direção é D (Direita) </li>
     * 
     * </ul>
     * 
     * <br>
     * 
     * Será passado para este método o texto atualmente aberto no editor. Caso
     * o texto esteja vazio, será criado um template padrão. Este é o caso quando
     * o programa é iniciado, por exemplo.
     * 
     * <br><br>
     * 
     * Caso o texto não esteja vazio, significa que o usuário já está com ele
     * aberto no editor, e provavelmente já realizou edições e comentários. Neste
     * caso, nenhuma alteração deverá ser feita no template do usuário. O que
     * será feito será a injeção do código atualizado no respectivo campo. 
     * 
     * <br><br>
     * 
     * Por exemplo. Suponha que no texto aberto no editor tenha o seguinte
     * alfabeto de entrada:
     * 
     * <br><br>
     * 
     * &nbsp;&nbsp;AlfabetoEntrada = {a, b, c}
     * 
     * <br><br>
     * 
     * Agora o código deve ser atualizado, pois o usuário acaba de adicionar à
     * lista do alfabeto da fita o símbolo de entrada 'e'. O algoritmo irá
     * localizar aonde está o campo AlfabetoEntrada no código do editor, e fará
     * a substituição apenas do valor que está entre os parênteses. Ao final, o
     * texto original é preservado, apenas o valor do campo AlfabetoEntrada muda
     * para:
     * 
     * <br><br>
     * 
     * &nbsp;&nbsp;AlfabetoEntrada = {a, b, c, e}
     * 
     * <br><br>
     * 
     * @param maquinaTuring configurações da máquina de Turing.
     * 
     * @param programa programa atualmente em edição.
     * 
     * @return Códido do programa.
     * 
     * @throws Exception erro ao converter para o código do programa.
     */
    public String executar(MaquinaTuring maquinaTuring, String programa) throws Exception {

        StringBuilder sb = new StringBuilder();
        
        if (programa == null || programa.isEmpty() || programa.isBlank()) {
            
            // Cria um template, caso não tenha um arquivo aberto no editor.
            
            sb.append("// Programa para o simulador de máquina de Turing, ver. 1.0.");

            sb.append("\n\n\n");

            sb.append(CABECALHO_DESCRICAO);

            sb.append("\n\n");

            sb.append("\u0009");        
            sb.append(CAMPO_NOME.replace("=", " = Máquina de Turing"));

            sb.append("\n\n\n");

            sb.append(CABECALHO_PARAMETROS);

            sb.append("\n\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ALFABETO_ENTRADA.replace("=", " = {}"));

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ALFABETO_AUXILIAR.replace("=", " = {}"));

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ESTADOS.replace("=", " = {}"));

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ESTADO_INICIAL.replace("=", " = "));

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ESTADOS_TERMINAIS.replace("=", " = {}"));

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_NUMERO_FITAS.replace("=", " = "));
            sb.append(String.valueOf(1));

            sb.append("\n\n\n");

            sb.append(CABECALHO_PROGRAMA);
        
        } else {
            
            // Injeta trechos no código existente. Vai alterar apenas o valor
            // do campo, mantendo todo o restante do texto sem qualquer modificação.
            
            AlfabetoFita alfabetoFita = maquinaTuring.getAlfabetoFita();
            ConjuntoEstados conjuntoEstados = maquinaTuring.getConjuntoEstados();
            FuncaoTransicao funcaoTransicao = maquinaTuring.getFuncaoTransicao();
            int numeroFitas = maquinaTuring.getNumeroFitas();

            String[] linhas = programa.split("\n");

            int indiceCabecalhoPrograma = -1;
            int indiceCampoAlfabetoEntrada = -1;
            int indiceCampoAlfabetoAuxiliar = -1;
            int indiceCampoEstados = -1;
            int indiceCampoEstadoInicial = -1;
            int indiceCampoEstadosTerminais = -1;
            int indiceCampoNumeroFitas = -1;
        
            for (int indice = 0 ; indice < linhas.length; indice++) {
                
                String linha = linhas[indice];
                
                if (!linha.startsWith(CAMPO_COMENTARIO)) {
                    if (contemCampo(linha, CABECALHO_PROGRAMA)) {
                        indiceCabecalhoPrograma = indice;
                    } else if (contemCampo(linha, CAMPO_ALFABETO_ENTRADA)) {
                        indiceCampoAlfabetoEntrada = indice;
                    } else if (contemCampo(linha, CAMPO_ALFABETO_AUXILIAR)) {
                        indiceCampoAlfabetoAuxiliar = indice;
                    } else if (contemCampo(linha, CAMPO_ESTADOS)) {
                        indiceCampoEstados = indice;
                    } else if (contemCampo(linha, CAMPO_ESTADO_INICIAL)) {
                        indiceCampoEstadoInicial = indice;
                    } else if (contemCampo(linha, CAMPO_ESTADOS_TERMINAIS)) {
                        indiceCampoEstadosTerminais = indice;
                    } else if (contemCampo(linha, CAMPO_NUMERO_FITAS)) {
                        indiceCampoNumeroFitas = indice;
                    }
                }
                
            }
            
            // Altera o valor do campo [Parametros]/AlfabetoEntrada.
            
            StringBuilder sb2 = new StringBuilder();
            
            sb2.append("{");
            
            int cont = 0;
            Alfabeto alfabetoEntrada = alfabetoFita.getAlfabetoEntrada();
            
            for (int i = 0; i < alfabetoEntrada.getComprimento(); i++) {
            
                Simbolo simbolo = alfabetoEntrada.getSimbolo(i);
                
                if (cont > 0) {
                    sb2.append(", ");
                }
                
                switch (simbolo.getCaracter()) {
                    case ' ' -> sb2.append(SIMBOLO_ESPACO);
                    case ',' -> sb2.append(SIMBOLO_VIRGULA);
                    default -> sb2.append(simbolo.getCaracter());
                }
                
                cont++;
            
            }
            
            sb2.append("}");
            
            String linha = linhas[indiceCampoAlfabetoEntrada];
            
            if (linha.contains(CAMPO_COMENTARIO)) {                
                String comentario = linha.substring(linha.indexOf("}") + 1, linha.length());                
                sb2.append(comentario);                
            }
            
            int idx;

            for (idx = linha.indexOf("=") + 1 ; idx < linha.length(); idx++) {
                if (!isCaractereEspaco(linha.charAt(idx))) {
                    break;
                }
            }

            linhas[indiceCampoAlfabetoEntrada] = linha.substring(0, idx) + sb2.toString();
            
            // Altera o valor do campo [Parametros]/AlfabetoAuxiliar.
            
            StringBuilder sb3 = new StringBuilder();
            
            sb3.append("{");
            
            cont = 0;
            Alfabeto alfabetoAuxiliar = alfabetoFita.getAlfabetoAuxiliar();
            
            for (int i = 0; i < alfabetoAuxiliar.getComprimento(); i++) {
                
                Simbolo simbolo = alfabetoAuxiliar.getSimbolo(i);
                
                if (cont > 0) {
                    sb3.append(", ");
                }
                
                switch (simbolo.getCaracter()) {
                    case ' ' -> sb3.append(SIMBOLO_ESPACO);
                    case ',' -> sb3.append(SIMBOLO_VIRGULA);
                    default -> sb3.append(simbolo.getCaracter());
                }
                
                cont++;
            
            }
            
            sb3.append("}");
            
            linha = linhas[indiceCampoAlfabetoAuxiliar];
            
            if (linha.contains(CAMPO_COMENTARIO)) {
                // Preserva o comentário, se existir.
                String comentario = linha.substring(linha.indexOf("}") + 1, linha.length());                
                sb3.append(comentario);                
            }
            
            for (idx = linha.indexOf("=") + 1 ; idx < linha.length(); idx++) {
                if (!isCaractereEspaco(linha.charAt(idx))) {
                    break;
                }
            }

            linhas[indiceCampoAlfabetoAuxiliar] = linha.substring(0, idx) + sb3.toString();
            
            // Altera o valor do campo [Parametros]/Estados.
            
            StringBuilder sb4 = new StringBuilder();
            
            sb4.append("{");
            
            if (conjuntoEstados.getComprimento() > 0) {
                
                sb4.append(conjuntoEstados.getEstado(0).getRotulo());
                
                for (int i = 1; i < conjuntoEstados.getComprimento(); i++) {
                    sb4.append(", ");
                    sb4.append(conjuntoEstados.getEstado(i).getRotulo());
                }
                
            }
            
            sb4.append("}");
            
            linha = linhas[indiceCampoEstados];
            
            if (linha.contains(CAMPO_COMENTARIO)) {
                // Preserva o comentário, se existir.
                String comentario = linha.substring(linha.indexOf("}") + 1, linha.length());                
                sb4.append(comentario);                
            }
            
            for (idx = linha.indexOf("=") + 1 ; idx < linha.length(); idx++) {
                if (!isCaractereEspaco(linha.charAt(idx))) {
                    break;
                }
            }

            linhas[indiceCampoEstados] = linha.substring(0, idx) + sb4.toString();
            
            // Altera o valor do campo [Parametros]/EstadoInicial.
            
            String estadoInicial = "";
            
            if (conjuntoEstados.getComprimento() > 0) {
                
                for (int i = 0; i < conjuntoEstados.getComprimento(); i++) {
                
                    Estado estado = conjuntoEstados.getEstado(i);
                    
                    if (estado.isInicial()) {
                        estadoInicial = estado.getRotulo();
                        break;
                    }
                    
                }
                
            }
            
            linha = linhas[indiceCampoEstadoInicial];
            
            for (idx = linha.indexOf("=") + 1 ; idx < linha.length(); idx++) {
                if (!isCaractereEspaco(linha.charAt(idx))) {
                    break;
                }
            }
            
            if (linha.contains(CAMPO_COMENTARIO)) {
                // Preserva o comentário, se existir.
                String comentario = linha.substring(linha.indexOf(" ", idx), linha.length());                
                estadoInicial += comentario;                
            }

            linhas[indiceCampoEstadoInicial] = linha.substring(0, idx) + estadoInicial;
            
            // Altera o valor do campo [Parametros]/EstadosTerminais.
            
            StringBuilder sb5 = new StringBuilder();
            
            sb5.append("{");
            
            if (conjuntoEstados.getComprimento() > 0) {
                
                cont = 0;
                
                for (int i = 0; i < conjuntoEstados.getComprimento(); i++) {
                
                    Estado estado = conjuntoEstados.getEstado(i);
                    
                    if (estado.isTerminal()) {
                    
                        if (cont > 0) {
                            sb5.append(", ");
                        }
                        
                        sb5.append(estado.getRotulo());
                        
                        cont++;
                    
                    }
                
                }
            
            }
    
            sb5.append("}");
            
            linha = linhas[indiceCampoEstadosTerminais];           
            
            if (linha.contains(CAMPO_COMENTARIO)) {
                // Preserva o comentário, se existir.
                String comentario = linha.substring(linha.indexOf("}") + 1, linha.length());                
                sb5.append(comentario);                
            }
            
            for (idx = linha.indexOf("=") + 1 ; idx < linha.length(); idx++) {
                if (!isCaractereEspaco(linha.charAt(idx))) {
                    break;
                }
            }

            linhas[indiceCampoEstadosTerminais] = linha.substring(0, idx) + sb5.toString();
            
            // Altera o valor do campo [Parametros]/NumeroFitas.

            linha = linhas[indiceCampoNumeroFitas];
            
            String numFitas = String.valueOf(numeroFitas); 
            
            for (idx = linha.indexOf("=") + 1 ; idx < linha.length(); idx++) {
                if (!isCaractereEspaco(linha.charAt(idx))) {
                    break;
                }
            }

            if (linha.contains(CAMPO_COMENTARIO)) {
                // Preserva o comentário, se existir.
                String comentario = linha.substring(linha.indexOf(" ", idx), linha.length());                
                numFitas += comentario;                
            }

            linhas[indiceCampoNumeroFitas] = linha.substring(0, idx) + numFitas;

            sb.append(linhas[0]);

            for (int i = 1; i <= indiceCabecalhoPrograma; i++) {
                sb.append("\n");
                sb.append(linhas[i]);
            }
            
            sb.append("\n\n");
            
            for (Transicao transicao : funcaoTransicao) {
                sb.append("\u0009");
                sb.append(transicao.toString());
                sb.append("\n");
            }
            
        }
        
        return sb.toString();
        
    }

    
}