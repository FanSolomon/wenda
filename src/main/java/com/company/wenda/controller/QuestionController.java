package com.company.wenda.controller;

import com.company.wenda.model.*;
import com.company.wenda.service.CommentService;
import com.company.wenda.service.LikeService;
import com.company.wenda.service.QuestionService;
import com.company.wenda.service.UserService;
import com.company.wenda.util.WendaUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() == null){
                //question.setUserId(WendaUtil.ANONYMOUS_USERID);
                return WendaUtil.getJSONString(999);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0) {
                return WendaUtil.getJSONString(0);      //问题添加成功，返回0
            }
        }catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1,"失败");
    }

    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.selectByid(qid);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));

        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }

            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("comments", vos);

        return "detail";
    }
}
