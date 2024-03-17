package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;

import org.hibernate.annotations.ColumnTransformer;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "evento")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long id;

    @Column(unique = true)
    @ColumnTransformer(write = "UPPER(?)")
    private String nombreEvento;

    private Boolean estado = true;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime hora_inicio;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime hora_fin;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDate fecha;

    @ColumnTransformer(write = "UPPER(?)")
    private String lugar;

    @ColumnTransformer(write = "UPPER(?)")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_admi", referencedColumnName = "id_admi", nullable = true)
    private Administrador admin;
}
