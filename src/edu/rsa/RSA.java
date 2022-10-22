package edu.rsa;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RSA {

    /**
     * 读入明文文件
     * <p>
     * 编码方式: 每次读入两个 byte, 如 a, b
     * ascii 码为 97 98
     * 将其组合为 097098 的 最高为 6 位的十进制数
     * 转化为 BigInteger, 存入数组
     * 如果一次没读满两个 byte, 用 000 补足
     *
     * @param inName 明文文件路径
     * @return mByteList 编码后的明文编码序列
     */
    private static List<BigInteger> readPlain(String inName) throws IOException {
        FileInputStream fis = new FileInputStream(inName);

        int by, byPre;
        BigInteger temp;
        List<BigInteger> mByteList = new ArrayList<>();

        do {
            // 这里只能这么读
            by = fis.read();
            byPre = fis.read();

            if ((by != -1) && (byPre != -1)) {
                // 正常两个
                temp = BigInteger.valueOf(by * 1_000 + byPre);
                mByteList.add(temp);
            } else if (by != -1) {
                // 只有一个, 由于 else if 就不需要判断 byPre
                temp = BigInteger.valueOf(by * 1_000);
                mByteList.add(temp);
                break;
            } else {
                break;
            }
        } while (true);

        // 释放系统资源
        fis.close();

        return mByteList;
    }

    /**
     * 加密过程
     *
     * @param rsaUtils  RSA 密钥
     * @param mByteList 编码后的明文编码序列
     * @return cByteList 密文编码序列
     */
    private static List<BigInteger> enCrypt(RSAUtils rsaUtils, List<BigInteger> mByteList) {
        // 对分组逐个加密
        List<BigInteger> cByteList = new ArrayList<>();
        for (BigInteger byte2 : mByteList)
            cByteList.add(rsaUtils.en(byte2));

        return cByteList;
    }

    /**
     * 写入密文文件
     * <p>
     * 密文分组长度 <= nLength, 且长度不一
     * 全部补足至 nLength 长度, 填入文件
     * 对于长度不足的情况, 要前面补 0
     *
     * @param cByteList 编码后的明文编码序列
     * @param nLength   密文分组长度
     * @param deName    明文文件路径
     */
    private static void writeCrypto(List<BigInteger> cByteList, int nLength, String deName) throws IOException {
        FileOutputStream fos = new FileOutputStream(deName);

        for (BigInteger byte2 : cByteList) {
            int len = byte2.toString().length(); // 需要的是字节数
            String str = "0".repeat(Math.max(0, nLength - len)) + byte2; // 前面补 0
            fos.write(str.getBytes());
        }

        // 释放系统资源
        fos.close();
    }

    /**
     * 读入密文文件
     * <p>
     * 读入的密文文件的字节长度是标准的 nLength 的倍数
     * 一次读入 nLength 长度的字节
     *
     * @param nLength 密文分组长度
     * @param enName  密文文件路径
     * @return cByteList 密文编码序列
     */
    private static List<BigInteger> readCrypto(int nLength, String enName) throws IOException {
        FileInputStream fis = new FileInputStream(enName);

        List<BigInteger> cByteList = new ArrayList<>();

        // 一次读入 nLength 长度的 byte
        byte[] tempData = new byte[nLength];
        while (fis.read(tempData) != -1)
            // 这种 BigInteger 构造不允许有空格等 blanks, 此处可以满足
            // 利用 String 去掉前导 0
            cByteList.add(new BigInteger(new String(tempData)));

        // 释放系统资源
        fis.close();

        return cByteList;
    }

    /**
     * 解密过程
     *
     * @param rsaUtils  RSA 密钥
     * @param cByteList 密文编码序列
     * @return dByteList 解码得到的明文编码序列
     */
    private static List<BigInteger> deCrypt(RSAUtils rsaUtils, List<BigInteger> cByteList) {
        // 对分组逐个解密
        List<BigInteger> dByteList = new ArrayList<>();
        for (BigInteger byte2 : cByteList)
            dByteList.add(rsaUtils.de(byte2));
        return dByteList;
    }

    /**
     * 写入解密文件
     * <p>
     * 按照之前读入的编码规则解码
     *
     * @param dByteList 解码得到的明文编码序列
     * @param deName    解码明文文件路径
     */
    private static void writePlain(List<BigInteger> dByteList, String deName) throws IOException {
        FileOutputStream fos = new FileOutputStream(deName);

        BigInteger by, byNext;
        BigInteger k1 = BigInteger.valueOf(1_000);
        for (BigInteger byte2 : dByteList) {
            by = byte2.divide(k1);
            byNext = byte2.mod(k1);
            fos.write(by.intValue());
            if (byNext.intValue() != 0) {
                // 把 0 加进去会有奇怪的字符
                fos.write(byNext.intValue());
            }
        }

        // 释放系统资源
        fos.close();
    }

    public static void main(String[] args) throws IOException {

        /*
         * in 明文文件路径
         * en 密文文件路径
         * de 解密文件路径
         */
        String in = "data/inData/lab2-Plaintext.txt";
        String en = "data/outData/lab2-Encrypt-text.txt";
        String de = "data/outData/lab2-Decrypt-text.txt";

        /*
         * bitLength 密钥长度设置参数
         */
        int bitLength = 10;

        /*
         * 调用工具生成密钥
         * 打印 RSA 关键参数
         */
        RSAUtils rsaUtils = new RSAUtils(bitLength);
        rsaUtils.printAll();

        /*
         * mByteList 编码后的明文编码序列
         * cByteList 密文编码序列
         * dByteList 解码得到的明文编码序列
         */
        List<BigInteger> mByteList;
        List<BigInteger> cByteList;
        List<BigInteger> dByteList;

        // 加密
        {
            mByteList = readPlain(in); // 读入明文, 得到序列
            cByteList = enCrypt(rsaUtils, mByteList); // 加密
            writeCrypto(cByteList, rsaUtils.getN().bitLength(), en); // 写入密文文件
        }

        // 解密
        {
            cByteList = readCrypto(rsaUtils.getN().bitLength(), en); // 读入密文, 得到序列
            dByteList = deCrypt(rsaUtils, cByteList); // 解密
            writePlain(dByteList, de); // 写入解密文件
        }

    }
}
