package org.server.utility.managers;

import org.common.interaction.Request;
import org.common.interaction.Response;

import java.util.concurrent.Callable;

public class ProcessRequestHandler implements Callable<Response> {
    public Request request;

    public ProcessRequestHandler(Request request) {
        this.request = request;
    }

    @Override
    public Response call() {
        return CommandManager.go(request);
    }


}
