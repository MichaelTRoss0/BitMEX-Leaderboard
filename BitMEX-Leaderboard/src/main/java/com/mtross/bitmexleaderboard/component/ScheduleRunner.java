package com.mtross.bitmexleaderboard.component;

import java.io.IOException;
import java.net.ProtocolException;

/**
 *
 * @author mike
 */
public interface ScheduleRunner {

	public void scheduledDataCollection() throws ProtocolException, IOException;

}
