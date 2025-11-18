package br.com.catalogo_livros_filmes.shared.repositories;

import br.com.catalogo_livros_filmes.shared.database.ConnectionFactory;
import br.com.catalogo_livros_filmes.shared.models.CatalogModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogRepository {

    private static final String SQL_FIND_ALL = """
        SELECT id, title, creator, release_year, genre, synopsis, media_type
        FROM catalog
        ORDER BY title
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT id, title, creator, release_year, genre, synopsis, media_type
        FROM catalog
        WHERE id = ?
        """;

    private static final String SQL_INSERT = """
        INSERT INTO catalog (title, creator, release_year, genre, synopsis, media_type)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    private static final String SQL_UPDATE = """
        UPDATE catalog
        SET title = ?, creator = ?, release_year = ?, genre = ?, synopsis = ?, media_type = ?
        WHERE id = ?
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM catalog
        WHERE id = ?
        """;

    private static final String SQL_SEARCH_BY_TITLE_OR_CREATOR = """
        SELECT id, title, creator, release_year, genre, synopsis, media_type
        FROM catalog
        WHERE LOWER(title) LIKE ? OR LOWER(creator) LIKE ?
        ORDER BY title
        """;

    public List<CatalogModel> findAll() throws SQLException {
        List<CatalogModel> items = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                items.add(mapRowToModel(rs));
            }
        }

        return items;
    }

    public CatalogModel findById(Long id) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_ID)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToModel(rs);
                }
                return null;
            }
        }
    }

    public void insert(CatalogModel catalog) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, catalog.getTitle());
            stmt.setString(2, catalog.getCreator());

            if (catalog.getReleaseYear() != null) {
                stmt.setInt(3, catalog.getReleaseYear());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, catalog.getGenre());
            stmt.setString(5, catalog.getSynopsis());
            stmt.setString(6, catalog.getMediaType());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    catalog.setId(keys.getLong(1));
                }
            }
        }
    }

    public void update(CatalogModel catalog) throws SQLException {
        if (catalog.getId() == null) {
            throw new IllegalArgumentException("Item id must not be null for update");
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, catalog.getTitle());
            stmt.setString(2, catalog.getCreator());

            if (catalog.getReleaseYear() != null) {
                stmt.setInt(3, catalog.getReleaseYear());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, catalog.getGenre());
            stmt.setString(5, catalog.getSynopsis());
            stmt.setString(6, catalog.getMediaType());
            stmt.setLong(7, catalog.getId());

            stmt.executeUpdate();
        }
    }

    public void deleteById(Long id) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_BY_ID)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public List<CatalogModel> searchByTitleOrCreator(String term) throws SQLException {
        List<CatalogModel> items = new ArrayList<>();

        String like = "%" + term.toLowerCase() + "%";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SEARCH_BY_TITLE_OR_CREATOR)) {

            stmt.setString(1, like);
            stmt.setString(2, like);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRowToModel(rs));
                }
            }
        }

        return items;
    }

    private CatalogModel mapRowToModel(ResultSet rs) throws SQLException {
        CatalogModel item = new CatalogModel();
        item.setId(rs.getLong("id"));
        item.setTitle(rs.getString("title"));
        item.setCreator(rs.getString("creator"));

        int year = rs.getInt("release_year");
        item.setReleaseYear(rs.wasNull() ? null : year);

        item.setGenre(rs.getString("genre"));
        item.setSynopsis(rs.getString("synopsis"));
        item.setMediaType(rs.getString("media_type"));

        return item;
    }
}
