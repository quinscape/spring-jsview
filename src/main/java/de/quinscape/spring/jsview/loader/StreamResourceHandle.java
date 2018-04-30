package de.quinscape.spring.jsview.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.InputStream;

/**
 * Stream-based, non-reloadable resource handle.
 *
 * @param <T>   Type of final content
 */
public class StreamResourceHandle<T>
    implements ResourceHandle<T>
{
    private final static Logger log = LoggerFactory.getLogger(StreamResourceHandle.class);

    private final T content;

    public StreamResourceHandle(ServletContext servletContext, String path, ResourceConverter<T> processor)
    {
        if (log.isDebugEnabled())
        {
            log.debug("Create StreamResourceHandle for {} (processor = {})", path, processor);
        }

        final InputStream is = servletContext.getResourceAsStream(path);
        content = processor.readStream(is);
    }


    @Override
    public boolean isWritable()
    {
        return false;
    }


    @Override
    public T getContent()
    {
        return content;
    }

    @Override
    public void update(T newValue)
    {
        throw new UnsupportedOperationException("Stream resources should never be updated");
    }


    @Override
    public void flush()
    {
        throw new UnsupportedOperationException("Stream resource handle should never be flushed");
    }
}
