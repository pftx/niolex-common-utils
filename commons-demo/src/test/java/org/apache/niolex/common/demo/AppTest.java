package org.apache.niolex.common.demo;

import static org.junit.Assert.assertEquals;
import static org.apache.niolex.common.demo.Patient.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

class Patient {
    private String name;
    private String age;

    /**
     * Constructor
     * @param name
     * @param age
     */
    public Patient(String name, String age) {
        super();
        this.name = name;
        this.age = age;
    }

    /**
     * This is the override of super method.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((age == null) ? 0 : age.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }


    /**
     * This is the override of super method.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Patient other = (Patient) obj;
        if (age == null) {
            if (other.age != null)
                return false;
        } else if (!age.equals(other.age))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    public String getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(String age) {
        this.age = age;
    }

    public static int parseInt(char[] chars, int offset, int length) {
        int r = 0;
        for (int i = offset; i < offset + length; ++i) {
            r *= 10;
            r += chars[i] - '0';
        }

        return r;
    }

}

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testParseInt() {
        char[] chars = "0000:0010:1111:3000".toCharArray();

        assertEquals(0, parseInt(chars, 0, 4));
        assertEquals(10, parseInt(chars, 5, 4));
        assertEquals(1111, parseInt(chars, 10, 4));
        assertEquals(3000, parseInt(chars, 15, 4));
    }

    /**
     * Rigourous Test :-)
     */
    @Test
    public void testApp() {
        List<Patient> unOrdered = new ArrayList<Patient>();
        List<Patient> ordered = new ArrayList<Patient>();

        ordered.add(new Patient("Charles Steak", "82"));
        ordered.add(new Patient("Chris Bacon", "45"));
        ordered.add(new Patient("Matt Pork", "32"));

        unOrdered.add(new Patient("Matt Pork", "32"));
        unOrdered.add(new Patient("Chris Bacon", "45"));
        unOrdered.add(new Patient("Charles Steak", "82"));

        Collections.sort(unOrdered, new Comparator<Patient>(){

            /**
             * This method is just for demo. Not well defined.
             *
             * @param o1
             * @param o2
             * @return
             */
            @Override
            public int compare(Patient o1, Patient o2) {
                return o1.getName().compareTo(o2.getName());
            }});

        assertEquals(ordered, unOrdered);
    }
}
