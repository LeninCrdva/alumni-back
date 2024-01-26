package ec.edu.ista.springgc1.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.model.entity.Persona;
import ec.edu.ista.springgc1.repository.PersonaRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;

@Service
public class PersonaServiceImp extends GenericServiceImpl<Persona>{
   @Autowired
   private PersonaRepository personarepository;
   public Optional<Persona> findBycedula(String cedula){
	   return personarepository.findBycedula(cedula);
   }
}
