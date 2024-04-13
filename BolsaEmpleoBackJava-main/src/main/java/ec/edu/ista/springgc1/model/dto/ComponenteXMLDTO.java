package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ComponenteXMLDTO implements Serializable {
    private Long id;

    private String tipo;

    private String xml_file;

    private byte[] foto_portada;
}
