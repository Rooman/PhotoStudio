package com.photostudio.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LocalDiskPhotoDao {

  private final Logger LOG = LoggerFactory.getLogger(getClass());
  private String path;

  public LocalDiskPhotoDao(String path) {this.path=path;}

  public void deleteByOrder(long orderId) {

      String dirToDelete = path + File.separator + String.valueOf(orderId);

      LOG.info("delete photos from local disk by path:{}", dirToDelete);
      try {
          File dir = new File(dirToDelete);
          if (dir.exists()) {
              deleteDir(dir);
          }
          LOG.info("photos for order were deleted");
      }
      catch (Exception e) {
          LOG.error("can't delete photos from disk", e);
          throw new RuntimeException("can't delete photos from disk:", e);
      }
  }

    private void deleteDir(File dir) {
        for (File file:dir.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
            }
            else {
                file.delete();
            }
        }
        dir.delete();
    }

}
