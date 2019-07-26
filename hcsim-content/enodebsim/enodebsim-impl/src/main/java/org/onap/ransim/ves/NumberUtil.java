/*
 * Copyright (C) 2018 Wipro Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.ransim.ves;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberUtil {

    private static int faultEventIdSequence = 1;
    private static int measEventIdSequence = 1;
    private static final Logger LOG = LoggerFactory.getLogger(NumberUtil.class);

    public static int getAndIncreamentFaultEventIdSequence() {
        return faultEventIdSequence++;
    }

    public static int getAndIncreamentMeasEventIdSequence() {
        return measEventIdSequence++;
    }

    public static String intToString(int num, int digits) {
        StringBuffer s = new StringBuffer(digits);
        int zeroes = digits - (int) (Math.log(num) / Math.log(10)) - 1;
        for (int i = 0; i < zeroes; i++) {
            s.append(0);
        }
        return s.append(num).toString();
    }
}
