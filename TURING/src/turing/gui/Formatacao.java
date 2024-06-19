package turing.gui;

import static turing.classes.Constantes.SIMBOLO_BRANCO;
import static turing.classes.Constantes.SIMBOLO_INICIO_FITA;
import static turing.gui.Sufixos.SUFIXO_BRANCO;
import static turing.gui.Sufixos.SUFIXO_INICIO;

public final class Formatacao {
    
    
    public static String formatarSimbolos(String string) {
        String simboloBranco = new String(new byte[] {SIMBOLO_BRANCO});
        String simboloInicio = new String(new byte[] {SIMBOLO_INICIO_FITA});
        return string.replace(simboloBranco, SUFIXO_BRANCO).replace(simboloInicio, SUFIXO_INICIO);
    }
    
    
    public static String reverterSimbolos(String string) {
        String simboloBranco = new String(new byte[] {SIMBOLO_BRANCO});
        String simboloInicio = new String(new byte[] {SIMBOLO_INICIO_FITA});
        return string.replace(SUFIXO_BRANCO, simboloBranco).replace(SUFIXO_INICIO, simboloInicio); 
    }
    
    
}