<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    You have an ability to ban the user
    <br>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th></th>
        </tr>
        </thead>
        <tbody>

        <#list users as user>
            <#if user.isAdmin()==false>
                <#if user.isModerator()==false>
                    <tr>
                        <td>${user.username}</td>
                        <td><#list user.roles as role>${role}<#sep>, </#list></td>
                        <td><a href="/banneduser/${user.id}">edit</a></td>
                    </tr>
                </#if>
            </#if>
        </#list>

        </tbody>
    </table>
</@c.page>