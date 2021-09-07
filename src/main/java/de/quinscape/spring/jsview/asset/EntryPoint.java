package de.quinscape.spring.jsview.asset;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONParameter;

import java.util.List;
import java.util.Map;

public class EntryPoint
    extends AbstractDynamicProperties
{
    private List<String> chunks;
    private List<Map<String,Object>> assets;

    public void setChunks(List<String> chunks)
    {
        this.chunks = chunks;
    }


    public void setAssets(List<Map<String,Object>> assets)
    {
        this.assets = assets;
    }


    public List<String> getChunks()
    {
        return chunks;
    }


    public List<Map<String,Object>> getAssets()
    {
        return assets;
    }
}
