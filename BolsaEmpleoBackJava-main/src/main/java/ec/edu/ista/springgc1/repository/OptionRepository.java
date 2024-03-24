package ec.edu.ista.springgc1.repository;

import ec.edu.ista.springgc1.model.entity.Option;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

import java.util.List;

public interface OptionRepository extends GenericRepository<Option> {

    List<Option> findByQuestionId(Long questionId);
}
