package com.ddl.egg.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liujs on 2016/8/19.
 */
public class EmojiUtil {

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (StringUtils.isNotBlank(source)) {
            int len = source.length();
            StringBuilder buf = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                char codePoint = source.charAt(i);
                if (isNotEmojiCharacter(codePoint)) {
                    buf.append(codePoint);
                } else {
                    buf.append("*");
                }
            }
            return buf.toString();
        } else {
            return "";
        }
    }

    public static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}
