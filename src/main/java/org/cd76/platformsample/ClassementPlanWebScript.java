package org.cd76.platformsample;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class ClassementPlanWebScript extends DeclarativeWebScript {
	private static Log logger = LogFactory.getLog(ClassementPlanWebScript.class);

    protected Map<String, Object> executeImpl(
            WebScriptRequest req, Status status, Cache cache) {
        Map<String, Object> model = new HashMap<String, Object>();
        logger.debug("Classement Plan Script called");
        return model;
    }
}
