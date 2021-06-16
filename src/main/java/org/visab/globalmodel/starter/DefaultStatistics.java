package org.visab.globalmodel.starter;

import org.visab.globalmodel.IStatistics;

public class DefaultStatistics implements IStatistics {

    private String json;

    public DefaultStatistics(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }

}
