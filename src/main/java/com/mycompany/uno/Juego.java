/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uno;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Joseph F
 */
public class Juego {

    private int jugadorActual;
    private String[] idsJugadores;

    private BarajaUno baraja;
    private ArrayList<ArrayList<CartaUno>> manoJugador;

    private ArrayList<CartaUno> pilaDescarte;
    private CartaUno.Color colorValido;
    private CartaUno.Valor valorValido;

    boolean direccionJuego;

    public Juego(String[] idsJugadores) {
        baraja = new BarajaUno();
        baraja.mezclar();
        pilaDescarte = new ArrayList<CartaUno>();

        this.idsJugadores = idsJugadores;
        jugadorActual = 0;

        manoJugador = new ArrayList<ArrayList<CartaUno>>();

        baraja.reiniciar(); // Asegúrate de que la baraja se haya reiniciado
        baraja.mezclar();

        int n = 7; // o cualquier otra cantidad
        if (n > baraja.obtenerTamañoBaraja()) {
            // Maneja el error, por ejemplo, reinicia la baraja
            baraja.reiniciar();
            baraja.mezclar();
        }

        for (int i = 0; i < idsJugadores.length; i++) {
            int numCartasATomar = 7;

            // Verificar si hay suficientes cartas antes de intentar tomar 7
            if (baraja.obtenerTamañoBaraja() < numCartasATomar) {
                // Manejar la situación de que no hay suficientes cartas
                System.err.println("No hay suficientes cartas en la baraja.");
                // Puedes agregar más lógica o lanzar una excepción según tus necesidades.
            } else {
                ArrayList<CartaUno> mano = new ArrayList<CartaUno>(Arrays.asList
        (baraja.tomarCartas(numCartasATomar)));
                manoJugador.add(mano);
            }
        }
    }

    public void iniciar(Juego juego) throws BarajaUno.BarajaVaciaException {
        CartaUno carta = baraja.tomarCarta();
        colorValido = carta.obtenerColor();
        valorValido = carta.obtenerValor();

        if (carta.obtenerValor() == CartaUno.Valor.Wild) {
            iniciar(juego);
        }

        if (carta.obtenerValor() == CartaUno.Valor.WildCuatro || carta.obtenerValor() ==
                CartaUno.Valor.DrawDos) {
            iniciar(juego);
        }

        if (carta.obtenerValor() == CartaUno.Valor.Saltar) {
            JLabel mensaje = new JLabel(idsJugadores[jugadorActual] + " fue saltado.");
            mensaje.setFont(new Font("Arial", Font.BOLD, 18));
            JOptionPane.showMessageDialog(null, mensaje);

            if (direccionJuego == false) {
                jugadorActual = (jugadorActual + 1) % idsJugadores.length;
            } else if (direccionJuego == true) {
                jugadorActual = (jugadorActual - 1) % idsJugadores.length;
                if (jugadorActual == -1) {
                    jugadorActual = idsJugadores.length - 1;
                }
            }
        }

        if (carta.obtenerValor() == CartaUno.Valor.Reversa) {
            JLabel mensaje = new JLabel(idsJugadores[jugadorActual] + " Cambió la dirección del juego.");
            mensaje.setFont(new Font("Arial", Font.BOLD, 18));
            JOptionPane.showMessageDialog(null, mensaje);
            direccionJuego ^= true;
            jugadorActual = idsJugadores.length - 1;
        }

        pilaDescarte.add(carta);
    }

    public CartaUno obtenerCartaSuperior() {
        return new CartaUno(colorValido, valorValido);
    }

    public ImageIcon obtenerImagenCartaSuperior() {
        return new ImageIcon(colorValido + "_" + valorValido + ".png");
    }

    public boolean esFinDelJuego() {
        for (String jugador : this.idsJugadores) {
            if (tieneManoVacia(jugador)) {
                return true;
            }
        }
        return false;
    }

    public String obtenerJugadorActual() {
        return this.idsJugadores[this.jugadorActual];
    }

    public String obtenerJugadorAnterior(int i) {
        int indice = this.jugadorActual - i;
        if (indice == 1) {
            indice = idsJugadores.length - 1;
        }
        return this.idsJugadores[indice];
    }

    public String[] obtenerJugadores() {
        return idsJugadores;
    }

    public ArrayList<CartaUno> obtenerManoJugador(String idJugador) {
        int indice = Arrays.asList(idsJugadores).indexOf(idJugador);
        return manoJugador.get(indice);
    }

    public int obtenerTamañoManoJugador(String idJugador) {
        return obtenerManoJugador(idJugador).size();
    }

    public CartaUno obtenerCartaJugador(String idJugador, int eleccion) {
        ArrayList<CartaUno> mano = obtenerManoJugador(idJugador);
        return mano.get(eleccion);
    }

    public boolean tieneManoVacia(String idJugador) {
        return obtenerManoJugador(idJugador).isEmpty();
    }

    public boolean esJugadaValida(CartaUno carta) {
        return carta.obtenerColor() == colorValido || carta.obtenerValor() == valorValido;
    }

    public void verificarTurnoJugador(String idJugador) throws JugadorTurnoInvalidoException {
        if (!this.idsJugadores[this.jugadorActual].equals(idJugador)) {
            throw new JugadorTurnoInvalidoException("No es el turno de ", idJugador);
        }
    }

    public void realizarRobo(String idJugador) throws JugadorTurnoInvalidoException, 
            BarajaUno.BarajaVaciaException {
        verificarTurnoJugador(idJugador);

        if (baraja.estaVacia()) {
            baraja.reemplazarBarajaCon(pilaDescarte);
            baraja.mezclar();
        }

        obtenerManoJugador(idJugador).add(baraja.tomarCarta());
        if (direccionJuego == false) {
            jugadorActual = (jugadorActual + 1) % idsJugadores.length;
        } else if (direccionJuego == true) {
            jugadorActual = (jugadorActual - 1) % idsJugadores.length;
            if (jugadorActual == -1) {
                jugadorActual = idsJugadores.length - 1;
            }
        }
    }

    public void establecerColorCarta(CartaUno.Color color) {
        colorValido = color;
    }

    public void realizarJugadaJugador(String idJugador, CartaUno carta, CartaUno.Color colorDeclarado)
            throws JugadorTurnoInvalidoException, ColorInvalidoException, ValorInvalidoException, 
            BarajaUno.BarajaVaciaException {
        verificarTurnoJugador(idJugador);

        ArrayList<CartaUno> manoJugador = obtenerManoJugador(idJugador);

        if (!esJugadaValida(carta)) {
            if (carta.obtenerColor() == CartaUno.Color.Wild) {
                colorValido = carta.obtenerColor();
                valorValido = carta.obtenerValor();
            }

            if (carta.obtenerColor() != colorValido) {
                JLabel mensaje = new JLabel("Movimiento no válido, se esperaba: " + colorValido + 
                        " pero se obtuvo " + carta.obtenerColor());
                mensaje.setFont(new Font("Arial", Font.BOLD, 18));
                JOptionPane.showMessageDialog(null, mensaje);
                throw new ColorInvalidoException(mensaje.toString(), carta.obtenerColor(), 
                        colorValido);
            }

            if (carta.obtenerValor() != valorValido) {
                JLabel mensaje2 = new JLabel("Movimiento no válido, se esperaba: "
                        + valorValido + " pero se obtuvo " + carta.obtenerValor());
                mensaje2.setFont(new Font("Arial", Font.BOLD, 18));
                JOptionPane.showMessageDialog(null, mensaje2);
                throw new ValorInvalidoException(mensaje2.toString(), 
                        carta.obtenerValor(), valorValido);
            }
        }

        manoJugador.remove(carta);

        if (tieneManoVacia(this.idsJugadores[jugadorActual])) {
            JLabel mensaje2 = new JLabel(this.idsJugadores[jugadorActual] + " ha ganado el juego!");
            mensaje2.setFont(new Font("Arial", Font.BOLD, 18));
            JOptionPane.showMessageDialog(null, mensaje2);
            System.exit(0);
        }

        colorValido = carta.obtenerColor();
        valorValido = carta.obtenerValor();
        pilaDescarte.add(carta);

        if (direccionJuego == false) {
            jugadorActual = (jugadorActual + 1) % idsJugadores.length;
        } else if (direccionJuego == true) {
            jugadorActual = (jugadorActual - 1) % idsJugadores.length;
            if (jugadorActual == -1) {
                jugadorActual = idsJugadores.length - 1;
            }
        }

        if (carta.obtenerColor() == CartaUno.Color.Wild) {
            colorValido = colorDeclarado;
        }

        if (carta.obtenerValor() == CartaUno.Valor.DrawDos) {
            idJugador = idsJugadores[jugadorActual];
            obtenerManoJugador(idJugador).add(baraja.tomarCarta());
            obtenerManoJugador(idJugador).add(baraja.tomarCarta());
            JLabel mensaje = new JLabel(idJugador + " tomó 2 cartas.");
        }

        if (carta.obtenerValor() == CartaUno.Valor.WildCuatro) {
            idJugador = idsJugadores[jugadorActual];
            obtenerManoJugador(idJugador).add(baraja.tomarCarta());
            obtenerManoJugador(idJugador).add(baraja.tomarCarta());
            obtenerManoJugador(idJugador).add(baraja.tomarCarta());
            obtenerManoJugador(idJugador).add(baraja.tomarCarta());
            JLabel mensaje = new JLabel(idJugador + " tomó 4 cartas.");
        }

        if (carta.obtenerValor() == CartaUno.Valor.Saltar) {
            JLabel mensaje = new JLabel(this.idsJugadores[jugadorActual] + " fue saltado.");
            mensaje.setFont(new Font("Arial", Font.BOLD, 18));
            JOptionPane.showMessageDialog(null, mensaje);
            if (direccionJuego == false) {
                jugadorActual = (jugadorActual + 1) % idsJugadores.length;
            } else if (direccionJuego == true) {
                jugadorActual = (jugadorActual - 1) % idsJugadores.length;
                if (jugadorActual == -1) {
                    jugadorActual = idsJugadores.length - 1;
                }
            }
        }

        if (carta.obtenerValor() == CartaUno.Valor.Reversa) {
            JLabel mensaje = new JLabel(idJugador + " cambió la dirección del juego.");
            mensaje.setFont(new Font("Arial", Font.BOLD, 18));
            JOptionPane.showMessageDialog(null, mensaje);

            direccionJuego ^= true;
            if (direccionJuego == true) {
                jugadorActual = (jugadorActual - 2) % idsJugadores.length;
                if (jugadorActual == -1) {
                    jugadorActual = idsJugadores.length - 1;
                }

                if (jugadorActual == -2) {
                    jugadorActual = idsJugadores.length - 2;
                }
            } else if (direccionJuego == false) {
                jugadorActual = (jugadorActual + 2) % idsJugadores.length;
            }
        }
    }

    String getImagenCartaArriba() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

class JugadorTurnoInvalidoException extends Exception {

    String idJugador;

    public JugadorTurnoInvalidoException(String mensaje, String idJugador) {
        super(mensaje);
        this.idJugador = idJugador;
    }

    public String getIdJugador() {
        return idJugador;
    }
}

class ColorInvalidoException extends Exception {

    private CartaUno.Color esperado;
    private CartaUno.Color actual;

    public ColorInvalidoException(String mensaje, CartaUno.Color actual, CartaUno.Color esperado) {
        super(mensaje);
        this.actual = actual;
        this.esperado = esperado;
    }

    public CartaUno.Color getActual() {
        return actual;
    }

    public CartaUno.Color getEsperado() {
        return esperado;
    }
}

class ValorInvalidoException extends Exception {

    private CartaUno.Valor esperado;
    private CartaUno.Valor actual;

    public ValorInvalidoException(String mensaje, CartaUno.Valor actual, CartaUno.Valor esperado) {
        super(mensaje);
        this.actual = actual;
        this.esperado = esperado;
    }

    public CartaUno.Valor getActual() {
        return actual;
    }

    public CartaUno.Valor getEsperado() {
        return esperado;
    }
}
