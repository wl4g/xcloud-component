/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wl4g;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.shardingsphere.proxy.arguments.BootstrapArguments;
import org.apache.shardingsphere.proxy.config.YamlProxyConfiguration;
import org.apache.shardingsphere.proxy.initializer.BootstrapInitializer;

import com.wl4g.component.integration.sharding.config.ProxyConfigurationLoader2;
import com.wl4g.component.integration.sharding.failover.initializer.FailoverGovernanceBootstrapInitializer;
import com.wl4g.component.integration.sharding.failover.initializer.FailoverStandardBootstrapInitializer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * ShardingSphere-Proxy Bootstrap.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShardingsphereProxy {

    public static void main(final String[] args) throws IOException, SQLException {
        BootstrapArguments bootstrapArgs = new BootstrapArguments(args);
        YamlProxyConfiguration yamlConfig = ProxyConfigurationLoader2.load(bootstrapArgs.getConfigurationPath());
        createBootstrapInitializer(yamlConfig).init(yamlConfig, bootstrapArgs.getPort());
    }

    //
    // ADD for failover.
    //

    private static BootstrapInitializer createBootstrapInitializer(final YamlProxyConfiguration yamlConfig) {
        return null == yamlConfig.getServerConfiguration().getGovernance() ? new FailoverStandardBootstrapInitializer()
                : new FailoverGovernanceBootstrapInitializer();
    }

}