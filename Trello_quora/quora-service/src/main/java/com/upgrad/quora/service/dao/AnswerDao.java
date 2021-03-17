package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    // method to create answer
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {

        entityManager.persist(answerEntity);
        return answerEntity;
    }

    // method to get answer using uuid
    public AnswerEntity getAnswerByUuid(String uuid) {

        try {
            return entityManager.createNamedQuery("answerEntityByUuid", AnswerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    // method to update answer
    public AnswerEntity editAnswer(AnswerEntity answerEntity) {
        return entityManager.merge(answerEntity);
    }

    // method to delete answer using uuid
    public void deleteAnswer(final String answerUuid) {
        AnswerEntity answerEntity = getAnswerByUuid(answerUuid);
        entityManager.remove(answerEntity);
    }

    // method to list all answers of given question
    public List<AnswerEntity> getAllAnswersToQuestion(final String questionId) {
        try {
            return entityManager.createNamedQuery("answerEntityByQuestionId", AnswerEntity.class).setParameter("uuid", questionId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
