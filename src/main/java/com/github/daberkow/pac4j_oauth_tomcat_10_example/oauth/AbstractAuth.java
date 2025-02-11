package com.github.daberkow.pac4j_oauth_tomcat_10_example.oauth;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.pac4j.core.util.generator.RandomValueGenerator;
import org.pac4j.oauth.client.GenericOAuth20Client;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.profile.generic.GenericOAuth20ProfileDefinition;
import org.pac4j.scribe.builder.api.GenericApi20;
/**
 * Abstract class to put our OAuth shared information in.
 */
public class AbstractAuth extends HttpServlet {
  //    @Serial
  private static final long serialVersionUID = 3611287488843780687L;
  protected static GenericOAuth20Client client;
  static RandomValueGenerator stateGenerator = new RandomValueGenerator(30);

  private final static String oauthServer = "http://127.0.0.1:8081/";

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    final OAuth20Configuration oAuthConfig = new OAuth20Configuration();
    oAuthConfig.setApi(new GenericApi20(oauthServer + "auth/realms/example/protocol/openid-connect/auth",
            oauthServer + "auth/realms/example/protocol/openid-connect/token"));
    oAuthConfig.setProfileDefinition(new GenericOAuth20ProfileDefinition());

    // Out client ID and secrets, we make these variables like in the other servlet to migrate this to a config file
    //        oAuthConfig.setKey("example-client");
    //        oAuthConfig.setSecret("km6BsRDXBqCaqLkJ0dCyTiamIgeQ9IKC");
    oAuthConfig.setKey("example-oidc");
    oAuthConfig.setSecret("IMUUVYYTa4EaeF5jxcBR7cg1z7egnRod");
    oAuthConfig.setWithState(true);
    oAuthConfig.setStateGenerator(stateGenerator);
    oAuthConfig.setScope("openid");

    client = new GenericOAuth20Client();
    client.setAuthUrl(oauthServer + "auth/realms/example/protocol/openid-connect/auth");
    client.setTokenUrl(oauthServer + "auth/realms/example/protocol/openid-connect/token");
    client.setName("example-client");
    client.setProfileUrl(oauthServer + "auth/realms/example/protocol/openid-connect/userinfo");
    client.setConfiguration(oAuthConfig);
    client.setProfileId("preferred_username"); // You can change this based on what you want to store on the user
    // This is the local server address, this matches your approved redirect URLs in your oauth server.
    client.setCallbackUrl("http://127.0.0.1:8080/oauth/redirect");
  }
}