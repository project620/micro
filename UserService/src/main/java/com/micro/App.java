package com.micro;

import com.ctrip.framework.apollo.ConfigService;
import com.micro.controller.AuthorController;
import com.micro.controller.ConfigController;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ConfigService.getAppConfig();
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.route().handler(LoggerHandler.create());

        new ConfigController(router);
        new AuthorController(router);

        vertx.createHttpServer().requestHandler(router::accept).listen(8088, lsRs -> {
            if (lsRs.succeeded()) {
                logger.info("启动成功，监听8088");
            } else {
                logger.error("启动失败", lsRs.cause());
                vertx.close();
            }
        });

    }

}
