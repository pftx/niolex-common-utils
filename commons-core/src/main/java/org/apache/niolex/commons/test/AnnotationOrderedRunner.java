/**
 * AnnotationOrderedRunner.java
 *
 * Copyright 2013 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.test.OrderedRunner.AlphabeticalOrder;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * Run all the test cases marked by this runner in the order specified by the annotation value.
 * <br>
 * All the methods not annotated will be put to the end of the list and sorted by alphabetical order.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-1
 */
public class AnnotationOrderedRunner extends BlockJUnit4ClassRunner {

    /**
     * The annotation used to specify the order.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-7-24
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public static @interface Order {
        int value();
    }

	/**
	 * default initializer, just invoke super.
	 *
	 * @param klass the test class
	 * @throws InitializationError if the test class is malformed
	 */
	public AnnotationOrderedRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	/**
	 * Sort methods here.
	 *
	 * Override super method
	 * @see org.junit.runners.BlockJUnit4ClassRunner#computeTestMethods()
	 */
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		List<FrameworkMethod> original = super.computeTestMethods();
		List<Pair<Integer, FrameworkMethod>> annotated = new ArrayList<Pair<Integer, FrameworkMethod>>();
		List<FrameworkMethod> others = new ArrayList<FrameworkMethod>();

		// Find out all the methods who has annotation.
		for (FrameworkMethod fm : original) {
		    Method m = fm.getMethod();
		    if (m.isAnnotationPresent(Order.class)) {
		        int or = m.getAnnotation(Order.class).value();
		        annotated.add(Pair.create(or, fm));
		    } else {
		        others.add(fm);
		    }
		}

		Collections.sort(annotated, NumericOrder.INSTANCE);
        Collections.sort(others, AlphabeticalOrder.INSTANCE);

		// Save the result.
		List<FrameworkMethod> result = new ArrayList<FrameworkMethod>(original.size());

        // Add all the annotated methods in the front.
		for (Pair<Integer, FrameworkMethod> p : annotated) {
		    result.add(p.b);
		}
        // Add all the others methods in the end.
		result.addAll(others);

		return result;
	}

	/**
     * For sort FrameworkMethod in Numeric Order. If the integer equals, sort by method names.
     * This class is used by {@link AnnotationOrderedRunner#computeTestMethods()}
     *
     * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-8-14
     */
    public static class NumericOrder implements Comparator<Pair<Integer, FrameworkMethod>> {

	    static final NumericOrder INSTANCE = new NumericOrder();

	    /**
	     * Override super method
	     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	     */
	    @Override
        public int compare(Pair<Integer, FrameworkMethod> o1, Pair<Integer, FrameworkMethod> o2) {
            int r = o1.a.compareTo(o2.a);
            if (r == 0) {
                return o1.b.getName().compareTo(o2.b.getName());
            }
            return r;
	    }

	}

}
