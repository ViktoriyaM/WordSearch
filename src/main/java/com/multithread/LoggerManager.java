package com.multithread;

import org.apache.logging.log4j.Logger;

public class LoggerManager
{
	static void loggerError(final Logger LOGGER, String exception)
	{
		LOGGER.error(exception);
	}
	
	static void loggerInfo(final Logger LOGGER, String exception)
	{
		LOGGER.info(exception);
	}
}
