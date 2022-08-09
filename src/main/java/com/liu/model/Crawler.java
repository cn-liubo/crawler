package com.liu.model;

import com.liu.dao.ProjectDao;
import com.liu.domain.Project;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {

    OkHttpClient okHttpClient = new OkHttpClient();

    public static void main(String[] args) throws IOException {
        Crawler crawler = new Crawler();
//        String url = "https://docs.oracle.com/javase/8/docs/api/overview-frame.html";
        String url = "https://docs.oracle.com/javase/8/docs/api/allclasses-frame.html";
        ProjectDao dao = new ProjectDao();
        String html = crawler.getPage(url);
        List<Project> projects = crawler.getProjectList(html);
//        System.out.println(projects);
//        for (int i = 0; i < projects.size(); i++) {
//            Project project = projects.get(i);
//            String repoName = crawler.getRepoName(project.getUrl());
//            System.out.println(repoName + "--" + project.getClassName());
//
//            System.out.println("==========================================");
////            String classHtml = crawler.getRepoInfo(project.getUrl());
//
//        }

//        //将数据保存到数据库中
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            dao.save(project);
            System.out.println(project.getClassName() +  "保存成功");
        }

    }

    //java/lang
    public String getRepoInfo(String url) throws IOException {
        //OkHttpClient对象前面已经创建过了，不需要重复创建
        //请求对象，Call对象，响应对象，还是需要重新创建的
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if(!response.isSuccessful()) {
            System.out.println("访问Github API失败！ url = " + url);
            return null;
        }
        return response.body().string();
    }

    //https://docs.oracle.com/javase/8/docs/api/java/applet/package-frame.html --> java/applet
    public String getRepoName(String url) {
        int a = url.indexOf("api");
        int b = url.lastIndexOf("/");
        if(a == -1 || b == -1) {
            System.out.println("当前的URL是不合法的：" + url);
            return null;
        }
        return url.substring(a + 4, b);
    }

    public List<Project> getProjectList(String html) {
        List<Project> projects = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements li = document.getElementsByTag("li");
        for(Element element : li) {
            Elements allLink = element.getElementsByTag("a");
            if(allLink.isEmpty()) {
                //说明当前的li标签中没有a标签
                continue;
            }
            Element alink = allLink.get(0);
            String url = alink.attr("href");
            String packageName = alink.attr("title");
            packageName = packageName.substring(packageName.lastIndexOf(" "));
            url = "https://docs.oracle.com/javase/8/docs/api/" + url;
            Project project = new Project();
            project.setClassName(alink.text());
            project.setPackageName(packageName);
            project.setUrl(url);
            projects.add(project);
        }
        return projects;
    }

    public String getPage(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if(!response.isSuccessful()) {
            System.out.println("请求失败");
            return null;
        }
        return response.body().string();
    }
}
