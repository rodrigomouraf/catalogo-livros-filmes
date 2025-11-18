package br.com.catalogo_livros_filmes.controllers;

import br.com.catalogo_livros_filmes.shared.models.CatalogModel;
import br.com.catalogo_livros_filmes.shared.repositories.CatalogRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/home", "/catalog"})
public class CatalogController extends HttpServlet {

    private final CatalogRepository catalogRepository = new CatalogRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null || action.isBlank() || action.equals("list")) {
            listItems(req, resp);
        } else if ("detail".equals(action)) {
            showDetail(req, resp);
        } else if ("form".equals(action)) {
            showForm(req, resp);
        } else if ("search".equals(action)) {
            search(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if ("save".equals(action)) {
            save(req, resp);
        } else if ("delete".equals(action)) {
            delete(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void listItems(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<CatalogModel> items = catalogRepository.findAll();
            req.setAttribute("items", items);
            req.getRequestDispatcher("/WEB-INF/views/items/list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error while listing media items", e);
        }
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            CatalogModel item = catalogRepository.findById(id);
            if (item == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
                return;
            }

            req.setAttribute("item", item);
            req.getRequestDispatcher("/WEB-INF/views/items/detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
        } catch (SQLException e) {
            throw new ServletException("Error while loading item detail", e);
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam != null && !idParam.isBlank()) {
            try {
                Long id = Long.parseLong(idParam);
                CatalogModel item = catalogRepository.findById(id);
                if (item == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
                    return;
                }
                req.setAttribute("item", item); // edição
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
                return;
            } catch (SQLException e) {
                throw new ServletException("Error while loading edit form", e);
            }
        }

        req.getRequestDispatcher("/WEB-INF/views/items/form.jsp").forward(req, resp);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String idParam = req.getParameter("id");
        String title = req.getParameter("title");
        String creator = req.getParameter("creator");
        String releaseYearParam = req.getParameter("releaseYear");
        String genre = req.getParameter("genre");
        String synopsis = req.getParameter("synopsis");
        String mediaType = req.getParameter("mediaType");

        Integer releaseYear = null;
        if (releaseYearParam != null && !releaseYearParam.isBlank()) {
            try {
                releaseYear = Integer.parseInt(releaseYearParam);
            } catch (NumberFormatException e) {
            }
        }

        CatalogModel item = new CatalogModel();
        if (idParam != null && !idParam.isBlank()) {
            item.setId(Long.parseLong(idParam));
        }
        item.setTitle(title);
        item.setCreator(creator);
        item.setReleaseYear(releaseYear);
        item.setGenre(genre);
        item.setSynopsis(synopsis);
        item.setMediaType(mediaType);

        try {
            if (item.getId() == null) {
                catalogRepository.insert(item);
            } else {
                catalogRepository.update(item);
            }
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (SQLException e) {
            throw new ServletException("Error while saving item", e);
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            catalogRepository.deleteById(id);
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
        } catch (SQLException e) {
            throw new ServletException("Error while deleting item", e);
        }
    }

    private void search(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String query = req.getParameter("query");
        if (query == null || query.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            List<CatalogModel> items = catalogRepository.searchByTitleOrCreator(query);
            req.setAttribute("items", items);
            req.setAttribute("query", query);
            req.getRequestDispatcher("/WEB-INF/views/items/list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error while searching items", e);
        }
    }
}
