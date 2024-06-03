package turing.gui;

import java.awt.Color;
import turing.classes.AlfabetoFita;

public class Constantes {
    
    
    public static Color COR_ALFABETO_ENTRADA = Color.BLACK;
    
    public static Color COR_ALFABETO_AUXILIAR = Color.BLUE;
    
    public static Color COR_CURSOR_FITA = Color.WHITE;
    
    public static Color COR_SIMBOLOS_RESERVADOS = new Color(150, 150, 150);
    
    public static String CARACTER_BRANCO = "\u03B2";
    
    public static String CARACTER_INICIO = "\u229B";
    
    
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
