# Spring-JsView

A Javascript view system for Spring WebMVC and Webpack

## Motivation

Modern Javascript UI libraries are often preferable over Java server-side technologies. 
Integrating a Javascript client side view implementation with optional data preloading
offers an attractive way to use Javascript SPA content as UI frontend for a Spring 
Boot/WebMVC application and integrate that with the existing Java and Spring eco system.

## Overview

*Spring-jsview* integrates into the view system of Spring WebMVC. In contrast to other 
view implementations, spring-jsview primarily does not render HTML on the server-side.

The view implementation aggregates view data from several user configurable sources which
it embeds as JSON data block inside a minimal HTML template that defines the most
basic HTML structure and loads JavaScript- and CSS-Libraries.

The views of *spring-jsview* correspond to Webpack entry points on the client side. These
end points are responsible for creating the actual markup from the embedded data.


## Simple Views

An end-point can just render a simple view that works just like the normal 
Spring WebMVC views. For example in the DomainQL starter that integrates into 
Spring Security, we have a 
[simple login view](https://github.com/quinscape/domainqlstarter/blob/master/src/main/js/login.js) 
that we need to show if the security context requires it.

## SPA Views 
 
The other alternative is to have the end point render a 
[single-page application / SPA](https://en.wikipedia.org/wiki/Single-page_application).

The Spring-WebMVC controller for such a view must define an URI space within the 
application URI space in which the SPA runs. 
 
Here is one [controller method from the domainqlstarter](https://github.com/quinscape/domainqlstarter/blob/master/src/main/java/de/quinscape/domainqlstarter/runtime/controller/JsEntryPointController.java#L9-L13)

```java
    @RequestMapping("/app/**")
    public String serveApplicationEndpoint()
    {
        return "main";
    }
```

The controller method is mapped to `/app/**` which means that every URL below /app will 
be routed to the "app" end-point it declares.

On the client-side the routing-library of course has to consider the chosen URI prefix:
 
```jsx harmony
<ConnectedRouter history={ history }>
    <Switch>
        <Route exact path="/app/" component={ Home }/>
        <Route exact path="/app/foo/" component={ FooAdmin }/>
        <Route path="/app/foo/:id/:name" component={ FooAdmin }/>
        <Route path="/app/lazy" component={ LazyLoader }/>
        <Route path="/app/about" component={ About }/>
        <Route path="/app/admin" component={ Admin }/>
    </Switch>
</ConnectedRouter>
```
( from [domainqlstarter: src/main/js/main/App.js](https://github.com/quinscape/domainqlstarter/blob/master/src/main/js/main/App.js#L93-L100))

The SPA can still enjoy the benefits of the initial data injection which is repeated any time
the user (re)loads an URL within the SPA space. 

The two kinds of views can be mixed and matched like needed.  

## Setup

On the server-side, this library provides a ViewResolver that needs to be configured
to serve Javascript views.

```java
package de.quinscape.eamodern.config;

import de.quinscape.spring.jsview.JsViewResolver;
import de.quinscape.spring.jsview.ModelMapProvider;
import de.quinscape.spring.jsview.loader.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;

@Configuration
public class WebConfiguration
    implements WebMvcConfigurer
{
    private final ServletContext servletContext;

    private final ResourceLoader resourceLoader;

    @Autowired
    public WebConfiguration(
        ServletContext servletContext,
        ResourceLoader resourceLoader
    )
    {
        this.servletContext = servletContext;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry)
    {
        registry.viewResolver(
            JsViewResolver.newResolver(servletContext, "WEB-INF/template.html")
                .withResourceLoader(resourceLoader)
                .withViewDataProvider(
                    new ModelMapProvider(
                        // keys of the Spring model to use for the initial data                            
                        Collections.singleton("value")
                    )
                )
                .build()
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
 * ASSETS : HTML markup including the webpack build assets for the current end point 
 
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

## View Data Providers

The JsViewProvider interface can be used to customize the behavior of the jsview. It allows to add objects to the 
view data block and to introduce custom placeholders. 

```java 
package de.quinscape.spring.jsview;

/**
 * Implemented by classes providing preloaded data or template placeholders to the js views.
 */
public interface JsViewProvider
{
    /**
     * Uses the received context object to declare view data or template placeholder content.
     *
     * @param context       js view context
     *
     * @throws Exception    if something goes wrong
     */
    void provide(JsViewContext context) throws Exception;
}
```
### Existing Providers

The library contains the ModelMapProvider which provides data from the Spring Controller ModelMap. 

The domainql project contains a JsViewProvider for preloaded graphql queries.

