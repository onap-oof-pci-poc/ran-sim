/*-
 * ============LICENSE_START=======================================================
 * Ran Simulator Controller
 * ================================================================================
 * Copyright (C) 2018 Wipro Limited.
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

package org.onap.ransim.rest.api.models;

public class FmAlarmInfo {
    
    private String problem;
    private String collisionCount;
    private String confusionCount;
    
    public FmAlarmInfo() {
        super();
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param problem
     *            Collision or Confusion
     * @param collisionCount
     *            No of cells that causes collision.
     * @param confusionCount
     *            No od cells that causes confusion.
     */
    public FmAlarmInfo(String problem, String collisionCount, String confusionCount) {
        super();
        this.problem = problem;
        this.collisionCount = collisionCount;
        this.confusionCount = confusionCount;
    }
    
    public String getProblem() {
        return problem;
    }
    
    public void setProblem(String problem) {
        this.problem = problem;
    }
    
    public String getCollisionCount() {
        return collisionCount;
    }
    
    public void setCollisionCount(String collisionCount) {
        this.collisionCount = collisionCount;
    }
    
    public String getConfusionCount() {
        return confusionCount;
    }
    
    public void setConfusionCount(String confusionCount) {
        this.confusionCount = confusionCount;
    }
    
}
