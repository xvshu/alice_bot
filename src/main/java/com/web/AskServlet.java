package com.web;

import com.context.ChartManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AskServlet extends HttpServlet {

    private ChartManager chartManager = null;

    public AskServlet() {
        super();
        chartManager = ChartManager.getInstance();
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String askWord=request.getParameter("askWord");
        String outWord=chartManager.response(askWord);
        response.setContentType("text/html");
        response.getWriter().println(outWord);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String askWord=request.getParameter("askWord");
        System.out.println("askWord:"+String.valueOf(askWord));
        String outWord=chartManager.response(askWord);
        System.out.println("outWord:"+String.valueOf(outWord));
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(outWord);
    }
}
