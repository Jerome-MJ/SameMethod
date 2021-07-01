package com.mj93.samemethod.util

import java.util.*

/**
 * @Title:
 * @Description: (用一句话描述该文件做什么)
 * @author liumengjie
 * @date 2021/6/30 15:27
 * @version V1.0
 */
internal object StrUtils {
    /**
     *
     * @param str1
     * @param str2
     */
    fun levenshtein(str1: String, str2: String):Float {
        //计算两个字符串的长度。
        val len1 = str1.length
        val len2 = str2.length
        //建立上面说的数组，比字符长度大一个空间
        val dif = Array(len1 + 1) {
            IntArray(
                len2 + 1
            )
        }
        //赋初值，步骤B。
        for (a in 0..len1) {
            dif[a][0] = a
        }
        for (a in 0..len2) {
            dif[0][a] = a
        }
        //计算两个字符是否一样，计算左上的值
        var temp: Int
        for (i in 1..len1) {
            for (j in 1..len2) {
                temp = if (str1[i - 1] == str2[j - 1]) {
                    0
                } else {
                    1
                }
                //取三个值中最小的
                dif[i][j] = min(
                    dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                    dif[i - 1][j] + 1
                )
            }
        }
        //计算相似度
        val similarity = 1 - dif[len1][len2].toFloat() / Math.max(str1.length, str2.length)
        return similarity
    }

    //得到最小值
    fun min(vararg `is`: Int): Int {
        var min = Int.MAX_VALUE
        for (i in `is`) {
            if (min > i) {
                min = i
            }
        }
        return min
    }

    /*
     * 计算两个字符串(英文字符)的相似度，简单的余弦计算，未添权重
     */



    fun AnotherCompare(string1: String, string2: String):Double {
        var vectorMap: MutableMap<Char, IntArray> = HashMap()
        var tempArray: IntArray? = null
        for (character1 in string1.toCharArray()) {  //拆解为向量
            if (vectorMap.containsKey(character1)) {   //该字符是否已经存在map中
                vectorMap[character1]!![0]++ //key对应的值是一个数组，第一个字符串的字符出现次数存储在数组第一个位置
            } else {
                tempArray = IntArray(2)
                tempArray!![0] = 1 //如果字符没有出现过，就添加到map中，数组第一个位置设为1
                tempArray!![1] = 0
                vectorMap[character1] = tempArray!!
            }
        }
        for (character2 in string2.toCharArray()) {  //拆解为向量
            if (vectorMap.containsKey(character2)) {
                vectorMap[character2]!![1]++
            } else {
                tempArray = IntArray(2)
                tempArray!![0] = 0
                tempArray!![1] = 1 //第二个字符串的字符出现次数存储在数组的第二个位置
                vectorMap[character2] = tempArray!!
            }
        }
        /*
             * 到最后，map里的key对应的数组，第一个位置存储的是第一个字符串字符对应的向量，第二个位置存储的是第二个字符串字符对应的向量。
             */
        // 求余弦相似度
       return pointMulti(vectorMap) / sqrtMulti(vectorMap)

    }



    //求平方根
    private fun sqrtMulti(paramMap: Map<Char, IntArray>): Double {
        var result = 0.0
        result = squares(paramMap) //先求平方和
        result = Math.sqrt(result) //再开根号，就是求模
        return result
    }

    // 求平方和，分母上，向量求模的平方
    private fun squares(paramMap: Map<Char, IntArray>): Double {
        var result1 = 0.0
        var result2 = 0.0
        val keySet = paramMap.keys
        for (character in keySet) {
            val temp = paramMap[character] //获取key对应的值--数组
            result1 += (temp!![0] * temp[0]).toDouble() //temp[0]存储的是第一个字符串对应的向量
            result2 += (temp[1] * temp[1]).toDouble() //temp[1]存储的是第二个字符串对应的向量
        }
        return result1 * result2
    }

    // 点乘法，在分子上，向量相乘
    private fun pointMulti(paramMap: Map<Char, IntArray>): Double {
        var result = 0.0
        val keySet = paramMap.keys //返回map中所有key值的列表，这里的set，也可以用list代替吧
        for (character in keySet) {    //存储的key值都是不重复的
            val temp = paramMap[character] //获取key对应的值
            result += (temp!![0] * temp[1]).toDouble()
        }
        return result
    }


}