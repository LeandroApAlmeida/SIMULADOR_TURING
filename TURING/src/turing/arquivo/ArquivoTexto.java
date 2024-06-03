package turing.arquivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Arquivo para leitura e escrita de conteúdo em formato texto.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class ArquivoTexto {
    
    
    /**Caminho do arquivo.*/
    private final File arquivo;

    
    /**
     * Constructor padrão.
     * 
     * @param arquivo caminho do arquivo.
     */
    public ArquivoTexto(File arquivo) {
        this.arquivo = arquivo;
    }
    
    
    /**
     * Gravar o conteúdo texto no arquivo.
     * 
     * @param texto Texto a ser gravado no arquivo.
     * 
     * @throws UnsupportedEncodingException erro na codificação de caracteres.
     * 
     * @throws IOException erro ao acessar o arquivo (bloqueado, inexistente,
     * falta de espaço em disco, etc).
     */
    public void gravar(String texto) throws UnsupportedEncodingException, IOException {
        try (FileWriter writer = new FileWriter(arquivo)) {
            writer.write(texto);
        }
    }
    
    
    /**
     * Ler o conteúdo texto do arquivo.
     * 
     * @return texto lido do arquivo.
     * 
     * @throws IOException erro ao acessar o arquivo (bloqueado, inexistente,
     * falta de espaço em disco, etc).
     */
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

    
    /**
     * Obter o caminho (path) do arquivo.
     * 
     * @return caminho (path do arquivo).
     */
    public File getArquivo() {
        return arquivo;
    }
    
    
}