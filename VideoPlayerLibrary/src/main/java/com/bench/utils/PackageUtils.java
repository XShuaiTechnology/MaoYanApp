package com.bench.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

public final class PackageUtils {
	private static final String TAG = "PackageUtils";

	/**
	 * 查询手机内非系统应用
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getAllApps(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
		//获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
			//判断是否为非系统预装的应用程序
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				// customs applications
				apps.add(pak);
			}
		}
		return apps;
	}

	/**
	 * 查询手机内非系统应用
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getSystemApps(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
		//获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
			//判断是否为非系统预装的应用程序
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// customs applications
				apps.add(pak);
			}
		}
		return apps;
	}

	/**
	 * 获取ApplicationInfo对象
	 */
	public static PackageInfo getPackageInfo(Context context, String packageName) {
		PackageInfo info = null;
		try {
			PackageManager pManager = context.getPackageManager();
			//获取手机内所有应用
			List<PackageInfo> paklist = pManager.getInstalledPackages(0);
			for (int i = 0; i < paklist.size(); i++) {
				PackageInfo pak = (PackageInfo) paklist.get(i);
				if (packageName.equals(pak.packageName)) {
					info = pak;
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return info;
	}

	/**
	 * 根据包名获取具体路径
	 */
	public static String getSysAppSourceDir(Context context, String packageName) throws Exception {
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo info = packageManager.getApplicationInfo(packageName, ApplicationInfo.FLAG_SYSTEM);
		return info.sourceDir;
	}

	/**
	 * 根据包名获取具体路径
	 */
	public static List<String> getSysAppSourceDirs(Context context, String packageName) throws Exception {
		List<String> dirs = new ArrayList<String>();
		PackageManager pManager = context.getPackageManager();
		//获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (PackageInfo info : paklist) {
			//判断是否为非系统预装的应用程序
			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				if (info.applicationInfo.packageName.equals(packageName)) {
					dirs.add(info.applicationInfo.sourceDir);
				}
			}
		}
		return dirs;
	}

	/**
	 * 获取正在运行的进程。会添加到processes列表中。
	 * 
	 * @param context
	 *            上下文
	 * @param processes
	 *            保存进程信息，获取的进程会添加到此List中
	 */
	public static boolean isExist(Context context, String packageName) {
		boolean flag = false;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//获取当前运行的所有进程
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		if (runningAppProcesses == null || runningAppProcesses.size() == 0)
			return false;
		doubleFor: for (RunningAppProcessInfo rapi : runningAppProcesses) {
			for (String pkgName : rapi.pkgList) {
				if (pkgName.equals(packageName)) {
					flag = true;
					//跳出双重循环
					break doubleFor;
				}
			}
		}
		return flag;
	}

	/**
	 * 用来判断服务是否运行.
	 * @param context
	 * @param className 判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(50);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * install package normal by system intent
	 * 
	 * @param context
	 * @param filePath
	 *            file path of package
	 * @return whether apk exist
	 */
	public static void installNormal(Context context, String filePath) {
		Log.d(TAG, "begin update...");
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		Log.d(TAG, "end update...");
	}

	/**
	 * install slient
	 * 
	 * @param context
	 * @param filePath
	 * @return 0 means normal, 1 means file not exist, 2 means other exception
	 *         error
	 * 需要系统签名
	 */
	public static int installSlient(String filePath) {
		File file = new File(filePath);
		if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
			return 1;
		}
		String[] args = { "pm", "install", "-r", filePath };
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder errorMsg = new StringBuilder();
		int result;
		try {
			process = processBuilder.start();
			successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
			errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String s;

			while ((s = successResult.readLine()) != null) {
				successMsg.append(s);
			}
			while ((s = errorResult.readLine()) != null) {
				errorMsg.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = 2;
		} finally {
			try {
				if (successResult != null) {
					successResult.close();
				}
				if (errorResult != null) {
					errorResult.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}

		if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
			Log.d(TAG, successMsg.toString());
			result = 0;
		} else {
			result = 2;
		}
		Log.d(TAG + "--installSlient", "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg + ",result=" + result);
		return result;
	}

	/**
	 * install slient
	 * 
	 * @param context
	 * @param filePath
	 * @return 0 means normal, 1 means file not exist, 2 means other exception
	 *         error
	 */
	public static void reboot() {
		String[] args = { "reboot" };
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder errorMsg = new StringBuilder();
		try {
			process = processBuilder.start();
			successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
			errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String s;

			while ((s = successResult.readLine()) != null) {
				successMsg.append(s);
			}
			while ((s = errorResult.readLine()) != null) {
				errorMsg.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (successResult != null) {
					successResult.close();
				}
				if (errorResult != null) {
					errorResult.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		Log.d(TAG + "--reboot", "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
	}

	public static void setPermission(String filePath, String rwx) {
		try {
			Log.d(TAG + "--setPermission", "设置文件权限.....");
			String[] command = { "chmod", rwx, filePath };
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void setPermissionForPath(String path, String rwx) {
		try {
			Log.e(TAG + "--setPermissionForPath", "设置文件夹权限.....");
			String[] command = { "chmod -R ", rwx, path };
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	* 判断当前Activity是否位于前台
	* @param context
	* @return
	*/
	public static boolean isForeGround(Context context) {
		boolean flag = false;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					flag = true;
				} else {
					flag = false;
				}
				break;
			}
		}
		return flag;
	}

	/**
	 * 获取versionCode
	 * 
	 * @param pkgname
	 * @return
	 */
	public static Integer getVersionCode(Context context, String pkgName) {
		// 获取packagemanager的实
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名,代表是获取版本信息
		PackageInfo packInfo;
		int version = 0;
		try {
			packInfo = packageManager.getPackageInfo(pkgName, 0);
			version = packInfo.versionCode;
		} catch (NameNotFoundException e) {
		}
		return version;
	}

}
