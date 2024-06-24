package turing.classes;

import java.util.ArrayList;
import java.util.List;
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
 * Compilador para a Máquina de Turing. A função do compilador é receber como
 * entrada o código do programa em formato de texto UTF-8 e produzir como saída
 * todas as instruções para a construção da máquina e seu programa.
 * 
 * <br><br>
 * 
 * Traçando um paralelo com um compilador real, esta classe realiza as etapas
 * de análise léxica e análise sintática de um compilador para uma linguagem
 * de programação de alto-nível. Após a análise, são extraídos os parâmetros
 * para a construção da Máquina de Turing projetada e seu programa. Basicamente
 * é a tarefa de transformar o texto em tokens, e extrair o contexto destes
 * tokens para as instruções para um computador, no caso, uma máquina de Turing
 * abstrata que será simulada pelo programa.
 * 
 * <br><br>
 * 
 * Os dados obtidos com a compilação são: 
 * 
 * <br><br>
 * 
 * <ul>
 * 
 * <li>&nbsp;O alfabeto de entrada;</li><br>
 * 
 * <li>&nbsp;O alfabeto auxiliar;</li><br>
 * 
 * <li>&nbsp;O conjunto dos estados;</li><br>
 * 
 * <li>&nbsp;O estado inicial;</li><br>
 * 
 * <li>&nbsp;O conjunto dos estados terminais;</li><br>
 * 
 * <li>&nbsp;O número de fitas da máquina;</li><br>
 * 
 * <li>&nbsp;O programa, ou função de transição.</li>
 * 
 * </ul>
 * 
 * <br>
 * 
 * Vale ressaltar que a Máquina de Turing é um ente teórico, uma abstração de 
 * algoritmo e não uma máquina em si. Turing nunca construiu o dispositivo, apenas
 * o concebeu como um método de cálculo de predicados de Lógica de Primeira Ordem.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public final class Compilador {
    
    
    /**
     * Verificar se a linha inicia com o rótulo do campo pesquisado.
     * 
     * @param linha linha de texto.
     * 
     * @param campo campo pesquisado.
     * 
     * @param cabecalho identifica se é um rótulo de cabeçalho ou não.
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
     * Remover todos os espaços e tabulações em uma linha.
     * 
     * @param linha linha de texto.
     * 
     * @return texto da linha, sem espaços e tabulações.
     */
    private String normalizar(String linha) {
        return linha.trim().replace(" ", "").replace("\u0009", "");
    }
    
    
    /**
     * Obter os elementos inscritos em notação de conjunto. Um conjunto pode
     * ser vazio, ter apenas um elemento, ou múltiplos elementos.
     * 
     * <br><br>
     * 
     * Em caso de mais de um elemento no conjunto, cada um deve estar separado
     * por vírgula, da seguinte forma:
     * 
     * <br><br>
     * 
     * <BLOCKQUOTE>
     * { a, b, c, d, 1, 2, 3 } 
     * </BLOCKQUOTE>
     * 
     * @param linha linha de texto.
     * 
     * @return Elementos do conjunto. Eventualmente pode retornar um array vazio.
     */
    private String[] getElementosConjunto(String linha) { 
        String[] listaItens = null;
        linha = normalizar(linha);
        if (linha.startsWith("{") && linha.endsWith("}")) {
            linha = linha.substring(linha.indexOf("{") + 1, linha.indexOf("}"));
            if (linha.contains(",")) {
                listaItens = linha.split(",");
            } else {
                if (!linha.isEmpty()) {
                    listaItens = new String[1];
                    listaItens[0] = linha;
                } else {
                    listaItens = new String[0];
                }
            }
        }
        return listaItens;    
    }
    
    
    /**
     * Extrair o caractere do símbolo do alfabeto a partir da string passada. Será
     * realizado a seguinte validação:
     * 
     * <br><br>
     * 
     * <ol>
     * 
     * <li>A string deve conter apenas um caractere.</li><br>
     * 
     * <li>Converte a TAG '$e' em espaço.</li><br>
     * 
     * <li>Converte a TAG '$v' em vírgula.</li>
     * 
     * </ol>
     * 
     * @param linha linha de texto.
     * 
     * @return Caractere do símbolo do alfabeto, ou null, caso não seja um 
     * símbolo válido.
     */
    private Character getSimbolo(String linha) {
        if (linha.length() == 1) {
            return linha.charAt(0);
        } else {
            Character caractere = null;
            switch (linha) {
                case SIMBOLO_ESPACO -> caractere = ' ';
                case SIMBOLO_VIRGULA -> caractere = ',';
            }
            return caractere;
        }
    }

    
    /**
     * Obter a transição inscrita em uma linha do texto.
     * 
     * @param linha linha do texto.
     * 
     * @param alfabetoFita alfabeto da fita.
     * 
     * @param conjuntoEstados conjunto dos estados.
     * 
     * @param numeroFitas número de fitas da máquina de Turing.
     * 
     * @return Transicao obtida do processamento da linha, ou null, caso o
     * padrão esteja incorreto.
     */
    private Transicao getTransicao(String linha, AlfabetoFita alfabetoFita,
    ConjuntoEstados conjuntoEstados, int numeroFitas) {
        
        String instrucao = normalizar(linha);
        String[] tokens = instrucao.split("=");
        
        if (tokens.length == 2) {
            
            String[] ladoEsquerdo = tokens[0].split(",");
            String[] ladoDireito = tokens[1].split(",");
            
            if ((ladoEsquerdo.length == numeroFitas + 1) && 
            (ladoDireito.length == ((2 * numeroFitas) + 1))) {
                
                Estado estadoInicial = conjuntoEstados.getEstado(ladoEsquerdo[0]);
                Estado estadoFinal = conjuntoEstados.getEstado(ladoDireito[0]);

                if (estadoInicial != null && estadoFinal != null) {
                    
                    List<ParametrosFita> paramsFita = new ArrayList<>();
                    
                    boolean erro = false;
                
                    for (int i = 0; i < numeroFitas; i++) {

                        Simbolo simboloLido = alfabetoFita.getSimbolo(
                            ladoEsquerdo[i + 1].charAt(0)
                        );

                        Simbolo simboloEscrito = alfabetoFita.getSimbolo(
                            ladoDireito[i + 1].charAt(0)
                        );

                        DirecaoMovimento direcao = DirecaoMovimento.getDirecao(
                            ladoDireito[i + 1 + numeroFitas]
                        );

                        if (simboloLido != null && simboloEscrito != null && direcao != null) {
                            paramsFita.add(
                                new ParametrosFita(
                                    simboloLido,
                                    simboloEscrito,
                                    direcao
                                )
                            );
                        } else {
                            erro = true;
                            break;
                        }

                    }

                    if (!erro) {
                        return new Transicao(
                            estadoInicial,
                            estadoFinal,
                            paramsFita
                        );
                    } else {
                        return null;
                    }
                
                } else {
                    return null;
                }
                        
            } else {
                return null;
            }
            
        } else {
            return null;
        }
        
    }
    

    /**
     * Traduzir o código do programa em instruções para a montagem de uma máquina
     * de Turing, retornadas na instância da classe {@link ConfigMaqTuring}.
     * 
     * <br><br>
     * 
     * Para obter as instruções, o compilador fará a validação do texto do código
     * do programa. Este código tem o seguinte formato:
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
     * 
     * Onde:
     * 
     * 
     * <br><br>
     *
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
     * Ao compilar o código do programa, será feita a seguinte análise no texto:
     * 
     * <br><br>
     * 
     * <ol>
     * 
     * <li>&nbsp;Identificação de comentários.</li><br>
     * 
     * <li>&nbsp;Identificação dos cabeçalhos de seções.</li><br>
     * 
     * <li>&nbsp;Validação da ordem das seções.</li><br>
     * 
     * <li>&nbsp;Identificação dos campos das seções.</li><br>
     * 
     * <li>&nbsp;Validação da sintaxe dos campos das seções.</li><br>
     * 
     * <li>&nbsp;Validação dos valores dos campos das seções.</li><br>
     * 
     * <li>&nbsp;Validação do programa ou função de transição.</li>
     * 
     * </ol>
     * 
     * <br>
     * 
     * Esta etapa seria o equivalente às analises léxica e sintática em um 
     * compilador real, com um formalismo muito mais simplificado. Tem a finalidade
     * de detectar os seguintes problemas:
     * 
     * <br><br>
     * 
     * <ol>
     * 
     * <li>Texto aleatório no programa, fora das linhas de comentário.</li><br>
     * 
     * <li>Ausência de marcadores obrigatórios, como os cabeçalhos de seção e os
     * identificadores de campos internos de cada seção.</li><br>
     * 
     * <li>Ordem das seções incorreta. Dentro de cada seção, a ordem dos campos 
     * não é importante, apenas a ausência de um campo obrigatório gera um erro.
     * Mas a ordem das seções é fixa e pré-estabelecida, por questão de estética
     * e melhor clareza do código.</li><br>
     * 
     * <li>Sintaxe do campo incorreta. Por exemplo, um campo que usa notação
     * de conjuntos deve, obrigatóriamente, iniciar com '{' após o sinal de '='
     * e terminar com '}'. Também cada campo deve ocupar uma linha apenas.</li><br>
     * 
     * <li>Valor do campo não obedece às regras. As regras são:
     * 
     * <br><br>
     * 
     * <ul>
     * 
     * <li>&nbsp;Em campo com notação de conjunto, se houver mais do que um item,
     * cada item deve estar separado por vírgula.</li><br>
     * 
     * <li>&nbsp;O símbolo de um alfabeto deve ter um único caracter. Se houver
     * mais do que um caracter, está errado, com exceção do símbolo de vírgula,
     * que é representado pela TAG $v e o símbolo de espaço é representado pela
     * TAG $e, que serão convertidos para os respectivos caracteres.</li><br>
     * 
     * <li>&nbsp;O rótulo de um estado deve, obrigatóriamente, iniciar com a letra
     * 'q' seguido de uma combinação de letras maiúsculas ou minúsculas e números. 
     * Não são aceitos quaisquer outros caracteres para nomear um estado, gerando
     * um erro.</li><br>
     * 
     * <li>&nbsp;Um símbolo do alfabeto de entrada não pode estar no alfabeto 
     * auxiliar.</li><br>
     * 
     * <li>&nbsp;O estado inicial deve, obrigatóriamente, pertencer ao conjunto dos
     * estados, bem como cada estado do conjunto dos estados terminais.</li><br>
     * 
     * </ul>
     * 
     * <li>A função de transição tem inconsistências nas transições.</li>
     * 
     * </ol>
     * 
     * <br>
     * 
     * Validado o código do programa, todos os parâmetros para a máquina de Turing
     * são recuperados, e compõe o objeto da classe {@link ConfigMaqTuring}.
     * 
     * @param codigoPrograma códido do programa
     * 
     * @return Instruções para a construção da máquina de Turing.
     * 
     * @throws Exception erro detectado no processamento do código do programa.
     */
    public ConfigMaqTuring executar(String codigoPrograma) throws Exception {
        
        String nome;
    
        AlfabetoFita alfabetoFita = new AlfabetoFita();
    
        ConjuntoEstados conjuntoEstados = new ConjuntoEstados();
    
        FuncaoTransicao funcaoTransicao = new FuncaoTransicao();
    
        int numeroFitas = 1;
        
        StringBuilder sb = new StringBuilder();
        
        List<Integer> linhasErro = new ArrayList<>();

        String[] linhas = codigoPrograma.split("\n");

        int indiceSecaoDescricao = -1;
        int indiceSecaoParametros = -1;
        int indiceSecaoPrograma = -1;
        
        boolean erro = false;
        
        // Recupera os índices das seções.
        
        for (int indice = 0; indice < linhas.length; indice++) {
            String linha = linhas[indice];
            if (contemCampo(linha, CABECALHO_DESCRICAO)) {
                indiceSecaoDescricao = indice;
            } else if (contemCampo(linha, CABECALHO_PARAMETROS)) {
                indiceSecaoParametros = indice;
            } else if (contemCampo(linha, CABECALHO_PROGRAMA)) {
                indiceSecaoPrograma = indice;
            }
        }
        
        // Verifica se tem alguma seção faltando. Se alguma seção estiver
        // faltando, o programa será considerado incorreto e não poderá
        // prosseguir com a compilação.
        
        if (indiceSecaoDescricao == -1) {
            sb.append("\u22B3 Cabeçalho da sessão 'Descricao' não encontrado\n");
            erro = true;
        }
        
        if (indiceSecaoParametros == -1) {
            sb.append("\u22B3 Cabeçalho da sessão 'Parametros' não encontrado\n");
            erro = true;
        }
        
        if (indiceSecaoPrograma == -1) {
            sb.append("\u22B3 Cabeçalho da sessão 'Programa' não encontrado\n");
            erro = true;
        }
        
        if (erro) {
            throw new Exception(sb.toString());
        }
        
        // Verifica se a ordem das seções está correta.
        //
        // A ordem correta é:
        //
        // 1. [Descricao]
        // 2. [Parametros]
        // 3. [Programa]
        //
        // Se qualquer das seções estiver na ordem incorreta, o programa
        // será considerado incorreto e não poderá prosseguir com a
        // compilação.
        
        if (indiceSecaoDescricao > indiceSecaoParametros || 
        indiceSecaoDescricao > indiceSecaoPrograma) {
            sb.append("\u22B3 A ordem das seções está incorreta\n");
            erro = true;
        }
        
        if (indiceSecaoParametros > indiceSecaoPrograma) {
            sb.append("\u22B3 A ordem das seções está incorreta\n");
            erro = true;
        }
        
        if (erro) {
            throw new Exception(sb.toString());
        }
        
        // Verifica se há linhas com texto antes da seção [Descricao] que
        // não é de comentário. Comentários iniciam com duas barras (//).
        
        for (int indice = 0; indice < indiceSecaoDescricao; indice++) {
            String linha = linhas[indice];
            if (!contemCampo(linha, CAMPO_COMENTARIO)) {
                if (linha.trim().length() > 0) {
                    linhasErro.add(indice);
                    erro = true;
                }
            }
        }
        
        // Verifica se todos os campos obrigatórios, em todas as seções estão
        // presentes. Caso um ou mais campos não exista, o programa será considerado 
        // incorreto e não poderá prosseguir com a compilação.
        
        // 1. Verifica a existência do campo Nome da seção [Descricao].

        int indiceCampoNome = -1;

        for (int indice = indiceSecaoDescricao + 1; indice < indiceSecaoParametros; indice++) {
            String linha = linhas[indice];
            if (!contemCampo(linha, CAMPO_COMENTARIO)) {
                if (contemCampo(linha, CAMPO_NOME)) {
                    indiceCampoNome = indice;
                } else  {
                    if (linha.trim().length() > 0) {
                        linhasErro.add(indice);
                        erro = true;
                    }
                }
            }
        }
        
        if (indiceCampoNome == -1) {
            sb.append("\u22B3 Campo 'Nome' da seção 'Descricao' não encontrado\n");
            erro = true;
        }
        
        // 2. Verifica a existência dos campos:
        //
        // 1. AlfabetoEntrada
        // 2. AlfabetoAuxiliar
        // 3. Estados
        // 4. EstadoInicial
        // 5. EstadosTerminais
        // 6. NumeroFitas
        //
        // da seção [Parametros].
        
        int indiceCampoAlfabetoEntrada = -1;
        int indiceCampoAlfabetoAuxiliar = -1;
        int indiceCampoEstados = -1;
        int indiceCampoEstadoInicial = -1;
        int indiceCampoEstadosTerminais = -1;
        int indiceCampoNumeroFitas = -1;
        
        for (int indice = indiceSecaoParametros + 1; indice < indiceSecaoPrograma; indice++) {   
            String linha = linhas[indice];
            if (!contemCampo(linha, CAMPO_COMENTARIO)) {
                if (contemCampo(linha, CAMPO_ALFABETO_ENTRADA)) {
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
                } else {
                    if (linha.trim().length() > 0) {
                        linhasErro.add(indice);
                        erro = true;
                    }
                }
            }
        }
        
        if (indiceCampoAlfabetoEntrada == -1) {
            sb.append("\u22B3 Campo 'AlfabetoEntrada' da seção 'Parametros' não encontrado\n");
            erro = true;
        }
        
        if (indiceCampoAlfabetoAuxiliar == -1) {
            sb.append("\u22B3 Campo 'AlfabetoAuxiliar' da seção 'Parametros' não encontrado\n");
            erro = true;
        }
        
        if (indiceCampoEstados == -1) {
            sb.append("\u22B3 Campo 'Estados' da seção 'Parametros' não encontrado\n");
            erro = true;
        }
        
        if (indiceCampoEstadoInicial == -1) {
            sb.append("\u22B3 Campo 'EstadoInicial' da seção 'Parametros' não encontrado\n");
            erro = true;
        }
        
        if (indiceCampoEstadosTerminais == -1) {
            sb.append("\u22B3 Campo 'EstadosTerminais' da seção 'Parametros' não encontrado\n");
            erro = true;
        }
        
        if (indiceCampoNumeroFitas == -1) {
            sb.append("\u22B3 Campo 'NumeroFitas' da seção 'Parametros' não encontrado\n");
            erro = true;
        }
        
        if (erro) {
            for (Integer linha : linhasErro) {
                sb.append("\u22B3 Erro na linha ");
                sb.append(String.valueOf(linha + 1));
                sb.append("\n");
            }
            throw new Exception(sb.toString());
        }
        
        // A partir deste ponto, recupera os valores de cada campo de cada
        // seção do código do programa. 
        
        // 1. Seção [Descricao]
        
        // [Descricao]/Nome: Obtém o nome da Máquina de Turing.

        String linha = linhas[indiceCampoNome];
        String[] tokens = linha.split("=");
        
        if (tokens.length > 1) {
            nome = tokens[1].trim();
        } else {
            nome = "";
        }

        // 2. Seção [Parametros].

        // [Parametros]/AlfabetoEntrada: Obtém o Alfabeto de Entrada.
        
        linha = normalizar(linhas[indiceCampoAlfabetoEntrada]);
        tokens = linha.split("=");
        
        if (tokens.length > 1) {
            
            String[] listaSimbolos = getElementosConjunto(tokens[1]);
            
            if (listaSimbolos != null) {
                
                for (String simbolo : listaSimbolos) {
                    
                    Character caractere = getSimbolo(simbolo);
                    
                    if (caractere != null) {
                        alfabetoFita.inserirSimbolo(new Simbolo(caractere, false));
                    } else {
                        sb.append("\u22B3 Símbolo do 'AlfabetoEntrada' inválido (linha ");
                        sb.append(String.valueOf(indiceCampoAlfabetoEntrada + 1));
                        sb.append("): ");
                        sb.append(simbolo);
                        sb.append("\n");
                        erro = true; 
                    }
                    
                }
                
            } else {
                sb.append("\u22B3 Sintaxe do campo 'AlfabetoEntrada' incorreta (linha ");
                sb.append(String.valueOf(indiceCampoAlfabetoEntrada + 1));
                sb.append(")\n");
                erro = true;
            }

        } else {
            sb.append("\u22B3 Sintaxe do campo 'AlfabetoEntrada' incorreta (linha ");
            sb.append(String.valueOf(indiceCampoAlfabetoEntrada + 1));
            sb.append(")\n");
            erro = true;
        }
        
        // [Parametros]/AlfabetoAuxiliar: Obtém o Alfabeto Auxiliar.
        
        linha = normalizar(linhas[indiceCampoAlfabetoAuxiliar]);
        tokens = linha.split("=");
        
        if (tokens.length > 1) {
            
            String[] listaSimbolos = getElementosConjunto(tokens[1]);
            
            if (listaSimbolos != null) {
                
                for (String simbolo : listaSimbolos) {
                    
                    Character caractere = getSimbolo(simbolo);
                    
                    if (caractere != null) {
                       
                        // Verifica se o símbolo pertence ao alfabeto
                        // de entrada, que já foi inserido no alfabeto 
                        // da fita.

                        boolean inserir = true;

                        for (Simbolo s : alfabetoFita) {
                            if (s.getCaracter() == simbolo.charAt(0)) {
                                inserir = false;
                                break;
                            }
                        }

                        if (inserir) {
                            alfabetoFita.inserirSimbolo(new Simbolo(caractere, true));
                        } else {
                            sb.append("\u22B3 Símbolo do 'AlfabetoAuxiliar' inválido (linha ");
                            sb.append(String.valueOf(indiceCampoAlfabetoAuxiliar + 1));
                            sb.append("): ");
                            sb.append(simbolo);
                            sb.append("\n");
                            erro = true;
                        }
                        
                    } else {
                        sb.append("\u22B3 Símbolo do 'AlfabetoAuxiliar' inválido (linha ");
                        sb.append(String.valueOf(indiceCampoAlfabetoEntrada + 1));
                        sb.append("): ");
                        sb.append(simbolo);
                        sb.append("\n");
                        erro = true; 
                    }
                    
                }
                
            } else {
                sb.append("\u22B3 Sintaxe do campo 'AlfabetoAuxiliar' incorreta (linha ");
                sb.append(String.valueOf(indiceCampoAlfabetoEntrada + 1));
                sb.append(")\n");
                erro = true;
            }

        } else {
            sb.append("\u22B3 Sintaxe do campo 'AlfabetoAuxiliar' incorreta (linha ");
            sb.append(String.valueOf(indiceCampoAlfabetoEntrada + 1));
            sb.append(")\n");
            erro = true;
        }

        // [Parametros]/Estados: Obtém os estados.
        
        linha = normalizar(linhas[indiceCampoEstados]);
        tokens = linha.split("=");

        if (tokens.length > 1) {
            
            String[] listaEstados = getElementosConjunto(tokens[1]);
            
            if (listaEstados != null) {

                for (String estado : listaEstados) {

                    if (Estado.rotuloValido(estado)) {
                        conjuntoEstados.inserirEstado(new Estado(estado));
                    } else {
                        sb.append("\u22B3 Rótulo do estado inválido (linha ");
                        sb.append(String.valueOf(indiceCampoEstados + 1));
                        sb.append("): ");
                        sb.append(estado);
                        sb.append("\n");
                        erro = true;
                    }

                }
                
            } else {   
                sb.append("\u22B3 Sintaxe do campo 'Estados' incorreta (linha ");
                sb.append(String.valueOf(indiceCampoEstados + 1));
                sb.append(")\n");
                erro = true;  
            }
            
        } else {
            sb.append("\u22B3 Sintaxe do campo 'Estados' incorreta (linha ");
            sb.append(String.valueOf(indiceCampoEstados + 1));
            sb.append(")\n");
            erro = true; 
        }

        // [Parametros]/EstadoInicial: Define o estado inicial.
        
        linha = normalizar(linhas[indiceCampoEstadoInicial]);
        tokens = linha.split("=");
        
        if (tokens.length > 1) {
            
            String estadoInicial = tokens[1];
            boolean alterado = false;
            
            for (Estado estado : conjuntoEstados) {
                if (estado.getRotulo().equals(estadoInicial)) {
                    estado.setInicial(true);
                    alterado = true;
                    break;
                }
            }
            
            if (!alterado) {
                sb.append("\u22B3 Campo 'EstadoInicial' incorreto (linha ");
                sb.append(String.valueOf(indiceCampoEstadoInicial + 1));
                sb.append("): ");
                sb.append(estadoInicial);
                sb.append("\n");
                erro = true;  
            }
            
        }

        // [Parametros]/EstadosTerminais: Define os estados terminais.
        
        linha = normalizar(linhas[indiceCampoEstadosTerminais]);
        tokens = linha.split("=");
        
        if (tokens.length > 1) {
            
            String[] listaEstados = getElementosConjunto(tokens[1]);
            
            if (listaEstados != null) {

                for (String rotulo : listaEstados) {

                    boolean alterado = false;

                    for (Estado estado : conjuntoEstados) {
                        if (estado.getRotulo().equals(rotulo)) {
                            estado.setTerminal(true);
                            alterado = true;
                            break;
                        }
                    }

                    if (!alterado) {
                        sb.append("\u22B3 Campo 'EstadoTerminal' incorreto (linha ");
                        sb.append(String.valueOf(indiceCampoEstadosTerminais + 1));
                        sb.append("): ");
                        sb.append(rotulo);
                        sb.append("\n");
                        erro = true;
                    }

                }
                
            } else {
                sb.append("\u22B3 Sintaxe do campo 'EstadosTerminais' incorreta (linha ");
                sb.append(String.valueOf(indiceCampoEstadosTerminais + 1));
                sb.append(")\n");
                erro = true;
            }
            
        } else {
            sb.append("\u22B3 Sintaxe do campo 'EstadosTerminais' incorreta (linha ");
            sb.append(String.valueOf(indiceCampoEstadosTerminais + 1));
            sb.append(")\n");
            erro = true;
        }
        
        // [Parametros]/NumeroFitas: Obtém o número de fitas.
        
        linha = normalizar(linhas[indiceCampoNumeroFitas]);
        tokens = linha.split("=");
        
        if (tokens.length > 1) {
            try {
                numeroFitas = Integer.parseInt(tokens[1]); 
            } catch (Exception ex) {
                sb.append("\u22B3 Campo 'NumeroFitas' incorreto (linha ");
                sb.append(String.valueOf(indiceCampoNumeroFitas + 1));
                sb.append(")\n");
                erro = true;
            }
        } else {
            sb.append("\u22B3 Sintaxe do campo 'NumeroFitas' incorreta (linha ");
            sb.append(String.valueOf(indiceCampoNumeroFitas + 1));
            sb.append(")\n");
            erro = true;
        }

        // 3. Seção [programa]

        // Recupera o programa da máquina.
        
        for (int i = indiceSecaoPrograma + 1; i < linhas.length; i++) {
            linha = linhas[i];
            if (linha.trim().length() > 0) {
                Transicao transicao = getTransicao(
                    linha,
                    alfabetoFita,
                    conjuntoEstados,
                    numeroFitas
                );
                if (transicao != null) {
                    funcaoTransicao.adicionarTransicao(transicao);
                } else  {
                    sb.append("\u22B3 Transição incorreta (linha ");
                    sb.append(String.valueOf(i + 1));
                    sb.append("): ");
                    sb.append(linha);
                    sb.append("\n");
                    erro = true;
                }
            } 
        }
        
        if (erro) {
            throw new Exception(sb.toString());
        }
        
        return new ConfigMaqTuring(
            nome,
            alfabetoFita,
            conjuntoEstados,
            funcaoTransicao,
            numeroFitas
        );


    }
    
    
}