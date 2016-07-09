/**
 * 
 */
package com.centling.radio.simulator;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.centling.radio.socket.model.PoolParameter;

/**
 * @author lenovo
 *
 */

public class ThreadPool  {
    private ExecutorService executorService = null;
    private final static Logger Log = LoggerFactory.getLogger(Thread.class);
    public ThreadPool() {
	PoolParameter parameter = PoolParameter.getForThreadPoolByCfgFile();
	if (parameter.getMaxConnect() <= 0) {
	    executorService = Executors.newCachedThreadPool();
	} else {
	    if (parameter.getMaxCatch() <= 0) {
		executorService = new ThreadPoolExecutor(parameter.getCoreSize(), parameter.getMaxConnect(),
			parameter.getTimeOut(), TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
			Executors.defaultThreadFactory(), new RejectedExecutionHandler() {
			    @Override
			    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				int maximumPoolSize = executor.getMaximumPoolSize();
				executor.setMaximumPoolSize(maximumPoolSize + 1);
				executor.submit(r);
			    }
			});
	    } else {
		ArrayBlockingQueue<Runnable> socketAdaptes = new ArrayBlockingQueue<Runnable>(parameter.getMaxCatch());
		executorService = new ThreadPoolExecutor(parameter.getCoreSize(), parameter.getMaxConnect(),
			parameter.getTimeOut(), TimeUnit.MILLISECONDS, socketAdaptes, Executors.defaultThreadFactory(),
			new RejectedExecutionHandler() {
			    @Override
			    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				int maximumPoolSize = executor.getMaximumPoolSize();
				executor.setMaximumPoolSize(maximumPoolSize + 1);
				executor.submit(r);
			    }
			});
	    }

	}
	Log.info("ThreadPoolService :: 初始化成功。");
    }
    
    public void submitTarget(Runnable target) {
	executorService.submit(target);
    }
}
