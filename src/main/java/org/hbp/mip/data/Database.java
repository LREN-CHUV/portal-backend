package org.hbp.mip.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hbp.mip.controllers.HibernateUtil;
import org.hbp.mip.model.Group;
import org.hbp.mip.model.Value;
import org.hbp.mip.model.Variable;
import org.hibernate.Session;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mirco on 11.01.16.
 */
public class Database {

    private static String GROUPS_SRC = "/home/mirco/Workspace/GitLab/mip/target/classes/data/groups.json";
    private static String VARIABLES_SRC = "/home/mirco/Workspace/GitLab/mip/target/classes/data/variables.json";

    public static void loadGroups() {
        // Read data from file
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(GROUPS_SRC)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize JSON
        Gson gson = new Gson();
        Group rootGroup = gson.fromJson(data, Group.class);

        // Insert into DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(rootGroup);
        session.getTransaction().commit();
    }

    public static void loadVariables() {
        // Read data from file
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(VARIABLES_SRC)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize JSON
        Gson gson = new Gson();
        Type listVariablesType = new TypeToken<List<Variable>>(){}.getType();
        List<Variable> variables = gson.fromJson(data, listVariablesType);

        // Sync groups and values with DB
        for(Variable v : variables)
        {
            if(v.getGroup() != null) {
                List<String> path = new LinkedList<>();
                v.setGroup(readGroupFromDB(getVarGrpRec(v.getGroup(), path)));
                v.setGrpPath(path);
            }

            // Save values if do not exist
            List<Value> newValues = new LinkedList<>();
            for(Value val : v.getValues())
            {
                // Check existence in the DB
                Value existingVal = readValueFromDB(val.getCode());
                if(existingVal == null)
                {
                    // Save if does not exist
                    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                    session.beginTransaction();
                    session.save(val);
                    session.getTransaction().commit();
                    existingVal = val;
                }
                newValues.add(existingVal);  // Set reference
            }
            v.setValues(newValues);  // Set reference
        }

        // Insert into DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        variables.forEach(session::save);
        session.getTransaction().commit();
    }

    private static String getVarGrpRec(Group group, List<String> path) {
        if(group.getGroups() != null)
        {
            path.add(group.getCode());
            if(!group.getGroups().isEmpty())
            {
                getVarGrpRec(group.getGroups().get(0), path);
            }
            return group.getCode();
        }
        return null;
    }

    private static Group readGroupFromDB(String code)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Group where code= :code");
        query.setString("code", code);
        Group group = (Group) query.uniqueResult();
        session.getTransaction().commit();
        return group;
    }

    private static Value readValueFromDB(String code)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Value where code= :code");
        query.setString("code", code);
        Value value = (Value) query.uniqueResult();
        session.getTransaction().commit();
        return value;
    }
}
