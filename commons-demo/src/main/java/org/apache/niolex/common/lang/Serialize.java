/**
 * Serialize.java
 *
 * Copyright 2016 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.niolex.common.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.niolex.commons.bean.BeanUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-3-28
 */
class NoSer {
    private char c = 'a';

    /**
     * @return the c
     */
    public char getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(char c) {
        this.c = c;
    }

}

class SerBase extends NoSer implements Serializable {
    /**
     * The serialVersionUID
     */
    private static final long serialVersionUID = -1294387293728046910L;
    private int i;
    private int j;

    /**
     * @return the i
     */
    public int getI() {
        System.out.println("invoke getI");
        return i;
    }

    /**
     * @param i
     *            the i to set
     */
    public void setI(int i) {
        System.out.println("invoke setI");
        this.i = i;
    }

    /**
     * @return the j
     */
    public int getJ() {
        return j;
    }

    /**
     * @param j
     *            the j to set
     */
    public void setJ(int j) {
        this.j = j;
    }

}

public class Serialize extends SerBase {

    /**
     * The serialVersionUID
     */
    private static final long serialVersionUID = -7332108957733272930L;
    private int k;
    private int l;

    /**
     * @return the l
     */
    public int getL() {
        return l;
    }

    /**
     * @param l the l to set
     */
    public void setL(int l) {
        this.l = l;
    }

    /**
     * The method does not need to concern itself with the state belonging to its superclasses or subclasses.
     *
     * @param out
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(k);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        k = in.readInt();
    }
/*
    private  Object writeReplace() {
        return 12345;
    }

    private Object readResolve() {
        return "N/A";
    }
*/
    /**
     * @param args
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Serialize s = new Serialize();
        s.setC('X');
        s.setI(3);
        s.setJ(5);
        s.k = 66778899;
        s.l = 99887766;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(s);
        oos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);

        System.out.println(BeanUtil.toString(s));
        System.out.println(BeanUtil.toString(ois.readObject()));
    }

}
