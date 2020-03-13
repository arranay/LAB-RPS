/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import dao.UserDaoLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import singlton.countAddUser;
import statefullBean.StatefullBeanLocal;

/**
 *
 * @author Лера
 */
public class addUser extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addUser</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addUser at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @EJB
    private StatefullBeanLocal slBean;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String go = request.getParameter("go");
                if (go.equals("add")){
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("addUser.jsp");
                    requestDispatcher.forward(request, response);
                }else{
                    List<String> ls = slBean.returnList();
                    request.setAttribute("userList", ls);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("show.jsp");
                    requestDispatcher.forward(request, response);
                }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB 
    private UserDaoLocal userDao;
    
    @EJB
    private countAddUser caUser;
    
    
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       if (!(request.getParameter("password").equals(request.getParameter("password2")))){
            request.setAttribute("error", "Пароли не совпадают");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
            requestDispatcher.forward(request, response);
        }
       User user = new User();
       user.setLogin(request.getParameter("login"));
       user.setEmail(request.getParameter("email"));
       user.setPassword(request.getParameter("password"));
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       String bdate = request.getParameter("bdate");
       try {
                Date date =format.parse(bdate); 
                user.setbDate(date);  
            } catch (ParseException e) {
                    e.printStackTrace();
            }
       if (userDao.insertUser(user)){
           caUser.plus();
           slBean.addLogin(request.getParameter("login"));
       }
       RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
       requestDispatcher.forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
