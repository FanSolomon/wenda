package com.company.wenda.service;

import com.company.wenda.aspect.LogAspect;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogAspect.class);
    //初始化
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null){
                addWord(lineTxt.trim());
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    //增加关键词
    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character c = lineTxt.charAt(i);    //当前字符

            if (isSymbol(c)) {
                continue;
            }

            TrieNode node = tempNode.getSubNode(c); //取当前节点下的节点

            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;

            if (i == lineTxt.length()-1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    private class TrieNode {
        private boolean end = false;    //标记是否为敏感词结尾

        //当前节点下所有的子节点
        private Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();

        //增加节点
        private void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        //获取下个节点
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        //判断是否为敏感词结尾
        boolean isKeywordEnd() {
            return end;
        }

        void setKeywordEnd(boolean end){
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();

    private boolean isSymbol(char c){
        int ic = (int) c;
        //0x2E80~0x9FFF为东亚文字
        return !CharUtils.isAsciiNumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        StringBuilder result = new StringBuilder();

        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0;  //红
        int position = 0;   //蓝

        while (position < text.length()) {
            char c = text.charAt(position);

            if (isSymbol(c)) {  //如果c是非法词，跳过
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            if (tempNode == null) {
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                //发现敏感词
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }

        result.append(text.substring(begin));   //加上最后一串
        return result.toString();
    }

    public static void main(String[] argv) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        System.out.print(s.filter("hi  我去色  //情！"));
    }
}
