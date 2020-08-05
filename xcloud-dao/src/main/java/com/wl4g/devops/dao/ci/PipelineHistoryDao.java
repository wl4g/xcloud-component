/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
 *
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
 */
package com.wl4g.devops.dao.ci;

import org.apache.ibatis.annotations.Param;

import com.wl4g.components.core.bean.ci.PipelineHistory;
import com.wl4g.components.core.bean.ci.TaskHistory;

import java.util.List;

public interface PipelineHistoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PipelineHistory record);

    int insertSelective(PipelineHistory record);

    PipelineHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PipelineHistory record);

    int updateByPrimaryKey(PipelineHistory record);

    List<TaskHistory> list(@Param("organizationCodes")List<String> organizationCodes, @Param("pipeName") String pipeName, @Param("clusterName") String clusterName,
                           @Param("environment") String environment, @Param("startDate") String startDate,
                           @Param("endDate") String endDate, @Param("providerKind") String providerKind);

    int updateStatus(Long time);
}