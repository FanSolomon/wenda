<!DOCTYPE html>
<html>
<body>
<pre>
    ${value1}
    <#list colors as color>
        This is Color ${color_index}: ${color}
    </#list>

    <#list map?keys as key>
        ${key}:${map[key]}
    </#list>

    User: ${user.name}
    ${user.description} <#--  查找get或is开头的方法  -->
    ${user.getDescription()}    <#--  直接调用方法  -->

    <#assign title = "nowcoder"> <#-- 创建变量 title -->
    Title: ${title}

    <#include "header.html" parse = true>
    <#-- parse如果为true,那么被包含的文件将会当作FTL来解析，
        否则整个文件将被视为简单文本默认是true
        主要用于公共部分的引入-->

    <#-- 不带参数的宏 -->
    <#macro test>
        Little text
    </#macro>
    <#-- call the macro: -->
    <@test/>

    <#-- 带参数的宏 -->
    <#macro render_color index color>
        Test text, and the params: ${index}, ${color}
    </#macro>
    <#-- call the macro: -->
    <#--<@render_color index="color_index" color="color">-->
    <#list colors as color>
        <@render_color color_index color></@render_color>
    </#list>

    <#assign h = "hello">
    <#assign hw1 = "${h} world1">
    <#assign hw2 = '${h} world2'>
    ${h}
    ${hw1}
    ${hw2}

</pre>
</body>
</html>