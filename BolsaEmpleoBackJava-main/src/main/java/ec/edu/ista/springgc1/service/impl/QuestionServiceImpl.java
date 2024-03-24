package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.model.entity.Question;
import ec.edu.ista.springgc1.model.enums.QuestionType;
import ec.edu.ista.springgc1.repository.QuestionRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl extends GenericServiceImpl<Question> {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> findAllBySurveyId(Long surveyId) {
        return questionRepository.findQuestionsBySurveyId(surveyId);
    }

    List<Question> findQuestionsBySurveyIdAndType(Long surveyId, QuestionType type) {
        return questionRepository.findQuestionsBySurveyIdAndType(surveyId, type);
    }

    public List<Question> findAllBySurveyIdAndTypeIn(Long surveyId, List<QuestionType> types) {
        return questionRepository.findQuestionsBySurveyIdAndTypeIn(surveyId, types);
    }

    public List<Question> findAllBySurveyIdAndTypeNotIn(Long surveyId, List<QuestionType> types) {
        return questionRepository.findQuestionsBySurveyIdAndTypeNotIn(surveyId, types);
    }

    public List<Question> findAllBySurveyIdAndTextContaining(Long surveyId, String text) {
        return questionRepository.findQuestionsBySurveyIdAndTextContaining(surveyId, text);
    }

    public List<Question> findAllBySurveyIdAndTextContainingAndType(Long surveyId, String text, QuestionType type) {
        return questionRepository.findQuestionsBySurveyIdAndTextContainingAndType(surveyId, text, type);
    }

    public List<Question> findAllBySurveyIdAndTextContainingAndTypeIn(Long surveyId, String text, List<QuestionType> types) {
        return questionRepository.findQuestionsBySurveyIdAndTextContainingAndTypeIn(surveyId, text, types);
    }

    public List<Question> findAllBySurveyIdAndTextContainingAndTypeNotIn(Long surveyId, String text, List<QuestionType> types) {
        return questionRepository.findQuestionsBySurveyIdAndTextContainingAndTypeNotIn(surveyId, text, types);
    }

    public List<Question> findAllBySurveyIdAndOptionsContaining(Long surveyId, String option) {
        return questionRepository.findQuestionsBySurveyIdAndOptionsContaining(surveyId, option);
    }

    public List<Question> findAllBySurveyIdAndOptionsContainingAndTextContaining(Long surveyId, String option, String text) {
        return questionRepository.findQuestionsBySurveyIdAndOptionsContainingAndTextContaining(surveyId, option, text);
    }
}
