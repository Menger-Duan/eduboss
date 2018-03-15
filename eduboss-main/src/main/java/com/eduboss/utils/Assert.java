/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.utils;

import com.eduboss.exception.EdubossRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author <a href="xiangshaoxu@wxchina.com">xiangshaoxu</a>
 * @version 1.0.0
 * @date 2015年11月17日
 */
@SuppressWarnings("rawtypes")
public abstract class Assert {

	public static void state(boolean expression) {
		state(expression, "[Assertion failed] - the expression argument must be null");
	}
	
	public static void state(final boolean expression, final String message) {
		if (!expression) {
			throwException(message);
		}
	}

	public static void state(final boolean expression, final String message, Throwable e) {
		if (!expression) {
			throwException(message, e);
		}
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.isNull(value);
	 * </pre>
	 *
	 * @param object
	 *            the object to check
	 * @throws IllegalStateException
	 *             if the object is not <code>null</code>
	 */
	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void isNull(Object object, String message) {
		if (object != null) {
			throwException(message);
		}
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz);
	 * </pre>
	 *
	 * @param object
	 *            the object to check
	 * @throws IllegalStateException
	 *             if the object is <code>null</code>
	 */
	public static void isNotNull(Object object) {
		isNotNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz, &quot;The class must not be null&quot;);
	 * </pre>
	 *
	 * @param object
	 *            the object to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalStateException
	 *             if the object is <code>null</code>
	 */
	public static void isNotNull(Object object, String message) {
		if (object == null) {
			throwException(message);
		}
	}

	/**
	 * 断言字符串为<code>null</code>、空字符串或者只包含空白字符串（空格、回车、换行、tab等字符） Assert that a
	 * character sequence is null, empty or contains blank characters only.
	 *
	 * @param text
	 *            the character sequence to be checked
	 * @param message
	 *            the exception message to display when the assertion failed
	 */
	public static void isBlank(CharSequence text, String message) {
		if (StringUtils.isNotBlank(text)) {
			throwException(message);
		}
	}

	/**
	 * 断言字符串为<code>null</code>、空字符串或者只包含空白字符串（空格、回车、换行、tab等字符） Assert that a
	 * character sequence is null, empty or contains blank characters only.
	 *
	 * @param text
	 *            the character sequence to be checked
	 */
	public static void isBlank(CharSequence text) {
		isBlank(text, "[Assertion failed] - this CharSequence argument must be null or blank");
	}

	/**
	 * 断言字符串text至少包含一个非空白字符串（空格、回车、换行、tab等空白字符之外的字符） Assert that a character
	 * sequence must have at least one not blank character.
	 *
	 * @param text
	 *            the character sequence to be checked
	 * @param message
	 *            the exception message to display when the assertion failed
	 */
	public static void isNotBlank(CharSequence text, String message) {
		if (StringUtils.isBlank(text)) {
			throwException(message);
		}
	}

	/**
	 * 断言字符串text至少包含一个非空白字符串（空格、回车、换行、tab等空白字符之外的字符） Assert that a character
	 * sequence must have at least one not blank character.
	 *
	 * @param text
	 *            the character sequence to be checked
	 */
	public static void isNotBlank(CharSequence text) {
		isNotBlank(text, "[Assertion failed] - this CharSequence argument must not be null or blank");
	}

	/**
	 * Assert that an array is <code>null</code> or has not elements.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.isEmpty(array, &quot;The array must have not elements&quot;);
	 * </pre>
	 *
	 * @param array
	 *            the array to check
	 * @throws IllegalStateException
	 *             if the object array has any elements
	 */
	public static void isEmpty(Object[] array) {
		isEmpty(array, "[Assertion failed] - this array must be empty: it must not contain any element");
	}

	/**
	 * Assert that an array is <code>null</code> or has not elements.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.isEmpty(array, &quot;The array must have not elements&quot;);
	 * </pre>
	 *
	 * @param array
	 *            the array to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalStateException
	 *             if the object array has any elements
	 */
	public static void isEmpty(Object[] array, String message) {
		if (array != null && array.length > 0) {
			throwException(message);
		}
	}

	/**
	 * Assert that an array has at least one element.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(array);
	 * </pre>
	 *
	 * @param array
	 *            the array to check
	 * @throws IllegalStateException
	 *             if the object array is <code>null</code> or has no elements
	 */
	public static void isNotEmpty(Object[] array) {
		isNotEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that an array has at least one element.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(array, &quot;The array must have elements&quot;);
	 * </pre>
	 *
	 * @param array
	 *            the array to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalStateException
	 *             if the object array is <code>null</code> or has no elements
	 */
	public static void isNotEmpty(Object[] array, String message) {
		if (array == null || array.length == 0) {
			throwException(message);
		}
	}

	/**
	 * Assert that a collection has no elements.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 *
	 * @param collection
	 *            the collection to check
	 * @throws IllegalStateException
	 *             if the collection has any elements
	 */
	public static void isEmpty(Collection collection) {
		isEmpty(collection, "[Assertion failed] - this array must have no elements");
	}

	/**
	 * Assert that a collection has no elements.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.isEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 *
	 * @param collection
	 *            the collection to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalStateException
	 *             if the collection has any elements
	 */
	public static void isEmpty(Collection collection, String message) {
		if (collection != null && collection.size() > 0) {
			throwException(message);
		}
	}

	/**
	 * Assert that a collection has elements.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 *
	 * @param collection
	 *            the collection to check
	 * @throws IllegalStateException
	 *             if the collection is <code>null</code> or has no elements
	 */
	public static void isNotEmpty(Collection collection) {
		isNotEmpty(collection, "[Assertion failed] - this collection must contain at least 1 element");
	}

	/**
	 * Assert that a collection has elements.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 *
	 * @param collection
	 *            the collection to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalStateException
	 *             if the collection is <code>null</code> or has no elements
	 */
	public static void isNotEmpty(Collection collection, String message) {
		if (collection == null || collection.isEmpty()) {
			throwException(message);
		}
	}

	/**
	 * Assert that a map must have no entries.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.isEmpty(map, &quot;Map must have no elements&quot;);
	 * </pre>
	 *
	 * @param map
	 *            the map to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalStateException
	 *             if the map has any entries
	 */
	public static void isEmpty(Map map, String message) {
		if (map != null && map.size() > 0) {
			throwException(message);
		}
	}

	/**
	 * Assert that a map must have no entries.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(map, &quot;Map must have no elements&quot;);
	 * </pre>
	 *
	 * @param map
	 *            the map to check
	 * @throws IllegalStateException
	 *             if the map has any entries
	 */
	public static void isEmpty(Map map) {
		isEmpty(map, "[Assertion failed] - this array must be null or empty");
	}

	/**
	 * Assert that a Map has entries.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(map, &quot;Map must have entries&quot;);
	 * </pre>
	 *
	 * @param map
	 *            the map to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalStateException
	 *             if the map is <code>null</code> or has no entries
	 */
	public static void isNotEmpty(Map map, String message) {
		if (map == null || map.isEmpty()) {
			throwException(message);
		}
	}

	/**
	 * Assert that a Map has entries.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(map);
	 * </pre>
	 *
	 * @param map
	 *            the map to check
	 * @throws IllegalStateException
	 *             if the map is <code>null</code> or has no entries
	 */
	public static void isNotEmpty(Map map) {
		isNotEmpty(map, "[Assertion failed] - this map must have at least one entry");
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.isInstanceOf(Foo.class, foo, &quot;The object must be instance of
	 * the type&quot;);
	 * </pre>
	 *
	 * @param type
	 *            the type to check against
	 * @param obj
	 *            the object to check
	 * @param message
	 *            a message which will be prepended to the message produced by
	 *            the function itself, and which may be used to provide context.
	 *            It should normally end in a ": " or ". " so that the function
	 *            generate message looks ok when prepended to it.
	 * @throws IllegalStateException
	 *             if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(Class type, Object obj, String message) {
		isNotNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throwException(message);
		}
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.isInstanceOf(Foo.class, foo);
	 * </pre>
	 *
	 * @param type
	 *            the required class
	 * @param obj
	 *            the object to check
	 * @throws IllegalStateException
	 *             if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(Class type, Object obj) {
		isInstanceOf(type, obj, "Object of class [" + (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
	}

	/**
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is
	 * <code>true</code>.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.isAssignableFrom(Number.class, myClass, &quot;The subType must be
	 * instance of the superType&quot;);
	 * </pre>
	 *
	 * @param superType
	 *            the super type to check against
	 * @param subType
	 *            the sub type to check
	 * @param message
	 *            a message which will be prepended to the message produced by
	 *            the function itself, and which may be used to provide context.
	 *            It should normally end in a ": " or ". " so that the function
	 *            generate message looks ok when prepended to it.
	 * @throws IllegalStateException
	 *             if the classes are not assignable
	 */
	@SuppressWarnings("unchecked")
	public static void isAssignableFrom(Class superType, Class subType, String message) {
		isNotNull(superType, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throwException(message);
		}
	}

	/**
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is
	 * <code>true</code>.
	 * <p/>
	 * 
	 * <pre class="code">
	 * Assert.isAssignableFrom(Number.class, myClass);
	 * </pre>
	 *
	 * @param superType
	 *            the super type to check
	 * @param subType
	 *            the sub type to check
	 * @throws IllegalStateException
	 *             if the classes are not assignable
	 */
	public static void isAssignableFrom(Class superType, Class subType) {
		isAssignableFrom(superType, subType, subType + " must be assignable to " + superType);
	}

	private static void throwException(final String message, Throwable cause) {
		throw new EdubossRuntimeException(message, cause);
	}

	private static void throwException(final String message) {
		throw new EdubossRuntimeException(message);
	}

}
