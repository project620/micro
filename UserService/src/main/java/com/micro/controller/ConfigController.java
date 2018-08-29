package com.micro.controller;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ConfigController {

    private Router router;

    private Config config = ConfigService.getAppConfig();

    public ConfigController(Router router) {
        this.router = router;
        init();
    }

    private void init(){
        router.get("/config/:key").handler(this::handleGet);
    }


    public void handleGet(RoutingContext routingContext) {
        String key = routingContext.request().getParam("key");
        String value = config.getProperty(key, "");
        routingContext.response().end(value);
    }

}
