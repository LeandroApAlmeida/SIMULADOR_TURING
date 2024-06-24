package turing.arquivo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Arquivo de ajuda em formato HTML incorporado ao .jar do projeto.
 * 
 * @author Leandro Ap. de Almeida
 * 
 * @since 1.0
 */
public class ArquivoAjuda {
    
    
    /**Caminho do arquivo.*/
    private final String arquivo;

    
    /**
     * Constructor padrão.
     * 
     * @param arquivo caminho do arquivo.
     */
    public ArquivoAjuda(String arquivo) {
        this.arquivo = arquivo;
    }
    
    
    /**
     * Ler o conteúdo do arquivo HTML de ajuda a partir dos recursos do arquivo
     * .jar do projeto.
     * 
     * @return conteúdo HTML do arquivo.
     * 
     * @throws IOException erro ao ler o arquivo.
     */
    public String ler() throws IOException {
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (InputStream inputStream = this.getClass().getResourceAsStream(arquivo)) {
            byte[] buffer = new byte[4096];
            int length;
            while((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
        
        return new String(outputStream.toByteArray());
        
    }
    
   
    /**
     * Obter o caminho do arquivo.
     * 
     * @return caminho do arquivo. 
     */
    public String getArquivo() {
        return arquivo;
    }
   
    
}