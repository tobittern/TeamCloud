package com.euler.community.utils;

import java.util.Arrays;
import java.util.Optional;

public class CommonForCommunityUtils {

    /**
     * 根据文件路径返回他的类型
     *
     * @param path
     * @return
     */
    public static Integer getResourceType(String path) {
        String[] split = path.split("\\.");
        if (split.length > 0) {
            String hz = split[split.length - 1].toLowerCase();
            String[] pictureTypeList = new String[]{"png", "jpg", "jpeg", "gif", "bmp", "pdf"};
            Optional<String> pictureTypeAny = Arrays.stream(pictureTypeList).filter(a -> a.equals(hz)).findAny();
            if (pictureTypeAny.isPresent()) {
                return 1;
            }
            String[] videoTypeList = new String[]{"mp4", "avi", "wmv", "mov", "flv"};
            Optional<String> videoTypeAny = Arrays.stream(videoTypeList).filter(a -> a.equals(hz)).findAny();
            if (videoTypeAny.isPresent()) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * 根据文件路径返回他的文件名
     *
     * @param path
     * @return
     */
    public static String getResourceName(String path) {
        return "";
    }


    /**
     * 获取展示的类型
     *
     * @return
     */
    public static Integer getCoverType(Integer width, Integer height, String resource) {
        if (width == null || height == null || width == 0 || height == 0) {
            return 0;
        }
        String[] split = resource.split(",");
        if (split.length == 1 && width >= height) {
            return 1;
        }
        if (split.length == 1 && width < height) {
            return 2;
        }
        if (split.length == 2) {
            return 3;
        }
        if (split.length > 2) {
            return 4;
        }
        return 0;
    }


    /**
     * //统计字符串包含某个字符串的个数
     * @param str1  原字符
     * @param str2  要统计的字符
     * @return 出现的次数
     */

    public static int countStr(String str1, String str2) {
        int counter = 0;
        counter = countStr(str1, str2, counter);
        return counter;
    }

    //递归函数
    private static int countStr(String str1, String str2, int counter) {
        if (str1.contains(str2)) {
            counter++;
            counter = countStr(str1.substring(str1.indexOf(str2) + str2.length()), str2, counter);
        }
        return counter;
    }
}

