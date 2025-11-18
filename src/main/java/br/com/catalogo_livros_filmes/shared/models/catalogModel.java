package br.com.catalogo_livros_filmes.shared.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogModel {
    private Long id;
    private String title;
    private String creator;
    private Integer releaseYear;
    private String genre;
    private String synopsis;
    private String mediaType;
}