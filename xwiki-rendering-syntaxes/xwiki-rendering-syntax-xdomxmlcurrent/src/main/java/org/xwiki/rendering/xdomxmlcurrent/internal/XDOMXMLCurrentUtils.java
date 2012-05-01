/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.rendering.xdomxmlcurrent.internal;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XDOMXMLCurrentUtils
{
    private static final Map<Class< ? >, Object> DEFAULTS = new HashMap<Class< ? >, Object>();

    static {
        DEFAULTS.put(boolean.class, false);
        DEFAULTS.put(char.class, '\0');
        DEFAULTS.put(byte.class, (byte) 0);
        DEFAULTS.put(short.class, (short) 0);
        DEFAULTS.put(int.class, 0);
        DEFAULTS.put(long.class, 0L);
        DEFAULTS.put(float.class, 0f);
        DEFAULTS.put(double.class, 0d);
    }

    private static final Set<Class< ? >> SIMPLECLASSES = new HashSet<Class< ? >>(Arrays.<Class< ? >> asList(
        String.class, Character.class, Boolean.class));

    public static boolean isSimpleType(Type type)
    {
        boolean simpleType = false;

        if (type instanceof Class) {
            Class< ? > typeClass = (Class< ? >) type;

            simpleType =
                SIMPLECLASSES.contains(typeClass) || Number.class.isAssignableFrom(typeClass)
                    || typeClass.isPrimitive() || typeClass.isEnum();
        }

        return simpleType;
    }

    public static Object defaultValue(Class< ? > type)
    {
        return DEFAULTS.get(type);
    }
}
