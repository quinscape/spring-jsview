package de.quinscape.spring.jsview.loader;

public interface ResourceLoader
{
    <T> ResourceHandle<T> getResourceHandle(String path, ResourceConverter<T> loader);

    void shutDown();
}
