package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AnswerBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(String questionUuid, AnswerEntity answerEntity, String token)
            throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuth = userDao.getUserAuthToken(token);
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userAuth.getLogoutAt() != null && userAuth.getLogoutAt().isAfter(userAuth.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
        }

        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionUuid);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }

        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setQuestion(questionEntity);
        answerEntity.setUser(userAuth.getUser());
        return answerDao.createAnswer(answerEntity);
    }
}
