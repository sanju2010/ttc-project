// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.config;

import org.apache.uima.resource.metadata.ConfigurationParameter;

import java.util.ArrayList;

/**
 * This class represents a group of parameters as a logic grouping of several parameters with:
 * <ul>
 *     <li>a group name</li>
 *     <li>eventually a checkbox toggles that enable/disable the other parameters</li>
 *     <li>a collection of parameters</li>
 * </ul>
 *
 * @author grdscarabe
 * @date 11/06/13
 */
public class GroupOfParameters {

    /** Name of the group of parameters */
    private final String groupName;
    /** Description of what the group of parameters consists of */
    private final String groupDescription;

    /** Flag indicating if the group of parameters can be enabled/disabled */
    private boolean hasHeadToggle;
    /** Label of the checkbox to enable/disable the group */
    private String headToggleLabel;
    /** Description of the toggle */
    private String headToggleDescription;

    /** List of the parameters in this group */
    private ArrayList<ConfigurationParameter> parameters;

    /**
     * Creates a new group of parameters with given name.
     *
     * @param label
     *          The name given to the group.
     * @param description
     *          A description of what the group of parameters is meant to be
     */
    public GroupOfParameters(String label, String description) {
        this.groupName = label;
        this.groupDescription = description;
        this.hasHeadToggle = false;
    }

    /**
     * Add an optional head toggle that enable/disable the parameters of this group.
     *
     * @param label
     *      label of the checkbox
     * @param description
     *      description of what the enabling/disabling does
     */
    public void setToggle(String label, String description) {
        this.hasHeadToggle = true;
        this.headToggleLabel = label;
        this.headToggleDescription = description;
    }

    /**
     * Adds a new {@link ConfigurationParameter} to the group.
     *
     * @param p
     *         the ConfigurationParameter to add to the group
     */
    public void addParameter(ConfigurationParameter p) {
        this.parameters.add(p);
    }


    public String getName() {
        return groupName;
    }


}
