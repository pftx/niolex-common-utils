package org.apache.niolex.commons.reflect;

import static org.junit.Assert.*;

import org.junit.Test;

import com.esotericsoftware.reflectasm.ConstructorAccess;

public class FastNewInstanceTest extends FastNewInstance {

    @Test
    public void testGetConstructorAccess() throws Exception {
        ConstructorAccess<FastNewInstanceTest> constructorAccess1 = getConstructorAccess(FastNewInstanceTest.class);
        ConstructorAccess<FastNewInstanceTest> constructorAccess2 = getConstructorAccess(FastNewInstanceTest.class);
        assertNotNull(constructorAccess2);
        assertEquals(constructorAccess1, constructorAccess2);
    }

    @Test
    public void testNewInstance() throws Exception {
        FastNewInstanceTest test = newInstance(FastNewInstanceTest.class);
        assertEquals(0, test.hello());
        assertEquals(1, test.hello());
    }
    
    @Test
    public void testSpeed() throws Exception {
        long in, out, t1, t2;
        newInstance(FastNewInstanceTest.class);
        
        in = System.nanoTime();
        for (int i = 0; i < 1000; ++i) {
            FastNewInstanceTest test = FastNewInstanceTest.class.newInstance();
            assertEquals(0, test.hello());
        }
        out = System.nanoTime();
        t2 = out - in;
        
        in = System.nanoTime();
        for (int i = 0; i < 1000; ++i) {
            FastNewInstanceTest test = newInstance(FastNewInstanceTest.class);
            assertEquals(0, test.hello());
        }
        out = System.nanoTime();
        t1 = out - in;
        
        System.out.println("t1 = " + t1 + ", t2 = " + t2 + ", 加速比(t1/t2) = " + ((t1 * 1.0 / t2) * 100) + "%");
    }

    public int hello() {
        return i++;
    }
    
    private int i = 0;
}
