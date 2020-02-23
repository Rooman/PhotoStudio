package com.photostudio.dao;

import java.io.File;

public class LocalDiskPhotoDao {
  private String path;

  public LocalDiskPhotoDao(String path) {this.path=path;}

  public void deleteByOrder(long orderId) {

      String dirToDelete = path + File.separator + String.valueOf(orderId);

      try {
          File dir = new File(dirToDelete);
          if (dir.exists()) {
              deleteDir(dir);
          }
      }
      catch (Exception e) {
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
