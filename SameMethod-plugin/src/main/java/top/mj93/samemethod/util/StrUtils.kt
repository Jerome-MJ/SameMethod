package top.mj93.samemethod.util


/**
 * @Title:
 * @Description: (用一句话描述该文件做什么)
 * @author liumengjie
 * @date 2021/6/30 15:27
 * @version V1.0
 */
internal object StrUtils {

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
        val split1 = string1.split("\n")
        val split2 = string2.split("\n")
        var sameCount = 0
        for (s in split1){
            for(s2 in split2){
                if(s == s2){
                    sameCount+=1
                    break
                }
            }
        }
        return sameCount.toDouble()/ kotlin.math.min(split1.size, split2.size)
    }


}