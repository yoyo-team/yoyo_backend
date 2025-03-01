package com.vanging.www.yoyo.extractor;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.runtime.regexp.RegExp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Extractor
{
    public static boolean extract(String cid, String fileAbsolutePath, boolean development) throws IOException
    {
        String rootDirPath;
        if( ! development)
        {
            rootDirPath = "/web/sites/www.vanging.com/yoyo/classes";
        }
        else
        {
            rootDirPath = "I:\\大创项目\\web\\classes";
        }

        String classDirPath = rootDirPath + File.separator + cid;
        File classDir = new File(classDirPath);
        if(!classDir.exists())
        {
            if(!classDir.mkdir())
            {
                System.out.println("创建目录失败 ： " + classDirPath);
                return false;
            }
        }

        File jsonFile = new File(classDirPath + File.separator + "index.json");

        List<Item> jsonObject = new ArrayList<Item>();

        File pptFile = new File(fileAbsolutePath);

        List pictureNames = Image.extract(pptFile,classDirPath);;
        if(pictureNames == null)
        {
            DeleteDirectory.deleteDir(classDir);
            return false;
        }
        for(Object picture : pictureNames)
        {
            Item item = new Item();
            item.setType("img");
            item.setContent(picture.toString());
            jsonObject.add(item);
        }

        String text = Text.extract(pptFile);
        for(String line : text.split("(\\r|\\n)+"))
        {
            if( ! line.matches("\\s*"))
            {
                // 去掉特殊字符，否则json解析会失败
//                line = line.replace("\\r", "");
//                line = line.replace("\\n", "");
//                line = line.replace("\"", "*");
//                line = line.replace("\\'", "*");

                Item item = new Item();
                item.setType("text");
                item.setContent(line.toString());
                jsonObject.add(item);

            }
        }

        String jsonText = JSON.toJSONString(jsonObject);
        System.out.println(jsonText);

        FileOutputStream fos = new FileOutputStream(jsonFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        JSON.writeJSONString(writer, jsonObject);
        fos.close();

        return true;
    }
}
class DeleteDirectory
{
    public static boolean deleteDir(File dir)
    {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            if(children != null)
            {
                //递归删除目录中的子目录下所有文件
                for (int i=0; i<children.length; i++)
                {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if(!success)
                    {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    /**
     *测试
     */
    public static void main(String[] args)
    {
        String newDir2 = "I:\\大创项目\\web\\classes\\ppt";
        boolean success = deleteDir(new File(newDir2));
        if(success)
        {
            System.out.println("Successfully deleted populated directory: " + newDir2);
        }
        else
        {
            System.out.println("Failed to delete populated directory: " + newDir2);
        }
    }
}

class Item
{
    private String type;
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}