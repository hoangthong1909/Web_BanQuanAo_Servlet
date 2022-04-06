package controller.admin;

import DAO.CategoryDAO;
import DAO.ProductDAO;
import DAO.UserDao;
import entity.Category;
import entity.Product;
import entity.User;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@WebServlet({"/Product", "/storeProduct", "/updateProduct", "/deleteProduct", "/editProduct"})
public class ProductServlet extends HttpServlet {
    private CategoryDAO categoryDAO;
    private ProductDAO dao;

    public ProductServlet() {
        this.dao = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.contains("Product")) {
            this.create(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.contains("updateProduct")) {
            this.update(request, response);
        } else if (uri.contains("storeProduct")) {
            this.store(request, response);
        } else if (uri.contains("editProduct")) {
            this.edit(request, response);
        } else if (uri.contains("deleteProduct")) {
            this.delete(request, response);
        }
    }

    protected void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> dsCategory = this.categoryDAO.all();
        request.setAttribute("dsCategory", dsCategory);
        List<Product> list = this.dao.all();
        request.setAttribute("list", list);
        request.setAttribute("view", "/views/admin/product/create.jsp");
        request.setAttribute("view1", "/views/admin/product/table.jsp");
        request.getRequestDispatcher("/views/admin/admin.jsp").forward(request, response);
    }

    protected void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getParameter("id");
        int id = Integer.parseInt(s);
        try {
            Product entity = this.dao.findByID(id);
            BeanUtils.populate(entity, request.getParameterMap());
            this.dao.update(entity);
            response.sendRedirect("/Product");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getParameter("id");
        int id = Integer.parseInt(s);
        try {
            Product entity = this.dao.findByID(id);
            BeanUtils.populate(entity, request.getParameterMap());
            entity.setStatus(false);
            this.dao.update(entity);
            response.sendRedirect("/Product");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> dsCategory = this.categoryDAO.all();
        request.setAttribute("dsCategory", dsCategory);
        String s = request.getParameter("id");
        int id = Integer.parseInt(s);
        Product entity = this.dao.findByID(id);
        request.setAttribute("product", entity);
        List<Product> list = this.dao.all();
        request.setAttribute("list", list);
        request.setAttribute("view", "/views/admin/product/edit.jsp");
        request.setAttribute("view1", "/views/admin/product/table.jsp");
        request.getRequestDispatcher("views/admin/admin.jsp").forward(request, response);
    }

    protected void store(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getParameter("category_id");
        int id = Integer.parseInt(s);
        Product entity = new Product();
        List<Product> list = new ArrayList<>();
        try {
            BeanUtils.populate(entity, request.getParameterMap());
            Category category = this.categoryDAO.findByID(id);
            entity.setCategory(category);
            this.dao.create(entity);
            list.add(entity);
            request.setAttribute("list", list);
            List<Product> all = this.dao.all();
            request.setAttribute("list", all);
            response.sendRedirect("/Product");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
