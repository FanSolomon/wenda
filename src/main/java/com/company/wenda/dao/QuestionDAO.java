package com.company.wenda.dao;

import com.company.wenda.model.Question;
import com.company.wenda.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper         //跟MyBatis关联的一个DAO
@Service
public interface QuestionDAO {
    String TABLE_NAME = " question ";        //前后加空格，以免连起来出问题
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})//head_url->headUrl,读取的是user里的字段
    int addQuestion(Question question);


    List<Question> selectLatestQuestions(@Param("userId") int userId,
                               @Param("offset") int offset,
                               @Param("limit") int limit);
}
