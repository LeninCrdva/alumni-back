package ec.edu.ista.springgc1.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.EmpresaDTO;
import ec.edu.ista.springgc1.model.entity.Ciudad;
import ec.edu.ista.springgc1.model.entity.Empresa;
import ec.edu.ista.springgc1.model.entity.Empresario;
import ec.edu.ista.springgc1.model.entity.SectorEmpresarial;
import ec.edu.ista.springgc1.repository.CiudadRepository;
import ec.edu.ista.springgc1.repository.EmpresaRepository;
import ec.edu.ista.springgc1.repository.EmpresarioRepository;
import ec.edu.ista.springgc1.repository.SectorEmpresarialRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;

@Service
public class EmpresaServiceImpl extends GenericServiceImpl<Empresa > implements Mapper<Empresa, EmpresaDTO>{
	private boolean comprueba=false;
	@Autowired
	private EmpresaRepository empresarepository;
	@Autowired
	 private EmpresarioRepository empresariorepository;
	 @Autowired
	 private CiudadRepository ciudadrepository;
	 @Autowired
	 private SectorEmpresarialRepository sectorrepository;
	@Override
	public Empresa mapToEntity(EmpresaDTO d) {
	Empresa em=new Empresa();
	Ciudad ci=ciudadrepository.findByNombre(d.getCiudad().getNombre())
		   .orElseThrow(() -> new ResourceNotFoundException("ciudad", d.getCiudad().getNombre()));
	
	SectorEmpresarial emp=sectorrepository.findByNombre(d.getSectorEmpresarial().getNombre())
			   .orElseThrow(() -> new ResourceNotFoundException("Sector empresarial", d.getCiudad().getNombre()));
	
	Empresario empresario = empresariorepository.findByUsuarioNombreUsuarioIgnoreCase(d.getEmpresario())
             .orElseThrow(() -> new ResourceNotFoundException("Empresario", String.valueOf(d.getEmpresario())));
     em.setId(d.getId());
     em.setCiudad(ci);
     em.setSectorEmpresarial(emp);
     em.setEmpresario(empresario);
     em.setArea(d.getArea());
     em.setNombre(d.getNombre());
     em.setRazonSocial(d.getRazonSocial());
     em.setRuc(d.getRuc());
     em.setSitioWeb(d.getSitioWeb());
     em.setTipoEmpresa(d.getTipoEmpresa());
     em.setUbicacion(d.getUbicacion());
     return em;
	}
	@Override
	public EmpresaDTO mapToDTO(Empresa e) {
		EmpresaDTO em1= new EmpresaDTO();
		
		
			em1.setId(e.getId());
			em1.setArea(e.getArea());
			em1.setCiudad(e.getCiudad());
			em1.setEmpresario(e.getEmpresario().getUsuario().getNombreUsuario());
			em1.setNombre(e.getNombre());
			em1.setRazonSocial(e.getRazonSocial());
			em1.setRuc(e.getRuc());
			em1.setSitioWeb(e.getSitioWeb());
			em1.setTipoEmpresa(e.getTipoEmpresa());
			em1.setSectorEmpresarial(e.getSectorEmpresarial());
			em1.setUbicacion(e.getUbicacion());
			return em1;
	
	}
	 @Override
	    public List findAll() {
	        return empresarepository.findAll()
	                .stream()
	                .map(c -> mapToDTO(c))
	                .collect(Collectors.toList());
	    }
	 @Override
	    public Empresa save(Object entity) {
		 comprueba = true;
	        try {
	            return empresarepository.save(mapToEntity((EmpresaDTO) entity));
	        } finally {
	            comprueba = false;
	        }
	    }

	    @Transactional
	    public Boolean existsBynombre(String name) {
	        return empresarepository.existsBynombre(name);
	    }
	    public EmpresaDTO update(Long id, EmpresaDTO updatedEmpresaDTO) {
	        Empresa existingEmpresa = empresarepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Empresa", String.valueOf(id)));

	     
	        String nuevoNombre = updatedEmpresaDTO.getNombre();
	        if (!existingEmpresa.getNombre().equals(nuevoNombre) && existsBynombre(nuevoNombre)) {
	            throw new EntityExistsException("Ya existe una empresa con el mismo nombre.");
	        }

	       
	        existingEmpresa.setNombre(updatedEmpresaDTO.getNombre());
	        existingEmpresa.setArea(updatedEmpresaDTO.getArea());
	       
	        return mapToDTO(empresarepository.save(existingEmpresa));
	    }
	    
	    public void delete(Long id) {
	        empresarepository.deleteById(id);
	    }
	    public Set<EmpresaDTO> findByNombreUsuario(String nombreUsuario) {
	        Set<Empresa> empresas = empresarepository.findByNombreUsuario(nombreUsuario);
	        return empresas.stream().map(this::mapToDTO).collect(Collectors.toSet());
	    }
	  

	 
}
