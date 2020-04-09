<#import "parts/common.ftlh" as c>

<@c.page>
    User editor

    <form action="/user" method="post">
        <label>
            <input type="text" name="username" value="${user.username}" readonly>
        </label>
        <div hidden>
            <label><input type="checkbox" name="USER" ${user.roles?seq_contains(USER)?string("checked", "checked")}>${"USER"}
            </label>
        </div>
        <div>
            <label><input type="checkbox" name="ADMIN" ${user.roles?seq_contains(ADMIN)?string("checked", "")}>${"ADMIN"}
            </label>
        </div>
        <div>
            <label><input type="checkbox"
                          name="MODERATOR" ${user.roles?seq_contains(MODERATOR)?string("checked", "")}>${"MODERATOR"}
            </label>
        </div>
        <div>
            <label><input type="checkbox"
                          name="BANNED_USER" ${user.roles?seq_contains(BANNED_USER)?string("checked", "")}>${"BANNED_USER"}
            </label>
        </div>

        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit">Save</button>
    </form>

</@c.page>