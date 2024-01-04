package ec.edu.ista.springgc1.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ofertaslaborales")
public class OfertaLaboral {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "oferta_id", nullable = false)
    private Long id;

    private String cargo;

    private  String descripcion;

    private String area_conocimiento;

    private  String salario;

    private  String jornada;

    private  String requisitos_academicos;

    private  String experiencia;

    private  String ubicacion;

    private Date fecha_inicio;

    private  Date fecha_fin;

    //relacion con empresa

    @ManyToOne
		 @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
		 private Empresa empresa;

    //relacion con ciudad
    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private Ciudad ciudad;

   
    @OneToMany(mappedBy = "ofertaslaborales",cascade = CascadeType.REFRESH ,orphanRemoval = true)
    private Set<Postulado> postulacions = new HashSet<>();



}

