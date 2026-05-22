package br.edu.atitus.currencyservice.dtos;

import java.util.List;

/**
 * DTO que representa a resposta da API PTAX do Banco Central do Brasil.
 */
public record BCBCurrencyDTO(List<BCBCotacaoDTO> value) {

    public record BCBCotacaoDTO(
            Double cotacaoCompra,
            Double cotacaoVenda,
            String dataHoraCotacao
    ) {}
}