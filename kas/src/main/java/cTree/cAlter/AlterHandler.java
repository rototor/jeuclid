/*
 * Copyright 2009 Erhard Kuenzel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cTree.cAlter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import cTree.CElement;
import cTree.adapter.DOMElementMap;

public class AlterHandler {
    private volatile static AlterHandler uniqueInstance;

    public HashMap<String, CAlter> getAlters;

    @SuppressWarnings("unchecked")
    private AlterHandler() {
        this.getAlters = new HashMap<String, CAlter>();
        try {

            final BufferedReader reader = new BufferedReader(new FileReader(
                    System.getProperty("user.dir") + "/Changers.txt"));
            final ArrayList<String> strings = new ArrayList<String>();
            String line = "";
            while ((line = reader.readLine()) != null) {
                strings.add(line);
            }
            reader.close();
            for (final String s : strings) {
                final Class c = Class.forName("cTree.cAlter." + s);
                final CAlter a = (CAlter) c.getConstructor().newInstance();
                a.register(this.getAlters);
            }
        } catch (final IOException e) {
            System.err.println("Error2");
        } catch (final ClassNotFoundException e) {
            System.err.println("Error3");

        } catch (final SecurityException e) {
            System.err.println("Error4");

        } catch (final NoSuchMethodException e) {
            System.err.println("Error5");

        } catch (final IllegalArgumentException e) {
            System.err.println("Error6");

        } catch (final InstantiationException e) {
            System.err.println("Error7");

        } catch (final IllegalAccessException e) {
            System.err.println("Error8");

        } catch (final InvocationTargetException e) {
            System.err.println("Error9");

        }
    }

    public static AlterHandler getInstance() {
        if (AlterHandler.uniqueInstance == null) {
            synchronized (DOMElementMap.class) {
                if (AlterHandler.uniqueInstance == null) {
                    AlterHandler.uniqueInstance = new AlterHandler();
                }
            }
        }
        return AlterHandler.uniqueInstance;
    }

    public ArrayList<String> getOptions(final ArrayList<CElement> els) {
        final ArrayList<String> options = new ArrayList<String>();
        for (final CAlter ca : this.getAlters.values()) {
            if (ca.check(els)) {
                options.add(ca.getText());
            }
        }
        return options;
    }

    public CElement change(final ArrayList<CElement> els,
            final String actionCommand) {
        if (this.getAlters.containsKey(actionCommand)) {
            els.get(0).removeCActiveProperty();
            final CElement el = this.getAlters.get(actionCommand).change(els);
            el.setCActiveProperty();
            return el;
        } else {
            return els.get(0);
        }
    }

}
