<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.isAdmin()
    isModerator = user.isModerator()
    isBannedUser = user.isBannedUser()
    >
<#else>
    <#assign
    name = "unknown"
    isAdmin = false
    isModerator = false
    isBannedUser = false
    >
</#if>