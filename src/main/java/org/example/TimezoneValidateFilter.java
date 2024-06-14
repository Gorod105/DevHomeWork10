package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.TemplateConfig;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.ZoneId;
import java.util.TimeZone;

@WebFilter(value = "/")
public class TimezoneValidateFilter extends HttpFilter {
    TemplateConfig templateConfig = new TemplateConfig();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timeZone = req.getParameter("timezone");
        Context context = new Context();
        if (timeZone == null || timeZone.trim().isEmpty()){
            chain.doFilter(req,res);
        }
        try {

            TimeZone tryGetZone = TimeZone.getTimeZone(ZoneId.of(timeZone.toUpperCase()));
            chain.doFilter(req,res);
        }catch (Exception exception){
            if(timeZone != null) {
                context.setVariable("action", "invalid");
                context.setVariable("invalidTimeZone", timeZone);
                templateConfig.process("index", context, res);
            }
        }
    }
}
