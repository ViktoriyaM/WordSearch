package com.multithread;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FileSearcher
{
	private String path;
	private String keyword;
	private ConcurrentLinkedQueue<File> queue;
	private static final int NUMBER_THREAD = 100;
	private static final File END_FILE = new File("");
	private static final Logger LOGGER = LogManager.getLogger(FileSearcher.class);
	
	FileSearcher(String path, String keyword)
	{
		this.path = path;
		this.keyword = keyword;
		queue = new ConcurrentLinkedQueue();
	}
	
	void search() throws ExecutionException, InterruptedException
	{
		Thread adder = new InnerAdder();
		adder.start();
		
		Callable searcher = new InnerSearcher();
		FutureTask<Boolean> task = new FutureTask<Boolean>(searcher);
		for (int i = 0; i < NUMBER_THREAD; i++)
		{
			new Thread(task).start();
			if(task.get())
			{
				return;
			}
		}
	}
	
	private void addDirectory(File directory) throws InterruptedException, NullPointerException
	{
		File[] files = directory.listFiles();
		for (File file : files)
		{
			if (file.isDirectory())
			{
				addDirectory(file);
			}
			else
			{
				queue.add(file);
			}
		}
	}
	
	private void searchFile(File file, String keyword) throws FileNotFoundException
	{
		try (Scanner input = new Scanner(file, "KOI8-R"))
		{
			int lineNumber = 0;
			System.out.printf("Searching in file %s%n", file.getPath());
			LoggerManager.loggerInfo(LOGGER, file.getPath());
			while (input.hasNext())
			{
				String line = input.nextLine();
				lineNumber++;
				if (line.contains(keyword))
				{
					System.out.printf("lineNumber %d : %s%n", lineNumber, keyword);
					LoggerManager.loggerInfo(LOGGER, "Done. Line number " + lineNumber);
				}
			}
		}
	}
	
	private class InnerAdder extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				addDirectory(new File(path));
				queue.add(END_FILE);
			}
			catch (InterruptedException | NullPointerException exception)
			{
				System.out.println("Error: incorrect directory");
				LoggerManager.loggerError(LOGGER, exception.toString());
				return;
			}
		}
	}
	
	private class InnerSearcher implements Callable<Boolean>
	{
		@Override
		public Boolean call()
		{
			boolean done = false;
			boolean result = false;
			while (!done)
			{
				try
				{
					File file = queue.poll();
					if (!file.equals(END_FILE))
					{
						searchFile(file, keyword);
					}
					else
					{
						queue.add(END_FILE);
						done = true;
					}
				}
				catch (FileNotFoundException | NullPointerException exception)
				{
					System.out.println("Error: incorrect file name");
					LoggerManager.loggerError(LOGGER, exception.toString());
					result = true;
					break;
				}
			}
			return result;
		}
	}
}
