package de.quinscape.spring.jsview.loader;

import java.io.InputStream;

/**
 * Implemented by classes converting {@link ResourceLoader} resource streams into a Java Type and potentially back.
 *
 * @param <T>   resource payload type
 */
public interface ResourceConverter<T>
{
    /**
     * Reads the given input stream and converts the data to the resource payload type.
     *
     * @param inputStream      input stream
     *
     * @return payload
     */
    T readStream(InputStream inputStream);


    /**
     * Converts the payload data back into a byte array.
     *
     * @param value     payload data
     *
     * @return  byte array
     *
     * @throws UnsupportedOperationException if the converter does not support back conversion.
     */
    byte[] toByteArray(T value);
}
