package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    Logger logger = LoggerFactory.getLogger(CredentialService.class);
    @Autowired
    CredentialMapper credentialMapper;
    @Autowired
    EncryptionService encryptionService;

    public List<Credential> getAllCredentials(Integer userId) {
        var credentials = credentialMapper.getAllCredentials(userId);
        for(var credential: credentials) {
            credential.setDisplayPassword(decryptPassword(credential.getPassword(), credential.getKey()));
        }
        return credentials;
    }

    public int addCredential(Credential credential) {
        var added = -1;
        try {
            added = credentialMapper.createCredential(encryptPassword(credential));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return added;
    }

    public int updateCredential(Credential credential) {
        var updated = -1;
        try {
            updated = credentialMapper.updateCredential(encryptPassword(credential));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return updated;
    }

    public int deleteCredential(Integer credentialId) {
        var deleted = -1;
        try {
            credentialMapper.deleteCredential(credentialId);
            deleted = 1;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return deleted;
    }

    private Credential encryptPassword(Credential credential) {
        if(credential.getKey() == null) {
            var random = new SecureRandom();
            var key = new byte[16];
            random.nextBytes(key);
            credential.setKey(Base64.getEncoder().encodeToString(key));
        }
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
        return credential;
    }

    private String decryptPassword(String password, String key) {
     return encryptionService.decryptValue(password, key);
    }
}
