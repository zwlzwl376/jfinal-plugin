package net.oschina.zwlzwl376.jfinal.plugin.utils;

public class BindUtils {

    /**
     * 
     * TableName -- table_name
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if ((name != null) && (name.length() > 0)) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); ++i) {
                String s = name.substring(i, i + 1);
                if (s.equals(s.toUpperCase())) {
                    result.append("_");
                    result.append(s.toLowerCase());
                } else {
                    result.append(s);
                }
            }
        }
        return result.toString();
    }

    /**
     * TableName -- tableName
     */
    public static String headLower(String className) {
        String firstLetter = className.substring(0, 1);
        String beanName = className.replace(firstLetter, firstLetter.toLowerCase());
        return beanName;
    }
}
