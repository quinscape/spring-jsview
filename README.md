# Spring-JsView

A Javascript view system for Spring WebMVC and Webpack

## Motivation

Modern Javascript UI libraries are often preferable over Java server-side technologies. 
Integrating a Javascript client side view implementation with optional data preloading
offers an attractive way to use Javascript SPA content as UI frontend for a Spring 
Boot/WebMVC application and integrate that with the existing Java and Spring eco system.

 
## Usage

On the server-side, this library provides a ViewResolver that needs to be configured
to serve Javascript views.

```java
import de.quinscape.spring.jsview.JsViewResolver;
import de.quinscape.spring.jsview.ModelMapProvider;
import de.quinscape.spring.jsview.loader.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class WebConfiguration
    implements WebMvcConfigurer
{
    private final ResourceLoader resourceLoader;


    @Autowired
    public WebConfiguration(
        ResourceLoader resourceLoader
    )
    {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry)
    {


        registry.viewResolver(
            new JsViewResolver(
                Collections.singleton(
                    new ModelMapProvider()
                ),
                resourceLoader,
                 "WEB-INF/template.html"
            )
        );
    }
}
```

## Controller

Since we're integrated into the Spring WebMVC view system, we can define easily controller methods using those views.

```java
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JsEntryPointController
{
    @RequestMapping("/example/**")
    public String exampleApp(ModelMap modelMap)
    {
        return "main";
    }
}
```

Here we serve the end-point "main" under "/example/\*\*". The end point can render a single view or use a client-side router
to display states withing the "/example/\*\*" URI space.  


### Base template

The base template is a HTML file with simple placeholder replacement

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    $ASSETS

    <link rel="stylesheet" type="text/css" href="$CONTEXT_PATH/css/fontawesome-all.min.css" />
    <link rel="stylesheet" type="text/css" href="$CONTEXT_PATH/css/bootstrap.min.css" />

    <script id="root-data" type="x-application/view-data">
        $VIEW_DATA
    </script>
</head>
<body>
<div id="root">
</div>
</body>
</html>
```

There is some very simple placeholder variables that get replaced :

 * CONTEXT_PATH : The current servlet context path of the application
 * VIEW_DATA : An optional view data JSON block 
 * ASSETS : the webpack build assets for the current end point 
 
## Webpack configuration

### Example configuration

Complete webpack example with babel, mini-css-extract-plugin for local component styles ( e.g. `const myCSS = require('./my.css')` )
and two entry points `main` and `test`.

This example assumes a maven build structure with the Javascript sources under src/main/js/.

You might need to adjust the target-directory and replace `"target/my-project-0.1/js/"` with the correct target.  

```js
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const JsViewPlugin = require("jsview-webpack-plugin");
const path = require("path");
const webpack = require("webpack");

const PRODUCTION = (process.env.NODE_ENV === "production");

module.exports = {
    mode: process.env.NODE_ENV,
    entry: {
        main: "./src/main/js/main.js",
        test: "./src/main/js/test.js"
    },
    
    devtool: "sourcemap",

    output: {
        path: path.join(__dirname, "target/my-project-0.1/js/"),
        filename: "bundle-[name]-[chunkhash].js",
        chunkFilename: "bundle-[id]-[chunkhash].js",
    },
    plugins: [
        new MiniCssExtractPlugin({
            filename: "bundle-[name]-[chunkhash].css",
            chunkFilename: "bundle-[id]-[chunkhash].css"
        }),

        new webpack.DefinePlugin({
            "__PROD": PRODUCTION,
            "__DEV": !PRODUCTION,
            "process.env.NODE_ENV": JSON.stringify(PRODUCTION ? "production" : "development")
        }),

        // clean old assets and generate webpack-assets.json
        new JsViewPlugin()
    ],

    module: {
        rules: [                                                                                                                                   
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader"
                }
            },
            {
                test: /\.css$/,
                exclude: /node_modules/,
                use: [MiniCssExtractPlugin.loader, "css-loader"]
            }
        ]
    },

    optimization: {
        splitChunks: {
            cacheGroups: {
                commons: { test: /[\\/]node_modules[\\/]/, name: "vendors", chunks: "all" }
            }
        }
    }
};

```

# View Data Providers

The JsViewDataProvider interface can be used to provide named java objects that get converted into JSON via
svenson.

```java 
package de.quinscape.spring.jsview;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Implemented by classes providing preloaded data to js views.
 */
public interface JsViewDataProvider
{
    /**
     * Provides a map object that will be jsonified for the client side.
     *
     * @param entryPoint        webpack entry point name
     * @param model             spring's model map
     * @param request           HTTP servlet request
     *
     * @return  data map
     * 
     * @throws Exception    if things go wrong
     */
    Map<String,Object> provide(String entryPoint, Map<String, ?> model, HttpServletRequest request) throws Exception;
}
```

The library contains the ModelMapProvider which provides data from the Spring Controller ModelMap. 

The domainql project contains a JsViewDataProvider for preloaded graphql queries.
