package ec.edu.ista.springgc1.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MailRequest {
	
	private String name;

	private String to;

	private String from;

	private String subject;

	private String caseEmail;
}
