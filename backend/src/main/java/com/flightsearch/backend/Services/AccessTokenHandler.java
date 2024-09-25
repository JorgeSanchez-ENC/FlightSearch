package com.flightsearch.backend.Services;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AccessTokenHandler {

  @Autowired
  AccessTokenService accessTokenService;

  private char[] tokenArray;

  public String getAccessToken() throws IOException {
    if (tokenArray == null) {
      String accessToken = accessTokenService.getAccessToken();
      tokenArray = accessToken.toCharArray();
    }
    return String.valueOf(tokenArray);
  }
}
