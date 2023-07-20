package com.github.daberkow.pac4j_oauth_tomcat_10_example;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@WebServlet(
        name = "RootApp",
        urlPatterns = {"/"}
)
public class WebApp extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(WebApp.class);
//    @Serial
    private static final long serialVersionUID = 2802147441289972890L;

    @Override
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.info("Passed In Info From Launcher: " + getInitParameter("passedParam"));
    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info(req.getRequestURI());
        // For the root of the server we will always return the index, this is not production quality code.
        final ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("index.html");

        String tempStringHolder = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8))
              .lines()
              .collect(Collectors.joining("\n"));

        // Here we know we have some contents. The index page has a variable I will replace to show the user's status
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tempStringHolder.substring(0, tempStringHolder.indexOf("THIS_SERVER_STATUS")));
        if (SessionManager.isLoggedIn(req)) {
            stringBuilder.append("Logged In");
        } else {
            stringBuilder.append("NOT Logged In");
        }
        stringBuilder.append(tempStringHolder.substring(tempStringHolder.indexOf("THIS_SERVER_STATUS") + 18));

        // Send the data to the user and close the connection
        resp.setContentType("text/html; charset=utf-8");
        final ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.println(stringBuilder.toString());
        outputStream.flush();
        outputStream.close();
    }
}
