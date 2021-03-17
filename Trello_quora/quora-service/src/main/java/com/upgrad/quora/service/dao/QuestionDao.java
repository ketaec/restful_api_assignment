package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    // method to create question
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    // method to list all questions
    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    // method to get question using uuid
    public QuestionEntity getQuestionByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("questionEntityByUuid", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    // method to edit question
    public QuestionEntity editQuestionContent(final QuestionEntity questionEntity) {
        return entityManager.merge(questionEntity);
    }

    // method to delete a question
    public void userQuestionDelete(final String uuid) {
        QuestionEntity questionEntity = getQuestionByUuid(uuid);
        entityManager.remove(questionEntity);
    }

    // method to list all question by user
    public List<QuestionEntity> getAllQuestionsByUser(final String uuid) {
        try {
            return entityManager.createNamedQuery("questionByUserId", QuestionEntity.class).setParameter("uuid", uuid).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
