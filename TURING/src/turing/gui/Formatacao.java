package turing.gui;

import turing.classes.AlfabetoFita;
import static turing.gui.Constantes.CARACTER_BRANCO;
import static turing.gui.Constantes.CARACTER_INICIO;

public final class Formatacao {
    
    
    public static String formatarSimbolos(String string) {
        String branco = new String(new byte[] {AlfabetoFita.BRANCO});
        String inicio = new String(new byte[] {AlfabetoFita.INICIO_FITA});
        return string.replace(branco, CARACTER_BRANCO).replace(inicio, CARACTER_INICIO);
    }
    
    
    public static String reverterSimbolos(String string) {
        String branco = new String(new byte[] {AlfabetoFita.BRANCO});
        String inicio = new String(new byte[] {AlfabetoFita.INICIO_FITA});
        return string.replace(CARACTER_BRANCO, branco).replace(CARACTER_INICIO, inicio); 
    }
    
    
}