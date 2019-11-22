package com.chudzick.expanses.domain.analysis;

import com.chudzick.expanses.domain.common.SimpleExcludeObject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupExcludeDto {
    private List<SimpleExcludeObject> excludeObjects;

    public List<SimpleExcludeObject> getExcludeObjects() {
        return excludeObjects;
    }

    public void setExcludeObjects(List<SimpleExcludeObject> excludeObjects) {
        this.excludeObjects = excludeObjects;
    }

    public Set<Long> getExcludedIds() {
        return excludeObjects
                .stream()
                .filter(SimpleExcludeObject::isExclude)
                .map(SimpleExcludeObject::getId)
                .collect(Collectors.toSet());
    }
}
