package com.discord.utilities;

import java.util.Random;
import java.util.ArrayList;

public class HelperFuncs {

  public static <T> T random_choice(ArrayList<T> list) {
    return list.get(new Random().nextInt(list.size() - 1));
	}

  public static String random_choice(String[] arr) {
		return arr[new Random().nextInt(arr.length-1)];
	}

  public static boolean hasValue(ArrayList<String> list, String value) {
		for (String str : list)
			if (str.equals(value))
				return true;

		return false;

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
