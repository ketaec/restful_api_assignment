package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private CommonBusinessService commonBusinessService;

    // API endpoint to get user profile using uuid
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/userprofile/{userId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userProfile(
            @PathVariable("userId") final String userUuid,
            @RequestHeader("authorization") final String authorization)
            throws UserNotFoundException, AuthorizationFailedException {

        UserEntity userEntity;
        try{
            String[] userToken = authorization.split("Bearer ");
            userEntity = commonBusinessService.getUserProfile(userUuid, userToken[1]);
        } catch(ArrayIndexOutOfBoundsException e){
            userEntity = commonBusinessService.getUserProfile(userUuid, authorization);
        }

        // creating user detail response
        UserDetailsResponse userDetailsResponse =
                new UserDetailsResponse()
                        .firstName(userEntity.getFirstName())
                        .lastName(userEntity.getLastName())
                        .userName(userEntity.getUserName())
                        .emailAddress(userEntity.getEmail())
                        .country(userEntity.getCountry())
                        .aboutMe(userEntity.getAboutMe())
                        .dob(userEntity.getDob())
                        .contactNumber(userEntity.getContactNumber());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}