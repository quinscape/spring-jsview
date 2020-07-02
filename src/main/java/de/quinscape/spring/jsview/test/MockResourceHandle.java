package de.quinscape.spring.jsview.test;

import de.quinscape.spring.jsview.loader.ResourceHandle;

import java.io.IOException;

/**
 * Simple no-op resource handle for tests. Just wraps the payload as-is.
 * 
 * @param <T>   payload type
 */
public class MockResourceHandle<T>
    implements ResourceHandle<T>
{
    private final T value;


    public MockResourceHandle(T value)
    {
        this.value = value;
    }


    @Override
    public boolean isWritable()
    {
        return false;
    }


    @Override
    public T getContent() throws IOException
    {
        return value;
    }


    @Override
    public void update(T t) throws IOException
    {
        throw new UnsupportedOperationException("Mock handle can't be updated");
    }


    @Override
    public void flush()
    {

    }
}
