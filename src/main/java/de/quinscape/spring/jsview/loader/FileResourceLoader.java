package de.quinscape.spring.jsview.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Hot-reloads resources from a file directory.
 */
public class FileResourceLoader
    implements ResourceChangeListener, ResourceLoader
{
    private final static Logger log = LoggerFactory.getLogger(ServletResourceLoader.class);

    private ConcurrentMap<String, ResourceHandle<?>> resourceHandles = new ConcurrentHashMap<>();

    private final Java7NIOResourceWatcher watchDir;

    private final String basePath;


    public FileResourceLoader(
        String resourcePath,
        boolean recursive
    ) throws IOException
    {
        this.basePath = resourcePath;

        final File baseDir = new File(basePath);
        if (!baseDir.exists())
        {
            throw new IllegalStateException(basePath + " does not exist");
        }

        if (!baseDir.isDirectory())
        {
            throw new IllegalStateException(basePath + " is not a directory");
        }

        watchDir = new Java7NIOResourceWatcher(basePath, recursive);
        watchDir.register(this);
        watchDir.start();
    }


    @Override
    public <T> ResourceHandle<T> getResourceHandle(String path, ResourceConverter<T> converter)
    {
        if (path == null)
        {
            throw new IllegalArgumentException("path can't be null");
        }

        if (converter == null)
        {
            throw new IllegalArgumentException("loader can't be null");
        }

        ResourceHandle<T> handle;

        handle = new FileResourceHandle(new File(basePath, path.replace('/', File.separatorChar)), converter);

        final ResourceHandle<T> existing = (ResourceHandle<T>) resourceHandles.putIfAbsent(path, handle);
        if (existing != null)
        {
            return existing;
        }
        else
        {
            return handle;
        }
    }


    @Override
    public void shutDown()
    {
        if (watchDir != null)
        {
            log.info("Shutting down");

            watchDir.shutDown();
        }
    }


    @Override
    public void onResourceChange(ResourceEvent resourceEvent, String rootPath, String resourcePath)
    {
        log.debug("Resource Event: {} {}", resourceEvent, resourcePath);

        final ResourceHandle<?> handle = resourceHandles.get(resourcePath);
        if (handle != null)
        {
            handle.flush();
        }
    }
}
