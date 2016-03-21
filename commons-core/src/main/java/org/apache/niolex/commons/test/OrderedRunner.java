/**
 * OrderedRunner.java
 *
 * Copyright 2012 Niolex, Inc.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * Run all the test cases marked by this runner in alphabetical order.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-14
 */
public class OrderedRunner extends BlockJUnit4ClassRunner {

	/**
	 * default initializer, just invoke super.
	 *
	 * @param klass the test class
     * @throws InitializationError if the test class is malformed
	 */
	public OrderedRunner(Class<?> klass) throws InitializationError {
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
		List<FrameworkMethod> cpy = new ArrayList<FrameworkMethod>(lst);

		Collections.sort(cpy, AlphabeticalOrder.INSTANCE);

		return cpy;
	}

	/**
	 * For sort FrameworkMethod in Alphabetical Order.
	 * This class is used by {@link OrderedRunner#computeTestMethods()}
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @since 2012-8-14
	 */
	public static class AlphabeticalOrder implements Comparator<FrameworkMethod> {

	    public static final AlphabeticalOrder INSTANCE = new AlphabeticalOrder();

	    /**
	     * Override super method
	     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	     */
	    @Override
	    public int compare(FrameworkMethod o1, FrameworkMethod o2) {
	        return o1.getName().compareTo(o2.getName());
	    }

	}

}
