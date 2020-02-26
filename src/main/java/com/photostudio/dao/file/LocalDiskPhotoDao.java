package com.photostudio.dao.file;

import com.photostudio.dao.PhotoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LocalDiskPhotoDao implements PhotoDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private String path;

    public LocalDiskPhotoDao(String path) {
        if (path == null) {
            LOG.error("can't create object LocalDiskPhotoDao: path is null");
            throw new RuntimeException("path to Photo folder is null");
        }
        this.path = path;
    }

    @Override
    public void deleteByOrder(long orderId) {
        LOG.info("delete photos from local disk by path:{}", path);
        File dir = new File(path, String.valueOf(orderId));
        if (dir.exists()) {
            deleteDir(dir);
        }
        LOG.info("photos for order {} were deleted", orderId);
    }

    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] content = dir.listFiles();
            if (content != null) {
                for (File file : content) {
                    deleteDir(file);
                }
            }
        }
        dir.delete();
    }

}
