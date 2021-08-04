package org.visab.util;

import java.util.Map;

/**
 * Helper class for better string formatting without unnecesarry dependencies.
 */
public final class NiceString {

    /**
     * Formats strings like in python. Placeholders look like this "Animal: {i}"
     * where i is the index for which param to add. A maximum of 9 placeholders is
     * supported.
     * 
     * Example: StringFormat.niceString("Animal: {0}", 123) => "Animal: 123"
     * 
     * @param str    The string to process
     * @param params The params to replace the placeholders
     * @return The formatted string
     */
    public static String make(String str, Object... params) {
        var niceString = "";

        var stringLength = str.toCharArray().length;
        for (int i = 0; i < stringLength; i++) {
            var c = str.charAt(i);

            var j = i + 1;
            var k = i + 2;
            if (c == '{' && k < stringLength && Character.isDigit(str.charAt(j)) && str.charAt(k) == '}') {
                var paramIndex = Character.getNumericValue(str.charAt(j));
                var object = params[paramIndex];

                if (object instanceof Iterable<?>)
                    niceString += niceIterable((Iterable<?>) object);
                else if (object instanceof Map<?, ?>)
                    niceString += niceMap((Map<?, ?>) object);
                else
                    niceString += params[paramIndex].toString();

                // Skip the next two iterations
                i = k;
            } else {
                niceString += c;
            }
        }

        return niceString;
    }

    private static String niceMap(Map<?, ?> map) {
        var str = "{";

        for (var item : map.entrySet()) {
            if (item.getKey() != null)
                str += " (" + item.getKey().toString() + ", ";
            else
                str += " (null, ";
            if (item.getValue() != null)
                str += item.getValue().toString() + "),";
            else
                str += "null),";
        }
        if (str.endsWith(","))
            str = str.substring(0, str.length() - 1);

        return str + " }";
    }

    private static String niceIterable(Iterable<?> iterable) {
        var str = "[";

        for (var item : iterable) {
            if (item != null)
                str += item.toString() + ", ";
            else
                str += "null, ";
        }
        if (str.endsWith(", "))
            str = str.substring(0, str.length() - 2);

        return str + "]";
    }

}
