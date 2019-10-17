package com.yly.remotebinder

import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yly.comm.Common
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_test_plugin.*
import java.io.File


class PluginTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_plugin)
        testPlugin.setOnClickListener {
            val dexOutputDir = getDir("dex", 0);
            val libPath = filesDir.absolutePath + File.separator + "plugin-debug.apk";
            val loader = DexClassLoader(
                libPath,
                dexOutputDir.getAbsolutePath(),
                null,
                classLoader
            )
            val clazz = loader.loadClass("com.yly.plugin.PluginImpl")
            val localConstructor = clazz.getConstructor()
            val mPluginInterface = localConstructor.newInstance() as Common
            println(mPluginInterface.test(10, 4))
        }


        testResources.setOnClickListener {
            val apkPath = filesDir.absolutePath + File.separator + "plugin-debug.apk";
            val packageArchiveInfo = packageManager.getPackageArchiveInfo(apkPath, GET_META_DATA)
            packageArchiveInfo?.applicationInfo?.apply {
                publicSourceDir = apkPath
                sourceDir = apkPath
                try {
                    val newResources = packageManager.getResourcesForApplication(this)
//                    val assetManager = AssetManager::class.java.newInstance()
//                    val addAssetPath =
//                        assetManager.javaClass.getMethod("addAssetPath", String::class.java)
//                    addAssetPath.invoke(assetManager, apkPath)
//                    val newResources =
//                        Resources(assetManager, resources.displayMetrics, resources.configuration)
                    val drawable =
                        newResources.getIdentifier("lufly", "drawable", "com.yly.plugin")
                    testImg.setImageDrawable(newResources.getDrawable(drawable, getTheme()))
                } catch (e: PackageManager.NameNotFoundException) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}