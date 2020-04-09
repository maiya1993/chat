<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <label>
                    <input type="text" name="filter" class="form-control" value="${filter?ifExists}"
                           placeholder="Search by tag">
                </label>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <p align="center" style="color:#0f0009; font-size:22px"> Общий чат</p>

    <#list messages as message>

        <div class="card-footer text-muted">
            <p> ${message.authorName} написал(а):</p>
        </div>
        <#if message.filename??>
            <img src="/img/${message.filename}" alt="file" class="card-img-top">
        </#if>
        <div class="m-2">
            <span>${message.text}</span>
            <i>${message.tag}</i>
        </div>

        <#if isAdmin || isModerator>

            <form action="del" method="post">
                <div hidden>
                <label>
                    <input type="text" name="id" value="${message.id}" readonly>
                </label>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Delete this message</button>
            </form>

        </#if>

    <#else>
        No message
    </#list>

    <br>
    <br>

    <#if isBannedUser>
        <p align="center" style="color:#0f0009; font-size:16px"> К сожалению, Ваш аккаунт заблокирован, Вы не можете
            отправлять сообщения </p>
    </#if>

    <#if !isBannedUser>
        <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            Add new Message
        </a>
        <div class="collapse" id="collapseExample">
            <div class="form-group mt-3">
                <form method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <input type="text" class="form-control" name="text" placeholder="Введите сообщение..."/>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="tag" placeholder="Тэг">
                    </div>
                    <div class="form-group">
                        <div class="custom-file">
                            <input type="file" name="file" id="customFile">
                            <label class="custom-file-label" for="customFile">Choose file</label>
                        </div>
                    </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Отправить сообщение</button>
                    </div>
                </form>
            </div>
        </div>
    </#if>

    <p align="right" style="color:#0f0009; font-size:14px"> All rights reserved (c) </p>

</@c.page>