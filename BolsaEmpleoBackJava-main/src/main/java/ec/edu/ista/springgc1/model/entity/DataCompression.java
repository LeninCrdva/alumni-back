package ec.edu.ista.springgc1.model.entity;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.*;

public class DataCompression {

    /**
     * Comprime los datos utilizando GZIP.
     *
     * @param data       Los datos a comprimir.
     * @param targetSize El tamaño deseado después de la compresión.
     * @return Los datos comprimidos.
     * @throws IOException Si ocurre un error durante la compresión.
     */
    public byte[] compress(byte[] data, int targetSize) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(data);
            gzipOutputStream.finish();
            byte[] compressedData = outputStream.toByteArray();
            System.out.println("Tamaño original de la foto: " + data.length + " bytes");
            System.out.println("Tamaño de la foto comprimida: " + compressedData.length + " bytes");

            if (Math.abs(compressedData.length - targetSize) <= 100) {
                System.out.println("Comprimido a aproximadamente " + targetSize + " bytes.");
            } else {
                System.out.println("No se pudo comprimir hasta alcanzar aproximadamente " + targetSize + " bytes");
            }

            byte[] decompressedData = decompress(compressedData); // Descomprimir los datos comprimidos
            System.out.println("Tamaño de la foto descomprimida: " + decompressedData.length + " bytes");

            return compressedData;
        }
    }

    /**
     * Descomprime los datos utilizando GZIP.
     *
     * @param data Los datos comprimidos.
     * @return Los datos descomprimidos.
     * @throws IOException Si ocurre un error durante la descompresión.
     */
    public byte[] decompress(byte[] data) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        }
    }

 
}
