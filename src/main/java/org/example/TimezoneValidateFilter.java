package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.TemplateConfig;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Arrays;

@WebFilter(value = "/")
public class TimezoneValidateFilter extends HttpFilter {
    TemplateConfig templateConfig = new TemplateConfig();

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timeZone = req.getParameter("timeZone");
        Context context = new Context();
        Cookie[] cookies = req.getCookies();
        boolean any = Arrays.stream(cookies).anyMatch(c -> c.getName().equals("timeZone"));
        if (any){
            chain.doFilter(req, res);
            return;
        }
        if (validateTimeZone(timeZone)){
            chain.doFilter(req, res);
            return;
        } else {
            context.setVariable("action", "invalid");
            context.setVariable("invalidTimeZone", timeZone);
            templateConfig.process("index", context, res);
        }
    }

    public static boolean validateTimeZone(String timeZone){
        return TimeServlet.validateTimeZone(timeZone);
    }
}