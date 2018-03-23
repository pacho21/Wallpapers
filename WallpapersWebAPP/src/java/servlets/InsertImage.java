package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.ImageJpaController;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import objects.Image;
import java.sql.Date;

/**
 *
 * @author Pacho
 */
public class InsertImage extends HttpServlet {

    //save in the database the link. image must be for mobile (not wide)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> mess = new HashMap<>(); //json message easy way
        Gson gson = new GsonBuilder().create(); //jsoning messages ;P izipizi

        String link = request.getParameter("link");

        java.util.Date date = new java.util.Date();

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");

        ImageJpaController ijc = new ImageJpaController(emf);

        try {

            URL linkToImg = new URL(link);

            if (!ijc.existsByUrl(link)) {
                BufferedImage c = ImageIO.read(linkToImg);

                if (c.getWidth() < c.getHeight()) {
                    // round's the numebrs to 2 decimals, that way we can give a small margin of resolutions
                    Image img = new Image(link, c.getHeight(), c.getWidth(), (float) Math.round((float) c.getWidth() / c.getHeight() * 100f) / 100f);
                    ijc.create(img);

                    //response
                    mess.put("mess", "Image saved successfuly :]");
                    response.setContentType("application/json");
                    PrintWriter pw = response.getWriter();
                    pw.println(gson.toJson(mess));
                } else {
                    //response -> image is not for mobile 
                    mess.put("mess", "Wrong image aspect ratio, it has to be for mobile!");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json");
                    PrintWriter pw = response.getWriter();
                    pw.println(gson.toJson(mess));
                }
            } else {
                mess.put("mess", "Image link allready exists in our database!");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(mess));
            }

        } catch (IOException ex) {
            //send error -> link not wellformed.
            mess.put("mess", "There was a problem inserting your image, probably somenthing wrong with your link!");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(mess));

        }

    }
}
