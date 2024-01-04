package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.model.entity.AreaTrabajo;
import ec.edu.ista.springgc1.repository.AreaTrabajoRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AreaTrabajoServiceImpl extends GenericServiceImpl<AreaTrabajo>{

    @Autowired
    private AreaTrabajoRepository areaTrabajoRepository;

    public Optional<AreaTrabajo> findByNombre(String nombre){
        return areaTrabajoRepository.findByNombre(nombre);
    }

}
