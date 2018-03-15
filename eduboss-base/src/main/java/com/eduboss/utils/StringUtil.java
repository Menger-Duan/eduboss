package com.eduboss.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * �ַ�����
 * <p>
 * �ṩһЩ���ַ����йص��ճ�����
 * <p>
 * �ο�apache commons-lang��
 * 
 * @see org.apache.commons.lang.StringUtils
 * @see org.springframework.util.StringUtils
 * @version 1.0
 */
public class StringUtil {

	public StringUtil() {
		super();
	}
	
	public static String cutLastChar(String str) {
		if (str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	/**
	 * ת����long����
	 * @param input
	 * @param defaultNumber
	 * @return
	 */
	public static long convertToLong(String input, long defaultNumber) {
		long result = defaultNumber;
		
		if (isNumber(input)) {
			result = Long.valueOf(input);
		}
		
		return result;
	}
	
	/**
	 * ת����int����
	 * @param input
	 * @param defaultNumber
	 * @return
	 */
	public static int convertToInt(String input, int defaultNumber) {
		int result = defaultNumber;
		
		if (isNumber(input)) {
			result = Integer.valueOf(input);
		}
		
		return result;
	}
	
	/**
	 * ��ȡ<a>123</a>�ַ��е�123
	 * 
	 * @param p_soure
	 *            String
	 * @param p_tag
	 *            String
	 * @return String
	 */
	public static String getConfigStr(String p_source, String p_tag) {
		String record = p_source;
		String ret = "";
		int i1, i2;

		if (record.indexOf("<" + p_tag + ">") >= 0) {
			i1 = record.indexOf("<" + p_tag + ">");
			i2 = record.indexOf("</" + p_tag + ">");
			if (i2 >= 0) {
				ret = record.substring(i1 + p_tag.trim().length() + 2, i2);
			} else {
				ret = "";
			}
		}
		return ret;
	}

	/**
	 * �ж���ݴ��Ƿ�Ϊ����
	 * 
	 * @param p_data
	 * @return
	 */
	public static boolean isNumber(String p_data) {
		if (StringUtils.isBlank(p_data)) {
			return false;
		}
		for (int i = 0; i < p_data.length(); i++) {
			char c = p_data.charAt(i);
			Character chr = new Character(c);
			if (!chr.isDigit(c)) {
				if (i == 0) {
					if (!(c == '+' || c == '-')) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * �ж���ݴ��Ƿ�Ϊ����
	 * 
	 * @param p_data
	 * @return
	 */
	public static boolean isDigit(String p_data) {
		for (int i = 0; i < p_data.length(); i++) {
			char c = p_data.charAt(i);

			Character chr = new Character(c);
			if (chr.toString().equals(".")) {
				if (i == 0 || i == p_data.length() - 1)
					return false;
				continue;
			}
			if (!chr.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * �ж��ַ����Ƿ������
	 * 
	 * @param p_data
	 * @return
	 */
	public static boolean isContainChn(String p_data) {
		for (int i = 0; i < p_data.length(); i++) {
			char c = p_data.charAt(i);
			Character chr = new Character(c);
			if (chr.hashCode() > 125) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �ж��ַ��Ƿ�С��
	 * 
	 * @param p_data
	 * @return
	 */
	public static boolean isDouble(String p_data) {
		int i_point_count = 0; // С������
		for (int i = 0; i < p_data.length(); i++) {
			char c = p_data.charAt(i);
			Character chr = new Character(c);
			if (!chr.isDigit(c)) {
				if (i == 0) {
					if (!(c == '+' || c == '-' || c == '.')) {
						return false;
					}
					if (c == '.') {
						i_point_count++;
					}
				} else {
					if (c == '.') {
						i_point_count++;
						if (i_point_count > 1) {
							return false;
						}
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * ��ָ������ݸ�ʽ��������
	 * 
	 * @param s_value
	 *            �ַ�����ֵ
	 * @param p_format
	 *            ��ʽ��
	 * @return
	 */
	public static String formatData(String s_value, String p_format) {
		DecimalFormat a = new DecimalFormat(p_format);
		return a.format(Double.valueOf(s_value));
	}

	/**
	 * �жϸ��ַ��Ƿ�Ϊ��,false:��Ϊ�գ�true����
	 * 
	 * @param s
	 *            String
	 * @return boolean
	 */
	public static boolean isNull(String s) {
		if (s != null && s.trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * ȥ���ַ��еĿո�
	 * 
	 * @param line
	 * @return
	 */
	public static String getNormalLine(String line) { // ȥ���ַ�Ŀո�
		Pattern p = Pattern.compile(" ");
		Matcher m = p.matcher(line);
		String s = m.replaceAll("");
		return s;

	}
	
	public static final String getUUID(){  
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", ""); 
    } 

	public static final String FOLDER_SEPARATOR = "/"; // folder separator

	public static final String WINDOWS_FOLDER_SEPARATOR = "\\"; // Windows
																// folder
																// separator

	public static final String TOP_PATH = ".."; // top folder

	public static final String CURRENT_PATH = "."; // current folder

	/**
	 * �ж�����ַ����Ƿ����0.
	 * <p>
	 * 
	 * <pre>
	 * StringUtil.hasLength(null) = false
	 * StringUtil.hasLength(&quot;&quot;) = false
	 * StringUtil.hasLength(&quot; &quot;) = true
	 * StringUtil.hasLength(&quot;Hello&quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            ��Ϊnull
	 * @return <code>true</code> ���ַ�ǿ��ҳ��ȴ���0
	 */
	public static boolean hasLength(String str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * �ж�����ַ��Ƿ��зǿ��ַ�
	 * <p>
	 * 
	 * <pre>
	 * StringUtil.hasText(null) = false
	 * StringUtil.hasText(&quot;&quot;) = false
	 * StringUtil.hasText(&quot; &quot;) = false
	 * StringUtil.hasText(&quot;12345&quot;) = true
	 * StringUtil.hasText(&quot; 12345 &quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            ��Ϊ<code>null</code>
	 * @return <code>true</code> ���ַ�Ϊ�ǿ��ҳ��ȴ���0�����ٰ�һ���ǿհ��ַ��true
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean hasText(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ȥ���ַ��ǰ���հ�
	 * 
	 * @param str
	 *            ��Ҫ�����е��ַ�
	 * @return ���к���ַ�
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(String str) {
		if (str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * ȥ���ַ�β���Ŀհ�
	 * 
	 * @param str
	 *            ��Ҫ�����е��ַ�
	 * @return ���к���ַ�
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimTrailingWhitespace(String str) {
		if (str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0
				&& Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * �������ַ��Ƿ����ָ����ǰ׺,���Դ�Сд����
	 * 
	 * @param str
	 *            ԭʼ�ַ�
	 * @param prefix
	 *            ǰ׺�����Դ�Сд��
	 * @see java.lang.String#startsWith
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * ͳ������Ӵ���ĸ���г��ֵĴ���
	 * 
	 * @param str
	 *            ĸ�� Ϊnullʱ����0.
	 * @param sub
	 *            �Ӵ� Ϊnullʱ����0.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0
				|| sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * ɾ��ĸ���г��ֵ������Ӵ�
	 * 
	 * @param pattern
	 *            �Ӵ�
	 * @return ɾ���Ӵ�֮����´�
	 */
	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	/**
	 * ��ĸ����ɾ��charsToDelete�����а���ַ�
	 * 
	 * @param charsToDelete
	 *            ��ɾ����ַ�. ��: "az\n" ����ɾ��'a's, 'z's �ͻ��з�.
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (inString == null || charsToDelete == null) {
			return inString;
		}
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}

	/**
	 * <p>
	 * ����ĸ��д
	 * </p>
	 * 
	 * <p>
	 * For a word based algorithm. A
	 * <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.capitalize(null)  = null
	 * StringUtil.capitalize(&quot;&quot;)    = &quot;&quot;
	 * StringUtil.capitalize(&quot;cat&quot;) = &quot;Cat&quot;
	 * StringUtil.capitalize(&quot;cAt&quot;) = &quot;CAt&quot;
	 * </pre>
	 * 
	 * @param str
	 *            ��Ҫת�����ַ� ��Ϊ<code>null</code>
	 * @return ת����Ľ��򵱲���Ϊnullʱ������<code>null</code>
	 * @see #uncapitalize(String)
	 * @since 2.0
	 */
	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
	}


	/**
	 * <p>
	 * ����ĸСд
	 * </p>
	 * 
	 * <p>
	 * For a word based algorithm, 
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.uncapitalize(null)  = null
	 * StringUtil.uncapitalize(&quot;&quot;)    = &quot;&quot;
	 * StringUtil.uncapitalize(&quot;Cat&quot;) = &quot;cat&quot;
	 * StringUtil.uncapitalize(&quot;CAT&quot;) = &quot;cAT&quot;
	 * </pre>
	 * 
	 * @param str
	 *            ��Ҫת�����ַ� ��Ϊ<code>null</code>
	 * @return ת����Ľ��򵱲���Ϊnullʱ������<code>null</code>
	 * @see #capitalize(String)
	 * @since 2.0
	 */
	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen).append(
				Character.toLowerCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	/**
	 * �����path����ȡ�ļ��� ��: "mypath/myfile.txt" -> "myfile.txt".
	 * 
	 * @param path
	 *            �ļ�ȫ·��
	 * @return �ļ���
	 */
	public static String getFilename(String path) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1)
				: path);
	}

	/**
	 * Apply the given relative path to the given path, assuming standard Java
	 * folder separation (i.e. "/" separators);
	 * 
	 * @param path
	 *            the path to start from (usually a full file path)
	 * @param relativePath
	 *            the relative path to apply (relative to the full file path
	 *            above)
	 * @return the full file path that results from applying the relative path
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith("/")) {
				newPath += "/";
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	/**
	 * Normalize the path by suppressing sequences like "path/.." and inner
	 * simple dots folders.
	 * <p>
	 * The result is convenient for path comparison. For other uses, notice that
	 * Windows separators ("\") are replaced by simple dashes.
	 * 
	 * @param path
	 *            the original path
	 * @return the normalized path
	 */
	public static String cleanPath(String path) {
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR,
				FOLDER_SEPARATOR);
		String[] pathArray = delimitedListToStringArray(pathToUse,
				FOLDER_SEPARATOR);
		List pathElements = new LinkedList();
		int tops = 0;
		for (int i = pathArray.length - 1; i >= 0; i--) {
			if (CURRENT_PATH.equals(pathArray[i])) {
				// do nothing
			} else if (TOP_PATH.equals(pathArray[i])) {
				tops++;
			} else {
				if (tops > 0) {
					tops--;
				} else {
					pathElements.add(0, pathArray[i]);
				}
			}
		}
		return collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * Compare two paths after normalization of them.
	 * 
	 * @param path1
	 *            First path for comparizon
	 * @param path2
	 *            Second path for comparizon
	 * @return True if the two paths are equivalent after normalization
	 */
	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	/**
	 * Parse the given locale string into a <code>java.util.Locale</code>.
	 * This is the inverse operation of Locale's <code>toString</code>.
	 * 
	 * @param localeString
	 *            the locale string, following <code>java.util.Locale</code>'s
	 *            toString format ("en", "en_UK", etc). Also accepts spaces as
	 *            separators, as alternative to underscores.
	 * @return a corresponding Locale instance
	 */
	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = parts.length > 0 ? parts[0] : "";
		String country = parts.length > 1 ? parts[1] : "";
		String variant = parts.length > 2 ? parts[2] : "";
		return (language.length() > 0 ? new Locale(language, country, variant)
				: null);
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with String arrays
	// ---------------------------------------------------------------------

	/**
	 * Append the given String to the given String array, returning a new array
	 * consisting of the input array contents plus the given String.
	 * 
	 * @param arr
	 *            the array to append to
	 * @param str
	 *            the String to append
	 * @return the new array
	 */
	public static String[] addStringToArray(String[] arr, String str) {
		String[] newArr = new String[arr.length + 1];
		System.arraycopy(arr, 0, newArr, 0, arr.length);
		newArr[arr.length] = str;
		return newArr;
	}

	/**
	 * Turn given source String array into sorted array.
	 * 
	 * @param source
	 *            the source array
	 * @return the sorted array (never <code>null</code>)
	 */
	public static String[] sortStringArray(String[] source) {
		if (source == null) {
			return new String[0];
		}
		Arrays.sort(source);
		return source;
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * Trims tokens and omits empty tokens.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * 
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those
	 *            characters is individually considered as delimiter).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * 
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those
	 *            characters is individually considered as delimiter)
	 * @param trimTokens
	 *            trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens
	 *            omit empty tokens from the result array (only applies to
	 *            tokens that are empty after trimming; StringTokenizer will not
	 *            consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters,
			boolean trimTokens, boolean ignoreEmptyTokens) {

		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return (String[]) tokens.toArray(new String[tokens.size()]);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of
	 * potential delimiter characters - in contrast to
	 * <code>tokenizeToStringArray</code>.
	 * 
	 * @param str
	 *            the input String
	 * @param delimiter
	 *            the delimiter between elements (this is a single delimiter,
	 *            rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str,
			String delimiter) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}

		List result = new ArrayList();
		int pos = 0;
		int delPos = 0;
		while ((delPos = str.indexOf(delimiter, pos)) != -1) {
			result.add(str.substring(pos, delPos));
			pos = delPos + delimiter.length();
		}
		if (str.length() > 0 && pos <= str.length()) {
			// Add rest of String, but not in case of empty input.
			result.add(str.substring(pos));
		}

		return (String[]) result.toArray(new String[result.size()]);
	}

	/**
	 * �����ŷָ����ַ���Ϊ�ַ����
	 * 
	 * @param str
	 *            ���ŷָ����ַ�
	 * @return �ַ�����(����Ϊ<code>null</code>ʱ������empty����)
	 */
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * �����ŷָ����ַ�𣬰����ŷָ��ֲ�Ϊ�ַ�ļ���(�˷�����ȥ���ظ��ַ�)
	 * 
	 * @param str
	 *            ���ŷָ����ַ�
	 * @return �ַ���
	 */
	public static Set commaDelimitedListToSet(String str) {
		Set set = new TreeSet();
		String[] tokens = commaDelimitedListToStringArray(str);
		for (int i = 0; i < tokens.length; i++) {
			set.add(tokens[i]);
		}
		return set;
	}

	/**
	 * ��������Ԫ����������ָ���delim�ָ����ַ���ʽ
	 * 
	 * @param arr
	 *            ��Ӧ����
	 * @param delim
	 *            �ָ���
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (arr == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * ��������Ԫ����������ָ���delim�ָ����ַ���ʽ(���ÿ��Ԫ�ؿɸ���ǰ׺����׺)
	 * 
	 * <pre>
	 * collectionToDelimitedString(coll,&quot;,&quot;,&quot;PREFIX&quot;,&quot;SUFFIX&quot;)=&gt;PREFIXcoll[0]SUFFIX,PREFIXcoll[1]SUFFIX,PREFIXcoll[2]SUFFIX
	 * </pre>
	 * 
	 * @param coll
	 *            ��Ӧ����
	 * @param delim
	 *            �ָ���
	 * @param prefix
	 *            ǰ׺
	 * @param suffix
	 *            ��׺
	 */
	public static String collectionToDelimitedString(Collection coll,
			String delim, String prefix, String suffix) {
		if (coll == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		Iterator it = coll.iterator();
		int i = 0;
		while (it.hasNext()) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(prefix).append(it.next()).append(suffix);
			i++;
		}
		return sb.toString();
	}

	/**
	 * ��������Ԫ����������ָ���delim�ָ����ַ���ʽ
	 * 
	 * @param coll
	 *            ��Ӧ����
	 * @param delim
	 *            �ָ���
	 * @param prefix
	 *            ǰ׺
	 * @param suffix
	 *            ��׺
	 */
	public static String collectionToDelimitedString(Collection coll,
			String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * �����Ԫ����Ϊ���ŷָ�������ֵ������������Ԫ�ض�Ӧ��toString������ȡ��Ӧ���ֵ
	 * 
	 * @param arr
	 *            ��Ӧ����
	 * @return ���ŷָ���Ԫ��
	 */
	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	/**
	 * �������е�Ԫ�ع���Ϊ���ŷָ�������ֵ�����ü�����Ԫ�ض�Ӧ��toString������ȡ��Ӧ���ֵ
	 * 
	 * @param coll
	 *            ����
	 * @return ���ŷָ���Ԫ��
	 */
	public static String collectionToCommaDelimitedString(Collection coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * The empty String <code>""</code>.
	 * 
	 * @since 2.0
	 */
	public static final String EMPTY = "";

	/**
	 * Represents a failed index search.
	 * 
	 * @since 2.1
	 */
	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * <p>
	 * The maximum size to which the padding constant(s) can expand.
	 * </p>
	 */
	private static final int PAD_LIMIT = 8192;
	

	// Empty checks
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if a String is empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isEmpty(null)      = true
	 * StringUtil.isEmpty(&quot;&quot;)        = true
	 * StringUtil.isEmpty(&quot; &quot;)       = false
	 * StringUtil.isEmpty(&quot;bob&quot;)     = false
	 * StringUtil.isEmpty(&quot;  bob  &quot;) = false
	 * </pre>
	 * 
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the
	 * String. That functionality is available in isBlank().
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * <p>
	 * Checks if a String is not empty ("") and not null.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isNotEmpty(null)      = false
	 * StringUtil.isNotEmpty(&quot;&quot;)        = false
	 * StringUtil.isNotEmpty(&quot; &quot;)       = true
	 * StringUtil.isNotEmpty(&quot;bob&quot;)     = true
	 * StringUtil.isNotEmpty(&quot;  bob  &quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null
	 */
	public static boolean isNotEmpty(String str) {
		return !StringUtil.isEmpty(str);
	}

	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isBlank(null)      = true
	 * StringUtil.isBlank(&quot;&quot;)        = true
	 * StringUtil.isBlank(&quot; &quot;)       = true
	 * StringUtil.isBlank(&quot;bob&quot;)     = false
	 * StringUtil.isBlank(&quot;  bob  &quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 * @since 2.0
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if a String is not empty (""), not null and not whitespace only.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isNotBlank(null)      = false
	 * StringUtil.isNotBlank(&quot;&quot;)        = false
	 * StringUtil.isNotBlank(&quot; &quot;)       = false
	 * StringUtil.isNotBlank(&quot;bob&quot;)     = true
	 * StringUtil.isNotBlank(&quot;  bob  &quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null and
	 *         not whitespace
	 * @since 2.0
	 */
	public static boolean isNotBlank(String str) {
		return !StringUtil.isBlank(str);
	}

	// Trim
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String,
	 * handling <code>null</code> by returning an empty String ("").
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.clean(null)          = &quot;&quot;
	 * StringUtil.clean(&quot;&quot;)            = &quot;&quot;
	 * StringUtil.clean(&quot;abc&quot;)         = &quot;abc&quot;
	 * StringUtil.clean(&quot;    abc    &quot;) = &quot;abc&quot;
	 * StringUtil.clean(&quot;     &quot;)       = &quot;&quot;
	 * </pre>
	 * 
	 * @see java.lang.String#trim()
	 * @param str
	 *            the String to clean, may be null
	 * @return the trimmed text, never <code>null</code>
	 * @deprecated Use the clearer named {@link #trimToEmpty(String)}. Method
	 *             will be removed in Commons Lang 3.0.
	 */
	public static String clean(String str) {
		return str == null ? EMPTY : str.trim();
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String,
	 * handling <code>null</code> by returning <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start
	 * and end characters &lt;= 32. To strip whitespace use
	 * {@link #strip(String)}.
	 * </p>
	 * 
	 * <p>
	 * To trim your choice of characters, use the {@link #strip(String, String)}
	 * methods.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.trim(null)          = null
	 * StringUtil.trim(&quot;&quot;)            = &quot;&quot;
	 * StringUtil.trim(&quot;     &quot;)       = &quot;&quot;
	 * StringUtil.trim(&quot;abc&quot;)         = &quot;abc&quot;
	 * StringUtil.trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be trimmed, may be null
	 * @return the trimmed string, <code>null</code> if null String input
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String
	 * returning <code>null</code> if the String is empty ("") after the trim
	 * or if it is <code>null</code>.
	 * 
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start
	 * and end characters &lt;= 32. To strip whitespace use
	 * {@link #stripToNull(String)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.trimToNull(null)          = null
	 * StringUtil.trimToNull(&quot;&quot;)            = null
	 * StringUtil.trimToNull(&quot;     &quot;)       = null
	 * StringUtil.trimToNull(&quot;abc&quot;)         = &quot;abc&quot;
	 * StringUtil.trimToNull(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be trimmed, may be null
	 * @return the trimmed String, <code>null</code> if only chars &lt;= 32,
	 *         empty or null String input
	 * @since 2.0
	 */
	public static String trimToNull(String str) {
		String ts = trim(str);
		return isEmpty(ts) ? null : ts;
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String
	 * returning an empty String ("") if the String is empty ("") after the trim
	 * or if it is <code>null</code>.
	 * 
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start
	 * and end characters &lt;= 32. To strip whitespace use
	 * {@link #stripToEmpty(String)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.trimToEmpty(null)          = &quot;&quot;
	 * StringUtil.trimToEmpty(&quot;&quot;)            = &quot;&quot;
	 * StringUtil.trimToEmpty(&quot;     &quot;)       = &quot;&quot;
	 * StringUtil.trimToEmpty(&quot;abc&quot;)         = &quot;abc&quot;
	 * StringUtil.trimToEmpty(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be trimmed, may be null
	 * @return the trimmed String, or an empty String if <code>null</code>
	 *         input
	 * @since 2.0
	 */
	public static String trimToEmpty(String str) {
		return str == null ? EMPTY : str.trim();
	}

	// Stripping
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Strips whitespace from the start and end of a String.
	 * </p>
	 * 
	 * <p>
	 * This is similar to {@link #trim(String)} but removes whitespace.
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.strip(null)     = null
	 * StringUtil.strip(&quot;&quot;)       = &quot;&quot;
	 * StringUtil.strip(&quot;   &quot;)    = &quot;&quot;
	 * StringUtil.strip(&quot;abc&quot;)    = &quot;abc&quot;
	 * StringUtil.strip(&quot;  abc&quot;)  = &quot;abc&quot;
	 * StringUtil.strip(&quot;abc  &quot;)  = &quot;abc&quot;
	 * StringUtil.strip(&quot; abc &quot;)  = &quot;abc&quot;
	 * StringUtil.strip(&quot; ab c &quot;) = &quot;ab c&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to remove whitespace from, may be null
	 * @return the stripped String, <code>null</code> if null String input
	 */
	public static String strip(String str) {
		return strip(str, null);
	}

	/**
	 * <p>
	 * Strips whitespace from the start and end of a String returning
	 * <code>null</code> if the String is empty ("") after the strip.
	 * </p>
	 * 
	 * <p>
	 * This is similar to {@link #trimToNull(String)} but removes whitespace.
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.stripToNull(null)     = null
	 * StringUtil.stripToNull(&quot;&quot;)       = null
	 * StringUtil.stripToNull(&quot;   &quot;)    = null
	 * StringUtil.stripToNull(&quot;abc&quot;)    = &quot;abc&quot;
	 * StringUtil.stripToNull(&quot;  abc&quot;)  = &quot;abc&quot;
	 * StringUtil.stripToNull(&quot;abc  &quot;)  = &quot;abc&quot;
	 * StringUtil.stripToNull(&quot; abc &quot;)  = &quot;abc&quot;
	 * StringUtil.stripToNull(&quot; ab c &quot;) = &quot;ab c&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be stripped, may be null
	 * @return the stripped String, <code>null</code> if whitespace, empty or
	 *         null String input
	 * @since 2.0
	 */
	public static String stripToNull(String str) {
		if (str == null) {
			return null;
		}
		str = strip(str, null);
		return str.length() == 0 ? null : str;
	}

	/**
	 * <p>
	 * Strips whitespace from the start and end of a String returning an empty
	 * String if <code>null</code> input.
	 * </p>
	 * 
	 * <p>
	 * This is similar to {@link #trimToEmpty(String)} but removes whitespace.
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.stripToEmpty(null)     = &quot;&quot;
	 * StringUtil.stripToEmpty(&quot;&quot;)       = &quot;&quot;
	 * StringUtil.stripToEmpty(&quot;   &quot;)    = &quot;&quot;
	 * StringUtil.stripToEmpty(&quot;abc&quot;)    = &quot;abc&quot;
	 * StringUtil.stripToEmpty(&quot;  abc&quot;)  = &quot;abc&quot;
	 * StringUtil.stripToEmpty(&quot;abc  &quot;)  = &quot;abc&quot;
	 * StringUtil.stripToEmpty(&quot; abc &quot;)  = &quot;abc&quot;
	 * StringUtil.stripToEmpty(&quot; ab c &quot;) = &quot;ab c&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be stripped, may be null
	 * @return the trimmed String, or an empty String if <code>null</code>
	 *         input
	 * @since 2.0
	 */
	public static String stripToEmpty(String str) {
		return str == null ? EMPTY : strip(str, null);
	}

	/**
	 * <p>
	 * Strips any of a set of characters from the start and end of a String.
	 * This is similar to {@link String#trim()} but allows the characters to be
	 * stripped to be controlled.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. An empty
	 * string ("") input returns the empty string.
	 * </p>
	 * 
	 * <p>
	 * If the stripChars String is <code>null</code>, whitespace is stripped
	 * as defined by {@link Character#isWhitespace(char)}. Alternatively use
	 * {@link #strip(String)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.strip(null, *)          = null
	 * StringUtil.strip(&quot;&quot;, *)            = &quot;&quot;
	 * StringUtil.strip(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.strip(&quot;  abc&quot;, null)    = &quot;abc&quot;
	 * StringUtil.strip(&quot;abc  &quot;, null)    = &quot;abc&quot;
	 * StringUtil.strip(&quot; abc &quot;, null)    = &quot;abc&quot;
	 * StringUtil.strip(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to remove characters from, may be null
	 * @param stripChars
	 *            the characters to remove, null treated as whitespace
	 * @return the stripped String, <code>null</code> if null String input
	 */
	public static String strip(String str, String stripChars) {
		if (isEmpty(str)) {
			return str;
		}
		str = stripStart(str, stripChars);
		return stripEnd(str, stripChars);
	}

	/**
	 * <p>
	 * Strips any of a set of characters from the start of a String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. An empty
	 * string ("") input returns the empty string.
	 * </p>
	 * 
	 * <p>
	 * If the stripChars String is <code>null</code>, whitespace is stripped
	 * as defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.stripStart(null, *)          = null
	 * StringUtil.stripStart(&quot;&quot;, *)            = &quot;&quot;
	 * StringUtil.stripStart(&quot;abc&quot;, &quot;&quot;)        = &quot;abc&quot;
	 * StringUtil.stripStart(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.stripStart(&quot;  abc&quot;, null)    = &quot;abc&quot;
	 * StringUtil.stripStart(&quot;abc  &quot;, null)    = &quot;abc  &quot;
	 * StringUtil.stripStart(&quot; abc &quot;, null)    = &quot;abc &quot;
	 * StringUtil.stripStart(&quot;yxabc  &quot;, &quot;xyz&quot;) = &quot;abc  &quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to remove characters from, may be null
	 * @param stripChars
	 *            the characters to remove, null treated as whitespace
	 * @return the stripped String, <code>null</code> if null String input
	 */
	public static String stripStart(String str, String stripChars) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		int start = 0;
		if (stripChars == null) {
			while ((start != strLen)
					&& Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((start != strLen)
					&& (stripChars.indexOf(str.charAt(start)) != -1)) {
				start++;
			}
		}
		return str.substring(start);
	}

	/**
	 * <p>
	 * Strips any of a set of characters from the end of a String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. An empty
	 * string ("") input returns the empty string.
	 * </p>
	 * 
	 * <p>
	 * If the stripChars String is <code>null</code>, whitespace is stripped
	 * as defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.stripEnd(null, *)          = null
	 * StringUtil.stripEnd(&quot;&quot;, *)            = &quot;&quot;
	 * StringUtil.stripEnd(&quot;abc&quot;, &quot;&quot;)        = &quot;abc&quot;
	 * StringUtil.stripEnd(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.stripEnd(&quot;  abc&quot;, null)    = &quot;  abc&quot;
	 * StringUtil.stripEnd(&quot;abc  &quot;, null)    = &quot;abc&quot;
	 * StringUtil.stripEnd(&quot; abc &quot;, null)    = &quot; abc&quot;
	 * StringUtil.stripEnd(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to remove characters from, may be null
	 * @param stripChars
	 *            the characters to remove, null treated as whitespace
	 * @return the stripped String, <code>null</code> if null String input
	 */
	public static String stripEnd(String str, String stripChars) {
		int end;
		if (str == null || (end = str.length()) == 0) {
			return str;
		}

		if (stripChars == null) {
			while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((end != 0)
					&& (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
				end--;
			}
		}
		return str.substring(0, end);
	}

	// StripAll
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Strips whitespace from the start and end of every String in an array.
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <p>
	 * A new array is returned each time, except for length zero. A
	 * <code>null</code> array will return <code>null</code>. An empty
	 * array will return itself. A <code>null</code> array entry will be
	 * ignored.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.stripAll(null)             = null
	 * StringUtil.stripAll([])               = []
	 * StringUtil.stripAll([&quot;abc&quot;, &quot;  abc&quot;]) = [&quot;abc&quot;, &quot;abc&quot;]
	 * StringUtil.stripAll([&quot;abc  &quot;, null])  = [&quot;abc&quot;, null]
	 * </pre>
	 * 
	 * @param strs
	 *            the array to remove whitespace from, may be null
	 * @return the stripped Strings, <code>null</code> if null array input
	 */
	public static String[] stripAll(String[] strs) {
		return stripAll(strs, null);
	}

	/**
	 * <p>
	 * Strips any of a set of characters from the start and end of every String
	 * in an array.
	 * </p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <p>
	 * A new array is returned each time, except for length zero. A
	 * <code>null</code> array will return <code>null</code>. An empty
	 * array will return itself. A <code>null</code> array entry will be
	 * ignored. A <code>null</code> stripChars will strip whitespace as
	 * defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.stripAll(null, *)                = null
	 * StringUtil.stripAll([], *)                  = []
	 * StringUtil.stripAll([&quot;abc&quot;, &quot;  abc&quot;], null) = [&quot;abc&quot;, &quot;abc&quot;]
	 * StringUtil.stripAll([&quot;abc  &quot;, null], null)  = [&quot;abc&quot;, null]
	 * StringUtil.stripAll([&quot;abc  &quot;, null], &quot;yz&quot;)  = [&quot;abc  &quot;, null]
	 * StringUtil.stripAll([&quot;yabcz&quot;, null], &quot;yz&quot;)  = [&quot;abc&quot;, null]
	 * </pre>
	 * 
	 * @param strs
	 *            the array to remove characters from, may be null
	 * @param stripChars
	 *            the characters to remove, null treated as whitespace
	 * @return the stripped Strings, <code>null</code> if null array input
	 */
	public static String[] stripAll(String[] strs, String stripChars) {
		int strsLen;
		if (strs == null || (strsLen = strs.length) == 0) {
			return strs;
		}
		String[] newArr = new String[strsLen];
		for (int i = 0; i < strsLen; i++) {
			newArr[i] = strip(strs[i], stripChars);
		}
		return newArr;
	}

	// Equals
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are equal.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two
	 * <code>null</code> references are considered to be equal. The comparison
	 * is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.equals(null, null)   = true
	 * StringUtil.equals(null, &quot;abc&quot;)  = false
	 * StringUtil.equals(&quot;abc&quot;, null)  = false
	 * StringUtil.equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * StringUtil.equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 * 
	 * @see java.lang.String#equals(Object)
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case sensitive, or
	 *         both <code>null</code>
	 */
	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are equal
	 * ignoring the case.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two
	 * <code>null</code> references are considered equal. Comparison is case
	 * insensitive.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.equalsIgnoreCase(null, null)   = true
	 * StringUtil.equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * StringUtil.equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * StringUtil.equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * StringUtil.equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 * 
	 * @see java.lang.String#equalsIgnoreCase(String)
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case insensitive,
	 *         or both <code>null</code>
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	}

	// IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>.
	 * This method uses {@link String#indexOf(int)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOf(null, *)         = -1
	 * StringUtil.indexOf(&quot;&quot;, *)           = -1
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, 'a') = 0
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b') = 2
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @return the first index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, char searchChar) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.indexOf(searchChar);
	}

	/**
	 * <p>
	 * Finds the first index within a String from a start position, handling
	 * <code>null</code>. This method uses {@link String#indexOf(int, int)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * A negative start position is treated as zero. A start position greater
	 * than the string length returns <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOf(null, *, *)          = -1
	 * StringUtil.indexOf(&quot;&quot;, *, *)            = -1
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b', 0)  = 2
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b', 3)  = 5
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b', 9)  = -1
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b', -1) = 2
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @param startPos
	 *            the start position, negative treated as zero
	 * @return the first index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, char searchChar, int startPos) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.indexOf(searchChar, startPos);
	}

	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>.
	 * This method uses {@link String#indexOf(String)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOf(null, *)          = -1
	 * StringUtil.indexOf(*, null)          = -1
	 * StringUtil.indexOf(&quot;&quot;, &quot;&quot;)           = 0
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;a&quot;)  = 0
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;)  = 2
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;ab&quot;) = 1
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;&quot;)   = 0
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.indexOf(searchStr);
	}

	/**
	 * <p>
	 * Finds the n-th index within a String, handling <code>null</code>. This
	 * method uses {@link String#indexOf(String)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.ordinalIndexOf(null, *, *)          = -1
	 * StringUtil.ordinalIndexOf(*, null, *)          = -1
	 * StringUtil.ordinalIndexOf(&quot;&quot;, &quot;&quot;, *)           = 0
	 * StringUtil.ordinalIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 1)  = 0
	 * StringUtil.ordinalIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 2)  = 1
	 * StringUtil.ordinalIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 1)  = 2
	 * StringUtil.ordinalIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 2)  = 5
	 * StringUtil.ordinalIndexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 1) = 1
	 * StringUtil.ordinalIndexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 2) = 4
	 * StringUtil.ordinalIndexOf(&quot;aabaabaa&quot;, &quot;&quot;, 1)   = 0
	 * StringUtil.ordinalIndexOf(&quot;aabaabaa&quot;, &quot;&quot;, 2)   = 0
	 * </pre>
	 * 
	 * <p>
	 * Note that 'head(String str, int n)' may be implemented as:
	 * </p>
	 * 
	 * <pre>
	 * str.substring(0, lastOrdinalIndexOf(str, &quot;\n&quot;, n))
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @param ordinal
	 *            the n-th <code>searchStr</code> to find
	 * @return the n-th index of the search String, <code>-1</code> (<code>INDEX_NOT_FOUND</code>)
	 *         if no match or <code>null</code> string input
	 * @since 2.1
	 */
	public static int ordinalIndexOf(String str, String searchStr, int ordinal) {
		return ordinalIndexOf(str, searchStr, ordinal, false);
	}

	/**
	 * <p>
	 * Finds the n-th index within a String, handling <code>null</code>. This
	 * method uses {@link String#indexOf(String)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>.
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @param ordinal
	 *            the n-th <code>searchStr</code> to find
	 * @param lastIndex
	 *            true if lastOrdinalIndexOf() otherwise false if
	 *            ordinalIndexOf()
	 * @return the n-th index of the search String, <code>-1</code> (<code>INDEX_NOT_FOUND</code>)
	 *         if no match or <code>null</code> string input
	 */
	// Shared code between ordinalIndexOf(String,String,int) and
	// lastOrdinalIndexOf(String,String,int)
	private static int ordinalIndexOf(String str, String searchStr,
			int ordinal, boolean lastIndex) {
		if (str == null || searchStr == null || ordinal <= 0) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return lastIndex ? str.length() : 0;
		}
		int found = 0;
		int index = lastIndex ? str.length() : INDEX_NOT_FOUND;
		do {
			if (lastIndex) {
				index = str.lastIndexOf(searchStr, index - 1);
			} else {
				index = str.indexOf(searchStr, index + 1);
			}
			if (index < 0) {
				return index;
			}
			found++;
		} while (found < ordinal);
		return index;
	}

	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>.
	 * This method uses {@link String#indexOf(String, int)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A negative
	 * start position is treated as zero. An empty ("") search String always
	 * matches. A start position greater than the string length only matches an
	 * empty search String.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOf(null, *, *)          = -1
	 * StringUtil.indexOf(*, null, *)          = -1
	 * StringUtil.indexOf(&quot;&quot;, &quot;&quot;, 0)           = 0
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 0)  = 0
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 0)  = 2
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 0) = 1
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 3)  = 5
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 9)  = -1
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, -1) = 2
	 * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;&quot;, 2)   = 2
	 * StringUtil.indexOf(&quot;abc&quot;, &quot;&quot;, 9)        = 3
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @param startPos
	 *            the start position, negative treated as zero
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, String searchStr, int startPos) {
		if (str == null || searchStr == null) {
			return -1;
		}
		// JDK1.2/JDK1.3 have a bug, when startPos > str.length for "", hence
		if (searchStr.length() == 0 && startPos >= str.length()) {
			return str.length();
		}
		return str.indexOf(searchStr, startPos);
	}

	/**
	 * <p>
	 * Case in-sensitive find of the first index within a String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A negative
	 * start position is treated as zero. An empty ("") search String always
	 * matches. A start position greater than the string length only matches an
	 * empty search String.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOfIgnoreCase(null, *)          = -1
	 * StringUtil.indexOfIgnoreCase(*, null)          = -1
	 * StringUtil.indexOfIgnoreCase(&quot;&quot;, &quot;&quot;)           = 0
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;a&quot;)  = 0
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;b&quot;)  = 2
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;ab&quot;) = 1
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.5
	 */
	public static int indexOfIgnoreCase(String str, String searchStr) {
		return indexOfIgnoreCase(str, searchStr, 0);
	}

	/**
	 * <p>
	 * Case in-sensitive find of the first index within a String from the
	 * specified position.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A negative
	 * start position is treated as zero. An empty ("") search String always
	 * matches. A start position greater than the string length only matches an
	 * empty search String.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOfIgnoreCase(null, *, *)          = -1
	 * StringUtil.indexOfIgnoreCase(*, null, *)          = -1
	 * StringUtil.indexOfIgnoreCase(&quot;&quot;, &quot;&quot;, 0)           = 0
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;A&quot;, 0)  = 0
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;B&quot;, 0)  = 2
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;AB&quot;, 0) = 1
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;B&quot;, 3)  = 5
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;B&quot;, 9)  = -1
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;B&quot;, -1) = 2
	 * StringUtil.indexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;&quot;, 2)   = 2
	 * StringUtil.indexOfIgnoreCase(&quot;abc&quot;, &quot;&quot;, 9)        = 3
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @param startPos
	 *            the start position, negative treated as zero
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.5
	 */
	public static int indexOfIgnoreCase(String str, String searchStr,
			int startPos) {
		if (str == null || searchStr == null) {
			return -1;
		}
		if (startPos < 0) {
			startPos = 0;
		}
		int endLimit = (str.length() - searchStr.length()) + 1;
		if (startPos > endLimit) {
			return -1;
		}
		if (searchStr.length() == 0) {
			return startPos;
		}
		for (int i = startPos; i < endLimit; i++) {
			if (str.regionMatches(true, i, searchStr, 0, searchStr.length())) {
				return i;
			}
		}
		return -1;
	}

	// LastIndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the last index within a String, handling <code>null</code>. This
	 * method uses {@link String#lastIndexOf(int)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lastIndexOf(null, *)         = -1
	 * StringUtil.lastIndexOf(&quot;&quot;, *)           = -1
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'a') = 7
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b') = 5
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @return the last index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, char searchChar) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.lastIndexOf(searchChar);
	}

	/**
	 * <p>
	 * Finds the last index within a String from a start position, handling
	 * <code>null</code>. This method uses
	 * {@link String#lastIndexOf(int, int)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * A negative start position returns <code>-1</code>. A start position
	 * greater than the string length searches the whole string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lastIndexOf(null, *, *)          = -1
	 * StringUtil.lastIndexOf(&quot;&quot;, *,  *)           = -1
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', 8)  = 5
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', 4)  = 2
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', 0)  = -1
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', 9)  = 5
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', -1) = -1
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'a', 0)  = 0
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @param startPos
	 *            the start position
	 * @return the last index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, char searchChar, int startPos) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.lastIndexOf(searchChar, startPos);
	}

	/**
	 * <p>
	 * Finds the last index within a String, handling <code>null</code>. This
	 * method uses {@link String#lastIndexOf(String)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lastIndexOf(null, *)          = -1
	 * StringUtil.lastIndexOf(*, null)          = -1
	 * StringUtil.lastIndexOf(&quot;&quot;, &quot;&quot;)           = 0
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;)  = 0
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;)  = 2
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;ab&quot;) = 1
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;&quot;)   = 8
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return the last index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.lastIndexOf(searchStr);
	}

	/**
	 * <p>
	 * Finds the n-th last index within a String, handling <code>null</code>.
	 * This method uses {@link String#lastIndexOf(String)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lastOrdinalIndexOf(null, *, *)          = -1
	 * StringUtil.lastOrdinalIndexOf(*, null, *)          = -1
	 * StringUtil.lastOrdinalIndexOf(&quot;&quot;, &quot;&quot;, *)           = 0
	 * StringUtil.lastOrdinalIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 1)  = 7
	 * StringUtil.lastOrdinalIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 2)  = 6
	 * StringUtil.lastOrdinalIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 1)  = 5
	 * StringUtil.lastOrdinalIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 2)  = 2
	 * StringUtil.lastOrdinalIndexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 1) = 4
	 * StringUtil.lastOrdinalIndexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 2) = 1
	 * StringUtil.lastOrdinalIndexOf(&quot;aabaabaa&quot;, &quot;&quot;, 1)   = 8
	 * StringUtil.lastOrdinalIndexOf(&quot;aabaabaa&quot;, &quot;&quot;, 2)   = 8
	 * </pre>
	 * 
	 * <p>
	 * Note that 'tail(String str, int n)' may be implemented as:
	 * </p>
	 * 
	 * <pre>
	 * str.substring(lastOrdinalIndexOf(str, &quot;\n&quot;, n) + 1)
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @param ordinal
	 *            the n-th last <code>searchStr</code> to find
	 * @return the n-th last index of the search String, <code>-1</code> (<code>INDEX_NOT_FOUND</code>)
	 *         if no match or <code>null</code> string input
	 * @since 2.5
	 */
	public static int lastOrdinalIndexOf(String str, String searchStr,
			int ordinal) {
		return ordinalIndexOf(str, searchStr, ordinal, true);
	}

	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>.
	 * This method uses {@link String#lastIndexOf(String, int)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A negative
	 * start position returns <code>-1</code>. An empty ("") search String
	 * always matches unless the start position is negative. A start position
	 * greater than the string length searches the whole string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lastIndexOf(null, *, *)          = -1
	 * StringUtil.lastIndexOf(*, null, *)          = -1
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 8)  = 7
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 8)  = 5
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 8) = 4
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 9)  = 5
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, -1) = -1
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 0)  = 0
	 * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 0)  = -1
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @param startPos
	 *            the start position, negative treated as zero
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, String searchStr, int startPos) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.lastIndexOf(searchStr, startPos);
	}

	/**
	 * <p>
	 * Case in-sensitive find of the last index within a String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A negative
	 * start position returns <code>-1</code>. An empty ("") search String
	 * always matches unless the start position is negative. A start position
	 * greater than the string length searches the whole string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lastIndexOfIgnoreCase(null, *)          = -1
	 * StringUtil.lastIndexOfIgnoreCase(*, null)          = -1
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;A&quot;)  = 7
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;B&quot;)  = 5
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;AB&quot;) = 4
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.5
	 */
	public static int lastIndexOfIgnoreCase(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return lastIndexOfIgnoreCase(str, searchStr, str.length());
	}

	/**
	 * <p>
	 * Case in-sensitive find of the last index within a String from the
	 * specified position.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A negative
	 * start position returns <code>-1</code>. An empty ("") search String
	 * always matches unless the start position is negative. A start position
	 * greater than the string length searches the whole string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lastIndexOfIgnoreCase(null, *, *)          = -1
	 * StringUtil.lastIndexOfIgnoreCase(*, null, *)          = -1
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;A&quot;, 8)  = 7
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;B&quot;, 8)  = 5
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;AB&quot;, 8) = 4
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;B&quot;, 9)  = 5
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;B&quot;, -1) = -1
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;A&quot;, 0)  = 0
	 * StringUtil.lastIndexOfIgnoreCase(&quot;aabaabaa&quot;, &quot;B&quot;, 0)  = -1
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @param startPos
	 *            the start position
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.5
	 */
	public static int lastIndexOfIgnoreCase(String str, String searchStr,
			int startPos) {
		if (str == null || searchStr == null) {
			return -1;
		}
		if (startPos > (str.length() - searchStr.length())) {
			startPos = str.length() - searchStr.length();
		}
		if (startPos < 0) {
			return -1;
		}
		if (searchStr.length() == 0) {
			return startPos;
		}

		for (int i = startPos; i >= 0; i--) {
			if (str.regionMatches(true, i, searchStr, 0, searchStr.length())) {
				return i;
			}
		}
		return -1;
	}

	// Contains
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if String contains a search character, handling <code>null</code>.
	 * This method uses {@link String#indexOf(int)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>false</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.contains(null, *)    = false
	 * StringUtil.contains(&quot;&quot;, *)      = false
	 * StringUtil.contains(&quot;abc&quot;, 'a') = true
	 * StringUtil.contains(&quot;abc&quot;, 'z') = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @return true if the String contains the search character, false if not or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static boolean contains(String str, char searchChar) {
		if (isEmpty(str)) {
			return false;
		}
		return str.indexOf(searchChar) >= 0;
	}

	/**
	 * <p>
	 * Checks if String contains a search String, handling <code>null</code>.
	 * This method uses {@link String#indexOf(String)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>false</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.contains(null, *)     = false
	 * StringUtil.contains(*, null)     = false
	 * StringUtil.contains(&quot;&quot;, &quot;&quot;)      = true
	 * StringUtil.contains(&quot;abc&quot;, &quot;&quot;)   = true
	 * StringUtil.contains(&quot;abc&quot;, &quot;a&quot;)  = true
	 * StringUtil.contains(&quot;abc&quot;, &quot;z&quot;)  = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return true if the String contains the search String, false if not or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static boolean contains(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		return str.indexOf(searchStr) >= 0;
	}

	/**
	 * <p>
	 * Checks if String contains a search String irrespective of case, handling
	 * <code>null</code>. Case-insensitivity is defined as by
	 * {@link String#equalsIgnoreCase(String)}.
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>false</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.contains(null, *) = false
	 * StringUtil.contains(*, null) = false
	 * StringUtil.contains(&quot;&quot;, &quot;&quot;) = true
	 * StringUtil.contains(&quot;abc&quot;, &quot;&quot;) = true
	 * StringUtil.contains(&quot;abc&quot;, &quot;a&quot;) = true
	 * StringUtil.contains(&quot;abc&quot;, &quot;z&quot;) = false
	 * StringUtil.contains(&quot;abc&quot;, &quot;A&quot;) = true
	 * StringUtil.contains(&quot;abc&quot;, &quot;Z&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return true if the String contains the search String irrespective of
	 *         case or false if not or <code>null</code> string input
	 */
	public static boolean containsIgnoreCase(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		int len = searchStr.length();
		int max = str.length() - len;
		for (int i = 0; i <= max; i++) {
			if (str.regionMatches(true, i, searchStr, 0, len)) {
				return true;
			}
		}
		return false;
	}

	// ContainsAny
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the String contains any character in the given set of
	 * characters.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>false</code>. A
	 * <code>null</code> or zero length search array will return
	 * <code>false</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.containsAny(null, *)                = false
	 * StringUtil.containsAny(&quot;&quot;, *)                  = false
	 * StringUtil.containsAny(*, null)                = false
	 * StringUtil.containsAny(*, [])                  = false
	 * StringUtil.containsAny(&quot;zzabyycdxx&quot;,['z','a']) = true
	 * StringUtil.containsAny(&quot;zzabyycdxx&quot;,['b','y']) = true
	 * StringUtil.containsAny(&quot;aba&quot;, ['z'])           = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChars
	 *            the chars to search for, may be null
	 * @return the <code>true</code> if any of the chars are found,
	 *         <code>false</code> if no match or null input
	 * @since 2.4
	 */
	public static boolean containsAny(String str, char[] searchChars) {
		if (str == null || str.length() == 0 || searchChars == null
				|| searchChars.length == 0) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			for (int j = 0; j < searchChars.length; j++) {
				if (searchChars[j] == ch) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if the String contains any character in the given set of
	 * characters.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>false</code>. A
	 * <code>null</code> search string will return <code>false</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.containsAny(null, *)            = false
	 * StringUtil.containsAny(&quot;&quot;, *)              = false
	 * StringUtil.containsAny(*, null)            = false
	 * StringUtil.containsAny(*, &quot;&quot;)              = false
	 * StringUtil.containsAny(&quot;zzabyycdxx&quot;, &quot;za&quot;) = true
	 * StringUtil.containsAny(&quot;zzabyycdxx&quot;, &quot;by&quot;) = true
	 * StringUtil.containsAny(&quot;aba&quot;,&quot;z&quot;)          = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChars
	 *            the chars to search for, may be null
	 * @return the <code>true</code> if any of the chars are found,
	 *         <code>false</code> if no match or null input
	 * @since 2.4
	 */
	public static boolean containsAny(String str, String searchChars) {
		if (searchChars == null) {
			return false;
		}
		return containsAny(str, searchChars.toCharArray());
	}

	// IndexOfAnyBut chars
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Search a String to find the first index of any character not in the given
	 * set of characters.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A
	 * <code>null</code> search string will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOfAnyBut(null, *)            = -1
	 * StringUtil.indexOfAnyBut(&quot;&quot;, *)              = -1
	 * StringUtil.indexOfAnyBut(*, null)            = -1
	 * StringUtil.indexOfAnyBut(*, &quot;&quot;)              = -1
	 * StringUtil.indexOfAnyBut(&quot;zzabyycdxx&quot;, &quot;za&quot;) = 3
	 * StringUtil.indexOfAnyBut(&quot;zzabyycdxx&quot;, &quot;&quot;)   = 0
	 * StringUtil.indexOfAnyBut(&quot;aba&quot;,&quot;ab&quot;)         = -1
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChars
	 *            the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 * @since 2.0
	 */
	public static int indexOfAnyBut(String str, String searchChars) {
		if (isEmpty(str) || isEmpty(searchChars)) {
			return -1;
		}
		for (int i = 0; i < str.length(); i++) {
			if (searchChars.indexOf(str.charAt(i)) < 0) {
				return i;
			}
		}
		return -1;
	}

	// ContainsNone
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks that the String does not contain certain characters.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>true</code>. A
	 * <code>null</code> invalid character array will return <code>true</code>.
	 * An empty String ("") always returns true.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.containsNone(null, *)       = true
	 * StringUtil.containsNone(*, null)       = true
	 * StringUtil.containsNone(&quot;&quot;, *)         = true
	 * StringUtil.containsNone(&quot;ab&quot;, '')      = true
	 * StringUtil.containsNone(&quot;abab&quot;, 'xyz') = true
	 * StringUtil.containsNone(&quot;ab1&quot;, 'xyz')  = true
	 * StringUtil.containsNone(&quot;abz&quot;, 'xyz')  = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param invalidChars
	 *            an array of invalid chars, may be null
	 * @return true if it contains none of the invalid chars, or is null
	 * @since 2.0
	 */
	public static boolean containsNone(String str, char[] invalidChars) {
		if (str == null || invalidChars == null) {
			return true;
		}
		int strSize = str.length();
		int validSize = invalidChars.length;
		for (int i = 0; i < strSize; i++) {
			char ch = str.charAt(i);
			for (int j = 0; j < validSize; j++) {
				if (invalidChars[j] == ch) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks that the String does not contain certain characters.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>true</code>. A
	 * <code>null</code> invalid character array will return <code>true</code>.
	 * An empty String ("") always returns true.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.containsNone(null, *)       = true
	 * StringUtil.containsNone(*, null)       = true
	 * StringUtil.containsNone(&quot;&quot;, *)         = true
	 * StringUtil.containsNone(&quot;ab&quot;, &quot;&quot;)      = true
	 * StringUtil.containsNone(&quot;abab&quot;, &quot;xyz&quot;) = true
	 * StringUtil.containsNone(&quot;ab1&quot;, &quot;xyz&quot;)  = true
	 * StringUtil.containsNone(&quot;abz&quot;, &quot;xyz&quot;)  = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param invalidChars
	 *            a String of invalid chars, may be null
	 * @return true if it contains none of the invalid chars, or is null
	 * @since 2.0
	 */
	public static boolean containsNone(String str, String invalidChars) {
		if (str == null || invalidChars == null) {
			return true;
		}
		return containsNone(str, invalidChars.toCharArray());
	}

	// IndexOfAny strings
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Find the first index of any of a set of potential substrings.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A
	 * <code>null</code> or zero length search array will return
	 * <code>-1</code>. A <code>null</code> search array entry will be
	 * ignored, but a search array containing "" will return <code>0</code> if
	 * <code>str</code> is not null. This method uses
	 * {@link String#indexOf(String)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOfAny(null, *)                     = -1
	 * StringUtil.indexOfAny(*, null)                     = -1
	 * StringUtil.indexOfAny(*, [])                       = -1
	 * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;ab&quot;,&quot;cd&quot;])   = 2
	 * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;cd&quot;,&quot;ab&quot;])   = 2
	 * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;mn&quot;,&quot;op&quot;])   = -1
	 * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;zab&quot;,&quot;aby&quot;]) = 1
	 * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;&quot;])          = 0
	 * StringUtil.indexOfAny(&quot;&quot;, [&quot;&quot;])                    = 0
	 * StringUtil.indexOfAny(&quot;&quot;, [&quot;a&quot;])                   = -1
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStrs
	 *            the Strings to search for, may be null
	 * @return the first index of any of the searchStrs in str, -1 if no match
	 */
	public static int indexOfAny(String str, String[] searchStrs) {
		if ((str == null) || (searchStrs == null)) {
			return -1;
		}
		int sz = searchStrs.length;

		// String's can't have a MAX_VALUEth index.
		int ret = Integer.MAX_VALUE;

		int tmp = 0;
		for (int i = 0; i < sz; i++) {
			String search = searchStrs[i];
			if (search == null) {
				continue;
			}
			tmp = str.indexOf(search);
			if (tmp == -1) {
				continue;
			}

			if (tmp < ret) {
				ret = tmp;
			}
		}

		return (ret == Integer.MAX_VALUE) ? -1 : ret;
	}

	/**
	 * <p>
	 * Find the latest index of any of a set of potential substrings.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A
	 * <code>null</code> search array will return <code>-1</code>. A
	 * <code>null</code> or zero length search array entry will be ignored,
	 * but a search array containing "" will return the length of
	 * <code>str</code> if <code>str</code> is not null. This method uses
	 * {@link String#indexOf(String)}
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lastIndexOfAny(null, *)                   = -1
	 * StringUtil.lastIndexOfAny(*, null)                   = -1
	 * StringUtil.lastIndexOfAny(*, [])                     = -1
	 * StringUtil.lastIndexOfAny(*, [null])                 = -1
	 * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;ab&quot;,&quot;cd&quot;]) = 6
	 * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;cd&quot;,&quot;ab&quot;]) = 6
	 * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;mn&quot;,&quot;op&quot;]) = -1
	 * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;mn&quot;,&quot;op&quot;]) = -1
	 * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;mn&quot;,&quot;&quot;])   = 10
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStrs
	 *            the Strings to search for, may be null
	 * @return the last index of any of the Strings, -1 if no match
	 */
	public static int lastIndexOfAny(String str, String[] searchStrs) {
		if ((str == null) || (searchStrs == null)) {
			return -1;
		}
		int sz = searchStrs.length;
		int ret = -1;
		int tmp = 0;
		for (int i = 0; i < sz; i++) {
			String search = searchStrs[i];
			if (search == null) {
				continue;
			}
			tmp = str.lastIndexOf(search);
			if (tmp > ret) {
				ret = tmp;
			}
		}
		return ret;
	}

	// Substring
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * 
	 * <p>
	 * A negative start position can be used to start <code>n</code>
	 * characters from the end of the String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>null</code>. An empty
	 * ("") String will return "".
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.substring(null, *)   = null
	 * StringUtil.substring(&quot;&quot;, *)     = &quot;&quot;
	 * StringUtil.substring(&quot;abc&quot;, 0)  = &quot;abc&quot;
	 * StringUtil.substring(&quot;abc&quot;, 2)  = &quot;c&quot;
	 * StringUtil.substring(&quot;abc&quot;, 4)  = &quot;&quot;
	 * StringUtil.substring(&quot;abc&quot;, -2) = &quot;bc&quot;
	 * StringUtil.substring(&quot;abc&quot;, -4) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the substring from, may be null
	 * @param start
	 *            the position to start from, negative means count back from the
	 *            end of the String by this many characters
	 * @return substring from start position, <code>null</code> if null String
	 *         input
	 */
	public static String substring(String str, int start) {
		if (str == null) {
			return null;
		}

		// handle negatives, which means last n characters
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return EMPTY;
		}

		return str.substring(start);
	}

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * 
	 * <p>
	 * A negative start position can be used to start/end <code>n</code>
	 * characters from the end of the String.
	 * </p>
	 * 
	 * <p>
	 * The returned substring starts with the character in the
	 * <code>start</code> position and ends before the <code>end</code>
	 * position. All position counting is zero-based -- i.e., to start at the
	 * beginning of the string use <code>start = 0</code>. Negative start and
	 * end positions can be used to specify offsets relative to the end of the
	 * String.
	 * </p>
	 * 
	 * <p>
	 * If <code>start</code> is not strictly to the left of <code>end</code>, ""
	 * is returned.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.substring(null, *, *)    = null
	 * StringUtil.substring(&quot;&quot;, * ,  *)    = &quot;&quot;;
	 * StringUtil.substring(&quot;abc&quot;, 0, 2)   = &quot;ab&quot;
	 * StringUtil.substring(&quot;abc&quot;, 2, 0)   = &quot;&quot;
	 * StringUtil.substring(&quot;abc&quot;, 2, 4)   = &quot;c&quot;
	 * StringUtil.substring(&quot;abc&quot;, 4, 6)   = &quot;&quot;
	 * StringUtil.substring(&quot;abc&quot;, 2, 2)   = &quot;&quot;
	 * StringUtil.substring(&quot;abc&quot;, -2, -1) = &quot;b&quot;
	 * StringUtil.substring(&quot;abc&quot;, -4, 2)  = &quot;ab&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the substring from, may be null
	 * @param start
	 *            the position to start from, negative means count back from the
	 *            end of the String by this many characters
	 * @param end
	 *            the position to end at (exclusive), negative means count back
	 *            from the end of the String by this many characters
	 * @return substring from start position to end positon, <code>null</code>
	 *         if null String input
	 */
	public static String substring(String str, int start, int end) {
		if (str == null) {
			return null;
		}

		// handle negatives
		if (end < 0) {
			end = str.length() + end; // remember end is negative
		}
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		// check length next
		if (end > str.length()) {
			end = str.length();
		}

		// if start is greater than end, return ""
		if (start > end) {
			return EMPTY;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return str.substring(start, end);
	}

	// Left/Right/Mid
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the leftmost <code>len</code> characters of a String.
	 * </p>
	 * 
	 * <p>
	 * If <code>len</code> characters are not available, or the String is
	 * <code>null</code>, the String will be returned without an exception.
	 * An exception is thrown if len is negative.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.left(null, *)    = null
	 * StringUtil.left(*, -ve)     = &quot;&quot;
	 * StringUtil.left(&quot;&quot;, *)      = &quot;&quot;
	 * StringUtil.left(&quot;abc&quot;, 0)   = &quot;&quot;
	 * StringUtil.left(&quot;abc&quot;, 2)   = &quot;ab&quot;
	 * StringUtil.left(&quot;abc&quot;, 4)   = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the leftmost characters from, may be null
	 * @param len
	 *            the length of the required String, must be zero or positive
	 * @return the leftmost characters, <code>null</code> if null String input
	 */
	public static String left(String str, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(0, len);
	}

	/**
	 * <p>
	 * Gets the rightmost <code>len</code> characters of a String.
	 * </p>
	 * 
	 * <p>
	 * If <code>len</code> characters are not available, or the String is
	 * <code>null</code>, the String will be returned without an an
	 * exception. An exception is thrown if len is negative.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.right(null, *)    = null
	 * StringUtil.right(*, -ve)     = &quot;&quot;
	 * StringUtil.right(&quot;&quot;, *)      = &quot;&quot;
	 * StringUtil.right(&quot;abc&quot;, 0)   = &quot;&quot;
	 * StringUtil.right(&quot;abc&quot;, 2)   = &quot;bc&quot;
	 * StringUtil.right(&quot;abc&quot;, 4)   = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the rightmost characters from, may be null
	 * @param len
	 *            the length of the required String, must be zero or positive
	 * @return the rightmost characters, <code>null</code> if null String
	 *         input
	 */
	public static String right(String str, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(str.length() - len);
	}

	/**
	 * <p>
	 * Gets <code>len</code> characters from the middle of a String.
	 * </p>
	 * 
	 * <p>
	 * If <code>len</code> characters are not available, the remainder of the
	 * String will be returned without an exception. If the String is
	 * <code>null</code>, <code>null</code> will be returned. An exception
	 * is thrown if len is negative.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.mid(null, *, *)    = null
	 * StringUtil.mid(*, *, -ve)     = &quot;&quot;
	 * StringUtil.mid(&quot;&quot;, 0, *)      = &quot;&quot;
	 * StringUtil.mid(&quot;abc&quot;, 0, 2)   = &quot;ab&quot;
	 * StringUtil.mid(&quot;abc&quot;, 0, 4)   = &quot;abc&quot;
	 * StringUtil.mid(&quot;abc&quot;, 2, 4)   = &quot;c&quot;
	 * StringUtil.mid(&quot;abc&quot;, 4, 2)   = &quot;&quot;
	 * StringUtil.mid(&quot;abc&quot;, -2, 2)  = &quot;ab&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the characters from, may be null
	 * @param pos
	 *            the position to start from, negative treated as zero
	 * @param len
	 *            the length of the required String, must be zero or positive
	 * @return the middle characters, <code>null</code> if null String input
	 */
	public static String mid(String str, int pos, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0 || pos > str.length()) {
			return EMPTY;
		}
		if (pos < 0) {
			pos = 0;
		}
		if (str.length() <= (pos + len)) {
			return str.substring(pos);
		}
		return str.substring(pos, pos + len);
	}

	// SubStringAfter/SubStringBefore
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the substring before the first occurrence of a separator. The
	 * separator is not returned.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input will return <code>null</code>. An
	 * empty ("") string input will return the empty string. A <code>null</code>
	 * separator will return the input string.
	 * </p>
	 * 
	 * <p>
	 * If nothing is found, the string input is returned.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.substringBefore(null, *)      = null
	 * StringUtil.substringBefore(&quot;&quot;, *)        = &quot;&quot;
	 * StringUtil.substringBefore(&quot;abc&quot;, &quot;a&quot;)   = &quot;&quot;
	 * StringUtil.substringBefore(&quot;abcba&quot;, &quot;b&quot;) = &quot;a&quot;
	 * StringUtil.substringBefore(&quot;abc&quot;, &quot;c&quot;)   = &quot;ab&quot;
	 * StringUtil.substringBefore(&quot;abc&quot;, &quot;d&quot;)   = &quot;abc&quot;
	 * StringUtil.substringBefore(&quot;abc&quot;, &quot;&quot;)    = &quot;&quot;
	 * StringUtil.substringBefore(&quot;abc&quot;, null)  = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get a substring from, may be null
	 * @param separator
	 *            the String to search for, may be null
	 * @return the substring before the first occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringBefore(String str, String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		if (separator.length() == 0) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>
	 * Gets the substring after the first occurrence of a separator. The
	 * separator is not returned.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input will return <code>null</code>. An
	 * empty ("") string input will return the empty string. A <code>null</code>
	 * separator will return the empty string if the input string is not
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * If nothing is found, the empty string is returned.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.substringAfter(null, *)      = null
	 * StringUtil.substringAfter(&quot;&quot;, *)        = &quot;&quot;
	 * StringUtil.substringAfter(*, null)      = &quot;&quot;
	 * StringUtil.substringAfter(&quot;abc&quot;, &quot;a&quot;)   = &quot;bc&quot;
	 * StringUtil.substringAfter(&quot;abcba&quot;, &quot;b&quot;) = &quot;cba&quot;
	 * StringUtil.substringAfter(&quot;abc&quot;, &quot;c&quot;)   = &quot;&quot;
	 * StringUtil.substringAfter(&quot;abc&quot;, &quot;d&quot;)   = &quot;&quot;
	 * StringUtil.substringAfter(&quot;abc&quot;, &quot;&quot;)    = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get a substring from, may be null
	 * @param separator
	 *            the String to search for, may be null
	 * @return the substring after the first occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringAfter(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * <p>
	 * Gets the substring before the last occurrence of a separator. The
	 * separator is not returned.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input will return <code>null</code>. An
	 * empty ("") string input will return the empty string. An empty or
	 * <code>null</code> separator will return the input string.
	 * </p>
	 * 
	 * <p>
	 * If nothing is found, the string input is returned.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.substringBeforeLast(null, *)      = null
	 * StringUtil.substringBeforeLast(&quot;&quot;, *)        = &quot;&quot;
	 * StringUtil.substringBeforeLast(&quot;abcba&quot;, &quot;b&quot;) = &quot;abc&quot;
	 * StringUtil.substringBeforeLast(&quot;abc&quot;, &quot;c&quot;)   = &quot;ab&quot;
	 * StringUtil.substringBeforeLast(&quot;a&quot;, &quot;a&quot;)     = &quot;&quot;
	 * StringUtil.substringBeforeLast(&quot;a&quot;, &quot;z&quot;)     = &quot;a&quot;
	 * StringUtil.substringBeforeLast(&quot;a&quot;, null)    = &quot;a&quot;
	 * StringUtil.substringBeforeLast(&quot;a&quot;, &quot;&quot;)      = &quot;a&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get a substring from, may be null
	 * @param separator
	 *            the String to search for, may be null
	 * @return the substring before the last occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringBeforeLast(String str, String separator) {
		if (isEmpty(str) || isEmpty(separator)) {
			return str;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>
	 * Gets the substring after the last occurrence of a separator. The
	 * separator is not returned.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input will return <code>null</code>. An
	 * empty ("") string input will return the empty string. An empty or
	 * <code>null</code> separator will return the empty string if the input
	 * string is not <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * If nothing is found, the empty string is returned.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.substringAfterLast(null, *)      = null
	 * StringUtil.substringAfterLast(&quot;&quot;, *)        = &quot;&quot;
	 * StringUtil.substringAfterLast(*, &quot;&quot;)        = &quot;&quot;
	 * StringUtil.substringAfterLast(*, null)      = &quot;&quot;
	 * StringUtil.substringAfterLast(&quot;abc&quot;, &quot;a&quot;)   = &quot;bc&quot;
	 * StringUtil.substringAfterLast(&quot;abcba&quot;, &quot;b&quot;) = &quot;a&quot;
	 * StringUtil.substringAfterLast(&quot;abc&quot;, &quot;c&quot;)   = &quot;&quot;
	 * StringUtil.substringAfterLast(&quot;a&quot;, &quot;a&quot;)     = &quot;&quot;
	 * StringUtil.substringAfterLast(&quot;a&quot;, &quot;z&quot;)     = &quot;&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get a substring from, may be null
	 * @param separator
	 *            the String to search for, may be null
	 * @return the substring after the last occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringAfterLast(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(separator)) {
			return EMPTY;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1 || pos == (str.length() - separator.length())) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	// Substring between
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the String that is nested in between two instances of the same
	 * String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. A
	 * <code>null</code> tag returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.substringBetween(null, *)            = null
	 * StringUtil.substringBetween(&quot;&quot;, &quot;&quot;)             = &quot;&quot;
	 * StringUtil.substringBetween(&quot;&quot;, &quot;tag&quot;)          = null
	 * StringUtil.substringBetween(&quot;tagabctag&quot;, null)  = null
	 * StringUtil.substringBetween(&quot;tagabctag&quot;, &quot;&quot;)    = &quot;&quot;
	 * StringUtil.substringBetween(&quot;tagabctag&quot;, &quot;tag&quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String containing the substring, may be null
	 * @param tag
	 *            the String before and after the substring, may be null
	 * @return the substring, <code>null</code> if no match
	 * @since 2.0
	 */
	public static String substringBetween(String str, String tag) {
		return substringBetween(str, tag, tag);
	}

	/**
	 * <p>
	 * Gets the String that is nested in between two Strings. Only the first
	 * match is returned.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. A
	 * <code>null</code> open/close returns <code>null</code> (no match). An
	 * empty ("") open and close returns an empty string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.substringBetween(&quot;wx[b]yz&quot;, &quot;[&quot;, &quot;]&quot;) = &quot;b&quot;
	 * StringUtil.substringBetween(null, *, *)          = null
	 * StringUtil.substringBetween(*, null, *)          = null
	 * StringUtil.substringBetween(*, *, null)          = null
	 * StringUtil.substringBetween(&quot;&quot;, &quot;&quot;, &quot;&quot;)          = &quot;&quot;
	 * StringUtil.substringBetween(&quot;&quot;, &quot;&quot;, &quot;]&quot;)         = null
	 * StringUtil.substringBetween(&quot;&quot;, &quot;[&quot;, &quot;]&quot;)        = null
	 * StringUtil.substringBetween(&quot;yabcz&quot;, &quot;&quot;, &quot;&quot;)     = &quot;&quot;
	 * StringUtil.substringBetween(&quot;yabcz&quot;, &quot;y&quot;, &quot;z&quot;)   = &quot;abc&quot;
	 * StringUtil.substringBetween(&quot;yabczyabcz&quot;, &quot;y&quot;, &quot;z&quot;)   = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String containing the substring, may be null
	 * @param open
	 *            the String before the substring, may be null
	 * @param close
	 *            the String after the substring, may be null
	 * @return the substring, <code>null</code> if no match
	 * @since 2.0
	 */
	public static String substringBetween(String str, String open, String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	// Joining
	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing
	 * the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No separator is added to the joined String. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.join(null)            = null
	 * StringUtil.join([])              = &quot;&quot;
	 * StringUtil.join([null])          = &quot;&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]) = &quot;abc&quot;
	 * StringUtil.join([null, &quot;&quot;, &quot;a&quot;]) = &quot;a&quot;
	 * </pre>
	 * 
	 * @param array
	 *            the array of values to join together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 * @since 2.0
	 */
	public static String join(Object[] array) {
		return join(array, null);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing
	 * the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty
	 * strings within the array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.join(null, *)               = null
	 * StringUtil.join([], *)                 = &quot;&quot;
	 * StringUtil.join([null], *)             = &quot;&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], ';')  = &quot;a;b;c&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null) = &quot;abc&quot;
	 * StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ';')  = &quot;;;a&quot;
	 * </pre>
	 * 
	 * @param array
	 *            the array of values to join together, may be null
	 * @param separator
	 *            the separator character to use
	 * @return the joined String, <code>null</code> if null array input
	 * @since 2.0
	 */
	public static String join(Object[] array, char separator) {
		if (array == null) {
			return null;
		}

		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing
	 * the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty
	 * strings within the array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.join(null, *)               = null
	 * StringUtil.join([], *)                 = &quot;&quot;
	 * StringUtil.join([null], *)             = &quot;&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], ';')  = &quot;a;b;c&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null) = &quot;abc&quot;
	 * StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ';')  = &quot;;;a&quot;
	 * </pre>
	 * 
	 * @param array
	 *            the array of values to join together, may be null
	 * @param separator
	 *            the separator character to use
	 * @param startIndex
	 *            the first index to start joining from. It is an error to pass
	 *            in an end index past the end of the array
	 * @param endIndex
	 *            the index to stop joining from (exclusive). It is an error to
	 *            pass in an end index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 * @since 2.0
	 */
	public static String join(Object[] array, char separator, int startIndex,
			int endIndex) {
		if (array == null) {
			return null;
		}
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		bufSize *= ((array[startIndex] == null ? 16 : array[startIndex]
				.toString().length()) + 1);
		StringBuffer buf = new StringBuffer(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing
	 * the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code>
	 * separator is the same as an empty String (""). Null objects or empty
	 * strings within the array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.join(null, *)                = null
	 * StringUtil.join([], *)                  = &quot;&quot;
	 * StringUtil.join([null], *)              = &quot;&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;--&quot;)  = &quot;a--b--c&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null)  = &quot;abc&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;&quot;)    = &quot;abc&quot;
	 * StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ',')   = &quot;,,a&quot;
	 * </pre>
	 * 
	 * @param array
	 *            the array of values to join together, may be null
	 * @param separator
	 *            the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(Object[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing
	 * the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code>
	 * separator is the same as an empty String (""). Null objects or empty
	 * strings within the array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.join(null, *)                = null
	 * StringUtil.join([], *)                  = &quot;&quot;
	 * StringUtil.join([null], *)              = &quot;&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;--&quot;)  = &quot;a--b--c&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null)  = &quot;abc&quot;
	 * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;&quot;)    = &quot;abc&quot;
	 * StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ',')   = &quot;,,a&quot;
	 * </pre>
	 * 
	 * @param array
	 *            the array of values to join together, may be null
	 * @param separator
	 *            the separator character to use, null treated as ""
	 * @param startIndex
	 *            the first index to start joining from. It is an error to pass
	 *            in an end index past the end of the array
	 * @param endIndex
	 *            the index to stop joining from (exclusive). It is an error to
	 *            pass in an end index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(Object[] array, String separator, int startIndex,
			int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) +
		// len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		bufSize *= ((array[startIndex] == null ? 16 : array[startIndex]
				.toString().length()) + separator.length());

		StringBuffer buf = new StringBuffer(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}


	/**
	 * <p>
	 * Deletes all whitespaces from a String as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.deleteWhitespace(null)         = null
	 * StringUtil.deleteWhitespace(&quot;&quot;)           = &quot;&quot;
	 * StringUtil.deleteWhitespace(&quot;abc&quot;)        = &quot;abc&quot;
	 * StringUtil.deleteWhitespace(&quot;   ab  c  &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to delete whitespace from, may be null
	 * @return the String without whitespaces, <code>null</code> if null
	 *         String input
	 */
	public static String deleteWhitespace(String str) {
		if (isEmpty(str)) {
			return str;
		}
		int sz = str.length();
		char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}
		if (count == sz) {
			return str;
		}
		return new String(chs, 0, count);
	}

	// Remove
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Removes a substring only if it is at the begining of a source string,
	 * otherwise returns the source string.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> source string will return <code>null</code>. An
	 * empty ("") source string will return the empty string. A
	 * <code>null</code> search string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.removeStart(null, *)      = null
	 * StringUtil.removeStart(&quot;&quot;, *)        = &quot;&quot;
	 * StringUtil.removeStart(*, null)      = *
	 * StringUtil.removeStart(&quot;www.domain.com&quot;, &quot;www.&quot;)   = &quot;domain.com&quot;
	 * StringUtil.removeStart(&quot;domain.com&quot;, &quot;www.&quot;)       = &quot;domain.com&quot;
	 * StringUtil.removeStart(&quot;www.domain.com&quot;, &quot;domain&quot;) = &quot;www.domain.com&quot;
	 * StringUtil.removeStart(&quot;abc&quot;, &quot;&quot;)    = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the source String to search, may be null
	 * @param remove
	 *            the String to search for and remove, may be null
	 * @return the substring with the string removed if found, <code>null</code>
	 *         if null String input
	 * @since 2.1
	 */
	public static String removeStart(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.startsWith(remove)) {
			return str.substring(remove.length());
		}
		return str;
	}

	/**
	 * <p>
	 * Case insensitive removal of a substring if it is at the begining of a
	 * source string, otherwise returns the source string.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> source string will return <code>null</code>. An
	 * empty ("") source string will return the empty string. A
	 * <code>null</code> search string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.removeStartIgnoreCase(null, *)      = null
	 * StringUtil.removeStartIgnoreCase(&quot;&quot;, *)        = &quot;&quot;
	 * StringUtil.removeStartIgnoreCase(*, null)      = *
	 * StringUtil.removeStartIgnoreCase(&quot;www.domain.com&quot;, &quot;www.&quot;)   = &quot;domain.com&quot;
	 * StringUtil.removeStartIgnoreCase(&quot;www.domain.com&quot;, &quot;WWW.&quot;)   = &quot;domain.com&quot;
	 * StringUtil.removeStartIgnoreCase(&quot;domain.com&quot;, &quot;www.&quot;)       = &quot;domain.com&quot;
	 * StringUtil.removeStartIgnoreCase(&quot;www.domain.com&quot;, &quot;domain&quot;) = &quot;www.domain.com&quot;
	 * StringUtil.removeStartIgnoreCase(&quot;abc&quot;, &quot;&quot;)    = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the source String to search, may be null
	 * @param remove
	 *            the String to search for (case insensitive) and remove, may be
	 *            null
	 * @return the substring with the string removed if found, <code>null</code>
	 *         if null String input
	 * @since 2.4
	 */
	public static String removeStartIgnoreCase(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (startsWithIgnoreCase(str, remove)) {
			return str.substring(remove.length());
		}
		return str;
	}

	/**
	 * <p>
	 * Removes a substring only if it is at the end of a source string,
	 * otherwise returns the source string.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> source string will return <code>null</code>. An
	 * empty ("") source string will return the empty string. A
	 * <code>null</code> search string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.removeEnd(null, *)      = null
	 * StringUtil.removeEnd(&quot;&quot;, *)        = &quot;&quot;
	 * StringUtil.removeEnd(*, null)      = *
	 * StringUtil.removeEnd(&quot;www.domain.com&quot;, &quot;.com.&quot;)  = &quot;www.domain.com&quot;
	 * StringUtil.removeEnd(&quot;www.domain.com&quot;, &quot;.com&quot;)   = &quot;www.domain&quot;
	 * StringUtil.removeEnd(&quot;www.domain.com&quot;, &quot;domain&quot;) = &quot;www.domain.com&quot;
	 * StringUtil.removeEnd(&quot;abc&quot;, &quot;&quot;)    = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the source String to search, may be null
	 * @param remove
	 *            the String to search for and remove, may be null
	 * @return the substring with the string removed if found, <code>null</code>
	 *         if null String input
	 * @since 2.1
	 */
	public static String removeEnd(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.endsWith(remove)) {
			return str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * <p>
	 * Case insensitive removal of a substring if it is at the end of a source
	 * string, otherwise returns the source string.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> source string will return <code>null</code>. An
	 * empty ("") source string will return the empty string. A
	 * <code>null</code> search string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.removeEndIgnoreCase(null, *)      = null
	 * StringUtil.removeEndIgnoreCase(&quot;&quot;, *)        = &quot;&quot;
	 * StringUtil.removeEndIgnoreCase(*, null)      = *
	 * StringUtil.removeEndIgnoreCase(&quot;www.domain.com&quot;, &quot;.com.&quot;)  = &quot;www.domain.com&quot;
	 * StringUtil.removeEndIgnoreCase(&quot;www.domain.com&quot;, &quot;.com&quot;)   = &quot;www.domain&quot;
	 * StringUtil.removeEndIgnoreCase(&quot;www.domain.com&quot;, &quot;domain&quot;) = &quot;www.domain.com&quot;
	 * StringUtil.removeEndIgnoreCase(&quot;abc&quot;, &quot;&quot;)    = &quot;abc&quot;
	 * StringUtil.removeEndIgnoreCase(&quot;www.domain.com&quot;, &quot;.COM&quot;) = &quot;www.domain&quot;)
	 * StringUtil.removeEndIgnoreCase(&quot;www.domain.COM&quot;, &quot;.com&quot;) = &quot;www.domain&quot;)
	 * </pre>
	 * 
	 * @param str
	 *            the source String to search, may be null
	 * @param remove
	 *            the String to search for (case insensitive) and remove, may be
	 *            null
	 * @return the substring with the string removed if found, <code>null</code>
	 *         if null String input
	 * @since 2.4
	 */
	public static String removeEndIgnoreCase(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (endsWithIgnoreCase(str, remove)) {
			return str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * <p>
	 * Removes all occurrences of a substring from within the source string.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> source string will return <code>null</code>. An
	 * empty ("") source string will return the empty string. A
	 * <code>null</code> remove string will return the source string. An empty
	 * ("") remove string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.remove(null, *)        = null
	 * StringUtil.remove(&quot;&quot;, *)          = &quot;&quot;
	 * StringUtil.remove(*, null)        = *
	 * StringUtil.remove(*, &quot;&quot;)          = *
	 * StringUtil.remove(&quot;queued&quot;, &quot;ue&quot;) = &quot;qd&quot;
	 * StringUtil.remove(&quot;queued&quot;, &quot;zz&quot;) = &quot;queued&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the source String to search, may be null
	 * @param remove
	 *            the String to search for and remove, may be null
	 * @return the substring with the string removed if found, <code>null</code>
	 *         if null String input
	 * @since 2.1
	 */
	public static String remove(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		return replace(str, remove, EMPTY, -1);
	}

	/**
	 * <p>
	 * Removes all occurrences of a character from within the source string.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> source string will return <code>null</code>. An
	 * empty ("") source string will return the empty string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.remove(null, *)       = null
	 * StringUtil.remove(&quot;&quot;, *)         = &quot;&quot;
	 * StringUtil.remove(&quot;queued&quot;, 'u') = &quot;qeed&quot;
	 * StringUtil.remove(&quot;queued&quot;, 'z') = &quot;queued&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the source String to search, may be null
	 * @param remove
	 *            the char to search for and remove, may be null
	 * @return the substring with the char removed if found, <code>null</code>
	 *         if null String input
	 * @since 2.1
	 */
	public static String remove(String str, char remove) {
		if (isEmpty(str) || str.indexOf(remove) == -1) {
			return str;
		}
		char[] chars = str.toCharArray();
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != remove) {
				chars[pos++] = chars[i];
			}
		}
		return new String(chars, 0, pos);
	}

	// Replacing
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, once.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.replaceOnce(null, *, *)        = null
	 * StringUtil.replaceOnce(&quot;&quot;, *, *)          = &quot;&quot;
	 * StringUtil.replaceOnce(&quot;any&quot;, null, *)    = &quot;any&quot;
	 * StringUtil.replaceOnce(&quot;any&quot;, *, null)    = &quot;any&quot;
	 * StringUtil.replaceOnce(&quot;any&quot;, &quot;&quot;, *)      = &quot;any&quot;
	 * StringUtil.replaceOnce(&quot;aba&quot;, &quot;a&quot;, null)  = &quot;aba&quot;
	 * StringUtil.replaceOnce(&quot;aba&quot;, &quot;a&quot;, &quot;&quot;)    = &quot;ba&quot;
	 * StringUtil.replaceOnce(&quot;aba&quot;, &quot;a&quot;, &quot;z&quot;)   = &quot;zba&quot;
	 * </pre>
	 * 
	 * @see #replace(String text, String searchString, String replacement, int
	 *      max)
	 * @param text
	 *            text to search and replace in, may be null
	 * @param searchString
	 *            the String to search for, may be null
	 * @param replacement
	 *            the String to replace with, may be null
	 * @return the text with any replacements processed, <code>null</code> if
	 *         null String input
	 */
	public static String replaceOnce(String text, String searchString,
			String replacement) {
		return replace(text, searchString, replacement, 1);
	}

	/**
	 * <p>
	 * Replaces all occurrences of a String within another String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.replace(null, *, *)        = null
	 * StringUtil.replace(&quot;&quot;, *, *)          = &quot;&quot;
	 * StringUtil.replace(&quot;any&quot;, null, *)    = &quot;any&quot;
	 * StringUtil.replace(&quot;any&quot;, *, null)    = &quot;any&quot;
	 * StringUtil.replace(&quot;any&quot;, &quot;&quot;, *)      = &quot;any&quot;
	 * StringUtil.replace(&quot;aba&quot;, &quot;a&quot;, null)  = &quot;aba&quot;
	 * StringUtil.replace(&quot;aba&quot;, &quot;a&quot;, &quot;&quot;)    = &quot;b&quot;
	 * StringUtil.replace(&quot;aba&quot;, &quot;a&quot;, &quot;z&quot;)   = &quot;zbz&quot;
	 * </pre>
	 * 
	 * @see #replace(String text, String searchString, String replacement, int
	 *      max)
	 * @param text
	 *            text to search and replace in, may be null
	 * @param searchString
	 *            the String to search for, may be null
	 * @param replacement
	 *            the String to replace it with, may be null
	 * @return the text with any replacements processed, <code>null</code> if
	 *         null String input
	 */
	public static String replace(String text, String searchString,String replacement) {
		return replace(text, searchString, replacement, -1);
	}

	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, for the
	 * first <code>max</code> values of the search String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.replace(null, *, *, *)         = null
	 * StringUtil.replace(&quot;&quot;, *, *, *)           = &quot;&quot;
	 * StringUtil.replace(&quot;any&quot;, null, *, *)     = &quot;any&quot;
	 * StringUtil.replace(&quot;any&quot;, *, null, *)     = &quot;any&quot;
	 * StringUtil.replace(&quot;any&quot;, &quot;&quot;, *, *)       = &quot;any&quot;
	 * StringUtil.replace(&quot;any&quot;, *, *, 0)        = &quot;any&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, null, -1) = &quot;abaa&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;&quot;, -1)   = &quot;b&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 0)   = &quot;abaa&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 1)   = &quot;zbaa&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 2)   = &quot;zbza&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, -1)  = &quot;zbzz&quot;
	 * </pre>
	 * 
	 * @param text
	 *            text to search and replace in, may be null
	 * @param searchString
	 *            the String to search for, may be null
	 * @param replacement
	 *            the String to replace it with, may be null
	 * @param max
	 *            maximum number of values to replace, or <code>-1</code> if
	 *            no maximum
	 * @return the text with any replacements processed, <code>null</code> if
	 *         null String input
	 */
	public static String replace(String text, String searchString,
			String replacement, int max) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null
				|| max == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == -1) {
			return text;
		}
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = (increase < 0 ? 0 : increase);
		increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
		StringBuffer buf = new StringBuffer(text.length() + increase);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	/**
	 * <p>
	 * Replaces all occurrences of Strings within another String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> reference passed to this method is a no-op, or if
	 * any "search string" or "string to replace" is null, that replace will be
	 * ignored. This will not repeat. For repeating replaces, call the
	 * overloaded method.
	 * </p>
	 * 
	 * <pre>
	 *  StringUtil.replaceEach(null, *, *)        = null
	 *  StringUtil.replaceEach(&quot;&quot;, *, *)          = &quot;&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, null, null) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[0], null) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, null, new String[0]) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[]{&quot;a&quot;}, null)  = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[]{&quot;a&quot;}, new String[]{&quot;&quot;})  = &quot;b&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[]{null}, new String[]{&quot;a&quot;})  = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;w&quot;, &quot;t&quot;})  = &quot;wcte&quot;
	 *  (example of how it does not repeat)
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;d&quot;, &quot;t&quot;})  = &quot;dcte&quot;
	 * </pre>
	 * 
	 * @param text
	 *            text to search and replace in, no-op if null
	 * @param searchList
	 *            the Strings to search for, no-op if null
	 * @param replacementList
	 *            the Strings to replace them with, no-op if null
	 * @return the text with any replacements processed, <code>null</code> if
	 *         null String input
	 * @throws IndexOutOfBoundsException
	 *             if the lengths of the arrays are not the same (null is ok,
	 *             and/or size 0)
	 * @since 2.4
	 */
	public static String replaceEach(String text, String[] searchList,
			String[] replacementList) {
		return replaceEach(text, searchList, replacementList, false, 0);
	}

	/**
	 * <p>
	 * Replaces all occurrences of Strings within another String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> reference passed to this method is a no-op, or if
	 * any "search string" or "string to replace" is null, that replace will be
	 * ignored. This will not repeat. For repeating replaces, call the
	 * overloaded method.
	 * </p>
	 * 
	 * <pre>
	 *  StringUtil.replaceEach(null, *, *, *) = null
	 *  StringUtil.replaceEach(&quot;&quot;, *, *, *) = &quot;&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, null, null, *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[0], null, *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, null, new String[0], *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[]{&quot;a&quot;}, null, *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[]{&quot;a&quot;}, new String[]{&quot;&quot;}, *) = &quot;b&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[]{null}, new String[]{&quot;a&quot;}, *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;w&quot;, &quot;t&quot;}, *) = &quot;wcte&quot;
	 *  (example of how it repeats)
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;d&quot;, &quot;t&quot;}, false) = &quot;dcte&quot;
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;d&quot;, &quot;t&quot;}, true) = &quot;tcte&quot;
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;d&quot;, &quot;ab&quot;}, true) = IllegalArgumentException
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;d&quot;, &quot;ab&quot;}, false) = &quot;dcabe&quot;
	 * </pre>
	 * 
	 * @param text
	 *            text to search and replace in, no-op if null
	 * @param searchList
	 *            the Strings to search for, no-op if null
	 * @param replacementList
	 *            the Strings to replace them with, no-op if null
	 * @return the text with any replacements processed, <code>null</code> if
	 *         null String input
	 * @throws IllegalArgumentException
	 *             if the search is repeating and there is an endless loop due
	 *             to outputs of one being inputs to another
	 * @throws IndexOutOfBoundsException
	 *             if the lengths of the arrays are not the same (null is ok,
	 *             and/or size 0)
	 * @since 2.4
	 */
	public static String replaceEachRepeatedly(String text,
			String[] searchList, String[] replacementList) {
		// timeToLive should be 0 if not used or nothing to replace, else it's
		// the length of the replace array
		int timeToLive = searchList == null ? 0 : searchList.length;
		return replaceEach(text, searchList, replacementList, true, timeToLive);
	}

	/**
	 * <p>
	 * Replaces all occurrences of Strings within another String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> reference passed to this method is a no-op, or if
	 * any "search string" or "string to replace" is null, that replace will be
	 * ignored.
	 * </p>
	 * 
	 * <pre>
	 *  StringUtil.replaceEach(null, *, *, *) = null
	 *  StringUtil.replaceEach(&quot;&quot;, *, *, *) = &quot;&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, null, null, *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[0], null, *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, null, new String[0], *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[]{&quot;a&quot;}, null, *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[]{&quot;a&quot;}, new String[]{&quot;&quot;}, *) = &quot;b&quot;
	 *  StringUtil.replaceEach(&quot;aba&quot;, new String[]{null}, new String[]{&quot;a&quot;}, *) = &quot;aba&quot;
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;w&quot;, &quot;t&quot;}, *) = &quot;wcte&quot;
	 *  (example of how it repeats)
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;d&quot;, &quot;t&quot;}, false) = &quot;dcte&quot;
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;d&quot;, &quot;t&quot;}, true) = &quot;tcte&quot;
	 *  StringUtil.replaceEach(&quot;abcde&quot;, new String[]{&quot;ab&quot;, &quot;d&quot;}, new String[]{&quot;d&quot;, &quot;ab&quot;}, *) = IllegalArgumentException
	 * </pre>
	 * 
	 * @param text
	 *            text to search and replace in, no-op if null
	 * @param searchList
	 *            the Strings to search for, no-op if null
	 * @param replacementList
	 *            the Strings to replace them with, no-op if null
	 * @param repeat
	 *            if true, then replace repeatedly until there are no more
	 *            possible replacements or timeToLive < 0
	 * @param timeToLive
	 *            if less than 0 then there is a circular reference and endless
	 *            loop
	 * @return the text with any replacements processed, <code>null</code> if
	 *         null String input
	 * @throws IllegalArgumentException
	 *             if the search is repeating and there is an endless loop due
	 *             to outputs of one being inputs to another
	 * @throws IndexOutOfBoundsException
	 *             if the lengths of the arrays are not the same (null is ok,
	 *             and/or size 0)
	 * @since 2.4
	 */
	private static String replaceEach(String text, String[] searchList,
			String[] replacementList, boolean repeat, int timeToLive) {

		// mchyzer Performance note: This creates very few new objects (one
		// major goal)
		// let me know if there are performance requests, we can create a
		// harness to measure

		if (text == null || text.length() == 0 || searchList == null
				|| searchList.length == 0 || replacementList == null
				|| replacementList.length == 0) {
			return text;
		}

		// if recursing, this shouldnt be less than 0
		if (timeToLive < 0) {
			throw new IllegalStateException("TimeToLive of " + timeToLive
					+ " is less than 0: " + text);
		}

		int searchLength = searchList.length;
		int replacementLength = replacementList.length;

		// make sure lengths are ok, these need to be equal
		if (searchLength != replacementLength) {
			throw new IllegalArgumentException(
					"Search and Replace array lengths don't match: "
							+ searchLength + " vs " + replacementLength);
		}

		// keep track of which still have matches
		boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

		// index on index that the match was found
		int textIndex = -1;
		int replaceIndex = -1;
		int tempIndex = -1;

		// index of replace array that will replace the search string found
		// NOTE: logic duplicated below START
		for (int i = 0; i < searchLength; i++) {
			if (noMoreMatchesForReplIndex[i] || searchList[i] == null
					|| searchList[i].length() == 0
					|| replacementList[i] == null) {
				continue;
			}
			tempIndex = text.indexOf(searchList[i]);

			// see if we need to keep searching for this
			if (tempIndex == -1) {
				noMoreMatchesForReplIndex[i] = true;
			} else {
				if (textIndex == -1 || tempIndex < textIndex) {
					textIndex = tempIndex;
					replaceIndex = i;
				}
			}
		}
		// NOTE: logic mostly below END

		// no search strings found, we are done
		if (textIndex == -1) {
			return text;
		}

		int start = 0;

		// get a good guess on the size of the result buffer so it doesnt have
		// to double if it goes over a bit
		int increase = 0;

		// count the replacement text elements that are larger than their
		// corresponding text being replaced
		for (int i = 0; i < searchList.length; i++) {
			if (searchList[i] == null || replacementList[i] == null) {
				continue;
			}
			int greater = replacementList[i].length() - searchList[i].length();
			if (greater > 0) {
				increase += 3 * greater; // assume 3 matches
			}
		}
		// have upper-bound at 20% increase, then let Java take over
		increase = Math.min(increase, text.length() / 5);

		StringBuffer buf = new StringBuffer(text.length() + increase);

		while (textIndex != -1) {

			for (int i = start; i < textIndex; i++) {
				buf.append(text.charAt(i));
			}
			buf.append(replacementList[replaceIndex]);

			start = textIndex + searchList[replaceIndex].length();

			textIndex = -1;
			replaceIndex = -1;
			tempIndex = -1;
			// find the next earliest match
			// NOTE: logic mostly duplicated above START
			for (int i = 0; i < searchLength; i++) {
				if (noMoreMatchesForReplIndex[i] || searchList[i] == null
						|| searchList[i].length() == 0
						|| replacementList[i] == null) {
					continue;
				}
				tempIndex = text.indexOf(searchList[i], start);

				// see if we need to keep searching for this
				if (tempIndex == -1) {
					noMoreMatchesForReplIndex[i] = true;
				} else {
					if (textIndex == -1 || tempIndex < textIndex) {
						textIndex = tempIndex;
						replaceIndex = i;
					}
				}
			}
			// NOTE: logic duplicated above END

		}
		int textLength = text.length();
		for (int i = start; i < textLength; i++) {
			buf.append(text.charAt(i));
		}
		String result = buf.toString();
		if (!repeat) {
			return result;
		}

		return replaceEach(result, searchList, replacementList, repeat,
				timeToLive - 1);
	}

	// Replace, character based
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Replaces all occurrences of a character in a String with another. This is
	 * a null-safe version of {@link String#replace(char, char)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input returns <code>null</code>. An empty
	 * ("") string input returns an empty string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.replaceChars(null, *, *)        = null
	 * StringUtil.replaceChars(&quot;&quot;, *, *)          = &quot;&quot;
	 * StringUtil.replaceChars(&quot;abcba&quot;, 'b', 'y') = &quot;aycya&quot;
	 * StringUtil.replaceChars(&quot;abcba&quot;, 'z', 'y') = &quot;abcba&quot;
	 * </pre>
	 * 
	 * @param str
	 *            String to replace characters in, may be null
	 * @param searchChar
	 *            the character to search for, may be null
	 * @param replaceChar
	 *            the character to replace, may be null
	 * @return modified String, <code>null</code> if null string input
	 * @since 2.0
	 */
	public static String replaceChars(String str, char searchChar,
			char replaceChar) {
		if (str == null) {
			return null;
		}
		return str.replace(searchChar, replaceChar);
	}

	/**
	 * <p>
	 * Replaces multiple characters in a String in one go. This method can also
	 * be used to delete characters.
	 * </p>
	 * 
	 * <p>
	 * For example:<br />
	 * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input returns <code>null</code>. An empty
	 * ("") string input returns an empty string. A null or empty set of search
	 * characters returns the input string.
	 * </p>
	 * 
	 * <p>
	 * The length of the search characters should normally equal the length of
	 * the replace characters. If the search characters is longer, then the
	 * extra search characters are deleted. If the search characters is shorter,
	 * then the extra replace characters are ignored.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.replaceChars(null, *, *)           = null
	 * StringUtil.replaceChars(&quot;&quot;, *, *)             = &quot;&quot;
	 * StringUtil.replaceChars(&quot;abc&quot;, null, *)       = &quot;abc&quot;
	 * StringUtil.replaceChars(&quot;abc&quot;, &quot;&quot;, *)         = &quot;abc&quot;
	 * StringUtil.replaceChars(&quot;abc&quot;, &quot;b&quot;, null)     = &quot;ac&quot;
	 * StringUtil.replaceChars(&quot;abc&quot;, &quot;b&quot;, &quot;&quot;)       = &quot;ac&quot;
	 * StringUtil.replaceChars(&quot;abcba&quot;, &quot;bc&quot;, &quot;yz&quot;)  = &quot;ayzya&quot;
	 * StringUtil.replaceChars(&quot;abcba&quot;, &quot;bc&quot;, &quot;y&quot;)   = &quot;ayya&quot;
	 * StringUtil.replaceChars(&quot;abcba&quot;, &quot;bc&quot;, &quot;yzx&quot;) = &quot;ayzya&quot;
	 * </pre>
	 * 
	 * @param str
	 *            String to replace characters in, may be null
	 * @param searchChars
	 *            a set of characters to search for, may be null
	 * @param replaceChars
	 *            a set of characters to replace, may be null
	 * @return modified String, <code>null</code> if null string input
	 * @since 2.0
	 */
	public static String replaceChars(String str, String searchChars,
			String replaceChars) {
		if (isEmpty(str) || isEmpty(searchChars)) {
			return str;
		}
		if (replaceChars == null) {
			replaceChars = EMPTY;
		}
		boolean modified = false;
		int replaceCharsLength = replaceChars.length();
		int strLength = str.length();
		StringBuffer buf = new StringBuffer(strLength);
		for (int i = 0; i < strLength; i++) {
			char ch = str.charAt(i);
			int index = searchChars.indexOf(ch);
			if (index >= 0) {
				modified = true;
				if (index < replaceCharsLength) {
					buf.append(replaceChars.charAt(index));
				}
			} else {
				buf.append(ch);
			}
		}
		if (modified) {
			return buf.toString();
		}
		return str;
	}


	/**
	 * <p>
	 * Overlays part of a String with another String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input returns <code>null</code>. A
	 * negative index is treated as zero. An index greater than the string
	 * length is treated as the string length. The start index is always the
	 * smaller of the two indices.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.overlay(null, *, *, *)            = null
	 * StringUtil.overlay(&quot;&quot;, &quot;abc&quot;, 0, 0)          = &quot;abc&quot;
	 * StringUtil.overlay(&quot;abcdef&quot;, null, 2, 4)     = &quot;abef&quot;
	 * StringUtil.overlay(&quot;abcdef&quot;, &quot;&quot;, 2, 4)       = &quot;abef&quot;
	 * StringUtil.overlay(&quot;abcdef&quot;, &quot;&quot;, 4, 2)       = &quot;abef&quot;
	 * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 2, 4)   = &quot;abzzzzef&quot;
	 * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 4, 2)   = &quot;abzzzzef&quot;
	 * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, -1, 4)  = &quot;zzzzef&quot;
	 * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 2, 8)   = &quot;abzzzz&quot;
	 * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, -2, -3) = &quot;zzzzabcdef&quot;
	 * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 8, 10)  = &quot;abcdefzzzz&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to do overlaying in, may be null
	 * @param overlay
	 *            the String to overlay, may be null
	 * @param start
	 *            the position to start overlaying at
	 * @param end
	 *            the position to stop overlaying before
	 * @return overlayed String, <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String overlay(String str, String overlay, int start, int end) {
		if (str == null) {
			return null;
		}
		if (overlay == null) {
			overlay = EMPTY;
		}
		int len = str.length();
		if (start < 0) {
			start = 0;
		}
		if (start > len) {
			start = len;
		}
		if (end < 0) {
			end = 0;
		}
		if (end > len) {
			end = len;
		}
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		return new StringBuffer(len + start - end + overlay.length() + 1)
				.append(str.substring(0, start)).append(overlay).append(
						str.substring(end)).toString();
	}

	// Padding
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Repeat a String <code>repeat</code> times to form a new String.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.repeat(null, 2) = null
	 * StringUtil.repeat(&quot;&quot;, 0)   = &quot;&quot;
	 * StringUtil.repeat(&quot;&quot;, 2)   = &quot;&quot;
	 * StringUtil.repeat(&quot;a&quot;, 3)  = &quot;aaa&quot;
	 * StringUtil.repeat(&quot;ab&quot;, 2) = &quot;abab&quot;
	 * StringUtil.repeat(&quot;a&quot;, -2) = &quot;&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to repeat, may be null
	 * @param repeat
	 *            number of times to repeat str, negative treated as zero
	 * @return a new String consisting of the original String repeated,
	 *         <code>null</code> if null String input
	 * @since 2.5
	 */
	public static String repeat(String str, int repeat) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		if (repeat <= 0) {
			return EMPTY;
		}
		int inputLength = str.length();
		if (repeat == 1 || inputLength == 0) {
			return str;
		}
		if (inputLength == 1 && repeat <= PAD_LIMIT) {
			return padding(repeat, str.charAt(0));
		}

		int outputLength = inputLength * repeat;
		switch (inputLength) {
		case 1:
			char ch = str.charAt(0);
			char[] output1 = new char[outputLength];
			for (int i = repeat - 1; i >= 0; i--) {
				output1[i] = ch;
			}
			return new String(output1);
		case 2:
			char ch0 = str.charAt(0);
			char ch1 = str.charAt(1);
			char[] output2 = new char[outputLength];
			for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
				output2[i] = ch0;
				output2[i + 1] = ch1;
			}
			return new String(output2);
		default:
			StringBuffer buf = new StringBuffer(outputLength);
			for (int i = 0; i < repeat; i++) {
				buf.append(str);
			}
			return buf.toString();
		}
	}

	/**
	 * <p>
	 * Repeat a String <code>repeat</code> times to form a new String, with a
	 * String separator injected each time.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.repeat(null, null, 2) = null
	 * StringUtil.repeat(null, &quot;x&quot;, 2)  = null
	 * StringUtil.repeat(&quot;&quot;, null, 0)   = &quot;&quot;
	 * StringUtil.repeat(&quot;&quot;, &quot;&quot;, 2)     = &quot;&quot;
	 * StringUtil.repeat(&quot;&quot;, &quot;x&quot;, 3)    = &quot;xxx&quot;
	 * StringUtil.repeat(&quot;?&quot;, &quot;, &quot;, 3)  = &quot;?, ?, ?&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to repeat, may be null
	 * @param separator
	 *            the String to inject, may be null
	 * @param repeat
	 *            number of times to repeat str, negative treated as zero
	 * @return a new String consisting of the original String repeated,
	 *         <code>null</code> if null String input
	 */
	public static String repeat(String str, String separator, int repeat) {
		if (str == null || separator == null) {
			return repeat(str, repeat);
		} else {
			// given that repeat(String, int) is quite optimized, better to rely
			// on it than try and splice this into it
			String result = repeat(str + separator, repeat);
			return removeEnd(result, separator);
		}
	}

	/**
	 * <p>
	 * Returns padding using the specified delimiter repeated to a given length.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.padding(0, 'e')  = &quot;&quot;
	 * StringUtil.padding(3, 'e')  = &quot;eee&quot;
	 * StringUtil.padding(-2, 'e') = IndexOutOfBoundsException
	 * </pre>
	 * 
	 * <p>
	 * Note: this method doesn't not support padding with <a
	 * href="http://www.unicode.org/glossary/#supplementary_character">Unicode
	 * Supplementary Characters</a> as they require a pair of <code>char</code>s
	 * to be represented. If you are needing to support full I18N of your
	 * applications consider using {@link #repeat(String, int)} instead.
	 * </p>
	 * 
	 * @param repeat
	 *            number of times to repeat delim
	 * @param padChar
	 *            character to repeat
	 * @return String with repeated character
	 * @throws IndexOutOfBoundsException
	 *             if <code>repeat &lt; 0</code>
	 * @see #repeat(String, int)
	 */
	private static String padding(int repeat, char padChar)
			throws IndexOutOfBoundsException {
		if (repeat < 0) {
			throw new IndexOutOfBoundsException(
					"Cannot pad a negative amount: " + repeat);
		}
		final char[] buf = new char[repeat];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = padChar;
		}
		return new String(buf);
	}

	/**
	 * <p>
	 * Right pad a String with spaces (' ').
	 * </p>
	 * 
	 * <p>
	 * The String is padded to the size of <code>size</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.rightPad(null, *)   = null
	 * StringUtil.rightPad(&quot;&quot;, 3)     = &quot;   &quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 3)  = &quot;bat&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 5)  = &quot;bat  &quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 1)  = &quot;bat&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, -1) = &quot;bat&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to pad out, may be null
	 * @param size
	 *            the size to pad to
	 * @return right padded String or original String if no padding is
	 *         necessary, <code>null</code> if null String input
	 */
	public static String rightPad(String str, int size) {
		return rightPad(str, size, ' ');
	}

	/**
	 * <p>
	 * Right pad a String with a specified character.
	 * </p>
	 * 
	 * <p>
	 * The String is padded to the size of <code>size</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.rightPad(null, *, *)     = null
	 * StringUtil.rightPad(&quot;&quot;, 3, 'z')     = &quot;zzz&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 3, 'z')  = &quot;bat&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 5, 'z')  = &quot;batzz&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 1, 'z')  = &quot;bat&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, -1, 'z') = &quot;bat&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to pad out, may be null
	 * @param size
	 *            the size to pad to
	 * @param padChar
	 *            the character to pad with
	 * @return right padded String or original String if no padding is
	 *         necessary, <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String rightPad(String str, int size, char padChar) {
		if (str == null) {
			return null;
		}
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PAD_LIMIT) {
			return rightPad(str, size, String.valueOf(padChar));
		}
		return str.concat(padding(pads, padChar));
	}

	/**
	 * <p>
	 * Right pad a String with a specified String.
	 * </p>
	 * 
	 * <p>
	 * The String is padded to the size of <code>size</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.rightPad(null, *, *)      = null
	 * StringUtil.rightPad(&quot;&quot;, 3, &quot;z&quot;)      = &quot;zzz&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 3, &quot;yz&quot;)  = &quot;bat&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 5, &quot;yz&quot;)  = &quot;batyz&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 8, &quot;yz&quot;)  = &quot;batyzyzy&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 1, &quot;yz&quot;)  = &quot;bat&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, -1, &quot;yz&quot;) = &quot;bat&quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 5, null)  = &quot;bat  &quot;
	 * StringUtil.rightPad(&quot;bat&quot;, 5, &quot;&quot;)    = &quot;bat  &quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to pad out, may be null
	 * @param size
	 *            the size to pad to
	 * @param padStr
	 *            the String to pad with, null or empty treated as single space
	 * @return right padded String or original String if no padding is
	 *         necessary, <code>null</code> if null String input
	 */
	public static String rightPad(String str, int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = " ";
		}
		int padLen = padStr.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return rightPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return str.concat(padStr);
		} else if (pads < padLen) {
			return str.concat(padStr.substring(0, pads));
		} else {
			char[] padding = new char[pads];
			char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return str.concat(new String(padding));
		}
	}

	/**
	 * <p>
	 * Left pad a String with spaces (' ').
	 * </p>
	 * 
	 * <p>
	 * The String is padded to the size of <code>size</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.leftPad(null, *)   = null
	 * StringUtil.leftPad(&quot;&quot;, 3)     = &quot;   &quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 3)  = &quot;bat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 5)  = &quot;  bat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 1)  = &quot;bat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, -1) = &quot;bat&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to pad out, may be null
	 * @param size
	 *            the size to pad to
	 * @return left padded String or original String if no padding is necessary,
	 *         <code>null</code> if null String input
	 */
	public static String leftPad(String str, int size) {
		return leftPad(str, size, ' ');
	}

	/**
	 * <p>
	 * Left pad a String with a specified character.
	 * </p>
	 * 
	 * <p>
	 * Pad to a size of <code>size</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.leftPad(null, *, *)     = null
	 * StringUtil.leftPad(&quot;&quot;, 3, 'z')     = &quot;zzz&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 3, 'z')  = &quot;bat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 5, 'z')  = &quot;zzbat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 1, 'z')  = &quot;bat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, -1, 'z') = &quot;bat&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to pad out, may be null
	 * @param size
	 *            the size to pad to
	 * @param padChar
	 *            the character to pad with
	 * @return left padded String or original String if no padding is necessary,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String leftPad(String str, int size, char padChar) {
		if (str == null) {
			return null;
		}
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PAD_LIMIT) {
			return leftPad(str, size, String.valueOf(padChar));
		}
		return padding(pads, padChar).concat(str);
	}

	/**
	 * <p>
	 * Left pad a String with a specified String.
	 * </p>
	 * 
	 * <p>
	 * Pad to a size of <code>size</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.leftPad(null, *, *)      = null
	 * StringUtil.leftPad(&quot;&quot;, 3, &quot;z&quot;)      = &quot;zzz&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 3, &quot;yz&quot;)  = &quot;bat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 5, &quot;yz&quot;)  = &quot;yzbat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 8, &quot;yz&quot;)  = &quot;yzyzybat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 1, &quot;yz&quot;)  = &quot;bat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, -1, &quot;yz&quot;) = &quot;bat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 5, null)  = &quot;  bat&quot;
	 * StringUtil.leftPad(&quot;bat&quot;, 5, &quot;&quot;)    = &quot;  bat&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to pad out, may be null
	 * @param size
	 *            the size to pad to
	 * @param padStr
	 *            the String to pad with, null or empty treated as single space
	 * @return left padded String or original String if no padding is necessary,
	 *         <code>null</code> if null String input
	 */
	public static String leftPad(String str, int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = " ";
		}
		int padLen = padStr.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return leftPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return padStr.concat(str);
		} else if (pads < padLen) {
			return padStr.substring(0, pads).concat(str);
		} else {
			char[] padding = new char[pads];
			char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return new String(padding).concat(str);
		}
	}

	/**
	 * Gets a String's length or <code>0</code> if the String is
	 * <code>null</code>.
	 * 
	 * @param str
	 *            a String or <code>null</code>
	 * @return String length or <code>0</code> if the String is
	 *         <code>null</code>.
	 * @since 2.4
	 */
	public static int length(String str) {
		return str == null ? 0 : str.length();
	}

	// Centering
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Centers a String in a larger String of size <code>size</code> using the
	 * space character (' ').
	 * <p>
	 * 
	 * <p>
	 * If the size is less than the String length, the String is returned. A
	 * <code>null</code> String returns <code>null</code>. A negative size
	 * is treated as zero.
	 * </p>
	 * 
	 * <p>
	 * Equivalent to <code>center(str, size, " ")</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.center(null, *)   = null
	 * StringUtil.center(&quot;&quot;, 4)     = &quot;    &quot;
	 * StringUtil.center(&quot;ab&quot;, -1)  = &quot;ab&quot;
	 * StringUtil.center(&quot;ab&quot;, 4)   = &quot; ab &quot;
	 * StringUtil.center(&quot;abcd&quot;, 2) = &quot;abcd&quot;
	 * StringUtil.center(&quot;a&quot;, 4)    = &quot; a  &quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to center, may be null
	 * @param size
	 *            the int size of new String, negative treated as zero
	 * @return centered String, <code>null</code> if null String input
	 */
	public static String center(String str, int size) {
		return center(str, size, ' ');
	}

	/**
	 * <p>
	 * Centers a String in a larger String of size <code>size</code>. Uses a
	 * supplied character as the value to pad the String with.
	 * </p>
	 * 
	 * <p>
	 * If the size is less than the String length, the String is returned. A
	 * <code>null</code> String returns <code>null</code>. A negative size
	 * is treated as zero.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.center(null, *, *)     = null
	 * StringUtil.center(&quot;&quot;, 4, ' ')     = &quot;    &quot;
	 * StringUtil.center(&quot;ab&quot;, -1, ' ')  = &quot;ab&quot;
	 * StringUtil.center(&quot;ab&quot;, 4, ' ')   = &quot; ab&quot;
	 * StringUtil.center(&quot;abcd&quot;, 2, ' ') = &quot;abcd&quot;
	 * StringUtil.center(&quot;a&quot;, 4, ' ')    = &quot; a  &quot;
	 * StringUtil.center(&quot;a&quot;, 4, 'y')    = &quot;yayy&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to center, may be null
	 * @param size
	 *            the int size of new String, negative treated as zero
	 * @param padChar
	 *            the character to pad the new String with
	 * @return centered String, <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String center(String str, int size, char padChar) {
		if (str == null || size <= 0) {
			return str;
		}
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str;
		}
		str = leftPad(str, strLen + pads / 2, padChar);
		str = rightPad(str, size, padChar);
		return str;
	}

	/**
	 * <p>
	 * Centers a String in a larger String of size <code>size</code>. Uses a
	 * supplied String as the value to pad the String with.
	 * </p>
	 * 
	 * <p>
	 * If the size is less than the String length, the String is returned. A
	 * <code>null</code> String returns <code>null</code>. A negative size
	 * is treated as zero.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.center(null, *, *)     = null
	 * StringUtil.center(&quot;&quot;, 4, &quot; &quot;)     = &quot;    &quot;
	 * StringUtil.center(&quot;ab&quot;, -1, &quot; &quot;)  = &quot;ab&quot;
	 * StringUtil.center(&quot;ab&quot;, 4, &quot; &quot;)   = &quot; ab&quot;
	 * StringUtil.center(&quot;abcd&quot;, 2, &quot; &quot;) = &quot;abcd&quot;
	 * StringUtil.center(&quot;a&quot;, 4, &quot; &quot;)    = &quot; a  &quot;
	 * StringUtil.center(&quot;a&quot;, 4, &quot;yz&quot;)   = &quot;yayz&quot;
	 * StringUtil.center(&quot;abc&quot;, 7, null) = &quot;  abc  &quot;
	 * StringUtil.center(&quot;abc&quot;, 7, &quot;&quot;)   = &quot;  abc  &quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to center, may be null
	 * @param size
	 *            the int size of new String, negative treated as zero
	 * @param padStr
	 *            the String to pad the new String with, must not be null or
	 *            empty
	 * @return centered String, <code>null</code> if null String input
	 * @throws IllegalArgumentException
	 *             if padStr is <code>null</code> or empty
	 */
	public static String center(String str, int size, String padStr) {
		if (str == null || size <= 0) {
			return str;
		}
		if (isEmpty(padStr)) {
			padStr = " ";
		}
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str;
		}
		str = leftPad(str, strLen + pads / 2, padStr);
		str = rightPad(str, size, padStr);
		return str;
	}

	// Case conversion
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts a String to upper case as per {@link String#toUpperCase()}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.upperCase(null)  = null
	 * StringUtil.upperCase(&quot;&quot;)    = &quot;&quot;
	 * StringUtil.upperCase(&quot;aBc&quot;) = &quot;ABC&quot;
	 * </pre>
	 * 
	 * <p>
	 * <strong>Note:</strong> As described in the documentation for
	 * {@link String#toUpperCase()}, the result of this method is affected by
	 * the current locale. For platform-independent case transformations, the
	 * method {@link #lowerCase(String, Locale)} should be used with a specific
	 * locale (e.g. {@link Locale#ENGLISH}).
	 * </p>
	 * 
	 * @param str
	 *            the String to upper case, may be null
	 * @return the upper cased String, <code>null</code> if null String input
	 */
	public static String upperCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase();
	}

	/**
	 * <p>
	 * Converts a String to upper case as per {@link String#toUpperCase(Locale)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.upperCase(null, Locale.ENGLISH)  = null
	 * StringUtil.upperCase(&quot;&quot;, Locale.ENGLISH)    = &quot;&quot;
	 * StringUtil.upperCase(&quot;aBc&quot;, Locale.ENGLISH) = &quot;ABC&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to upper case, may be null
	 * @param locale
	 *            the locale that defines the case transformation rules, must
	 *            not be null
	 * @return the upper cased String, <code>null</code> if null String input
	 * @since 2.5
	 */
	public static String upperCase(String str, Locale locale) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase(locale);
	}

	/**
	 * <p>
	 * Converts a String to lower case as per {@link String#toLowerCase()}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lowerCase(null)  = null
	 * StringUtil.lowerCase(&quot;&quot;)    = &quot;&quot;
	 * StringUtil.lowerCase(&quot;aBc&quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * <p>
	 * <strong>Note:</strong> As described in the documentation for
	 * {@link String#toLowerCase()}, the result of this method is affected by
	 * the current locale. For platform-independent case transformations, the
	 * method {@link #lowerCase(String, Locale)} should be used with a specific
	 * locale (e.g. {@link Locale#ENGLISH}).
	 * </p>
	 * 
	 * @param str
	 *            the String to lower case, may be null
	 * @return the lower cased String, <code>null</code> if null String input
	 */
	public static String lowerCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toLowerCase();
	}

	/**
	 * <p>
	 * Converts a String to lower case as per {@link String#toLowerCase(Locale)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.lowerCase(null, Locale.ENGLISH)  = null
	 * StringUtil.lowerCase(&quot;&quot;, Locale.ENGLISH)    = &quot;&quot;
	 * StringUtil.lowerCase(&quot;aBc&quot;, Locale.ENGLISH) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to lower case, may be null
	 * @param locale
	 *            the locale that defines the case transformation rules, must
	 *            not be null
	 * @return the lower cased String, <code>null</code> if null String input
	 * @since 2.5
	 */
	public static String lowerCase(String str, Locale locale) {
		if (str == null) {
			return null;
		}
		return str.toLowerCase(locale);
	}

	/**
	 * <p>
	 * Uncapitalizes a String changing the first letter to title case as per
	 * {@link Character#toLowerCase(char)}. No other letters are changed.
	 * </p>
	 * 
	 * @param str
	 *            the String to uncapitalize, may be null
	 * @return the uncapitalized String, <code>null</code> if null String
	 *         input
	 * @deprecated Use the standardly named {@link #uncapitalize(String)}.
	 *             Method will be removed in Commons Lang 3.0.
	 */
	public static String uncapitalise(String str) {
		return uncapitalize(str);
	}

	/**
	 * <p>
	 * Swaps the case of a String changing upper and title case to lower case,
	 * and lower case to upper case.
	 * </p>
	 * 
	 * <ul>
	 * <li>Upper case character converts to Lower case</li>
	 * <li>Title case character converts to Lower case</li>
	 * <li>Lower case character converts to Upper case</li>
	 * </ul>
	 * 
	 * <p>
	 * For a word based algorithm. A
	 * <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.swapCase(null)                 = null
	 * StringUtil.swapCase(&quot;&quot;)                   = &quot;&quot;
	 * StringUtil.swapCase(&quot;The dog has a BONE&quot;) = &quot;tHE DOG HAS A bone&quot;
	 * </pre>
	 * 
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer performs a
	 * word based algorithm. If you only use ASCII, you will notice no change.
	 * </p>
	 * 
	 * @param str
	 *            the String to swap case, may be null
	 * @return the changed String, <code>null</code> if null String input
	 */
	public static String swapCase(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		StringBuffer buffer = new StringBuffer(strLen);

		char ch = 0;
		for (int i = 0; i < strLen; i++) {
			ch = str.charAt(i);
			if (Character.isUpperCase(ch)) {
				ch = Character.toLowerCase(ch);
			} else if (Character.isTitleCase(ch)) {
				ch = Character.toLowerCase(ch);
			} else if (Character.isLowerCase(ch)) {
				ch = Character.toUpperCase(ch);
			}
			buffer.append(ch);
		}
		return buffer.toString();
	}

	// Count matches
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Counts how many times the substring appears in the larger String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String input returns <code>0</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.countMatches(null, *)       = 0
	 * StringUtil.countMatches(&quot;&quot;, *)         = 0
	 * StringUtil.countMatches(&quot;abba&quot;, null)  = 0
	 * StringUtil.countMatches(&quot;abba&quot;, &quot;&quot;)    = 0
	 * StringUtil.countMatches(&quot;abba&quot;, &quot;a&quot;)   = 2
	 * StringUtil.countMatches(&quot;abba&quot;, &quot;ab&quot;)  = 1
	 * StringUtil.countMatches(&quot;abba&quot;, &quot;xxx&quot;) = 0
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param sub
	 *            the substring to count, may be null
	 * @return the number of occurrences, 0 if either String is
	 *         <code>null</code>
	 */
	public static int countMatches(String str, String sub) {
		if (isEmpty(str) || isEmpty(sub)) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1) {
			count++;
			idx += sub.length();
		}
		return count;
	}

	// Character Tests
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the String contains only unicode letters.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String
	 * ("") will return <code>true</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isAlpha(null)   = false
	 * StringUtil.isAlpha(&quot;&quot;)     = true
	 * StringUtil.isAlpha(&quot;  &quot;)   = false
	 * StringUtil.isAlpha(&quot;abc&quot;)  = true
	 * StringUtil.isAlpha(&quot;ab2c&quot;) = false
	 * StringUtil.isAlpha(&quot;ab-c&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains letters, and is non-null
	 */
	public static boolean isAlpha(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetter(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only unicode letters and space (' ').
	 * </p>
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code> An empty String ("")
	 * will return <code>true</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isAlphaSpace(null)   = false
	 * StringUtil.isAlphaSpace(&quot;&quot;)     = true
	 * StringUtil.isAlphaSpace(&quot;  &quot;)   = true
	 * StringUtil.isAlphaSpace(&quot;abc&quot;)  = true
	 * StringUtil.isAlphaSpace(&quot;ab c&quot;) = true
	 * StringUtil.isAlphaSpace(&quot;ab2c&quot;) = false
	 * StringUtil.isAlphaSpace(&quot;ab-c&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains letters and space, and is
	 *         non-null
	 */
	public static boolean isAlphaSpace(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ((Character.isLetter(str.charAt(i)) == false)
					&& (str.charAt(i) != ' ')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only unicode letters or digits.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String
	 * ("") will return <code>true</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isAlphanumeric(null)   = false
	 * StringUtil.isAlphanumeric(&quot;&quot;)     = true
	 * StringUtil.isAlphanumeric(&quot;  &quot;)   = false
	 * StringUtil.isAlphanumeric(&quot;abc&quot;)  = true
	 * StringUtil.isAlphanumeric(&quot;ab c&quot;) = false
	 * StringUtil.isAlphanumeric(&quot;ab2c&quot;) = true
	 * StringUtil.isAlphanumeric(&quot;ab-c&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains letters or digits, and is
	 *         non-null
	 */
	public static boolean isAlphanumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetterOrDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only unicode letters, digits or space (<code>' '</code>).
	 * </p>
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String
	 * ("") will return <code>true</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isAlphanumeric(null)   = false
	 * StringUtil.isAlphanumeric(&quot;&quot;)     = true
	 * StringUtil.isAlphanumeric(&quot;  &quot;)   = true
	 * StringUtil.isAlphanumeric(&quot;abc&quot;)  = true
	 * StringUtil.isAlphanumeric(&quot;ab c&quot;) = true
	 * StringUtil.isAlphanumeric(&quot;ab2c&quot;) = true
	 * StringUtil.isAlphanumeric(&quot;ab-c&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains letters, digits or space,
	 *         and is non-null
	 */
	public static boolean isAlphanumericSpace(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ((Character.isLetterOrDigit(str.charAt(i)) == false)
					&& (str.charAt(i) != ' ')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only unicode digits. A decimal point is not
	 * a unicode digit and returns false.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String
	 * ("") will return <code>true</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isNumeric(null)   = false
	 * StringUtil.isNumeric(&quot;&quot;)     = true
	 * StringUtil.isNumeric(&quot;  &quot;)   = false
	 * StringUtil.isNumeric(&quot;123&quot;)  = true
	 * StringUtil.isNumeric(&quot;12 3&quot;) = false
	 * StringUtil.isNumeric(&quot;ab2c&quot;) = false
	 * StringUtil.isNumeric(&quot;12-3&quot;) = false
	 * StringUtil.isNumeric(&quot;12.3&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains digits, and is non-null
	 */
	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only unicode digits or space (<code>' '</code>).
	 * A decimal point is not a unicode digit and returns false.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String
	 * ("") will return <code>true</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isNumeric(null)   = false
	 * StringUtil.isNumeric(&quot;&quot;)     = true
	 * StringUtil.isNumeric(&quot;  &quot;)   = true
	 * StringUtil.isNumeric(&quot;123&quot;)  = true
	 * StringUtil.isNumeric(&quot;12 3&quot;) = true
	 * StringUtil.isNumeric(&quot;ab2c&quot;) = false
	 * StringUtil.isNumeric(&quot;12-3&quot;) = false
	 * StringUtil.isNumeric(&quot;12.3&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains digits or space, and is
	 *         non-null
	 */
	public static boolean isNumericSpace(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ((Character.isDigit(str.charAt(i)) == false)
					&& (str.charAt(i) != ' ')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only whitespace.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String
	 * ("") will return <code>true</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isWhitespace(null)   = false
	 * StringUtil.isWhitespace(&quot;&quot;)     = true
	 * StringUtil.isWhitespace(&quot;  &quot;)   = true
	 * StringUtil.isWhitespace(&quot;abc&quot;)  = false
	 * StringUtil.isWhitespace(&quot;ab2c&quot;) = false
	 * StringUtil.isWhitespace(&quot;ab-c&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains whitespace, and is non-null
	 * @since 2.0
	 */
	public static boolean isWhitespace(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only lowercase characters.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String
	 * ("") will return <code>false</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isAllLowerCase(null)   = false
	 * StringUtil.isAllLowerCase(&quot;&quot;)     = false
	 * StringUtil.isAllLowerCase(&quot;  &quot;)   = false
	 * StringUtil.isAllLowerCase(&quot;abc&quot;)  = true
	 * StringUtil.isAllLowerCase(&quot;abC&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains lowercase characters, and is
	 *         non-null
	 * @since 2.5
	 */
	public static boolean isAllLowerCase(String str) {
		if (str == null || isEmpty(str)) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLowerCase(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only uppercase characters.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String
	 * ("") will return <code>false</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isAllUpperCase(null)   = false
	 * StringUtil.isAllUpperCase(&quot;&quot;)     = false
	 * StringUtil.isAllUpperCase(&quot;  &quot;)   = false
	 * StringUtil.isAllUpperCase(&quot;ABC&quot;)  = true
	 * StringUtil.isAllUpperCase(&quot;aBC&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains uppercase characters, and is
	 *         non-null
	 * @since 2.5
	 */
	public static boolean isAllUpperCase(String str) {
		if (str == null || isEmpty(str)) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isUpperCase(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	// Defaults
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Returns either the passed in String, or if the String is
	 * <code>null</code>, an empty String ("").
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.defaultString(null)  = &quot;&quot;
	 * StringUtil.defaultString(&quot;&quot;)    = &quot;&quot;
	 * StringUtil.defaultString(&quot;bat&quot;) = &quot;bat&quot;
	 * </pre>
	 * 
	 * @see String#valueOf(Object)
	 * @param str
	 *            the String to check, may be null
	 * @return the passed in String, or the empty String if it was
	 *         <code>null</code>
	 */
	public static String defaultString(String str) {
		return str == null ? EMPTY : str;
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is
	 * <code>null</code>, the value of <code>defaultStr</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.defaultString(null, &quot;NULL&quot;)  = &quot;NULL&quot;
	 * StringUtil.defaultString(&quot;&quot;, &quot;NULL&quot;)    = &quot;&quot;
	 * StringUtil.defaultString(&quot;bat&quot;, &quot;NULL&quot;) = &quot;bat&quot;
	 * </pre>
	 * 
	 * @see String#valueOf(Object)
	 * @param str
	 *            the String to check, may be null
	 * @param defaultStr
	 *            the default String to return if the input is <code>null</code>,
	 *            may be null
	 * @return the passed in String, or the default if it was <code>null</code>
	 */
	public static String defaultString(String str, String defaultStr) {
		return str == null ? defaultStr : str;
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is empty or
	 * <code>null</code>, the value of <code>defaultStr</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.defaultIfEmpty(null, &quot;NULL&quot;)  = &quot;NULL&quot;
	 * StringUtil.defaultIfEmpty(&quot;&quot;, &quot;NULL&quot;)    = &quot;NULL&quot;
	 * StringUtil.defaultIfEmpty(&quot;bat&quot;, &quot;NULL&quot;) = &quot;bat&quot;
	 * StringUtil.defaultIfEmpty(&quot;&quot;, null)      = null
	 * </pre>
	 * 
	 * @see StringUtil#defaultString(String, String)
	 * @param str
	 *            the String to check, may be null
	 * @param defaultStr
	 *            the default String to return if the input is empty ("") or
	 *            <code>null</code>, may be null
	 * @return the passed in String, or the default
	 */
	public static String defaultIfEmpty(String str, String defaultStr) {
		return StringUtil.isEmpty(str) ? defaultStr : str;
	}

	// Reversing
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Reverses a String as per {@link StringBuffer#reverse()}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.reverse(null)  = null
	 * StringUtil.reverse(&quot;&quot;)    = &quot;&quot;
	 * StringUtil.reverse(&quot;bat&quot;) = &quot;tab&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to reverse, may be null
	 * @return the reversed String, <code>null</code> if null String input
	 */
	public static String reverse(String str) {
		if (str == null) {
			return null;
		}
		return new StringBuffer(str).reverse().toString();
	}

	// Abbreviating
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Abbreviates a String using ellipses. This will turn "Now is the time for
	 * all good men" into "Now is the time for..."
	 * </p>
	 * 
	 * <p>
	 * Specifically:
	 * <ul>
	 * <li>If <code>str</code> is less than <code>maxWidth</code>
	 * characters long, return it.</li>
	 * <li>Else abbreviate it to
	 * <code>(substring(str, 0, max-3) + "...")</code>.</li>
	 * <li>If <code>maxWidth</code> is less than <code>4</code>, throw an
	 * <code>IllegalArgumentException</code>.</li>
	 * <li>In no case will it return a String of length greater than
	 * <code>maxWidth</code>.</li>
	 * </ul>
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.abbreviate(null, *)      = null
	 * StringUtil.abbreviate(&quot;&quot;, 4)        = &quot;&quot;
	 * StringUtil.abbreviate(&quot;abcdefg&quot;, 6) = &quot;abc...&quot;
	 * StringUtil.abbreviate(&quot;abcdefg&quot;, 7) = &quot;abcdefg&quot;
	 * StringUtil.abbreviate(&quot;abcdefg&quot;, 8) = &quot;abcdefg&quot;
	 * StringUtil.abbreviate(&quot;abcdefg&quot;, 4) = &quot;a...&quot;
	 * StringUtil.abbreviate(&quot;abcdefg&quot;, 3) = IllegalArgumentException
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param maxWidth
	 *            maximum length of result String, must be at least 4
	 * @return abbreviated String, <code>null</code> if null String input
	 * @throws IllegalArgumentException
	 *             if the width is too small
	 * @since 2.0
	 */
	public static String abbreviate(String str, int maxWidth) {
		return abbreviate(str, 0, maxWidth);
	}

	/**
	 * <p>
	 * Abbreviates a String using ellipses. This will turn "Now is the time for
	 * all good men" into "...is the time for..."
	 * </p>
	 * 
	 * <p>
	 * Works like <code>abbreviate(String, int)</code>, but allows you to
	 * specify a "left edge" offset. Note that this left edge is not necessarily
	 * going to be the leftmost character in the result, or the first character
	 * following the ellipses, but it will appear somewhere in the result.
	 * 
	 * <p>
	 * In no case will it return a String of length greater than
	 * <code>maxWidth</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.abbreviate(null, *, *)                = null
	 * StringUtil.abbreviate(&quot;&quot;, 0, 4)                  = &quot;&quot;
	 * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, -1, 10) = &quot;abcdefg...&quot;
	 * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 0, 10)  = &quot;abcdefg...&quot;
	 * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 1, 10)  = &quot;abcdefg...&quot;
	 * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 4, 10)  = &quot;abcdefg...&quot;
	 * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 5, 10)  = &quot;...fghi...&quot;
	 * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 6, 10)  = &quot;...ghij...&quot;
	 * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 8, 10)  = &quot;...ijklmno&quot;
	 * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 10, 10) = &quot;...ijklmno&quot;
	 * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 12, 10) = &quot;...ijklmno&quot;
	 * StringUtil.abbreviate(&quot;abcdefghij&quot;, 0, 3)        = IllegalArgumentException
	 * StringUtil.abbreviate(&quot;abcdefghij&quot;, 5, 6)        = IllegalArgumentException
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param offset
	 *            left edge of source String
	 * @param maxWidth
	 *            maximum length of result String, must be at least 4
	 * @return abbreviated String, <code>null</code> if null String input
	 * @throws IllegalArgumentException
	 *             if the width is too small
	 * @since 2.0
	 */
	public static String abbreviate(String str, int offset, int maxWidth) {
		if (str == null) {
			return null;
		}
		if (maxWidth < 4) {
			throw new IllegalArgumentException(
					"Minimum abbreviation width is 4");
		}
		if (str.length() <= maxWidth) {
			return str;
		}
		if (offset > str.length()) {
			offset = str.length();
		}
		if ((str.length() - offset) < (maxWidth - 3)) {
			offset = str.length() - (maxWidth - 3);
		}
		if (offset <= 4) {
			return str.substring(0, maxWidth - 3) + "...";
		}
		if (maxWidth < 7) {
			throw new IllegalArgumentException(
					"Minimum abbreviation width with offset is 7");
		}
		if ((offset + (maxWidth - 3)) < str.length()) {
			return "..." + abbreviate(str.substring(offset), maxWidth - 3);
		}
		return "..." + str.substring(str.length() - (maxWidth - 3));
	}

	/**
	 * <p>
	 * Abbreviates a String to the length passed, replacing the middle
	 * characters with the supplied replacement String.
	 * </p>
	 * 
	 * <p>
	 * This abbreviation only occurs if the following criteria is met:
	 * <ul>
	 * <li>Neither the String for abbreviation nor the replacement String are
	 * null or empty </li>
	 * <li>The length to truncate to is less than the length of the supplied
	 * String</li>
	 * <li>The length to truncate to is greater than 0</li>
	 * <li>The abbreviated String will have enough room for the length supplied
	 * replacement String and the first and last characters of the supplied
	 * String for abbreviation</li>
	 * </ul>
	 * Otherwise, the returned String will be the same as the supplied String
	 * for abbreviation.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.abbreviateMiddle(null, null, 0)      = null
	 * StringUtil.abbreviateMiddle(&quot;abc&quot;, null, 0)      = &quot;abc&quot;
	 * StringUtil.abbreviateMiddle(&quot;abc&quot;, &quot;.&quot;, 0)      = &quot;abc&quot;
	 * StringUtil.abbreviateMiddle(&quot;abc&quot;, &quot;.&quot;, 3)      = &quot;abc&quot;
	 * StringUtil.abbreviateMiddle(&quot;abcdef&quot;, &quot;.&quot;, 4)     = &quot;ab.f&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to abbreviate, may be null
	 * @param middle
	 *            the String to replace the middle characters with, may be null
	 * @param length
	 *            the length to abbreviate <code>str</code> to.
	 * @return the abbreviated String if the above criteria is met, or the
	 *         original String supplied for abbreviation.
	 * @since 2.5
	 */
	public static String abbreviateMiddle(String str, String middle, int length) {
		if (isEmpty(str) || isEmpty(middle)) {
			return str;
		}

		if (length >= str.length() || length < (middle.length() + 2)) {
			return str;
		}

		int targetSting = length - middle.length();
		int startOffset = targetSting / 2 + targetSting % 2;
		int endOffset = str.length() - targetSting / 2;

		StringBuffer builder = new StringBuffer(length);
		builder.append(str.substring(0, startOffset));
		builder.append(middle);
		builder.append(str.substring(endOffset));

		return builder.toString();
	}

	// Difference
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares two Strings, and returns the portion where they differ. (More
	 * precisely, return the remainder of the second String, starting from where
	 * it's different from the first.)
	 * </p>
	 * 
	 * <p>
	 * For example,
	 * <code>difference("i am a machine", "i am a robot") -> "robot"</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.difference(null, null) = null
	 * StringUtil.difference(&quot;&quot;, &quot;&quot;) = &quot;&quot;
	 * StringUtil.difference(&quot;&quot;, &quot;abc&quot;) = &quot;abc&quot;
	 * StringUtil.difference(&quot;abc&quot;, &quot;&quot;) = &quot;&quot;
	 * StringUtil.difference(&quot;abc&quot;, &quot;abc&quot;) = &quot;&quot;
	 * StringUtil.difference(&quot;ab&quot;, &quot;abxyz&quot;) = &quot;xyz&quot;
	 * StringUtil.difference(&quot;abcde&quot;, &quot;abxyz&quot;) = &quot;xyz&quot;
	 * StringUtil.difference(&quot;abcde&quot;, &quot;xyz&quot;) = &quot;xyz&quot;
	 * </pre>
	 * 
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return the portion of str2 where it differs from str1; returns the empty
	 *         String if they are equal
	 * @since 2.0
	 */
	public static String difference(String str1, String str2) {
		if (str1 == null) {
			return str2;
		}
		if (str2 == null) {
			return str1;
		}
		int at = indexOfDifference(str1, str2);
		if (at == -1) {
			return EMPTY;
		}
		return str2.substring(at);
	}

	/**
	 * <p>
	 * Compares two Strings, and returns the index at which the Strings begin to
	 * differ.
	 * </p>
	 * 
	 * <p>
	 * For example,
	 * <code>indexOfDifference("i am a machine", "i am a robot") -> 7</code>
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOfDifference(null, null) = -1
	 * StringUtil.indexOfDifference(&quot;&quot;, &quot;&quot;) = -1
	 * StringUtil.indexOfDifference(&quot;&quot;, &quot;abc&quot;) = 0
	 * StringUtil.indexOfDifference(&quot;abc&quot;, &quot;&quot;) = 0
	 * StringUtil.indexOfDifference(&quot;abc&quot;, &quot;abc&quot;) = -1
	 * StringUtil.indexOfDifference(&quot;ab&quot;, &quot;abxyz&quot;) = 2
	 * StringUtil.indexOfDifference(&quot;abcde&quot;, &quot;abxyz&quot;) = 2
	 * StringUtil.indexOfDifference(&quot;abcde&quot;, &quot;xyz&quot;) = 0
	 * </pre>
	 * 
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return the index where str2 and str1 begin to differ; -1 if they are
	 *         equal
	 * @since 2.0
	 */
	public static int indexOfDifference(String str1, String str2) {
		if (str1 == str2) {
			return -1;
		}
		if (str1 == null || str2 == null) {
			return 0;
		}
		int i;
		for (i = 0; i < str1.length() && i < str2.length(); ++i) {
			if (str1.charAt(i) != str2.charAt(i)) {
				break;
			}
		}
		if (i < str2.length() || i < str1.length()) {
			return i;
		}
		return -1;
	}

	/**
	 * <p>
	 * Compares all Strings in an array and returns the index at which the
	 * Strings begin to differ.
	 * </p>
	 * 
	 * <p>
	 * For example,
	 * <code>indexOfDifference(new String[] {"i am a machine", "i am a robot"}) -> 7</code>
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.indexOfDifference(null) = -1
	 * StringUtil.indexOfDifference(new String[] {}) = -1
	 * StringUtil.indexOfDifference(new String[] {&quot;abc&quot;}) = -1
	 * StringUtil.indexOfDifference(new String[] {null, null}) = -1
	 * StringUtil.indexOfDifference(new String[] {&quot;&quot;, &quot;&quot;}) = -1
	 * StringUtil.indexOfDifference(new String[] {&quot;&quot;, null}) = 0
	 * StringUtil.indexOfDifference(new String[] {&quot;abc&quot;, null, null}) = 0
	 * StringUtil.indexOfDifference(new String[] {null, null, &quot;abc&quot;}) = 0
	 * StringUtil.indexOfDifference(new String[] {&quot;&quot;, &quot;abc&quot;}) = 0
	 * StringUtil.indexOfDifference(new String[] {&quot;abc&quot;, &quot;&quot;}) = 0
	 * StringUtil.indexOfDifference(new String[] {&quot;abc&quot;, &quot;abc&quot;}) = -1
	 * StringUtil.indexOfDifference(new String[] {&quot;abc&quot;, &quot;a&quot;}) = 1
	 * StringUtil.indexOfDifference(new String[] {&quot;ab&quot;, &quot;abxyz&quot;}) = 2
	 * StringUtil.indexOfDifference(new String[] {&quot;abcde&quot;, &quot;abxyz&quot;}) = 2
	 * StringUtil.indexOfDifference(new String[] {&quot;abcde&quot;, &quot;xyz&quot;}) = 0
	 * StringUtil.indexOfDifference(new String[] {&quot;xyz&quot;, &quot;abcde&quot;}) = 0
	 * StringUtil.indexOfDifference(new String[] {&quot;i am a machine&quot;, &quot;i am a robot&quot;}) = 7
	 * </pre>
	 * 
	 * @param strs
	 *            array of strings, entries may be null
	 * @return the index where the strings begin to differ; -1 if they are all
	 *         equal
	 * @since 2.4
	 */
	public static int indexOfDifference(String[] strs) {
		if (strs == null || strs.length <= 1) {
			return -1;
		}
		boolean anyStringNull = false;
		boolean allStringsNull = true;
		int arrayLen = strs.length;
		int shortestStrLen = Integer.MAX_VALUE;
		int longestStrLen = 0;

		// find the min and max string lengths; this avoids checking to make
		// sure we are not exceeding the length of the string each time through
		// the bottom loop.
		for (int i = 0; i < arrayLen; i++) {
			if (strs[i] == null) {
				anyStringNull = true;
				shortestStrLen = 0;
			} else {
				allStringsNull = false;
				shortestStrLen = Math.min(strs[i].length(), shortestStrLen);
				longestStrLen = Math.max(strs[i].length(), longestStrLen);
			}
		}

		// handle lists containing all nulls or all empty strings
		if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
			return -1;
		}

		// handle lists containing some nulls or some empty strings
		if (shortestStrLen == 0) {
			return 0;
		}

		// find the position with the first difference across all strings
		int firstDiff = -1;
		for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
			char comparisonChar = strs[0].charAt(stringPos);
			for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
				if (strs[arrayPos].charAt(stringPos) != comparisonChar) {
					firstDiff = stringPos;
					break;
				}
			}
			if (firstDiff != -1) {
				break;
			}
		}

		if (firstDiff == -1 && shortestStrLen != longestStrLen) {
			// we compared all of the characters up to the length of the
			// shortest string and didn't find a match, but the string lengths
			// vary, so return the length of the shortest string.
			return shortestStrLen;
		}
		return firstDiff;
	}

	/**
	 * <p>
	 * Compares all Strings in an array and returns the initial sequence of
	 * characters that is common to all of them.
	 * </p>
	 * 
	 * <p>
	 * For example,
	 * <code>getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) -> "i am a "</code>
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.getCommonPrefix(null) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;abc&quot;}) = &quot;abc&quot;
	 * StringUtil.getCommonPrefix(new String[] {null, null}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;&quot;, &quot;&quot;}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;&quot;, null}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;abc&quot;, null, null}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {null, null, &quot;abc&quot;}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;&quot;, &quot;abc&quot;}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;abc&quot;, &quot;&quot;}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;abc&quot;, &quot;abc&quot;}) = &quot;abc&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;abc&quot;, &quot;a&quot;}) = &quot;a&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;ab&quot;, &quot;abxyz&quot;}) = &quot;ab&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;abcde&quot;, &quot;abxyz&quot;}) = &quot;ab&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;abcde&quot;, &quot;xyz&quot;}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;xyz&quot;, &quot;abcde&quot;}) = &quot;&quot;
	 * StringUtil.getCommonPrefix(new String[] {&quot;i am a machine&quot;, &quot;i am a robot&quot;}) = &quot;i am a &quot;
	 * </pre>
	 * 
	 * @param strs
	 *            array of String objects, entries may be null
	 * @return the initial sequence of characters that are common to all Strings
	 *         in the array; empty String if the array is null, the elements are
	 *         all null or if there is no common prefix.
	 * @since 2.4
	 */
	public static String getCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0) {
			return EMPTY;
		}
		int smallestIndexOfDiff = indexOfDifference(strs);
		if (smallestIndexOfDiff == -1) {
			// all strings were identical
			if (strs[0] == null) {
				return EMPTY;
			}
			return strs[0];
		} else if (smallestIndexOfDiff == 0) {
			// there were no common initial characters
			return EMPTY;
		} else {
			// we found a common initial character sequence
			return strs[0].substring(0, smallestIndexOfDiff);
		}
	}

	// Misc
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Find the Levenshtein distance between two Strings.
	 * </p>
	 * 
	 * <p>
	 * This is the number of changes needed to change one String into another,
	 * where each change is a single character modification (deletion, insertion
	 * or substitution).
	 * </p>
	 * 
	 * <p>
	 * The previous implementation of the Levenshtein distance algorithm was
	 * from <a
	 * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a>
	 * </p>
	 * 
	 * <p>
	 * Chas Emerick has written an implementation in Java, which avoids an
	 * OutOfMemoryError which can occur when my Java implementation is used with
	 * very large strings.<br>
	 * This implementation of the Levenshtein distance algorithm is from <a
	 * href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a>
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.getLevenshteinDistance(null, *)             = IllegalArgumentException
	 * StringUtil.getLevenshteinDistance(*, null)             = IllegalArgumentException
	 * StringUtil.getLevenshteinDistance(&quot;&quot;,&quot;&quot;)               = 0
	 * StringUtil.getLevenshteinDistance(&quot;&quot;,&quot;a&quot;)              = 1
	 * StringUtil.getLevenshteinDistance(&quot;aaapppp&quot;, &quot;&quot;)       = 7
	 * StringUtil.getLevenshteinDistance(&quot;frog&quot;, &quot;fog&quot;)       = 1
	 * StringUtil.getLevenshteinDistance(&quot;fly&quot;, &quot;ant&quot;)        = 3
	 * StringUtil.getLevenshteinDistance(&quot;elephant&quot;, &quot;hippo&quot;) = 7
	 * StringUtil.getLevenshteinDistance(&quot;hippo&quot;, &quot;elephant&quot;) = 7
	 * StringUtil.getLevenshteinDistance(&quot;hippo&quot;, &quot;zzzzzzzz&quot;) = 8
	 * StringUtil.getLevenshteinDistance(&quot;hello&quot;, &quot;hallo&quot;)    = 1
	 * </pre>
	 * 
	 * @param s
	 *            the first String, must not be null
	 * @param t
	 *            the second String, must not be null
	 * @return result distance
	 * @throws IllegalArgumentException
	 *             if either String input <code>null</code>
	 */
	public static int getLevenshteinDistance(String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		/*
		 * The difference between this impl. and the previous is that, rather
		 * than creating and retaining a matrix of size s.length()+1 by
		 * t.length()+1, we maintain two single-dimensional arrays of length
		 * s.length()+1. The first, d, is the 'current working' distance array
		 * that maintains the newest distance cost counts as we iterate through
		 * the characters of String s. Each time we increment the index of
		 * String t we are comparing, d is copied to p, the second int[]. Doing
		 * so allows us to retain the previous cost counts as required by the
		 * algorithm (taking the minimum of the cost count to the left, up one,
		 * and diagonally up and to the left of the current cost count being
		 * calculated). (Note that the arrays aren't really copied anymore, just
		 * switched...this is clearly much better than cloning an array or doing
		 * a System.arraycopy() each time through the outer loop.)
		 * 
		 * Effectively, the difference between the two implementations is this
		 * one does not cause an out of memory condition when calculating the LD
		 * over two very large strings.
		 */

		int n = s.length(); // length of s
		int m = t.length(); // length of t

		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}

		if (n > m) {
			// swap the input strings to consume less memory
			String tmp = s;
			s = t;
			t = tmp;
			n = m;
			m = t.length();
		}

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char t_j; // jth character of t

		int cost; // cost

		for (i = 0; i <= n; i++) {
			p[i] = i;
		}

		for (j = 1; j <= m; j++) {
			t_j = t.charAt(j - 1);
			d[0] = j;

			for (i = 1; i <= n; i++) {
				cost = s.charAt(i - 1) == t_j ? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally left
				// and up +cost
				d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
						+ cost);
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		}

		// our last action in the above loop was to switch d and p, so p now
		// actually has the most recent cost counts
		return p[n];
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>int</code> values.
	 * </p>
	 * 
	 * @param a
	 *            value 1
	 * @param b
	 *            value 2
	 * @param c
	 *            value 3
	 * @return the smallest of the values
	 */
	/*
	 * private static int min(int a, int b, int c) { // Method copied from
	 * NumberUtils to avoid dependency on subpackage if (b < a) { a = b; } if (c <
	 * a) { a = c; } return a; }
	 */

	// startsWith
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Check if a String starts with a specified prefix.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two
	 * <code>null</code> references are considered to be equal. The comparison
	 * is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.startsWith(null, null)      = true
	 * StringUtil.startsWith(null, &quot;abc&quot;)     = false
	 * StringUtil.startsWith(&quot;abcdef&quot;, null)  = false
	 * StringUtil.startsWith(&quot;abcdef&quot;, &quot;abc&quot;) = true
	 * StringUtil.startsWith(&quot;ABCDEF&quot;, &quot;abc&quot;) = false
	 * </pre>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param prefix
	 *            the prefix to find, may be null
	 * @return <code>true</code> if the String starts with the prefix, case
	 *         sensitive, or both <code>null</code>
	 * @since 2.4
	 */
	public static boolean startsWith(String str, String prefix) {
		return startsWith(str, prefix, false);
	}

	/**
	 * <p>
	 * Check if a String starts with a specified prefix (optionally case
	 * insensitive).
	 * </p>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param prefix
	 *            the prefix to find, may be null
	 * @param ignoreCase
	 *            inidicates whether the compare should ignore case (case
	 *            insensitive) or not.
	 * @return <code>true</code> if the String starts with the prefix or both
	 *         <code>null</code>
	 */
	private static boolean startsWith(String str, String prefix,
			boolean ignoreCase) {
		if (str == null || prefix == null) {
			return (str == null && prefix == null);
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
	}

	/**
	 * <p>
	 * Check if a String starts with any of an array of specified strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.startsWithAny(null, null)      = false
	 * StringUtil.startsWithAny(null, new String[] {&quot;abc&quot;})  = false
	 * StringUtil.startsWithAny(&quot;abcxyz&quot;, null)     = false
	 * StringUtil.startsWithAny(&quot;abcxyz&quot;, new String[] {&quot;&quot;}) = false
	 * StringUtil.startsWithAny(&quot;abcxyz&quot;, new String[] {&quot;abc&quot;}) = true
	 * StringUtil.startsWithAny(&quot;abcxyz&quot;, new String[] {null, &quot;xyz&quot;, &quot;abc&quot;}) = true
	 * </pre>
	 * 
	 * @see #startsWith(String, String)
	 * @param string
	 *            the String to check, may be null
	 * @param searchStrings
	 *            the Strings to find, may be null or empty
	 * @return <code>true</code> if the String starts with any of the the
	 *         prefixes, case insensitive, or both <code>null</code>
	 * @since 2.5
	 */
	public static boolean startsWithAny(String string, String[] searchStrings) {
		if (isEmpty(string) || searchStrings==null ||searchStrings.length==0) {
			return false;
		}
		for (int i = 0; i < searchStrings.length; i++) {
			String searchString = searchStrings[i];
			if (StringUtil.startsWith(string, searchString)) {
				return true;
			}
		}
		return false;
	}

	// endsWith
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Check if a String ends with a specified suffix.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two
	 * <code>null</code> references are considered to be equal. The comparison
	 * is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.endsWith(null, null)      = true
	 * StringUtil.endsWith(null, &quot;def&quot;)     = false
	 * StringUtil.endsWith(&quot;abcdef&quot;, null)  = false
	 * StringUtil.endsWith(&quot;abcdef&quot;, &quot;def&quot;) = true
	 * StringUtil.endsWith(&quot;ABCDEF&quot;, &quot;def&quot;) = false
	 * StringUtil.endsWith(&quot;ABCDEF&quot;, &quot;cde&quot;) = false
	 * </pre>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param suffix
	 *            the suffix to find, may be null
	 * @return <code>true</code> if the String ends with the suffix, case
	 *         sensitive, or both <code>null</code>
	 * @since 2.4
	 */
	public static boolean endsWith(String str, String suffix) {
		return endsWith(str, suffix, false);
	}

	/**
	 * <p>
	 * Case insensitive check if a String ends with a specified suffix.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two
	 * <code>null</code> references are considered to be equal. The comparison
	 * is case insensitive.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.endsWithIgnoreCase(null, null)      = true
	 * StringUtil.endsWithIgnoreCase(null, &quot;def&quot;)     = false
	 * StringUtil.endsWithIgnoreCase(&quot;abcdef&quot;, null)  = false
	 * StringUtil.endsWithIgnoreCase(&quot;abcdef&quot;, &quot;def&quot;) = true
	 * StringUtil.endsWithIgnoreCase(&quot;ABCDEF&quot;, &quot;def&quot;) = true
	 * StringUtil.endsWithIgnoreCase(&quot;ABCDEF&quot;, &quot;cde&quot;) = false
	 * </pre>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param suffix
	 *            the suffix to find, may be null
	 * @return <code>true</code> if the String ends with the suffix, case
	 *         insensitive, or both <code>null</code>
	 * @since 2.4
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		return endsWith(str, suffix, true);
	}

	/**
	 * <p>
	 * Check if a String ends with a specified suffix (optionally case
	 * insensitive).
	 * </p>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param suffix
	 *            the suffix to find, may be null
	 * @param ignoreCase
	 *            inidicates whether the compare should ignore case (case
	 *            insensitive) or not.
	 * @return <code>true</code> if the String starts with the prefix or both
	 *         <code>null</code>
	 */
	private static boolean endsWith(String str, String suffix,
			boolean ignoreCase) {
		if (str == null || suffix == null) {
			return (str == null && suffix == null);
		}
		if (suffix.length() > str.length()) {
			return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix
				.length());
	}
	
	public static String URLDecodeUTF8(String input) throws UnsupportedEncodingException {
		return URLDecode(input, "utf-8");
	}
	
	public static String URLDecode(String input, String encoding) throws UnsupportedEncodingException {
		String output = "";
		if (StringUtils.isNotBlank(input)) {
			output = URLDecoder.decode(input, encoding);
		}
		return output;
	}
	

	/**��ȡ����
	 * @return
	 */
	public static String getRate(String source){
		if("null".equalsIgnoreCase(source)){
			return "";
		}
		else{
			return source+"%";
		}
	}
	
	/**
	 * �ƶ�����ǰ������
	 * 0 ���ǰ�油��0  ��d ������Ϊ������ 
	 * @param value
	 * @param len len ��?��
	 * @return
	 */
	public static String numberPadding0(long value,int len){
	    return String.format("%0"+len+"d", value); 
	}
	
	/** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */  
    public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }  
    
    /**
     * 替换占位符
     * @param sourceStr 原字符串
     * @param descMap 替换的字符串Map
     * @param startStr 占位符开始符
     * @param endStr 占位符结束符
     * @return
     */
    public static String replacePlaceholder(String sourceStr, Map<String, Object> descMap, String startStr, String endStr) {
    	String temp = "" + sourceStr;
    	temp = "," + temp + ",";
		String[] ary = temp.split("\\$", 0);
		String temStr = "";
		String replaceOri = "";
		String replaceTar = "";
		for (int i=1; i<ary.length; i++) {
			temStr = ary[i];
			replaceOri = temStr.substring(1, temStr.indexOf(endStr));
			replaceTar = (String) descMap.get(replaceOri);
			replaceTar = replaceTar != null ? replaceTar : "";
			temp = temp.replace(startStr + replaceOri + endStr, replaceTar);
		}
		temp = temp.substring(1, temp.length() -1);
		return temp;
    }
    
    public static String toString(Object obj) {
		return obj != null ? obj.toString() : "";
	}
    
    /**
     * 去掉空格字符 单引号
     * @param string
     * @return
     */
    public static String replaceSpace(String string){
    	return string.trim().replaceAll(" ", "").replaceAll("'", "");
    }
}
