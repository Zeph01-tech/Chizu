package com.discord.utilities;

import java.util.Random;

public class Utils {
  public static String random_choice(String[] arr) {
		return arr[new Random().nextInt(arr.length-1)];
	}

	public static int random_choice(int[] arr) {
		return arr[new Random().nextInt(arr.length-1)];
	}
  
  public static boolean hasValue(String[] arr, String value) {
		for (String str : arr)
			if (str.equals(value))
				return true;

		return false;
	}

	public static boolean hasValueIgnoreCase(String[] arr, String value) {
		for (String str : arr)
			if (str.equalsIgnoreCase(value))
				return true;

		return false;
	}

	public static String mergearr(String[] arr) {
		String out = "";
		for (String str : arr)
			out += str + " ";

		return out;
	}
}
