package top.mj93.samemethod

import top.mj93.samemethod.bean.SameFuncBean

/**
 * @Title:
 * @Description: (用一句话描述该文件做什么)
 * @author liumengjie
 * @date 2021/6/30 15:59
 * @version V1.0
 */
internal object Const {
    var samesMap = mutableMapOf<String, MutableList<String>>()
    var samesList = mutableListOf<SameFuncBean>()
    var appExt: AppExt? = null
    var COMPARE_COUNT=0
}