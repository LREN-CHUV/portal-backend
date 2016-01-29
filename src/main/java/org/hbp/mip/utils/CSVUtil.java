package org.hbp.mip.utils;

import org.hbp.mip.model.Dataset;
import org.hbp.mip.model.Filter;
import org.hbp.mip.model.Query;
import org.hbp.mip.model.Variable;
import org.hibernate.Session;

import java.io.*;
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
        Dataset result = new Dataset();
        File file = new File(filename);
        List<String[]> rows = getFilteredRows(file, query.getFilters());
        System.out.println("nb of filtered rows : "+rows.size());
        String code = GenerateDSCode(query);
        Date date = new Date();
        List<String> header = new LinkedList<>();
        Map<String, List<Object>> data = new HashMap<>();

        List<Variable> variables = new LinkedList<>();
        //variables.addAll(query.getVariables());  // TODO : check that
        variables.addAll(query.getCovariables());
        //variables.addAll(query.getGrouping());  // TODO : check that

        header.addAll(variables.stream().map(Variable::getCode).collect(Collectors.toList()));

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String[] firstRow = br.readLine().split(SEPARATOR, -1);
            br.close();
            fr.close();

            for (Variable v : variables) {
                String type = getTypeFromDB(v);
                String c = v.getCode();
                int idx = find(c, firstRow);
                List<Object> l = new LinkedList<>();
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
                if(l.size() > 100)
                {
                    l = l.subList(0, 99);
                }
                data.put(c, l);
                System.out.println("Adding "+l.size()+" values to "+c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.setCode(code);
        result.setDate(date);
        result.setHeader(header);
        result.setData(data);

        return result;
    }

    private static List<String[]> getFilteredRows(File file, List<Filter> filters) {
        List<String[]> filteredRows = new LinkedList<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String[] firstRow = br.readLine().split(SEPARATOR, -1);  // 1st row -> headers
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] row = line.split(SEPARATOR, -1);
                if (matchFilter(row, filters, firstRow)) {
                    filteredRows.add(row);
                }
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filteredRows;
    }

    private static int find(String code, String[] firstRow) {
        for (int i = 0; i < firstRow.length; i++) {
            if (firstRow[i].equals(code))
                return i;
        }
        return -1;
    }

    private static boolean matchFilter(String[] row, List<Filter> filters,
                                       String[] firstRow) {
        int nbPassed = 0;
        boolean temp;
        for (Filter f : filters) {
            int idx = find(f.getVariable().getCode(), firstRow);
            String op = f.getOperator();
            List<String> testValues = f.getValues();

            temp = false;
            switch (op) {
                case "eq":
                    for (String t : testValues) {
                        if (t.equals(row[idx])) {
                            temp = true;
                        }
                    }
                    if (temp)
                        nbPassed++;
                    break;
                case "lt": {
                    double r = Double.parseDouble(row[idx]);
                    for (String t : testValues) {
                        double d = Double.parseDouble(t);
                        if (r < d) {
                            temp = true;
                        }
                    }
                    if (temp)
                        nbPassed++;
                    break;
                }
                case "gt": {
                    double r = Double.parseDouble(row[idx]);
                    for (String t : testValues) {
                        double d = Double.parseDouble(t);
                        if (r > d) {
                            temp = true;
                        }
                    }
                    if (temp)
                        nbPassed++;
                    break;
                }
                case "gte": {
                    double r = Double.parseDouble(row[idx]);
                    for (String t : testValues) {
                        double d = Double.parseDouble(t);
                        if (r >= d) {
                            temp = true;
                        }
                    }
                    if (temp)
                        nbPassed++;
                    break;
                }
                case "lte": {
                    double r = Double.parseDouble(row[idx]);
                    for (String t : testValues) {
                        double d = Double.parseDouble(t);
                        if (r <= d) {
                            temp = true;
                        }
                    }
                    if (temp)
                        nbPassed++;
                    break;
                }
                case "neq":
                    for (String t : testValues) {
                        if (!t.equals(row[idx])) {
                            temp = true;
                        }
                    }
                    if (temp)
                        nbPassed++;
                    break;
                case "in":
                    for (String t : testValues) {
                        if (t.equals(row[idx])) {
                            temp = true;
                        }
                    }
                    if (temp)
                        nbPassed++;
                    break;
                case "notin":
                    for (String t : testValues) {
                        if (!t.equals(row[idx])) {
                            temp = true;
                        }
                    }
                    if (temp)
                        nbPassed++;
                    break;
                case "between": {
                    double r = Double.parseDouble(row[idx]);
                    if (testValues.size() == 2) {
                        double min = Double.parseDouble(testValues.get(0));
                        double max = Double.parseDouble(testValues.get(1));

                        if (r >= min && r <= max) {
                            temp = true;
                        }
                    }
                    if (temp)
                        nbPassed++;
                    break;
                }
            }

        }
        return nbPassed == filters.size();
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
        session.beginTransaction();
        org.hibernate.Query q = session.createQuery("SELECT type FROM Variable where code= :code").setString("code", v.getCode());
        String type = (String) q.uniqueResult();
        session.getTransaction().commit();

        return type;
    }

}
