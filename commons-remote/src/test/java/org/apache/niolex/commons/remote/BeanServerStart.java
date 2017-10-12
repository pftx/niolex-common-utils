package org.apache.niolex.commons.remote;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.test.SystemInfo;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.apache.niolex.commons.test.Benchmark.Group;
import org.apache.niolex.commons.util.Runme;

public class BeanServerStart {

    public static class B implements Invokable {

        private String msg = "Please invoke me!";

        /**
         * Override super method
         * @throws IOException
         * @see org.apache.niolex.commons.remote.Invokable#invoke()
         */
        @Override
        public void invoke(OutputStream out, String[] args) throws IOException {
            System.out.println("I am invoked.");
            out.write(StringUtil.strToAsciiByte("I am invoked." + ConnectionWorker.endl()));
        }

        public String getMsg() {
            return msg;
        }

    }

    public static class A {
        int[] ids = new int[] {1, 2, 3, 4, 5};
        String[] names = new String[] {"Adam", "Shalve", "Bob"};
        Group group = Group.makeGroup();
        Integer i = new Integer(128);
        final Boolean b = Boolean.FALSE;
        Byte by = new Byte((byte) 3);
        Map<Integer, String> map = new HashMap<Integer, String>();
        Map<String, String> smap = new HashMap<String, String>();
        Map<String, Object> bmap = new HashMap<String, Object>();
        private Map<Object, Object> imap = new HashMap<Object, Object>();
        Set<String> set = new HashSet<String>();

        public A() {
            map.put(1, "Good");
            smap.put("test", "but");
            smap.put("this.[is].good", "See You!");
            bmap.put("b", new Bean(3, "Bean", 12212, new Date()));
            bmap.put("c", Benchmark.makeBenchmark());
            bmap.put("invoke", new B());
            bmap.put("os", new OSInfo());
            imap.put(new Date(), new Bean(3, "Bean", 12212, new Date()));
            set.add("Goog Morning");
            set.add("This is Good");
            set.add("中文");
        }

        public int[] getIds() {
            return ids;
        }

        public void setIds(int[] ids) {
            this.ids = ids;
        }

        public String[] getNames() {
            return names;
        }

        public void setNames(String[] names) {
            this.names = names;
        }

        public Integer getI() {
            return i;
        }

        public void setI(Integer i) {
            this.i = i;
        }

        public Byte getBy() {
            return by;
        }

        public void setBy(Byte by) {
            this.by = by;
        }

        public Map<Integer, String> getMap() {
            return map;
        }

        public void setMap(Map<Integer, String> map) {
            this.map = map;
        }

        public Map<String, String> getSmap() {
            return smap;
        }

        public void setSmap(Map<String, String> smap) {
            this.smap = smap;
        }

        public Set<String> getSet() {
            return set;
        }

        public void setSet(Set<String> set) {
            this.set = set;
        }

        public Boolean getB() {
            return b;
        }
        
    }

    /**
     * Test method for {@link org.apache.niolex.commons.remote.BeanServer#start()}.
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ConnectionWorker.setAuthInfo("abcD");
        BeanServerTest test = new BeanServerTest();
        test.beanS.putIfAbsent("bench", Benchmark.makeBenchmark());
        test.beanS.putIfAbsent("group", new A());
        test.beanS.putIfAbsent("system", SystemInfo.getInstance());
        final Monitor m = new Monitor(10);
        test.beanS.putIfAbsent("cdc", m);
        Runme rme = new Runme() {

            @Override
            public void runMe() {
                m.addValue("test.me", MockUtil.randInt(200));
            }

        };
        rme.setSleepInterval(1000);
        rme.start();
        test.beanS.start();
        Thread.sleep(3000000);
        test.beanS.stop();
    }

}
