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

import java.util.List;

public class TopologyDump {

    private List<CellData> cellList;

    /**
     * A constructor for TopologyDump.
     *
     * @param cellList
     *            list of cells
     */
    public TopologyDump(List<CellData> cellList) {

        this.cellList = cellList;
    }

    public TopologyDump() {

    }

    public List<CellData> getCellList() {
        return cellList;
    }

    public void setCellList(List<CellData> cellList) {
        this.cellList = cellList;
    }

}
