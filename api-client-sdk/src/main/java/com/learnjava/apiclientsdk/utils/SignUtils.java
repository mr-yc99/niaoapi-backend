package com.learnjava.apiclientsdk.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;

import java.util.Arrays;

public class SignUtils {

    public static String getSign(String body, String secretKey) {

        String content = body + "." + secretKey;

        return DigestUtil.md5Hex(content);
    }
}
