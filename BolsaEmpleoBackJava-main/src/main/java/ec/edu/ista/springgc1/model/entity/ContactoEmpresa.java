package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "contacto_empresa")
public class ContactoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cont_emp_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Empresa empresa;

    private String nombre;

    private String cargo;

    private String telefono;

    private String email;

}
