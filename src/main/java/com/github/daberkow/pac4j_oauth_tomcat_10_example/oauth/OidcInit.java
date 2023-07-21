package com.github.daberkow.pac4j_oauth_tomcat_10_example.oauth;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.http.RedirectionAction;
import org.pac4j.core.exception.http.WithContentAction;
import org.pac4j.core.exception.http.WithLocationAction;

/**
 * Our entry into our OAuth flow, this gets your session between the server and your browser, makes sure you have a
 * valid cookie, then redirects you to the auth server to login.
 */
public class OidcInit extends AbstractOidc {
  private static final Logger logger = LogManager.getLogger(OidcInit.class);
  //    @Serial
  private static final long serialVersionUID = -2859249637388126407L;
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    final WebContext context = new JEEContext(req, resp);
    final Optional<RedirectionAction> redirect = client.getRedirectionAction(context);
    if (redirect.isPresent()) {
      final RedirectionAction action = redirect.get();
      logger.debug("Determined final redirect action for client [{}] as [{}]", client, action);
      if (action instanceof WithLocationAction) {
        final String tempLoc = ((WithLocationAction)action).getLocation();
        logger.info("Redirecting client [{}] to [{}] based on identifier [{}]", client.getName(), tempLoc, 1);
        resp.sendRedirect(tempLoc);
      }
      if (action instanceof WithContentAction) {
        resp.sendError(500);
      }
    }
  }
}

