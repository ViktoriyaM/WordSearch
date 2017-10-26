package com.multithread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InputManager
{
	private String path;
	private String keyword;
	private static final Logger LOGGER = LogManager.getLogger(InputManager.class);
	
	void inputData()
	{
		try (Scanner scanner = new Scanner(System.in))
		{
			System.out.println("Enter base directory: ");
			
			path = scanner.nextLine();
			
			System.out.println("Enter the keyword: ");
			
			keyword = scanner.nextLine();
			
			LoggerManager.loggerInfo(LOGGER, "Entered base directory: " + path);
			LoggerManager.loggerInfo(LOGGER, "Entered the keyword: " + keyword);
		}
	}
	
	String getPath()
	{
		return path;
	}
	
	String getKeyword()
	{
		return keyword;
	}
}
