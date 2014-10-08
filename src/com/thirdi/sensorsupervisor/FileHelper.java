package com.thirdi.sensorsupervisor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author htkibar CHECK LATER, WITH A DARN CABLE.
 */
public class FileHelper {

	public FileHelper() {
		
	}

	public Boolean write(String fname, String fcontent) {
		try {
			String fpath = "/sdcard/" + fname + ".txt";
			File file = new File(fpath);
			// If file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(fcontent);
			bw.close();
			Log.d("Suceess", "Sucess");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isExtStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}

		return false;
	}

	public boolean isExtStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}

		return false;
	}
}
