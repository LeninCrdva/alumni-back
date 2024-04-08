package ec.edu.ista.springgc1.service.generatorpdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import ec.edu.ista.springgc1.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class PDFGeneratorService {

    private static final String TEMPLATE_PATH = "curriculum/cv-graduate.html";

    @Autowired
    private ITemplateEngine templateEngine;

    public byte[] generatePDF(Map<String, Object> data) throws AppException, IOException {
        // Crear el contexto con las variables proporcionadas
        Context context = new Context();
        context.setVariables(data);

        // Procesar la plantilla HTML
        String processedHtml = templateEngine.process(TEMPLATE_PATH, context);

        // Convertir HTML a PDF
        ByteArrayOutputStream outputStream = null;
        PdfWriter writer = null;
        try {
            outputStream = new ByteArrayOutputStream();
            writer = new PdfWriter(outputStream);
            ConverterProperties properties = new ConverterProperties();
            HtmlConverter.convertToPdf(processedHtml, writer, properties);
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // Manejar la excepción
                }
            }
        }

        return outputStream.toByteArray();
    }
}