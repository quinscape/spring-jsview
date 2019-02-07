package de.quinscape.spring.jsview.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Util
{
    public static List<String> split(String s, String separator)
    {
        StringTokenizer tokenizer = new StringTokenizer(s, separator);
        List<String> list = new ArrayList<>();
        while (tokenizer.hasMoreElements())
        {
            list.add(tokenizer.nextToken().trim());
        }
        return list;
    }


    /**
     * Replaces the system dependent File separator character with slash (<code>'/'</code>)
     * @param s     path
     *              
     * @return  path with slashes
     */
    public static String path(String s)
    {
        return s.replace(File.separatorChar, '/');
    }
}
