<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>

    <table>
        <thead>
        <tr>
            <th>Список пользователей: </th>
        </tr>
        </thead>
        <br>
        <tbody>
        <#list users as user>
            <#if name != user.username>
                <tr>
                    <td>${user.username}</td>
                </tr>
            </#if>
        </#list>
        </tbody>
    </table>

    <br>
    <br>

    <#if !isBannedUser>
        <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            Add new Room
        </a>
        <div class="collapse" id="collapseExample">
            <div class="form-group mt-3">
                <form method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <input type="text" class="form-control" name="name" placeholder="Введите название комнаты"/>
                    </div>
                    <div class="form-group">
                        <p><input name="isPrivate" type="radio" value="false"> Публичная</p>
                        <p><input name="isPrivate" type="radio" value="true"> Приватная </p>
                    </div>

                    <div class="form-group">
                        <input type="text" class="form-control" name="usersList"
                               placeholder="Добавьте сюда пользователей через запятую">
                    </div>

                    <div class="form-group">
                        <p style="color:#0f0009; font-size:16px"> *Внимание: создатель комнаты добавляется в неё автоматически </p>
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Создать комнату</button>
                    </div>
                </form>
            </div>
        </div>
    </#if>
    <br>
    ${ifNameRoomExist?ifExists}
    ${errorName?ifExists}
    <br>
    <p align="center" style="color:#0f0009; font-size:22px"> Список действующих комнат</p>
    <br>
    <br>

<#--    Для админа видны все приватные комнаты и любую можно удалить и переименовать -->
    <#list rooms as room>
        <#if isAdmin>
            <div class="card-footer text-muted">
                <p> ${room.authorName} создал(а) новую приватную комнату:</p>
            </div>

            <div class="m-2">
                <span> <a href="privateRoom/${room.name}" target="_blank"> ${room.name} </a> </span>
            </div>

            <form action="deleteRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="idRoom" value="${room.id}" readonly>
                    </label>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Delete this room</button>
            </form>

            <br>

            <form action="roomRename" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="oldRoom" value="${room.name}" readonly>
                    </label>
                </div>
                    <label>
                        <input type="text" class="form-control" name="newRoom" placeholder="Введите новое название комнаты"/>
                    </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Rename this room</button>
            </form>

            <br>

            <form action="addUserInRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="nameOfRoom" value="${room.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="newUsers" placeholder="Введите имена пользователей"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Add users in room</button>
            </form>

            <br>

            <form action="deleteUserFromRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="nameOfRoom" value="${room.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="deleteUsers" placeholder="Введите имена пользователей"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Delete users from room</button>
            </form>

        </#if>

    <#else>
        No private rooms
    </#list>


<#--    Для админа видны все публичные комнаты и любую можно удалить и переименовать -->
    <#list publicRooms as publicRoom>
        <#if isAdmin>
            <div class="card-footer text-muted">
                <p> ${publicRoom.authorName} создал(а) новую публичную комнату:</p>
            </div>

            <div class="m-2">
                <span> <a href="chat/${publicRoom.name}" target="_blank"> ${publicRoom.name} </a> </span>
            </div>

            <form action="deleteRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="idRoom" value="${publicRoom.id}" readonly>
                    </label>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Delete this room</button>
            </form>

            <br>

            <form action="roomRename" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="oldRoom" value="${publicRoom.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="newRoom" placeholder="Введите новое название комнаты"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Rename this room</button>
            </form>

            <br>

            <form action="addUserInRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="nameOfRoom" value="${publicRoom.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="newUsers" placeholder="Введите имена пользователей"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Add users in room</button>
            </form>

            <br>

            <form action="deleteUserFromRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="nameOfRoom" value="${publicRoom.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="deleteUsers" placeholder="Введите имена пользователей"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Delete users from room</button>
            </form>

        </#if>

    <#else>
        No public rooms
    </#list>

<#--    Обычный пользователь может видеть и удалять только свои приватные комнаты-->
    <#list rooms as room>
        <#if name == room.authorName && !isAdmin>
            <div class="card-footer text-muted">
                <p> ${room.authorName} создал(а) новую приватную комнату:</p>
            </div>

            <div class="m-2">
                <span> <a href="privateRoom/${room.name}" target="_blank"> ${room.name} </a> </span>
            </div>

            <form action="deleteRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="idRoom" value="${room.id}" readonly>
                    </label>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Delete this room</button>
            </form>

            <br>

            <form action="roomRename" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="oldRoom" value="${room.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="newRoom" placeholder="Введите новое название комнаты"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Rename this room</button>
            </form>

            <br>

            <form action="addUserInRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="nameOfRoom" value="${room.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="newUsers" placeholder="Введите имена пользователей"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Add users in room</button>
            </form>

            <br>

            <form action="deleteUserFromRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="nameOfRoom" value="${room.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="deleteUsers" placeholder="Введите имена пользователей"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Delete users from room</button>
            </form>

        </#if>
    </#list>

<#--    Обычный пользователь может видеть и удалять только свои публичные комнаты-->
    <#list publicRooms as publicRoom>
        <#if name == publicRoom.authorName && !isAdmin>
            <div class="card-footer text-muted">
                <p> ${publicRoom.authorName} создал(а) новую публичную комнату:</p>
            </div>

            <div class="m-2">
                <span> <a href="chat/${publicRoom.name}" target="_blank"> ${publicRoom.name} </a> </span>
            </div>

            <form action="deleteRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="idRoom" value="${publicRoom.id}" readonly>
                    </label>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Delete this room</button>
            </form>

            <br>

            <form action="roomRename" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="oldRoom" value="${publicRoom.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="newRoom" placeholder="Введите новое название комнаты"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Rename this room</button>
            </form>

            <br>

            <form action="addUserInRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="nameOfRoom" value="${publicRoom.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="newUsers" placeholder="Введите имена пользователей"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Add users in room</button>
            </form>

            <br>

            <form action="deleteUserFromRoom" method="post">
                <div hidden>
                    <label>
                        <input type="text" name="nameOfRoom" value="${publicRoom.name}" readonly>
                    </label>
                </div>
                <label>
                    <input type="text" class="form-control" name="deleteUsers" placeholder="Введите имена пользователей"/>
                </label>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Delete users from room</button>
            </form>

        </#if>
    </#list>

<#--    Пользователь, которые не создавал свои приватные комнаты, может видеть те комнаты, куда его пригласили -->
    <#list privateRooms as privateRoom>
        <#if name != privateRoom.authorName && !isAdmin>
            <div class="card-footer text-muted">
                <p> ${privateRoom.authorName} создал(а) новую приватную комнату:</p>
            </div>

            <div class="m-2">
                <span> <a href="privateRoom/${privateRoom.name}" target="_blank"> ${privateRoom.name} </a> </span>
            </div>

        </#if>

    </#list>

<#--    Пользователь, которые не создавал свои публичные комнаты, может видеть те комнаты, куда его пригласили -->
    <#list publicRoomsOfUser as publicRoomOfUser>
        <#if name != publicRoomOfUser.authorName && !isAdmin>
            <div class="card-footer text-muted">
                <p> ${publicRoomOfUser.authorName} создал(а) новую публичную комнату:</p>
            </div>

            <div class="m-2">
                <span> <a href="chat/${publicRoomOfUser.name}" target="_blank"> ${publicRoomOfUser.name} </a> </span>
            </div>

        </#if>

    </#list>

    <br>


</@c.page>