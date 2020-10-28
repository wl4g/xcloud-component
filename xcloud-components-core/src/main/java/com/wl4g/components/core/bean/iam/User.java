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

import java.io.Serializable;
import java.util.List;

import com.wl4g.components.core.bean.BaseBean;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link User}
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version 2019-05-28
 * @sine v1.0.0
 * @see
 */
@Getter
@Setter
public class User extends BaseBean implements Serializable {
	private static final long serialVersionUID = 381411777614066880L;

	private String userName;
	private String nameEn;
	private String nameZh;
	private String password;
	private String pubSalt; // public salt
	private Integer userType;
	private Integer status;
	private String email;
	private String phone;
	private String wechatOpenId;
	private String wechatUnionId;
	private String facebookId;
	private String googleId;
	private String twitterId;
	private String linkedinId;
	private String alipayId;
	private String githubId;
	private String awsId;

	// --- Temporary. ---

	private List<Long> roleIds;
	private String roleStrs;
	private List<Long> groupIds;
	private String groupNameStrs;

	public User() {
	}

	public User(String userName) {
		this.userName = userName;
	}

}