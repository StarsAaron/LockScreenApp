package com.aaron.lockscreenapp

import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

/**
 * 打开权限和卸载功能
 * 因为开启权限之后不能按照平常卸载方式，需要先清除权限才可以卸载
 * @author Aaron
 */
class MainActivity : AppCompatActivity() {
    private var dpm: DevicePolicyManager? = null //设备策略服务
    private var mDeviceAdmin: ComponentName? = null
    private var btnOpen:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dpm = getSystemService(Service.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
        mDeviceAdmin = ComponentName(this, AdminManageReceiver::class.java)
// 如果要使用应用图标作为开关快捷可以使用这段代码，但是就不可以点击卸载按钮了
//        dpm?.let {
//            if (it.isAdminActive(mDeviceAdmin)) {
//                it.lockNow()
//                finish()
//                android.os.Process.killProcess(android.os.Process.myPid())
//            }else{
//                setContentView(R.layout.activity_main)
//                val btnUninstall = findViewById<Button>(R.id.btn_uninstall)
//                btnUninstall.setOnClickListener { uninstall(null) }
//            }
//        }
        setContentView(R.layout.activity_main)

        btnOpen = findViewById<Button>(R.id.btn_open)
        btnOpen?.setOnClickListener { getAdminControl() }

        val btnUninstall = findViewById<Button>(R.id.btn_uninstall)
        btnUninstall.setOnClickListener { uninstall(null) }

        // 修改按钮显示文字
        dpm?.let {
            if (it.isAdminActive(mDeviceAdmin)) {
                btnOpen?.text = "已开启权限"
            } else {
                btnOpen?.text = "开启权限"
            }
        }
    }

    /**
     * 激活权限
     */
    fun getAdminControl() {
        dpm?.let {
            if (it.isAdminActive(mDeviceAdmin)) {
                Toast.makeText(this, "已经激活设备管理权限", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(
                        DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                        mDeviceAdmin)
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "需要激活设备管理器，才能使用锁屏功能")
                startActivity(intent)
            }
        }
    }

    /**
     * 卸载当前软件
     */
    private fun uninstall(view: View?) {
        //1.先清除管理员权限
        dpm?.let {
            if (it.isAdminActive(mDeviceAdmin)) {
                it.removeActiveAdmin(mDeviceAdmin)
            }
        }
        //2.普通应用的卸载
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.addCategory("android.intent.category.DEFAULT")
        intent.data = Uri.parse("package:" + packageName)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // 修改按钮显示文字
        dpm?.let {
            if (it.isAdminActive(mDeviceAdmin)) {
                btnOpen?.text = "已开启权限"
            } else {
                btnOpen?.text = "开启权限"
            }
        }
    }
}
