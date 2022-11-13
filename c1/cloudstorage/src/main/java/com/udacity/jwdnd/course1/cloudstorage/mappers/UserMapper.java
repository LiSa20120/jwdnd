package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * from USERS where username=#{username}")
    User findUserByName(String username);

    @Insert("INSERT into USERS (username, password, salt, firstname, lastname) values (#{username}, #{password}, #{salt}, #{firstname}, #{lastname})")
    @Options(keyProperty = "userid", useGeneratedKeys = true)
    int insertNewUser(User user);
}
