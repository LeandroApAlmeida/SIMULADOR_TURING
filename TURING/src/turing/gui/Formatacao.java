package turing.gui;

import turing.programa.Tokens;
import static turing.programa.Tokens.SIMBOLO_BRANCO;
import static turing.gui.Sufixos.SUFIXO_BRANCO;

public final class Formatacao {
    
    
    public static String formatarSimbolos(String string) {
        
        String simboloBranco = new String(new byte[] {SIMBOLO_BRANCO});
        
        return string
        .replace(simboloBranco, SUFIXO_BRANCO)
        .replace(Sufixos.SUFIXO_CURSOR, "")
        .replace(Sufixos.SUFIXO_CEL_PIVO, "")
        .replace(Tokens.SIMBOLO_ESPACO, " ")
        .replace(Tokens.SIMBOLO_VIRGULA, ",");
    
    }
    
    
    public static String reverterSimbolos(String string) {
        
        String simboloBranco = new String(new byte[] {SIMBOLO_BRANCO});
        
        return string.replace(SUFIXO_BRANCO, simboloBranco);
        
    }
    
    
}