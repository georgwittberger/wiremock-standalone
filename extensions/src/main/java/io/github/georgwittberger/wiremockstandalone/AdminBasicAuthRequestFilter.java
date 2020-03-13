package io.github.georgwittberger.wiremockstandalone;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.tomakehurst.wiremock.extension.requestfilter.AdminRequestFilter;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

/**
 * Checks every admin API request for HTTP basic authentication credentials.
 * Authorized credentials can be configured in the file "admin-users.conf" in
 * the current working directory.
 */
public class AdminBasicAuthRequestFilter extends AdminRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BASIC_AUTH_PREFIX = "basic ";
  private static final String USERS_FILE_NAME = "admin-users.conf";

  private final Set<String> authorizedCredentials;

  public AdminBasicAuthRequestFilter() throws IOException {
    Path usersFilePath = FileSystems.getDefault().getPath(USERS_FILE_NAME);
    if (Files.notExists(usersFilePath)) {
      throw new IllegalStateException("Users file not found: " + usersFilePath);
    }

    List<String> usernamePasswords = Files.readAllLines(usersFilePath, StandardCharsets.UTF_8);

    Base64.Encoder base64Encoder = Base64.getEncoder();
    authorizedCredentials = usernamePasswords.stream().filter(usernamePassword -> usernamePassword.trim().contains(":"))
        .map(usernamePassword -> base64Encoder.encodeToString(usernamePassword.getBytes(StandardCharsets.UTF_8)))
        .collect(Collectors.toSet());
  }

  @Override
  public RequestFilterAction filter(Request request) {
    if (!request.containsHeader(AUTHORIZATION_HEADER)) {
      return RequestFilterAction.stopWith(ResponseDefinition.notAuthorised());
    }

    String authorization = request.header(AUTHORIZATION_HEADER).firstValue();
    if (!authorization.toLowerCase().startsWith(BASIC_AUTH_PREFIX)) {
      return RequestFilterAction.stopWith(ResponseDefinition.notAuthorised());
    }

    String credentials = authorization.substring(BASIC_AUTH_PREFIX.length()).trim();
    if (!authorizedCredentials.contains(credentials)) {
      return RequestFilterAction.stopWith(ResponseDefinition.notAuthorised());
    }

    return RequestFilterAction.continueWith(request);
  }

  @Override
  public String getName() {
    return "admin-basic-auth";
  }

}
