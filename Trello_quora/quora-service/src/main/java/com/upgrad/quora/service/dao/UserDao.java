package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext private EntityManager entityManager;

    // this method creates new user with userEntity object
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    // method to find user by his/her email address
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager
                    .createNamedQuery("userByEmail", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    // method to find user by using uuid
    public UserEntity getUserByUuid(final String uuid) {
        try {
            return entityManager
                    .createNamedQuery("userByUuid", UserEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    // method to find user by using username
    public UserEntity getUserByUsername(final String userName) {
        try {
            return entityManager
                    .createNamedQuery("userByUsername", UserEntity.class)
                    .setParameter("userName", userName)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    // method to create auth token
    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity UserAuthTokenEntity) {
        entityManager.persist(UserAuthTokenEntity);
        return UserAuthTokenEntity;
    }

    // method to update update user
    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    // method to get user auth token
    public UserAuthTokenEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager
                    .createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    // method to signout user
    public void updateUserSignOutAt(final UserAuthTokenEntity updateUserSignOutAt) {
        entityManager.merge(updateUserSignOutAt);
    }

    public void deleteUser(final String userUuid) {
        UserEntity userEntity = getUserByUuid(userUuid);
        entityManager.remove(userEntity);
    }

}
