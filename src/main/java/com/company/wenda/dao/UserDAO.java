package com.company.wenda.dao;

import com.company.wenda.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Mapper         //跟MyBatis关联的一个DAO
@Service
public interface UserDAO {
    String TABLE_NAME = " user ";        //前后加空格，以免连起来出问题
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{name}, #{password}, #{salt}, #{headUrl})"})//head_url->headUrl,读取的是user里的字段
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectByid(int id);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteByid(int id);
}
