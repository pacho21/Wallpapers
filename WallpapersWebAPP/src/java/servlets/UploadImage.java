/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.ImageJpaController;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import objects.Image;

/**
 *
 * @author Pacho
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class UploadImage extends HttpServlet {

    private static final String SAVE_DIR = "img//uploads//";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
            ImageJpaController ijc = new ImageJpaController(emf);

            String appPath = request.getServletContext().getRealPath("");
            String savePath = appPath + File.separator + SAVE_DIR;

            File fileSaveDir = new File(savePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }

            Part filePart = request.getPart("inputImg");

            InputStream fileContent = filePart.getInputStream();

            BufferedImage image = ImageIO.read(fileContent);

            if (image != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss");
                Date date = new Date();
                String sDate = sdf.format(date);
               

                File ofB = new File(savePath + File.separator + sDate + ".jpg"); //jpg para no guardar transparencias
                OutputStream osB = new FileOutputStream(ofB);
                ImageIO.write(image, "jpg", ofB);
                String link = "http://35.204.4.192/Wallpapers/img/uploads/" + sDate + ".jpg";                
                
                Image img = new Image(link, image.getHeight(), image.getWidth(), (float) Math.round((float) image.getWidth() / image.getHeight() * 100f) / 100f);
                ijc.create(img);

                Map<String, String> mess = new HashMap<>();
                mess.put("mess", "Image uploaded!");
                Gson gson = new GsonBuilder().create();
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(mess));

            } else {

                Map<String, String> emess = new HashMap<>();
                emess.put("error", "The archive that you tried to upload may not be an image!");
                Gson gson = new GsonBuilder().create();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(emess));
            }

        } catch (Exception e) {
            Map<String, String> emess = new HashMap<>();
            emess.put("error", "Internal server error!");
            Gson gson = new GsonBuilder().create();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(emess));
        }

    }

}
