package com.liebe.base_comm.util

import android.text.TextUtils
import com.dating.app.lib.util.PreferenceUtil
import com.liebe.base_comm.AppSetting

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description loginUtil
 * @Author LiangZe
 * @Date 2020/4/26
 * @Version 2.0
 */

fun saveManager(userId: String, userName: String, password: String) {
    //添加管理员信息，用户名密码组成键值对
    PreferenceUtil.setString(userName, password)
    //存储当前管理员Id
    PreferenceUtil.setString(AppSetting.manager_id, userId)
    //存储当前管理员名称
    PreferenceUtil.setString(AppSetting.manager_name, userName)
}

fun getManagerName(): String {
    return PreferenceUtil.getString(AppSetting.manager_name)
}

fun getManagerId(): String {
    return PreferenceUtil.getString(AppSetting.manager_id)
}

fun isManager(userName: String, password: String): Boolean {
    return !TextUtils.isEmpty(PreferenceUtil.getString(userName)) && password.equals(
        PreferenceUtil.getString(
            userName
        )
    )
}

fun loginApp() {
    PreferenceUtil.setBoolean(AppSetting.user_login, true)
}

fun loginOutApp() {
    PreferenceUtil.setBoolean(AppSetting.user_login, false)
}

fun isLogin(): Boolean {
    return PreferenceUtil.getBoolean(AppSetting.user_login)
}

fun saveToken(token: String) {
    PreferenceUtil.setString(AppSetting.token, token)
}

fun removeToken() {
    PreferenceUtil.remove(AppSetting.token)
}

fun getToken(): String {
    return PreferenceUtil.getString(AppSetting.token, AppSetting.defalut_token)
}

fun saveSiteId(siteId: Int) {
    PreferenceUtil.setInt(AppSetting.siteId, siteId)
}

fun getSiteId(): Int? {
    return PreferenceUtil.getInt(AppSetting.siteId)
}

fun isOpenVoice(): Boolean {
    return PreferenceUtil.getBoolean(AppSetting.user_play_music, true)
}

fun isOpenPayLimit(): Boolean {
    return PreferenceUtil.getBoolean(AppSetting.open_pay_limit, true)
}

fun isOpenChopsticksMachine(): Boolean {
    return PreferenceUtil.getBoolean(AppSetting.chopsticksMachine, false)
}

fun getUserPayInterval(): Long {
    return PreferenceUtil.getLong(AppSetting.userPayInterval, AppSetting.userPayIntervalDefalut)
}

fun saveAdType(fileName: String, type: Int) {
    PreferenceUtil.setInt(fileName, type)
}

fun getAdType(fileName: String): Int {
    return PreferenceUtil.getInt(fileName)
}


fun hasActiveFace(): Boolean {
    return PreferenceUtil.getBoolean(AppSetting.activeFace, false)
}

fun isFirstLogin(): Boolean {
    return PreferenceUtil.getBoolean(AppSetting.isFirstLogin, true)
}

fun isShowPayAndBanlance(): Boolean {
    return PreferenceUtil.getBoolean(AppSetting.showPayAndBanlance, true)
}

fun getConsumptionSetting(): Int {
    return PreferenceUtil.getInt(AppSetting.consumptionSetting, 0)
}

fun setConsumptionSetting(type: Int) {
    PreferenceUtil.setInt(AppSetting.consumptionSetting, type)
}

fun getEatCount(): String {
    return PreferenceUtil.getString(AppSetting.eatCount, "")
}

fun saveEatCount(count: String) {
    PreferenceUtil.setString(AppSetting.eatCount, count)
}

fun getActiveTime(): String {
    return PreferenceUtil.getString(AppSetting.activeTime, "")
}

//保存人脸识别距离
fun setFaceDistance(distance: Int) {
    PreferenceUtil.setInt(AppSetting.distanceFace, distance)
}

//获取本地人脸识别数据，默认1-近。 其他的  2-中，3-远
fun getFaceDistance(): Int {
    return PreferenceUtil.getInt(AppSetting.distanceFace, 1)
}

//设置是否活体检测
fun setLiveness(isLive: Boolean) {
    PreferenceUtil.setBoolean(AppSetting.isLiveness, isLive)
}

fun getLiveness(): Boolean {
    return PreferenceUtil.getBoolean(AppSetting.isLiveness, true)//默认活体检测打开
}