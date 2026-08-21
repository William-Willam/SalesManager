package br.com.salesmanager.desktop.http;

import javafx.scene.image.Image;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImagemCache {

    private static final Map<String, Image> cache = new ConcurrentHashMap<>();

    public static Image obter(String url, double largura, double altura) {
        return cache.computeIfAbsent(url, u -> new Image(u, largura, altura, true, true, true));
    }
}