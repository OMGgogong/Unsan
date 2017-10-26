package com.unsan.core.utils;

public class SystemUtils {
public static final boolean isWindowSystem = System.getProperty("os.name").toLowerCase().startsWith("win");
public static void main(String[] args) {
	System.out.println(isWindowSystem);
}
}
