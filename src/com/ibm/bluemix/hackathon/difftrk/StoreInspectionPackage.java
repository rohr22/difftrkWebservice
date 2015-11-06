package com.ibm.bluemix.hackathon.difftrk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cloudant.client.api.CloudantClient;
import com.google.gson.JsonObject;

public class StoreInspectionPackage {
	/*
	 * Take in a track # and a file name and store the file contents into the
	 * Cloudant db using the track number and file name as the key.
	 * 
	 * Chad will send me one file at a time.
	 */

	public static void main(String[] args) {
		System.out.println("Hi, world. Here I am!");

		for (String arg : args) {
			System.out.println(arg);
		}

		if (args.length != 2) {
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			StackTraceElement main = stack[stack.length - 1];
			String mainClass = main.getClassName();

			System.out.println("Error: you must specify two arguments.");
			System.out.println("usage: " + mainClass + " trackNumber fileName");
			System.exit(1);
		}

		StoreInspectionPackage app = new StoreInspectionPackage();
		try {
			boolean storedFile = app.storeFile(args[0], args[1]);
			if (storedFile) {
				System.out.println("The file contents have been stored!");
			} else {
				System.out.println("Error storing the file contents.");
			}
		} catch (Exception err) {
			System.out.println("Error storing the file contents. The error was:");
			err.printStackTrace();
		}
		

		
	}

	public boolean storeFile(String trackNumber, String fileName) throws IOException {

		boolean result = true;

		JsonObject difftrkObj = new JsonObject();
		

		difftrkObj.addProperty("id", trackNumber + "|" + fileName);
		System.out.println(difftrkObj);

		/*
		 * Read file contents from filename and put into a string and store this
		 * as a "contents" property in the Json object.
		 */

		File difftrkFile = new File(fileName);
		if (difftrkFile.exists()) {
			if (difftrkFile.isDirectory()) {
				System.out.println("Error: file " + fileName + " is a directory. It should be a file.");
				return false;
			}

			Path path = Paths.get(fileName);
			try {
				byte[] fileByteArray = Files.readAllBytes(path);
				String fileContentsAsString = new String(fileByteArray, "US-ASCII");
				System.out.println("The files contents are:");
				System.out.println(fileContentsAsString);

				difftrkObj.addProperty("contents", fileContentsAsString);
				
				/* From information sent by Phong */
				String url = new String("https://61fc7816-dbdf-46ca-80f8-6603ecd9f9f1-bluemix:fc22f227b14bb7f2c087ad178a6849aad313c51f3bbccb5eab886d644300786a@61fc7816-dbdf-46ca-80f8-6603ecd9f9f1-bluemix.cloudant.com");
				String username = new String("61fc7816-dbdf-46ca-80f8-6603ecd9f9f1-bluemix");
				String password = new String("fc22f227b14bb7f2c087ad178a6849aad313c51f3bbccb5eab886d644300786a");
				
				CloudantClient client = new CloudantClient(url, username, password);
				
				/* client.get */

			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
		} else {
			System.out.println("Error: file " + fileName + " does not exist.");
			result = false;
		}

		return result;
	}
}
