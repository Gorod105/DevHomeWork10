package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;


@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Clock clock;
        String timeZone;
        try {
            timeZone = req.getParameter("timezone").replace(" ", "+");
            clock = Clock.system(ZoneId.of(timeZone));


        }catch (Exception exception){
            timeZone = "UTC";
            clock = Clock.system(ZoneId.of(timeZone));

        }



        LocalDateTime time = LocalDateTime.now(clock);
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write(formatDate(time,timeZone));
        resp.getWriter().close();
    }
    private String formatDate(LocalDateTime time, String timeZone){
        String result;
        result = time.getYear() + "-" +
        time.getMonthValue() + "-" +
        time.getDayOfMonth() + "  " +
        time.getHour() + ":" +
        time.getMinute() + ":" +
        time.getSecond() + "  " +
        timeZone;
        return result;
    }

}