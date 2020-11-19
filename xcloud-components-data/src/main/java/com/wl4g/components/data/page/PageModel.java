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
package com.wl4g.components.data.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.pagehelper.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Customized page model.
 * 
 * @auhtor Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2018年9月7日
 * @since
 */
@ApiModel("Page query data")
public class PageModel<E> implements Serializable {
	private static final long serialVersionUID = -7002775417254397561L;

	/**
	 * Page of {@link Page}
	 */
	@JsonIgnore(true)
	private Page<E> page;

	/**
	 * Page record rows.</br>
	 * </br>
	 * 
	 * <b>Note:</b> The following annotation combination configuration does not
	 * implement the effect that the records field can make the request display
	 * no two responses. Can't swagger2 still achieve this effect: when the type
	 * of request and response are the same model class, can't some fields be
	 * displayed or not displayed according to the request and response? </br>
	 * </br>
	 * 
	 * <p>
	 * for negative examples:
	 * 
	 * <pre>
	 * &#64;ApiOperation(value = "Query myuser page list")
	 * &#64;RequestMapping(value = "/list", method = { GET })
	 * public RespBase&lt;PageModel&lt;MyUserModel&gt;&gt; list(PageModel&lt;MyUserModel&gt; pm, MyUserModel param) {
	 * 	RespBase&lt;PageModel&lt;MyUserModel&gt;&gt; resp = RespBase.create();
	 * 	resp.setData(myUserService.page(pm, param));
	 * 	return resp;
	 * }
	 * </pre>
	 * 
	 * for positive examples(Solution):
	 * 
	 * <pre>
	 * &#64;ApiOperation(value = "Query myuser page list")
	 * &#64;ApiImplicitParams({
	 *	&#64;ApiImplicitParam(name = "pageNum", dataType = "int32", defaultValue = "1"),
	 *	&#64;ApiImplicitParam(name = "pageSize", dataType = "int32", defaultValue = "10")
	 * })
	 * &#64;RequestMapping(value = "/list", method = { GET })
	 * public RespBase&lt;PageModel&lt;MyUserModel&gt;&gt; list({@code @ApiIgnore} PageModel&lt;MyUserModel&gt; pm, MyUserModel param) {
	 * 	RespBase&lt;PageModel&lt;MyUserModel&gt;&gt; resp = RespBase.create();
	 * 	resp.setData(myUserService.page(pm, param));
	 * 	return resp;
	 * }
	 * </pre>
	 * </p>
	 */
	@ApiModelProperty(readOnly = true, accessMode = AccessMode.READ_ONLY)
	@ApiParam(readOnly = true, hidden = true)
	@JsonIgnoreProperties(allowGetters = true, allowSetters = false)
	private List<E> records = emptyList();

	public PageModel() {
		this(1, 10);
	}

	public PageModel(Integer pageNum, Integer pageSize) {
		setPageNum(pageNum);
		setPageSize(pageSize);
	}

	public Integer getPageNum() {
		return forPage().getPageNum();
	}

	public void setPageNum(Integer pageNum) {
		if (nonNull(pageNum)) {
			forPage().setPageNum(pageNum);
		}
	}

	public Integer getPageSize() {
		return forPage().getPageSize();
	}

	public void setPageSize(Integer pageSize) {
		if (nonNull(pageSize)) {
			forPage().setPageSize(pageSize);
		}
	}

	public Long getTotal() {
		return forPage().getTotal();
	}

	public void setTotal(Long total) {
		if (nonNull(total)) {
			forPage().setTotal(total);
		}
	}

	public List<E> getRecords() {
		return records;
	}

	public void setRecords(List<E> records) {
		if (!isNull(records)) {
			this.records = records;
		}
	}

	/**
	 * Setup page info.
	 *
	 * @param page
	 */
	public void page(Page<E> page) {
		this.page = page;
	}

	/**
	 * Ensure for get page.
	 * 
	 * @return
	 */
	private Page<E> forPage() {
		if (isNull(page)) {
			synchronized (this) {
				if (isNull(page)) {
					return (this.page = new Page<>());
				}
			}
		}
		return page;
	}

	@Override
	public String toString() {
		return "PageModel [pageNum=" + getPageNum() + ", pageSize=" + getPageSize() + ", total=" + getTotal() + ", records="
				+ getRecords().size() + "]";
	}

}