package controller.admin;

import DAO.CategoryDAO;
import DAO.UserDao;
import entity.Category;
import entity.User;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet({"/Category", "/storeCategory", "/updateCategory", "/deleteCategory", "/editCategory"})
public class CategoryServlet extends HttpServlet {
    private CategoryDAO dao;
    private UserDao userDao;

    public CategoryServlet() {
        this.dao = new CategoryDAO();
        this.userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String uri = request.getRequestURI();
        if (uri.contains("Category")) {
            this.create(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String uri = request.getRequestURI();
        if (uri.contains("updateCategory")) {
            this.update(request, response);
        } else if (uri.contains("storeCategory")) {
            this.store(request, response);
        } else if (uri.contains("editCategory")) {
            this.edit(request, response);
        } else if (uri.contains("deleteCategory")) {
            this.delete(request, response);
        }
    }

    protected void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> list = this.dao.all();
        request.setAttribute("list", list);
        request.setAttribute("view", "/views/admin/category/createCategory.jsp");
        request.setAttribute("view1", "/views/admin/category/tableCategory.jsp");
        request.getRequestDispatcher("/views/admin/admin.jsp").forward(request, response);
    }

    protected void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        String s = request.getParameter("id");
        int id = Integer.parseInt(s);
        try {
            Category entity = this.dao.findByID(id);
            BeanUtils.populate(entity, request.getParameterMap());
            this.dao.update(entity);
            session.setAttribute("message","C???p Nh???t Th??nh C??ng");
            response.sendRedirect("/Category");
        } catch (Exception e) {
            session.setAttribute("error","C???p Nh???t Th???t B???i");
            response.sendRedirect("/Category");
            e.printStackTrace();
        }
    }

    protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        String s = request.getParameter("id");
        int id = Integer.parseInt(s);
        try {
            Category entity = this.dao.findByID(id);
            BeanUtils.populate(entity, request.getParameterMap());
            entity.setStatus(false);
            this.dao.update(entity);
            session.setAttribute("message","X??a Th??nh C??ng");
            response.sendRedirect("/Category");
        } catch (Exception e) {
            session.setAttribute("error","X??a Th???t B???i");
            response.sendRedirect("/Category");
            e.printStackTrace();
        }
    }

    protected void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getParameter("id");
        int id = Integer.parseInt(s);
        Category entity = this.dao.findByID(id);
        request.setAttribute("category", entity);
        List<Category> list = this.dao.all();
        request.setAttribute("list", list);
        request.setAttribute("view", "/views/admin/category/editCategory.jsp");
        request.setAttribute("view1", "/views/admin/category/tableCategory.jsp");
        request.getRequestDispatcher("views/admin/admin.jsp").forward(request, response);
    }

    protected void store(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Category entity = new Category();
        List<Category> list = new ArrayList<>();
        try {
            BeanUtils.populate(entity, request.getParameterMap());
            User user = (User) session.getAttribute("user");
            entity.setUser(user);
            entity.setStatus(true);
            entity.setNgayTao(new Date());
            this.dao.create(entity);
            session.setAttribute("message", "Th??m M???i Th??nh C??ng");
            list.add(entity);
            request.setAttribute("list", list);
            List<Category> all = this.dao.all();
            request.setAttribute("list", all);
            response.sendRedirect("/Category");
        } catch (Exception e) {
            session.setAttribute("error", "Th??m M???i Th???t B???i");
            response.sendRedirect("/Category");
            e.printStackTrace();
        }
    }
}
