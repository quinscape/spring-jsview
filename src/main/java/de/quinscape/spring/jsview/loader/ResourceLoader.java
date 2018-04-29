package de.quinscape.spring.jsview.loader;

/**
 * Resource loader abstraction.
 */
public interface ResourceLoader
{
    /**
     * Returns a resource handle for the given relative path and resource loader
     *
     * @param path          relative servlet resource path
     * @param converter     converter implementation
     *
     * @param <T>       payload type
     *           
     * @return resource handle
     */
    <T> ResourceHandle<T> getResourceHandle(String path, ResourceConverter<T> converter);

    /**
     * Shutdowns down the resource loader.
     */
    void shutDown();
}
