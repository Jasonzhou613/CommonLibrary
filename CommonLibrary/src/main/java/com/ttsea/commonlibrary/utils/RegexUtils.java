package com.ttsea.commonlibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用正则表达式 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class RegexUtils {

    /**
     * 常字符，包含标点
     */
    public static final String COMMON_CHAR = "^[\\w \\p{P}]+$";

    /**
     * 空行
     */
    public static final String EMPTY_LINE = "^[\\s\\n]*$";

    /**
     * 可用作人名的字符
     */
    public static final String PERSION_NAME = "^[\\w .\u00B7-]+$";

    /**
     * 中国的电话
     */
    public static final String CN_PHONE = "^((13[0-9])|(14[5-9])|(15[^4,//D])|(18[0,5-9]))\\d{8}$";

    /**
     * Email: The Official Standard: RFC 2822
     **/
    public static final String EMAIL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    /**
     * <b>description :</b> 执行正则表达匹配 </br><b>time :</b> 2012-8-16 下午10:03:26
     *
     * @param regex 正则表达式
     * @param str   字符内容
     * @return 如果正则表达式匹配成功，返回true，否则返回false。
     */
    public static boolean matcherRegex(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }

    /**
     * <b>description :</b> 匹配有效字符（中英文数字及常用标点） </br><b>time :</b> 2012-8-16
     * 下午10:03:26
     *
     * @param str 字符内容
     * @return 如果正则表达式匹配成功，返回true，否则返回false。
     */
    public static boolean matchCommonChar(String str) {
        return matcherRegex(COMMON_CHAR, str);
    }

    /**
     * <b>description :</b> 判断手机号码 </br><b>time :</b> 2012-8-16 下午10:03:26
     *
     * @param number 手机号码
     * @return 如果正则表达式匹配成功，返回true，否则返回false。
     */
    public static boolean matchCNMobileNumber(String number) {
        return matcherRegex(CN_PHONE, number);
    }

    /**
     * <b>description :</b> 匹配空行 </br><b>time :</b> 2012-8-16 下午10:03:26
     *
     * @param line 字符内容
     * @return 如果正则表达式匹配成功，返回true，否则返回false。
     */
    public static boolean matchEmptyLine(String line) {
        return null == line ? true : matcherRegex(EMPTY_LINE, line);
    }

    /**
     * <b>description :</b> 清除标点符号 </br><b>time :</b> 2012-8-22 下午16:53:26
     *
     * @param content 字符内容
     * @return 如果正则表达式匹配成功，返回true，否则返回false。
     */
    public static String cleanPunctuation(String content) {
        return content.replaceAll("[\\p{P}‘’“”]", content);
    }

    /**
     * @param email
     * @return
     */
    public static boolean matchEmail(String email) {
        return null == email ? false : matcherRegex(EMAIL, email);
    }

    /**
     * <b>description :</b> 判断是否为有效的人物名字，可带·间隔符号。 </br><b>time :</b> 2012-8-22
     * 下午18:03:26
     *
     * @param name 字符内容
     * @return 如果正则表达式匹配成功，返回true，否则返回false。
     */
    public static boolean matchPersionName(String name) {
        return null == name ? false : matcherRegex(PERSION_NAME, name);
    }

    /**
     * 判断输入的是否是手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        /*
         * 可接受的电话格式有：
         */
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        /*
         * 可接受的电话格式有：
         */
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if (matcher.matches() || matcher2.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断ip是否正确
     *
     * @param ip
     * @return
     */
    public static boolean isIP(String ip) {
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(rexp);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * 判断端口号是否正确
     *
     * @param port
     * @return
     */
    public static boolean isPort(String port) {
        if (port == null || port.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(port);
        if (isNum.matches() && port.length() < 6 && Integer.valueOf(port) >= 1
                && Integer.valueOf(port) <= 65535) {
            return true;
        }
        return false;
    }

    /** 替换最后的字符 */
    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")",
                replacement);
    }
}
