package com.aaron.lockscreenapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.widget.Toast

/**
 * 锁屏服务
 */
class LockService : Service() {
    private var dpm: DevicePolicyManager? = null //设备策略服务

    override fun onCreate() {
        super.onCreate()
        dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager?
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lockscreen()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * 一键锁屏
     */
    private fun lockscreen() {
        val who = ComponentName(this, AdminManageReceiver::class.java)
        if (dpm?.isAdminActive(who)!!) {
            dpm?.lockNow()//锁屏
//            dpm?.resetPassword("", 0)//设置屏蔽密码
            //清除Sdcard上的数据
            // dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
            //恢复出厂设置
            // dpm.wipeData(0);
        } else {
//            openAdmin()
            Toast.makeText(this, "还没有打开管理员权限", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}
