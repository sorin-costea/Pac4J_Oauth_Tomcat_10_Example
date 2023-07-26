package com.github.daberkow.pac4j_oauth_tomcat_10_example.oauth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.oidc.credentials.OidcCredentials;

/**
 * Now we have returned from the OAuth server, and need to check the token we got and pull any user info we want.
 */
public class OidcReturn extends AbstractOidc {
  private static final Logger logger = LogManager.getLogger(OidcReturn.class);
  //    @Serial
  private static final long serialVersionUID = -2859242436388126407L;
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    final WebContext context = new JEEContext(req, resp);
    Optional<OidcCredentials> credentials;
    //Wrap this in a try
    try {
      credentials = client.getCredentials(context);
    } catch (final TechnicalException e) {
      // 'unauthorized_client' means the clientid and secret between this server and the oauth server do not match
      // In restarting this Java server you may see "invalid grant", this is left over from the session before
      logger.error("User came with bad credentials to the redirect endpoint.", e);
      resp.sendRedirect("/");
      return;
    }

    final Optional<UserProfile> profile = client.getUserProfile(credentials.get(), context);
    if (!profile.isPresent()) {
      logger.error("User is missing profile data.");
      resp.sendRedirect("/");
      return;
    }
    final HttpSession session = req.getSession(true);
    final String username = profile.get().getId();
    logger.info("Successful login for " + username + " from " + req.getLocalAddr());
    final String vo = (String) profile.get().getAttribute("vo_id");
    session.setAttribute("user", username);
    session.setAttribute("vo", vo);
    resp.sendRedirect("/");
  }
}
