package com.babyfs.utils;

import java.util.Random;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-22 下午10:23
 */
public class KeyUtil {

    /**
     * 生产主键
     *
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();

        Integer result = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(result);

    }
}
