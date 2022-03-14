package com.divary.cimbtestrequirement.config.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
public class HttpLogInterface {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final String REQUEST_ID = "requestId";
  private static final String HTTP_METHOD = "httpMethod";
  private static final String REMOTE_HOST = "remoteHost";
  private static final String REMOTE_PORT = "remotePort";
  private static final String REQUEST_PATH = "requestPath";
  private static final String REQUEST_PARAM = "requestParam";
  private static final String REQUEST_BODY = "requestBody";
  private static final String RESPONSE_BODY = "responseBody";
  private static final String HTTP_STATUS = "httpStatus";

  public void logRequest(HttpServletRequest httpServletRequest, String body) {
    if (shouldNotLog(httpServletRequest.getServletPath())) {
      setThreadContext(httpServletRequest);

      ThreadContext.put(REQUEST_ID, String.valueOf(System.currentTimeMillis()));
      ThreadContext.put(REMOTE_HOST, httpServletRequest.getRemoteHost());
      ThreadContext.put(REMOTE_PORT, String.valueOf(httpServletRequest.getRemotePort()));
      ThreadContext.put(REQUEST_PARAM, buildParametersMap(httpServletRequest).toString());
      ThreadContext.put(REQUEST_BODY, body);

      LOGGER.info(
          "event=START, httpMethod={}, requestPath={}, remoteHost={}, remotePort={}, requestParam={}, requestBody={}",
          ThreadContext.get(HTTP_METHOD),
          ThreadContext.get(REQUEST_PATH),
          ThreadContext.get(REMOTE_HOST),
          ThreadContext.get(REMOTE_PORT),
          ThreadContext.get(REQUEST_PARAM),
          ThreadContext.get(REQUEST_BODY));
    }
  }

  public void logResponse(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String body) {
    if (shouldNotLog(httpServletRequest.getServletPath())) {
      setThreadContext(httpServletRequest);

      ThreadContext.put(HTTP_STATUS, String.valueOf(httpServletResponse.getStatus()));
      ThreadContext.put(RESPONSE_BODY, body);

      LOGGER.info(
          "event=END, httpMethod={}, requestPath={}, httpStatus={},responseBody={}",
          ThreadContext.get(HTTP_METHOD),
          ThreadContext.get(REQUEST_PATH),
          ThreadContext.get(HTTP_STATUS),
          ThreadContext.get(RESPONSE_BODY));
    }
  }

  private void setThreadContext(HttpServletRequest httpServletRequest) {
    ThreadContext.put(HTTP_METHOD, httpServletRequest.getMethod());
    ThreadContext.put(REQUEST_PATH, httpServletRequest.getRequestURI());
  }

  private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
    Map<String, String> resultMap = new HashMap<>();
    Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

    while (parameterNames.hasMoreElements()) {
      String key = parameterNames.nextElement();
      String value = httpServletRequest.getParameter(key);
      resultMap.put(key, value);
    }

    return resultMap;
  }

  private boolean shouldNotLog(String url) {
    return listUrl().stream().noneMatch(url::matches);
  }

  private Collection<String> listUrl() {
    List<String> urls = new ArrayList<>();
    urls.add("/v3/api-docs");
    urls.add("/swagger-ui/(.*)");
    urls.add("/swagger-resources/(.*)");
    urls.add("/swagger-resources");
    return urls;
  }
}
