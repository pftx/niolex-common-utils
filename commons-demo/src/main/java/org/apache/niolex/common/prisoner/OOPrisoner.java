/**
 * OOPrisoner.java
 *
 * Copyright 2014 the original author or authors.
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
package org.apache.niolex.common.prisoner;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import org.apache.niolex.commons.reflect.MethodFilter;
import org.apache.niolex.commons.reflect.MethodUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-9
 */
public class OOPrisoner {

    private static final int TOTAL = Prisoner.TOTAL;
    private static final int ROUND = 1000;
    private static final Prisoner[] prisoners = new Prisoner[TOTAL];

    public static void initSimple() {
        prisoners[0] = new BaseCollector();
        for (int i = 1; i < TOTAL; ++i) {
            prisoners[i] = new BasePrisoner();
        }
    }

    public static void initLetK() {
        for (int i = 0; i < TOTAL; ++i) {
            prisoners[i] = new ElectCollector();
        }
    }

    public static int oneRound(Random r) {
        LightBulb l = new LightBulb();
        boolean[] checks = new boolean[TOTAL];
        for (int i = 0; i < TOTAL; ++i) {
            checks[i] = false;
        }
        int days = 0, k;
        while (!prisoners[k = r.nextInt(TOTAL)].sure(days, l)) {
            checks[k] = true;
            ++days;
        }
        for (int i = 0; i < TOTAL; ++i) {
            if (!checks[i]) {
                throw new RuntimeException("Invalid Algorithm!");
            }
        }
        return days;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        final double BASE = 365.0002;
        final double BASE_T = BASE * ROUND;
        List<Method> list = MethodUtil.getMethods(OOPrisoner.class, MethodFilter.c().includeStatic().p().r(void.class));
        for (Method m : list) {
            int total = 0, max = 0, min = Integer.MAX_VALUE, k;
            Random generator = new Random(32785);
            for (int i = 0; i < 1000; ++i) {
                m.invoke(null);
                k = oneRound(generator);
                total += k;
                if (max < k) max = k;
                if (min > k) min = k;
            }
            System.out.printf("%s\t - Avg %.2f, Max %.2f, Min %.2f\n", m.getName().substring(4),
                    (total / BASE_T), (max / BASE), (min / BASE));
        }
    }

}

class LightBulb {
    private boolean status;

    boolean isOn() {
        return status;
    }

    boolean isOff() {
        return !status;
    }

    void setOn() {
        status = true;
    }

    void turnOff() {
        status = false;
    }

}

interface Prisoner {
    int TOTAL = 100;
    boolean sure(int day, LightBulb bulb);
}

class BasePrisoner implements Prisoner {
    protected int count = 1;

    public boolean sure(int day, LightBulb bulb) {
        if (bulb.isOff() && count > 0) {
            --count;
            bulb.setOn();
        }
        return false;
    }
}

class BaseCollector implements Prisoner {
    protected int count = 1;

    public BaseCollector() {
        super();
    }

    public BaseCollector(int count) {
        super();
        this.count = count;
    }

    public boolean sure(int day, LightBulb bulb) {
        if (bulb.isOn()) {
            ++count;
            bulb.turnOff();
            if (count == TOTAL) {
                return true;
            }
        }
        return false;
    }
}

class ElectCollector extends BasePrisoner {

    private int visit = 0;
    private Prisoner deligate;

    public boolean sure(int day, LightBulb bulb) {
        if (day < 100) {
            // The first stage, elect the collector.
            if (bulb.isOff()) {
                ++visit;
                if (visit == 2) {
                    visit = 0;
                    bulb.setOn();
                    deligate = new BaseCollector(day);
                }
            }
            if (day == 99) {
                bulb.turnOff();
            }
            return false;
        } else {
            if (visit > 0) return false;
            if (deligate != null) return deligate.sure(day, bulb);
            return super.sure(day, bulb);
        }
    }

}

abstract class LevelPrisoner implements Prisoner {
    static final int FIRST_LEN = 450;
    static final int SECOND_LEN = 450;
    static final int TOTAL_LEN = FIRST_LEN + SECOND_LEN;
    static enum Role {PRI, FIR, SEC;}
    static final int FIRST_COLLECT = 10;

    abstract boolean sure(int level, boolean endDay, int day, LightBulb bulb);
    protected int count = 1;
    protected Role role = Role.PRI;

    public boolean sure(int day, LightBulb bulb) {
        int rem = day % TOTAL_LEN;
        int level = rem < FIRST_LEN ? 1 : 2;
        boolean endDay = rem == FIRST_LEN - 1 || rem == TOTAL_LEN - 1;
        return sure(level, endDay, day, bulb);
    }

    public void prisoner(LightBulb bulb) {
        if (bulb.isOff() && count > 0) {
            --count;
            bulb.setOn();
        }
    }

    private int collNumber = 0;

    public void firstCollect(LightBulb bulb) {
        if (bulb.isOn() && count < collNumber * FIRST_COLLECT) {
            ++count;
            bulb.turnOff();
        }
    }

    public void secondGive(LightBulb bulb) {
        if (bulb.isOff() && count >= FIRST_COLLECT) {
            count -= FIRST_COLLECT;
            --collNumber;
            bulb.setOn();
        }
    }

    public void secondCollect(LightBulb bulb) {
        if (bulb.isOn()) {
            count += FIRST_COLLECT;
            bulb.turnOff();
        }
    }
}

class DLevelPrisoner extends LevelPrisoner {
    public boolean sure(int level, boolean endDay, int day, LightBulb bulb) {

        return false;
    }
}

class FirstLevelCollector extends LevelPrisoner {

    public boolean sure(int level, boolean endDay, int day, LightBulb bulb) {
        if (bulb.isOff() && count > 0) {
            --count;
            bulb.setOn();
        }
        return false;
    }
}
