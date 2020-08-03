package com.wl4g.devops.common.bean.gw;

import com.wl4g.devops.common.bean.BaseBean;
import java.util.List;

public class GWUpstreamGroup extends BaseBean {
    private static final long serialVersionUID = -3298424126317938674L;

    private String name;

    private List<GWUpstreamGroupRef> gwUpstreamGroupRefs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public List<GWUpstreamGroupRef> getGwUpstreamGroupRefs() {
        return gwUpstreamGroupRefs;
    }

    public void setGwUpstreamGroupRefs(List<GWUpstreamGroupRef> gwUpstreamGroupRefs) {
        this.gwUpstreamGroupRefs = gwUpstreamGroupRefs;
    }




}