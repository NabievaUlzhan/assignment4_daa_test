package io;

import model.EdgeData;
import model.GraphData;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

public class GraphLoader {
    public GraphData load(String path) throws Exception {
        String text = new String(Files.readAllBytes(Paths.get(path)));
        GraphData g = new GraphData();

        boolean directed = extractBoolean(text, "\"directed\"\\s*:\\s*(true|false)");
        g.setDirected(directed);

        int nodes = extractInt(text, "\"nodes\"\\s*:\\s*(\\d+)");
        g.setNodes(nodes);

        int source = extractInt(text, "\"source\"\\s*:\\s*(-?\\d+)");
        g.setSource(source);

        String model = extractString(text, "\"model\"\\s*:\\s*\"([^\"]+)\"");
        g.setModel(model);

        List<EdgeData> edges = extractEdges(text);
        g.setEdges(edges);
        return g;
    }

    private boolean extractBoolean(String text, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        if (m.find()) {
            String v = m.group(1);
            return "true".equalsIgnoreCase(v);
        }
        return true;
    }

    private int extractInt(String text, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    private String extractString(String text, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    private List<EdgeData> extractEdges(String text) {
        List<EdgeData> list = new ArrayList<EdgeData>();
        Pattern p = Pattern.compile("\\{[^{}]*\\}");
        Matcher m = p.matcher(text);
        while (m.find()) {
            String obj = m.group();
            boolean hasU = obj.contains("\"u\"");
            boolean hasV = obj.contains("\"v\"");
            boolean hasFrom = obj.contains("\"from\"");
            boolean hasTo = obj.contains("\"to\"");

            int u = hasU ? extractInt(obj, "\"u\"\\s*:\\s*(-?\\d+)") : extractInt(obj, "\"from\"\\s*:\\s*(-?\\d+)");
            int v = hasV ? extractInt(obj, "\"v\"\\s*:\\s*(-?\\d+)") : extractInt(obj, "\"to\"\\s*:\\s*(-?\\d+)");
            int w;
            if (obj.contains("\"w\"")) {
                w = extractInt(obj, "\"w\"\\s*:\\s*(-?\\d+)");
            } else if (obj.contains("\"weight\"")) {
                w = extractInt(obj, "\"weight\"\\s*:\\s*(-?\\d+)");
            } else {
                w = 1;
            }

            if ((hasU && hasV) || (hasFrom && hasTo)) {
                list.add(new EdgeData(u, v, w));
            }
        }
        return list;
    }
}
