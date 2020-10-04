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
package com.wl4g.components.core.bean.iam;

import com.wl4g.components.core.bean.BaseBean;
import com.wl4g.components.core.bean.iam.model.GroupExt;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Group extends BaseBean implements Serializable {
	private static final long serialVersionUID = 381411777614066880L;

	private String organizationCode;

	private String name;

	private String displayName;

	private Integer type;

	private Long parentId;

	private Integer status;

	private String parentCode;

	private Long areaId;

	//
	// --- Temporary fields. ---
	//

	private List<Group> children;
	private List<Long> menuIds;
	private List<Long> roleIds;
	private GroupExt groupExt;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Group group = (Group) o;
		return Objects.equals(getId(), group.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

}