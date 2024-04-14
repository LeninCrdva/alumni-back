package ec.edu.ista.springgc1.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreviousDataForPdfDTO {

    @JsonView(View.Public.class)
    private Graduado data;

    @JsonView(View.Public.class)
    private byte[] pdfBytes;
}