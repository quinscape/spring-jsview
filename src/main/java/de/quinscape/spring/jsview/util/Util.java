package de.quinscape.spring.jsview.util;

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

}
