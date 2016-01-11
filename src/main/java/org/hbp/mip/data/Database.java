package org.hbp.mip.data;

import org.hbp.mip.controllers.HibernateUtil;
import org.hbp.mip.model.Group;
import org.hibernate.Session;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mirco on 11.01.16.
 */
public class Database {

    private static final String GROUPS_FILE = "data/groups.csv";
    private static final String VARIABLES_FILE = "data/variables.csv";
    private static final String SEPARATOR = ",";
    private static final ClassLoader CL = Database.class.getClassLoader();

    public static void loadGroups() {

        String inputFile = "/home/mirco/Workspace/GitLab/mip/target/classes/data/groups.csv"; //CL.getResource(GROUPS_FILE).getFile();

        List<Group> groups = new LinkedList<Group>();

        File f = new File(inputFile);
        FileReader fr;
        BufferedReader br;

        try {

            fr = new FileReader(f);
            br = new BufferedReader(fr);

            int stackPtr = -1;
            int oldIdx = -1;

            Group root = new Group(); //root
            root.setCode("root");
            root.setLabel("root");

            stackPtr++;
            groups.add(root);

            for (String line = br.readLine(); line != null; line = br.readLine()) {

                String[] data = line.split(SEPARATOR);

                int idx = data.length - 1;

                String label = data[idx];
                String code = label.replace(" ", "_");
                Group g = new Group();
                g.setLabel(label);
                g.setCode(code);

                if (idx > oldIdx && stackPtr > -1) {
                    groups.get(stackPtr).addGroup(g);
                } else if (idx < oldIdx) {
                    stackPtr -= 2;
                } else if (idx == oldIdx) {
                    stackPtr--;
                    groups.get(stackPtr).addGroup(g);
                }

                stackPtr++;
                groups.add(g);

                oldIdx = idx;

            }

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            for (Group group : groups)
            {
                session.save(group);
            }
            session.getTransaction().commit();

            br.close();
            fr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
