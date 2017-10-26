package com.multithread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutionException;

public class Controller
{
	private static final Logger LOGGER = LogManager.getLogger(Controller.class);
	
	void control()
	{
		InputManager inputManager = new InputManager();
		inputManager.inputData();
		
		String path = inputManager.getPath();
		String keyword = inputManager.getKeyword();
		
		FileSearcher fileSearcher = new FileSearcher(path, keyword);
		try
		{
			fileSearcher.search();
		}
		catch (ExecutionException | InterruptedException exception)
		{
			LoggerManager.loggerError(LOGGER, exception.toString());
		}
	}
}
