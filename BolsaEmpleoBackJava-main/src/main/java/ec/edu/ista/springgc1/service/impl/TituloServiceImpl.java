package ec.edu.ista.springgc1.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.GraduadoDTO;
import ec.edu.ista.springgc1.model.dto.SuperAdminDTO;
import ec.edu.ista.springgc1.model.dto.TituloDTO;
import ec.edu.ista.springgc1.model.dto.UsuarioDTO;
import ec.edu.ista.springgc1.model.entity.Carrera;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.Persona;
import ec.edu.ista.springgc1.model.entity.Rol;
import ec.edu.ista.springgc1.model.entity.SuperAdmin;
import ec.edu.ista.springgc1.model.entity.Titulo;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.repository.CarreraRepository;
import ec.edu.ista.springgc1.repository.GraduadoRepository;
import ec.edu.ista.springgc1.repository.TituloRepository;
import ec.edu.ista.springgc1.repository.UsuarioRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

@Service
public class TituloServiceImpl extends GenericServiceImpl<Titulo> implements Mapper<Titulo, TituloDTO>{
	  private boolean includeGraduadoInfo = false;
	 @Autowired
	    private GraduadoRepository graduadoRepository;
	 @Autowired
	 private TituloRepository titulorepository;
	 @Autowired
	 private CarreraRepository carrerarepository;
	@Override
	public Titulo mapToEntity(TituloDTO d) {
		Titulo t= new Titulo();
	    Graduado g= graduadoRepository.findById(d.getIdgraduado())
	    .orElseThrow(() -> new ResourceNotFoundException("id_graduado:", d.getIdgraduado()));
	    Carrera c =  carrerarepository.findByNombre(d.getNombrecarrera())
	 .orElseThrow(() -> new ResourceNotFoundException("carrera:", d.getNombrecarrera()));
		t.setFecha_emision(d.getFecha_emision());
		t.setFecha_registro(d.getFecha_registro());
		t.setInstitucion(d.getInstitucion());
		t.setNivel(d.getNivel());
		t.setNombre_titulo(d.getNombre_titulo());
		t.setTipo(d.getTipo());
		t.setNum_registro(d.getNum_registro());
		t.setCarrera(c);
		t.setGraduado(g);
		return t;
	}

	@Override
	public TituloDTO mapToDTO(Titulo e) {
		TituloDTO d= new TituloDTO ();
		d.setId(e.getId());
		d.setIdgraduado(e.getGraduado().getId());
		  
		d.setFecha_emision(e.getFecha_emision());
		d.setFecha_registro(e.getFecha_registro());
		d.setInstitucion(e.getInstitucion());
		d.setNivel(e.getNivel());
		d.setNombre_titulo(e.getNombre_titulo());
		d.setNombrecarrera(e.getCarrera().getNombre());
		d.setNum_registro(e.getNum_registro());
		d.setTipo(e.getTipo());
		return d;
	}

	@Override
	public List<TituloDTO> findAll() {
		includeGraduadoInfo = true; 
		List<TituloDTO> titulosDTO = titulorepository.findAll()
			.stream()
			.map(titulo -> mapToDTO(titulo))
			.collect(Collectors.toList());
	
		return titulosDTO;
	}
	

	  public TituloDTO findByIdToDTO(Long id) {
	    	Titulo t = titulorepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("id", id));

	        return mapToDTO(t);
	    }

    public Titulo update(long id, TituloDTO e) {

        Titulo t = titulorepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id", id));
      
        Graduado g= graduadoRepository.findById(e.getIdgraduado())
        	    .orElseThrow(() -> new ResourceNotFoundException("id_graduado:", e.getIdgraduado()));
        	    Carrera c =  carrerarepository.findByNombre(e.getNombrecarrera())
        	 .orElseThrow(() -> new ResourceNotFoundException("carrera:", e.getNombrecarrera()));
        
      
        
      
       t.setCarrera(c);
       t.setGraduado(g);
       t.setFecha_emision(e.getFecha_emision());
       t.setFecha_registro(e.getFecha_registro());
       t.setInstitucion(e.getInstitucion());
       t.setNivel(e.getNivel());
       t.setNombre_titulo(e.getNombre_titulo());
       t.setNum_registro(e.getNum_registro());
       t.setTipo(e.getTipo());
     
        return titulorepository.save(t);

    }

   

    @Override
    public Titulo save(Object entity) {
    	 includeGraduadoInfo = false;
        return titulorepository.save(mapToEntity((TituloDTO) entity));
    }

    public Long counttitulo(){
        return titulorepository.count();
    }
   

}
