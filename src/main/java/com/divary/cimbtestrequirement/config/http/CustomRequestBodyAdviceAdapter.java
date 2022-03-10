package com.divary.cimbtestrequirement.config.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

@RequiredArgsConstructor
@RestControllerAdvice
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {
  private final HttpLogInterface httpLogInterface;
  private final HttpServletRequest httpServletRequest;
  private final ObjectMapper objectMapper;

  @Override
  public boolean supports(
      MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
    return true;
  }

  @Override
  public Object afterBodyRead(
      Object body,
      HttpInputMessage inputMessage,
      MethodParameter parameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    String bodyString;
    try{
      bodyString = objectMapper.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      bodyString = null;
    }

    httpLogInterface.logRequest(httpServletRequest, bodyString);

    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }
}
