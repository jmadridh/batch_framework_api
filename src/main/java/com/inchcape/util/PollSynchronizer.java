/**
 * Inchcape PLC.
 * All rights reserverd.  http://www.inchcape.com
 */

package com.inchcape.util;

import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * This class objective is to create a critical area around the application in
 * order to stop what ever threads access the area if other is in it.
 * 
 * A semaphore initialized to one, and which is used such that it only has at
 * most one permit available, can serve as a mutual exclusion lock. This is more
 * commonly known as a binary semaphore, because it only has two states: one
 * permit available, or zero permits available. When used in this way, the
 * binary semaphore has the property (unlike many Lock implementations), that
 * the "lock" can be released by a thread other than the owner (as semaphores
 * have no notion of ownership).
 * 
 */
public class PollSynchronizer {

	private static final int MAX_PERMITS_NUMBER = 1;
	private static final Logger log = LogManager.getLogger(PollSynchronizer.class);
	
	private Semaphore semaphore;

	public void lock() {
		try {
			getLock().acquire();
		} catch (InterruptedException e) {
			log.error(e.getCause(), e);
		}
	}
	
	public void unlock() {
		getLock().release();
	}

	public Semaphore getLock() {
		if (semaphore == null) {
			this.semaphore = new Semaphore(MAX_PERMITS_NUMBER);
		}
		
		return this.semaphore;
	}

}