package turing.classes;

import static turing.classes.Constantes.CABECALHO_DESCRICAO;
import static turing.classes.Constantes.CABECALHO_PARAMETROS;
import static turing.classes.Constantes.CABECALHO_PROGRAMA;
import static turing.classes.Constantes.CAMPO_ALFABETO_AUXILIAR;
import static turing.classes.Constantes.CAMPO_ALFABETO_ENTRADA;
import static turing.classes.Constantes.CAMPO_COMENTARIO;
import static turing.classes.Constantes.CAMPO_ESTADOS;
import static turing.classes.Constantes.CAMPO_ESTADOS_TERMINAIS;
import static turing.classes.Constantes.CAMPO_ESTADO_INICIAL;
import static turing.classes.Constantes.CAMPO_NOME;
import static turing.classes.Constantes.CAMPO_NUMERO_FITAS;
import static turing.classes.Constantes.SIMBOLO_ESPACO;
import static turing.classes.Constantes.SIMBOLO_VIRGULA;

/**
 * Gerador do código do programa a partir dos parâmetros definidos na interface
 * gráfica de usuário do simulador.
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
public class GeradorCodigo {
    
    
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
        if (!campo.equals(CAMPO_COMENTARIO)) {
            String comando = (!campoCabecalho ? campo.substring(0, campo.indexOf("=")) : campo);
            if (linha.contains(comando)) {
                String nova = normalizar(linha);
                if (!nova.startsWith(CAMPO_COMENTARIO)) {
                    if (!campoCabecalho) {
                        return nova.startsWith(campo);
                    } else {
                        return nova.equals(campo);
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
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
     * [Descricao]
     * 
     *     Nome = Programa para a máquina de Turing
     * 
     * [Parametros]
     * 
     *     AlfabetoEntrada = { a, b, c }
     *     AlfabetoAuxiliar = { X, Y, Z }
     *     Estados = { q0, q1, q2, q3 }
     *     EstadoInicial = q0
     *     EstadosTerminais = { q2, q3 }
     *     NumeroFitas = 1
     * 
     * [Programa]
     * 
     *     ...
     *     ...
     * </pre>
     * 
     * Onde:
     * 
     * <br><br>
     * 
     * <b>[Descricao]</b>: Cabeçalho da seção de Descricao. A seção Descricao
     * serve para identificar o programa a ser executado. Obrigatóriamente, deve
     * ser a primeira seção do código do programa. Ela tem os seguintes campos: 
     * 
     * <br><br>
     * 
     * <ul>
     * 
     * <li>&nbsp;<b>Nome = </b>: Nome do programa. No exemplo acima: Programa 
     * para a máquina de Turing</li>
     * 
     * </ul>
     * 
     * <br>
     * 
     * <b>[Parametros]</b>: Cabeçalho da seção Parametros. Tem as informações 
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
     * por vírgula em caso de mais de um símbolo. No exemplo acima: { a, b, c }.</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>AlfabetoAuxiliar = </b>: Alfabeto auxiliar. Este campo usa a 
     * notação de conjunto { ... }. Cada símbolo do alfabeto deve ser separado 
     * por vírgula em caso de mais de um símbolo. No exemplo acima: { X, Y, Z }.</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>Estados = </b>: Conjunto dos Estados. Este campo usa a notação
     * de conjunto { ... }. Cada estado do conjunto deve ser separado por vírgula
     * em caso de mais de um estado. No exemplo acima: { q0, q1, q2, q3 }.</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>EstadoInicial = </b>: Estado inical. Este estado, obrigatóriamente,
     * deve pertencer ao conjunto dos estados. No exemplo acima: q0</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>EstadosTerminais = </b>: Conjunto dos Estados terminais. Este 
     * campo usa a notação de conjunto { ... }. Cada estado do conjunto deve ser 
     * separado por vírgula em caso de mais de um estado. Cada um dos estados 
     * deste conjunto deve, obrigatóriamente, pertencer ao conjunto dos estados.
     * No exemplo acima: { q2, q3 }</li>
     * 
     * <br>
     * 
     * <li>&nbsp;<b>NumeroFitas = </b>: Número de fitas. No exemplo acima: 1</li>
     * 
     * </ul>
     * 
     * <br>
     * 
     * <b>[Programa]</b>: Cabeçalho da seção Programa. A seção Programa tem as
     * instruções para a operação da máquina de Turing, o programa ou algoritmo.
     * Toda a instrução abaixo deste cabeçalho implementa as regras para a operação
     * da máquina com os parâmetros passados na seção acima. A seção Programa deve,
     * obrigatóriamente, ser a terceira seção do código do programa.
     * 
     * <br><br>
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
     * @param confMaqTuring configurações da máquina de Turing.
     * 
     * @param codigoAtual código atualmente em edição.
     * 
     * @return Códido do programa.
     * 
     * @throws Exception erro ao converter para o código do programa.
     */
    public String executar(ConfigMaqTuring confMaqTuring, String codigoAtual) throws Exception {

        AlfabetoFita alfabetoFita = confMaqTuring.getAlfabetoFita();
        ConjuntoEstados conjuntoEstados = confMaqTuring.getConjuntoEstados();
        FuncaoTransicao funcaoTransicao = confMaqTuring.getFuncaoTransicao();
        int numeroFitas = confMaqTuring.getNumeroFitas();
        
        StringBuilder sb = new StringBuilder();
        
        if (codigoAtual == null || codigoAtual.isEmpty() || codigoAtual.isBlank()) {
            
            // Cria um template, caso não tenha um arquivo aberto no editor.
            
            sb.append("// Programa para o simulador de máquina de Turing, ver. 1.0.");


            sb.append("\n\n\n");

            sb.append(CABECALHO_DESCRICAO);

            sb.append("\n\n");

            sb.append("\u0009");        
            sb.append(CAMPO_NOME.replace("=", " = "));
            sb.append(" ");
            sb.append("Máquina de Turing");

            sb.append("\n\n\n");

            sb.append(CABECALHO_PARAMETROS);

            sb.append("\n\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ALFABETO_ENTRADA.replace("=", " = {"));

            int cont = 0;
            Alfabeto alfabetoEntrada = alfabetoFita.getAlfabetoEntrada();
            for (int i = 0; i < alfabetoEntrada.getComprimento(); i++) {
                Simbolo simbolo = alfabetoEntrada.getSimbolo(i);
                if (cont > 0) {
                    sb.append(", ");
                }
                switch (simbolo.getCaracter()) {
                    case ' ' -> sb.append(SIMBOLO_ESPACO);
                    case ',' -> sb.append(SIMBOLO_VIRGULA);
                    default -> sb.append(simbolo.getCaracter());
                }
                cont++;
            }

            sb.append("}");

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ALFABETO_AUXILIAR.replace("=", " = {"));

            cont = 0;
            Alfabeto alfabetoAuxiliar = alfabetoFita.getAlfabetoAuxiliar();
            for (int i = 0; i < alfabetoAuxiliar.getComprimento(); i++) {
                Simbolo simbolo = alfabetoAuxiliar.getSimbolo(i);
                if (cont > 0) {
                    sb.append(", ");
                }
                switch (simbolo.getCaracter()) {
                    case ' ' -> sb.append(SIMBOLO_ESPACO);
                    case ',' -> sb.append(SIMBOLO_VIRGULA);
                    default -> sb.append(simbolo.getCaracter());
                }
                cont++;
            }

            sb.append("}");

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ESTADOS.replace("=", " = {"));

            if (conjuntoEstados.getComprimento() > 0) {
                sb.append(conjuntoEstados.getEstado(0).getRotulo());
                for (int i = 1; i < conjuntoEstados.getComprimento(); i++) {
                    sb.append(", ");
                    sb.append(conjuntoEstados.getEstado(i).getRotulo());
                }
            }

            sb.append("}");

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ESTADO_INICIAL.replace("=", " = "));

            if (conjuntoEstados.getComprimento() > 0) {
                for (int i = 0; i < conjuntoEstados.getComprimento(); i++) {
                    Estado estado = conjuntoEstados.getEstado(i);
                    if (estado.isInicial()) {
                        sb.append(estado.getRotulo());
                        break;
                    }
                }
            }

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_ESTADOS_TERMINAIS.replace("=", " = {"));

            if (conjuntoEstados.getComprimento() > 0) {
                cont = 0;
                for (int i = 0; i < conjuntoEstados.getComprimento(); i++) {
                    Estado estado = conjuntoEstados.getEstado(i);
                    if (estado.isTerminal()) {
                        if (cont > 0) {
                            sb.append(", ");
                        }
                        sb.append(estado.getRotulo());
                        cont++;
                    }
                }
            }

            sb.append("}");

            sb.append("\n");

            sb.append("\u0009");        
            sb.append(CAMPO_NUMERO_FITAS.replace("=", " = "));
            sb.append(String.valueOf(numeroFitas));

            sb.append("\n\n\n");

            sb.append(CABECALHO_PROGRAMA);
        
        } else {
            
            // Injeta trechos no código existente. Vai alterar apenas o valor
            // do campo, mantendo todo o restante do texto sem qualquer modificação.

            String[] linhas = codigoAtual.split("\n");

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
            
            for (idx = linha.indexOf("=") + 1 ; idx < linha.length(); idx++) {
                if (!isCaractereEspaco(linha.charAt(idx))) {
                    break;
                }
            }

            linhas[indiceCampoEstadosTerminais] = linha.substring(0, idx) + sb5.toString();
            
            // Altera o valor do campo [Parametros]/NumeroFitas.

            linha = linhas[indiceCampoNumeroFitas];
            
            for (idx = linha.indexOf("=") + 1 ; idx < linha.length(); idx++) {
                if (!isCaractereEspaco(linha.charAt(idx))) {
                    break;
                }
            }

            linhas[indiceCampoNumeroFitas] = linha.substring(0, idx) + 
            String.valueOf(numeroFitas);

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