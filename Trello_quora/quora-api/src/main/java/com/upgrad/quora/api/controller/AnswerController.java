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

        // Creating Answer entity for further update
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answerRequest.getAnswer());
        answerEntity.setUuid(UUID.randomUUID().toString());

        // Return response with updated Answer entity
        AnswerEntity createdAnswerEntity = answerBusinessService.createAnswer(questionId, answerEntity, authorization);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

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

        answerBusinessService.editAnswer(ansUuid, answerRequest.getAnswer(), authorization);
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

        answerBusinessService.deleteAnswer(ansUuid, authorization);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(ansUuid).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }


}
