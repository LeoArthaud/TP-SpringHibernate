package org.epsi.b3.simplewebapp.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * A simple filter on all URLs to force using UTF-8 encoding.
 */
@WebFilter(filterName = "encodingFilter", urlPatterns = { "/*" })
public class EncodingFilter implements Filter {
 
  public EncodingFilter() {
  }
 
  @Override
  public void init(FilterConfig fConfig) throws ServletException {
  }
 
  @Override
  public void destroy() {
  }
 
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
      request.setCharacterEncoding("UTF-8");
      chain.doFilter(request, response);
  }
 
}