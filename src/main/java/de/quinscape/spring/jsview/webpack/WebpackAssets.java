package de.quinscape.spring.jsview.webpack;

import org.svenson.JSONParameters;
import org.svenson.JSONTypeHint;

import java.util.Map;

public class WebpackAssets
{

    private final Map<String,EntryPoint> entryPoints;

    public WebpackAssets(
        @JSONParameters
        @JSONTypeHint(EntryPoint.class)
        Map<String, EntryPoint> entryPoints
    )
    {
        this.entryPoints = entryPoints;
    }


    public Map<String, EntryPoint> getEntryPoints()
    {
        return entryPoints;
    }
}
