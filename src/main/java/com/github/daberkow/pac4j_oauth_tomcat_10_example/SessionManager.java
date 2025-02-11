package com.github.daberkow.pac4j_oauth_tomcat_10_example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A servlet to allow for checking of the users status by AJAX.
 */
@WebServlet(name = "SessionManager", urlPatterns = { "/session/*" })
public class SessionManager extends HttpServlet {
  // @Serial
  private static final long serialVersionUID = 3611213278843780687L;
  private static final Logger logger = LogManager.getLogger(SessionManager.class);

  @Override
  protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    logger.info(req.getRequestURI());

    String returningText = "";
    switch (req.getRequestURI().toLowerCase()) {
      case "/session/status": {
        if (isLoggedIn(req)) {
          returningText = "Logged In";
        } else {
          returningText = "Not Logged In";
        }
      }
      break;
      case "/session/logout": {
        req.getSession().invalidate();
        req.logout();
        resp.sendRedirect("/");
      }
      break;
      default:
        returningText = "No Return";
    }
    final ServletOutputStream outputStream = resp.getOutputStream();
    resp.setContentType("text/html; charset=utf-8");
    outputStream.write(returningText.getBytes());
    outputStream.flush();
    outputStream.close();
  }

  protected static boolean isLoggedIn(HttpServletRequest req) {
    // By saying getSession(false), Tomcat won't make a new session if the user is not currently logged in.
    final HttpSession session = req.getSession(false);
    return session != null && session.getAttribute("user") != null && session.getAttribute("user") != "";
  }

}
