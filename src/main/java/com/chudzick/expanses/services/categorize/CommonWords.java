package com.chudzick.expanses.services.categorize;

public enum CommonWords {
    RACHUNEK("rachunek"), ODBIORCY("odbiorcy"), NAZWA("nazwa"), REFERENCJE("referencje"), WLASNE("własne"),
    ZLECENIODAWCY("zleceniodawcy"), PRZELEW("przelew"), TELEFON("telefon"), KWOTA("kwota"), OPERACJI("operacji"),
    ORYGINALNA("oryginalna"),TELEFONU("telefonu:"),ADRES("adres:"),KARTY("karty:"),LOKALIZACJA("lokalizacja:"),TYTYL("tytuł:"),
    ODBIORCY2("odbiorcy:"),OGLOSZENIA("ogłoszenia:"),NUMER("numer"),NUMER2("numer"),KRAJ("kraj:"),ZLECENIODAWCY2("zleceniodawcy:"),
    POLSKA("polska"),MIASTO("miasto:"),NADAWCY("nadawcy:"),ADRES2("adres"),DATA("data"),CZAS("czas"),KRAKOW("kraków"),KRAKOW2("krakow");

    private String word;

    CommonWords(String word) {
        this.word = word;
    }

    public String getWord() {
        return word.toLowerCase();
    }
}
