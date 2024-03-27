package ec.edu.ista.springgc1.repository;

import ec.edu.ista.springgc1.model.entity.OptionQuestion;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

import java.util.List;

public interface OptionRepository extends GenericRepository<OptionQuestion> {

    List<OptionQuestion> findByQuestionId(Long questionId);
}
