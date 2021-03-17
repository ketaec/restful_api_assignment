package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/")
public class AnswerController {

    @Autowired
    private AnswerBusinessService answerBusinessService;

    // Endpoint to create an answer to a particular question
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/question/{questionId}/answer/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> editQuestionContent(
            final AnswerRequest answerRequest,
            @PathVariable("questionId") final String questionId,
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, InvalidQuestionException {

        String bearerToken = getBearerToken(authorization);
        // Creating Answer entity for further update
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answerRequest.getAnswer());
        answerEntity.setUuid(UUID.randomUUID().toString());

        // Return response with updated Answer entity
        AnswerEntity createdAnswerEntity = answerBusinessService.createAnswer(questionId, answerEntity, bearerToken);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    // Endpoint to edit an answer
    @RequestMapping(
            path = "/answer/edit/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer(
            @RequestHeader("authorization") final String authorization,
            @PathVariable("answerId") final String ansUuid,
            final AnswerRequest answerRequest)
            throws AuthorizationFailedException, AnswerNotFoundException {

        String bearerToken = getBearerToken(authorization);
        // calling edit answer business logic
        answerBusinessService.editAnswer(ansUuid, answerRequest.getAnswer(), bearerToken);
        AnswerEditResponse updatedAnswerResponse =
                new AnswerEditResponse().id(ansUuid).status("ANSWER EDITED");
        return new ResponseEntity<>(updatedAnswerResponse, HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/answer/delete/{answerId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(
            @RequestHeader("authorization") final String authorization,
            @PathVariable("answerId") final String ansUuid)
            throws AuthorizationFailedException, AnswerNotFoundException {

        String bearerToken = getBearerToken(authorization);
        // delete answer
        answerBusinessService.deleteAnswer(ansUuid, bearerToken);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(ansUuid).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "answer/all/{questionId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(
            @RequestHeader("authorization") final String authorization,
            @PathVariable("questionId") final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {

        String bearerToken = getBearerToken(authorization);
        // Get all answers for requested question
        List<AnswerEntity> allAnswers = answerBusinessService.getAllAnswersToQuestion(questionId, bearerToken);

        // Create response
        List<AnswerDetailsResponse> allAnswersResponse = new ArrayList<AnswerDetailsResponse>();

        for (int i = 0; i < allAnswers.size(); i++) {
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse()
                    .answerContent(allAnswers.get(i).getAnswer())
                    .questionContent(allAnswers.get(i).getQuestion().getContent())
                    .id(allAnswers.get(i).getUuid());
            allAnswersResponse.add(answerDetailsResponse);
        }

        // Return response
        return new ResponseEntity<List<AnswerDetailsResponse>>(allAnswersResponse, HttpStatus.FOUND);
    }

    // method to get bearer token from authorization token
    public String getBearerToken(String authorization) {
        String bearerToken;
        try {
            bearerToken = authorization.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = authorization;
        }
        return bearerToken;
    }
}
