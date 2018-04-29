package de.quinscape.spring.jsview.template;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class BaseTemplateTest
{
    @Test
    public void testTemplate() throws Exception
    {
        assertThat(render("ABC", null), is("ABC"));
        assertThat(render("[$A]", map("A", "foo")), is("[foo]"));
        assertThat(render("[$a]", map("A", "foo")), is("[$a]"));
        assertThat(render("$A]", map("A", "foo")), is("foo]"));
        assertThat(render("[$A", map("A", "foo")), is("[foo"));
        assertThat(render("$A", map("A", "foo")), is("foo"));
        assertThat(render("1 $A 2 $B 3", map("A", "foo", "B", "bar")), is("1 foo 2 bar 3"));
        assertThat(render("$A$B", map("A", "foo", "B", "bar")), is("foobar"));
        assertThat(render("\\$A", map("A", "foo")), is("$A"));
        assertThat(render("\\\\$A", map("A", "foo")), is("\\$A"));                                                              

    }
    private static Map<String,Object> map(String... keysAndValues)
    {
        if ( (keysAndValues.length & 1) != 0)
        {
            throw new IllegalArgumentException("Map arguments must be an even number: key, value pairs");
        }

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            map.put(keysAndValues[i], keysAndValues[i + 1]);
        }
        return map;
    }


    private String render(String s, Map<String, Object> map) throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new BaseTemplate(s).write(os, map);
        return new String(os.toByteArray(),BaseTemplate.UTF_8);
    }
}
