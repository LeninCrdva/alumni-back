package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "registro_evento_grad")
public class Registro_Evento_Grad {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_registro")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "graduado_id", referencedColumnName = "graduado_id")
	private Graduado graduado;

	@ManyToOne
	@JoinColumn(name = "id_evento", referencedColumnName = "id_evento")
	private Evento evento;

	@DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDate fecha_registro;
}
