package com.photostudio.entity.photo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Photos {
    private List<String> unselectedPhotosPath;
    private List<String> retouchedPhotosPath;
}
