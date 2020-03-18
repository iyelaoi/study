package cn.wqz.servlet;

import cn.wqz.model.Account;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CheckAccount extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = new Account();
        String username = req.getParameter("username");
        String pwd = req.getParameter("pwd");
        account.setPassword(pwd);
        account.setUsername(username);
        if((username != null)&&(username.trim().equals("jsp"))) {
            if((pwd != null)&&(pwd.trim().equals("1"))) {
                System.out.println("success");
                session.setAttribute("account", account);
                String login_suc = "WEB-INF/views/success.jsp";
                req.getRequestDispatcher(login_suc).forward(req, resp);
                return;
            }
        }
        String login_fail = "WEB-INF/views/fail.jsp";
        req.getRequestDispatcher(login_fail).forward(req,resp);
    }
}
