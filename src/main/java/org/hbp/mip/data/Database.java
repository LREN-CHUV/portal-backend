package org.hbp.mip.data;

import com.google.gson.Gson;
import org.hbp.mip.controllers.HibernateUtil;
import org.hbp.mip.model.Group;
import org.hibernate.Session;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by mirco on 11.01.16.
 */
public class Database {

    private static String GROUPS_SRC = "/home/mirco/Workspace/GitLab/mip/target/classes/data/groups.json";
    private static String VARIABLES_SRC = "/home/mirco/Workspace/GitLab/mip/target/classes/data/variables.json";

    public static void loadGroups() {
        // Read data from file
        String inputFile = GROUPS_SRC;
        File f = new File(inputFile);
        FileReader fr;
        BufferedReader br;
        String data = "";
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            data = new String(Files.readAllBytes(Paths.get(GROUPS_SRC)));
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse JSON
        Gson gson = new Gson();
        Group rootGroup = gson.fromJson(data, Group.class);

        // Insert into DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(rootGroup);
        session.getTransaction().commit();
    }

    public static void loadVariables() {

        String inputFile = VARIABLES_SRC;

        File f = new File(inputFile);
        FileReader fr;
        BufferedReader br;

        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);

            br.close();
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
