package kr.ac.kaist.se;

import java.io.File;

/**
 * Util.java
 * Author: Junho Kim <jhkim@se.kaist.ac.kr>

 * The MIT License (MIT)

 * Copyright (c) 2016 Junho Kim

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: TBD
 */
public class Util {

    public static void create_result_directory(String resultDirName) throws Exception{
        File directory = new File(resultDirName);
        if(directory.exists()){
            if(!directory.isDirectory())
                throw new Exception("The result file cannot be created!");
        }else{
            try{
                directory.mkdirs();
            }catch(SecurityException e){
                e.printStackTrace();
            }
        }
    }

}
