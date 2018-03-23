/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.ImageJpaController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pacho
 */
public class GetSomeImages extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> mess = new HashMap<>(); //json message easy way
        Gson gson = new Gson(); //jsoning messages ;P izipizi        

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");

        ImageJpaController ijc = new ImageJpaController(emf);
        try {
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(ijc.getRandom()));
        } catch (IOException e) {
            mess.put("mess", "Somenthing went wrong... Blame the backend!");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(mess));
        }
    }

}
