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
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@WebFilter(value = "/")
public class TimezoneValidateFilter extends HttpFilter {
    TemplateConfig templateConfig = new TemplateConfig();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timeZone = req.getParameter("timeZone");
        Context context = new Context();
        Cookie[] cookies = req.getCookies();
        boolean any = Arrays.stream(cookies).anyMatch(c -> c.getName().equals("timeZone"));
        System.out.println(any);
        if (any){
            chain.doFilter(req,res);
        }
        if (validateTimeZone(timeZone)){
            chain.doFilter(req,res);
        }else {
            context.setVariable("action", "invalid");
            context.setVariable("invalidTimeZone", timeZone);
            templateConfig.process("index", context, res);
        }

    }
    public static boolean validateTimeZone(String timeZone){
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        return availableZoneIds.contains(timeZone);
    }
}
