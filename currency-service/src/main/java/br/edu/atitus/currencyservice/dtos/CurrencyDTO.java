package br.edu.atitus.currencyservice.dtos;

public record CurrencyDTO(String SourceCurrency, String targetCurrency, Double conversionRate, String enviroment) {


}
