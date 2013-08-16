// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.commons;

import java.io.File;

/**
 * Defines the version information (and a bit more) of
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 16/08/13
 */
public class TermSuiteVersion {

    /** Current version of the program */
    public static final String VERSION = "1.5-SNAPSHOT";
    /** Title */
    public static final String TITLE = "Term Suite";
    /** Summary description of TermSuite */
    public static final String SUMMARY =
            "This tool provides 3 tools for processing terminology extraction "
            + "and terminology alignment from comparable corpora. "
            + "It has been developed within the European TTC project (see http://www.ttc-project.eu/).";
    /** License */
    public static final String LICENSE =
            "This software is distributed under the Apache Licence version 2.";

    /** Name of the directory where the config is persisted */
    public static final String CFG_ROOT = System.getProperty("user.home")
                    + File.separator + ".term-suite" + File.separator + VERSION;
    /** Name of the file where the SpotterController config is persisted */
    public static final String CFG_SPOTTER = CFG_ROOT + File.separator + "spotter.settings";
    /** Name of the file where the Indexer config is persisted */
    public static final String CFG_INDEXER = CFG_ROOT + File.separator + "indexer.settings";
    /** Name of the file where the Aligner config is persisted */
    public static final String CFG_ALIGNER = CFG_ROOT + File.separator + "aligner.settings";

}
