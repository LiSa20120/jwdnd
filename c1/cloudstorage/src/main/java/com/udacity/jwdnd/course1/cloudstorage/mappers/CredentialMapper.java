package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid=#{userId}")
    List<Credential> getAllCredentials(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(keyProperty = "credentialId", useGeneratedKeys = true)
    int createCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url=#{url}, username=#{username}, password=#{password}, key=#{key} WHERE credentialid=#{credentialId}")
    int updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid=#{credentialId}")
    void deleteCredential(Integer credentialId);
}
