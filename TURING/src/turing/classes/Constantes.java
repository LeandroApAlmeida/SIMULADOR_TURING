package turing.classes;

/**
 * Classe para definição de tokens do código do programa e constantes da linguagem.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class Constantes {
    
    
// --------------------------------- TOKENS --------------------------------- //
    
    /**Cabeçalho [Descricao] do arquivo.*/
    public static final String CABECALHO_DESCRICAO = "[Descricao]";
    /**Cabeçalho [Parametros] do arquivo.*/
    public static final String CABECALHO_PARAMETROS = "[Parametros]";
    /**Cabeçalho [Programa] do arquivo.*/
    public static final String CABECALHO_PROGRAMA = "[Programa]";
    /**Campo Nome= da seção [Descricao] do arquivo.*/
    public static final String CAMPO_NOME = "Nome=";
    /**Campo Modelo= da seção [Descricao] do arquivo.*/
    public static final String CAMPO_MODELO = "Modelo=";
    /**Campo AlfabetoEntrada= da seção [Parametros] do arquivo.*/
    public static final String CAMPO_ALFABETO_ENTRADA = "AlfabetoEntrada=";
    /**Campo AlfabetoAuxiliar= da seção [Parametros] do arquivo.*/
    public static final String CAMPO_ALFABETO_AUXILIAR = "AlfabetoAuxiliar=";
    /**Campo Estados= da seção [Parametros] do arquivo.*/
    public static final String CAMPO_ESTADOS = "Estados=";
    /**Campo EstadoInicial= da seção [Parametros] do arquivo.*/
    public static final String CAMPO_ESTADO_INICIAL = "EstadoInicial=";
    /**Campo EstadosTerminais= da seção [Parametros] do arquivo.*/
    public static final String CAMPO_ESTADOS_TERMINAIS = "EstadosTerminais=";
    /**Campo NumeroFitas= da seção [Parametros] do arquivo.*/
    public static final String CAMPO_NUMERO_FITAS = "NumeroFitas=";
    /**Campo // para digitação de comentários no arquivo.*/
    public static final String CAMPO_COMENTARIO = "//";
    
     
// -------------------------- SÍMBOLOS RESERVADOS  -------------------------- //
    
    /**Símbolo de início da fita ( * ).*/
    public static final char SIMBOLO_INICIO_FITA = '*';    
    /**Símbolo de branco ( _ ).*/
    public static final char SIMBOLO_BRANCO = '_';
    /**Símbolo de delimitador de seções ( # ).*/
    public static final char SIMBOLO_DELIMITADOR = '#';
    /**Símbolo de movimento para a direita ( D ).*/
    public static final char SIMBOLO_MOV_DIREITA = 'D';
    /**Símbolo de movimento para a esquerda ( E ).*/
    public static final char SIMBOLO_MOV_ESQUERDA = 'E';
    /**Símbolo de movimento nulo ( P ).*/
    public static final char SIMBOLO_MOV_NULO = 'P';
    /**Símbolo para representar o símbolo de espaço ( ).*/
    public static final String SIMBOLO_ESPACO = "$e";
    /**Símbolo para representar o símbolo de vírgula ( , ).*/
    public static final String SIMBOLO_VIRGULA = "$v";
 
    
// ------------------------- DEFINIÇÃO DA MÁQUINA  -------------------------- //    
    
    /**Tamanho inicial padrão da fita ( 25 células ).*/
    public static int TAMANHO_FITA = 25;
       
    
}