package com.mj93.samefuncplugin

import com.mj93.samefuncplugin.bean.SameFuncBean

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
}