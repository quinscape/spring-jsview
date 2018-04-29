package de.quinscape.spring.jsview.asset;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONParameter;

import java.util.List;

public class EntryPoint
    extends AbstractDynamicProperties
{
    private List<String> chunks;
    private List<String> assets;

    public void setChunks(List<String> chunks)
    {
        this.chunks = chunks;
    }


    public void setAssets(List<String> assets)
    {
        this.assets = assets;
    }


    public List<String> getChunks()
    {
        return chunks;
    }


    public List<String> getAssets()
    {
        return assets;
    }
}
