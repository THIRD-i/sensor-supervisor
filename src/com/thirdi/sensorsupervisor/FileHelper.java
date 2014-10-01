package com.thirdi.sensorsupervisor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
/**
 * 
 * @author htkibar
 * CHECK LATER, WITH A DARN CABLE.
 */
public class FileHelper {
	File file;
	FileWriter fw;
	BufferedWriter bw;
	Context context;
	
	final String filename = "sensor_data.jpg";
	
	public FileHelper(Context c) {
		context = c;
		file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
				.getAbsolutePath(), filename);
		if (!file.mkdirs()) {
			Toast.makeText(context, "sensorsupervisor.err.dirnotcreated", Toast.LENGTH_LONG).show();
		}
		final String time = System.currentTimeMillis() + "\n";
		try {
			fw =  new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(time);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
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
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		
		return false;
	}
}
