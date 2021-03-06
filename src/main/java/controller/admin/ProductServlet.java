package controller.admin;

import DAO.CategoryDAO;
import DAO.ProductDAO;
import DAO.UserDao;
import JPAUtils.FileUtil;
import entity.Category;
import entity.Product;
import entity.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
@WebServlet({"/Product", "/storeProduct", "/updateProduct", "/deleteProduct", "/editProduct", "/Hidden", "/Open"})
public class ProductServlet extends HttpServlet {
    private CategoryDAO categoryDAO;
    private ProductDAO dao;

    public ProductServlet() {
        this.dao = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String uri = request.getRequestURI();
        if (uri.contains("Product")) {
            this.create(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String uri = request.getRequestURI();
        if (uri.contains("updateProduct")) {
            this.update(request, response);
        } else if (uri.contains("storeProduct")) {
            this.store(request, response);
        } else if (uri.contains("editProduct")) {
            this.edit(request, response);
        } else if (uri.contains("deleteProduct")) {
            this.delete(request, response);
        } else if (uri.contains("Hidden")) {
            this.hidden(request, response);
        } else if (uri.contains("Open")) {
            this.open(request, response);
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
        HttpSession session = request.getSession();
        String s = request.getParameter("id");
        int id = Integer.parseInt(s);
        int idCate = Integer.parseInt(request.getParameter("category_id"));
        try {
            Category category = this.categoryDAO.findByID(idCate);
            Product entity = this.dao.findByID(id);
            BeanUtils.populate(entity, request.getParameterMap());
            File file = FileUtil.saveFileUpload(request.getServletContext().getRealPath("/images/products"), request.getPart("img"));
            if (file.getName().equals("/images/products")) {
                entity.setImg(entity.getImg());
            } else {
                entity.setImg(file.getName());
            }
            entity.setCategory(category);
            this.dao.update(entity);
            session.setAttribute("message", "C???p Nh???t Th??nh C??ng");
            response.sendRedirect("/Product");
        } catch (Exception e) {
            response.sendRedirect("/Product");
            session.setAttribute("error", "C???p Nh???t Th???t B???i");
            e.printStackTrace();
        }
    }

    protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String s = request.getParameter("id");
        int id = Integer.parseInt(s);
        try {
            Product entity = this.dao.findByID(id);
            BeanUtils.populate(entity, request.getParameterMap());
            entity.setStatus(0);
            this.dao.update(entity);
            session.setAttribute("message", "X??a Th??nh C??ng");
            response.sendRedirect("/Product");
        } catch (Exception e) {
            session.setAttribute("error", "X??a Th???t B???i");
            response.sendRedirect("/Product");
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
        HttpSession session = request.getSession();
        String s = request.getParameter("category_id");
        String price = request.getParameter("donGia");
        int id = Integer.parseInt(s);
        Product entity = new Product();
        List<Product> list = new ArrayList<>();
        try {
            if (Integer.parseInt(price) < 0) {
                session.setAttribute("error", "Vui L??ng Nh???p Gi?? l???n h??n 0");
                response.sendRedirect("/Product");
                return;
            }
            BeanUtils.populate(entity, request.getParameterMap());
            File file = FileUtil.saveFileUpload(request.getServletContext().getRealPath("/images/products"), request.getPart("img"));
            if (file.getName().equals("/images/products")) {
                entity.setImg("");
            } else {
                entity.setImg(file.getName());
            }
            Category category = this.categoryDAO.findByID(id);
            entity.setCategory(category);
            entity.setStatus(1);
            this.dao.create(entity);
            session.setAttribute("message", "Th??m M???i Th??nh C??ng");
            list.add(entity);
            request.setAttribute("list", list);
            List<Product> all = this.dao.all();
            request.setAttribute("list", all);
            response.sendRedirect("/Product");
        } catch (Exception e) {
            session.setAttribute("error", "Th??m M???i Th???t B???i");
            response.sendRedirect("/Product");
            e.printStackTrace();
        }
    }

    protected void hidden(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String s = request.getParameter("id");
        try {
            int id = Integer.parseInt(s);
            Product entity = this.dao.findByID(id);
            entity.setStatus(2);
            this.dao.update(entity);
            session.setAttribute("message", "???n Th??nh C??ng");
            response.sendRedirect("/Product");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "???n  Th???t B???i");
            response.sendRedirect("/Product");
        }
    }

    protected void open(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String s = request.getParameter("id");
        try {
            int id = Integer.parseInt(s);
            Product entity = this.dao.findByID(id);
            entity.setStatus(1);
            this.dao.update(entity);
            session.setAttribute("message", "M??? Th??nh C??ng");
            response.sendRedirect("/Product");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "M??? Th???t B???i");
            response.sendRedirect("/Product");
        }
    }
}
