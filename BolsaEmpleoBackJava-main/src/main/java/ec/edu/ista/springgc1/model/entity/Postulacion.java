package ec.edu.ista.springgc1.model.entity;

import ec.edu.ista.springgc1.model.enums.EstadoPostulacion;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "graduado_id", referencedColumnName = "graduado_id")
    private Graduado graduado;

    @ManyToOne
    @JoinColumn(name = "oferta_laboral_id", referencedColumnName = "oferta_id")
    private OfertasLaborales ofertaLaboral;

    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estado;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    @Column(name = "fecha_postulacion")
    private LocalDateTime fechaPostulacion;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    @Nullable
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    @Nullable
    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    @PrePersist
    public void beforePersist() {
        this.fechaPostulacion = LocalDateTime.now();
    }

    @PreUpdate
    public void beforeUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        if (StringUtils.hasText(this.estado.name()) && ("CANCELADA_POR_GRADUADO".equals(this.estado.name()) || "CANCELADA_POR_ADMINISTRADOR".equals(this.estado.name()))) {
            this.fechaCancelacion = LocalDateTime.now();
        }
    }
}
