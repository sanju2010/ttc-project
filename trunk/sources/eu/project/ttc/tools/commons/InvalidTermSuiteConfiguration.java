// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.commons;

/**
 * This exception is thrown when the configuration of TermSuite is invalid
 * and so the process cannot continue.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 15/08/13
 */
public class InvalidTermSuiteConfiguration extends Exception {

    public InvalidTermSuiteConfiguration(String msg, Exception e) {
        super(msg);
        initCause(e);
    }
}
