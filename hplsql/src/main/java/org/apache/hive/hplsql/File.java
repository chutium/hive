/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hive.hplsql;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;

/**
 * HDFS file operations
 */
public class File {
  Path path;
  FileSystem fs;
  FSDataInputStream in;
  FSDataOutputStream out;
  
  /**
   * Create FileSystem object
   */
  public FileSystem createFs() throws IOException {
    fs = FileSystem.get(new Configuration());
    return fs;
  }
  
  /**
   * Create a file
   */
  public FSDataOutputStream create(boolean overwrite) {
    try {
      if (fs == null) {
        fs = createFs();
      }
      out = fs.create(path, overwrite);
    } 
    catch (IOException e) {
      e.printStackTrace();
    }
    return out;
  }
  
  public FSDataOutputStream create(String dir, String file, boolean overwrite) {
    path = new Path(dir, file);
    return create(overwrite);
  }

  public FSDataOutputStream create(String file, boolean overwrite) {
    path = new Path(file);
    return create(overwrite);
  }
  
  /**
  * Open an existing file
  */
 public void open(String dir, String file) {
   path = new Path(dir, file);
   try {
     if (fs == null) {
       fs = createFs();
     }
     in = fs.open(path);
   } catch (IOException e) {
     e.printStackTrace();
   }
 }
  
 /**
  * Read a character from input
  * @throws IOException 
  */
 public char readChar() throws IOException {
   return in.readChar();
 }
 
  /**
   * Write string to file
   */
  public void writeString(String str) {
    try {
      out.writeChars(str);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Close a file
   */
  public void close() {
    try {
      if (in != null) {
        in.close();
      }      
      if (out != null) {
        out.close();
      }      
      in = null;
      out = null;
      path = null;
      fs = null;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Get the fully-qualified path
   * NOTE: FileSystem.resolvePath() is not available in Hadoop 1.2.1 
   * @throws IOException 
   */
  public Path resolvePath(Path path) throws IOException {
    return fs.getFileStatus(path).getPath();  
  }
  
  @Override
  public String toString() {
    if (path != null) {
      return "FILE <" + path.toString() + ">";
    }
    return "FILE <null>";
  }
}
