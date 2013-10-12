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
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * Run all the test cases marked by this runner in the order specified by the annotation value.
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
		List<FrameworkMethod> lst = super.computeTestMethods();
		List<Pair<Integer, FrameworkMethod>> cpy = new ArrayList<Pair<Integer, FrameworkMethod>>();
		List<FrameworkMethod> other = new ArrayList<FrameworkMethod>();

		// Find out all the methods who has annotation.
		for (FrameworkMethod fm : lst) {
		    Method m = fm.getMethod();
		    if (m.isAnnotationPresent(Order.class)) {
		        int or = m.getAnnotation(Order.class).value();
		        cpy.add(Pair.create(or, fm));
		    } else {
		        other.add(fm);
		    }
		}

		Collections.sort(cpy, NumericOrder.INSTANCE);
		// Save the result.
		List<FrameworkMethod> ret = new ArrayList<FrameworkMethod>(lst.size());

		// Add all the sorted methods in the front.
		for (Pair<Integer, FrameworkMethod> p : cpy) {
		    ret.add(p.b);
		}
		// Add all the non-sort methods in the end.
		ret.addAll(other);

		return ret;
	}

	/**
	 * For sort FrameworkMethod in Numeric Order.
	 * This class is used by {@link AnnotationOrderedRunner#computeTestMethods()}
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @since 2013-8-14
	 */
	public static class NumericOrder implements Comparator<Pair<Integer, ?>> {

	    static final NumericOrder INSTANCE = new NumericOrder();

	    /**
	     * Override super method
	     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	     */
	    @Override
	    public int compare(Pair<Integer, ?> o1, Pair<Integer, ?> o2) {
	        return o1.a.compareTo(o2.a);
	    }

	}

}
