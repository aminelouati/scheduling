package org.ow2.proactive_grid_cloud_portal.cli.cmd;

import static org.ow2.proactive_grid_cloud_portal.cli.HttpResponseStatus.OK;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;

public class ResumeSchedulerCommand extends AbstractCommand implements Command {

    public ResumeSchedulerCommand() {
    }

    @Override
    public void execute() throws Exception {
        HttpPut request = new HttpPut(resourceUrl("resume"));
        HttpResponse response = execute(request);
        if (statusCode(OK) == statusCode(response)) {
            boolean success = readValue(response, Boolean.TYPE);
            if (success) {
                writeLine("Scheduler successfully resumed.");
            } else {
                writeLine("Cannot resume scheduler.");
            }
        } else {
            handleError(
                    "An error occurred while attempting to resume scheduler:",
                    response);
        }
    }

}
