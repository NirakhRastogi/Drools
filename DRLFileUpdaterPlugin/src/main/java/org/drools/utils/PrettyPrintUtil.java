package org.drools.utils;


import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class PrettyPrintUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <K, V> String prettyPrint(Map<K, V> map) throws IOException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    }

}
