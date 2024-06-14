package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.config.TemplateConfig;
import org.thymeleaf.context.Context;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


@WebServlet(urlPatterns = {"/"})
public class TimeServlet extends HttpServlet {
    private TemplateConfig templateConfig = new TemplateConfig();

    public TimeServlet() throws ServletException {
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timeZone;
        Context context = new Context();
        context.setVariable("action", "enterTimeZone");
        HttpSession session = req.getSession(true);
        timeZone = req.getParameter("timezone");

        if (timeZone == null && session.getAttribute("timezone") == null) {
            session.setAttribute("timezone", "UTC");
        }else {
            session.setAttribute("timezone", timeZone);
        }
        String getTime = getTime((String) session.getAttribute("timezone"));
        context.setVariable("showTime", getTime);

        templateConfig.process("index", context, resp);
    }

    private String getTime(String timeZone) {
        LocalDateTime localNow = LocalDateTime.now(TimeZone.getTimeZone(ZoneId.of(timeZone)).toZoneId());
        return localNow.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss")) + " " + timeZone;
    }

}