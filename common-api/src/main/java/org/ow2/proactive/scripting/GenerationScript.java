/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2011 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.ow2.proactive.scripting;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.objectweb.proactive.annotation.PublicAPI;
import org.apache.log4j.Logger;


/**
 * This script can return the command that have to be executed as Task.
 *
 * @see org.ow2.proactive.scheduler.task.NativeExecutable
 * @author The ProActive Team
 * @since 3.9
 */
@PublicAPI
@XmlAccessorType(XmlAccessType.FIELD)
public class GenerationScript extends Script<Object> {
    /** Loggers */
    public static final Logger logger = Logger.getLogger(GenerationScript.class);

    /**
     * The variable name which must be set after the evaluation
     * of a verifying script.
     */
    public static final String RESULT_VARIABLE = "command";

    /**
     * The variable name which must be set after the evaluation
     * of a verifying script.
     */
    public static final String RESULTLIST_VARIABLE = "commandList";

    /**
     * Default value for GenerationScript return value
     */
    public static final List<String> DEFAULT_COMMAND_VALUE = new ArrayList<String>();

    /** ProActive needed constructor */
    public GenerationScript() {
    }

    /** Constructor that set the script.
     * @param script already instantiated script object.
     * @throws InvalidScriptException if the Constructor fails.
     */
    public GenerationScript(Script<?> script) throws InvalidScriptException {
        super(script);
    }

    public GenerationScript(String script, String engineName, String[] parameters)
            throws InvalidScriptException {
        super(script, engineName, parameters, "GenerationScript");
    }

    @Override
    protected String getDefaultScriptName() {
        return "GenerationScript";
    }

    /** Constructor. Directly create a script with a string.
     * @param script representing the source code of the script
     * @param engineName engine name used to interpret the script.
     * @throws InvalidScriptException if the creation fails.
     */
    public GenerationScript(String script, String engineName) throws InvalidScriptException {
        super(script, engineName, "GenerationScript");
    }

    /** Create a script from a file.
     * @param file containing the source code
     * @param parameters scripts parameters (arguments)
     * @throws InvalidScriptException if the creation fails.
     */
    public GenerationScript(File file, String[] parameters) throws InvalidScriptException {
        super(file, parameters);
    }

    /** Create a script from an URL.
     * @param url containing the source code
     * @param parameters scripts parameters (arguments)
     * @throws InvalidScriptException if the creation fails.
     */
    public GenerationScript(URL url, String[] parameters) throws InvalidScriptException {
        super(url, parameters);
    }

    /**
     * Return the script id
     * @see org.ow2.proactive.scripting.Script#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Return a stream's reader associated to this script.
     * @see org.ow2.proactive.scripting.Script#getReader()
     */
    @Override
    protected Reader getReader() {
        return new StringReader(script);
    }

    /**
     * @see org.ow2.proactive.scripting.Script#getResult(javax.script.Bindings)
     */
    @Override
    protected ScriptResult<Object> getResult(Bindings bindings) {
        if (bindings.containsKey(RESULT_VARIABLE)) {
            Object result = bindings.get(RESULT_VARIABLE);

            if (result instanceof CharSequence) {
                return new ScriptResult<Object>(result);
            } else {
                String msg = "Bad result format : awaited String, found " + result.getClass().getName();
                logger.warn(msg);
                return new ScriptResult<Object>(new Exception(msg));
            }
        } else if (bindings.containsKey(RESULTLIST_VARIABLE)) {
            Object resultList = bindings.get(RESULTLIST_VARIABLE);

            if (resultList instanceof List) {
                return new ScriptResult<Object>(resultList);
            } else {
                String msg = "Bad result format : awaited String[], found " + resultList.getClass().getName();
                logger.warn(msg);
                return new ScriptResult<Object>(new Exception(msg));
            }
        } else {
            String msg = "No binding for key " + RESULT_VARIABLE;
            logger.warn(msg);
            return new ScriptResult<Object>(new Exception(msg));
        }
    }

    /**
     * Prepare the script's special bindings; string that represents the selected native command
     * to launch .
     * @see org.ow2.proactive.scripting.Script#prepareSpecialBindings(javax.script.Bindings)
     */
    @Override
    protected void prepareSpecialBindings(Bindings bindings) {
        bindings.put(RESULTLIST_VARIABLE, DEFAULT_COMMAND_VALUE);
    }
}
