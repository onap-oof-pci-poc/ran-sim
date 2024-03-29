/*
 * Copyright (c) 2016 Cisco and/or its affiliates.
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

package org.onap.ransim;

import com.google.common.collect.Lists;
import com.google.inject.Module;
import io.fd.honeycomb.infra.distro.activation.ActivationModule;
import java.util.List;
import org.onap.ransim.ConfigJsonHandler;

public class Main {

    public static void main(String[] args) {
/*
        final List<Module> sampleModules = Lists.newArrayList(io.fd.honeycomb.infra.distro.Main.BASE_MODULES);

        sampleModules.add(new org.onap.ransim.enodebsim.Module());

        io.fd.honeycomb.infra.distro.Main.init(sampleModules);
*/
        //io.fd.honeycomb.infra.distro.Main.init(new ActivationModule());
        ConfigJsonHandler.ignoreEvents();
        io.fd.honeycomb.infra.distro.Main.main(args);
        ConfigJsonHandler.monitorEvents();
    }
}
