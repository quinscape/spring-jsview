package de.quinscape.spring.jsview.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Loads servlet context resources.
 * <p>
 *  If the servlet context can be resolved to an actual file system path ("exploded WAR deployment"), the loader will delegate
 *  to a {@link FileResourceLoader} otherwise it will issue non-hot-reloadable {@link StreamResourceHandle}s.
 *  </p>
 */
public class ServletResourceLoader
    implements ResourceLoader
{
    private final static Logger log = LoggerFactory.getLogger(ServletResourceLoader.class);

    private final ServletContext servletContext;

    private final FileResourceLoader fileLoader;

    private final String resourcePath;

    private ConcurrentMap<String, ResourceHandle<?>> resourceHandles = new ConcurrentHashMap<>();

    public ServletResourceLoader(ServletContext servletContext, String resourcePath, boolean recursive) throws IOException
    {
        String basePath = servletContext.getRealPath(resourcePath);
        boolean useFileAccess = basePath != null;


        if (log.isInfoEnabled())
        {
            log.info(
                "Creating {} ServletResourceLoader for resource path '{}' (recursive = {})",
                    useFileAccess ? "hot-reload" : "stream",
                    resourcePath,
                    recursive
            );
        }

        this.servletContext = servletContext;
        this.resourcePath = resourcePath;

        if (useFileAccess)
        {
            fileLoader = new FileResourceLoader(basePath, recursive);
        }
        else
        {
            fileLoader = null;
        }
    }


    @Override
    public <T> ResourceHandle<T> getResourceHandle(
        String path, ResourceConverter<T> converter
    )
    {
        if (path == null)
        {
            throw new IllegalArgumentException("path can't be null");
        }

        if (converter == null)
        {
            throw new IllegalArgumentException("loader can't be null");
        }

        if (fileLoader != null)
        {
            return fileLoader.getResourceHandle(path, converter);
        }

        final ResourceHandle<T> handle = new StreamResourceHandle(servletContext, resourcePath + ensureLeadingSlash(path), converter);

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

    private String ensureLeadingSlash(String path)
    {
        if (path.startsWith("/"))
        {
            return path;
        }
        return "/" + path;
    }

    @Override
    public void shutDown()
    {
        if (fileLoader != null)
        {
            fileLoader.shutDown();
        }
    }
}
