package com.euler.statistics.utils;

import cn.hutool.core.date.DateUtil;
import com.euler.common.core.domain.dto.KeyValueDto;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsUtils {


    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_SIMPLE = "M.d";

    /**
     * @param start
     * @param end
     * @return
     */
    @SneakyThrows
    public static List<String> getDate(String start, String end) {
        return getDate(start, end, FORMAT_DATE);
    }


    /**
     * @param start
     * @param end
     * @return
     */
    @SneakyThrows
    public static List<String> getDate(String start, String end, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        List<String> list = new ArrayList<>();
        try {
            Date date_start = DateUtil.parse(start);
            Date date_end = DateUtil.parse(end);
            Date date = date_start;
            Calendar cd = Calendar.getInstance();//用Calendar 进行日期比较判断
            while (date.getTime() <= date_end.getTime()) {
                list.add(sdf.format(date));
                cd.setTime(date);
                cd.add(Calendar.DATE, 1);//增加一天 放入集合
                date = cd.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<KeyValueDto<String>> getTimeList() {
        List<KeyValueDto<String>> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i % 2 == 0) {
                KeyValueDto<String> keyValueDto = new KeyValueDto<>();
                keyValueDto.setKey(i + ":00");
                keyValueDto.setValue(i + ":00");
                list.add(keyValueDto);
            }
        }
        return list;
    }


    /**
     * @param start
     * @param end
     * @return
     */
    @SneakyThrows
    public static List<KeyValueDto<String>> getDateList(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_SIMPLE);
        SimpleDateFormat keySdf = new SimpleDateFormat(FORMAT_DATE);

        List<KeyValueDto<String>> list = new ArrayList<>();
        try {
            Date date_start = DateUtil.parse(start);
            Date date_end = DateUtil.parse(end);
            Date date = date_start;
            Calendar cd = Calendar.getInstance();//用Calendar 进行日期比较判断
            while (date.getTime() <= date_end.getTime()) {
                KeyValueDto<String> keyValueDto = new KeyValueDto<>();
                keyValueDto.setKey(keySdf.format(date));
                keyValueDto.setValue(sdf.format(date));
                list.add(keyValueDto);
                cd.setTime(date);
                cd.add(Calendar.DATE, 1);//增加一天 放入集合
                date = cd.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}

