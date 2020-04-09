<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <#if name = "unknown" >
    <h5>Hello, guest</h5>
    </#if>

    <#if user??>
        <h5>Hello, ${name} </h5>
    </#if>

    <div>This is a simple clone of Twitter</div>
    <#if name = "unknown" >
    <div> <a href = "/login"> Перейти на страницу логина </a> </div>
    </#if>
</@c.page>