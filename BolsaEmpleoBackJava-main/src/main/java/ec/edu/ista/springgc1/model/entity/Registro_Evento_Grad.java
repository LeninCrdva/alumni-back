package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

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
	private LocalDateTime fechaRegistro;
}
