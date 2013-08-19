// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.config;

import eu.project.ttc.tools.aligner.AlignerModel;
import eu.project.ttc.tools.indexer.IndexerModel;
import eu.project.ttc.tools.spotter.SpotterModel;

import java.io.File;

/**
 * @author grdscarabe
 * @date 23/05/13
 */
public class TermSuiteSettings {

    /** Name of the directory where the config is persisted */
    static final String CFG_ROOTDIR_NAME = ".term-suite";
    /** Name of the file where the SpotterController config is persisted */
    static final String CFG_SPOTTER_NAME = "spotter.settings";
    /** Name of the file where the Indexer config is persisted */
    static final String CFG_INDEXER_NAME = "indexer.settings";
    /** Name of the file where the Aligner config is persisted */
    static final String CFG_ALIGNER_NAME = "aligner.settings";

    /** Path to the configuration directory */
    private final String cfgDirPath;
    /** Actual configuration directory */
    private final File cfgDir;

    /** Path to the SpotterController configuration*/
    private final String cfgSpotterPath;
    /** Instance of SpotterController settings */
    private final SpotterModel cfgSpotter;

    /** Path to the Indexer configuration*/
    private final String cfgIndexerPath;
    /** Instance of Indexer settings */
    private final IndexerModel cfgIndexer;

    /** Path to the Aligner configuration*/
    private final String cfgAlignerPath;
    /** Instance of Aligner settings */
    private final AlignerModel cfgAligner;

    public TermSuiteSettings(String version) {
        // Compute the path to the configuration dir and create it if necessary
        cfgDirPath = System.getProperty("user.home")
                + File.separator + CFG_ROOTDIR_NAME + File.separator + version;
        cfgDir = new File(cfgDirPath);
        if (! cfgDir.exists()) {
            cfgDir.mkdirs();
        }

        // Prepare SpotterController settings
        cfgSpotterPath = cfgDir.getAbsolutePath() + File.separator + CFG_SPOTTER_NAME;
        cfgSpotter = new SpotterModel( new File(cfgSpotterPath) );

        // Prepare Indexer settings
        cfgIndexerPath = cfgDir.getAbsolutePath() + File.separator + CFG_INDEXER_NAME;
        cfgIndexer = new IndexerModel( new File(cfgIndexerPath) );

        // Prepare Aligner settings
        cfgAlignerPath = cfgDir.getAbsolutePath() + File.separator + CFG_ALIGNER_NAME;
        cfgAligner = new AlignerModel( new File(cfgIndexerPath) );
    }

    public SpotterModel getSpotterSettings() {
        return cfgSpotter;
    }

    public IndexerModel getIndexerSettings() {
        return cfgIndexer;
    }

    public AlignerModel getAlignerSettings() {
        return cfgAligner;
    }

}
