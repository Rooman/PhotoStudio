package com.photostudio.entity.photo;

import java.util.List;

public class Photos {
    private List<String> unselectedPhotosPath;
    private List<String> retouchedPhotosPath;

    public List<String> getUnselectedPhotosPath() {
        return unselectedPhotosPath;
    }

    public void setUnselectedPhotosPath(List<String> newPhotosPath) {
        this.unselectedPhotosPath = newPhotosPath;
    }

    public List<String> getRetouchedPhotosPath() {
        return retouchedPhotosPath;
    }

    public void setRetouchedPhotosPath(List<String> retouchedPhotosPath) {
        this.retouchedPhotosPath = retouchedPhotosPath;
    }
}
