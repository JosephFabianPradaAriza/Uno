/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uno;

/**
 *
 * @author Joseph F
 */
public class CartaUno {
    enum Color {
        Rojo, Azul, Verde, Amarillo, Wild;

        private static final Color[] colores = Color.values();

        public static Color obtenerColor(int i) {
            return Color.colores[i];
        }
    }

    enum Valor {
        Cero, Uno, Dos, Tres, Cuatro, Cinco, Seis, Siete, Ocho, Nueve, DrawDos, Saltar, Reversa, 
        Wild, WildCuatro;

        private static final Valor[] valores = Valor.values();

        public static Valor obtenerValor(int i) {
            return Valor.valores[i];
        }
    }

    private final Color color;
    private final Valor valor;

    public CartaUno(Color color, Valor valor) {
        this.color = color;
        this.valor = valor;
    }

    public Color obtenerColor() {
        return color;
    }

    public Valor obtenerValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "CartaUno{" + "color=" + color + ", valor=" + valor + '}';
    }
}