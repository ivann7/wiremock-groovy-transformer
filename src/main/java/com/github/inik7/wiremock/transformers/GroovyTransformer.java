package com.github.inik7.wiremock.transformers;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.IOException;

public class GroovyTransformer extends ResponseDefinitionTransformer {
    private static final String NAME = "groovy-transformer";

    public GroovyTransformer() {
    }

    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource, Parameters parameters) {
        try {
            GroovyScriptEngine gse = new GroovyScriptEngine(fileSource.getPath());
            String scriptName = responseDefinition.getBodyFileName();
            gse.loadScriptByName(scriptName);
            return ResponseDefinitionBuilder.like(responseDefinition).withBody(gse.run(scriptName, "")).build();
        } catch (ResourceException | ScriptException | IOException var7) {
            var7.printStackTrace();
            return responseDefinition;
        }
    }

    public String getName() {
        return "groovy-transformer";
    }

    public boolean applyGlobally() {
        return false;
    }
}
