package com.company.wenda;

import com.company.wenda.dao.QuestionDAO;
import com.company.wenda.dao.UserDAO;
import com.company.wenda.model.Question;
import com.company.wenda.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Test
    public void initDatabase() {
        Random random = new Random();

        for (int i = 0; i < 11; ++i){
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("  ");
            user.setSalt("  ");
            userDAO.addUser(user);

            user.setPassword("xx");
            userDAO.updatePassword(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*36008*i);
            question.setCreatedDate(date);
            question.setUserId(i+1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("ABKDJFALJDFLA Content %d", i));

            questionDAO.addQuestion(question);
        }

        Assert.assertEquals("xx", userDAO.selectByid(1).getPassword());
        userDAO.deleteByid(1);
        Assert.assertNull(userDAO.selectByid(1));

        //System.out.print(questionDAO.selectLatestQuestions(0, 0, 10));

    }
}
