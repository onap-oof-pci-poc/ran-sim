/*-
 * ============LICENSE_START=======================================================
 * Ran Simulator Controller
 * ================================================================================
 * Copyright (C) 2020 Wipro Limited.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

//package org.onap.ransim.websocket.models;
package com.wipro.www.websocket.models;

public class SlicingPmMessage {

	private String sourceName;
	private String fileName;
	private long startEpochMicrosec;
	private long lastEpochMicrosec;
	private String pmData;

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getStartEpochMicrosec() {
		return startEpochMicrosec;
	}

	public void setStartEpochMicrosec(long startEpochMicrosec) {
		this.startEpochMicrosec = startEpochMicrosec;
	}

	public double getLastEpochMicrosec() {
		return lastEpochMicrosec;
	}

	public void setLastEpochMicrosec(long lastEpochMicrosec) {
		this.lastEpochMicrosec = lastEpochMicrosec;
	}
	
	public String getPmData() {
	    return pmData;
	}
	
	public void setPmData(String pmData) {
	    this.pmData = pmData;
	}
	
	public SlicingPmMessage(String sourceName, String fileName, long startEpochMicrosec, long lastEpochMicrosec, String pmData) {
	    this.sourceName = sourceName;
	    this.fileName = fileName;
	    this.startEpochMicrosec = startEpochMicrosec;
	    this.lastEpochMicrosec = lastEpochMicrosec;
	    this.pmData = pmData;
	}
	
	public SlicingPmMessage() {
	    
	}

}
