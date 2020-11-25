// Generated by XCloud DevOps for Codegen, refer: http://dts.devops.wl4g.com

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

package com.wl4g.components.core.bean.doc;

import com.wl4g.components.core.bean.BaseBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link EnterpriseDocument}
 *
 * @author root
 * @version master
 * @Date 2020-11-25
 * @since v1.0
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class EnterpriseDocument extends BaseBean {
    private static final long serialVersionUID = 227097628555982784L;

    /**
     * 
     */
    private Long versionId;

    /**
     * 父级id
     */
    private Long parentid;

    /**
     * 文档标题
     */
    private String title;

    /**
     * md内容
     */
    private String content;

    /**
     * 同级菜单排序
     */
    private Long sort;

    /**
     * 语言
     */
    private String lang;

    /**
     * 
     */
    private String remark;

    /**
     * 组织编码
     */
    private String organizationCode;

    public EnterpriseDocument() {
    }

    public EnterpriseDocument withVersionId(Long versionId) {
        setVersionId(versionId);
        return this;
    }

    public EnterpriseDocument withParentid(Long parentid) {
        setParentid(parentid);
        return this;
    }

    public EnterpriseDocument withTitle(String title) {
        setTitle(title);
        return this;
    }

    public EnterpriseDocument withContent(String content) {
        setContent(content);
        return this;
    }

    public EnterpriseDocument withSort(Long sort) {
        setSort(sort);
        return this;
    }

    public EnterpriseDocument withLang(String lang) {
        setLang(lang);
        return this;
    }

    public EnterpriseDocument withRemark(String remark) {
        setRemark(remark);
        return this;
    }

    public EnterpriseDocument withOrganizationCode(String organizationCode) {
        setOrganizationCode(organizationCode);
        return this;
    }
}