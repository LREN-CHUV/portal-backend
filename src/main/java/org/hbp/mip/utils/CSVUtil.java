package org.hbp.mip.utils;

import org.hbp.mip.model.Dataset;
import org.hbp.mip.model.Query;
import org.hbp.mip.model.Variable;
import org.hibernate.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mirco on 29.01.16.
 */
public class CSVUtil {

    private static final String SEPARATOR = ",";

    public static Dataset parseValues(String filename, Query query)
    {
        List<String[]> rows = getRows(filename);

        Dataset result = new Dataset();
        String code = GenerateDSCode(query);
        Date date = new Date();
        List<String> header = new LinkedList<>();
        List<String> grouping = new LinkedList<>();
        Map<String, LinkedList<Object>> data = new HashMap<>();

        List<Variable> covs = new LinkedList<>();
        List<Variable> grps = new LinkedList<>();
        List<Variable> all = new LinkedList<>();
        covs.addAll(query.getCovariables());
        grps.addAll(query.getGrouping());
        all.addAll(query.getCovariables());
        all.addAll(query.getGrouping());

        header.addAll(covs.stream().map(Variable::getCode).collect(Collectors.toList()));
        grouping.addAll(grps.stream().map(Variable::getCode).collect(Collectors.toList()));

        try {
            InputStream is = Dataset.class.getClassLoader().getResourceAsStream(filename);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String[] firstRow = br.readLine().split(SEPARATOR, -1);
            br.close();
            isr.close();
            is.close();

            for (Variable v : all) {
                String type = getTypeFromDB(v);
                String c = v.getCode();
                int idx = find(c, firstRow);
                List<Object> l = new LinkedList<>();
                LinkedList<Object> ll = new LinkedList<>();
                for (String[] row : rows) {
                    switch (type) {
                        case "T": {
                            String d = null;
                            String r = row[idx];
                            if (!r.isEmpty())
                                d = r;
                            l.add(d);
                            break;
                        }
                        case "I": {
                            Integer d;
                            try {
                                d = Integer.parseInt(row[idx]);
                            } catch (NumberFormatException e) {
                                try {
                                    d = (int) Double.parseDouble(row[idx]); // Age
                                    // for
                                    // example
                                    // has type
                                    // I but is
                                    // double
                                } catch (NumberFormatException e2) {
                                    d = null; // No value found
                                }
                            }
                            l.add(d);
                            break;
                        }
                        case "N": {
                            Double d = null;
                            try {
                                d = Double.parseDouble(row[idx]);
                            } catch (NumberFormatException e) {
                                // should only happen when no data available ->
                                // we
                                // could have check this with
                                // !row[idx].isEmpty()
                            }
                            l.add(d);
                            break;
                        }
                        case "D": {
                            String d = null; // Formatted (ISO8601) String

                            // instead
                            // of Date
                            String r = row[idx];
                            if (!r.isEmpty())
                                d = r;
                            l.add(d);
                            break;
                        }
                        case "B": {
                            Boolean d;
                            d = !row[idx].isEmpty();
                            l.add(d);
                            break;
                        }
                        default: {
                            String d = null;
                            String r = row[idx];
                            if (!r.isEmpty())
                                d = r;
                            l.add(d);
                            break;
                        }
                    }
                }
                // TODO : Remove this limit -> only to avoid bug with Virtua's front-end
                if(l.size() > 50)
                {
                    ll.addAll(l.subList(0,49));
                }
                data.put(c, ll);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.setCode(code);
        result.setDate(date);
        result.setHeader(header);
        result.setGrouping(grouping);
        result.setData(data);

        return result;
    }

    private static List<String[]> getRows(String filename) {
        List<String[]> rows = new LinkedList<>();
        try {
            InputStream is = Dataset.class.getClassLoader().getResourceAsStream(filename);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String[] firstRow = br.readLine().split(SEPARATOR, -1);  // 1st row -> headers
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] row = line.split(SEPARATOR, -1);
                rows.add(row);
            }
            br.close();
            isr.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }

    private static int find(String code, String[] firstRow) {
        for (int i = 0; i < firstRow.length; i++) {
            if (firstRow[i].equals(code))
                return i;
        }
        return -1;
    }

    private static String GenerateDSCode(Query query) {
        String prefix = "DS";
        String queryStr = Integer.toString(query.hashCode());
        String memId;
        Pattern p = Pattern.compile("@(\\w+)");
        Matcher m = p.matcher(queryStr);
        if (m.find()) {
            memId = m.group(1);
        } else {
            memId = Long.toString(new Date().getTime()); // In case a the regex fails (should not
            // happen)
        }
        return prefix + memId;
    }

    private static String getTypeFromDB(Variable v)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String type = null;
        try{
            session.beginTransaction();
            org.hibernate.Query q = session.createQuery("SELECT type FROM Variable where code= :code").setString("code", v.getCode());
            type = (String) q.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
        }

        if(type == null)
        {
            type = "unknown";
        }
        return type;
    }

}
