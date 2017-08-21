/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  上午10:48:39
 */
package com.irh.tcp.core.manager.webserver;

import com.irh.core.util.LogUtil;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 基础servlet
 *
 * @author iritchie.ren
 */
public abstract class BaseServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 3432572293207677347L;

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setHeader("Content-Type", "text/json; charset=UTF-8");

        String json = req.getParameter("data");
        String result = "NO DATA!";
        if (json != null) {
            try {
                result = execute(req, resp, json);
            } catch (Exception e) {
                result = createFailInfo();
                LogUtil.error(e);
            }
        } else {
            result = createFailInfo("没有data");
        }

        PrintWriter out = resp.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    /**
     * @return
     */
    protected abstract String execute(HttpServletRequest req,
                                      HttpServletResponse resp, String data);

    /**
     * 创建失败的内容
     */
    protected String createFailInfo() {
        JSONObject result = new JSONObject();
        result.put("code", 0);
        result.put("message", "执行失败");
        return result.toString();
    }

    /**
     * 创建失败的内容
     */
    protected String createFailInfo(String reason) {
        JSONObject result = new JSONObject();
        result.put("code", 0);
        result.put("message", reason);
        return result.toString();
    }

}
