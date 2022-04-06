<%--
  Created by IntelliJ IDEA.
  User: thongpro
  Date: 3/31/22
  Time: 5:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<c:if test="${empty list}">
    <p class="alert alert-warning">
        Vui Lòng Thêm Mới Dữ Liệu
    </p>
</c:if>
<%--<c:if test="${!empty list}">--%>
<%--    <p class="alert alert-success">--%>
<%--        Data OK 1--%>
<%--    </p>--%>
<table class="table table-success table-striped">
    <thead>
    <tr>
        <th scope="col">Name</th>
        <th scope="col">Creator</th>
        <th scope="col">DateCreate</th>
        <th colspan="2">Manipulation</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="category">
        <tr>
                <%--                <td>${category.id}</td>--%>
            <td>${category.ten}</td>
            <td>${category.user.hoTen}</td>
            <td><fmt:formatDate value="${category.ngayTao}" pattern="dd/MM/yyyy"/></td>
            <td>
                <form action="editCategory" method="post">
                    <input type="hidden" value="${category.id}" name="id">
                    <button class="btn btn-primary">Update</button>
                </form>
            </td>
            <td>
                <form action="deleteCategory" method="post">
                    <input type="hidden" value="${category.id}" name="id">
                    <button class="btn btn-danger">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--</c:if>--%>
<%--<div id="exampleModal" class="modal" tabindex="-1">--%>
<%--    <div class="modal-dialog">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header">--%>
<%--                <h3 class="modal-title">Xác nhận</h3>--%>
<%--                <button type="button" class="btn-close" data-bs-dismiss="modal"--%>
<%--                        aria-label="Close"></button>--%>
<%--            </div>--%>
<%--            <div class="modal-body">--%>
<%--                <h5>Bạn có muốn xóa Category này?</h5>--%>
<%--            </div>--%>
<%--            <div class="modal-footer">--%>
<%--                <form method="get" action="/deleteCategory?id=${category.id}">--%>
<%--                    <button type="submit">Continue</button>--%>
<%--                </form>--%>
<%--                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"--%>
<%--                        aria-label="Close">Hủy--%>
<%--                </button>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

