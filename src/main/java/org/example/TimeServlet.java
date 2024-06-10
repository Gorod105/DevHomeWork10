package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timeZone;
        try {
            timeZone = req.getParameter("timezone").replace(" ", "+");

        }
        catch (Exception exception){
            timeZone = "UTC";
        }
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write(getTime(timeZone));
        resp.getWriter().close();
    }
    private String getTime(String timeZone){
        LocalDateTime localNow = LocalDateTime.now(TimeZone.getTimeZone(ZoneId.of(timeZone)).toZoneId());
        return localNow.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss")) + " " + timeZone;
    }

}