package de.quinscape.spring.jsview.loader;


public interface ResourceChangeListener
{
    void onResourceChange(ResourceEvent resourceEvent, String rootPath, String resourcePath);
}
