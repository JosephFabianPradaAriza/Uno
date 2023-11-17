/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;

/**
 *
 * @author Joseph F
 */
public class BarajaUno {

    private CartaUno[] cartas;
    private int cartasEnBaraja;

    public BarajaUno() {
        cartas = new CartaUno[108];
        reiniciar();
    }

    public void reiniciar() {
        inicializarCartas();
    }

    public void reemplazarBarajaCon(List<CartaUno> cartas) {
        this.cartas = cartas.toArray(new CartaUno[cartas.size()]);
        this.cartasEnBaraja = this.cartas.length;
    }

    public boolean estaVacia() {
        return cartasEnBaraja == 0;
    }

    public void mezclar() {
        try {
            List<CartaUno> listaCartas;
            if (cartas != null) {
                listaCartas = Arrays.stream(cartas)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toCollection(ArrayList::new));
            } else {
                listaCartas = new ArrayList<>();
            }
            Collections.shuffle(listaCartas);
            cartas = listaCartas.toArray(new CartaUno[listaCartas.size()]);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Se produjo una NullPointerException en el método mezclar: " + e.getMessage());
        }
    }

    public CartaUno tomarCarta() throws BarajaVaciaException {
        if (estaVacia()) {
            throw new BarajaVaciaException("No se puede tomar cartas porque no hay cartas en la baraja");
        }
        return cartas[--cartasEnBaraja];
    }

    public ImageIcon tomarImagenCarta() throws BarajaVaciaException {
        if (estaVacia()) {
            throw new BarajaVaciaException("No se pueden tomar cartas, la baraja está vacía");
        }
        return new ImageIcon(cartas[--cartasEnBaraja].toString() + ".png");
    }

    public CartaUno[] tomarCartas(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException("Se deberían tomar números positivos, pero se intentó "
                    + "tomar " + n + " cartas");
        }

        if (n > cartasEnBaraja) {
            throw new IllegalArgumentException("No se pueden tomar " + n + " cartas, "
                    + "porque solo hay " + cartasEnBaraja + " cartas.");
        }

        CartaUno[] ret = new CartaUno[n];

        for (int i = 0; i < n; i++) {
            ret[i] = cartas[--cartasEnBaraja];
        }
        return ret;
    }

    private void inicializarCartas() {
        CartaUno.Color[] colores = CartaUno.Color.values();
        cartasEnBaraja = 0;

        for (int i = 0; i < colores.length; i++) {
            CartaUno.Color color = colores[i];

            cartas[cartasEnBaraja++] = new CartaUno(color, CartaUno.Valor.obtenerValor(0));

            for (int j = 0; j < 2; j++) {
                cartas[cartasEnBaraja++] = new CartaUno(color, CartaUno.Valor.obtenerValor(j));
            }

            CartaUno.Valor[] valores = new CartaUno.Valor[]{CartaUno.Valor.DrawDos, CartaUno.Valor.Saltar,
                CartaUno.Valor.Reversa};
            for (CartaUno.Valor valor : valores) {
                cartas[cartasEnBaraja++] = new CartaUno(color, valor);
                cartas[cartasEnBaraja++] = new CartaUno(color, valor);
            }
        }

        CartaUno.Valor[] valores = new CartaUno.Valor[]{CartaUno.Valor.Wild, CartaUno.Valor.WildCuatro};
        for (CartaUno.Valor valor : valores) {
            for (int i = 0; i < 4; i++) {
                cartas[cartasEnBaraja++] = new CartaUno(CartaUno.Color.Wild, valor);
            }
        }
    }

    public class BarajaVaciaException extends Exception {

        public BarajaVaciaException(String message) {
            super(message);
        }
    }
    
    public int obtenerTamañoBaraja() {
    return cartasEnBaraja;
}
}