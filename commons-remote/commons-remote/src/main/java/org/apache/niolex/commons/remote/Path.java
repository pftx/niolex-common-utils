/**
 * Path.java
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
package org.apache.niolex.commons.remote;

/**
 * Store the path to navigate into objects.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-26
 */
public class Path {

	/**
	 * The Path Type enumeration.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.5, $Date: 2012-11-23$
	 */
	public static enum Type {
		FIELD, MAP, ARRAY, INVALID
	}

	// Path Type, default to Field.
	private Type type = Type.FIELD;

	// Path Name, the fields name.
	private String name;

	// This is for Map Type.
	private String key;

	// This is for Array Type.
	private int idx;

	// The Path chain.
	private Path next;

	/**
	 * The static method to parse a Path chain from the string representation.
	 *
	 * @param strPath the string path.
	 * @return the parsed Path chain.
	 */
	public static Path parsePath(String strPath) {
		strPath += '.';
		int start = 0;
		int end = 0;
		int fstart = 0, fend = 0;
		final Path path = new Path();
		Path cur = path;
		// Current parse phase, 0 - 1 - 2 - 3
		// 1 means node
		// 2 means array[list]
		// 3 means map
		int curPhase = 1;
		for (int i = 0; i < strPath.length(); ++i) {
			char ch = strPath.charAt(i);
			switch (ch) {
			case '.':
				switch (curPhase) {
				case 1:
					end = i;
					if (start < end) {
						// This is not a empty node, create it.
						cur.next = makePath(strPath, start, end);
						cur = cur.next;
					}
					start = i + 1;
					break;
				case 3:
					break;
				default:
					path.name = "Invalid Path at " + strPath.substring(0, i);
					path.type = Type.INVALID;
					return path;
				}
				break;
			case '[':
				switch (curPhase) {
				case 1:
					curPhase = 2;
					end = i;
					fstart = i + 1;
					break;
				case 3:
					break;
				default:
					path.name = "Invalid Path at " + strPath.substring(0, i);
					path.type = Type.INVALID;
					return path;
				}
				break;
			case ']':
				switch (curPhase) {
				case 3:
					break;
				case 2:
					curPhase = 1;
					fend = i;
					if (start < end && fstart < fend) {
						// This is a correct array node.
						try {
							int idx = Integer.parseInt(strPath.substring(fstart, fend));
							cur.next = makePath(strPath, start, end, idx);
							cur = cur.next;
							start = i + 1;
							break;
						} catch (Exception e) {}
					}
				default:
					path.name = "Invalid Path at " + strPath.substring(0, i);
					path.type = Type.INVALID;
					return path;
				}
				break;
			case '{':
				switch (curPhase) {
				case 1:
					curPhase = 3;
					end = i;
					fstart = i + 1;
					break;
				case 3:
					break;
				default:
					path.name = "Invalid Path at " + strPath.substring(0, i);
					path.type = Type.INVALID;
					return path;
				}
				break;
			case '}':
				switch (curPhase) {
				case 3:
					curPhase = 1;
					fend = i;
					if (start < end && fstart < fend) {
						// This is a correct map node.
						cur.next = makePath(strPath, start, end, strPath.substring(fstart, fend));
						cur = cur.next;
						start = i + 1;
						break;
					}
				default:
					path.name = "Invalid Path at " + strPath.substring(0, i);
					path.type = Type.INVALID;
					return path;
				}
				break;
			default:
				break;
			}
		}// End of for.
		if (curPhase != 1) {
			path.name = "Invalid Path at " + strPath.substring(0, strPath.length() - 2);
			path.type = Type.INVALID;
			return path;
		}
		return path.next;
	}

	/**
	 * Make path for map section.
	 *
	 * @param strPath
	 * @param start
	 * @param end
	 * @param substring the map key
	 * @return the path
	 */
	private static Path makePath(String strPath, int start, int end, String substring) {
		Path p = makePath(strPath, start, end);
		p.type = Type.MAP;
		p.key = substring;
		return p;
	}

	/**
	 * Make path for array section.
	 *
	 * @param strPath
	 * @param start
	 * @param end
	 * @param idx2 the array index
	 * @return the path
	 */
	private static Path makePath(String strPath, int start, int end, int idx2) {
		Path p = makePath(strPath, start, end);
		p.type = Type.ARRAY;
		p.idx = idx2;
		return p;
	}

	/**
	 * Make path for field section.
	 *
	 * @param strPath
	 * @param start the field start index
	 * @param end the field end index
	 * @return the path
	 */
	private static Path makePath(String strPath, int start, int end) {
		Path p = new Path();
		p.name = strPath.substring(start, end);
		return p;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public int getIdx() {
		return idx;
	}

	public Path next() {
		return next;
	}

	@Override
	public String toString() {
		String str = null;
		switch (type) {
		case FIELD:
			str = "." + name;
			break;
		case ARRAY:
			str = name + "[" + idx + "]";
			break;
		case MAP:
			str = name + "{" + key + "}";
			break;
		case INVALID:
		default:
			str = name;
			break;
		}
		return next == null ? str : str + " => " + next.toString();
	}

}
