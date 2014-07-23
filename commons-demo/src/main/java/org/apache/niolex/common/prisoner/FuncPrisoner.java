/**
 * Prisoner.java
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

import java.util.Random;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-8
 */
public class FuncPrisoner {

    // The single light
    private static boolean light = false;
    private static int[] prisoners = new int[100];
    private static int[] prisoners_out = new int[100];

    public static int oneRound(Random generator, final int io) throws Exception {
        light = false;
        for (int i = 0; i < 100; ++i) {
            prisoners[i] = 1;
            prisoners_out[i] = 0;
        }
        int i = 1;
        simpleOneCollector = 0;

        switch (io) {
            case 0:
                while (!simpleOne(i, generator.nextInt(100))) {
                    ++i;
                }
                break;
            case 1:
                while (!simpleKth(i, generator.nextInt(100))) {
                    ++i;
                }
                break;
            case 2:
                while (!splitNth(i, generator.nextInt(100))) {
                    ++i;
                }
                break;
            case 3:
                while (!split10to2(i, generator.nextInt(100))) {
                    ++i;
                }
                break;
        }
        return i;
    }

    private static int simpleOneCollector = 0;


    public static boolean simpleOne(int day, int k) {
        if (k != simpleOneCollector) {
            if (!light && prisoners[k] == 1) {
                --prisoners[k];
                light = true;
            }
        } else if (light) {
            light = false;
            ++prisoners[k];
            if (prisoners[k] == 100) {
                return true;
            }
        }
        return false;
    }

    public static boolean simpleKth(int day, int k) {
        if (day < 101) {
            if (day == 100 && !light) {
                return true;
            }
            if (light) {
                return false;
            }
            ++prisoners_out[k];
            --prisoners[k];
            if (prisoners_out[k] == 2) {
                // The collector found.
                simpleOneCollector = k;
                light = true;
                prisoners[k] = day - 2;
            }
            return false;
        } else {
            return simpleOne(day, k);
        }
    }

    private static final int PHASE_LENGTH = 450;

    public static boolean splitNth(int day, final int k) {
        // We start from 0.
        --day;
        int iteration = (day / PHASE_LENGTH) % 7;
        final int cnt = 1 << iteration;
        final boolean lastDay = (day % PHASE_LENGTH) == (PHASE_LENGTH - 1);
        if (k != simpleOneCollector) {
            if (light) {
                if ((prisoners[k] & cnt) > 0 || lastDay) {
                    prisoners[k] += cnt;
                    light = false;
                }
            } else {
                // light is off, check today mask.
                if ((prisoners[k] & cnt) > 0 && !lastDay) {
                    prisoners[k] -= cnt;
                    light = true;
                }
            }
        } else if (light) {
            light = false;
            prisoners[k] += cnt;
            if (prisoners[k] == 100) {
                return true;
            }
        }
        return false;
    }

    private static final int PHASE_1 = 450;
    private static final int PHASE_2 = 450;
    private static final int PHASE_TOTAL = PHASE_1 + PHASE_2;

    public static boolean split10to2(int day, final int k) {
        // We start from 0.
        --day;
        final int remind = day % PHASE_TOTAL;
        final int phase = remind < PHASE_1 ? 1 : 2;
        if (remind == PHASE_1 - 1) {
            if (light) {
                ++prisoners[k];
                light = false;
                if (k == 0 && prisoners[k] % 10 == 0) {
                    ++prisoners_out[k];
                }
            }
            return prisoners[k] == 100;
        } else if (remind == PHASE_TOTAL - 1) {
            if (light) {
                prisoners[k] += 10;
                --prisoners_out[k];
                light = false;
            }
            return prisoners[k] == 100;
        }
        if (phase == 1) {
            // collect all
            if (k < 10) {
                if (light && prisoners_out[k] <= 0 && prisoners[k] % 10 != 0) {
                    ++prisoners[k];
                    light = false;
                    if (k == 0 && prisoners[k] % 10 == 0) {
                        ++prisoners_out[k];
                    }
                    return prisoners[k] == 100;
                }
                if (!light && prisoners_out[k] > 0 && prisoners[k] % 10 != 0) {
                    --prisoners[k];
                    light = true;
                }
            } else {
                if (!light && prisoners[k] != 10 && prisoners[k] % 10 > 0) {
                    --prisoners[k];
                    light = true;
                }
            }
        } else {
            // collect to base
            if (k == 0) {
                if (light) {
                    prisoners[k] += 10;
                    light = false;
                    return prisoners[k] == 100;
                }
            } else {
                if (!light && prisoners[k] >= 10) {
                    prisoners[k] -= 10;
                    ++prisoners_out[k];
                    light = true;
                }
            }
        }
        return false;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        double base = 365000.2;
        double base2 = 365.0002;
        for (int io = 0; io < 4; ++io) {
            int total = 0, max = 0, min = Integer.MAX_VALUE, k;
            Random generator = new Random(32785);
            for (int i = 0; i < 1000; ++i) {
                k = oneRound(generator, io);
                total += k;
                if (max < k) max = k;
                if (min > k) min = k;
            }
            System.out.printf("%s\t - Avg %.2fY, Max %.2fY, Min %5.2fY\n", getName(io),
                    (total / base), (max / base2), (min / base2));
        }
    }

    /**
     * @param io
     * @return
     */
    private static Object getName(int io) {
        switch (io) {
            case 0:
                return "simpleOne";
            case 1:
                return "simpleKth";
            case 2:
                return "splitNth";
            case 3:
                return "split10to2";
        }
        return null;
    }

}
