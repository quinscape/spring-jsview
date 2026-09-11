package de.quinscape.spring.jsview.util;

import org.svenson.JSON;
import org.svenson.JSONParser;
import org.svenson.TypeAnalyzer;
import org.svenson.info.JSONClassInfo;
import org.svenson.info.JSONPropertyInfo;
import org.svenson.info.JavaObjectPropertyInfo;
import org.svenson.info.JavaObjectSupport;
import org.svenson.util.JSONBeanUtil;
import org.svenson.util.JSONBuilder;

import java.lang.annotation.Annotation;

/**
 * JSON helper utils methods.
 */
public class JSONUtil
{
    public static String formatJSON(String s)
    {
        return JSON.formatJSON(s);
    }

    private final static String OK_RESPONSE = "{\"ok\":true}";

    public final static JavaObjectSupport OBJECT_SUPPORT = new JavaObjectSupport();
    public final static JSON DEFAULT_GENERATOR;
    public final static JSONParser DEFAULT_PARSER;
    public final static JSONBeanUtil DEFAULT_UTIL;
    static
    {
        final JSONParser jsonParser = new JSONParser();
        jsonParser.setObjectSupport(OBJECT_SUPPORT);
        JSONBeanUtil util = new JSONBeanUtil();
        util.setObjectSupport(OBJECT_SUPPORT);
        DEFAULT_PARSER = jsonParser;
        DEFAULT_UTIL = util;
        // Not JSON.defaultJSON(): that is svenson's process-wide singleton and it builds its own
        // JavaObjectSupport. TypeAnalyzer caches class info by class alone rather than per support, so
        // whichever support analyzes a type first answers for every caller of that type. Generating
        // through the same support the parser and the bean util use keeps that out of the hands of class
        // loading order.
        DEFAULT_GENERATOR = new JSON(OBJECT_SUPPORT, '"');
    }

    public static String ok()
    {
        return OK_RESPONSE;
    }

    public static String error(Throwable err)
    {
        return error(err.getMessage());
    }

    public static String error(String message)
    {
        return error(message, null);
    }

    public static String error(String message, Object payload)
    {
        return JSONBuilder.buildObject()
            .property("ok", false)
            .property("error", message)
            .propertyUnlessNull("detail", payload)
            .output();
    }


    public static <T extends Annotation> T findAnnotation(JSONPropertyInfo propertyInfo, Class<T> annoClass)
    {

        if (!(propertyInfo instanceof JavaObjectPropertyInfo))
        {
            throw new IllegalArgumentException("Invalid property info type: " + propertyInfo + ", must be " + JavaObjectPropertyInfo.class.getName());
        }

        final JavaObjectPropertyInfo info = (JavaObjectPropertyInfo) propertyInfo;

        if (propertyInfo.isReadable())
        {
            final T getterAnno = info.getGetterMethod().getAnnotation(annoClass);
            if (getterAnno != null)
            {
                return getterAnno;
            }
        }

        if (propertyInfo.isWriteable())
        {
            final T setterAnno = info.getSetterMethod().getAnnotation(annoClass);
            if (setterAnno != null)
            {
                return setterAnno;
            }
        }
        
        return null;
    }

    public static JSONClassInfo getClassInfo(Class<?> cls)
    {
        return TypeAnalyzer.getClassInfo(JSONUtil.OBJECT_SUPPORT,cls);
    }

    public static String forValue(Object value)
    {
        return formatJSON(DEFAULT_GENERATOR.forValue(value));
    }
}


