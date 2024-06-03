package turing.arquivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ArquivoTexto {
    
    
    private final File arquivo;

    
    public ArquivoTexto(File arquivo) {
        this.arquivo = arquivo;
    }
    
    
    public void gravar(String script) throws UnsupportedEncodingException, IOException {
        try (FileWriter writer = new FileWriter(arquivo)) {
            writer.write(script);
        }
    }
    
    
    public String ler() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (FileReader reader = new FileReader(arquivo);
            BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = bufferedReader.readLine();
            if (line != null) {
                sb.append(line);
            }
            while ((line = bufferedReader.readLine()) != null) {
                sb.append("\n");
                sb.append(line);
            }
        }
        return sb.toString();
    }

    
    public File getArquivo() {
        return arquivo;
    }
    
    
}