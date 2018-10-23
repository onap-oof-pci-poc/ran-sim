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

package org.onap.ransim.rest.api.controller;

public enum GridType {
    HONEYCOMB(1), CIRCULAR(2), RANDOM(3);

    private final int gridCode;

    private GridType(int gridCode) {
        this.gridCode = gridCode;
    }

    /**
     * It assigns the grid type to an integer value.
     *
     * @param type
     *            --
     * @return --
     */
    public static GridType fromInteger(int type) {
        switch (type) {
            case 1:
                return HONEYCOMB;
            case 2:
                return CIRCULAR;
            case 3:
                return RANDOM;
            default:
                return null;

        }

    }
}
