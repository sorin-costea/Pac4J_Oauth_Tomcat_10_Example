package com.github.daberkow.pac4j_oauth_tomcat_10_example.oauth;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;

public class AbstractOidc extends HttpServlet {
  private static final long serialVersionUID = 3611287354543780687L;

  protected static OidcClient<OidcConfiguration> client;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    final String discoveryURI = "http://127.0.0.1:8081/auth/realms/example/.well-known/openid-configuration";

    final OidcConfiguration oidcConfig = new OidcConfiguration();
    oidcConfig.setClientId("example-oidc");
    oidcConfig.setSecret("IMUUVYYTa4EaeF5jxcBR7cg1z7egnRod");
    oidcConfig.setDiscoveryURI(discoveryURI);
    oidcConfig.setScope("openid email profile phone roles");
    client = new OidcClient<>(oidcConfig);
    client.setCallbackUrl("http://127.0.0.1:8080/oauth/redirect");
  }

}
