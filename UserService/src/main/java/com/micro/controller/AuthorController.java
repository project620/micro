package com.micro.controller;

import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.micro.entity.Author;
import com.micro.util.DBUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;


public class AuthorController {

    private Router router;

    private static final ResultSetHandler<Author> handler = new BeanHandler<>(Author.class);

    public AuthorController(Router router) {
        this.router = router;
        init();
    }

    private void init(){
        router.get("/author/:authorId").handler(this::handleGet);
        router.put("/author/:authorId").handler(this::handlePut);
    }

    private void handleGet(RoutingContext context) {
        String authorId = context.request().getParam("authorId");
        String sql = "select * from Author where id = ?";
        Author author = (Author) DBUtil.executeQuery(handler, sql, Long.valueOf(authorId));
        JsonObject rs = JsonObject.mapFrom(author);
        context.response().end(rs.encode());
    }

    private void handlePut(RoutingContext context) {
        String authorId = context.request().getParam("authorId");
        String sql = null;
        JsonObject body = context.getBodyAsJson();
        int row = 0;
        if (StringUtils.isEmpty(authorId)) {
            sql = "insert into Author(name, age, version) values (?, ?, 1)";
            row = DBUtil.executeUpdate(sql, body.getString("name"), body.getString("age"));
        } else {
            sql = "select * from Author where id = ?";
            Author author = (Author) DBUtil.executeQuery(handler, sql, Long.valueOf(authorId));
            JsonObject original = JsonObject.mapFrom(author);
            original.mergeIn(body);
            sql = "update Author set name = ?, age = ?, version = version + 1 where id = ? and version = ?";
            row = DBUtil.executeUpdate(sql,
                    body.getString("name"),
                    body.getInteger("age"),
                    Long.valueOf(authorId),
                    body.getLong("version"));
        }
        if (row == 1) {
            context.response().end("update success");
        } else {
            context.response().end("update fail");
        }
    }

}
