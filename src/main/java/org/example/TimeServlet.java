package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.config.TemplateConfig;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.TimeZone;


@WebServlet(urlPatterns = {"/"})
public class TimeServlet extends HttpServlet {
    private final TemplateConfig templateConfig = new TemplateConfig();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timeZone;
        Context context = new Context();
        context.setVariable("action", "enterTimeZone");
        timeZone = req.getParameter("timeZone");
        if (validateTimeZone(timeZone)) {
            resp.addCookie(new Cookie("timeZone", timeZone));
        }else if (Arrays.stream(req.getCookies()).noneMatch(c -> c.getName().equals("timeZone"))){
            resp.addCookie(new Cookie("timeZone", "UTC"));
        }
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("timeZone".equals(c.getName())) {
                        timeZone = c.getValue();
                        break;
                    }
                }
            }
        String getTime = getTime(timeZone);
        context.setVariable("showTime", getTime);
        templateConfig.process("index", context, resp);
    }

    private String getTime(String timeZone) {
        LocalDateTime localNow = LocalDateTime.now(TimeZone.getTimeZone(ZoneId.of(timeZone)).toZoneId());
        return localNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + timeZone;
    }
    public static boolean validateTimeZone(String timeZone){
        String[] availableZoneIds = TimeZone.getAvailableIDs();
        if (timeZone != null) {
            timeZone = timeZone.replace("UTC", "Etc/GMT");
        }

        return Arrays.asList(availableZoneIds).contains(timeZone);
    }

}