package com.freelancer.xwatch.utils;

import org.apache.logging.log4j.*;

import com.freelancer.xwatch.utils.strings.StringUtils;

public final class FreelancerLoger {

	private final static Logger LOG = LogManager.getLogger();

	private FreelancerLoger() {
	}

	public final static void info(String message) {
		FreelancerLoger.LOG.info(message);
	}

	public final static void debug(String message) {
		FreelancerLoger.LOG.debug(message);
	}

	public final static void debug(String message, Throwable t) {
		if (LOG.isDebugEnabled()) {
			FreelancerLoger.LOG.debug(StringUtils.isEmpty(message) ? "Unexpected error" : message, t);
		}
	}

	public static void debug(Throwable t) {
		FreelancerLoger.debug(null, t);
	}

	public final static void error(String message) {
		FreelancerLoger.LOG.error(message);
	}

	public final static void error(String message, Throwable t) {
		FreelancerLoger.LOG.error(message, t);
	}

}
