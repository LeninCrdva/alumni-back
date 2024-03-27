package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.model.entity.OptionQuestion;
import ec.edu.ista.springgc1.repository.OptionRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl extends GenericServiceImpl<OptionQuestion> {

    @Autowired
    private OptionRepository optionRepository;

    public List<OptionQuestion> findByQuestionId(Long questionId) {
        return optionRepository.findByQuestionId(questionId);
    }
}