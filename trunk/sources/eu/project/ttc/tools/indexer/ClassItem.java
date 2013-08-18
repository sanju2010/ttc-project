// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.indexer;

/**
 * @author grdscarabe
 * @date 17/08/13
 */
public class ClassItem {

    private final String className;
    private final String name;

    public ClassItem(String className, String name) {
        this.className = className;
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }
}
